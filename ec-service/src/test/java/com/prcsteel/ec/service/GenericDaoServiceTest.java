
package com.prcsteel.ec.service;

import com.prcsteel.ec.model.domain.ec.Demo;
import com.prcsteel.ec.model.domain.ec.generic.EntityInfo;
import com.prcsteel.ec.model.dto.Page;
import com.prcsteel.ec.persist.dao.ec.GenericMybatisDao;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Created by Rolyer on 2016/5/5.
 */
public class GenericDaoServiceTest extends BaseTest {

    @Autowired
    private GenericDaoService genericDaoService;

    private GenericMybatisDao genericDao;

    @Before
    public void before() {
        genericDao = EasyMock.createMock(GenericMybatisDao.class);

        ReflectionTestUtils.setField(genericDaoService, "genericDao", genericDao);
    }

    @After
    public void after() {
        genericDao = null;
    }

    @Test
    public void findAll() {

        EasyMock.expect(genericDao.query(EasyMock.anyObject(EntityInfo.class))).andReturn(new Page<>());
        EasyMock.replay(genericDao);

        Demo demo = new  Demo();
        genericDaoService.findAll(demo);



        EasyMock.verify(genericDao);
    }
}
