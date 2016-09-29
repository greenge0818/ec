package com.prcsteel.ec.service.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName: RestPickService
 * @Description: 超市调用分检系统
 * @Author Tiny
 * @Date 2016年06月23日
 */
@Service
@RestApi(value = "restPickService", restServer = "esbServer")
public interface RestPickService {
    /**
     * 通过询价单ID，获取询价单明细
     * @param inquiryCodes
     * @return
     */
    @RestMapping(value = "inquiry/query", method = RequestMethod.GET)
    String getInquiryItems(@UrlParam("inquiryCodes") String inquiryCodes);

}
