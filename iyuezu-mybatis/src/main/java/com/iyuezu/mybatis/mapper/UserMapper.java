package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iyuezu.common.beans.User;
import com.iyuezu.common.beans.UserDto;
import com.iyuezu.common.beans.UserMember;
import com.iyuezu.common.beans.UserRelation;

public interface UserMapper {

	@Select("select * from users where account = #{account}")
	@ResultMap("com.iyuezu.mybatis.mapper.UserMapper.userResult")
	public User selectUserByAccount(@Param("account") String account);

	@Select("select * from users where phone = #{phone}")
	@ResultMap("com.iyuezu.mybatis.mapper.UserMapper.userResult")
	public User selectUserByPhone(@Param("phone") String phone);

	@Select("select * from users where account = #{account} or phone = #{account}")
	@ResultType(User.class)
	public User selectUserByAccountOrPhone(@Param("account") String account);

	@Select("select * from users where uuid = #{uuid}")
	@ResultMap("com.iyuezu.mybatis.mapper.UserMapper.userResult")
	public User selectUserByUuid(@Param("uuid") String uuid);

	@Select("select * from users where token = #{token}")
	@ResultMap("com.iyuezu.mybatis.mapper.UserMapper.userResult")
	public User selectUserByToken(@Param("token") String token);

	@Select("select count(*) from users where account = #{account}")
	public Integer selectUserCountByAccount(@Param("account") String account);

	@Select("select count(*) from users where phone = #{phone}")
	public Integer selectUserCountByPhone(@Param("phone") String phone);

	public UserDto selectUserDtoByUuid(@Param("uuid") String uuid);

	public List<User> selectUsers(@Param("parentUuid") String parentUuid, @Param("type") Integer type,
			@Param("status") Integer status);

	public Integer insertUser(User user);

	public Integer updateUser(User user);

	@Update("update users set password = #{newPassword} where uuid = #{uuid} and password = #{password}")
	public Integer changePassword(@Param("uuid") String uuid, @Param("password") String password,
			@Param("newPassword") String newPassword);
	
	public Integer insertUserRelation(UserRelation userRelation);
	
	public Integer updateUserRelation(UserRelation userRelation);
	
	public Integer selectUserRelationId(@Param("userUuid") String userUuid, @Param("friendUuid") String friendUuid);
	
	public List<UserMember> selectFriends(@Param("userUuid") String userUuid);
	
	public List<User> selectUsersByKeyword(@Param("keyword") String keyword);

}
