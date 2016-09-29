package com.prcsteel.ec.model.query;

/**
 * @ClassName: RequirementQuery
 * @Author Tiny
 * @Date 2016年05月05日
 */
public class RequirementQuery {
    /**
     * 搜索开始时间
     */
    private String startTime;
    /**
     * 搜索结束时间
     */
    private String endTime;
    /**
     * 需求单状态
     */
    private String stageStatus;

    /**
     * 用户Guid
     */
    private String userGuid;

    /**
     * 开始
     */
    private Integer start;

    /**
     * 长度
     */
    private Integer length;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStageStatus() {
        return stageStatus;
    }

    public void setStageStatus(String stageStatus) {
        this.stageStatus = stageStatus;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
