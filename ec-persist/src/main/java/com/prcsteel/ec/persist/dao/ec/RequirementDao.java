package com.prcsteel.ec.persist.dao.ec;

import com.prcsteel.ec.model.domain.ec.Requirement;
import com.prcsteel.ec.model.dto.RequirementDto;
import com.prcsteel.ec.model.dto.RequirementForPickDto;
import com.prcsteel.ec.model.query.RequirementQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: RequirementDao
 * @Description: 需求单
 * @Author Tiny
 * @Date 2016年4月28日
 */
@Repository
public interface RequirementDao {

    /**
     * 按需求单code筛选
     *
     * @param codeList
     * @return
     */
    List<RequirementDto> selectByCodes(@Param("codeList") List<String> codeList);

    /**
     * 获取需求总数
     *
     * @param requirementQuery
     * @Date: 2016年05月09日
     */
    Map<String, Integer> totalRequirement(RequirementQuery requirementQuery);

    /**
     * 根据状态获取需求总数
     *
     * @param requirementQuery
     * @Date: 2016年05月09日
     */
    int requirementCountByStatus(RequirementQuery requirementQuery);

    /**
     * 根据参数查询出不可以再次提交的记录
     *
     * @param userGuid
     * @Date: 2016年05月11日
     */
    Requirement selectUnresendByParam(@Param("userGuid") String userGuid);

    /**
     * @Author: Tiny
     * @Description: 根据code获取需求单明细信息
     * @Date: 2016年05月26日
     */
    RequirementForPickDto selectPickDtoByCode(@Param("code") String code);

    /**
     * @Author: Green.Ge
     * @Description: 根据日期获取当天最大单号
     * @Date: 2016年05月26日
     */
    Integer getMaxCode(@Param("date") String date);
}
