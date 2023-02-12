package cn.sparrowmini.org.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Service
public class PositionLevelServiceImpl extends AbstractPreserveScope implements PositionLevelService, LevelScope {

	@Autowired
	PositionLevelRepository levelRepository;
	@Autowired
	OrganizationLevelRelationRepository organizationLevelRelationRepository;
	@Autowired
	OrganizationLevelRepository organizationLevelRepository;
	@Autowired
	EmployeeOrganizationLevelRepository employeeOrganizationLevelRepository;
	@Autowired
	OrganizationRepository organizationRepository;

	@Override
	public List<EmployeeOrganizationLevel> getEmployees(OrganizationPositionLevelPK organizationLevelId) {
		return employeeOrganizationLevelRepository.findByIdOrganizationLevelId(organizationLevelId);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_CREATE+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public PositionLevel create(PositionLevel lvel) {
		return levelRepository.save(lvel);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ADD+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public void addRelation(List<OrganizationPositionLevelRelation> organizationLevelRelations) {
		organizationLevelRelationRepository.saveAll(organizationLevelRelations);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_DELETE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void delete(String[] ids) {
		levelRepository.deleteByIdIn(ids);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_CHILD_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public List<OrganizationPositionLevel> getChildren(OrganizationPositionLevelPK parentId) {
		List<OrganizationPositionLevel> positionLevels = new ArrayList<OrganizationPositionLevel>();
		organizationLevelRelationRepository.findByIdParentId(parentId).forEach(f -> {
			positionLevels.add(f.getOrganizationLevel());
		});
		return positionLevels;
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public List<OrganizationPositionLevel> getParents(OrganizationPositionLevelPK id) {
		List<OrganizationPositionLevel> positionLevels = new ArrayList<OrganizationPositionLevel>();
		organizationLevelRelationRepository.findByIdId(id).forEach(f -> {
			positionLevels.add(organizationLevelRepository.findById(f.getId().getParentId()).get());
		});
		return positionLevels;
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ORG_LIST+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public List<Organization> getParentOrganizations(@NotBlank String positionLevelId) {
		List<Organization> organizations = new ArrayList<Organization>();
		organizationLevelRepository.findByIdPositionLevelId(positionLevelId).forEach(f -> {
			organizations.add(organizationRepository.findById(f.getId().getOrganizationId()).get());
		});
		return organizations;
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_REMOVE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void removeRelation(List<OrganizationPositionLevelRelationPK> ids) {
		organizationLevelRelationRepository.deleteAllByIdInBatch(ids);
		;
	}

	@Override
	@Transactional
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_UPDATE+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public PositionLevel update(String positionLevelId, Map<String, Object> map) {
		PositionLevel source = levelRepository.getById(positionLevelId);
		PatchUpdateHelper.merge(source, map);
		return levelRepository.save(source);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ORG_ADD+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public void setParentOrg(String positionLevelId, List<String> orgs) {
		orgs.forEach(f -> {
			organizationLevelRepository.save(new OrganizationPositionLevel(f, positionLevelId));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ORG_REMOVE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void removeParentOrg(String positionLevelId, List<String> orgs) {
		orgs.forEach(f -> {
			organizationLevelRepository.deleteById(new OrganizationPositionLevelPK(f, positionLevelId));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_ADD+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public void addRelation(OrganizationPositionLevelPK organizationLevelId, List<OrganizationPositionLevelPK> ids) {
		ids.forEach(f -> {
			organizationLevelRelationRepository.save(new OrganizationPositionLevelRelation(organizationLevelId, f));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_PARENT_REMOVE+"') or hasRole('ROLE_"+ROLE_SUPER_ADMIN+"')")
	public void removeRelation(OrganizationPositionLevelPK organizationLevelId, List<OrganizationPositionLevelPK> ids) {
		ids.forEach(f -> {
			organizationLevelRelationRepository
					.deleteById(new OrganizationPositionLevelRelationPK(organizationLevelId, f));
		});
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_"+SCOPE_ADMIN_READ+"') or hasRole('ROLE_"+ROLE_ADMIN+"')")
	public PositionLevel get(String positionLevelId) {
		return levelRepository.findById(positionLevelId).orElse(null);
	}

}
