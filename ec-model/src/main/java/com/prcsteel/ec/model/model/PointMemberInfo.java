package com.prcsteel.ec.model.model;

/**
 * A entity for the Point System MemberInfo
 */

public class PointMemberInfo {
	private String openId;

	private String phone;

    private String name;

    private String area;

    private Integer point;

    private String level;

    private String orgName;

	public PointMemberInfo() {
	}

	public String getOpenId() {
		return openId;
	}


	public void setOpenId(String openId) {
		this.openId = openId;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public Integer getPoint() {
		return point;
	}


	public void setPoint(Integer point) {
		this.point = point;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
