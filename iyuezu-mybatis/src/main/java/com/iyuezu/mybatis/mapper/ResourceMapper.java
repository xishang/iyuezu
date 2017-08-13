package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.iyuezu.common.beans.Resource;

public interface ResourceMapper {

	public Resource selectResourceByUuid(@Param("uuid") String uuid);

	public List<Resource> selectResources(@Param("level") Integer level, @Param("status") Integer status,
			@Param("offset") Integer offset, @Param("size") Integer size);

	public List<Resource> selectResourcesByUser(@Param("userUuid") String userUuid, @Param("status") Integer status,
			@Param("offset") Integer offset, @Param("size") Integer size);

	public List<Resource> selectResourcesByRole(@Param("roleUuid") String roleUuid, @Param("status") Integer status,
			@Param("offset") Integer offset, @Param("size") Integer size);

	public int insertResource(Resource resource);

	public int updateResource(Resource resource);

	@Delete("delete from resources where uuid = #{uuid}")
	public int deleteResourceByUuid(@Param("uuid") String uuid);

	@Insert("insert into roleresourcerelation (role_uuid, resource_uuid) values (#{roleUuid}, #{resourceUuid})")
	public int insertRelation(@Param("roleUuid") String roleUuid, @Param("resourceUuid") String resourceUuid);

	@Delete("delete from roleresourcerelation where role_uuid = #{roleUuid} and resource_uuid = #{resourceUuid}")
	public int deleteRelation(@Param("roleUuid") String roleUuid, @Param("resourceUuid") String resourceUuid);

	public List<Resource> selectResourcesWithRole(@Param("resourceUuid") String resourceUuid,
			@Param("level") Integer level, @Param("status") Integer status);

}
