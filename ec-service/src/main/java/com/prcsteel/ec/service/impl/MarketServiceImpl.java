package com.prcsteel.ec.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.model.RestResult;
import com.prcsteel.ec.core.service.CacheService;
import com.prcsteel.ec.core.util.StringUtil;
import com.prcsteel.ec.model.dto.*;
import com.prcsteel.ec.service.MarketService;
import com.prcsteel.ec.service.api.RestSmartService;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Rabbit on 2015-11-16 16:03:31.
 */
@Service("marketService")
public class MarketServiceImpl implements MarketService {

    Logger logger = LoggerFactory.getLogger(MarketServiceImpl.class);

    @Resource
    private CacheService cacheService;

    @Resource
    private RestSmartService restSmartService;

    private static final Gson gson = new Gson();

    /**
     * 获取品名
     *
     * @return
     */
    @Override
    public SmartVo getSortAndNsort() {
        SmartVo smartVo = (SmartVo) cacheService.get(Constant.CATEGORY_CACHE_KEY);
        if (smartVo == null) {
            smartVo = getNsortList();
            if (smartVo != null) {
                cacheService.set(Constant.CATEGORY_CACHE_KEY, Constant.DEFAULT_CACHE_EXPIRED, smartVo);
            }
        }
        return smartVo == null ? new SmartVo(MessageTemplate.RESOURCE_CATEGORY_GET_SUCCESS.getCode(), null) : smartVo;
    }

    /**
     * 从找货获取所有品名
     *
     * @return
     */
    private SmartVo getNsortList() {
        SmartVo smartVo = null;
        String smartResult;
        try {
            smartResult = restSmartService.getSortAndNsort();
        } catch (Exception e) {
            throw new BusinessException(MessageTemplate.SM_SERVER_ERROR.getCode(), MessageTemplate.SM_SERVER_ERROR.getMsg());
        }
        try {
            RestResult result = gson.fromJson(smartResult, RestResult.class);
            if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                List<Map<String, Object>> nsortList = gson.fromJson(gson.toJson(result.getData()), new TypeToken<List<Map<String, Object>>>() {
                }.getType());
                smartVo = new SmartVo(MessageTemplate.RESOURCE_CATEGORY_GET_SUCCESS.getCode(), nsortList);
            }
        } catch (JsonParseException jpe) {
            throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
        }
        return smartVo;
    }

    /**
     * 通过品名uuid获取材质
     *
     * @param categoryUUID
     * @return
     */
    @Override
    public SmartVo getMaterial(String categoryUUID) {
        SmartVo smartVo = (SmartVo) cacheService.get(Constant.MATERIAL_CACHE_KEY_PRXFIX + categoryUUID);
        if (smartVo == null) {
            //去找货获取对应品名的所有材质
            String smartResult;
            try {
                smartResult = restSmartService.getMaterial(categoryUUID);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.SM_SERVER_ERROR.getCode(), MessageTemplate.SM_SERVER_ERROR.getMsg());
            }
            try {
                RestResult result = gson.fromJson(smartResult, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                    LinkedList<Map<String, BaseDataDto>> materialList = new LinkedList<>(gson.fromJson(gson.toJson(result.getData()), new TypeToken<List<Map<String, BaseDataDto>>>() {
                    }.getType()));
                    smartVo = new SmartVo(MessageTemplate.RESOURCE_MATERIAL_GET_SUCCESS.getCode(), materialList);
                    cacheService.set(Constant.MATERIAL_CACHE_KEY_PRXFIX + categoryUUID, Constant.DEFAULT_CACHE_EXPIRED, smartVo);
                }
            } catch (JsonParseException jpe) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        }
        return smartVo == null ? new SmartVo(MessageTemplate.RESOURCE_MATERIAL_GET_SUCCESS.getCode(), null) : smartVo;
    }

    /**
     * 通过品名uuid获取钢厂（按首字母分类）
     *
     * @param categoryUUID
     * @return
     */
    @Override
    public SmartVo getFactory(String categoryUUID) {
        SmartVo result = getFactory2(categoryUUID);
        List<BaseDataDto> data = ListUtils.typedList(((ArrayList<Map>) result.getData()).stream().map(e -> e.get("factory")).collect(Collectors.toList()), BaseDataDto.class);
        Map<String, List<BaseDataDto>> groupResult = new TreeMap<>(data.stream().
                collect(Collectors.groupingBy(BaseDataDto::fetchFirstSpell, Collectors.toList())));  //按首字母分类
        result.setData(groupResult);
        return result;
    }

    /**
     * 通过品名uuid获取钢厂(不按首字母分类)
     *
     * @param categoryUUID
     * @return
     */
    @Override
    public SmartVo getFactory2(String categoryUUID) {
        SmartVo result = (SmartVo) cacheService.get(Constant.FACTORY_CACHE_KEY_PRXFIX + categoryUUID);
        if (result == null) {
            //去找货获取对应品名的所有钢厂
            String smartResult;
            try {
                smartResult = restSmartService.getFactory(categoryUUID);
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.SM_SERVER_ERROR.getCode(), MessageTemplate.SM_SERVER_ERROR.getMsg());
            }
            try {
                RestResult data = gson.fromJson(smartResult, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(data.getStatus())) {
                    List<Map<String, BaseDataDto>> factoryList = gson.fromJson(gson.toJson(data.getData()), new TypeToken<List<Map<String, BaseDataDto>>>() {
                    }.getType());
                    result = new SmartVo(MessageTemplate.RESOURCE_FACTORY_GET_SUCCESS.getCode(), factoryList);
                    cacheService.set(Constant.FACTORY_CACHE_KEY_PRXFIX + categoryUUID, Constant.DEFAULT_CACHE_EXPIRED, result);
                }
            } catch (JsonParseException jpe) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        }
        if (result == null) {
            return new SmartVo(MessageTemplate.RESOURCE_FACTORY_GET_SUCCESS.getCode(), null);
        }
        return result;
    }

    /**
     * 通过品名uuid和材质uuid获取规格
     *
     * @param categoryUUID
     * @param materialUUID
     * @return
     */
    @Override
    public SmartVo getSpec(String categoryUUID, String materialUUID) {
        //去找货获取对应品名，材质的所有规格
        String smartResult;
        try {
            smartResult = restSmartService.getSpec(categoryUUID, materialUUID);
        } catch (Exception e) {
            throw new BusinessException(MessageTemplate.SM_SERVER_ERROR.getCode(), MessageTemplate.SM_SERVER_ERROR.getMsg());
        }
        try {
            RestResult result = gson.fromJson(smartResult, RestResult.class);
            if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                Map<String, Object> specList = gson.fromJson(gson.toJson(result.getData()), new TypeToken<Map<String, Object>>() {
                }.getType());
                return new SmartVo(MessageTemplate.RESOURCE_SPEC_GET_SUCCESS.getCode(), specList);
            }
        } catch (JsonParseException jpe) {
            throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
        }
        return new SmartVo(MessageTemplate.RESOURCE_SPEC_GET_SUCCESS.getCode(), null);
    }

    /**
     * 根据品名uuid获取品名名称
     *
     * @param categoryUuid 品名uuid
     * @return
     * @author peanut
     * @date 2016/05/26
     */
    @Override
    public String getCategoryName(String categoryUuid) {
        if (StringUtils.isBlank(categoryUuid)) {
            return null;
        }
        SmartVo smartVo = (SmartVo) cacheService.get(Constant.CATEGORY_CACHE_KEY);
        if (smartVo == null) {
            smartVo = getNsortList();
            if (smartVo != null) {
                cacheService.set(Constant.CATEGORY_CACHE_KEY, Constant.DEFAULT_CACHE_EXPIRED, smartVo);
            }
        }
        if (smartVo == null) {
            return null;
        }
        List<CategoryCacheDto> categoryCacheDtoList;
        try {
            categoryCacheDtoList = new Gson().fromJson(new Gson().toJson(smartVo.getData()), new TypeToken<List<CategoryCacheDto>>() {
            }.getType());
        } catch (JsonParseException jpe) {
            throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
        }
        final String[] result = {null};
        if (categoryCacheDtoList != null) {
            categoryCacheDtoList.stream().forEach(a -> a.getClassInfo().stream().forEach(b -> {
                Optional<CategoryCacheDto.CategoryClass.Nsort> optional = b.getNsort().stream().filter(c -> c.getNsortID().equals(categoryUuid)).findFirst();
                if (optional.isPresent()) {
                    result[0] = optional.get().getNsortName();
                }
            }));
        }
        return result[0];
    }

    /**
     * 从找货或缓存中取得全国中心城市
     *
     * @return 城市对象
     * @author peanut
     * @date 2016/05/27
     */
    @Override
    public CitysDto getAllCitys() {
        //从缓存中取
        CitysDto citysDto = (CitysDto) cacheService.get(Constant.AREA_CACHE_KEY);
        if (citysDto == null) {

            citysDto = new CitysDto();
            citysDto.setCode("0");

            //从ESB调用服务取得城市
            String smartResult;
            try {
                smartResult = restSmartService.getAllCenterCity();
            } catch (Exception e) {
                throw new BusinessException(MessageTemplate.SM_SERVER_ERROR.getCode(), MessageTemplate.SM_SERVER_ERROR.getMsg());
            }
            try {
                RestResult result = gson.fromJson(smartResult, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {
                    List<CitysDto.Area> cityList = gson.fromJson(gson.toJson(result.getData()), new TypeToken<List<CitysDto.Area>>() {
                    }.getType());

                    citysDto.setData(cityList);
                /*数据缓存*/
                    cacheService.set(Constant.AREA_CACHE_KEY, Constant.DEFAULT_CACHE_EXPIRED, citysDto);
                } else {
                    citysDto.setData(null);
                }
            } catch (JsonParseException jpe) {
                throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
            }
        }
        return citysDto;
    }

    /**
     * 根据城市id集获取城市名称集
     *
     * @param cityIds 城市id集,如：1,2,3
     * @return 例如：cityIds:1,2,3 返回 北京,上海,杭州
     * @author peanut
     * @date 2016/06/07
     */
    @Override
    public String getCityNames(String cityIds) {

        if (StringUtils.isBlank(cityIds)) return null;

        List<String> cityIdsList = Arrays.asList(cityIds.split(","));
        //检验城市id数据格式
        Optional<String> optional = cityIdsList.stream().filter(e -> !StringUtils.isNumeric(e)).findAny();
        if (optional.isPresent()) {
            return null;
        }
        List<String> names = new ArrayList<>();
        //城市数据
        CitysDto citysDto = getAllCitys();
        if (citysDto != null) {
            citysDto.getData().stream().forEach(a -> names.addAll(
                    //过滤出id对应的城市名称
                    a.getCitys().stream().filter(
                            b -> cityIdsList.contains(b.getId().toString())
                    ).collect(Collectors.toList()).stream().map(e -> e.getName()).collect(Collectors.toList())));
        }
        return StringUtils.join(names, ",");
    }

    /**
     * 根据材质uuid集和品名uuid获取材质名称
     *
     * @param materialUuids 材质uuid
     * @param categoryUuid  品名uuid
     * @return 例：materialUuids：1343e43sss...，2erere34343 返回 Q2B5H，Q235B
     * @author peanut
     * @date 2016/05/30
     */
    @Override
    public String getMaterialNames(String materialUuids, String categoryUuid) {
        if (StringUtils.isBlank(materialUuids) || StringUtils.isBlank(categoryUuid)) return null;
        return getNames(getMaterial(categoryUuid), materialUuids);
    }

    /**
     * 根据钢厂id集和品名uuid获取钢厂名称
     *
     * @param facotryIds   钢厂uuid
     * @param categoryUuid 品名uuid
     * @return 例：facotryIds：1，2 返回 中天，鞍钢
     * @author peanut
     * @date 2016/05/30
     */
    @Override
    public String getFactoryNames(String facotryIds, String categoryUuid) {
        if (StringUtils.isBlank(facotryIds) || StringUtils.isBlank(categoryUuid)) return null;
        return getNames(getFactory(categoryUuid), facotryIds);
    }

    /**
     * 根据用户ip地址取得所属城市的中心城市id集 ---从找货拿
     *
     * @param ip ip地址
     * @return 例，ip:61.153.142.0 属于宁波，宁波的中心城市为杭州，返回杭州
     * @author peanut
     * @date 2016/06/01
     */
    @Override
    public String getCenterCitysByIp(String ip) {
        if (StringUtils.isBlank(ip)) return null;
        //ip 所属城市
        String cityName = StringUtil.getCityByIp(ip);
        String smartResult;
        try {
            smartResult = restSmartService.getCenterCityByCityName(cityName);
        } catch (Exception e) {
            throw new BusinessException(MessageTemplate.SM_SERVER_ERROR.getCode(), MessageTemplate.SM_SERVER_ERROR.getMsg());
        }
        try {
            RestResult restResult = gson.fromJson(smartResult, RestResult.class);
            if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(restResult.getStatus())) {
                List<Map<String, Object>> cityList = gson.fromJson(gson.toJson(restResult.getData()), new TypeToken<List<Map<String, Object>>>() {
                }.getType());
                cityList.stream().forEach(e -> e.put("id", (long) (double) e.get("id")));
                return StringUtils.join(cityList.stream().map(e -> e.get("id")).collect(Collectors.toList()), ",");
            }
        } catch (JsonParseException jpe) {
            throw new BusinessException(MessageTemplate.JSON_PARSE_ERROR.getCode(), MessageTemplate.JSON_PARSE_ERROR.getMsg());
        }
        return null;
    }

    private String getNames(SmartVo smartVo, String ids) {
        if (smartVo != null && StringUtils.isNotBlank(ids)) {
            Object o = smartVo.getData();
            if (o == null) {
                return null;
            }
            if (o instanceof LinkedList) {
                List<String> result = new ArrayList<>();
                List<Map<String, BaseDataDto>> list = (LinkedList) o;
                //名称按id集顺序
                Arrays.asList(ids.split(",")).stream().forEach(e -> {
                    Optional<String> optional = list.stream().filter(n -> n.get("material").getUuid().equals(e)).map(m -> m.get("material").getName()).findAny();
                    if (optional.isPresent()) {
                        result.add(optional.get());
                    }
                });
                // String[] namesArray = list.stream().filter(e -> ArrayUtils.contains(e.get("material").getUuid(),ids.split(","))).map(e -> e.get("material").getName()).toArray(String[]::new);
                return StringUtils.join(result, ",");
            } else {
                Map<String, List<BaseDataDto>> map = (Map) o;
                if (map != null && !map.isEmpty()) {
                    List<String> namesList = new ArrayList<>();
                    Arrays.asList(ids.split(",")).stream().forEach(n -> {
                        for (Map.Entry<String, List<BaseDataDto>> bentry : map.entrySet()) {
                            Optional<String> optional = bentry.getValue().stream().filter(e -> e.getUuid().equals(n)).map(e -> e.getName()).findAny();
                            if (optional.isPresent()) {
                                namesList.add(optional.get());
                            }
                        }
                    });
                    return StringUtils.join(namesList, ",");
                }
            }
        }
        return null;
    }

    /**
     * get all category materials info, contains name and uuid
     *
     * @return
     * @author peanut
     * @date 2016/08/18
     */
    @Override
    public List<CategoryMaterials> getAllCategoryMaterials() {

        /*** get data from cache */
        Object o = cacheService.get(Constant.CATEGORY_MATERIALS_CACHE_KEY);
        List<CategoryMaterials> list;
        if (o != null) {
            list = (List<CategoryMaterials>) o;
        } else {
            String content = restSmartService.getAllCategoryMaterials();
            if (StringUtils.isBlank(content)) {
                logger.error("========category materials info load error: result content is empty ！");
                return null;
            } else {

                RestResult result = gson.fromJson(content, RestResult.class);
                if (MessageTemplate.OPERATE_SUCCESS.getCode().equals(result.getStatus())) {

                    list = gson.fromJson(gson.toJson(result.getData()), new TypeToken<List<CategoryMaterials>>() {
                    }.getType());

                    cacheService.set(Constant.CATEGORY_MATERIALS_CACHE_KEY, Constant.DEFAULT_CACHE_EXPIRED, list);
                } else {
                    logger.error("========category materials info load error: result status return is not 0 ！");
                    return null;
                }
            }
        }
        return list;
    }
}