package com.prcsteel.ec.model.domain.ec;

import com.prcsteel.ec.model.annotations.Column;
import com.prcsteel.ec.model.annotations.Entity;
import com.prcsteel.ec.model.annotations.KeyColumn;
import com.prcsteel.ec.model.common.DataEntity;

import java.math.BigDecimal;

@Entity("busi_requirement_item")
public class RequirementItem extends DataEntity {
    @KeyColumn(useGeneratedKeys = true)
    @Column(value = "id")
    private Long id;
    @Column(value = "requirement_guid")
    private String requirementGuid;
    @Column(value = "category_uuid")
    private String categoryUuid;
    @Column(value = "category_name")
    private String categoryName;
    @Column(value = "material_uuid")
    private String materialUuid;
    @Column(value = "material_name")
    private String materialName;
    @Column(value = "spec1")
    private String spec1;
    @Column(value = "spec2")
    private String spec2;
    @Column(value = "spec3")
    private String spec3;
    @Column(value = "factory_id")
    private Long factoryId;
    @Column(value = "factory_name")
    private String factoryName;
    @Column(value = "city_id")
    private Long cityId;
    @Column(value = "city_name")
    private String cityName;
    @Column(value = "warehouse_id")
    private Long warehouseId;
    @Column(value = "warehouse_name")
    private String warehouseName;
    @Column(value = "price")
    private BigDecimal price;
    @Column(value = "weight")
    private BigDecimal weight;
    @Column(value = "amount")
    private BigDecimal amount;
    @Column(value = "weight_concept")
    private String weightConcept;

    public RequirementItem() {
    }

    public RequirementItem(Cart cart) {
        this.categoryUuid = cart.getCategoryUuid();
        this.categoryName = cart.getCategoryName();
        this.materialName = cart.getMaterialName();
        this.materialUuid = cart.getMaterialUuid();
        this.spec1 = cart.getSpec1();
        this.spec2 = cart.getSpec2();
        this.spec3 = cart.getSpec3();
        this.factoryId = cart.getFactoryId();
        this.factoryName = cart.getFactoryName();
        this.cityId = cart.getCityId();
        this.cityName = cart.getCityName();
        this.warehouseId = cart.getWarehouseId();
        this.warehouseName = cart.getWarehouseName();
        this.price = cart.getPrice();
        this.weight = cart.getWeight();
        this.amount = cart.getAmount();
        this.weightConcept = cart.getWeightConcept();
    }

    public RequirementItem(APPOnceMore appOnceMore) {
        this.categoryUuid = appOnceMore.getCategoryUuid();
        this.materialUuid = appOnceMore.getMaterialUuid();
        String[] specs = appOnceMore.getSpec().split("\\*");
        this.spec1 = specs[0];
        if (specs.length > 1) {
            this.spec2 = specs[1];
        }
        if (specs.length > 2) {
            this.spec3 = specs[2];
        }
        this.factoryId = appOnceMore.getFactoryId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequirementGuid() {
        return requirementGuid;
    }

    public void setRequirementGuid(String requirementGuid) {
        this.requirementGuid = requirementGuid;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getWeightConcept() {
        return weightConcept;
    }

    public void setWeightConcept(String weightConcept) {
        this.weightConcept = weightConcept;
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
}