package cn.sparrowmini.org.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.Role;
import cn.sparrowmini.org.model.Role_;
import cn.sparrowmini.org.model.relation.OrganizationRole;
import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;
import cn.sparrowmini.org.model.relation.OrganizationRoleRelation;
import cn.sparrowmini.org.model.relation.OrganizationRoleRelation.OrganizationRoleRelationPK;
import cn.sparrowmini.org.service.RoleService;
import cn.sparrowmini.org.service.repository.EmployeeOrganizationRoleRepository;
import cn.sparrowmini.org.service.repository.EmployeeRepository;
import cn.sparrowmini.org.service.repository.OrganizationRoleRelationRepository;
import cn.sparrowmini.org.service.repository.OrganizationRoleRepository;
import cn.sparrowmini.org.service.repository.RoleRepository;
import cn.sparrowmini.org.service.scope.RoleScope;
import cn.sparrowmini.pem.service.ScopePermission;

@Service
public class RoleServiceImpl extends AbstractPreserveScope implements RoleService, RoleScope {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private EmployeeOrganizationRoleRepository employeeOrganizationRoleRepository;
	@Autowired
	private OrganizationRoleRepository organizationRoleRepository;
	@Autowired
	private OrganizationRoleRelationRepository organizationRoleRelationRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.CREATED)
	@ScopePermission(scope = SCOPE_ADMIN_CREATE)
	public Role create(Role role) {
		return this.roleRepository.save(role);
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ORG_LIST)
	public List<OrganizationRole> getParentOrganizations(@NotBlank String roleId) {
		return this.organizationRoleRepository.findByIdRoleId(roleId);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_DELETE)
	public void delete(@NotNull String[] ids) {
		this.roleRepository.deleteByIdIn(ids);
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_UPDATE)
	public Role update(String roleId, Map<String, Object> map) {
		Role source = this.roleRepository.getById(roleId);
		PatchUpdateHelper.merge(source, map);
		return this.roleRepository.save(source);
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_CHILD_LIST)
	public List<OrganizationRoleRelation> getChildren(String organizationId, String roleId) {
		return this.getChildren(new OrganizationRolePK(organizationId, roleId));
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_LIST)
	public List<OrganizationRoleRelation> getParents(String organizationId, String roleId) {
		return this.getParents(new OrganizationRolePK(organizationId, roleId));
	}

	public List<OrganizationRoleRelation> getChildren(@NotNull OrganizationRolePK parentId) {
		return this.organizationRoleRelationRepository.findByIdParentId(parentId);
	}

	public List<OrganizationRoleRelation> getParents(@NotNull OrganizationRolePK organizationRolePK) {
		return this.organizationRoleRelationRepository.findByIdId(organizationRolePK);
	}

	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ADD)
	public void addRelations(List<OrganizationRoleRelationPK> ids) {
		ids.forEach(f -> {
			this.organizationRoleRelationRepository.save(new OrganizationRoleRelation(f));
		});
	}

	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_REMOVE)
	public void delRelations(List<OrganizationRoleRelationPK> ids) {
		ids.forEach(f -> {
			this.organizationRoleRelationRepository.deleteById(f);
		});
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_LIST)
	public Page<Role> all(Pageable pageable, Role role) {
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths(Role_.IS_ROOT).withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING);
		return this.roleRepository.findAll(Example.of(role, matcher), pageable);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ORG_ADD)
	public void setParentOrg(String roleId, List<String> orgs) {
		orgs.forEach(f -> {
			this.organizationRoleRepository.save(new OrganizationRole(f, roleId));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ORG_REMOVE)
	public void removeParentOrg(String roleId, List<String> orgs) {
		orgs.forEach(f -> {
			this.organizationRoleRepository.deleteById(new OrganizationRolePK(f, roleId));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ADD)
	public void addParents(OrganizationRolePK organizationRoleId, @NotNull List<OrganizationRolePK> ids) {
		ids.forEach(f -> {
			this.organizationRoleRelationRepository.save(new OrganizationRoleRelation(organizationRoleId, f));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_REMOVE)
	public void delParents(OrganizationRolePK organizationRoleId, @NotNull List<OrganizationRolePK> ids) {
		ids.forEach(f -> {
			this.organizationRoleRelationRepository.deleteById(new OrganizationRoleRelationPK(organizationRoleId, f));
		});
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_EMP_LIST)
	public List<Employee> getEmployees(OrganizationRolePK organizationRoleId) {
		List<Employee> employees = new ArrayList<Employee>();
		this.employeeOrganizationRoleRepository.findByIdOrganizationRoleId(organizationRoleId).forEach(f -> {
			employees.add(this.employeeRepository.findById(f.getId().getEmployeeId()).get());
		});
		return employees;
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_READ)
	public Role get(String roleId) {
		return this.roleRepository.findById(roleId).get();
	}

}
