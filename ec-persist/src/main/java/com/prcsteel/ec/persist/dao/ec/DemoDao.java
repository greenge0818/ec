package com.prcsteel.ec.persist.dao.ec;

import com.prcsteel.ec.model.domain.ec.Demo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemoDao {
	int insert(Demo demo);

	Demo queryUserById(Integer id);

	List<Demo> query();
}
