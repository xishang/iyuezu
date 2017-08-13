package com.iyuezu.common.beans;

import java.util.List;

public class Resource {

	private String uuid; // 资源唯一标识
	private String key; // 资源标志，如："house:save"
	private String name; // 资源名
	private String parent; // 父权限[0:根]
	private Integer level; // 权限级别[0:根, 1~n:菜单/按钮]
	private String icon; // 资源显示图标
	private String description; // 资源描述
	private String url; // 资源对应url
	private Integer status; // 资源状态
	private List<Role> roles; // 拥有该资源权限的角色列表

	public void formatDefault() {
		uuid = uuid == null ? "" : uuid;
		key = key == null ? "" : key;
		name = name == null ? "" : name;
		parent = parent == null ? "" : parent;
		level = level == null ? 0 : level;
		icon = icon == null ? "" : icon;
		description = description == null ? "" : description;
		url = url == null ? "" : url;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
