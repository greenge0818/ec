package com.prcsteel.ec.service.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName: RestCbmsService
 * @Description: 超市调用CBMS
 * @Author Tiny
 * @Date 2016年06月08号
 */
@Service
@RestApi(value = "restCbmsService", restServer = "esbServer")
public interface RestCbmsService {

    /**
     * @ClassName: getFriendshipLink
     * @Description: 获取友情链接列表
     * @Author Tiny
     * @Date 2016年06月08日
     */
    @RestMapping(value = "/website/links", method = RequestMethod.GET)
    String getFriendshipLink(@UrlParam("count") Integer count, @UrlParam("type") Integer type);

    /**
     * 根据订单id获取订单信息
     * @return
     */
    @RestMapping(value = "//orders/queryByIds", method = RequestMethod.GET)
    String getOrdersByIds(@UrlParam("Ids") String ids);

    /**
     * 根据用戶id获取订单信息
     * @return
     */
    @RestMapping(value = "/orders/queryForMarket", method = RequestMethod.GET)

    String getOrders(@UrlParam("userId") String userId, @UrlParam("status") String status,
                     @UrlParam("startTime") String startTime, @UrlParam("endTime") String endTime,
                     @UrlParam("keyword") String keyword, @UrlParam("pageIndex") String pageIndex,
                     @UrlParam("pageSize") String pageSize);

    /**
     * 超市从CBMS获取订单分类总数信息
     * @param userId
     * @return
     */
    @RestMapping(value = "/orders/count/by/{userId}", method = RequestMethod.GET)
    String getOrderCount(@UrlParam("userId") String userId);

    /**
     * 根据超市用户id获取cbms用户所属公司名称
     *
     * @param userId 超市用户id
     * @Author peanut
     * @Date 2016年07月5日
     */
    @RestMapping(value = "/accounts/by/{userId}", method = RequestMethod.GET)
    String getCompanyNameByEcUserId(@UrlParam("userId") Integer userId);

    /**
     * 根据时间获取当前时间是否是工作日
     *
     * @param workDate 时间
     * @Author tiny
     * @Date 2016年09月21日
     */
    @RestMapping(value = "/orders/getHoliday", method = RequestMethod.GET)
    String getHoliday(@UrlParam("workDate") String workDate);
}
