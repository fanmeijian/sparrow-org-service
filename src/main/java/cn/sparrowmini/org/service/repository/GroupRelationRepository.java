package cn.sparrowmini.org.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.GroupRelation;
import cn.sparrowmini.org.model.relation.GroupRelation.GroupRelationPK;

public interface GroupRelationRepository extends JpaRepository<GroupRelation, GroupRelationPK> {

  Page<GroupRelation> findByIdParentId(String groupId, Pageable pageable);

}
