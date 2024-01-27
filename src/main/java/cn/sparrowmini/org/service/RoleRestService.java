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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.Role;
import cn.sparrowmini.org.model.relation.OrganizationRole;
import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;
import cn.sparrowmini.org.model.relation.OrganizationRoleRelation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "role", description = "岗位服务")
@RequestMapping("/roles")
public interface RoleRestService {
	@Operation(summary = "岗位员工列表", operationId = "roleEmployees")
	@GetMapping("/employees")
	@ResponseBody
	public List<Employee> getEmployees(@ParameterObject OrganizationRolePK organizationRoleId);

	@Operation(summary = "获取下属岗位", operationId = "roleChildren")
	@GetMapping("/children")
	@ResponseBody
	public List<OrganizationRoleRelation> getChildren(@ParameterObject OrganizationRolePK organizationRoleId);

	@Operation(summary = "获取上级岗位", operationId = "roleParent")
	@GetMapping("/parents")
	@ResponseBody
	public List<OrganizationRoleRelation> getParents(@ParameterObject OrganizationRolePK organizationRoleId);

	@Operation(summary = "设置上级岗位", operationId = "addRoleParent")
	@PostMapping("/parents")
	@ResponseBody
	public void addParents(@ParameterObject OrganizationRolePK organizationRoleId,
			@NotNull @RequestBody List<OrganizationRolePK> ids);

	@Operation(summary = "移除上级岗位", operationId = "removeRoleParent")
	@PutMapping("/parents/remove")
	@ResponseBody
	public void delParents(@ParameterObject OrganizationRolePK organizationRoleId,
			@NotNull @RequestBody List<OrganizationRolePK> ids);

	@Operation(summary = "岗位所属的组织", operationId = "roleParentOrgs")
	@GetMapping("/{roleId}/parentOrganizations")
	@ResponseBody
	public List<OrganizationRole> getParentOrganizations(@PathVariable("roleId") String roleId);

	@Operation(summary = "岗位详情", operationId = "role")
	@GetMapping("/{roleId}")
	@ResponseBody
	public Role get(@PathVariable("roleId") String roleId);

	@Operation(summary = "设置岗位所属组织", operationId = "addRoleParentOrg")
	@PostMapping("/{roleId}/parentOrganizations")
	@ResponseBody
	public void setParentOrg(@PathVariable("roleId") String roleId, @RequestBody List<String> orgs);

	@Operation(summary = "移除岗位所属组织", operationId = "removeRoleParentOrg")
	@PutMapping("/{roleId}/parentOrganizations/remove")
	@ResponseBody
	public void removeParentOrg(@PathVariable("roleId") String roleId, @RequestBody List<String> orgs);

	@Operation(summary = "岗位列表", operationId = "roles")
	@GetMapping("")
	@ResponseBody
	public Page<Role> all(@Nullable @ParameterObject Pageable pageable,
			@Nullable @ParameterObject CommonFilterBean commonFilterBean);

	@Operation(summary = "新增岗位", operationId = "newRole")
	@PostMapping("")
	@ResponseBody
	public Role create(@NotNull @RequestBody Role role);

	@Operation(summary = "更新岗位", operationId = "updateRole")
	@PatchMapping("/{roleId}")
	@ResponseBody
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Role.class)))
	public Role update(@PathVariable("roleId") String roleId, @RequestBody Map<String, Object> map);

	@Operation(summary = "删除岗位", operationId = "deleteRole")
	@PutMapping("/delete")
	@ResponseBody
	public void delete(@NotNull @RequestBody final String[] ids);
}
