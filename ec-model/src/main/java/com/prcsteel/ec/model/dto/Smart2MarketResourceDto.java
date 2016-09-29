package com.prcsteel.ec.model.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 超市与找货交互的资源Dto
 *
 * @author peanut
 * @date 2016/5/19 18:53
 */
public class Smart2MarketResourceDto implements Serializable {

    /**
     * 数据传输状态码
     */
    private int code;

    /**
     * 总记录数
     */
    private int total;

    /**
     * 资源列表
     */
    private List<ResourceDto> data;

    public Smart2MarketResourceDto() {}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ResourceDto> getData() {
        return data;
    }

    public void setData(List<ResourceDto> data) {
        this.data = data;
    }
}
