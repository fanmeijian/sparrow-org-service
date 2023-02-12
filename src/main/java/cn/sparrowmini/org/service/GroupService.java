package cn.sparrowmini.org.service;


import java.util.List;

import cn.sparrowmini.org.model.Employee;

public interface GroupService extends GroupRestService{

	public List<Employee> getFinalEmployees(String groupId);
	
}
