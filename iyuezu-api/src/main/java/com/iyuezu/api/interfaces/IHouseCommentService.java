package com.iyuezu.api.interfaces;

import java.util.List;

import com.iyuezu.common.beans.HouseComment;
import com.iyuezu.mybatis.params.HouseCommentParams;

public interface IHouseCommentService {
	
	public List<HouseComment> getHouseComments(HouseCommentParams params);
	
	public List<HouseComment> getHouseCommentsWithHouse(HouseCommentParams params);
	
	public List<HouseComment> getHouseCommentsWithReplys(HouseCommentParams params);
	
	public List<HouseComment> getCompleteHouseComments(HouseCommentParams params);
	
	public Integer getHouseCommentCount(HouseCommentParams params);
	
	public HouseComment getHouseCommentByUuid(String uuid);
	
	public HouseComment createHouseComment(HouseComment comment) throws Exception;
	
	public HouseComment editHouseComment(HouseComment comment);
	
	public Integer thumbHouseComment(String commentUuid, String userUuid, String userIp) throws Exception;
	
	public List<String> getThumbCommentUuids(String userUuid, String userIp);

}
