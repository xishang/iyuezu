package com.iyuezu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iyuezu.api.interfaces.IRoleService;
import com.iyuezu.common.beans.Role;
import com.iyuezu.common.utils.UuidUtil;
import com.iyuezu.mybatis.mapper.RoleMapper;

@Service
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public Role getRoleByUuid(String uuid) {
		return roleMapper.selectRoleByUuid(uuid);
	}

	@Override
	public List<Role> getRoles(Integer status, Integer page, Integer row) {
		Integer offset = null;
		Integer size = null;
		if (page != null && page > 1 && row != null && row > 0) {
			offset = (page - 1) * row;
			size = row;
		}
		return roleMapper.selectRoles(status, offset, size);
	}

	@Override
	public List<Role> getRolesByUser(String userUuid, Integer status, Integer page, Integer row) {
		Integer offset = null;
		Integer size = null;
		if (page != null && page > 1 && row != null && row > 0) {
			offset = (page - 1) * row;
			size = row;
		}
		return roleMapper.selectRolesByUser(userUuid, status, offset, size);
	}

	@Override
	public Role createRole(Role role) {
		role.formatDefault();
		role.setUuid(UuidUtil.getUuidByTimestamp(5));
		int flag = roleMapper.insertRole(role);
		return flag == 0 ? null : role;
	}

	@Override
	public Role editRole(Role role) {
		int flag = roleMapper.updateRole(role);
		return flag == 0 ? null : role;
	}

	@Override
	public int removeRoleByUuid(String uuid) {
		return roleMapper.deleteRoleByUuid(uuid);
	}

	@Override
	public int createUserRoleRelation(String userUuid, String roleUuid) {
		return roleMapper.insertRelation(userUuid, roleUuid);
	}

	@Override
	public int removeUserRoleRelation(String userUuid, String roleUuid) {
		return roleMapper.deleteRelation(userUuid, roleUuid);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int batchCreateRelation(String userUuid, String[] roleUuids) throws Exception {
		int count = 0;
		for (String roleUuid : roleUuids) {
			int flag = roleMapper.insertRelation(userUuid, roleUuid);
			if (flag == 0) {
				throw new Exception();
			} else {
				count++;
			}
		}
		return count;
	}

}
