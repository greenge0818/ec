package com.prcsteel.ec.persist.dao.ec;

import com.prcsteel.ec.model.domain.ec.RequirementItem;
import com.prcsteel.ec.model.dto.RequirementItemDto;
import com.prcsteel.ec.model.dto.RequirementItemForPickDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementItemDao {
    int insert(RequirementItem record);

    /**
     * 批量插入
     *
     * @param record
     * @return
     */
    int batchInsert(List<RequirementItem> record);

    /**
     * 根据requirementGuid查询
     */
    List<RequirementItemDto> selectByRequirementGuid(String requirementGuid);

    /**
     * @Author: Tiny
     * @Description: 根据requiremetCode获取需求单明细信息
     * @Date: 2016年05月26日
     */
    List<RequirementItemForPickDto> selectItemsByRequirementCode(@Param("requirementCode") String requirementCode);
}