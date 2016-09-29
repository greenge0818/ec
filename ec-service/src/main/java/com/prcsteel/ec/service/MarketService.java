package com.prcsteel.ec.service;

import com.prcsteel.ec.model.dto.CategoryMaterials;
import com.prcsteel.ec.model.dto.CitysDto;
import com.prcsteel.ec.model.dto.SmartVo;

import java.util.List;

/**
 * Created by Rabbit on 2015-11-16 15:59:23.
 */
public interface MarketService {
    /**
     * 获取品名
     *
     * @return
     */
    SmartVo getSortAndNsort();

    /**
     * 通过品名uuid获取材质
     *
     * @param categoryUUID
     * @return
     */
    SmartVo getMaterial(String categoryUUID);

    /**
     * 通过品名uuid获取钢厂（按首字母分类）
     *
     * @param categoryUUID
     * @return
     */
    SmartVo getFactory(String categoryUUID);

    /**
     * 通过品名uuid获取钢厂(不按首字母分类)
     *
     * @param categoryUUID
     * @return
     */
    SmartVo getFactory2(String categoryUUID);

    /**
     * 通过品名uuid和材质uuid获取规格
     *
     * @param categoryUUID
     * @param materialUUID
     * @return
     */
    SmartVo getSpec(String categoryUUID, String materialUUID);

    /**
     * 根据品名uuid获取品名名称
     *
     * @param categoryUuid
     * @return
     * @author peanut
     * @date 2016/05/26
     */
    String getCategoryName(String categoryUuid);

    /**
     * 从cbms或缓存中取得全国城市
     *
     * @return 城市对象
     * @author peanut
     * @date 2016/05/27
     */
    CitysDto getAllCitys();

    /**
     * 根据城市id集获取城市名称集
     *
     * @param cityIds 城市id集,如：1,2,3
     * @return 例如：cityIds:1,2,3 返回 北京,上海,杭州
     * @author peanut
     * @date 2016/06/07
     */
    String getCityNames(String cityIds);


    /**
     * 根据材质uuids集和品名uuid获取材质名称
     *
     * @param materialUuids 材质uuid
     * @param categoryUuid  品名uuid
     * @return
     * @author peanut
     * @date 2016/05/30
     */
    String getMaterialNames(String materialUuids, String categoryUuid);

    /**
     * 根据钢厂id集和品名uuid获取材质名称
     *
     * @param facotryIds   钢厂uuid
     * @param categoryUuid 品名uuid
     * @return
     * @author peanut
     * @date 2016/05/30
     */
    String getFactoryNames(String facotryIds, String categoryUuid);

    /**
     * 根据用户ip地址取得所属城市的中区城市id集
     *
     * @param ip ip地址
     * @return
     * @author peanut
     * @date 2016/06/01
     */
    String getCenterCitysByIp(String ip);

    /**
     * get all category materials info, contains name and uuid
     *
     * @return
     * @author peanut
     * @date 2016/08/18
     */
    List<CategoryMaterials> getAllCategoryMaterials();
}
