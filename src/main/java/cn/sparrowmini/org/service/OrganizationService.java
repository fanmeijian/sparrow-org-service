package cn.sparrowmini.org.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.Organization;
import cn.sparrowmini.org.model.relation.OrganizationGroup;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevel;
import cn.sparrowmini.org.model.relation.OrganizationRole;
import cn.sparrowmini.org.service.scope.OrgScope;

public interface OrganizationService extends OrganizationRestService,OrgScope {

	public List<Organization> getChildren(@PathVariable("organizationId") String organizationId);

	public Page<OrganizationRole> getRoles(@PathVariable("organizationId") String organizationId, Pageable pageable);

	public Page<OrganizationPositionLevel> getLevels(@PathVariable("organizationId") String organizationId,
			Pageable pageable);

	public Page<OrganizationGroup> getGroups(@PathVariable("organizationId") String organizationId, Pageable pageable);

	public Page<Employee> getEmployees(@PathVariable("organizationId") String organizationId, Pageable pageable);

}
