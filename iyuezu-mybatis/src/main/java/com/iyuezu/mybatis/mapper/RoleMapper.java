package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.iyuezu.common.beans.Role;

public interface RoleMapper {

	public Role selectRoleByUuid(@Param("uuid") String uuid);

	public List<Role> selectRoles(@Param("status") Integer status, @Param("offset") Integer offset,
			@Param("size") Integer size);

	public List<Role> selectRolesByUser(@Param("userUuid") String userUuid, @Param("status") Integer status,
			@Param("offset") Integer offset, @Param("size") Integer size);

	public int insertRole(Role role);

	public int updateRole(Role role);

	@Delete("delete from roles where uuid = #{uuid}")
	public int deleteRoleByUuid(@Param("uuid") String uuid);

	@Insert("insert into userrolerelation (user_uuid, role_uuid) values (#{userUuid}, #{roleUuid})")
	public int insertRelation(@Param("userUuid") String userUuid, @Param("roleUuid") String roleUuid);

	@Delete("delete from userrolerelation where user_uuid = #{userUuid} and role_uuid = #{roleUuid}")
	public int deleteRelation(@Param("userUuid") String userUuid, @Param("roleUuid") String roleUuid);

}
