package com.prcsteel.ec.service.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName: RestCommonService
 * @Description: 公共的rest服务
 * @Author Tiny
 * @Date 2016年06月12日
 */
@Service
@RestApi(value = "restCommonService", restServer = "esbServer")
public interface RestCommonService {

    /**
     * @ClassName: send
     * @Description: 发短信
     * @Author Tiny
     * @Date 2016年06月12日
     */
    @RestMapping(value = "/sms/send/V2", method = RequestMethod.POST)
    String send(@UrlParam("timestamp") String timestamp, @UrlParam("token") String token,@UrlParam("phone") String phone, @UrlParam("content") String content, @UrlParam("from") String from);
}
