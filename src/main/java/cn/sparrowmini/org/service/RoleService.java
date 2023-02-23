package cn.sparrowmini.org.service;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.Role;
import cn.sparrowmini.org.model.relation.OrganizationRole;
import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;
import cn.sparrowmini.org.model.relation.OrganizationRoleRelation;
import cn.sparrowmini.org.model.relation.OrganizationRoleRelation.OrganizationRoleRelationPK;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "role", description = "岗位服务")
@RequestMapping("/roles")
public interface RoleService {

	@Operation(summary = "岗位员工列表")
	@GetMapping("/{organizationRoleId}/employees")
	@ResponseBody
	public List<Employee> getEmployees(
			@Parameter(example = "organizationId_roleId", schema = @Schema(implementation = String.class)) @PathVariable("organizationRoleId") OrganizationRolePK organizationRoleId);

	@Operation(summary = "获取下属岗位")
	@GetMapping("/{organizationRoleId}/children")
	@ResponseBody
	public List<OrganizationRoleRelation> getChildren(
			@Parameter(example = "organizationId_roleId", schema = @Schema(implementation = String.class)) @PathVariable("organizationRoleId") OrganizationRolePK organizationRoleId);

	@Operation(summary = "获取上级岗位")
	@GetMapping("/{organizationRoleId}/parents")
	@ResponseBody
	public List<OrganizationRoleRelation> getParents(
			@Parameter(example = "organizationId_roleId", schema = @Schema(implementation = String.class)) @PathVariable("organizationRoleId") OrganizationRolePK organizationRoleId);

	@Operation(summary = "设置上级岗位")
	@PostMapping("/{organizationRoleId}/parents")
	@ResponseBody
	public void addParents(
			@Parameter(example = "organizationId_roleId", schema = @Schema(implementation = String.class)) @PathVariable("organizationRoleId") OrganizationRolePK organizationRoleId,
			@NotNull @RequestBody List<OrganizationRolePK> ids);

	@Operation(summary = "移除上级岗位")
	@DeleteMapping("/{organizationRoleId}/parents")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delParents(
			@Parameter(example = "organizationId_roleId", schema = @Schema(implementation = String.class)) @PathVariable("organizationRoleId") OrganizationRolePK organizationRoleId,
			@NotNull @RequestBody List<OrganizationRolePK> ids);

	@Operation(summary = "岗位所属的组织")
	@GetMapping("/{roleId}/parentOrganizations")
	@ResponseBody
	public List<OrganizationRole> getParentOrganizations(@PathVariable("roleId") String roleId);

	@Operation(summary = "岗位详情")
	@GetMapping("/{roleId}")
	@ResponseBody
	public Role get(@PathVariable("roleId") String roleId);

	@Operation(summary = "设置岗位所属组织")
	@PostMapping("/{roleId}/parentOrganizations")
	@ResponseBody
	public void setParentOrg(@PathVariable("roleId") String roleId, @RequestBody List<String> orgs);

	@Operation(summary = "移除岗位所属组织")
	@DeleteMapping("/{roleId}/parentOrganizations/delete")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeParentOrg(@PathVariable String roleId, @RequestBody List<String> orgs);

	@Operation(summary = "岗位列表")
	@GetMapping("")
	@ResponseBody
	public Page<Role> all(@Nullable Pageable pageable, @Nullable Role role);

	@Operation(summary = "新增岗位")
	@PostMapping("")
	@ResponseBody
	public Role create(@NotNull @RequestBody Role role);

	@Operation(summary = "更新岗位")
	@PatchMapping("/{roleId}")
	@ResponseBody
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Role.class)))
	public Role update(@PathVariable("roleId") String roleId, @RequestBody Map<String, Object> map);

	@Operation(summary = "删除岗位")
	@DeleteMapping("")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ResponseBody
	public void delete(@NotNull @RequestBody final String[] ids);

	public List<OrganizationRoleRelation> getChildren(String organizationId, String roleId);

	public List<OrganizationRoleRelation> getParents(String organizationId, String roleId);

	public void addRelations(@NotNull @RequestBody List<OrganizationRoleRelationPK> ids);

	public void delRelations(@NotNull @RequestBody List<OrganizationRoleRelationPK> ids);

}
