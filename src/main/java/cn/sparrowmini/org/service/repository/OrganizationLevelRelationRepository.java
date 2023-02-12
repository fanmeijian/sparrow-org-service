package cn.sparrowmini.org.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.OrganizationPositionLevel.OrganizationPositionLevelPK;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevelRelation;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevelRelation.OrganizationPositionLevelRelationPK;

public interface OrganizationLevelRelationRepository extends JpaRepository<OrganizationPositionLevelRelation,OrganizationPositionLevelRelationPK> {

	List<OrganizationPositionLevelRelation> findByIdParentId(OrganizationPositionLevelPK parentId);

	List<OrganizationPositionLevelRelation> findByIdId(OrganizationPositionLevelPK id);

}
