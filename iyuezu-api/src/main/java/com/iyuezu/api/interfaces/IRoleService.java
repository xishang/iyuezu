package com.iyuezu.api.interfaces;

import java.util.List;

import com.iyuezu.common.beans.Role;

public interface IRoleService {

	public Role getRoleByUuid(String uuid);

	public List<Role> getRoles(Integer status, Integer page, Integer row);

	public List<Role> getRolesByUser(String userUuid, Integer status, Integer page, Integer row);

	public Role createRole(Role role);

	public Role editRole(Role role);

	public int removeRoleByUuid(String uuid);

	public int createUserRoleRelation(String userUuid, String roleUuid);

	public int removeUserRoleRelation(String userUuid, String roleUuid);

	public int batchCreateRelation(String userUuid, String[] roleUuids) throws Exception;

}
