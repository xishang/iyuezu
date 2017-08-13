package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iyuezu.common.beans.HouseComment;
import com.iyuezu.common.beans.HouseCommentThumb;
import com.iyuezu.mybatis.params.HouseCommentParams;

public interface HouseCommentMapper {

	/**
	 * HouseCommentParams不在mybatis在xml配置的映射文件夹[typeAliases.package]
	 * 需要指明parameterType的全路径包名
	 */
	public List<HouseComment> selectComments(HouseCommentParams params);

	public List<HouseComment> selectCommentsWithReplys(HouseCommentParams params);

	public List<HouseComment> selectCommentsWithHouse(HouseCommentParams params);

	public List<HouseComment> selectCompleteComments(HouseCommentParams params);

	public HouseComment selectCommentWithHouseByUuid(@Param("uuid") String uuid);

	public HouseComment selectCommentWithReplysByUuid(@Param("uuid") String uuid);

	public Integer selectCommentCount(HouseCommentParams params);

	public Integer insertHouseComment(HouseComment houseComment);

	public Integer updateHouseComment(HouseComment houseComment);

	public Integer raiseCommentThumb(@Param("uuid") String uuid);

	public Integer reduceCommentThumb(@Param("uuid") String uuid);

	public List<HouseCommentThumb> selectThumbsWithComment(HouseCommentThumb thumb);

	public List<HouseCommentThumb> selectThumbsWithUser(HouseCommentThumb thumb);

	public List<HouseCommentThumb> selectThumbs(HouseCommentThumb thumb);

	public HouseCommentThumb selectThumbByUuid(@Param("uuid") String uuid);

	public Integer insertHouseCommentThumb(HouseCommentThumb thumb);

	public Integer updateHouseCommentThumb(HouseCommentThumb thumb);

	public Integer raiseReplyCount(@Param("uuid") String uuid);

	public Integer reduceReplyCount(@Param("uuid") String uuid);
	
	public List<String> selectThumbCommentUuids(@Param("userUuid") String userUuid, @Param("userIp") String userIp);

}
