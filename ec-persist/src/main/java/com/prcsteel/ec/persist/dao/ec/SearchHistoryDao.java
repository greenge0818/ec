package com.prcsteel.ec.persist.dao.ec;

import com.prcsteel.ec.model.dto.SearchHistoryDto;
import com.prcsteel.ec.model.query.Market2SmartResourceQuery;
import com.prcsteel.ec.model.query.SearchHistoryQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryDao {
    /**
     * 获取最近搜索数据
     * @param query
     * @return
     */
    List<SearchHistoryDto> getRecentSearchList(SearchHistoryQuery query);

    /**
     * 按id（软）删除最近搜索记录
     * @param query
     * @return
     */
    Integer softDelById(SearchHistoryQuery query);

    /**
     * 搜索记录归属权确认
     * @param userGuid
     * @param cookieId
     * @return
     */
    Integer setCookieToLoginId(@Param("userGuid") String userGuid, @Param("cookieId") String cookieId);

    /**
     * 获取最近10条不重复的搜索记录
     * @param userGuid
     * @return
     */
    List<Market2SmartResourceQuery> getHistoryList(@Param("userGuid") String userGuid);
}