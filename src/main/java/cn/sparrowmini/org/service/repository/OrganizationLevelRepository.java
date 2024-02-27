package cn.sparrowmini.org.service.repository;

import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.OrganizationPositionLevel;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevel.OrganizationPositionLevelPK;

public interface OrganizationLevelRepository extends JpaRepository<OrganizationPositionLevel, OrganizationPositionLevelPK> {

	Page<OrganizationPositionLevel> findByIdOrganizationId(String organizationId, Pageable pageable);

	long countByIdOrganizationId(String id);

	Iterable<OrganizationPositionLevel> findByIdPositionLevelId(@NotBlank String positionLevelId);

	void deleteByIdPositionLevelIdIn(String[] ids);

}
