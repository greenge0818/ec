package com.prcsteel.ec.model.domain.ec;

import com.prcsteel.ec.model.dto.RequirementItemDto;
import org.apache.commons.lang.StringUtils;

/**
 * @Auther:Green.Ge
 * @Description:超市返回给app端的再来一单model
 * @Date:2016-05-30
 */
public class APPOnceMore {
    String categoryUuid;//品名
    String categoryName;
    String materialUuid;
    String materialName;
    Long factoryId;
    String factoryName;
    String spec;

    public APPOnceMore() {
    }

    public APPOnceMore(RequirementItemDto requirementItemDto) {
        this.categoryUuid = requirementItemDto.getCategoryUuid();
        this.categoryName = requirementItemDto.getCategoryName();
        this.materialUuid = requirementItemDto.getMaterialUuid();
        this.materialName = requirementItemDto.getMaterialName();
        this.factoryId = requirementItemDto.getFactoryId();
        this.factoryName = requirementItemDto.getFactoryName();
        this.spec = requirementItemDto.getSpec1()
                + (StringUtils.isNotBlank(requirementItemDto.getSpec2()) ? "*" + requirementItemDto.getSpec2() : "")
                + (StringUtils.isNotBlank(requirementItemDto.getSpec3()) ? "*" + requirementItemDto.getSpec3() : "");
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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()){
            return false;
        }
        APPOnceMore s = (APPOnceMore) obj;
        return categoryUuid.equals(s.categoryUuid) && materialUuid.equals(s.materialUuid)
                && factoryId.equals(s.factoryId) && spec.equals(s.spec);
    }

    /**
     * 重写hashCode用于去重
     *
     * @return
     */
    @Override
    public int hashCode() {
        String code = categoryUuid + materialUuid + factoryId + spec;
        return code.hashCode();
    }
}
