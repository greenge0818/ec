package com.prcsteel.ec.persist.dao.ec;


import com.prcsteel.ec.model.domain.ec.RequirementCode;
import org.springframework.stereotype.Repository;

/**
 * 需求单code生成器
 * Created by peanut on 2016/07/22.
 */
@Repository
public interface RequirementCodeDao {

    /**
     * 插入数据
     *
     * @param requirementCode
     * @return
     */
    int insert(RequirementCode requirementCode);

    /**
     * 清空表数据
     */
    void truncate();
}
