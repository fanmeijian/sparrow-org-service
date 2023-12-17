package cn.sparrowmini.org.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.GroupSysrole;
import cn.sparrowmini.org.model.relation.GroupSysrole.GroupSysrolePK;
import org.springframework.stereotype.Repository;

@Repository("PemGroupSysroleRepository")
public interface GroupSysroleRepository extends JpaRepository<GroupSysrole, GroupSysrolePK> {

	Page<GroupSysrole> findByIdGroupId(String groupId, Pageable pageable);

}
