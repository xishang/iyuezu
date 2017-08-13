package com.iyuezu.platform.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyuezu.api.interfaces.IRoleService;
import com.iyuezu.common.beans.ResponseResult;
import com.iyuezu.common.beans.Role;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.utils.DecoderUtil;

@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private IRoleService roleService;

	@RequestMapping("/list")
	public ResponseResult<List<Role>> getRoleList(@RequestParam(value = "userUuid", required = false) String userUuid,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "row", required = false) Integer row) {
		List<Role> roleList = null;
		if (userUuid == null) {
			roleList = roleService.getRoles(status, page, row);
		} else {
			roleList = roleService.getRolesByUser(userUuid, status, page, row);
		}
		if (roleList == null) {
			return new ResponseResult<List<Role>>("1", "获取角色列表失败");
		} else {
			return new ResponseResult<List<Role>>("0", "获取角色列表成功", roleList);
		}
	}

	@RequestMapping("/ownList")
	public ResponseResult<List<Role>> getOwnRoleList(@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer row, HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		List<Role> roleList = roleService.getRolesByUser(user.getUuid(), status, page, row);
		if (roleList == null) {
			return new ResponseResult<List<Role>>("1", "获取角色列表失败");
		} else {
			return new ResponseResult<List<Role>>("0", "获取角色列表成功", roleList);
		}
	}

	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
	public ResponseResult<Role> getRoleById(@PathVariable("uuid") String uuid) {
		Role role = roleService.getRoleByUuid(uuid);
		if (role == null) {
			return new ResponseResult<Role>("1", "获取角色失败，该角色不存在");
		} else {
			return new ResponseResult<Role>("0", "获取角色成功", role);
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseResult<Role> createRole(Role role) {
		DecoderUtil.decode(role, "utf-8");
		Role resRole;
		if (role.getUuid() == null) {
			resRole = roleService.createRole(role);
		} else {
			resRole = roleService.editRole(role);
		}
		if (resRole == null) {
			return new ResponseResult<Role>("1", "保存角色失败");
		} else {
			return new ResponseResult<Role>("0", "保存角色成功", resRole);
		}
	}

	@RequestMapping(value = "/remove/{uuid}", method = RequestMethod.POST)
	public ResponseResult<Void> removeRoleById(@PathVariable("uuid") String uuid) {
		int flag = roleService.removeRoleByUuid(uuid);
		if (flag == 0) {
			return new ResponseResult<Void>("1", "删除角色失败");
		} else {
			return new ResponseResult<Void>("0", "删除角色成功");
		}
	}

	@RequestMapping(value = "/relation/create", method = RequestMethod.POST)
	public ResponseResult<Void> createRelation(@RequestParam("userUuid") String userUuid,
			@RequestParam("roleUuid") String roleUuid) {
		int flag = roleService.createUserRoleRelation(userUuid, roleUuid);
		if (flag == 0) {
			return new ResponseResult<Void>("1", "创建用户与角色关系失败");
		} else {
			return new ResponseResult<Void>("0", "创建用户与角色关系成功");
		}
	}

	@RequestMapping(value = "/relation/remove", method = RequestMethod.POST)
	public ResponseResult<Void> removeRelation(@RequestParam("userUuid") String userUuid,
			@RequestParam("roleUuid") String roleUuid) {
		int flag = roleService.removeUserRoleRelation(userUuid, roleUuid);
		if (flag == 0) {
			return new ResponseResult<Void>("1", "删除用户与角色关系失败");
		} else {
			return new ResponseResult<Void>("0", "删除用户与角色关系成功");
		}
	}

	@RequestMapping(value = "/relation/batchCreate", method = RequestMethod.POST)
	public ResponseResult<Integer> batchCreateRelation(@RequestParam("userUuid") String userUuid,
			@RequestParam("roleUuids[]") String[] roleUuids) {
		try {
			int count = roleService.batchCreateRelation(userUuid, roleUuids);
			return new ResponseResult<Integer>("0", "批量创建用户与角色关系成功", count);
		} catch (Exception e) {
			return new ResponseResult<Integer>("0", "批量创建用户与角色关系失败");
		}
	}

}
