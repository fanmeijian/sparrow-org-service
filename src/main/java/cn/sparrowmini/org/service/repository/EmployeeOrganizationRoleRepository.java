package cn.sparrowmini.org.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.EmployeeOrganizationRole;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationRole.EmployeeOrganizationRolePK;
import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;

public interface EmployeeOrganizationRoleRepository extends JpaRepository<EmployeeOrganizationRole, EmployeeOrganizationRolePK> {

	List<EmployeeOrganizationRole> findByIdOrganizationRoleId(OrganizationRolePK organizationRoleId);

	List<EmployeeOrganizationRole> findByIdEmployeeId(String employeeId);

}
