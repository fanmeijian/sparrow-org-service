package cn.sparrowmini.org.service.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;
import cn.sparrowmini.org.model.relation.OrganizationRoleRelation;
import cn.sparrowmini.org.model.relation.OrganizationRoleRelation.OrganizationRoleRelationPK;

public interface OrganizationRoleRelationRepository
		extends JpaRepository<OrganizationRoleRelation, OrganizationRoleRelationPK> {

	List<OrganizationRoleRelation> findByIdParentId(@NotNull OrganizationRolePK parentId);

	List<OrganizationRoleRelation> findByIdId(@NotNull OrganizationRolePK organizationRolePK);

	long countByIdParentId(OrganizationRolePK id);

}
