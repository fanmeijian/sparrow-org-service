package cn.sparrowmini.org.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cn.sparrowmini.org.model.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

	@Transactional
	void deleteByIdIn(String[] ids);

	Iterable<Role> findByIsRoot(boolean b);

}
