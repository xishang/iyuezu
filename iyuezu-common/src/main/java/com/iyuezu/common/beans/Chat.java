package com.iyuezu.common.beans;

/**
 * 聊天信息[去掉所谓的"群"的概念, 使用自由度更大的聊天]
 */
public class Chat {

	private Integer id;
	private String name; // 名称
	private String remark; // 备注
	private Integer status; // 状态[0:无效, 1:有效]
	private Long createTime; // 创建时间
	private Long updateTime; // 更新时间
	private Long activeTime; // 活动时间[最后一条聊天内容创建时间]

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Long activeTime) {
		this.activeTime = activeTime;
	}

}
