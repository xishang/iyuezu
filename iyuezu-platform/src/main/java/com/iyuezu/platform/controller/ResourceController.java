package com.iyuezu.platform.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyuezu.api.interfaces.IResourceService;
import com.iyuezu.common.beans.Resource;
import com.iyuezu.common.beans.ResponseResult;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.utils.DecoderUtil;

@RestController
@RequestMapping("/resource")
public class ResourceController {

	@Autowired
	private IResourceService resourceService;

	@RequestMapping("/list")
	public ResponseResult<List<Resource>> getResourceList(
			@RequestParam(value = "userUuid", required = false) String userUuid,
			@RequestParam(value = "roleUuid", required = false) String roleUuid,
			@RequestParam(value = "level", required = false) Integer level,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "row", required = false) Integer row) {
		List<Resource> resourceList = null;
		if (roleUuid != null) {
			resourceList = resourceService.getResourcesByRole(roleUuid, status, page, row);
		}
		if (userUuid != null) {
			resourceList = resourceService.getResourcesByUser(userUuid, status, page, row);
		}
		if (resourceList == null) {
			resourceList = resourceService.getResources(level, status, page, row);
		}
		if (resourceList == null) {
			return new ResponseResult<List<Resource>>("1", "获取资源列表失败");
		} else {
			return new ResponseResult<List<Resource>>("0", "获取资源列表成功", resourceList);
		}
	}

	@RequestMapping("/ownList")
	public ResponseResult<List<Resource>> getOwnRoleList(
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer row, HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		List<Resource> resourceList = resourceService.getResourcesByUser(user.getUuid(), status, page, row);
		if (resourceList == null) {
			return new ResponseResult<List<Resource>>("1", "获取资源列表失败");
		} else {
			return new ResponseResult<List<Resource>>("0", "获取资源列表成功", resourceList);
		}
	}

	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
	public ResponseResult<Resource> getResourceById(@PathVariable("uuid") String uuid) {
		Resource resource = resourceService.getResourceByUuid(uuid);
		if (resource == null) {
			return new ResponseResult<Resource>("1", "获取资源失败，该资源不存在");
		} else {
			return new ResponseResult<Resource>("0", "获取资源成功", resource);
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseResult<Resource> createResource(Resource resource) {
		DecoderUtil.decode(resource, "utf-8");
		Resource resResource = null;
		if (resource.getUuid() == null) {
			resResource = resourceService.createResource(resource);
		} else {
			resResource = resourceService.editResource(resource);
		}
		if (resResource == null) {
			return new ResponseResult<Resource>("1", "保存资源失败");
		} else {
			return new ResponseResult<Resource>("0", "保存资源成功", resResource);
		}
	}

	@RequestMapping(value = "/remove/{uuid}", method = RequestMethod.POST)
	public ResponseResult<Void> removeResourceById(@PathVariable("uuid") String uuid) {
		int flag = resourceService.removeResourceByUuid(uuid);
		if (flag == 0) {
			return new ResponseResult<Void>("1", "删除资源失败");
		} else {
			return new ResponseResult<Void>("0", "删除资源成功");
		}
	}

	@RequestMapping(value = "/relation/create", method = RequestMethod.POST)
	public ResponseResult<Void> createRelation(@RequestParam("roleUuid") String roleUuid,
			@RequestParam("resourceUuid") String resourceUuid) {
		int flag = resourceService.createRoleResourceRelation(roleUuid, resourceUuid);
		if (flag == 0) {
			return new ResponseResult<Void>("1", "创建角色与资源关系失败");
		} else {
			return new ResponseResult<Void>("0", "创建角色与资源关系成功");
		}
	}

	@RequestMapping(value = "/relation/remove", method = RequestMethod.POST)
	public ResponseResult<Void> removeRelation(@RequestParam("roleUuid") String roleUuid,
			@RequestParam("resourceUuid") String resourceUuid) {
		int flag = resourceService.removeRoleResourceRelation(roleUuid, resourceUuid);
		if (flag == 0) {
			return new ResponseResult<Void>("1", "删除角色与资源关系失败");
		} else {
			return new ResponseResult<Void>("0", "删除角色与资源关系成功");
		}
	}

	@RequestMapping(value = "/relation/edit", method = RequestMethod.POST)
	public ResponseResult<Integer> editRelation(@RequestParam("roleUuid") String roleUuid,
			@RequestParam(value = "createUuids[]", required = false, defaultValue = "") String[] createUuids,
			@RequestParam(value = "removeUuids[]", required = false, defaultValue = "") String[] removeUuids) {
		try {
			int count = resourceService.batchOperateRelation(roleUuid, createUuids, removeUuids);
			return new ResponseResult<Integer>("0", "批量更新角色与资源关系成功", count);
		} catch (Exception e) {
			return new ResponseResult<Integer>("0", "批量更新角色与资源关系失败");
		}
	}

}
