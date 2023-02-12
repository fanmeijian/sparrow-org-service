package cn.sparrowmini.org.service.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.Employee_;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationLevel;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationLevel_;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationRole;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationRole_;

public interface EmployeeRepository extends JpaRepository<Employee, String>, JpaSpecificationExecutor<Employee> {

	Iterable<Employee> findByIsRoot(boolean b);

	@Transactional
	void deleteByIdIn(String[] ids);

	Page<Employee> findByOrganizationId(String organizationId, Pageable pageable);

	default Page<Employee> findAllByOrganizationId(String organizationId, Pageable pageable) {
		Specification<Employee> specification = new Specification<Employee>() {
			private static final long serialVersionUID = 1L;
			List<Predicate> predicates = new ArrayList<Predicate>();

			@Override
			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				Subquery<EmployeeOrganizationRole> roleSubquery = query.subquery(EmployeeOrganizationRole.class);
				roleSubquery.select(roleSubquery.from(EmployeeOrganizationRole.class).get(EmployeeOrganizationRole_.id)
						.get("employeeId"));
				roleSubquery.where(criteriaBuilder.equal(roleSubquery.from(EmployeeOrganizationRole.class)
						.get(EmployeeOrganizationRole_.id).get("organizationRoleId")
						.get("organizationId"), organizationId));

				Subquery<EmployeeOrganizationLevel> levelSubquery = query.subquery(EmployeeOrganizationLevel.class);
				levelSubquery.select(levelSubquery.from(EmployeeOrganizationLevel.class)
						.get(EmployeeOrganizationLevel_.ID).get("employeeId"));
				levelSubquery.where(criteriaBuilder.equal(levelSubquery.from(EmployeeOrganizationLevel.class)
						.get(EmployeeOrganizationLevel_.ID).get("organizationLevelId")
						.get("organizationId"), organizationId));

				predicates.add(criteriaBuilder.equal(root.get(Employee_.organizationId), organizationId));
				predicates.add(criteriaBuilder.in(root.get(Employee_.ID)).value(roleSubquery));
				predicates.add(criteriaBuilder.in(root.get(Employee_.ID)).value(levelSubquery));
				query.distinct(true);
				return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
			}

		};
		return findAll(specification, pageable);
	}

	long countByOrganizationId(String id);

	default Page<Employee> search(Employee employee, Pageable pageable) {
		Specification<Employee> specification = new Specification<Employee>() {
			private static final long serialVersionUID = 1L;
			List<Predicate> predicates = new ArrayList<Predicate>();

			@Override
			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (employee.getName() != null) {
					predicates.add(criteriaBuilder.like(root.get(Employee_.name), "%" + employee.getName() + "%"));
				}
				if (employee.getCode() != null) {
					predicates.add(criteriaBuilder.like(root.get(Employee_.code), "%" + employee.getCode() + "%"));
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			}

		};
		return findAll(specification, pageable);
	};

}
