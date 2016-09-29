
    /**  
    * @Title: BaiduIPResult.java
    * @Package com.prcsteel.ec.core.model
    * @Description: TODO(用一句话描述该文件做什么)
    * @author Green.Ge
    * @date 2016年4月29日
    * @version V1.0  
    */
    
package com.prcsteel.ec.core.model;


    /**
    * @ClassName: BaiduIPResult
    * @Description: 根据IP查询到的百度API结果
    * @Author Green.Ge
    * @Date 2016年4月29日
    *
    */

public class BaiduIPResult {
	private Integer errNum;
	private String errMsg;
	private IPData retData;
	
	public Integer getErrNum() {
		return errNum;
	}

	public void setErrNum(Integer errNum) {
		this.errNum = errNum;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public IPData getRetData() {
		return retData;
	}

	public void setRetData(IPData retData) {
		this.retData = retData;
	}
}
