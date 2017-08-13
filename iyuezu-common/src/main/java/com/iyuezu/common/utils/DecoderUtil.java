package com.iyuezu.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.iyuezu.common.beans.District;
import com.iyuezu.common.beans.Furniture;
import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.HouseComment;
import com.iyuezu.common.beans.RentInfo;
import com.iyuezu.common.beans.Resource;
import com.iyuezu.common.beans.Role;
import com.iyuezu.common.beans.Target;
import com.iyuezu.common.beans.User;

public class DecoderUtil {

	public static void decode(User user, String enc) {
		try {
			if (user.getUsername() != null) {
				user.setUsername(URLDecoder.decode(user.getUsername(), enc));
			}
			if (user.getName() != null) {
				user.setName(URLDecoder.decode(user.getName(), enc));
			}
		} catch (UnsupportedEncodingException e) {

		}
	}

	public static void decode(House house, String enc) {
		try {
			if (house.getTitle() != null) {
				house.setTitle(URLDecoder.decode(house.getTitle(), enc));
			}
			if (house.getCommunity() != null) {
				house.setCommunity(URLDecoder.decode(house.getCommunity(), enc));
			}
			if (house.getContactName() != null) {
				house.setContactName(URLDecoder.decode(house.getContactName(), enc));
			}
			if (house.getDescription() != null) {
				house.setDescription(URLDecoder.decode(house.getDescription(), enc));
			}
			if (house.getDetail() != null) {
				house.setDetail(URLDecoder.decode(house.getDetail(), enc));
			}
		} catch (UnsupportedEncodingException e) {

		}
	}

	public static void decode(Target target, String enc) {
		try {
			if (target.getTitle() != null) {
				target.setTitle(URLDecoder.decode(target.getTitle(), enc));
			}
			if (target.getCommunity() != null) {
				target.setCommunity(URLDecoder.decode(target.getCommunity(), enc));
			}
			if (target.getContactName() != null) {
				target.setContactName(URLDecoder.decode(target.getContactName(), enc));
			}
			if (target.getDescription() != null) {
				target.setDescription(URLDecoder.decode(target.getDescription(), enc));
			}
			if (target.getDetail() != null) {
				target.setDetail(URLDecoder.decode(target.getDetail(), enc));
			}
		} catch (UnsupportedEncodingException e) {

		}
	}

	public static void decode(Role role, String enc) {
		try {
			if (role.getName() != null) {
				role.setName(URLDecoder.decode(role.getName(), enc));
			}
			if (role.getDescription() != null) {
				role.setDescription(URLDecoder.decode(role.getDescription(), enc));
			}
		} catch (UnsupportedEncodingException e) {

		}
	}

	public static void decode(Resource resource, String enc) {
		try {
			if (resource.getName() != null) {
				resource.setName(URLDecoder.decode(resource.getName(), enc));
			}
			if (resource.getDescription() != null) {
				resource.setDescription(URLDecoder.decode(resource.getDescription(), enc));
			}
		} catch (UnsupportedEncodingException e) {

		}
	}

	public static void decode(RentInfo rentInfo, String enc) {
		try {
			if (rentInfo.getContactName() != null) {
				rentInfo.setContactName(URLDecoder.decode(rentInfo.getContactName(), enc));
			}
			if (rentInfo.getDetail() != null) {
				rentInfo.setDetail(URLDecoder.decode(rentInfo.getDetail(), enc));
			}
		} catch (UnsupportedEncodingException e) {

		}
	}

	public static void decode(District district, String enc) {
		try {
			if (district.getName() != null) {
				district.setName(URLDecoder.decode(district.getName(), enc));
			}
		} catch (UnsupportedEncodingException e) {

		}
	}

	public static void decode(Furniture furniture, String enc) {
		try {
			if (furniture.getName() != null) {
				furniture.setName(URLDecoder.decode(furniture.getName(), enc));
			}
		} catch (UnsupportedEncodingException e) {

		}
	}
	
	public static void decode(HouseComment comment, String enc) {
		try {
			if (comment.getContent() != null) {
				comment.setContent(URLDecoder.decode(comment.getContent(), enc));
			}
		} catch (UnsupportedEncodingException e) {

		}
	}

}
