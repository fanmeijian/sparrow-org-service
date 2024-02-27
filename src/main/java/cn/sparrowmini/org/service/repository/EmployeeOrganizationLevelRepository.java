package cn.sparrowmini.org.service.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.sparrowmini.org.model.relation.EmployeeOrganizationLevel;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationLevel.EmployeeOrganizationLevelPK;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevel.OrganizationPositionLevelPK;

public interface EmployeeOrganizationLevelRepository
		extends JpaRepository<EmployeeOrganizationLevel, EmployeeOrganizationLevelPK> {

	List<EmployeeOrganizationLevel> findByIdOrganizationLevelId(OrganizationPositionLevelPK organizationLevelId);

	List<EmployeeOrganizationLevel> findByIdEmployeeId(String employeeId);

	@Modifying
	@Transactional
	@Query("DELETE FROM EmployeeOrganizationLevel r where r.id.organizationLevelId.positionLevelId in (?1)")
	void deleteByLevelId(String[] ids);

}
