package cn.sparrowmini.org.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.common.CurrentUser;
import cn.sparrowmini.common.api.SparrowTree;
import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.Organization;
import cn.sparrowmini.org.model.constant.OrganizationChildTypeEnum;
import cn.sparrowmini.org.model.relation.OrganizationGroup;
import cn.sparrowmini.org.model.relation.OrganizationGroup.OrganizationGroupPK;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevel;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevel.OrganizationPositionLevelPK;
import cn.sparrowmini.org.model.relation.OrganizationRelation;
import cn.sparrowmini.org.model.relation.OrganizationRelation.OrganizationRelationPK;
import cn.sparrowmini.org.model.relation.OrganizationRole;
import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;
import cn.sparrowmini.org.service.OrganizationService;
import cn.sparrowmini.org.service.repository.EmployeeRepository;
import cn.sparrowmini.org.service.repository.OrganizationGroupRepository;
import cn.sparrowmini.org.service.repository.OrganizationLevelRepository;
import cn.sparrowmini.org.service.repository.OrganizationRelationRepository;
import cn.sparrowmini.org.service.repository.OrganizationRepository;
import cn.sparrowmini.org.service.repository.OrganizationRoleRepository;
import cn.sparrowmini.org.service.scope.OrgScope;
import cn.sparrowmini.pem.service.ScopePermission;

@Service
public class OrganizationServiceImpl extends AbstractPreserveScope implements OrganizationService, OrgScope {

	@Autowired
	private OrganizationRelationRepository organizationRelationRepository;
	@Autowired
	private OrganizationRoleRepository organizationRoleRepository;
	@Autowired
	private OrganizationLevelRepository organizationLevelRepository;
	@Autowired
	private OrganizationGroupRepository organizationGroupRepository;
	@Autowired
	private OrganizationRepository organizationRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Page<OrganizationGroup> getGroups(String organizationId, Pageable pageable) {
		return this.organizationGroupRepository.findByIdOrganizationId(organizationId, pageable);
	}

	@Override
	public Page<OrganizationRole> getRoles(String organizationId, Pageable pageable) {
		return this.organizationRoleRepository.findByIdOrganizationId(organizationId, pageable);
	}

	@Override
	public Page<OrganizationPositionLevel> getLevels(String organizationId, Pageable pageable) {
		return this.organizationLevelRepository.findByIdOrganizationId(organizationId, pageable);
	}

	@Override
	public Page<Employee> getEmployees(@NotBlank String organizationId, Pageable pageable) {
		return this.employeeRepository.findByOrganizationId(organizationId, pageable);
	}

	@Override
	public List<Organization> getChildren(String parentId) {
		List<OrganizationRelation> organizationRelations = new ArrayList<OrganizationRelation>();
		if (parentId == null || parentId.isBlank()) {
			this.organizationRepository.findByIsRoot(true).forEach(f -> {
				organizationRelations.add(new OrganizationRelation(f));
			});
		} else {
			organizationRelations.addAll(organizationRelationRepository.findByIdParentId(parentId));
		}

		List<Organization> children = new ArrayList<Organization>();
		organizationRelations.forEach(f -> {
			Organization organization = this.organizationRepository.findById(f.getId().getOrganizationId()).get();
			children.add(organization);
		});
		return children;
	}

	@Override
	public List<OrganizationRelation> getParents(String organizationId) {
		return this.organizationRelationRepository.findByIdOrganizationId(organizationId);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_DELETE)
	public void delete(String organizationId) {
		this.organizationRepository.deleteById(organizationId);
		this.organizationRelationRepository.deleteByIdOrganizationId(organizationId);
		this.organizationRelationRepository.deleteByIdParentId(organizationId);

	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ADD)
	public void addParent(String organizationId, List<String> parentIds) {

		parentIds.forEach(f -> {
			if (f.equals("root")) {
				Organization organization = organizationRepository.getById(organizationId);
				organization.setIsRoot(true);
				this.organizationRepository.save(organization);
			} else {
				this.organizationRelationRepository
						.save(new OrganizationRelation(new OrganizationRelationPK(organizationId, f)));
			}
		});

	}

	public void addRoles(Set<OrganizationRolePK> ids) {
		ids.forEach(f -> {
			this.organizationRoleRepository.saveAndFlush(new OrganizationRole(f));
		});
	}

	public void delRoles(Set<OrganizationRolePK> ids) {
		ids.forEach(f -> {
			this.organizationRoleRepository.delete(new OrganizationRole(f));
		});
	}

	public void addLevels(Set<OrganizationPositionLevelPK> ids) {
		ids.forEach(f -> {
			this.organizationLevelRepository.saveAndFlush(new OrganizationPositionLevel(f));
		});
	}

	public void delLevels(Set<OrganizationPositionLevelPK> ids) {
		ids.forEach(f -> {
			this.organizationLevelRepository.delete(new OrganizationPositionLevel(f));
		});
	}

	public void addGroups(Set<OrganizationGroupPK> ids) {
		ids.forEach(f -> {
			this.organizationGroupRepository.saveAndFlush(new OrganizationGroup(f));
		});
	}

	public void delGroups(Set<OrganizationGroupPK> ids) {
		ids.forEach(f -> {
			this.organizationGroupRepository.delete(new OrganizationGroup(f));
		});
	}

	@Override
	public SparrowTree<Organization, String> getTreeByParentId(String parentId) {

		if (parentId == null) {
			SparrowTree<Organization, String> rootTree = new SparrowTree<Organization, String>(null);
			this.organizationRepository.findByIsRoot(true).forEach(f -> {
				SparrowTree<Organization, String> myTree = new SparrowTree<Organization, String>(f);
				buildTree(myTree);
				rootTree.getChildren().add(myTree);
			});

			return rootTree;
		} else {
			SparrowTree<Organization, String> myTree = new SparrowTree<Organization, String>(
					organizationRepository.findById(parentId).orElse(null));
			buildTree(myTree);
			return myTree;
		}

	}

	public void buildTree(SparrowTree<Organization, String> myTree) {
		this.organizationRelationRepository.findByIdParentId(myTree.getMe() == null ? null : myTree.getMe().getId())
				.forEach(f -> {
					Organization organization = organizationRepository.findById(f.getId().getOrganizationId()).get();
					SparrowTree<Organization, String> leaf = new SparrowTree<Organization, String>(organization);
					// 防止死循环
					if (organizationRelationRepository
							.findById(
									new OrganizationRelationPK(f.getId().getParentId(), f.getId().getOrganizationId()))
							.orElse(null) == null)
						buildTree(leaf);
					myTree.getChildren().add(leaf);
				});
	}

	@Override
	@ResponseStatus(code = HttpStatus.CREATED)
	@ScopePermission(scope = SCOPE_ADMIN_CREATE)
	public Organization create(@Valid Organization organization) {
		return this.organizationRepository.save(organization);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_UPDATE + "') or hasRole('ROLE_" + ROLE_ADMIN + "')")
	public Organization update(String id, Map<String, Object> map) {
		Organization source = organizationRepository.getById(id);
		PatchUpdateHelper.merge(source, map, Organization.class);
		return this.organizationRepository.save(source);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_REMOVE)
	public void removeParent(String organizationId, String parentId) {
		if (parentId.equals("root")) {
			Organization organization = this.organizationRepository.getById(organizationId);
			organization.setIsRoot(false);
			this.organizationRepository.save(organization);
		} else {
			this.organizationRelationRepository.deleteById(new OrganizationRelationPK(organizationId, parentId));
		}

		// 防止称为孤儿
		if (this.organizationRelationRepository.findByIdOrganizationId(organizationId).size() == 0) {
			Organization organization = this.organizationRepository.getById(organizationId);
			organization.setIsRoot(true);
			this.organizationRepository.save(organization);
		}

	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_READ + "') or hasRole('ROLE_" + ROLE_ADMIN + "')")
	public Organization get(String organizationId) {
		return this.organizationRepository.findById(organizationId).get();
	}

	@Override
	public Page<?> getChildren(String organizationId, OrganizationChildTypeEnum type, Pageable pageable) {
		System.out.println(CurrentUser.get());
		switch (type) {
		case ORGANIZATION:
			if (organizationId.toLowerCase().equals("root")) {
				return this.organizationRepository.findByIsRoot(true, Pageable.unpaged());
			} else {
				return this.organizationRelationRepository.findByIdParentId(organizationId, pageable);
			}
		case ROLE:
			return this.organizationRoleRepository.findByIdOrganizationId(organizationId, pageable);
		case LEVEL:
			return this.organizationLevelRepository.findByIdOrganizationId(organizationId, pageable);
		case GROUP:
			return this.organizationGroupRepository.findByIdOrganizationId(organizationId, pageable);
		case EMPLOYEE:
			return this.employeeRepository.findAllByOrganizationId(organizationId, pageable);
		default:
			break;
		}
		return null;
	}

}
