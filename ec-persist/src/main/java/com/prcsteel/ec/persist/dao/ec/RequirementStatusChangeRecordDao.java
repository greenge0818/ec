package com.prcsteel.ec.persist.dao.ec;


import com.prcsteel.ec.model.domain.ec.RequirementStatusChangeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.prcsteel.ec.model.query.RequirementQuery;

import java.util.Date;
import java.util.List;

@Repository
public interface RequirementStatusChangeRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RequirementStatusChangeRecord record);

    int insertSelective(RequirementStatusChangeRecord record);

    RequirementStatusChangeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RequirementStatusChangeRecord record);

    int updateByPrimaryKey(RequirementStatusChangeRecord record);

    /**
     * 从lastTime往前筛选index条数据
     *
     * @param lastTime
     * @param index
     * @return
     */
    List<RequirementStatusChangeRecord> selectByLastTimeAndUserGuidLimit(@Param("userGuid") String userGuid,
                                                                         @Param("lastTime") Date lastTime, @Param("index") Integer index);

    /**
     * 根据状态获取需求（指定时间内）
     *
     * @param requirementQuery
     * @return
     */
    List<RequirementStatusChangeRecord> selectByQuery(RequirementQuery requirementQuery);

    /**
     * 根据需求ID获取需求
     *
     * @param requirementCode
     * @return
     */
    List<RequirementStatusChangeRecord> selectByRequirementCode(String requirementCode);

    /**
     * 获取某个时间点之后的再来一单列表
     *
     * @param lastTime
     * @param userGuid
     * @return
     */
    List<RequirementStatusChangeRecord> selectByLastTimeAndUserGuidForAPP(@Param("userGuid") String userGuid,
                                                                          @Param("lastTime") Date lastTime,
                                                                          @Param("isOnceMore") Boolean isOnceMore);
}