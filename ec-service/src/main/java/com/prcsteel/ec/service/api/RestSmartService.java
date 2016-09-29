package com.prcsteel.ec.service.api;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 智能找货rest服务service
 * Created by myh on 2016/5/26.
 */
@Service
@RestApi(value = "restSmartService", restServer = "esbServer")
public interface RestSmartService {
    /**
     * 获取品名
     *
     * @return
     */
    @RestMapping(value = "/basic/classInfos/V2", method = RequestMethod.GET)
    String getSortAndNsort();

    /**
     * 通过品名uuid获取材质
     *
     * @param categoryUuid
     * @return
     */
    @RestMapping(value = "/basic/materials", method = RequestMethod.GET)
    String getMaterial(@UrlParam("categoryUuid") String categoryUuid);

    /**
     * 通过品名uuid获取钢厂
     *
     * @param categoryUuid
     * @return
     */
    @RestMapping(value = "/basic/factories", method = RequestMethod.GET)
    String getFactory(@UrlParam("categoryUuid") String categoryUuid);

    /**
     * 通过品名uuid和材质uuid获取规格
     *
     * @param categoryUuid
     * @param materialsUuid
     * @return
     */
    @RestMapping(value = "/basic/specs", method = RequestMethod.GET)
    String getSpec(@UrlParam("categoryUuid") String categoryUuid, @UrlParam("materialsUuid") String materialsUuid);

    /**
     * 通过报价单ID，获取报价单明细
     *
     * @param quotationIds
     * @return
     */
    @RestMapping(value = "/quote/query", method = RequestMethod.GET)
    String getQuotationItems(@UrlParam("quotationIds") String quotationIds);

    /**
     * 通过搜索内容查询资源
     *
     * @param content
     * @return
     */
    @RestMapping(value = "/basic/resources/query", method = RequestMethod.GET)
    String queryResourceBySearch(@UrlParam("content") String content);

    /**
     * 根据地区分组获取所有中心城市
     *
     * @return
     */
    @RestMapping(value = "/basic/city/centers", method = RequestMethod.GET)
    String getAllCenterCity();

    /**
     * 获取热门资源
     *
     * @return
     */
    @RestMapping(value = "/basic/resources/hot/query", method = RequestMethod.POST)
    String getHotResource(String content);

    /**
     * 根据城市名称获取所有中心城市
     *
     * @param cityName
     * @return
     */
    @RestMapping(value = "/basic/city/centers/nearby")
    String getCenterCityByCityName(@UrlParam("cityName") String cityName);

    /**
     * get all category materials info, contains name and uuid
     *
     * @return
     * @author peanut
     * @date 2016/08/18
     */
    @RestMapping(value = "/basic/nsortmaterials" ,method = RequestMethod.GET)
    String getAllCategoryMaterials();
}
