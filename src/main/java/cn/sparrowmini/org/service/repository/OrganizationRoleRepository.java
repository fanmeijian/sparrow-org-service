package cn.sparrowmini.org.service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.OrganizationRole;
import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;

public interface OrganizationRoleRepository extends JpaRepository<OrganizationRole, OrganizationRolePK> {

	Page<OrganizationRole> findByIdOrganizationId(String organizationId, Pageable pageable);

	long countByIdOrganizationId(String id);

	List<OrganizationRole> findByIdRoleId(String roleId);

}
