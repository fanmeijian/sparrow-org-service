package cn.sparrowmini.org.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.org.model.Organization;
import cn.sparrowmini.org.model.PositionLevel;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationLevel;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevel;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevel.OrganizationPositionLevelPK;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevelRelation;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevelRelation.OrganizationPositionLevelRelationPK;
import cn.sparrowmini.org.service.PositionLevelService;
import cn.sparrowmini.org.service.repository.EmployeeOrganizationLevelRepository;
import cn.sparrowmini.org.service.repository.OrganizationLevelRelationRepository;
import cn.sparrowmini.org.service.repository.OrganizationLevelRepository;
import cn.sparrowmini.org.service.repository.OrganizationRepository;
import cn.sparrowmini.org.service.repository.PositionLevelRepository;
import cn.sparrowmini.org.service.scope.LevelScope;
import cn.sparrowmini.pem.service.ScopePermission;

@Service
public class PositionLevelServiceImpl extends AbstractPreserveScope implements PositionLevelService, LevelScope {

	@Autowired
	private PositionLevelRepository levelRepository;
	@Autowired
	private OrganizationLevelRelationRepository organizationLevelRelationRepository;
	@Autowired
	private OrganizationLevelRepository organizationLevelRepository;
	@Autowired
	private EmployeeOrganizationLevelRepository employeeOrganizationLevelRepository;
	@Autowired
	private OrganizationRepository organizationRepository;

	@Override
	public List<EmployeeOrganizationLevel> getEmployees(OrganizationPositionLevelPK organizationLevelId) {
		return this.employeeOrganizationLevelRepository.findByIdOrganizationLevelId(organizationLevelId);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.CREATED)
	@ScopePermission(scope = SCOPE_ADMIN_CREATE)
	public PositionLevel create(PositionLevel lvel) {
		return this.levelRepository.save(lvel);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ADD)
	public void addRelation(List<OrganizationPositionLevelRelation> organizationLevelRelations) {
		this.organizationLevelRelationRepository.saveAll(organizationLevelRelations);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_DELETE)
	public void delete(String[] ids) {
		this.levelRepository.deleteByIdIn(ids);
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_CHILD_LIST)
	public List<OrganizationPositionLevel> getChildren(OrganizationPositionLevelPK parentId) {
		List<OrganizationPositionLevel> positionLevels = new ArrayList<OrganizationPositionLevel>();
		this.organizationLevelRelationRepository.findByIdParentId(parentId).forEach(f -> {
			positionLevels.add(f.getOrganizationLevel());
		});
		return positionLevels;
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_LIST)
	public List<OrganizationPositionLevel> getParents(OrganizationPositionLevelPK id) {
		List<OrganizationPositionLevel> positionLevels = new ArrayList<OrganizationPositionLevel>();
		this.organizationLevelRelationRepository.findByIdId(id).forEach(f -> {
			positionLevels.add(this.organizationLevelRepository.findById(f.getId().getParentId()).get());
		});
		return positionLevels;
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ORG_LIST)
	public List<Organization> getParentOrganizations(@NotBlank String positionLevelId) {
		List<Organization> organizations = new ArrayList<Organization>();
		this.organizationLevelRepository.findByIdPositionLevelId(positionLevelId).forEach(f -> {
			organizations.add(this.organizationRepository.findById(f.getId().getOrganizationId()).get());
		});
		return organizations;
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_REMOVE)
	public void removeRelation(List<OrganizationPositionLevelRelationPK> ids) {
		this.organizationLevelRelationRepository.deleteAllByIdInBatch(ids);
		;
	}

	@Override
	@Transactional
	@ScopePermission(scope = SCOPE_ADMIN_UPDATE)
	public PositionLevel update(String positionLevelId, Map<String, Object> map) {
		PositionLevel source = this.levelRepository.getById(positionLevelId);
		PatchUpdateHelper.merge(source, map);
		return this.levelRepository.save(source);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ORG_ADD)
	public void setParentOrg(String positionLevelId, List<String> orgs) {
		orgs.forEach(f -> {
			this.organizationLevelRepository.save(new OrganizationPositionLevel(f, positionLevelId));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ORG_REMOVE)
	public void removeParentOrg(String positionLevelId, List<String> orgs) {
		orgs.forEach(f -> {
			this.organizationLevelRepository.deleteById(new OrganizationPositionLevelPK(f, positionLevelId));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_ADD)
	public void addRelation(OrganizationPositionLevelPK organizationLevelId, List<OrganizationPositionLevelPK> ids) {
		ids.forEach(f -> {
			this.organizationLevelRelationRepository
					.save(new OrganizationPositionLevelRelation(organizationLevelId, f));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(scope = SCOPE_ADMIN_PARENT_REMOVE)
	public void removeRelation(OrganizationPositionLevelPK organizationLevelId, List<OrganizationPositionLevelPK> ids) {
		ids.forEach(f -> {
			this.organizationLevelRelationRepository
					.deleteById(new OrganizationPositionLevelRelationPK(organizationLevelId, f));
		});
	}

	@Override
	@ScopePermission(scope = SCOPE_ADMIN_READ)
	public PositionLevel get(String positionLevelId) {
		return this.levelRepository.findById(positionLevelId).orElse(null);
	}

}
