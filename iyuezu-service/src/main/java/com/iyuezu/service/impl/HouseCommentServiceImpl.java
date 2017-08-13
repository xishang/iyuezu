package com.iyuezu.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.StringUtil;
import com.iyuezu.api.interfaces.IHouseCommentService;
import com.iyuezu.common.beans.HouseComment;
import com.iyuezu.common.beans.HouseCommentThumb;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.utils.UuidUtil;
import com.iyuezu.mybatis.mapper.HouseCommentMapper;
import com.iyuezu.mybatis.mapper.HouseMapper;
import com.iyuezu.mybatis.params.HouseCommentParams;

@Service
public class HouseCommentServiceImpl implements IHouseCommentService {
	
	@Autowired
	private HouseCommentMapper houseCommentMapper;
	
	@Autowired
	private HouseMapper houseMapper;

	@Override
	public List<HouseComment> getHouseComments(HouseCommentParams params) {
		return houseCommentMapper.selectComments(params);
	}
	
	@Override
	public List<HouseComment> getHouseCommentsWithHouse(HouseCommentParams params) {
		return houseCommentMapper.selectCommentsWithHouse(params);
	}

	@Override
	public List<HouseComment> getHouseCommentsWithReplys(HouseCommentParams params) {
		return houseCommentMapper.selectCommentsWithReplys(params);
	}
	
	@Override
	public List<HouseComment> getCompleteHouseComments(HouseCommentParams params) {
		return houseCommentMapper.selectCompleteComments(params);
	}

	@Override
	public HouseComment getHouseCommentByUuid(String uuid) {
		return houseCommentMapper.selectCommentWithHouseByUuid(uuid);
	}
	
	@Override
	public Integer getHouseCommentCount(HouseCommentParams params) {
		return houseCommentMapper.selectCommentCount(params);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public HouseComment createHouseComment(HouseComment comment) throws Exception {
		comment.formatDefault();
		String uuid = UuidUtil.getUuidByTimestamp(5);
		comment.setUuid(uuid);
		comment.setStatus(1);
		Long timestamp = new Date().getTime();
		comment.setCreateTime(timestamp);
		int flag = houseCommentMapper.insertHouseComment(comment);
		if (flag == 0) {
			throw new Exception("insertHouseComment失败");
		}
		houseMapper.raiseCommentCount(comment.getHouse().getUuid()); // 房源评论数+1
		if (!StringUtil.isEmpty(comment.getReplyUuid())) {
			houseCommentMapper.raiseReplyCount(comment.getReplyUuid()); // 若为回复，则评论回复数+1
		}
		return houseCommentMapper.selectCommentWithHouseByUuid(uuid);
	}

	@Override
	public HouseComment editHouseComment(HouseComment comment) {
		// 删除或恢复评论不再改commentCount或replyCount
		int flag = houseCommentMapper.updateHouseComment(comment);
		return flag == 0 ? null : houseCommentMapper.selectCommentWithHouseByUuid(comment.getUuid());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Integer thumbHouseComment(String commentUuid, String userUuid, String userIp) throws Exception {
		HouseCommentThumb queryThumb = new HouseCommentThumb();
		HouseComment comment = new HouseComment();
		comment.setUuid(commentUuid);
		queryThumb.setComment(comment);
		User user = new User();
		user.setUuid(userUuid);
		queryThumb.setUser(user); // 需要把user_uuid=''的条件加入，过滤掉使用相同ip但是已经登录的用户的点赞
		if (StringUtil.isEmpty(userUuid)) {
			queryThumb.setUserIp(userIp); // 游客点赞，使用ip区别
		}
		List<HouseCommentThumb> resThumbs = houseCommentMapper.selectThumbsWithUser(queryThumb);
		if (resThumbs.size() == 0) { // 没有点赞过，插入新的数据
			HouseCommentThumb thumb = new HouseCommentThumb();
			thumb.setUuid(UuidUtil.getUuidByTimestamp(5));
			thumb.setComment(comment);
			thumb.setUser(user);
			thumb.setUserIp(userIp);
			thumb.setStatus(1);
			Long timestamp = new Date().getTime();
			thumb.setCreateTime(timestamp);
			thumb.setUpdateTime(timestamp);
			int flag = houseCommentMapper.insertHouseCommentThumb(thumb);
			if (flag == 0) {
				throw new Exception("insertHouseCommentThumb出错");
			}
			houseCommentMapper.raiseCommentThumb(commentUuid);
			return 1;
		} else {
			HouseCommentThumb oriThumb = resThumbs.get(0);
			HouseCommentThumb updateThumb = new HouseCommentThumb();
			updateThumb.setUuid(oriThumb.getUuid());
			updateThumb.setUpdateTime(new Date().getTime());
			updateThumb.setStatus(oriThumb.getStatus() == 0 ? 1 : 0); // 点赞或取消点赞
			int flag = houseCommentMapper.updateHouseCommentThumb(updateThumb);
			if (flag == 0) {
				throw new Exception("updateHouseCommentThumb出错");
			}
			if (oriThumb.getStatus() == 0) { // 原始是取消状态，此处为恢复点赞
				houseCommentMapper.raiseCommentThumb(commentUuid);
				return 1;
			} else {
				houseCommentMapper.reduceCommentThumb(commentUuid);
				return -1;
			}
		}
	}

	@Override
	public List<String> getThumbCommentUuids(String userUuid, String userIp) {
		return houseCommentMapper.selectThumbCommentUuids(userUuid, userIp);
	}

}
