package com.prcsteel.ec.model.domain.ec;

import java.io.Serializable;
import java.util.Date;

/**
 * 需求单code生成器
 * Created by peanut on 2016/07/22.
 */
public class RequirementCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
