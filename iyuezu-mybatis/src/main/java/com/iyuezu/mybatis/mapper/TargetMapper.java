package com.iyuezu.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

import com.iyuezu.common.beans.Target;

public interface TargetMapper {

	public Target selectTargetByUuid(@Param("uuid") String uuid);

	public Integer insertTarget(Target target);

	public Integer updateTarget(Target target);

}
