package com.prcsteel.ec.service.impl;

import com.prcsteel.ec.model.domain.ec.Demo;
import com.prcsteel.ec.persist.dao.ec.DemoDao;
import com.prcsteel.ec.service.DemoService;
import com.prcsteel.ec.service.GenericDaoService;
import com.prcsteel.ec.service.GlobalIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Rolyer on 2016/4/29.
 */
@Service("demoService")
public class DemoServiceImpl implements DemoService {
    @Resource
    private DemoDao demoDao;

    @Resource
    private GlobalIdService globalIdService;

    @Autowired
    private GenericDaoService genericDaoService;

    @Override
    public void add(Demo demo) {
        demo.preInsert(globalIdService.getId());
        genericDaoService.insert(demo);
    }

    @Override
    public void delete(Demo demo) {
        genericDaoService.deleteByKey(demo);
    }

    @Override
    public void update(Demo demo) {
        demo.preUpdate();
        genericDaoService.updateByKey(demo);
    }

    @Override
    public Demo queryUserById(Integer id) {
        return demoDao.queryUserById(id);
    }

    @Override
    public List<Demo> query(Demo demo, int pageNum, int pageSize, String... orderBys) {
        List<Demo> list = genericDaoService.findAll(demo, pageNum, pageSize, orderBys);
        return list;
    }
}
