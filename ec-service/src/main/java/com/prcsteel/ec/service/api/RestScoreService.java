package com.prcsteel.ec.service.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 超市调用积分系统 ESB Service
 *
 * @author peanut
 * @date 2016/6/6 10:08
 */
@Service
@RestApi(value = "restScoreService", restServer = "esbServer")
public interface RestScoreService {

    /**
     * 根据手机号获取用户积分
     *
     * @param token
     * @param timestamp 时间戳
     * @param code      用户手机号
     * @return 例：code:13336205489 ,返回对应积分:1500
     * @author peanut
     * @date 2016/6/6
     */
    @RestMapping(value = "/points/query", method = RequestMethod.GET)
    String getScoreByMobile(@UrlParam("token") String token, @UrlParam("timestamp") String timestamp, @UrlParam("code") String code);

}
