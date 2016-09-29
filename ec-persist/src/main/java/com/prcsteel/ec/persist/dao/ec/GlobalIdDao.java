package com.prcsteel.ec.persist.dao.ec;


import com.prcsteel.ec.model.domain.ec.GlobalId;
import org.springframework.stereotype.Repository;

/**
 * Created by Rolyer on 2016/4/26.
 */
@Repository
public interface GlobalIdDao {
    int insert(GlobalId globalId);
}
