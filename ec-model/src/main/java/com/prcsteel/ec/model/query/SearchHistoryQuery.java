package com.prcsteel.ec.model.query;

import com.prcsteel.ec.core.model.Constant;

/**
 * Created by myh on 2016/5/16.
 */
public class SearchHistoryQuery {
    private Long id;
    private String cookieId;                       //cookieId
    private String userGuid;                       //userGuid
    private Integer size = Constant.PAGE_SIZE;     //获取记录数

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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
