package com.prcsteel.ec.model.query;

import com.prcsteel.ec.core.enums.ConsignOrderStatus;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.util.DateUtil;

import java.util.Date;

/**
 * 采购单查询条件
 * Created by myh on 2016/5/11.
 */
public class ConsignOrderQuery {
    private String status;  //状态
    private String timeFrom;  //起始时间
    private String timeTo;    //结束时间
    private String keyWord; //查询关键字(订单号或者品名)
    private String id;     //客户id
    private Integer pageIndex; //页码
    private Integer pageSize;  //单页记录数
    private Integer from;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getPageIndex() {
        return pageIndex.toString();
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from.toString();
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public String getPageSize() {
        return pageSize.toString();
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void preQuery(){
        if(ConsignOrderStatus.ALL.getCode().equals(this.status)){
            this.status = null;
        }
        this.pageSize = this.pageSize == null ? Constant.PAGE_SIZE : this.pageSize;
        this.from =  this.pageSize * (this.pageIndex - 1);
    }
}
