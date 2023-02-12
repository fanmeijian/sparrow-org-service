package cn.sparrowmini.org.service;


import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestBody;

import cn.sparrowmini.org.model.relation.OrganizationRoleRelation;
import cn.sparrowmini.org.model.relation.OrganizationRoleRelation.OrganizationRoleRelationPK;

public interface RoleService extends RoleRestService {

	public List<OrganizationRoleRelation> getChildren(String organizationId, String roleId);

	public List<OrganizationRoleRelation> getParents(String organizationId, String roleId);

	public void addRelations(@NotNull @RequestBody List<OrganizationRoleRelationPK> ids);

	public void delRelations(@NotNull @RequestBody List<OrganizationRoleRelationPK> ids);

}
