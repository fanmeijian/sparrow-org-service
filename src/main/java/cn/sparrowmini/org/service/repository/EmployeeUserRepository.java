package cn.sparrowmini.org.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.EmployeeUser;
import cn.sparrowmini.org.model.relation.EmployeeUser.EmployeeUserPK;

public interface EmployeeUserRepository extends JpaRepository<EmployeeUser, EmployeeUserPK> {
  EmployeeUser findOneByIdUsername(String username);
  List<EmployeeUser> findByIdEmployeeId(String employeeId);
}
