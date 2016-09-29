package com.prcsteel.ec.model.query;

import com.prcsteel.ec.core.model.Constant;

/**
 * Created by myh on 2016/5/16.
 */
public class SearchResourceQuery {
    private String categoryUuid;  //品名uuid
    private String categoryName;  //品名
    private String materialUuid;  //材质uuid
    private String materialName;  //材质
    private String factoryIds;    //厂家ids（多选）
    private String factoryNames;  //厂家
    private String spec1;         //品规1（多选）
    private String spec2;         //品规2（多选）
    private String spec3;         //品规3（多选）
    private String cityIds;       //城市ids（多选）
    private String cityNames;     //城市
    private String orderBy;       //排序
    private String cookieId;      //cookieId
    private String userGuid;      //userGuid
    //分页参数
    private Integer pageIndex;    //页码
    private Integer pageSize;     //单页记录数
    private Integer from;

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

    public String getMaterialUuid() {
        return materialUuid;
    }

    public void setMaterialUuid(String materialUuid) {
        this.materialUuid = materialUuid;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
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

    public String getCityIds() {
        return cityIds;
    }

    public void setCityIds(String cityIds) {
        this.cityIds = cityIds;
    }

    public String getCityNames() {
        return cityNames;
    }

    public void setCityNames(String cityNames) {
        this.cityNames = cityNames;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 查询之前调用，确定分页参数
     */
    public void preQuery(){
        this.pageSize = this.pageSize == null ? Constant.PAGE_SIZE : this.pageSize;
        this.from =  this.pageSize * (this.pageIndex - 1);
    }
}
