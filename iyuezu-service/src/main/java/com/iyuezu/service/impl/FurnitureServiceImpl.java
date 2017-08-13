package com.iyuezu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IFurnitureService;
import com.iyuezu.common.beans.Furniture;
import com.iyuezu.mybatis.mapper.FurnitureMapper;

@Service
public class FurnitureServiceImpl implements IFurnitureService {

	@Autowired
	private FurnitureMapper furnitureMapper;

	@Override
	public Furniture getFurnitureById(int id) {
		return furnitureMapper.selectFurnitureById(id);
	}

	@Override
	public PageInfo<Furniture> getFurnitures(Furniture furniture, int page, int row) {
		PageHelper.startPage(page, row);
		List<Furniture> furnitures = furnitureMapper.selectFurnitures(furniture);
		return new PageInfo<Furniture>(furnitures);
	}

	@Override
	public List<Furniture> getFurnitures(Furniture furniture) {
		return furnitureMapper.selectFurnitures(furniture);
	}

	@Override
	public List<Furniture> getAllFurnitures() {
		return furnitureMapper.selectFurnitures(null);
	}

	@Override
	public List<Furniture> getValidFurnitures() {
		Furniture furniture = new Furniture();
		furniture.setStatus(1);
		return furnitureMapper.selectFurnitures(furniture);
	}

	@Override
	public int createFurniture(Furniture furniture) {
		return furnitureMapper.insertFurniture(furniture);
	}

	@Override
	public int editFurniture(Furniture furniture) {
		return furnitureMapper.updateFurniture(furniture);
	}

}
