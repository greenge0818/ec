package com.prcsteel.ec.service.impl;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.prcsteel.ec.core.enums.*;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.*;
import com.prcsteel.ec.core.service.CacheService;
import com.prcsteel.ec.core.util.DateUtil;
import com.prcsteel.ec.model.domain.ec.*;
import com.prcsteel.ec.model.dto.CartDto;
import com.prcsteel.ec.model.dto.ConsignTabsDto;
import com.prcsteel.ec.model.dto.RequirementDto;
import com.prcsteel.ec.model.dto.RequirementItemDto;
import com.prcsteel.ec.model.query.ConsignOrderQuery;
import com.prcsteel.ec.persist.dao.ec.*;
import com.prcsteel.ec.service.*;
import com.prcsteel.ec.service.api.RestCbmsService;
import com.prcsteel.ec.service.api.RestMarketCenterService;
import com.prcsteel.ec.service.api.RestPickService;
import com.prcsteel.ec.service.api.RestSmartService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("memberService")
public class MemberServiceImpl implements MemberService {
    @Resource
    private RequirementStatusChangeRecordDao requirementStatusChangeRecordDao;
    @Resource
    private RequirementService requirementService;
    @Resource
    private GlobalIdService globalIdService;
    @Resource
    private CartDao cartDao;
    @Resource
    private CartService cartService;
    @Resource
    private CacheService cacheService;
    @Resource
    private CommonService commonService;

    @Resource
    private UserService userService;

    @Resource
    private RestSmartService restSmartService;

    @Resource
    private RestPickService restPickService;

    @Resource
    private RestMarketCenterService restMarketCenterService;

    @Resource
    private RestCbmsService restCbmsService;

    /**
     * 分页大小
     */
    private static final Integer _pageSize = Constant.PAGE_SIZE;

    private static final Gson gson = new Gson();

    /**
     * 获取待办事项列表
     *
     * @param maxId     行情中心最大id
     * @param lastTime  最后时间,获取该时间之前的数据
     * @param pageIndex 页码
     * @return
     */
    @Override
    public List<RequirementDto> getTodoList(Integer maxId, Long lastTime, Integer pageIndex) {
        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        Date reqLastTime = new Date(lastTime);
        Integer quoMaxId = maxId;
        List<RequirementDto> todoList = new LinkedList<>();
        Integer requirementLength = _pageSize, quotationLength = _pageSize;
        if (pageIndex > 0) {    //先从缓存获取
            List<RequirementDto> dtos = (List<RequirementDto>) cacheService.get(Constant.CACHE_TODO_LIST + user.getGuid() + "_" + (pageIndex - 1));
            if (dtos != null && !dtos.isEmpty()) {
                todoList.addAll(dtos);
                //计算要从行情中心和其他系统获取的记录数
                List<RequirementDto> quoList = dtos.stream().filter(a -> RequirementType.MARKETANALYSIS.getCode().equals(a.getType())).collect(Collectors.toList());
                List<RequirementDto> reqList = dtos.stream().filter(a -> !RequirementType.MARKETANALYSIS.getCode().equals(a.getType())).collect(Collectors.toList());
                quotationLength = quotationLength - quoList.size();
                requirementLength = requirementLength - reqList.size();
                if (!quoList.isEmpty()) {
                    quoMaxId = quoList.get(quoList.size() - 1).getId();
                }
                if (!reqList.isEmpty()) {
                    reqLastTime = reqList.get(reqList.size() - 1).getCreated();
                }
            }
        }
        //查询需求单变化记录表，获取该页所需要的数据来源
        List<RequirementStatusChangeRecord> statusChangeRecords = requirementStatusChangeRecordDao.selectByLastTimeAndUserGuidLimit(user.getGuid(), reqLastTime, requirementLength);
        if (!statusChangeRecords.isEmpty()) {
            //按数据来源来分数据
            Map<String, List<RequirementStatusChangeRecord>> groupBySourceRecordsMap =
                    statusChangeRecords.stream().collect(Collectors.groupingBy(RequirementStatusChangeRecord::getSource, Collectors.toList()));
            //按key去不同系统取数据
            for (String key : groupBySourceRecordsMap.keySet()) {
                todoList.addAll(getRequirement(key, groupBySourceRecordsMap.get(key)));
            }
        }
        //获取行情中心
        if (quotationLength > 0) {
            String mcResult;
            try {
                mcResult = restMarketCenterService.getNewsArticles(quoMaxId, quotationLength);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.MC_SERVER_ERROR.getCode(), MessageTemplate.MC_SERVER_ERROR.getMsg());
            }
            try {
                RestResult restResult = gson.fromJson(mcResult, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(restResult.getStatus())) {

                    Gson newGson = new GsonBuilder().setDateFormat(Constant.DATE_TIME_FORMAT).create();

                    List<RequirementDto> newsArticles = newGson.fromJson(newGson.toJson(restResult.getData()), new TypeToken<List<RequirementDto>>() {
                    }.getType());

                    if (newsArticles != null && !newsArticles.isEmpty()) {
                        newsArticles.stream().forEach(a -> a.setType(RequirementType.MARKETANALYSIS.getCode()));
                        todoList.addAll(newsArticles);
                    }
                }
            } catch (JsonParseException jpe) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        }

        //排序(按创建时间从大到小倒序排)
        todoList.sort((a, b) -> b.getCreated().compareTo(a.getCreated()));
        //截取pageIndex长度
        if (todoList.size() > _pageSize) {
            //做缓存
            cacheService.set(Constant.CACHE_TODO_LIST + user.getGuid() + "_" + pageIndex, Constant.MEMCACHE_TODO_LIST_TIMEOUT,
                    new ArrayList<>(todoList.subList(_pageSize, todoList.size())));
            return todoList.subList(0, _pageSize);
        }
        //返回
        return todoList;
    }

    /**
     * 获取需求单详情
     *
     * @param source  数据来源
     * @param records 状态变化表数据
     * @return
     */
    @Override
    public List<RequirementDto> getRequirement(String source, List<RequirementStatusChangeRecord> records) {
        List<RequirementDto> requirements = new LinkedList<>();
        //取出需求数据的codes
        String codes = StringUtils.join(records.stream().map(RequirementStatusChangeRecord::getRemoteOrderCode).distinct().toArray(String[]::new), ",");
        String innerCodes = StringUtils.join(records.stream().map(RequirementStatusChangeRecord::getRequirementCode).distinct().toArray(String[]::new), ",");
        List<RequirementDto> requirementInfo = requirementService.selectRequirementsByCodes(innerCodes);
        //source为app或者web的，数据都在本系统requirement表
        if (RemoteDataSource.WEB.getCode().equals(source) || RemoteDataSource.APP.getCode().equals(source)) {
            records.stream().forEach(a ->
                    requirementInfo.stream().forEach(b -> {
                        if (b.getCode().equals(a.getRequirementCode())) {
                            RequirementDto dto = b.clone();
                            dto.setStageStatus(a.getChangeToStatus());
                            dto.setCreated(a.getRemoteOrderCreated());   //时间取状态变化时间
                            requirements.add(dto);
                        }
                    })
            );
        } else if (RemoteDataSource.PICK.getCode().equals(source)) {
            //从分拣系统取数据
            String pickResult;
            try {
                pickResult = restPickService.getInquiryItems(codes);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.PICK_SERVER_ERROR.getCode(), MessageTemplate.PICK_SERVER_ERROR.getMsg());
            }
            try {
                RestResult data = gson.fromJson(pickResult, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(data.getStatus())) {
                    Gson newGson = new GsonBuilder().setDateFormat(Constant.DATE_TIME_FORMAT).create();
                    List<RequirementDto> requirementDtos = newGson.fromJson(newGson.toJson(data.getData()), new TypeToken<List<RequirementDto>>() {
                    }.getType());
                    requirements.addAll(buildData(records, requirementInfo, requirementDtos));
                }
            } catch (JsonParseException jpe) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        } else if (RemoteDataSource.SMART.getCode().equals(source)) {
            //从智能找货取数据
            String smResult;
            try {
                smResult = restSmartService.getQuotationItems(codes);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.SM_SERVER_ERROR.getCode(), MessageTemplate.SM_SERVER_ERROR.getMsg());
            }
            try {
                RestResult data = gson.fromJson(smResult, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(data.getStatus()) && data.getData() != null) {
                    requirements.addAll(buildData(records, requirementInfo, getFromRemoteServer(data)));
                }
            } catch (JsonParseException jpe) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        } else if (RemoteDataSource.CBMS.getCode().equals(source)) {
            //从CBMS取数据
            String cbmsResult;
            try {
                cbmsResult = restCbmsService.getOrdersByIds(codes);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.CBMS_SERVER_ERROR.getCode(), MessageTemplate.CBMS_SERVER_ERROR.getMsg());
            }
            try {
                CbmsResult data = gson.fromJson(cbmsResult, CbmsResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(data.getStatus())) {
                    Gson newGson = new GsonBuilder().setDateFormat(Constant.DATE_TIME_FORMAT).create();
                    List<RequirementDto> requirementDtos = newGson.fromJson(newGson.toJson(data.getData()), new TypeToken<List<RequirementDto>>() {
                    }.getType());
                    requirements.addAll(buildData(records, requirementInfo, requirementDtos));
                }
            } catch (JsonParseException jpe) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        }
        return requirements;
    }

    /**
     * 组装数据
     *
     * @param records
     * @param requirementDtos
     * @return
     */
    private List<RequirementDto> buildData(List<RequirementStatusChangeRecord> records, List<RequirementDto> requirementInfo, List<RequirementDto> requirementDtos) {
        if (requirementDtos == null) {
            return new ArrayList<>();
        }
        List<RequirementDto> requirements = new LinkedList<>();
        records.stream().forEach(a ->
                requirementDtos.stream().forEach(b -> {
                    if (a.getRemoteOrderCode().equals(b.getRemoteCode())) {
                        RequirementDto dto = b.clone();
                        dto.setStageStatus(a.getChangeToStatus());
                        dto.setCode(a.getRequirementCode());
                        dto.setCreated(a.getRemoteOrderCreated());
                        requirementInfo.stream().forEach(c -> {   //遍历需求单，有些值只在需求单中有存
                            if (c.getCode().equals(a.getRequirementCode())) {
                                dto.setSource(c.getSource());  //设置来源
                                dto.setType(c.getType());      //设置类型
                                if (RequirementStatus.CLOSED.getCode().equals(dto.getStageStatus())) {
                                    dto.setCloseStage(fetchCloseStage(a.getSource()));   //设置关闭阶段和关闭理由
                                    dto.setCloseReason(c.getCloseReason());
                                }
                            }
                        });
                        requirements.add(dto);
                    }
                })
        );
        return requirements;
    }

    /**
     * 设置关闭阶段
     * @param source 来源
     * @return
     */
    private String fetchCloseStage(String source) {
        if (RemoteDataSource.PICK.getCode().equals(source)) {
            return RequirementCloseStage.INQUIRY.getCode();
        } else if (RemoteDataSource.SMART.getCode().equals(source)) {
            return RequirementCloseStage.QUOTED.getCode();
        } else if (RemoteDataSource.CBMS.getCode().equals(source)) {
            return RequirementCloseStage.BILL.getCode();
        }
        return null;
    }

    /**
     * 处理外部系统数据(找货，CBMS)
     *
     * @param data
     * @return
     */
    private List<RequirementDto> getFromRemoteServer(RestResult data) {
        List<RequirementDto> requirementDtos;
        try {
            Gson newGson = new GsonBuilder().setDateFormat(Constant.DATE_TIME_FORMAT).create();
            requirementDtos = newGson.fromJson(newGson.toJson(data.getData()), new TypeToken<List<RequirementDto>>() {
            }.getType());
        } catch (JsonParseException jpe) {
            throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
        }
        requirementDtos.stream().forEach(a ->
                a.getItems().stream().forEach(b -> {
                    if (b.getAmount() != null) {
                        if (a.getAmount() == null) {
                            a.setAmount(BigDecimal.ZERO);
                        }
                        a.setAmount(a.getAmount().add(b.getAmount()));
                    }
                })
        );
        return requirementDtos;
    }

    /**
     * 再来一单，资源加入购物车
     *
     * @param cartList 购物车列表
     * @return 购物车资源数量
     * @author peanut
     * @date 2016/05/09
     */
    @Override
    public int onceMoreOrder(List<Cart> cartList) {
        if (cartList == null || cartList.isEmpty()) {
            throw new BusinessException(MessageTemplate.CART_RESOURCE_EMPTY.getCode(), MessageTemplate.CART_RESOURCE_EMPTY.getMsg());
        }

        User user = commonService.getCurrentUser();
        if (user != null) {
            //用户其他信息
            cartList.stream().forEach(e -> {
                e.preInsert(globalIdService.getId());
                e.setChecked("Y");
                e.setCreatedBy(user.getGuid());
                e.setLastUpdatedBy(user.getGuid());
                e.setUserGuid(user.getGuid());
                e.setSellerId(Constant.DEFAULT_SELLER_ID);
                e.setSellerName(Constant.DEFAULT_SELLER_NAME);
                e.setAmount(e.getPrice().multiply(e.getWeight()).setScale(Constant.MONEY_DIGIT, BigDecimal.ROUND_HALF_UP));
            });

            //批量添加购物车
            if (cartDao.batchInsert(cartList) <= 0) {
                throw new BusinessException(MessageTemplate.CART_ADD_ERROR.getCode(), MessageTemplate.CART_ADD_ERROR.getMsg());
            }

            //合并购物车
            cartService.updateCartUserGuidByCookieId(null);

            //用户不同公司的购物车资源总数量
            List<CartDto> list = cartService.selectByUserGuidAndCookieId(null);
            if (list != null) {
                return list.stream().mapToInt(e -> e.getResourceList().size()).sum();
            }
        }
        return 0;
    }

    /**
     * 获取采购单数据
     * ESB
     */
    @Override
    public List<ConsignOrderDto> getConsignInfo(ConsignOrderQuery query) {
        List<ConsignOrderDto> consignOrderDtos = new LinkedList<>();
        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        query.setId(user.getId().toString());
        query.preQuery();
        String cbmsResult;
        try {
            cbmsResult = restCbmsService.getOrders(query.getId(), query.getStatus(), query.getTimeFrom(), query.getTimeTo(),
                    query.getKeyWord(), query.getPageIndex(), query.getPageSize());
        } catch (Exception e) {
            throw new BusinessException(MessageTemplate.CBMS_SERVER_ERROR.getCode(), MessageTemplate.CBMS_SERVER_ERROR.getMsg());
        }
        try {
            CbmsResult data = gson.fromJson(cbmsResult, CbmsResult.class);
            if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(data.getStatus())) {
                Gson newGson = new GsonBuilder().setDateFormat(Constant.DATE_TIME_FORMAT).create();
                CbmsConsignResult cbmsConsignResult = newGson.fromJson(newGson.toJson(data.getData()), CbmsConsignResult.class);
                if (cbmsConsignResult.getTotal() > 0) {
                    consignOrderDtos = newGson.fromJson(newGson.toJson(cbmsConsignResult.getList()), new TypeToken<List<ConsignOrderDto>>() {
                    }.getType());
                    consignOrderDtos.stream().forEach(a -> a.setAccountName(Constant.DEFAULT_SELLER_NAME)); //卖家默认为钢为
                }
                //加上总数统计
                ConsignOrderDto consignOrderDto = new ConsignOrderDto();
                consignOrderDto.setTotal(cbmsConsignResult.getTotal());
                consignOrderDtos.add(consignOrderDto);
            } else {
                //加上总数统计
                ConsignOrderDto consignOrderDto = new ConsignOrderDto();
                consignOrderDto.setTotal(0);
                consignOrderDtos.add(consignOrderDto);
            }
        } catch (JsonParseException jpe) {
            throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
        }
        return consignOrderDtos;
    }

    /**
     * 获取采购单tab信息（状态枚举和数量角标）
     */
    @Override
    public List<ConsignTabsDto> getConsignTabs() {
        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        List<ConsignTabsDto> re = new LinkedList<>();
        String cbmsResult;
        try {
            cbmsResult = restCbmsService.getOrderCount(user.getId().toString());
        } catch (Exception e) {
            throw new BusinessException(MessageTemplate.CBMS_SERVER_ERROR.getCode(), MessageTemplate.CBMS_SERVER_ERROR.getMsg());
        }
        TabCountResult tabCountResult = new TabCountResult();
        try {
            CbmsResult data = gson.fromJson(cbmsResult, CbmsResult.class);
            if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(data.getStatus())) {
                tabCountResult = gson.fromJson(gson.toJson(data.getData()), TabCountResult.class);
            }
        } catch (JsonParseException jpe) {
            throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
        }
        for (ConsignOrderStatus status : ConsignOrderStatus.values()) {
            ConsignTabsDto tab = new ConsignTabsDto();
            tab.setCode(status.getCode());
            tab.setMsg(status.getMsg());
            if (ConsignOrderStatus.ALL.getCode().equals(status.getCode())) {
                tab.setCount(tabCountResult.getALL());
            } else if (ConsignOrderStatus.CLOSED.getCode().equals(status.getCode())) {
                tab.setCount(tabCountResult.getCLOSED());
            } else if (ConsignOrderStatus.TO_BE_RELATED.getCode().equals(status.getCode())) {
                tab.setCount(tabCountResult.getRELATED());
            } else if (ConsignOrderStatus.TO_BE_SECONDARY.getCode().equals(status.getCode())) {
                tab.setCount(tabCountResult.getSECONDSETTLE());
            } else if (ConsignOrderStatus.FINISHED.getCode().equals(status.getCode())) {
                tab.setCount(tabCountResult.getFINISH());
            } else {
                tab.setCount(0);
            }
            re.add(tab);
        }
        return re;
    }

    /**
     * 获取某个时间点之后的再来一单列表(APP)
     *
     * @param token
     * @param lastDateTime
     * @return
     */
    @Override
    public List<APPOnceMore> getAppOncemorelist(String lastDateTime, String token) {
        //再来一单列表
        List<APPOnceMore> list = new ArrayList<>();
        List<RequirementDto> todoList = getTodoListAPP(lastDateTime, token, true);
        //数据转换
        for (RequirementDto requirementDto : todoList) {
            for (RequirementItemDto requirementItemDto : requirementDto.getItems()) {
                APPOnceMore onceMore = new APPOnceMore(requirementItemDto);
                list.add(onceMore);
            }
        }
        //去重
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获取待办事项列表（APP）
     *
     * @param token      登录凭证
     * @param lastTime   最后时间,获取该时间之后的所有数据
     * @param isOnceMore 是否是再来一单
     */
    @Override
    public List<RequirementDto> getTodoListAPP(String lastTime, String token, Boolean isOnceMore) {
        //时间格式判断
        Date lastDateTimeD = null;
        if (StringUtils.isNotBlank(lastTime)) {
            lastDateTimeD = DateUtil.strToDate(lastTime, Constant.DATE_TIME_FORMAT);
            if (lastDateTimeD == null) {
                throw new BusinessException(MessageTemplate.TIME_ERROR.getCode(), MessageTemplate.TIME_ERROR.getMsg());
            }
        }

        User user = userService.checkUser(token);

        List<RequirementDto> todoList = new LinkedList<>();
        //获取过程
        //查询需求单变化记录表，获取该页所需要的数据来源
        List<RequirementStatusChangeRecord> statusChangeRecords = requirementStatusChangeRecordDao.selectByLastTimeAndUserGuidForAPP(user.getGuid(), lastDateTimeD, isOnceMore);
        if (!statusChangeRecords.isEmpty()) {
            //按数据来源来分数据
            Map<String, List<RequirementStatusChangeRecord>> groupBySourceRecordsMap =
                    statusChangeRecords.stream().collect(Collectors.groupingBy(RequirementStatusChangeRecord::getSource, Collectors.toList()));
            //按key去不同系统取数据
            for (String key : groupBySourceRecordsMap.keySet()) {
                todoList.addAll(getRequirement(key, groupBySourceRecordsMap.get(key)));
            }
        }
        //排序(按创建时间从大到小倒序排)
        todoList.sort((a, b) -> b.getCreated().compareTo(a.getCreated()));
        setAppTodoListType(todoList);
        return todoList;
    }

    /**
     * 设置传递给app的类型
     *
     * @param list
     */
    private void setAppTodoListType(List<RequirementDto> list) {
        if (list != null) {
            list.forEach(a -> {
                String status = a.getStageStatus();
                String type = a.getType();
                String source = a.getSource();
                //新开单
                if (RequirementStatus.NEW.getCode().equals(status)) {
                    //1.回执型
                    Boolean isReceipt = RequirementType.RECEIPT.getCode().equals(type);

                    if (isReceipt) {
                        a.setType("1");
                        return;
                    }
                    //2.图片型
                    Boolean isImageList = RequirementType.IMAGE.getCode().equals(type);
                    if (isImageList) {
                        a.setType("2");
                        return;
                    }
                    //3.文字+文件列表
                    Boolean isRequest = RemoteDataSource.WEB.getCode().equals(source) && RequirementType.HELP.getCode().equals(type);
                    if (isRequest) {
                        a.setType("3");
                        return;
                    }
                    //4.表单列表型
                    Boolean isItemList = a.getItems() != null && !a.getItems().isEmpty();
                    if (isItemList) {
                        a.setType("4");
                        return;
                    }
                } else {
                    //非新开单，从其他系统取到的数据

                    //非关闭类型
                    if (RequirementStatus.PICKED.getCode().equals(status)) {
                        //分拣完成-待报价
                        a.setType("5");
                    } else if (RequirementStatus.QUOTED.getCode().equals(status)) {
                        //报价完成
                        a.setType("6");
                    } else if (RequirementStatus.FINISHED.getCode().equals(status)) {
                        //付款完成
                        a.setType("7");
                    } else if (RequirementStatus.CLOSED.getCode().equals(status)) {
                        //已关闭
                        a.setType("8");
                    }
                }
            });
        }
    }
}
