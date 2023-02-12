package cn.sparrowmini.org.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.GroupRole;
import cn.sparrowmini.org.model.relation.GroupRole.GroupRolePK;

public interface GroupRoleRepository extends JpaRepository<GroupRole, GroupRolePK> {

	Page<GroupRole> findByIdGroupId(String groupId, Pageable pageable);

}
