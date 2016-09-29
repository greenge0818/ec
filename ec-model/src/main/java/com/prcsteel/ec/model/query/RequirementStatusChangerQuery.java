package com.prcsteel.ec.model.query;

import com.prcsteel.ec.model.model.RequirementStatusChanger;

/**
 * @ClassName: RequirementStatusChangerQuery
 * @Description: 用户外部系统修改需求单时传入对象的组装
 * @Author Tiny
 * @Date 2016年05月24日
 */
public class RequirementStatusChangerQuery extends RequirementStatusChanger  {
    private String source;              //数据来源：PICK分拣系统，SMART智能找货，CBMS

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
