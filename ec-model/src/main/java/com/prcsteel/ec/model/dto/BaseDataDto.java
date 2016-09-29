package com.prcsteel.ec.model.dto;

import com.prcsteel.ec.core.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.stream.Collector;

public class BaseDataDto  implements Serializable {
	private String uuid;
	
	private String name;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public BaseDataDto() {
	}

	public String fetchFirstSpell(){
		String firstSpell = StringUtil.getFirstSpell(this.name);
		if(StringUtils.isBlank(firstSpell)){
			return "";
		}
		return firstSpell.substring(0, 1).toUpperCase();
	}

	public void setName(String name) {
		this.name = name;
	}
}
