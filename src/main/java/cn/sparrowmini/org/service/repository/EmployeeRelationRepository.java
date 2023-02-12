package cn.sparrowmini.org.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cn.sparrowmini.org.model.relation.EmployeeRelation;
import cn.sparrowmini.org.model.relation.EmployeeRelation.EmployeeRelationPK;

public interface EmployeeRelationRepository extends JpaRepository<EmployeeRelation, EmployeeRelationPK> {

	List<EmployeeRelation> findByIdParentId(String object);

	@Transactional
	void deleteByIdEmployeeIdInOrIdParentIdIn(String[] ids, String[] ids2);

	long countByIdParentId(String parentId);

	List<EmployeeRelation> findByIdEmployeeId(String employeeId);


}
