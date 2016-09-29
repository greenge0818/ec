package com.prcsteel.ec.model.domain.ec;

import java.math.BigDecimal;

public class ConsignOrderItemsDto {
    private Long id;

    private Long orderId;

    private String nsortName;

    private String material;

    private String spec;

    private String factory;

    private String city;

    private String warehouse;

    private BigDecimal weight;

    private String weightConcept;

    private BigDecimal dealPrice;

    private BigDecimal actualPickWeight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getNsortName() {
        return nsortName;
    }

    public void setNsortName(String nsortName) {
        this.nsortName = nsortName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getWeightConcept() {
        return weightConcept;
    }

    public void setWeightConcept(String weightConcept) {
        this.weightConcept = weightConcept;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public BigDecimal getActualPickWeight() {
        return actualPickWeight;
    }

    public void setActualPickWeight(BigDecimal actualPickWeight) {
        this.actualPickWeight = actualPickWeight;
    }
}