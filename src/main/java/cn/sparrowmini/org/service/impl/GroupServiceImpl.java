package cn.sparrowmini.org.service.impl;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.common.api.SparrowTree;
import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.Group;
import cn.sparrowmini.org.model.constant.GroupTypeEnum;
import cn.sparrowmini.org.model.relation.GroupEmployee;
import cn.sparrowmini.org.model.relation.GroupEmployee.GroupEmployeePK;
import cn.sparrowmini.org.model.relation.GroupOrganization;
import cn.sparrowmini.org.model.relation.GroupOrganization.GroupOrganizationPK;
import cn.sparrowmini.org.model.relation.GroupPositionLevel;
import cn.sparrowmini.org.model.relation.GroupPositionLevel.GroupPositionLevelPK;
import cn.sparrowmini.org.model.relation.GroupRelation;
import cn.sparrowmini.org.model.relation.GroupRelation.GroupRelationPK;
import cn.sparrowmini.org.model.relation.GroupRole;
import cn.sparrowmini.org.model.relation.GroupRole.GroupRolePK;
import cn.sparrowmini.org.model.relation.OrganizationGroup;
import cn.sparrowmini.org.model.relation.OrganizationGroup.OrganizationGroupPK;
import cn.sparrowmini.org.service.GroupService;
import cn.sparrowmini.org.service.repository.EmployeeRepository;
import cn.sparrowmini.org.service.repository.GroupEmployeeRepository;
import cn.sparrowmini.org.service.repository.GroupLevelRepository;
import cn.sparrowmini.org.service.repository.GroupOrganizationRepository;
import cn.sparrowmini.org.service.repository.GroupRelationRepository;
import cn.sparrowmini.org.service.repository.GroupRepository;
import cn.sparrowmini.org.service.repository.GroupRoleRepository;
import cn.sparrowmini.org.service.repository.OrganizationGroupRepository;
import cn.sparrowmini.org.service.repository.OrganizationLevelRepository;
import cn.sparrowmini.org.service.repository.OrganizationRepository;
import cn.sparrowmini.org.service.repository.PositionLevelRepository;
import cn.sparrowmini.org.service.repository.RoleRepository;
import cn.sparrowmini.org.service.scope.GroupScope;

/**
 * 群组服务
 * 
 * @author fanmj
 *
 */

@Service
public class GroupServiceImpl extends AbstractPreserveScope implements GroupService, GroupScope {

//	@Autowired
//	GroupUserRepository groupUserRepository;
	@Autowired
	GroupOrganizationRepository groupOrganizationRepository;
	@Autowired
	GroupRoleRepository groupRoleRepository;
	@Autowired
	GroupLevelRepository groupLevelRepository;
	@Autowired
	GroupRelationRepository groupRelationRepository;
	@Autowired
	OrganizationGroupRepository organizationGroupRepository;
	@Autowired
	GroupEmployeeRepository groupEmployeeRepository;

	// @Autowired
	// SubGroupRepository subGroupRepository;

	@Autowired
	GroupRepository groupRepository;
	@Autowired
	OrganizationRepository organizationRepository;
	@Autowired
	OrganizationLevelRepository organizationLevelRepository;
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PositionLevelRepository positionLevelRepository;

	@Override
	public SparrowTree<Group, String> getTree(String parentId) {
		SparrowTree<Group, String> myTree = new SparrowTree<Group, String>(
				parentId == null ? null : groupRepository.findById(parentId).orElse(null));
		buildTree(myTree);
		return myTree;
	}

	public void buildTree(SparrowTree<Group, String> myTree) {

		groupRelationRepository
				.findByIdParentId(myTree.getMe() == null ? null : myTree.getMe().getId(), Pageable.unpaged()).toList()
				.forEach(f -> {
					SparrowTree<Group, String> leaf = new SparrowTree<Group, String>(
							groupRepository.findById(f.getId().getGroupId()).get());
					// 防止死循环
					if (groupRelationRepository
							.findById(new GroupRelationPK(f.getId().getParentId(), f.getId().getGroupId()))
							.orElse(null) == null)
						buildTree(leaf);

					myTree.getChildren().add(leaf);
				});
	}

	@Override
	@ResponseStatus(code = HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_CREATE+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public Group create(Group group) {
		return groupRepository.save(group);
	}

	@Override
	public List<Employee> getFinalEmployees(@NotBlank String groupId) {
		// get the actual employees from all group member except organization for
		// organization member are too large

		return null;
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_UPDATE+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public Group update(String groupId, Map<String, Object> map) {
		Group source = groupRepository.getById(groupId);
		PatchUpdateHelper.merge(source, map);
		return groupRepository.save(source);
	}

	@Override
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_DELETE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void delete(List<String> ids) {
		groupRepository.deleteByIdIn(ids.toArray(new String[] {}));
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public Page<Group> all(@Nullable Pageable pageable, @Nullable Group group) {
		return groupRepository.search(group, pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ORG_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public Page<OrganizationGroup> getParentOrgs(String groupId, Pageable pageable) {
		return organizationGroupRepository.findByIdGroupId(groupId, pageable);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ORG_ADD+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public void setParentOrgs(String groupId, List<String> orgs) {
		orgs.forEach(f -> {
			organizationGroupRepository.save(new OrganizationGroup(f, groupId));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ORG_REMOVE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void removeParentOrgs(String groupId, List<String> orgs) {
		orgs.forEach(f -> {
			organizationGroupRepository.deleteById(new OrganizationGroupPK(f, groupId));
		});
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_MEMBER_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public Page<?> getMembers(String groupId, @NotNull GroupTypeEnum type, Pageable pageable) {
		switch (type) {
		case EMPLOYEE:
			Page<GroupEmployee> employees = groupEmployeeRepository.findByIdGroupId(groupId, pageable);
			employees.getContent().stream().map(m -> employeeRepository.findById(m.getId().getEmployeeId()));
			return employees;
		case ORGANIZATION:
			Page<GroupOrganization> orgs = groupOrganizationRepository.findByIdGroupId(groupId, pageable);
			orgs.getContent().stream().map(m -> organizationRepository.findById(m.getId().getGroupId()));
			return orgs;
		case ROLE:
			Page<GroupRole> roles = groupRoleRepository.findByIdGroupId(groupId, pageable);
			roles.getContent().stream().map(m -> roleRepository.findById(m.getId().getRoleId()));
			return roles;
		case LEVEL:
			Page<GroupPositionLevel> levels = groupLevelRepository.findByIdGroupId(groupId, pageable);
			levels.getContent().stream().map(m -> positionLevelRepository.findById(m.getId().getPositionLevelId()));
			return levels;
//		case SYSROLE:
//			Page<GroupSysrole> sysroles = groupSysroleRepository.findByIdGroupId(groupId, pageable);
//			sysroles.getContent().stream().map(m -> positionLevelRepository.findById(m.getId().getSysroleId()));
//			return sysroles;
//		case USER:
//			Page<GroupUser> users = groupUserRepository.findByIdGroupId(groupId, pageable);
//			users.getContent().stream().map(m -> positionLevelRepository.findById(m.getId().getUsername()));
//			return users;
		case GROUP:
			Page<GroupRelation> groups = groupRelationRepository.findByIdParentId(groupId, pageable);
			groups.getContent().stream().map(m -> positionLevelRepository.findById(m.getId().getGroupId()));
			return groups;
		default:
			break;
		}
		return null;
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_MEMBER_ADD+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public void addMembers(String groupId, @NotNull GroupTypeEnum type, List<Object> memberIds) {
		memberIds.forEach(f -> {
			switch (type) {
			case EMPLOYEE:
				groupEmployeeRepository.save(new GroupEmployee(groupId, f.toString()));
				break;
			case ORGANIZATION:
				groupOrganizationRepository.save(new GroupOrganization(groupId, f.toString()));
				break;
			case ROLE:
				groupRoleRepository.save(new GroupRole(groupId, f.toString()));
				break;
			case LEVEL:
				groupLevelRepository.save(new GroupPositionLevel(groupId, f.toString()));
				break;
//			case SYSROLE:
//				groupSysroleRepository.save(new GroupSysrole(groupId, f.toString()));
//				break;
//			case USER:
//				groupUserRepository.save(new GroupUser(groupId, f.toString()));
//				break;
			case GROUP:
				groupRelationRepository.save(new GroupRelation(f.toString(),groupId));
				break;
			default:
				break;
			}
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_MEMBER_REMOVE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void removeMembers(String groupId, @NotNull GroupTypeEnum type, List<Object> memberIds) {
		memberIds.forEach(f -> {
			switch (type) {
			case EMPLOYEE:
				groupEmployeeRepository.deleteById(new GroupEmployeePK(groupId, f.toString()));
				break;
			case ORGANIZATION:
				groupOrganizationRepository.deleteById(new GroupOrganizationPK(groupId, f.toString()));
				break;
			case ROLE:
				groupRoleRepository.deleteById(new GroupRolePK(groupId, f.toString()));
				break;
			case LEVEL:
				groupLevelRepository.deleteById(new GroupPositionLevelPK(groupId, f.toString()));
				break;
//			case SYSROLE:
//				groupSysroleRepository.deleteById(new GroupSysrolePK(groupId, f.toString()));
//				break;
//			case USER:
//				groupUserRepository.deleteById(new GroupUserPK(groupId, f.toString()));
//				break;
			case GROUP:
				groupRelationRepository.deleteById(new GroupRelationPK(f.toString(),groupId));
			default:
				break;
			}
		});
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_READ+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public Group get(String groupId) {
		return groupRepository.findById(groupId).orElse(null);
	}
}
