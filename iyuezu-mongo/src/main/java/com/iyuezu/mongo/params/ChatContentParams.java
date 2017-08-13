package com.iyuezu.mongo.params;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class ChatContentParams {

	private Integer chatId;
	private String userUuid;
	private Integer type;
	private Integer status;
	private Long startTime;
	private Long endTime;
	private int pageIndex;
	private int pageSize;
	private int orderBy = 2; // 默认为按时间降序排序[1:按时间升序, 2:按时间降序]

	public Query createQuery(boolean isFind) {
		Query query = new Query();
		if (chatId != null) {
			query.addCriteria(Criteria.where("chatId").is(chatId));
		}
		if (userUuid != null) {
			query.addCriteria(Criteria.where("userUuid").is(userUuid));
		}
		if (type != null) {
			query.addCriteria(Criteria.where("type").is(type));
		}
		if (status != null) {
			query.addCriteria(Criteria.where("status").is(status));
		}
		if (startTime != null) {
			query.addCriteria(Criteria.where("createTime").gte(startTime));
		}
		if (endTime != null) {
			query.addCriteria(Criteria.where("createTime").lte(endTime));
		}
		if (isFind) {
			if (pageIndex > 0 && pageSize > 0) {
				int start = (pageIndex - 1) * pageSize;
				int count = pageSize;
				query.limit(count).skip(start);
			}
			query.with(new Sort(new Order(orderBy == 1 ? Direction.ASC : Direction.DESC, "createTime")));
		}
		return query;
	}

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
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

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

}
