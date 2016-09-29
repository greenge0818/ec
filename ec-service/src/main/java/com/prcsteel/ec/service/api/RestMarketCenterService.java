package com.prcsteel.ec.service.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName: RestMarketCenterService
 * @Description: rest行情中心服务
 * @Author Tiny
 * @Date 2016年05月25日
 */

@Service
@RestApi(value = "restMarketCenterService", restServer = "esbServer")
public interface RestMarketCenterService {

    /**
     * @ClassName: getHotMarket
     * @Description: 获取热门行情
     * @Author Tiny
     * @Date 2016年05月25日
     */
    @RestMapping(value = "/news/hotlinks", method = RequestMethod.GET)
    String getHotMarket(@UrlParam("pagesize") Integer pagesize);

    /**
     * @ClassName: getSteelStatistics
     * @Description: 获取钢架汇总统计
     * @Author Tiny
     * @Date 2016年05月25日
     */
    @RestMapping(value = "/news/steel/prices", method = RequestMethod.GET)
    String getSteelStatistics(@UrlParam("pagesize") Integer pagesize);

    /**
     * @ClassName: getNewsArticles
     * @Description: 获取某个时间点之后的N条行情推送（咨）
     * @Author Tiny
     * @Date 2016年07月01日
     */
    @RestMapping(value = "/news/articles", method = RequestMethod.GET)
    String getNewsArticles(@UrlParam("maxid") Integer maxid, @UrlParam("pagesize") Integer pagesize);

    /**
     * @Author: Tiny
     * @Description: 获取掌柜指数
     * @Date: 2016年07月07日
     */
    @RestMapping(value = "/news/priceIndexes", method = RequestMethod.GET)
    String getPriceIndexes(@UrlParam("city") String city, @UrlParam("pagesize") Integer pagesize);

    /**
     * @Author: Tiny
     * @Description: 获取广告
     * @Date: 2016年07月07日
     */
    @RestMapping(value = "/ad/{posId}", method = RequestMethod.GET)
    String getAd(@UrlParam("posId") Integer posId);

    /**
     * @Author: Rabbit
     * @Description: 获取悬浮广告
     * @Date: 2016年7月29日
     */
    @RestMapping(value = "/ad/{posId}", method = RequestMethod.GET)
    String getFloatAd(@UrlParam("posId") Integer posId);

    /**
     * @Author: Tiny
     * @Description: 获取会员活动
     * @Date: 2016年07月07日
     */
    @RestMapping(value = "/news/activities", method = RequestMethod.GET)
    String getActivities(@UrlParam("pagesize") Integer pagesize);
}
