package com.prcsteel.ec.persist.dao.ec;

import com.prcsteel.ec.model.dto.SearchHistoryDto;
import com.prcsteel.ec.model.query.Market2SmartResourceQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchPreferenceDao {
    /**
     * 猜你喜欢job
     */
    void searchPreferencesJob();

    /**
     * 获取猜你喜欢数据
     * @return
     */
    List<SearchHistoryDto> getSearchPreferenceList(String userGuid);

    /**
     * 显示猜你喜欢
     * @param query
     * @return
     */
    Integer showSearchPreference(Market2SmartResourceQuery query);

    /**
     * 删除猜你喜欢
     * @param id
     * @return
     */
    Integer delSearchPreference(Long id);
}