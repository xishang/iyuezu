package com.iyuezu.api.interfaces;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.iyuezu.common.beans.Furniture;

public interface IFurnitureService {
	
	public Furniture getFurnitureById(int id);
	
	public PageInfo<Furniture> getFurnitures(Furniture furniture, int page, int row);
	
	public List<Furniture> getFurnitures(Furniture furniture);
	
	public List<Furniture> getAllFurnitures();
	
	public List<Furniture> getValidFurnitures();
	
	public int createFurniture(Furniture furniture);
	
	public int editFurniture(Furniture furniture);

}
