package com.iyuezu.platform.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IFurnitureService;
import com.iyuezu.common.beans.Furniture;
import com.iyuezu.common.beans.ResponseResult;
import com.iyuezu.common.utils.DecoderUtil;

@RestController
@RequestMapping("/furniture")
public class FurnitureController {

	@Autowired
	private IFurnitureService furnitureService;

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<Furniture>> getFurnitureList(Furniture furniture,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		PageInfo<Furniture> furniturePage = furnitureService.getFurnitures(furniture, page, row);
		return new ResponseResult<PageInfo<Furniture>>("0", "获取家具列表成功", furniturePage);
	}

	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<Furniture> getFurnitureById(@PathVariable("id") Integer id) {
		Furniture furniture = furnitureService.getFurnitureById(id);
		if (furniture == null) {
			return new ResponseResult<Furniture>("1", "获取家具失败");
		} else {
			return new ResponseResult<Furniture>("0", "获取家具成功", furniture);
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> saveFurniture(Furniture furniture, HttpServletRequest request) {
		DecoderUtil.decode(furniture, "utf-8");
		int flag;
		if (furniture.getId() == null) {
			flag = furnitureService.createFurniture(furniture);
		} else {
			flag = furnitureService.editFurniture(furniture);
		}
		if (flag == 0) {
			return new ResponseResult<Void>("1", "保存家具失败");
		} else {
			return new ResponseResult<Void>("0", "保存家具成功");
		}
	}

}
