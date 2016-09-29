 package com.prcsteel.ec.core.model;


    /**
    * @ClassName: TaobaoIPResult
    * @Description: 根据IP查询到的淘宝API结果
    * @Author Green.Ge
    * @Date 2016年4月29日
    *
    */

public class TaobaoIPResult {
	private Integer code;
	private IPData data;
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public IPData getData() {
		return data;
	}

	public void setData(IPData data) {
		this.data = data;
	}
}
