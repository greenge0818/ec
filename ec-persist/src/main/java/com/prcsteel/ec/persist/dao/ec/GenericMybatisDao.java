package com.prcsteel.ec.persist.dao.ec;


import com.prcsteel.ec.model.domain.ec.generic.EntityInfo;
import com.prcsteel.ec.model.dto.Page;
import com.prcsteel.ec.model.query.ComplexCond;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Rolyer on 2016/4/27.
 */
public interface GenericMybatisDao {
    int insert(EntityInfo entityInfo);

    int batchInsert(List<EntityInfo> entityInfo);

    int updateByKey(EntityInfo entityInfo);

    int deleteByKey(EntityInfo entityInfo);

    Page<Map<String, Object>> query(EntityInfo entityInfo);

    Page<Map<String, Object>> queryByComplex(@Param("entityInfo") EntityInfo entityInfo, @Param("complexCond")ComplexCond complexCond);
}
