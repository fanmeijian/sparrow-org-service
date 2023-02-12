package cn.sparrowmini.org.service.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cn.sparrowmini.org.model.relation.OrganizationRelation;
import cn.sparrowmini.org.model.relation.OrganizationRelation.OrganizationRelationPK;

public interface OrganizationRelationRepository
    extends JpaRepository<OrganizationRelation, OrganizationRelationPK> {
  List<OrganizationRelation> findByIdOrganizationId(String organizationId);
  List<OrganizationRelation> findByIdParentId(String parentId);
  Page<OrganizationRelation> findByIdParentId(String parentId, Pageable pageable);

  @Transactional
  void deleteByIdOrganizationIdInOrIdParentIdIn(String[] ids1, String[] ids2);

  long countByIdParentId(String id);

  long countByIdOrganizationId(String id);	

  @Transactional
  void deleteByIdIn(Set<OrganizationRelationPK> ids);

  void deleteByIdOrganizationId(String organizationId);
}
