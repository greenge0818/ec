package com.prcsteel.ec.model.domain.ec;

import com.prcsteel.ec.model.annotations.Column;
import com.prcsteel.ec.model.annotations.Entity;
import com.prcsteel.ec.model.annotations.KeyColumn;
import com.prcsteel.ec.model.common.DataEntity;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Entity("mkt_cart")
public class Cart extends DataEntity {
    @KeyColumn(useGeneratedKeys = true)
    @Column(value = "id")
    private Long id;
    @Column(value = "cookie_id")
    private String cookieId;
    @Column(value = "user_guid")
    private String userGuid;
    @Column(value = "seller_id")
    private Long sellerId;
    @Column(value = "seller_name")
    private String sellerName;
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
    @Column(value = "is_checked")
    @Pattern(regexp = "^[N|Y]$", message = "4001")
    private String isChecked;

    public Cart() {
    }

    public Cart(Long id) {
        this.id = id;
    }

    public Cart(String guid, Long id, String cookieId, String userGuid, Long sellerId, String sellerName, String categoryUuid,
                String categoryName, String materialUuid, String materialName, String spec1, String spec2, String spec3,
                Long factoryId, String factoryName, Long cityId, String cityName, Long warehouseId, String warehouseName, BigDecimal price,
                BigDecimal weight, BigDecimal amount, String weightConcept, String isChecked) {
        super(guid);
        this.id = id;
        this.cookieId = cookieId;
        this.userGuid = userGuid;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.categoryUuid = categoryUuid;
        this.categoryName = categoryName;
        this.materialUuid = materialUuid;
        this.materialName = materialName;
        this.spec1 = spec1;
        this.spec2 = spec2;
        this.spec3 = spec3;
        this.factoryId = factoryId;
        this.factoryName = factoryName;
        this.cityId = cityId;
        this.cityName = cityName;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.price = price;
        this.weight = weight;
        this.amount = amount;
        this.weightConcept = weightConcept;
        this.isChecked = isChecked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
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

    public String isChecked() {
        return isChecked;
    }

    public void setChecked(String checked) {
        isChecked = checked;
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