package com.prcsteel.ec.service.impl;

import com.prcsteel.ec.persist.dao.ec.GlobalIdDao;
import com.prcsteel.ec.model.domain.ec.GlobalId;
import com.prcsteel.ec.service.GlobalIdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Rolyer on 2016/4/26.
 */
@Service("GlobalIdService")
public class GlobalIdServiceImpl implements GlobalIdService {
    @Resource
    private GlobalIdDao globalIdDao;

    @Override
    public Long getId() {
        GlobalId globalId = new GlobalId();
        globalIdDao.insert(globalId);
        return globalId.getId();
    }
}
