package com.prcsteel.ec.service;

import com.prcsteel.ec.model.domain.ec.Demo;

import java.util.List;

public interface DemoService {
	void add(Demo demo);
	void delete(Demo demo);
	void update(Demo user);
	Demo queryUserById(Integer id);
	List<Demo> query(Demo demo, int pageNum, int pageSize, String... orderBys);
}
