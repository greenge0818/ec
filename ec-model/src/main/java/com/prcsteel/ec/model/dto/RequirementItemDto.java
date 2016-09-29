package com.prcsteel.ec.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: RequirementItemDto
 * @Author Tiny
 * @Date 2016年05月04日
 */
public class RequirementItemDto implements Serializable{
    /**
     * 品名Uuid
     */
    private String categoryUuid;
    /**
     * 品名名称
     */
    private String categoryName;
    /**
     * 材质Uuid
     */
    private String materialUuid;
    /**
     * 材质名称
     */
    private String materialName;
    /**
     * 规格1
     */
    private String spec1;
    /**
     * 规格2
     */
    private String spec2;
    /**
     * 规格3
     */
    private String spec3;
    /**
     * 厂家Id
     */
    private Long factoryId;
    /**
     * 厂家名称
     */
    private String factoryName;
    /**
     * 城市Id
     */
    private Long cityId;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 采购量
     */
    private BigDecimal weight;
    /**
     * 订单金额（已报价之后才会有）
     */
    private BigDecimal amount;
    /**
     * 计重方式
     */
    private String weightConcept;
    /**
     * 仓库Id
     */
    private Long warehouseId;
    /**
     * 仓库
     */
    private String warehouseName;
    /**
     * 备注
     */
    private String remark;

    public RequirementItemDto() {
    }

    public RequirementItemDto(String categoryUuid, String categoryName, String materialUuid, String materialName, String spec1, String spec2, String spec3, Long factoryId, String factoryName, BigDecimal price, BigDecimal weight, Long warehouseId, String warehouseName) {
        this.categoryUuid = categoryUuid;
        this.categoryName = categoryName;
        this.materialUuid = materialUuid;
        this.materialName = materialName;
        this.spec1 = spec1;
        this.spec2 = spec2;
        this.spec3 = spec3;
        this.factoryId = factoryId;
        this.factoryName = factoryName;
        this.price = price;
        this.weight = weight;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
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

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
