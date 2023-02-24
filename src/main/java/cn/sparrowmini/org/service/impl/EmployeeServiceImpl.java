package cn.sparrowmini.org.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.common.api.SparrowTree;
import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationLevel;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationLevel.EmployeeOrganizationLevelPK;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationRole;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationRole.EmployeeOrganizationRolePK;
import cn.sparrowmini.org.model.relation.EmployeeRelation;
import cn.sparrowmini.org.model.relation.EmployeeRelation.EmployeeRelationPK;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevel.OrganizationPositionLevelPK;
import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;
import cn.sparrowmini.org.service.EmployeeService;
import cn.sparrowmini.org.service.repository.EmployeeOrganizationLevelRepository;
import cn.sparrowmini.org.service.repository.EmployeeOrganizationRoleRepository;
import cn.sparrowmini.org.service.repository.EmployeeRelationRepository;
import cn.sparrowmini.org.service.repository.EmployeeRepository;
import cn.sparrowmini.org.service.scope.EmployeeScope;
import cn.sparrowmini.pem.service.ScopePermission;

@Service
public class EmployeeServiceImpl extends AbstractPreserveScope implements EmployeeService, EmployeeScope {

	@Autowired
	private EmployeeRelationRepository employeeRelationRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeOrganizationRoleRepository employeeOrganizationRoleRepository;
	@Autowired
	private EmployeeOrganizationLevelRepository employeeOrganizationLevelRepository;

	@Override
	@Transactional
	@ScopePermission(scope = SCOPE_ADMIN_UPDATE)
	public Employee update(String employeeId, Map<String, Object> map) {
		Employee source = this.employeeRepository.getById(employeeId);
		PatchUpdateHelper.merge(source, map);
		return this.employeeRepository.save(source);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.CREATED)
	@ScopePermission(scope = SCOPE_ADMIN_CREATE)
	public Employee create(Employee employee) {
		return this.employeeRepository.save(employee);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ADD)
	public void addParent(String employeeId, List<String> parentIds) {
		parentIds.forEach(f -> {
			this.employeeRelationRepository.save(new EmployeeRelation(employeeId, f));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_REMOVE)
	public void removeParent(String employeeId, List<String> parentIds) {
		parentIds.forEach(f -> {
			this.employeeRelationRepository.deleteById(new EmployeeRelationPK(employeeId, f));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_ROLE_ADD)
	public void addRole(String employeeId, List<OrganizationRolePK> ids) {
		ids.forEach(f -> {
			this.employeeOrganizationRoleRepository.save(new EmployeeOrganizationRole(employeeId, f));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_ROLE_REMOVE)
	public void removeRole(String employeeId, List<OrganizationRolePK> ids) {
		ids.forEach(f -> {
			this.employeeOrganizationRoleRepository.deleteById(new EmployeeOrganizationRolePK(f, employeeId));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_LEVEL_ADD)
	public void addLevel(String employeeId, List<OrganizationPositionLevelPK> ids) {
		ids.forEach(f -> {
			this.employeeOrganizationLevelRepository.save(new EmployeeOrganizationLevel(employeeId, f));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_LEVEL_REMOVE)
	public void removeLevel(String employeeId, List<OrganizationPositionLevelPK> ids) {
		ids.forEach(f -> {
			this.employeeOrganizationLevelRepository.deleteById(new EmployeeOrganizationLevelPK(f, employeeId));
		});
	}

	public SparrowTree<Employee, String> getTree(String parentId) {
		if (parentId == null) {
			SparrowTree<Employee, String> rootTree = new SparrowTree<Employee, String>(null);
			this.employeeRepository.findByIsRoot(true).forEach(f -> {
				SparrowTree<Employee, String> myTree = new SparrowTree<Employee, String>(f);
				buildTree(myTree);
				rootTree.getChildren().add(myTree);
			});

			return rootTree;
		} else {
			SparrowTree<Employee, String> myTree = new SparrowTree<Employee, String>(
					this.employeeRepository.findById(parentId).orElse(null));
			buildTree(myTree);
			return myTree;
		}
	}

	public void buildTree(SparrowTree<Employee, String> myTree) {
		this.employeeRelationRepository.findByIdParentId(myTree.getMe() == null ? null : myTree.getMe().getId())
				.forEach(f -> {
					SparrowTree<Employee, String> leaf = new SparrowTree<Employee, String>(
							this.employeeRepository.findById(f.getId().getEmployeeId()).get());
					// 防止死循环
					if (this.employeeRelationRepository
							.findById(new EmployeeRelationPK(f.getId().getParentId(), f.getId().getEmployeeId()))
							.orElse(null) == null)
						buildTree(leaf);
					myTree.getChildren().add(leaf);
				});
	}

	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_DELETE)
	public void delBatch(String[] ids) {
		this.employeeRelationRepository.deleteByIdEmployeeIdInOrIdParentIdIn(ids, ids);
		this.employeeRepository.deleteByIdIn(ids);
	}

	@Override
	public List<EmployeeRelation> getChildren(String employeeId) {
		return this.employeeRelationRepository.findByIdParentId(employeeId);
	}

	public long getChildCount(String parentId) {
		return this.employeeRelationRepository.countByIdParentId(parentId);
	}

	@Override
	public List<EmployeeRelation> getParents(String employeeId) {
		return this.employeeRelationRepository.findByIdEmployeeId(employeeId);
	}

	@Override
	public List<EmployeeOrganizationLevel> getLevels(String employeeId) {
		return this.employeeOrganizationLevelRepository.findByIdEmployeeId(employeeId);
	}

	@Override
	public List<EmployeeOrganizationRole> getRoles(String employeeId) {
		return this.employeeOrganizationRoleRepository.findByIdEmployeeId(employeeId);
	}

	@Override
	public SparrowTree<Employee, String> tree(String parentId) {
		return getTree(parentId);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_DELETE)
	public void delete(String[] ids) {
		this.employeeRepository.deleteByIdIn(ids);
	}

	@Override
	public Employee get(String employeeId) {
		return this.employeeRepository.findById(employeeId).orElse(null);
	}

	@Override
	public Page<Employee> all(Pageable pageable, Employee employee) {
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
		return this.employeeRepository.findAll(Example.of(employee, matcher), pageable);
	}

}
