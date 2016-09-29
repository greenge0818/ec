package com.prcsteel.ec.model.domain.ec;

import com.prcsteel.ec.model.annotations.Column;
import com.prcsteel.ec.model.annotations.Entity;
import com.prcsteel.ec.model.annotations.KeyColumn;
import com.prcsteel.ec.model.common.DataEntity;

@Entity("mkt_search_history")
public class SearchHistory extends DataEntity{
    @KeyColumn(useGeneratedKeys=true)
    @Column(value = "id")
    private Long id;

    @Column(value = "cookie_id")
    private String cookieId;

    @Column(value = "user_guid")
    private String userGuid;

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
    private String factoryId;

    @Column(value = "factory_name")
    private String factoryName;

    @Column(value = "city_id")
    private String cityId;

    @Column(value = "city_name")
    private String cityName;

    @Column(value = "is_cookie_deleted")
    private String isCookieDeleted = "N";

    @Column(value = "is_user_deleted")
    private String isUserDeleted = "N";

    @Column(value = "is_deleted_for_preference", insertIfNull = "N")
    private String isDeletedForPreference = "N";

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

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIsDeletedForPreference() {
        return isDeletedForPreference;
    }

    public void setIsDeletedForPreference(String isDeletedForPreference) {
        this.isDeletedForPreference = isDeletedForPreference;
    }

    public String getIsCookieDeleted() {
        return isCookieDeleted;
    }

    public void setIsCookieDeleted(String isCookieDeleted) {
        this.isCookieDeleted = isCookieDeleted;
    }

    public String getIsUserDeleted() {
        return isUserDeleted;
    }

    public void setIsUserDeleted(String isUserDeleted) {
        this.isUserDeleted = isUserDeleted;
    }
}