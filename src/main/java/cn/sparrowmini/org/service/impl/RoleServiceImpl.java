package cn.sparrowmini.org.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.Role;
import cn.sparrowmini.org.model.relation.OrganizationRole;
import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;
import cn.sparrowmini.org.model.relation.OrganizationRoleRelation;
import cn.sparrowmini.org.model.relation.OrganizationRoleRelation.OrganizationRoleRelationPK;
import cn.sparrowmini.org.service.CommonFilterBean;
import cn.sparrowmini.org.service.RoleService;
import cn.sparrowmini.org.service.repository.EmployeeOrganizationRoleRepository;
import cn.sparrowmini.org.service.repository.EmployeeRepository;
import cn.sparrowmini.org.service.repository.OrganizationRepository;
import cn.sparrowmini.org.service.repository.OrganizationRoleRelationRepository;
import cn.sparrowmini.org.service.repository.OrganizationRoleRepository;
import cn.sparrowmini.org.service.repository.RoleRepository;
import cn.sparrowmini.org.service.scope.RoleScope;

@Service
public class RoleServiceImpl extends AbstractPreserveScope implements RoleService,RoleScope {

	@Autowired
	RoleRepository roleRepository;
	@Autowired
	EmployeeOrganizationRoleRepository employeeOrganizationRoleRepository;
	@Autowired
	OrganizationRoleRepository organizationRoleRepository;
	@Autowired
	OrganizationRoleRelationRepository organizationRoleRelationRepository;
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	OrganizationRepository organizationRepository;

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_CREATE+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public Role create(Role role) {
		return roleRepository.save(role);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ORG_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public List<OrganizationRole> getParentOrganizations(@NotBlank String roleId) {
		return organizationRoleRepository.findByIdRoleId(roleId);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_DELETE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void delete(@NotNull String[] ids) {
		roleRepository.deleteByIdIn(ids);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_UPDATE+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public Role update(String roleId, Map<String, Object> map) {
		Role source = roleRepository.getById(roleId);
		PatchUpdateHelper.merge(source, map);
		return roleRepository.save(source);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_CHILD_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public List<OrganizationRoleRelation> getChildren(String organizationId, String roleId) {
		return getChildren(new OrganizationRolePK(organizationId, roleId));
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public List<OrganizationRoleRelation> getParents(String organizationId, String roleId) {
		return getParents(new OrganizationRolePK(organizationId, roleId));
	}

	public List<OrganizationRoleRelation> getChildren(@NotNull OrganizationRolePK parentId) {
		return organizationRoleRelationRepository.findByIdParentId(parentId);
	}

	public List<OrganizationRoleRelation> getParents(@NotNull OrganizationRolePK organizationRolePK) {
		return organizationRoleRelationRepository.findByIdId(organizationRolePK);
	}

	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ADD+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public void addRelations(List<OrganizationRoleRelationPK> ids) {
		ids.forEach(f -> {
			organizationRoleRelationRepository.save(new OrganizationRoleRelation(f));
		});
	}

	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_REMOVE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void delRelations(List<OrganizationRoleRelationPK> ids) {
		ids.forEach(f -> {
			organizationRoleRelationRepository.deleteById(f);
		});
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public Page<Role> all(Pageable pageable, CommonFilterBean commonFilterBean) {
		return roleRepository.findAll(commonFilterBean.toRoleExample(), pageable);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ORG_ADD+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public void setParentOrg(String roleId, List<String> orgs) {
		orgs.forEach(f -> {
			organizationRoleRepository.save(new OrganizationRole(f, roleId));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ORG_REMOVE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void removeParentOrg(String roleId, List<String> orgs) {
		orgs.forEach(f -> {
			organizationRoleRepository.deleteById(new OrganizationRolePK(f, roleId));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ADD+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public void addParents(OrganizationRolePK organizationRoleId, @NotNull List<OrganizationRolePK> ids) {
		ids.forEach(f -> {
			organizationRoleRelationRepository.save(new OrganizationRoleRelation(organizationRoleId, f));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_REMOVE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void delParents(OrganizationRolePK organizationRoleId, @NotNull List<OrganizationRolePK> ids) {
		ids.forEach(f -> {
			organizationRoleRelationRepository.deleteById(new OrganizationRoleRelationPK(organizationRoleId, f));
		});
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_EMP_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public List<Employee> getEmployees(OrganizationRolePK organizationRoleId) {
		List<Employee> employees = new ArrayList<Employee>();
		employeeOrganizationRoleRepository.findByIdOrganizationRoleId(organizationRoleId).forEach(f -> {
			employees.add(employeeRepository.findById(f.getId().getEmployeeId()).get());
		});
		return employees;
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_READ+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public Role get(String roleId) {
		return roleRepository.findById(roleId).get();
	}

}
