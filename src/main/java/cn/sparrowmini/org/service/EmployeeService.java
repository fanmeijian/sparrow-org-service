package cn.sparrowmini.org.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.common.api.SparrowTree;
import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationLevel;
import cn.sparrowmini.org.model.relation.EmployeeOrganizationRole;
import cn.sparrowmini.org.model.relation.EmployeeRelation;
import cn.sparrowmini.org.model.relation.OrganizationPositionLevel.OrganizationPositionLevelPK;
import cn.sparrowmini.org.model.relation.OrganizationRole.OrganizationRolePK;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "employee", description = "人员服务")
@RequestMapping("/employees")
public interface EmployeeService {

	@Operation(summary = "新增员工")
	@PostMapping("")
	@ResponseBody
	public Employee create(@RequestBody Employee employee);

	@Operation(summary = "员工详情")
	@GetMapping("/{employeeId}")
	@ResponseBody
	public Employee get(@PathVariable("employeeId") String employeeId);

	@Operation(summary = "员工详情")
	@GetMapping("/all")
	@ResponseBody
	public Page<Employee> all(@Nullable Pageable pageable, @Nullable Employee employee);

	@Operation(summary = "更新员工")
	@PatchMapping("/{employeeId}")
	@ResponseBody
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Employee.class)))
	public Employee update(@PathVariable("employeeId") String employeeId, @RequestBody Map<String, Object> map);

	@Operation(summary = "删除员工")
	@DeleteMapping("")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@RequestBody String[] ids);

	@Operation(summary = "获取直接下属")
	@GetMapping("/{employeeId}/children")
	@ResponseBody
	public List<EmployeeRelation> getChildren(@PathVariable("employeeId") String employeeId);

	@Operation(summary = "获取直接上级")
	@GetMapping("/{employeeId}/parents")
	@ResponseBody
	public List<EmployeeRelation> getParents(@PathVariable("employeeId") String employeeId);

	@Operation(summary = "获取所属职级")
	@GetMapping("/{employeeId}/levels")
	@ResponseBody
	public List<EmployeeOrganizationLevel> getLevels(@PathVariable("employeeId") String employeeId);

	@Operation(summary = "获取担任岗位")
	@GetMapping("/{employeeId}/roles")
	@ResponseBody
	public List<EmployeeOrganizationRole> getRoles(@PathVariable("employeeId") String employeeId);

	@Operation(summary = "获取员工关系树")
	@GetMapping("/{employeeId}/tree")
	@ResponseBody
	public SparrowTree<Employee, String> tree(@PathVariable("employeeId") String employeeId);

	@Operation(summary = "设置担任岗位")
	@PostMapping("/{employeeId}/roles")
	@ResponseBody
	public void addRole(@PathVariable("employeeId") String employeeId,
			@RequestBody List<OrganizationRolePK> organizationRoleIds);

	@Operation(summary = "移除担任岗位")
	@DeleteMapping("/{employeeId}/roles")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeRole(@PathVariable String employeeId, @RequestBody List<OrganizationRolePK> organizationRoleIds);

	@Operation(summary = "设置所属职级别")
	@PostMapping("/{employeeId}/levels")
	@ResponseBody
	public void addLevel(@PathVariable("employeeId") String employeeId,
			@RequestBody List<OrganizationPositionLevelPK> organizationPositionLevelIds);

	@Operation(summary = "移除所属职级别")
	@DeleteMapping("/{employeeId}/levels")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeLevel(@PathVariable String employeeId,
			@RequestBody List<OrganizationPositionLevelPK> organizationPositionLevelIds);

	@Operation(summary = "设置员工上级")
	@PostMapping("/{employeeId}/parents")
	@ResponseBody
	public void addParent(@PathVariable String employeeId, @RequestBody List<String> parentIds);

	@Operation(summary = "移除员工上级")
	@DeleteMapping("/{employeeId}/parents")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeParent(@PathVariable String employeeId, @RequestBody List<String> parentIds);

	public long getChildCount(String employeeId);

}
