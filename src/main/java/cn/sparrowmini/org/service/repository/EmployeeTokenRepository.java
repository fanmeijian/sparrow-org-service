package cn.sparrowmini.org.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.EmployeeToken;
import cn.sparrowmini.org.model.SparrowEmployeeToken;

public interface EmployeeTokenRepository extends JpaRepository<SparrowEmployeeToken, String> {
	EmployeeToken findOneByEmployeeId(String employeeId);

}
