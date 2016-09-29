package com.prcsteel.ec.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.model.RestResult;
import com.prcsteel.ec.core.service.CacheService;
import com.prcsteel.ec.core.util.NumberUtil;
import com.prcsteel.ec.model.domain.ec.SearchHistory;
import com.prcsteel.ec.model.domain.ec.User;
import com.prcsteel.ec.model.dto.ResourceDto;
import com.prcsteel.ec.model.dto.SearchHistoryDto;
import com.prcsteel.ec.model.dto.Smart2MarketResourceDto;
import com.prcsteel.ec.model.query.Market2SmartResourceQuery;
import com.prcsteel.ec.model.query.MarketToSmartHotResourceQuery;
import com.prcsteel.ec.model.query.SearchHistoryQuery;
import com.prcsteel.ec.persist.dao.ec.SearchHistoryDao;
import com.prcsteel.ec.persist.dao.ec.SearchPreferenceDao;
import com.prcsteel.ec.service.CommonService;
import com.prcsteel.ec.service.GlobalIdService;
import com.prcsteel.ec.service.MarketService;
import com.prcsteel.ec.service.SearchService;
import com.prcsteel.ec.service.api.RestSmartService;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by myh on 2016/5/16.
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {
    @Resource
    private GenericDaoServiceImpl genericDaoService;
    @Resource
    private GlobalIdService globalIdService;
    @Resource
    private CommonService commonService;
    @Resource
    private SearchHistoryDao searchHistoryDao;
    @Resource
    private SearchPreferenceDao searchPreferenceDao;
    @Resource
    private MarketService marketService;
    @Resource
    private RestSmartService restSmartService;
    @Resource
    private CacheService cacheService;

    private static final Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    /**
     * 按条件搜索资源
     *
     * @param market2SmartResourceQuery
     * @return
     */
    @Override
    public Smart2MarketResourceDto search(Market2SmartResourceQuery market2SmartResourceQuery) {

        if (market2SmartResourceQuery == null) return null;

//        if(StringUtils.isNotBlank(market2SmartResourceQuery.getCategoryUuid())){
//            market2SmartResourceQuery.setCategoryName(null);
//        }
        //获取当前登录用户guid
        User user = commonService.getCurrentUser();
        if (user != null) {  //如果是登录用户，就记录userGuid，否则记录cookieId
            market2SmartResourceQuery.setUserGuid(user.getGuid());
            market2SmartResourceQuery.setCookieId(null);
        }
        Smart2MarketResourceDto s2m = null;
        //搜索   后续改造从找货拉取数据
        market2SmartResourceQuery.preQuery();

        // 0 代表搜索全国数据
        if ("0".equals(market2SmartResourceQuery.getPurchaseCityIds())) {
            market2SmartResourceQuery.setPurchaseCityIds(null);
        } else {
            //设置城市id对应的城市名称
            market2SmartResourceQuery.setPurchaseCityNames(marketService.getCityNames(market2SmartResourceQuery.getPurchaseCityIds()));
            market2SmartResourceQuery.setTdkCityNames(market2SmartResourceQuery.getPurchaseCityNames());
        }

        List<ResourceDto> result = getResourceFormSM(market2SmartResourceQuery);

        int userPageIndex = market2SmartResourceQuery.getPageIndex();
        int pageNum = result != null && !result.isEmpty() ? NumberUtil.pageCount(result.get(result.size() - 1).getTotal(), market2SmartResourceQuery.getPageSize()) : 0;

        //用户输出的页码数超过数据最大页码，取最后一页数据
        if (pageNum != 0 && userPageIndex > pageNum) {
            market2SmartResourceQuery.setPageIndex(pageNum);
            market2SmartResourceQuery.preQuery();
            result = getResourceFormSM(market2SmartResourceQuery);
        }

        if ((StringUtils.isNotBlank(market2SmartResourceQuery.getCookieId()) || StringUtils.isNotBlank(market2SmartResourceQuery.getUserGuid())) &&
                market2SmartResourceQuery.getIsSearchHistory() && StringUtils.isNotBlank(market2SmartResourceQuery.getCategoryUuid()) && StringUtils.isNotBlank(market2SmartResourceQuery.getCategoryName())) {
            //记录搜索记录
            SearchHistory history = buildHistory(market2SmartResourceQuery);
            history.preInsert(globalIdService.getId());
            if (genericDaoService.insert(history) != 1) {
                throw new BusinessException(MessageTemplate.SEARCH_HISTORY_INSERT_FAIL.getCode(), MessageTemplate.SEARCH_HISTORY_INSERT_FAIL.getMsg());
            }
            //更新偏好
            searchPreferenceDao.showSearchPreference(market2SmartResourceQuery);
        }

        //采购城市不为空并且结果集为空，采用全国二次搜索
        if (StringUtils.isNotBlank(market2SmartResourceQuery.getPurchaseCityIds()) && (result != null && result.size() <= 1)) {
            //没有采购城市取全国的数据
            market2SmartResourceQuery.setPurchaseCityIds(null);
            market2SmartResourceQuery.setPurchaseCityNames(null);
            result = getResourceFormSM(market2SmartResourceQuery);
            market2SmartResourceQuery.setIsSearchFromCity2Country(true);
        }

        if (result != null && !result.isEmpty()) {
            s2m = new Smart2MarketResourceDto();
            //最后一条记录条数
            ResourceDto last = result.get(result.size() - 1);
            result.remove(last);
            s2m.setTotal(last.getTotal());
            s2m.setData(result);
        }
        return s2m;
    }

    /**
     * 从找货获取钢铁超市资源
     *
     * @param market2SmartResourceQuery
     * @return
     */
    private List<ResourceDto> getResourceFormSM(Market2SmartResourceQuery market2SmartResourceQuery) {
        String smartData;
        List<ResourceDto> result = new ArrayList<>();
        RestResult restResult;
        try {
            smartData = restSmartService.queryResourceBySearch(UrlUtils.urlEncode(gson.toJson(market2SmartResourceQuery)));
        } catch (Exception e) {
            throw new BusinessException(MessageTemplate.SM_SERVER_ERROR.getCode(), MessageTemplate.SM_SERVER_ERROR.getMsg());
        }
        try {
            restResult = gson.fromJson(smartData, RestResult.class);
            if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(restResult.getStatus())) {
                Gson newGson = new GsonBuilder().setDateFormat(Constant.DATE_TIME_FORMAT).create();
                result = newGson.fromJson(newGson.toJson(restResult.getData()), new TypeToken<List<ResourceDto>>() {
                }.getType());
            }
        } catch (Exception e) {
            throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
        }
        //过滤资源议价
        if (!result.isEmpty()) {
            Long timeMillis = System.currentTimeMillis();
            result.stream().forEach(e -> {
                //当前时间大于更新时间24小时,资源过期
                if ( e.getLastUpdated() == null || timeMillis - e.getLastUpdated() > 86400000 ) {
                    e.setExpired(true);
                }
            });
        }
        return result;
    }

    /**
     * 组装history数据
     *
     * @param market2SmartResourceQuery
     * @return
     */
    private SearchHistory buildHistory(Market2SmartResourceQuery market2SmartResourceQuery) {
        SearchHistory history = new SearchHistory();
        history.setCookieId(market2SmartResourceQuery.getCookieId());
        history.setUserGuid(market2SmartResourceQuery.getUserGuid());
        history.setCategoryUuid(market2SmartResourceQuery.getCategoryUuid());
        history.setCategoryName(market2SmartResourceQuery.getCategoryName());
        history.setMaterialUuid(market2SmartResourceQuery.getMaterialUuids());
        history.setMaterialName(market2SmartResourceQuery.getMaterialNames());
        history.setSpec1(market2SmartResourceQuery.getSpec1());
        history.setSpec2(market2SmartResourceQuery.getSpec2());
        history.setSpec3(market2SmartResourceQuery.getSpec3());
        history.setCityId(market2SmartResourceQuery.getPurchaseCityIds());
        history.setCityName(market2SmartResourceQuery.getPurchaseCityNames());
        history.setFactoryId(market2SmartResourceQuery.getFactoryIds());
        history.setFactoryName(market2SmartResourceQuery.getFactoryNames());
        history.setLastUpdatedBy(market2SmartResourceQuery.getCookieId());
        history.setCreatedBy(market2SmartResourceQuery.getCookieId());
        return history;
    }

    /**
     * 获取最近搜索数据
     *
     * @param cookieId
     */
    @Override
    public List<SearchHistoryDto> getRecentSearchList(String cookieId) {
        SearchHistoryQuery query = new SearchHistoryQuery();
        User user = commonService.getCurrentUser();
        if (user != null) {  //如果是登录用户，就用guid去查
            query.setUserGuid(user.getGuid());
        } else {
            query.setCookieId(cookieId);
        }
        return searchHistoryDao.getRecentSearchList(query);
    }

    /**
     * 按id（软）删除最近搜索记录
     *
     * @param id
     */
    @Override
    public void delById(Long id, String cookieId) {
        SearchHistoryQuery query = new SearchHistoryQuery();
        User user = commonService.getCurrentUser();
        if (user != null) {  //如果是登录用户，就用guid去查
            query.setUserGuid(user.getGuid());
        } else {
            query.setCookieId(cookieId);
        }
        query.setId(id);
        if (searchHistoryDao.softDelById(query) <= 0) {
            throw new BusinessException(MessageTemplate.SEARCH_HISTORY_DELETE_FAIL.getCode(),
                    MessageTemplate.SEARCH_HISTORY_DELETE_FAIL.getMsg());
        }
    }

    /**
     * 获取猜你喜欢数据
     *
     * @return
     */
    @Override
    public List<SearchHistoryDto> getSearchPreferenceList() {
        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        return searchPreferenceDao.getSearchPreferenceList(user.getGuid());
    }

    /**
     * 删除猜你喜欢
     *
     * @param id
     * @return
     */
    @Override
    public void delSearchPreference(Long id) {
        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        if (searchPreferenceDao.delSearchPreference(id) != 1) {
            throw new BusinessException(MessageTemplate.SEARCH_PREFERENCE_DELETE_FAIL.getCode(), MessageTemplate.SEARCH_PREFERENCE_DELETE_FAIL.getMsg());
        }
    }

    /**
     * @Author: Tiny
     * @Description: 获取热门资源
     * @Date: 2016年05月18日
     */
    @Override
    public List<ResourceDto> getHotResourceList(String city) {

        User user = commonService.getCurrentUser();
        List<ResourceDto> resourceList = new ArrayList<>();
        List<Market2SmartResourceQuery> searchHistoryList = null;
        //未登录；根据IP所在城市从资源表中随机获取十条资源
        if (user != null) {
            searchHistoryList = searchHistoryDao.getHistoryList(user.getGuid());
        }

        //已登录；获取用户历史搜索记录中排行前10的记录进行展示；
        // 若不足十条，则根据IP所在城市从资源表中获取随机资源补足十条
        String data;
        try {
            data = restSmartService.getHotResource(gson.toJson(new MarketToSmartHotResourceQuery(city, Constant.HOT_RESOURCE_SIZE, searchHistoryList)));
        } catch (Exception e) {
            logger.error("获取热门资源数据错误:ESB连接或读取数据超时!");
            //其他系统挂了，从缓存中取
            Object o = cacheService.get(city);
            data = o == null ? null : o.toString();
        }


        if (data != null) {
            try {
                RestResult result = gson.fromJson(data, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                    Gson newGson = new GsonBuilder().setDateFormat(Constant.DATE_TIME_FORMAT).create();
                    resourceList = newGson.fromJson(newGson.toJson(result.getData()), new TypeToken<List<ResourceDto>>() {
                    }.getType());
                }
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }

            //未登录,缓存当前城市热门资源数据
            try {
                if (user == null) {
                    cacheService.add(Constant.HOT_RESOURCE_CACHE_KEY_PRXFIX + city, Constant.HOME_PAGE_CACHE_EXPIRED, data);
                }
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.MEM_OPT_ERROR.getCode(), MessageTemplate.MEM_OPT_ERROR.getMsg());
            }
        }

        //过滤资源议价
        if (!resourceList.isEmpty()) {
            Long timeMillis = System.currentTimeMillis();
            resourceList.stream().forEach(e -> {
                //当前时间大于更新时间24小时,资源过期
                if ( e.getLastUpdated() == null || timeMillis - e.getLastUpdated() > 86400000 ) {
                    e.setExpired(true);
                }
            });
        }
        return resourceList;
    }

    /**
     * 根据当天有效的搜索记录Union已有猜你喜欢记录，按账号ID及品种组合其他条件更新到偏好表中
     */
    @Override
    public void searchPreferenceJob() {
        searchPreferenceDao.searchPreferencesJob();
    }

}
