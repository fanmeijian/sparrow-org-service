package cn.sparrowmini.org.service;


import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import cn.sparrowmini.org.model.relation.OrganizationPositionLevelRelation;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevelRelation.OrganizationPositionLevelRelationPK;

public interface PositionLevelService extends JobLevelRestService{

	public void addRelation(@RequestBody List<OrganizationPositionLevelRelation> organizationLevelRelations);

	public void removeRelation(@RequestBody List<OrganizationPositionLevelRelationPK> ids);

}
