package cn.sparrowmini.org.service;


import java.util.List;
import java.util.Map;

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

import cn.sparrowmini.org.model.Group;
import cn.sparrowmini.org.model.constant.GroupTypeEnum;
import cn.sparrowmini.org.model.relation.OrganizationGroup;
import cn.sparrowmini.org.service.impl.SparrowTree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "group", description = "群组服务")
@RequestMapping("/groups")
public interface GroupRestService {

	@Operation(summary = "群组详情", operationId = "group")
	@GetMapping("/{groupId}")
	@ResponseBody
	public Group get(@PathVariable("groupId") String groupId);

	@Operation(summary = "群组树", operationId = "groupTree")
	@GetMapping("/tree")
	@ResponseBody
	public SparrowTree<Group, String> getTree(String parentId);

	@Operation(summary = "群组列表", operationId = "groups")
	@GetMapping("")
	@ResponseBody
	public Page<Group> all(@Nullable @ParameterObject Pageable pageable, @Nullable @ParameterObject Group group);

	@Operation(summary = "组成员列表", operationId = "groupMembers")
	@GetMapping("/{groupId}/members")
	@ResponseBody
	public Page<?> getMembers(@PathVariable("groupId") String groupId, GroupTypeEnum type, @Nullable @ParameterObject Pageable pageable);

	@Operation(summary = "新增群组", operationId = "newGroup")
	@PostMapping("")
	@ResponseBody
	public Group create(@RequestBody Group group);

	@Operation(summary = "更新群组", operationId = "updateGroup")
	@PatchMapping("/{groupId}")
	@ResponseBody
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Group.class)))
	public Group update(@PathVariable("groupId") String groupId, @RequestBody Map<String, Object> map);

	@Operation(summary = "删除群组", operationId = "deleteGroup")
	@PutMapping("/delete")
	@ResponseBody
	public void delete(@RequestBody List<String> ids);

	@Operation(summary = "获取所属组织", operationId = "groupParentOrgs")
	@GetMapping("/{groupId}/parentOrganizations")
	@ResponseBody
	public Page<OrganizationGroup> getParentOrgs(@PathVariable("groupId") String groupId, @Nullable @ParameterObject Pageable pageable);

	@Operation(summary = "设置所属组织", operationId = "addGroupParentOrg")
	@PostMapping("/{groupId}/parentOrganizations")
	@ResponseBody
	public void setParentOrgs(@PathVariable("groupId") String groupId, @RequestBody List<String> orgs);

	@Operation(summary = "移除所属组织", operationId = "removeGroupParentOrg")
	@PutMapping("/{groupId}/parentOrganizations/delete")
	@ResponseBody
	public void removeParentOrgs(@PathVariable("groupId") String groupId, @RequestBody List<String> orgs);

	@Operation(summary = "添加组成员", operationId = "addGroupMember")
	@PostMapping("/{groupId}/members")
	@ResponseBody
	public void addMembers(@PathVariable("groupId") String groupId, @RequestParam("type") GroupTypeEnum type,
			@RequestBody List<Object> memberIds);

	@Operation(summary = "移除组成员", operationId = "removeGroupMember")
	@PutMapping("/{groupId}/members/delete")
	@ResponseBody
	public void removeMembers(@PathVariable("groupId") String groupId, @RequestParam("type") GroupTypeEnum type,
			@RequestBody List<Object> memberIds);

}
