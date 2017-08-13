package com.iyuezu.common.beans;

import java.util.List;

public class UserDto {

	private String uuid; // 用户UUID,用来唯一标识该用户
	private String account; // 用户账号
	private String username; // 用户名
	private String password; // 用户密码
	private String name; // 用户真实姓名
	private String phone; // 用户手机号
	private String email; // 用户邮箱
	private String identity; // 用户身份证号
	private String head; // 用户头像
	private Integer type; // 用户类型 [1:租客, 2:房东, 3:管理员]
	private Integer status; // 用户状态 [1:有效, 0:无效]
	private Long createTime; // 用户创建时间戳
	private List<Order> orders; // 房东所有订单
	private List<House> houses; // 房东所有房源

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<House> getHouses() {
		return houses;
	}

	public void setHouses(List<House> houses) {
		this.houses = houses;
	}

}
