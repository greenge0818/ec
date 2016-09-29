package com.prcsteel.ec.service.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * rest 服务测试service
 *
 * @author peanut
 * @date 2016/5/20 9:37
 */
@Service
@RestApi(value = "restDemoService", restServer = "esbServer")
public interface RestDemoService {

    @RestMapping(value = "/basic/cities", method = RequestMethod.GET)
    String fetchCity();
}
