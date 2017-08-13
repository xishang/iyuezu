package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.iyuezu.common.beans.Furniture;

public interface FurnitureMapper {

	public Integer insertFurniture(Furniture furniture);

	public Integer updateFurniture(Furniture furniture);

	@Select("select * from furnitures where id = #{id}")
	public Furniture selectFurnitureById(@Param("id") Integer id);

	public List<Furniture> selectFurnitures(Furniture furniture);

}
