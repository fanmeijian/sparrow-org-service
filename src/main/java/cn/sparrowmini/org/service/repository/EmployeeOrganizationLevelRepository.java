package cn.sparrowmini.org.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.EmployeeOrganizationLevel;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationLevel.EmployeeOrganizationLevelPK;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevel.OrganizationPositionLevelPK;

public interface EmployeeOrganizationLevelRepository extends JpaRepository<EmployeeOrganizationLevel, EmployeeOrganizationLevelPK> {

	List<EmployeeOrganizationLevel> findByIdOrganizationLevelId(OrganizationPositionLevelPK organizationLevelId);

	List<EmployeeOrganizationLevel> findByIdEmployeeId(String employeeId);

}
