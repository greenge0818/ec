package com.prcsteel.ec.service;

import com.prcsteel.ec.model.dto.ResourceDto;
import com.prcsteel.ec.model.dto.SearchHistoryDto;
import com.prcsteel.ec.model.dto.Smart2MarketResourceDto;
import com.prcsteel.ec.model.query.Market2SmartResourceQuery;

import java.util.List;

/**
 * Created by myh on 2016/5/16.
 */
public interface SearchService {
    /**
     * 按条件搜索资源
     *
     * @param market2SmartResourceQuery
     * @return
     * @see Market2SmartResourceQuery
     */
    Smart2MarketResourceDto search(Market2SmartResourceQuery market2SmartResourceQuery);

    /**
     * 获取最近搜索数据
     *
     * @param cookieId
     */
    List<SearchHistoryDto> getRecentSearchList(String cookieId);

    /**
     * 按id（软）删除最近搜索记录
     *
     * @param id
     */
    void delById(Long id, String cookieId);

    /**
     * @Author: Tiny
     * @Description: 获取热门资源
     * @Date: 2016年05月18日
     */
    List<ResourceDto> getHotResourceList(String city);

    /**
     * 根据当天有效的搜索记录Union已有猜你喜欢记录，按账号ID及品种组合其他条件更新到偏好表中
     */
    void searchPreferenceJob();

    /**
     * 获取猜你喜欢数据
     * @return
     */
    List<SearchHistoryDto> getSearchPreferenceList();

    /**
     * 删除猜你喜欢
     * @param id
     * @return
     */
    void delSearchPreference(Long id);
}
