package cn.sparrowmini.org.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.GroupOrganization;
import cn.sparrowmini.org.model.relation.GroupOrganization.GroupOrganizationPK;

public interface GroupOrganizationRepository extends JpaRepository<GroupOrganization, GroupOrganizationPK> {

	
	Page<GroupOrganization> findByIdGroupId(String groupId, Pageable pageable);

}
