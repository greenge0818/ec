package com.prcsteel.ec.model.query;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: MarketToSmartHotResourceQuery
 * @Description: 超市到找货热门资源查询对象
 * @Author Tiny
 * @Date 2016年06月30日
 */
public class MarketToSmartHotResourceQuery implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 客户端所在城市名称
     */
    private String specificCityName;

    /**
     * 资源条数
     */
    private Integer length;

    /**
     * 搜索记录明细字符串
     */
    private List<Market2SmartResourceQuery> items;

    public MarketToSmartHotResourceQuery() {
    }

    public MarketToSmartHotResourceQuery(String specificCityName, Integer length, List<Market2SmartResourceQuery> items) {
        this.specificCityName = specificCityName;
        this.length = length;
        this.items = items;
    }

    public String getSpecificCityName() {
        return specificCityName;
    }

    public void setSpecificCityName(String specificCityName) {
        this.specificCityName = specificCityName;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Market2SmartResourceQuery> getItems() {
        return items;
    }

    public void setItems(List<Market2SmartResourceQuery> items) {
        this.items = items;
    }
}
