package com.prcsteel.ec.model.dto;

import java.math.BigDecimal;

/**
 * @Author:Green.Ge
 * @Description:返回给分拣系统的需求单item
 * @Date:2016/5/6.
 */
public class RequirementItemForPickDto {
    private String categoryID;
    private String categoryName;
    private String materialID;
    private String materialName;
    private String spec1;
    private String spec2;
    private String spec3;
    private Integer factoryID;
    private String factoryName;
    private Integer cityID;
    private String cityName;
    private BigDecimal price;
    private BigDecimal weight;
    private String weightConcept;
    private String remark;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMaterialID() {
        return materialID;
    }

    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
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

    public Integer getFactoryID() {
        return factoryID;
    }

    public void setFactoryID(Integer factoryID) {
        this.factoryID = factoryID;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getWeightConcept() {
        return weightConcept;
    }

    public void setWeightConcept(String weightConcept) {
        this.weightConcept = weightConcept;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
