package com.iyuezu.api.interfaces;

import java.util.List;

import com.iyuezu.common.beans.Resource;

public interface IResourceService {

	public Resource getResourceByUuid(String uuid);

	public List<Resource> getResources(Integer level, Integer status, Integer page, Integer row);

	public List<Resource> getResourcesByUser(String userUuid, Integer status, Integer page, Integer row);

	public List<Resource> getResourcesByRole(String roleUuid, Integer status, Integer page, Integer row);

	public Resource createResource(Resource resource);

	public Resource editResource(Resource resource);

	public int removeResourceByUuid(String uuid);

	public int createRoleResourceRelation(String roleUuid, String resourceUuid);

	public int removeRoleResourceRelation(String roleUuid, String resourceUuid);

	public List<Resource> getResourcesWithRole(String resourceUuid, Integer level, Integer status);

	public int batchOperateRelation(String roleUuid, String[] createUuids, String[] removeUuids) throws Exception;

}
