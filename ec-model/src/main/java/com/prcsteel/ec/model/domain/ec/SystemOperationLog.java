package com.prcsteel.ec.model.domain.ec;

import com.prcsteel.ec.core.enums.OpLevel;
import com.prcsteel.ec.core.enums.OpType;
import com.prcsteel.ec.model.annotations.Column;
import com.prcsteel.ec.model.annotations.Entity;
import com.prcsteel.ec.model.annotations.KeyColumn;
import com.prcsteel.ec.model.common.DataEntity;

/**
 * @author peanut
 * @description 系统操作日志实体对象
 * @date 2016/4/29 9:48
 */
@Entity("base_system_opration_log")
public class SystemOperationLog extends DataEntity<SystemOperationLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主健id
     */
    @KeyColumn(useGeneratedKeys = true)
    @Column("id")
    private Long id;

    /**
     * 操作人guid
     */
    @Column("operator_guid")
    private String operatorGuid;

    /**
     * 操作人名称
     */
    @Column("operator_name")
    private String operatorName;

    /**
     * 操作类型的key
     */
    @Column("operation_key")
    private OpType operationKey;

    /**
     * 操作名称
     */
    @Column("operation_name")
    private String operationName;

    /**
     * 操作级别：Safe,Warning,Dangerous,Damning
     */
    @Column("operation_level")
    private OpLevel operationLevel;

    /**
     * 操作等级的数字值，值越高表示越危险，用于按危险等级排序
     */
    @Column("operation_level_value")
    private Integer operationLevelValue;

    /**
     * 提交的参数
     */
    @Column("parameters")
    private String parameters;

    public SystemOperationLog() {
    }

    public SystemOperationLog(String operatorGuid, String operatorName, OpType operationKey, String operationName,
                              OpLevel operationLevel, Integer operationLevelValue, String parameters, String createdBy, String lastUpdateBy) {
        this.operatorGuid = operatorGuid;
        this.operatorName = operatorName;
        this.operationKey = operationKey;
        this.operationName = operationName;
        this.operationLevel = operationLevel;
        this.operationLevelValue = operationLevelValue;
        this.parameters = parameters;
        this.setCreatedBy(createdBy);
        this.setLastUpdatedBy(lastUpdateBy);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperatorGuid() {
        return operatorGuid;
    }

    public void setOperatorGuid(String operatorGuid) {
        this.operatorGuid = operatorGuid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public OpType getOperationKey() {
        return operationKey;
    }

    public void setOperationKey(OpType operationKey) {
        this.operationKey = operationKey;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public OpLevel getOperationLevel() {
        return operationLevel;
    }

    public void setOperationLevel(OpLevel operationLevel) {
        this.operationLevel = operationLevel;
    }

    public Integer getOperationLevelValue() {
        return operationLevelValue;
    }

    public void setOperationLevelValue(Integer operationLevelValue) {
        this.operationLevelValue = operationLevelValue;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
