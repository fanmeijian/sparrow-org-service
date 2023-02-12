package cn.sparrowmini.org.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cn.sparrowmini.org.model.PositionLevel;

public interface PositionLevelRepository extends JpaRepository<PositionLevel, String> {

	@Transactional
	void deleteByIdIn(String[] ids);

	Iterable<PositionLevel> findByIsRoot(boolean b);
}
