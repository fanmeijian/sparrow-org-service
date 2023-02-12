package cn.sparrowmini.org.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.OrganizationGroup;
import cn.sparrowmini.org.model.relation.OrganizationGroup.OrganizationGroupPK;

public interface OrganizationGroupRepository extends JpaRepository<OrganizationGroup, OrganizationGroupPK> {

	long countByIdOrganizationId(String id);

	Page<OrganizationGroup> findByIdOrganizationId(String organizationId, Pageable pageable);

    Page<OrganizationGroup> findByIdGroupId(String groupId, Pageable pageable);
}
