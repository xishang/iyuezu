package com.iyuezu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iyuezu.api.interfaces.IResourceService;
import com.iyuezu.common.beans.Resource;
import com.iyuezu.common.utils.UuidUtil;
import com.iyuezu.mybatis.mapper.ResourceMapper;

@Service
public class ResourceServiceImpl implements IResourceService {

	@Autowired
	private ResourceMapper resourceMapper;

	@Override
	public Resource getResourceByUuid(String uuid) {
		return resourceMapper.selectResourceByUuid(uuid);
	}

	@Override
	public List<Resource> getResources(Integer level, Integer status, Integer page, Integer row) {
		Integer offset = null;
		Integer size = null;
		if (page != null && page > 1 && row != null && row > 0) {
			offset = (page - 1) * row;
			size = row;
		}
		return resourceMapper.selectResources(level, status, offset, size);
	}

	@Override
	public List<Resource> getResourcesByUser(String userUuid, Integer status, Integer page, Integer row) {
		Integer offset = null;
		Integer size = null;
		if (page != null && page > 1 && row != null && row > 0) {
			offset = (page - 1) * row;
			size = row;
		}
		return resourceMapper.selectResourcesByUser(userUuid, status, offset, size);
	}

	@Override
	public List<Resource> getResourcesByRole(String roleUuid, Integer status, Integer page, Integer row) {
		Integer offset = null;
		Integer size = null;
		if (page != null && page > 1 && row != null && row > 0) {
			offset = (page - 1) * row;
			size = row;
		}
		return resourceMapper.selectResourcesByRole(roleUuid, status, offset, size);
	}

	@Override
	public Resource createResource(Resource resource) {
		resource.formatDefault();
		resource.setUuid(UuidUtil.getUuidByTimestamp(5));
		int flag = resourceMapper.insertResource(resource);
		return flag == 0 ? null : resource;
	}

	@Override
	public Resource editResource(Resource resource) {
		int flag = resourceMapper.updateResource(resource);
		return flag == 0 ? null : resource;
	}

	@Override
	public int removeResourceByUuid(String uuid) {
		return resourceMapper.deleteResourceByUuid(uuid);
	}

	@Override
	public int createRoleResourceRelation(String roleUuid, String resourceUuid) {
		return resourceMapper.insertRelation(roleUuid, resourceUuid);
	}

	@Override
	public int removeRoleResourceRelation(String roleUuid, String resourceUuid) {
		return resourceMapper.deleteRelation(roleUuid, resourceUuid);
	}

	@Override
	public List<Resource> getResourcesWithRole(String resourceUuid, Integer level, Integer status) {
		return resourceMapper.selectResourcesWithRole(resourceUuid, level, status);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int batchOperateRelation(String roleUuid, String[] createUuids, String[] removeUuids) throws Exception {
		int count = 0;
		for (String createUuid : createUuids) {
			int insertFlag = resourceMapper.insertRelation(roleUuid, createUuid);
			if (insertFlag == 0) {
				throw new Exception();
			} else {
				count++;
			}
		}
		for (String removeUuid : removeUuids) {
			int deleteFlag = resourceMapper.deleteRelation(roleUuid, removeUuid);
			if (deleteFlag == 0) {
				throw new Exception();
			} else {
				count++;
			}
		}
		return count;
	}

}
