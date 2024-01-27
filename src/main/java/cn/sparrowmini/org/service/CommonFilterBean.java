package cn.sparrowmini.org.service;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.sparrowmini.org.model.Employee;
import cn.sparrowmini.org.model.Group;
import cn.sparrowmini.org.model.PositionLevel;
import cn.sparrowmini.org.model.Role;

public class CommonFilterBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String code;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifitedBy;
	private String status;

	@JsonIgnore
	private ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);

	public Example<Employee> toEmployeeExample() {
		return Example.of(new Employee(this.name, this.code,null), this.matcher);
	}
	
	public Example<Role> toRoleExample() {
		return Example.of(new Role(this.name, this.code), this.matcher);
	}
	
	public Example<PositionLevel> toLevelExample() {
		return Example.of(new PositionLevel(this.name, this.code), this.matcher);
	}
	
	public Example<Group> toGroupExample() {
		return Example.of(new Group(this.name, this.code), this.matcher);
	}

	public CommonFilterBean() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifitedBy() {
		return modifitedBy;
	}

	public void setModifitedBy(String modifitedBy) {
		this.modifitedBy = modifitedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
