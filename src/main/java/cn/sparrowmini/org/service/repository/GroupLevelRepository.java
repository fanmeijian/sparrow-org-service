package cn.sparrowmini.org.service.repository;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.org.model.relation.GroupPositionLevel;
import cn.sparrowmini.org.model.relation.GroupPositionLevel.GroupPositionLevelPK;

public interface GroupLevelRepository extends JpaRepository<GroupPositionLevel, GroupPositionLevelPK> {

	List<GroupPositionLevel> findByIdGroupId(@NotBlank String groupId);

	Page<GroupPositionLevel> findByIdGroupId(String groupId, Pageable pageable);

}
