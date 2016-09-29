package com.prcsteel.ec.model.dto;

import java.io.Serializable;

/**
 * @ClassName: ChangeContactStatusDto
 * @Description: CBMS禁用/启用联系人Dto
 * @Author Tiny
 * @Date 2016年07月01日
 */
public class ChangeContactStatusDto implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;                 //超市userId
    private Integer status;             //1启用0禁用

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
