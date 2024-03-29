package cn.sparrowmini.org.service;


import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sparrowmini.org.model.Organization;
import cn.sparrowmini.org.model.constant.OrganizationChildTypeEnum;
import cn.sparrowmini.org.model.relation.OrganizationRelation;
import cn.sparrowmini.org.service.impl.SparrowTree;
import cn.sparrowmini.org.service.scope.PreserveScope;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "organization", description = "组织服务")
@RequestMapping("/organizations")
public interface OrganizationRestService extends PreserveScope {

	@Operation(summary = "获取下级", operationId = "orgChildren")
	@GetMapping("/{organizationId}/children")
	@ResponseBody
	public Page<?> getChildren(@PathVariable("organizationId") String organizationId, OrganizationChildTypeEnum type,
			@Nullable @ParameterObject Pageable pageable);

	@Operation(summary = "获取上级组织", operationId = "orgParent")
	@GetMapping("/{organizationId}/parents")
	@ResponseBody
	public List<OrganizationRelation> getParents(@PathVariable("organizationId") String organizationId);

	@Operation(summary = "新增组织", operationId = "newOrg")
	@PostMapping("")
	@ResponseBody
	public Organization create(@NotNull @RequestBody Organization organization);

	@Operation(summary = "更新组织", operationId = "updateOrg")
	@PatchMapping("/{organizationId}")
	@ResponseBody
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Organization.class)))
	public Organization update(@PathVariable("organizationId") String organizationId,
			@RequestBody Map<String, Object> map);

	@Operation(summary = "删除组织", operationId = "deleteOrg")
	@PutMapping("/delete")
	@ResponseBody
	public void delete(@NotNull @RequestBody String[] ids);

	@Operation(summary = "添加所属上级", operationId = "addOrgParent")
	@PostMapping("/{organizationId}/parents")
	@ResponseBody
	public void addParent(@PathVariable("organizationId") String organizationId, @RequestBody List<String> parentIds);

	@Operation(summary = "移除所属上级",operationId = "removeOrgParent")
	@PutMapping("/{organizationId}/parents/delete")
	@ResponseBody
	public void removeParent(@PathVariable("organizationId") String organizationId,
			@RequestBody List<String> parentIds);

	@Operation(summary = "获取组织树", operationId = "orgTree")
	@GetMapping("/tree")
	@ResponseBody
	public SparrowTree<Organization, String> getTreeByParentId(@Nullable @RequestParam("parentId") String parentId);

	@Operation(summary = "组织详情", operationId = "org")
	@GetMapping("/{organizationId}")
	@ResponseBody
	public Organization get(@PathVariable("organizationId") String organizationId);
	
	@Operation(summary = "下级数量", operationId = "orgChildCount")
	@GetMapping(value = "/{organizationId}/childCount")
	@ResponseBody
	public long childCount(@PathVariable String organizationId,OrganizationChildTypeEnum type);
}
