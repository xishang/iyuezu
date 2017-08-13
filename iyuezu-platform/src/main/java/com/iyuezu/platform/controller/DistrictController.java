package com.iyuezu.platform.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IDistrictService;
import com.iyuezu.common.beans.District;
import com.iyuezu.common.beans.ResponseResult;
import com.iyuezu.common.utils.DecoderUtil;

@RestController
@RequestMapping("/district")
public class DistrictController {

	@Autowired
	private IDistrictService districtService;

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<District>> getDistrictList(District district,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		PageInfo<District> districtPage = districtService.getDistricts(district, page, row);
		return new ResponseResult<PageInfo<District>>("0", "获取地区列表成功", districtPage);
	}

	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<District> getDistrictById(@PathVariable("id") Integer id) {
		District district = districtService.getDistrictById(id);
		if (district == null) {
			return new ResponseResult<District>("1", "获取地区失败");
		} else {
			return new ResponseResult<District>("0", "获取地区成功", district);
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> saveDistrict(District district, HttpServletRequest request) {
		DecoderUtil.decode(district, "utf-8");
		int flag;
		if (district.getId() == null) {
			flag = districtService.createDistrict(district);
		} else {
			flag = districtService.editDistrict(district);
		}
		if (flag == 0) {
			return new ResponseResult<Void>("1", "保存地区失败");
		} else {
			return new ResponseResult<Void>("0", "保存地区成功");
		}
	}

}
