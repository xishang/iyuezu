package com.iyuezu.common.beans;

public class Role {

	private String uuid; // 角色唯一标识
	private String key; // 角色标志
	private String name; // 角色名
	private String parent; // 父角色
	private String description; // 角色描述
	private Integer status; // 角色状态
	
	public void formatDefault() {
		uuid = uuid == null ? "" : uuid;
		key = key == null ? "" : key;
		name = name == null ? "" : name;
		parent = parent == null ? "" : parent;
		description = description == null ? "" : description;
		status = status == null ? 1 : status;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
