package cn.sparrowmini.org.service.repository;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.sparrowmini.org.model.relation.EmployeeOrganizationRole;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationRole.EmployeeOrganizationRolePK;
import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;

public interface EmployeeOrganizationRoleRepository
		extends JpaRepository<EmployeeOrganizationRole, EmployeeOrganizationRolePK> {

	List<EmployeeOrganizationRole> findByIdOrganizationRoleId(OrganizationRolePK organizationRoleId);

	List<EmployeeOrganizationRole> findByIdEmployeeId(String employeeId);

	@Modifying
	@Transactional
	@Query("DELETE FROM EmployeeOrganizationRole r where r.id.organizationRoleId.roleId in (?1)")
	void deleteByRoleId(@NotNull String[] ids);

}
