package cn.sparrowmini.org.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.GroupUser;
import cn.sparrowmini.org.model.relation.GroupUser.GroupUserPK;
import org.springframework.stereotype.Repository;

@Repository("PemGroupUserRepository")
public interface GroupUserRepository extends JpaRepository<GroupUser, GroupUserPK> {

	Page<GroupUser> findByIdGroupId(String groupId, Pageable p);

}
