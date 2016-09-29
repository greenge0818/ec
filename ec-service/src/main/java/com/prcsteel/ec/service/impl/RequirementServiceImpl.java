package com.prcsteel.ec.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.prcsteel.ec.core.enums.*;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.service.CacheService;
import com.prcsteel.ec.core.util.DateUtil;
import com.prcsteel.ec.core.util.StringUtil;
import com.prcsteel.ec.model.domain.ec.*;
import com.prcsteel.ec.model.dto.RequirementDto;
import com.prcsteel.ec.model.dto.RequirementForPickDto;
import com.prcsteel.ec.model.model.AddCbmsContact;
import com.prcsteel.ec.model.model.CreateRequirementFromPick;
import com.prcsteel.ec.model.model.Market2PickRequirement;
import com.prcsteel.ec.model.query.RequirementQuery;
import com.prcsteel.ec.model.query.RequirementStatusChangerQuery;
import com.prcsteel.ec.persist.dao.cas.CasUserDao;
import com.prcsteel.ec.persist.dao.ec.*;
import com.prcsteel.ec.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: RequirementServiceImpl
 * @Description: 需求单服务实现
 * @Author Green.Ge
 * @Date 2016年4月27日
 */
@Service("requirementService")
@Transactional
public class RequirementServiceImpl implements RequirementService {
    protected static final Logger logger = LoggerFactory.getLogger(RequirementServiceImpl.class);
    @Resource
    CacheService cacheService;

    @Resource
    private GlobalIdService globalIdService;

    @Resource
    private RequirementDao requirementDao;

    @Resource
    private RequirementStatusChangeRecordDao requirementStatusChangeRecordDao;

    @Resource
    private RequirementItemDao requirementItemDao;

    @Resource
    private GenericDaoService genericDaoService;

    @Resource
    private MemberService memberService;

    @Resource
    private CommonService commonService;

    @Resource
    private UserDao userDao;

    @Resource
    private CasUserDao casUserDao;

    @Resource
    private MarketService marketService;

    @Resource
    private UserService userService;

    @Resource
    private RequirementCodeDao requirementCodeDao;

    @Value("${amq.addCBMSContact}")
    private String addCBMSContactDes;

    @Value("${amq.channelName}")
    private String requirementDes;

    /**
     * @Author: Green.Ge
     * @Description: 获取需求单号
     * @Date: 2016年4月27日
     */
    @Override
    public String genCode() {
        Date date = new Date();
        String today = DateUtil.dateToStr(date, "yyyyMMdd");
        RequirementCode requirementCode = new RequirementCode();
        requirementCodeDao.insert(requirementCode);
        String code = String.format("%06d", requirementCode.getId().intValue());
        return today + "-" + code;
    }

    @Override
    public Integer getMaxCode(String date) {
        return requirementDao.getMaxCode(date);
    }

    /**
     * @Author: Rabbit
     * @Description: 提交app再来一单需求
     * @Date: 2016年6月2日
     */
    @Override
    public void submitAppOnceMore(String req, String token) {
        User user = userService.checkUser(token);  //当前用户
        List<APPOnceMore> onceMoreList;
        try {
            onceMoreList = new Gson().fromJson(req, new TypeToken<List<APPOnceMore>>() {
            }.getType());
        } catch (JsonSyntaxException jse) {
            throw new BusinessException(MessageTemplate.ONCE_MORE_JSON_PARSE_ERROR.getCode(), MessageTemplate.ONCE_MORE_JSON_PARSE_ERROR.getMsg());
        }
        //插入主表
        Requirement requirement = new Requirement();
        requirement.setSource(RemoteDataSource.APP.getCode());
        requirement.setType(RequirementType.ONEMORE.getCode());
        requirement.setStageStatus(RequirementStatus.NEW.getCode());

        //插入详情
        List<RequirementItem> requirementItems = new LinkedList<>();
        for (APPOnceMore appOnceMore : onceMoreList) {
            //非空判断
            if (StringUtils.isBlank(appOnceMore.getCategoryUuid())) {
                throw new BusinessException(MessageTemplate.CATEGORY_UUID_EMPTY.getCode(), MessageTemplate.CATEGORY_UUID_EMPTY.getMsg());
            }
            if (StringUtils.isBlank(appOnceMore.getMaterialUuid())) {
                throw new BusinessException(MessageTemplate.MATERIAL_UUID_EMPTY.getCode(), MessageTemplate.MATERIAL_UUID_EMPTY.getMsg());
            }
            if (appOnceMore.getFactoryId() != null && StringUtils.isBlank(appOnceMore.getFactoryId().toString())) {
                throw new BusinessException(MessageTemplate.FACTORY_ID_EMPTY.getCode(), MessageTemplate.FACTORY_ID_EMPTY.getMsg());
            }
            RequirementItem requirementItem = new RequirementItem(appOnceMore);
            requirementItem.preInsert(globalIdService.getId());
            requirementItem.setCreatedBy(user.getGuid());
            requirementItem.setLastUpdatedBy(user.getGuid());
            requirementItems.add(requirementItem);
            //设置品名，材质，钢厂名称
            requirementItem.setCategoryName(marketService.getCategoryName(appOnceMore.getCategoryUuid()));
            requirementItem.setMaterialName(marketService.getMaterialNames(appOnceMore.getMaterialUuid(), appOnceMore.getCategoryUuid()));
            requirementItem.setFactoryName(marketService.getFactoryNames(appOnceMore.getFactoryId().toString(), appOnceMore.getCategoryUuid()));
            //品名名称等非空判断
            if (StringUtils.isBlank(requirementItem.getCategoryName())) {
                throw new BusinessException(MessageTemplate.CATEGORY_UUID_ERROR.getCode(), MessageTemplate.CATEGORY_UUID_ERROR.getMsg());
            }
            if (StringUtils.isBlank(requirementItem.getMaterialName())) {
                throw new BusinessException(MessageTemplate.MATERIAL_UUID_ERROR.getCode(), MessageTemplate.MATERIAL_UUID_ERROR.getMsg());
            }
            if (StringUtils.isBlank(requirementItem.getFactoryName())) {
                throw new BusinessException(MessageTemplate.FACTORY_ID_ERROR.getCode(), MessageTemplate.FACTORY_ID_ERROR.getMsg());
            }
        }
        submitRequirment(requirement, token, requirementItems);
    }

    /**
     * @Author: Tiny
     * edit by Rabbit 增加token参数兼容app接口调用
     * @Description: 提交需求
     * @Date: 2016年4月28日
     */
    @Override
    @Transactional
    public void submitRequirment(Requirement req, String token, List<RequirementItem> requirementItems) {
        User user;
        if (token == null || StringUtils.isBlank(token)) {
            user = commonService.getCurrentUser();
        } else {
            user = userService.checkUser(token);
        }
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        String userGuid = user.getGuid();

        if (req == null) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_EMPTY.getCode(), MessageTemplate.REQUIREMENT_EMPTY.getMsg());
        }
        if ((req.getRequest() != null) && (req.getRequest().length() > Constant.SIZE)) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_SIZE_ERROR.getCode(),
                    MessageTemplate.REQUIREMENT_SIZE_ERROR.getMsg());
        }

        if (StringUtils.isBlank(req.getRequest())) {
            req.setRequest(null);
        }

        req.setFileUrl(req.getFileUrl() == "" ? null : req.getFileUrl());

        //空白需求类型为RECEIPT
        if (req.getRequest() == null && req.getFileUrl() == null && req.getType().equals(RequirementType.HELP.getCode())) {
            req.setType(RequirementType.RECEIPT.getCode());
        }
        //检查空白需求再次提交是否达到规定时间;
        if (req.getType().equals(RequirementType.RECEIPT.getCode())) {
            if (checkSubmitIsLimited(user.getGuid())) {
                throw new BusinessException(MessageTemplate.REQUIREMENT_SUBMIT_LIMITED.getCode(),
                        MessageTemplate.REQUIREMENT_SUBMIT_LIMITED.getMsg());
            }
        }

        req.setUserGuid(userGuid);
        req.setCode(genCode());
        req.preInsert(globalIdService.getId());

        if (genericDaoService.insert(req) != 1) {  //插入采购需求主表
            throw new BusinessException(MessageTemplate.REQUIREMENT_SUBMIT_ERROR.getCode(), MessageTemplate.REQUIREMENT_SUBMIT_ERROR.getMsg());
        }

        RequirementStatusChangeRecord record = new RequirementStatusChangeRecord(userGuid, req.getGuid(), req.getCode(), req.getCode(),
                new Date(), null, RequirementStatus.NEW.getCode(), RemoteDataSource.WEB.getCode());
        record.preInsert(globalIdService.getId());

        if (genericDaoService.insert(record) != 1) { //插入需求单状态变化记录表
            throw new BusinessException(MessageTemplate.REQUIREMENT_SUBMIT_ERROR.getCode(), MessageTemplate.REQUIREMENT_SUBMIT_ERROR.getMsg());
        }

        if (requirementItems != null && !requirementItems.isEmpty()) {
            requirementItems.stream().forEach(a -> a.setRequirementGuid(req.getGuid()));          //设置主表外键
            if (requirementItemDao.batchInsert(requirementItems) != requirementItems.size()) {
                throw new BusinessException(MessageTemplate.CART_SUBMIT_ERROR.getCode(), MessageTemplate.CART_SUBMIT_ERROR.getMsg());
            }
        }
        Market2PickRequirement market2PickRequirement = new Market2PickRequirement(req.getCode(), req.getSource(), commonService.getASSRequirementType(req.getType()), user.getMobile());
        MqLog mqLog = new MqLog(MqLogModual.MARKET_REQUIREMENT.getModule(), RemoteDataSource.PICK.getCode(), new Gson().toJson(market2PickRequirement), "Y", null);

        //将需求单编号,source，type(计算过后),mobile推送到分拣系统
        commonService.sendMqAndInsertLog(requirementDes, market2PickRequirement, mqLog);

        //如果是网站提交的需求，向APP推送消息
        if((token == null || StringUtils.isBlank(token)) &&
                (requirementItems == null || requirementItems.isEmpty())) {
            commonService.sendNotification(user.getMobile());
        }
    }

    /**
     * 检查空白需求再次提交是否达到规定时间
     *
     * @param userGuid
     * @return
     */
    private boolean checkSubmitIsLimited(String userGuid) {
        if (requirementDao.selectUnresendByParam(userGuid) == null) {
            return false;
        }
        return true;
    }

    /**
     * @Author: Tiny
     * @Description: 获取需求单
     * @Date: 2016年05月05日
     */
    @Override
    public List<RequirementDto> getRequirement(RequirementQuery requirementQuery) {

        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }

        requirementQuery.setLength(Constant.PAGE_SIZE);
        requirementQuery.setStart(Constant.PAGE_SIZE * (requirementQuery.getStart() - 1));
        requirementQuery.setUserGuid(user.getGuid());

        /**
         * 数据
         * 待确认（系统内） NEW
         * 报价中（分拣系统）PICKED
         * 已报价（智能找货） QUOTED
         * 已完成（智能找货） FINISHED
         * 已关闭 CLOSED
         *
         * 按时间排序
         *
         * 返回
         */

        List<RequirementDto> requirementList = new LinkedList<>();

        List<RequirementStatusChangeRecord> recordList = requirementStatusChangeRecordDao.selectByQuery(requirementQuery);
        if (recordList.isEmpty()) {
            return new LinkedList<>();
        }

        //按数据来源来分数据
        Map<String, List<RequirementStatusChangeRecord>> groupBySourceRecordsMap =
                recordList.stream().collect(Collectors.groupingBy(RequirementStatusChangeRecord::getSource, Collectors.toList()));
        //按key去不同系统取数据
        for (String key : groupBySourceRecordsMap.keySet()) {
            requirementList.addAll(memberService.getRequirement(key, groupBySourceRecordsMap.get(key)));
        }

        //排序(按创建时间从大到小倒序排)
        requirementList.sort((a, b) -> b.getCreated().compareTo(a.getCreated()));

        return requirementList;
    }

    /**
     * @Author: Tiny
     * @Description: 获取需求详情
     * @Date: 2016年05月05日
     */
    @Override
    public List<RequirementDto> viewDetail(String requirementCode) {
        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }

        List<RequirementStatusChangeRecord> statusChangeRecords = requirementStatusChangeRecordDao.selectByRequirementCode(requirementCode);
        List<RequirementDto> detailList = new LinkedList<>();
        if (statusChangeRecords.isEmpty()) {
            return new LinkedList<>();
        }

        //按数据来源来分数据
        Map<String, List<RequirementStatusChangeRecord>> groupBySourceRecordsMap =
                statusChangeRecords.stream().collect(Collectors.groupingBy(RequirementStatusChangeRecord::getSource, Collectors.toList()));
        //按key去不同系统取数据
        for (String key : groupBySourceRecordsMap.keySet()) {
            detailList.addAll(memberService.getRequirement(key, groupBySourceRecordsMap.get(key)));
        }

        //排序(按创建时间从大到小倒序排)
        detailList.sort((a, b) -> b.getCreated().compareTo(a.getCreated()));

        return detailList;
    }

    /**
     * 按需求单code筛选
     *
     * @param codes
     * @return
     */
    @Override
    public List<RequirementDto> selectRequirementsByCodes(String codes) {
        List<RequirementDto> requirements = requirementDao.selectByCodes(Arrays.asList(codes.split(",")));
        requirements.stream().forEach(a -> {
            a.setStageStatus(RequirementStatus.NEW.getCode());
            if (RequirementType.CART.getCode().equals(a.getType()) || RequirementType.ONEMORE.getCode().equals(a.getType())) {
                a.setItems(requirementItemDao.selectByRequirementGuid(a.getGuid()));
            }
        });
        return requirements;
    }

    /**
     * 获取需求总数
     *
     * @Date: 2016年05月09日
     */
    @Override
    public Map<String, Integer> totalRequirement(User user) {

        RequirementQuery requirementQuery = new RequirementQuery();
        requirementQuery.setUserGuid(user.getGuid());

        return requirementDao.totalRequirement(requirementQuery);
    }

    /**
     * 根据状态获取需求总数
     *
     * @param requirementQuery
     * @Date: 2016年05月09日
     */
    @Override
    public int requirementCountByStatus(RequirementQuery requirementQuery) {

        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        requirementQuery.setUserGuid(user.getGuid());

        return requirementDao.requirementCountByStatus(requirementQuery);
    }

    /**
     * @Author: Tiny
     * @Description: 分拣系统/找货系统/CBMS 更新需求单状态
     * @Date: 2016年05月23日
     */
    @Override
    @Transactional
    public void updateRequirementByRemote(RequirementStatusChangerQuery changer) {
        //1.参数格式检验
        // 1.1 非空校验
        // 1.2 statusTo，是否为PICKED或CLOSED，如果是CLOSED，是否提供了closeReason
        // 1.3 remoteOrderCreated生成时间格式是否正确
        //2.更改需求单状态
        //3.在busi_requirement_status_change_record表里插一条记录

        // 1.1 非空校验
        if (StringUtils.isBlank(changer.getCode())) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CODE_EMPTY.getCode(),
                    MessageTemplate.REQUIREMENT_CODE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(changer.getStatusTo())) {
            throw new BusinessException(MessageTemplate.REMOTE_STATUS_TO_EMPTY.getCode(),
                    MessageTemplate.REMOTE_STATUS_TO_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(changer.getOperateCode())) {
            throw new BusinessException(MessageTemplate.REMOTE_CODE_EMPTY.getCode(),
                    MessageTemplate.REMOTE_CODE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(changer.getOperated()) && RequirementStatus.CLOSED.getCode().equals(changer.getStatusTo())) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CLOSED_DATE.getCode(),
                    MessageTemplate.REQUIREMENT_CLOSED_DATE.getMsg());
        } else if (StringUtils.isBlank(changer.getOperated()) && !RequirementStatus.CLOSED.getCode().equals(changer.getStatusTo())) {
            throw new BusinessException(MessageTemplate.REMOTE_CREATED_EMPTY.getCode(),
                    MessageTemplate.REMOTE_CREATED_EMPTY.getMsg());
        }

        //分拣系统更新需求单状态（PICKED/CLOSED）
        if (RemoteDataSource.PICK.getCode().equals(changer.getSource()) &&
                !RequirementStatus.PICKED.getCode().equals(changer.getStatusTo()) &&
                !RequirementStatus.CLOSED.getCode().equals(changer.getStatusTo())) {
            throw new BusinessException(MessageTemplate.REMOTE_STATUS_TO_ERROR.getCode(),
                    MessageTemplate.REMOTE_STATUS_TO_ERROR.getMsg());
        }
        //找货系统更新需求单状态（QUOTED/CLOSED/QUOTED_CLOSED/ACTIVATED）
        if (RemoteDataSource.SMART.getCode().equals(changer.getSource()) &&
                !RequirementStatus.QUOTED.getCode().equals(changer.getStatusTo()) &&
                !RequirementStatus.CLOSED.getCode().equals(changer.getStatusTo()) &&
                !RequirementStatus.QUOTED_CLOSED.getCode().equals(changer.getStatusTo()) &&
                !RequirementStatus.ACTIVATED.getCode().equals(changer.getStatusTo())) {
            throw new BusinessException(MessageTemplate.REMOTE_STATUS_TO_ERROR.getCode(),
                    MessageTemplate.REMOTE_STATUS_TO_ERROR.getMsg());
        }
        //CBMS系统更新需求单状态（FINISHED/CLOSED）
        if (RemoteDataSource.CBMS.getCode().equals(changer.getSource()) &&
                !RequirementStatus.FINISHED.getCode().equals(changer.getStatusTo()) &&
                !RequirementStatus.CLOSED.getCode().equals(changer.getStatusTo())) {
            throw new BusinessException(MessageTemplate.REMOTE_STATUS_TO_ERROR.getCode(),
                    MessageTemplate.REMOTE_STATUS_TO_ERROR.getMsg());

        }
        //如果是CLOSED/QUOTED_CLOSED，closeReason必填
        if ((RequirementStatus.CLOSED.getCode().equals(changer.getStatusTo()) ||
                RequirementStatus.QUOTED_CLOSED.getCode().equals(changer.getStatusTo())) && StringUtils.isBlank(changer.getCloseReason())) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CLOSED_REASON_EMPTY.getCode(),
                    MessageTemplate.REQUIREMENT_CLOSED_REASON_EMPTY.getMsg());
        }

        Requirement req = genericDaoService.findOne(new Requirement(changer.getCode()));
        if (req == null) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CODE_NOT_EXIST.getCode(),
                    MessageTemplate.REQUIREMENT_CODE_NOT_EXIST.getMsg());
        }

        //操作时间是否正确
        //1.关闭时间错误
        //2.创建时间错误
        if ((RequirementStatus.CLOSED.getCode().equals(changer.getStatusTo()) ||
                RequirementStatus.QUOTED_CLOSED.getCode().equals(changer.getStatusTo())) &&
                (DateUtil.strToDate(changer.getOperated(), Constant.DATE_TIME_FORMAT) == null
                        || DateUtil.strToDate(changer.getOperated(), Constant.DATE_TIME_FORMAT).before(req.getLastUpdated()))) {
            throw new BusinessException(MessageTemplate.REMOTE_CLOSED_DATE_ERROR.getCode(),
                    MessageTemplate.REMOTE_CLOSED_DATE_ERROR.getMsg());
        } else if (!RequirementStatus.CLOSED.getCode().equals(changer.getStatusTo()) &&
                !RequirementStatus.QUOTED_CLOSED.getCode().equals(changer.getStatusTo()) &&
                (DateUtil.strToDate(changer.getOperated(), Constant.DATE_TIME_FORMAT) == null
                        || DateUtil.strToDate(changer.getOperated(), Constant.DATE_TIME_FORMAT).before(req.getLastUpdated()))) {
            throw new BusinessException(MessageTemplate.REMOTE_CREATED_DATE_ERROR.getCode(),
                    MessageTemplate.REMOTE_CREATED_DATE_ERROR.getMsg());
        }

        //需求单状态已完成，不能做任何操作
        if (RequirementStatus.FINISHED.getCode().equals(req.getStageStatus())) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CODE_FINISHED.getCode(),
                    MessageTemplate.REQUIREMENT_CODE_FINISHED.getMsg());
        }

        //需求单状态已关闭，只可以做激活操作
        if (RequirementStatus.CLOSED.getCode().equals(req.getStageStatus()) && !RequirementStatus.ACTIVATED.getCode().equals(changer.getStatusTo())) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CODE_CLOSED.getCode(),
                    MessageTemplate.REQUIREMENT_CODE_CLOSED.getMsg());
        }

        //只有需求单状态是关闭的才需要做激活操作
        if (RequirementStatus.ACTIVATED.getCode().equals(changer.getStatusTo()) && !RequirementStatus.CLOSED.getCode().equals(req.getStageStatus())) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_IS_OPEN.getCode(),
                    MessageTemplate.REQUIREMENT_IS_OPEN.getMsg());
        }

        //需求单状态不为new,则需求单号跟外部单号不能相同
        if (!RequirementStatus.NEW.getCode().equals(req.getStageStatus()) && changer.getOperateCode().equals(changer.getCode())) {
            throw new BusinessException(MessageTemplate.REMOTE_ORDER_CODE_ERROR.getCode(),
                    MessageTemplate.REMOTE_ORDER_CODE_ERROR.getMsg());
        }
        //分检只能处理状态为NEW的需求单
        if (!RequirementStatus.NEW.getCode().equals(req.getStageStatus()) && RemoteDataSource.PICK.getCode().equals(changer.getSource())) {
            throw new BusinessException(MessageTemplate.PICK_OPERATE_ERROR.getCode(),
                    MessageTemplate.PICK_OPERATE_ERROR.getMsg());
        }
        //找货只能处理状态为PICKED,QUOTED,CLOSED,ACTIVATED的需求单
        if (!RequirementStatus.PICKED.getCode().equals(req.getStageStatus()) && !RequirementStatus.QUOTED.getCode().equals(req.getStageStatus())
                && !RequirementStatus.CLOSED.getCode().equals(req.getStageStatus()) && !RequirementStatus.ACTIVATED.getCode().equals(req.getStageStatus())
                && RemoteDataSource.SMART.getCode().equals(changer.getSource())) {
            throw new BusinessException(MessageTemplate.SMART_OPERATE_ERROR.getCode(),
                    MessageTemplate.SMART_OPERATE_ERROR.getMsg());
        }
        //CBMS只能处理状态为QUOTED的需求单
        if (!RequirementStatus.QUOTED.getCode().equals(req.getStageStatus()) && RemoteDataSource.CBMS.getCode().equals(changer.getSource())) {
            throw new BusinessException(MessageTemplate.CBMS_OPERATE_ERROR.getCode(),
                    MessageTemplate.CBMS_OPERATE_ERROR.getMsg());
        }
        //如果是已报价已关闭QUOTED_CLOSED状态的;更改需求单主表状态;在busi_requirement_status_change_record表里插两条记录
        if (RequirementStatus.QUOTED_CLOSED.getCode().equals(changer.getStatusTo())) {
            doSmartQuotedClosedStatus(changer, req);
        } else {//否则更改需求单状态;更改需求单主表状态;在busi_requirement_status_change_record表里插一条记录
            updateAndInsertRecord(changer, req);
        }
        //向app发送消息
        User user = userDao.selectByGuid(req.getUserGuid());
        if (user != null) {
            commonService.sendNotification(user.getMobile());
        }
    }

    /**
     * @Author: Tiny
     * @Description: 如果是已报价已关闭QUOTED_CLOSED状态的;更改需求单主表状态;在busi_requirement_status_change_record表里插两条记录
     * @Date: 2016年08月16日
     */
    private void doSmartQuotedClosedStatus(RequirementStatusChangerQuery changer, Requirement req) {
        RequirementStatusChangeRecord recordQuoted = new RequirementStatusChangeRecord(req.getUserGuid(), req.getGuid(), req.getCode(),
                changer.getOperateCode(), DateUtil.strToDate(changer.getOperated(), Constant.DATE_TIME_FORMAT),
                req.getStageStatus(), RequirementStatus.QUOTED.getCode(), changer.getSource(), Constant.SYS_USER, Constant.SYS_USER);

        req.setCloseStage(RequirementCloseStage.QUOTED.getCode());
        req.setCloseReason(changer.getCloseReason());
        req.setStageStatus(RequirementStatus.CLOSED.getCode());
        req.setLastUpdated(DateUtil.strToDate(changer.getOperated(), Constant.DATE_TIME_FORMAT));
        //更改需求单状态
        if (genericDaoService.updateByKey(req) != 1) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_UPDATE_FAIL.getCode(),
                    MessageTemplate.REQUIREMENT_UPDATE_FAIL.getMsg());
        }

        //插入需求单状态变化记录表(QUOTED)
        recordQuoted.preInsert(globalIdService.getId());
        if (genericDaoService.insert(recordQuoted) != 1) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_INSERT_FAIL.getCode(),
                    MessageTemplate.REQUIREMENT_INSERT_FAIL.getMsg());
        }

        //插入需求单状态变化记录表(CLOSED)
        RequirementStatusChangeRecord recordClosed = new RequirementStatusChangeRecord(req.getUserGuid(), req.getGuid(), req.getCode(),
                changer.getOperateCode(), DateUtil.strToDate(changer.getOperated(), Constant.DATE_TIME_FORMAT),
                RequirementStatus.QUOTED.getCode(), RequirementStatus.CLOSED.getCode(), changer.getSource(), Constant.SYS_USER, Constant.SYS_USER);
        recordClosed.preInsert(globalIdService.getId());
        if (genericDaoService.insert(recordClosed) != 1) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_INSERT_FAIL.getCode(),
                    MessageTemplate.REQUIREMENT_INSERT_FAIL.getMsg());
        }
    }

    /**
     * @Author: Tiny
     * @Description: 更改需求单状态;在busi_requirement_status_change_record表里插一条记录
     * @Date: 2016年05月23日
     */
    private void updateAndInsertRecord(RequirementStatusChangerQuery changer, Requirement req) {

        //原状态是已报价时做更新或者原状态和现状态不一致时做更新
        if (req.getStageStatus().equals(RequirementStatus.QUOTED.getCode()) || !req.getStageStatus().equals(changer.getStatusTo())) {
            String statusTo = RequirementStatus.ACTIVATED.getCode().equals(changer.getStatusTo()) ? RequirementStatus.PICKED.getCode() : changer.getStatusTo();
            RequirementStatusChangeRecord reqRecord = new RequirementStatusChangeRecord(req.getUserGuid(), req.getGuid(), req.getCode(),
                    changer.getOperateCode(), DateUtil.strToDate(changer.getOperated(), Constant.DATE_TIME_FORMAT),
                    req.getStageStatus(), statusTo, changer.getSource());

            if (RequirementStatus.CLOSED.getCode().equals(changer.getStatusTo())) {
                //分拣阶段关闭需求
                if (RequirementStatus.NEW.getCode().equals(req.getStageStatus())) {
                    req.setCloseStage(RequirementCloseStage.PICKUP.getCode());
                    reqRecord.setSource(RemoteDataSource.WEB.getCode());
                }
                //询价阶段关闭需求
                if (RequirementStatus.PICKED.getCode().equals(req.getStageStatus())) {
                    req.setCloseStage(RequirementCloseStage.INQUIRY.getCode());
                    reqRecord.setSource(RemoteDataSource.PICK.getCode());
                }
                //报价阶段,来源是找货 关闭需求
                if (RequirementStatus.QUOTED.getCode().equals(req.getStageStatus()) && RemoteDataSource.SMART.getCode().equals(changer.getSource())) {
                    req.setCloseStage(RequirementCloseStage.QUOTED.getCode());
                    reqRecord.setSource(RemoteDataSource.SMART.getCode());
                }
                //报价阶段,来源是cbms 关闭需求
                if (RequirementStatus.QUOTED.getCode().equals(req.getStageStatus()) && RemoteDataSource.CBMS.getCode().equals(changer.getSource())) {
                    req.setCloseStage(RequirementCloseStage.BILL.getCode());
                    reqRecord.setSource(RemoteDataSource.CBMS.getCode());
                }

                req.setCloseReason(changer.getCloseReason());
            }

            //激活外部单号来源设为分检；关闭理由，关闭阶段清空
            if (RequirementStatus.ACTIVATED.getCode().equals(changer.getStatusTo())) {
                reqRecord.setSource(RemoteDataSource.PICK.getCode());
                req.setCloseStage("");
                req.setCloseReason("");
            }

            req.setStageStatus(statusTo);
            req.setLastUpdated(DateUtil.strToDate(changer.getOperated(), Constant.DATE_TIME_FORMAT));

            //更改需求单状态
            if (genericDaoService.updateByKey(req) != 1) {
                throw new BusinessException(MessageTemplate.REQUIREMENT_UPDATE_FAIL.getCode(),
                        MessageTemplate.REQUIREMENT_UPDATE_FAIL.getMsg());
            }

            //在busi_requirement_status_change_record表里插一条记录
            reqRecord.setCreatedBy(Constant.SYS_USER);
            reqRecord.setLastUpdatedBy(Constant.SYS_USER);
            reqRecord.preInsert(globalIdService.getId());
            if (genericDaoService.insert(reqRecord) != 1) { //插入需求单状态变化记录表
                throw new BusinessException(MessageTemplate.REQUIREMENT_UPDATE_FAIL.getCode(),
                        MessageTemplate.REQUIREMENT_UPDATE_FAIL.getMsg());
            }
        } else {
            throw new BusinessException(MessageTemplate.REQUIREMENT_STAGESTATUS_ERROR.getCode(),
                    MessageTemplate.REQUIREMENT_STAGESTATUS_ERROR.getMsg());
        }
    }

    /**
     * @Author: Tiny
     * @Description: 分拣系统根据单号获取需求单明细信息
     * @Date: 2016年05月26日
     */
    @Override
    public RequirementForPickDto getDetailsByCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CODE_EMPTY.getCode(),
                    MessageTemplate.REQUIREMENT_CODE_EMPTY.getMsg());
        }
        RequirementForPickDto pickDto = requirementDao.selectPickDtoByCode(code);
        if (pickDto == null) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CODE_NOT_EXIST.getCode(),
                    MessageTemplate.REQUIREMENT_CODE_NOT_EXIST.getMsg());
        }

        pickDto.setRequestType(commonService.getASSRequirementType(pickDto.getRequestType()));

        if (ASSRequirementType.FORM.getCode().equals(pickDto.getRequestType())) {
            pickDto.setItems(requirementItemDao.selectItemsByRequirementCode(code));
        }

        if (RemoteDataSource.PICK.getCode().equals(pickDto.getSource())) {
            pickDto.setSource(Constant.SOURCE_TO_PICK);
        }

        return pickDto;
    }

    /**
     * @Author: Tiny
     * @Description: 分拣系统推送新增需求单给超市
     * @Date: 2016年05月26日
     */
    @Override
    @Transactional
    public void createRequirementPick(CreateRequirementFromPick create) {
        /**
         * 1.非空判断,时间校验
         * 2.判断客户是否存在；不存在，则新增客户（同步新增到CBMS）
         * 3.新增需求单到需求表（1条记录；来源 PICK; 状态 PICKED）和 需求记录表中(2条; 1条NEW, 1条PICKED)
         */
        if (create == null) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_PARAM_EMPTY.getCode(),
                    MessageTemplate.REQUIREMENT_PARAM_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(create.getRequirementCode())) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CODE_EMPTY.getCode(),
                    MessageTemplate.REQUIREMENT_CODE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(create.getRequirementCreated())) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CREATED_EMPTY.getCode(),
                    MessageTemplate.REQUIREMENT_CREATED_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(create.getUserMobile())) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_MOBILE_EMPTY.getCode(),
                    MessageTemplate.REMOTE_USER_MOBILE_EMPTY.getMsg());
        }
        if (create.getUserMobile().length() != Constant.MOBILE_SIZE || !StringUtil.isPhoneNumberCheck(create.getUserMobile())) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_MOBILE_FORMAT_ERROR.getCode(),
                    MessageTemplate.REMOTE_USER_MOBILE_FORMAT_ERROR.getMsg());
        }
        if (StringUtils.isBlank(create.getUserName())) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_NAME_EMPTY.getCode(),
                    MessageTemplate.REMOTE_USER_NAME_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(create.getUserAccount())) {
            throw new BusinessException(MessageTemplate.REMOTE_USER_ACCOUNT_EMPTY.getCode(),
                    MessageTemplate.REMOTE_USER_ACCOUNT_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(create.getPickCode())) {
            throw new BusinessException(MessageTemplate.REMOTE_CODE_EMPTY.getCode(),
                    MessageTemplate.REMOTE_CODE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(create.getPickCreated())) {
            throw new BusinessException(MessageTemplate.REMOTE_CREATED_EMPTY.getCode(),
                    MessageTemplate.REMOTE_CREATED_EMPTY.getMsg());
        }
        if (DateUtil.strToDate(create.getRequirementCreated(), Constant.DATE_TIME_FORMAT) == null) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_CREATED_DATE_ERROR.getCode(),
                    MessageTemplate.REQUIREMENT_CREATED_DATE_ERROR.getMsg());
        }
        //分检单号生成时间格式校验；分检单号生成时间要在超市需求单生成时间之后
        if (DateUtil.strToDate(create.getPickCreated(), Constant.DATE_TIME_FORMAT) == null ||
                DateUtil.strToDate(create.getPickCreated(), Constant.DATE_TIME_FORMAT).before(DateUtil.strToDate(create.getRequirementCreated(), Constant.DATE_TIME_FORMAT))) {
            throw new BusinessException(MessageTemplate.REMOTE_CREATED_DATE_ERROR.getCode(),
                    MessageTemplate.REMOTE_CREATED_DATE_ERROR.getMsg());
        }

        AddCbmsContact addCbmsContact = null;

        User user = userDao.selectByPhone(create.getUserMobile());

        //cas表中不存在或者user表中不存在
        if (casUserDao.selectByPhone(create.getUserMobile()) == null) {
            user = userService.addRemoteUser(create.getUserMobile(), create.getUserName(), UserSource.PICK,
                    SMSTemplate.PICK_USER_DYNAMIC_PWD, OpType.PICK_USER_ADD);
            addCbmsContact = new AddCbmsContact(user.getId(), user.getMobile(), user.getName(), create.getUserAccount(), user.getSource());
        }

        if (user == null) {
            user = new User(create.getUserName(), create.getUserMobile(), null,
                    UserSource.PICK.toString(), Constant.USER_STATUS_ENABLE, Constant.SYS_USER, Constant.SYS_USER);
            user.preInsert(globalIdService.getId());
            userDao.insert(user);
            addCbmsContact = new AddCbmsContact(user.getId(), user.getMobile(), user.getName(), create.getUserAccount(), user.getSource());
        }

        //分检新增的需求单单号已经存在
        if (genericDaoService.findOne(new Requirement(create.getRequirementCode())) != null) {
            throw new BusinessException(MessageTemplate.PICK_REQUIREMENT_CODE_EXIST.getCode(),
                    MessageTemplate.PICK_REQUIREMENT_CODE_EXIST.getMsg());
        }
        //3.新增需求单到需求表（1条记录；来源 PICK; 状态 PICKED）和 需求记录表中(2条; 1条NEW, 1条PICKED)
        createRequirement(create, user);
        //新增的超市用户推送到cbms
        if (addCbmsContact != null) {
            MqLog mqLog = new MqLog(MqLogModual.ADD_CBMS_CONTACT.getModule(), RemoteDataSource.CBMS.getCode(), new Gson().toJson(addCbmsContact), "Y", null);
            commonService.sendMqAndInsertLog(addCBMSContactDes, addCbmsContact, mqLog);
        }
        commonService.sendNotification(user.getMobile());
    }

    /**
     * @Author: Tiny
     * @Description: 新增需求单到需求表（1条记录；来源 PICK; 状态 PICKED）和 需求记录表中(2条; 1条NEW, 1条PICKED)
     * @Date: 2016年05月26日
     */
    private void createRequirement(CreateRequirementFromPick create, User user) {
        //插入采购需求主表
        Requirement req = new Requirement(create.getRequirementCode(), user.getGuid(), RemoteDataSource.PICK.getCode(), RequirementType.RECEIPT.getCode(),
                RequirementStatus.PICKED.getCode(), Constant.SYS_USER, Constant.SYS_USER);
        req.preInsert(globalIdService.getId());
        req.setCreated(DateUtil.strToDate(create.getRequirementCreated(), Constant.DATE_TIME_FORMAT));
        req.setLastUpdated(DateUtil.strToDate(create.getPickCreated(), Constant.DATE_TIME_FORMAT));

        if (genericDaoService.insert(req) != 1) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_INSERT_FAIL.getCode(),
                    MessageTemplate.REQUIREMENT_INSERT_FAIL.getMsg());
        }

        //插入需求单状态变化记录表(NEW)
        RequirementStatusChangeRecord recordNew = new RequirementStatusChangeRecord(user.getGuid(), req.getGuid(), req.getCode(),
                req.getCode(), req.getCreated(), null, RequirementStatus.NEW.getCode(), RemoteDataSource.WEB.getCode(),
                Constant.SYS_USER, Constant.SYS_USER);
        recordNew.preInsert(globalIdService.getId());

        if (genericDaoService.insert(recordNew) != 1) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_INSERT_FAIL.getCode(),
                    MessageTemplate.REQUIREMENT_INSERT_FAIL.getMsg());
        }

        //插入需求单状态变化记录表(PICKED)
        RequirementStatusChangeRecord recordPicked = new RequirementStatusChangeRecord(user.getGuid(), req.getGuid(), req.getCode(),
                create.getPickCode(), req.getLastUpdated(), RequirementStatus.NEW.getCode(), RequirementStatus.PICKED.getCode(),
                RemoteDataSource.PICK.getCode(), Constant.SYS_USER, Constant.SYS_USER);
        recordPicked.preInsert(globalIdService.getId());

        if (genericDaoService.insert(recordPicked) != 1) {
            throw new BusinessException(MessageTemplate.REQUIREMENT_INSERT_FAIL.getCode(),
                    MessageTemplate.REQUIREMENT_INSERT_FAIL.getMsg());
        }

    }
}
