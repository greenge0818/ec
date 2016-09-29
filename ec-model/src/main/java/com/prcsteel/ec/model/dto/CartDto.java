package com.prcsteel.ec.model.dto;

import java.util.List;

/**
 * Created by myh on 2016/4/28.
 */
public class CartDto {
    Long sellerId;
    String sellerName;
    List<ResourceDto> resourceList;

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

    public List<ResourceDto> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<ResourceDto> resourceList) {
        this.resourceList = resourceList;
    }
}
