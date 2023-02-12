package cn.sparrowmini.org.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.OrganizationPositionLevel.OrganizationPositionLevelPK;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevelRelation;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevelRelation.OrganizationPositionLevelRelationPK;

public interface OrganizationPositionLevelRelationRepository extends JpaRepository<OrganizationPositionLevelRelation, OrganizationPositionLevelRelationPK> {

	long countByIdParentId(OrganizationPositionLevelPK id);

}
