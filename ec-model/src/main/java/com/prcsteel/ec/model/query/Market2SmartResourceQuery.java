package com.prcsteel.ec.model.query;

import com.prcsteel.ec.core.model.Constant;

import java.io.Serializable;

/**
 * 超市至找货查找资源的查询对象
 *
 * @author peanut
 * @date 2016/5/19 19:05
 */
public class Market2SmartResourceQuery implements Serializable {

    /**
     * 品名uuid
     */
    private String categoryUuid;

    /**
     * 品名名称
     */
    private String categoryName;

    /**
     * 材质uuids(多个材质)
     */
    private String materialUuids;

    /**
     * 材质名称
     */
    private String materialNames;

    /**
     * 厂家ids（多选）
     */
    private String factoryIds;

    /**
     * 厂家名称（多选）
     */
    private String factoryNames;

    /**
     * 品规1（多选）
     */
    private String spec1;

    /**
     * 品规2（多选，可选区间）
     */
    private String spec2;

    /**
     * 品规3（多选，可选区间）
     */
    private String spec3;

    /**
     * 采购城市ID列表
     */
    private String purchaseCityIds;

    /**
     * 采购城市名称列表
     */
    private String purchaseCityNames;

    /**
     * 按某个字段排序 ，默认价格price
     */
    private String orderBy = Constant.DEFAULT_ORDERBY;

    /**
     * 顺排还是倒排 默认ASC从小到大；DESC 从大到小
     */
    private String orderWay = Constant.DEFAULT_ORDER_WAY;

    /**
     * 分页起始下标
     */
    private int start;

    /**
     * 页码
     */
    private int pageIndex = Constant.DEFAULT_PAGE_INDEX;

    /**
     * 一页条数
     */
    private int pageSize = Constant.MARKET_PAGE_SIZE;

    /**
     * 是否由采购城市转化为全国搜索
     */
    private boolean isSearchFromCity2Country = false;

    /**
     * 是否加入搜索历史
     */
    private boolean isSearchHistory = true;

    /**
     * tdk名称列表
     */
    private String tdkCityNames;

    private String cookieId;      //cookieId
    private String userGuid;      //userGuid


    public Market2SmartResourceQuery() {
    }

    public Market2SmartResourceQuery(String categoryUuid, String categoryName, String materialUuids, String materialNames,
                                     String factoryIds, String factoryNames, String spec1, String spec2, String spec3,
                                     String purchaseCityIds, String purchaseCityNames) {
        this.categoryUuid = categoryUuid;
        this.categoryName = categoryName;
        this.materialUuids = materialUuids;
        this.materialNames = materialNames;
        this.factoryIds = factoryIds;
        this.factoryNames = factoryNames;
        this.spec1 = spec1;
        this.spec2 = spec2;
        this.spec3 = spec3;
        this.purchaseCityIds = purchaseCityIds;
        this.purchaseCityNames = purchaseCityNames;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMaterialUuids() {
        return materialUuids;
    }

    public void setMaterialUuids(String materialUuids) {
        this.materialUuids = materialUuids;
    }

    public String getMaterialNames() {
        return materialNames;
    }

    public void setMaterialNames(String materialNames) {
        this.materialNames = materialNames;
    }

    public String getFactoryIds() {
        return factoryIds;
    }

    public void setFactoryIds(String factoryIds) {
        this.factoryIds = factoryIds;
    }

    public String getFactoryNames() {
        return factoryNames;
    }

    public void setFactoryNames(String factoryNames) {
        this.factoryNames = factoryNames;
    }

    public String getSpec1() {
        return spec1;
    }

    public void setSpec1(String spec1) {
        this.spec1 = spec1;
    }

    public String getSpec2() {
        return spec2;
    }

    public void setSpec2(String spec2) {
        this.spec2 = spec2;
    }

    public String getSpec3() {
        return spec3;
    }

    public void setSpec3(String spec3) {
        this.spec3 = spec3;
    }

    public String getPurchaseCityIds() {
        return purchaseCityIds;
    }

    public void setPurchaseCityIds(String purchaseCityIds) {
        this.purchaseCityIds = purchaseCityIds;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderWay() {
        return orderWay;
    }

    public void setOrderWay(String orderWay) {
        this.orderWay = orderWay;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getPurchaseCityNames() {
        return purchaseCityNames;
    }

    public void setPurchaseCityNames(String purchaseCityNames) {
        this.purchaseCityNames = purchaseCityNames;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public boolean getIsSearchFromCity2Country() {
        return this.isSearchFromCity2Country;
    }

    public void setIsSearchFromCity2Country(boolean searchFromCity2Country) {
        this.isSearchFromCity2Country = searchFromCity2Country;
    }

    public boolean getIsSearchHistory() {
        return isSearchHistory;
    }

    public void setIsSearchHistory(boolean isSearchHistory) {
        this.isSearchHistory = isSearchHistory;
    }

    public String getTdkCityNames() {
        return tdkCityNames;
    }

    public void setTdkCityNames(String tdkCityNames) {
        this.tdkCityNames = tdkCityNames;
    }

    /**
     * 查询之前调用，确定分页参数
     */
    public void preQuery() {
        if (this.pageIndex < 0) {
            this.pageIndex = Constant.DEFAULT_PAGE_INDEX;
        }
        if (this.pageSize < 0) {
            this.pageSize = Constant.MARKET_PAGE_SIZE;
        }
        this.start = this.pageIndex == 1 ? 0 : this.pageSize * (this.pageIndex - 1);
    }
}
