package com.prcsteel.ec.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Created by Rolyer on 2016/5/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-spring-service.xml",
        "classpath:spring-persist.xml",
        "classpath:spring-mybatis.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class BaseTest {

    @Test
    public void test(){
    }

}

