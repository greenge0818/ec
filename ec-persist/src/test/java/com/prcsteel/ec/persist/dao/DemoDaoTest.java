package com.prcsteel.ec.persist.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.prcsteel.ec.model.domain.ec.Demo;
import com.prcsteel.ec.persist.dao.ec.DemoDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-spring-web.xml",
		"classpath:test-spring-mybatis.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("classpath:/data/DemoDaoTest.xml")
@ActiveProfiles("hsqldb")
public class DemoDaoTest {

	@Resource
	private DemoDao demoDao;

	@Test
	public void insert() {

		Demo demo = new Demo("account", "password", "", new Date(), new Date());

		Assert.assertNull(demo.getGuid());

		demo.preInsert(854774807L);
		demoDao.insert(demo);

		Assert.assertEquals(demo.getGuid(), "GUID_00000vqXyR");
	}
	
	@Test
	public void queryUserById() {
		Demo demo = demoDao.queryUserById(1);
		
		Assert.assertNotNull(demo);
	}

	@Test
	public void query() {
		List<Demo> demos = demoDao.query();

		Assert.assertNotNull(demos);
	}
}
