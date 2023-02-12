package cn.sparrowmini.org.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.GroupEmployee;
import cn.sparrowmini.org.model.relation.GroupEmployee.GroupEmployeePK;

public interface GroupEmployeeRepository extends JpaRepository<GroupEmployee, GroupEmployeePK> {

	Page<GroupEmployee> findByIdGroupId(String groupId, Pageable pageable);

	Page<GroupEmployee> findByIdEmployeeId(String employeeId, Pageable pageable);

}
