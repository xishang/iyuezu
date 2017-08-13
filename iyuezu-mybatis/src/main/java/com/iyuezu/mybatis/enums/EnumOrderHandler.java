package com.iyuezu.mybatis.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class EnumOrderHandler extends BaseTypeHandler<EnumOrder> {

	private Class<EnumOrder> type;

	private final EnumOrder[] enums;

	/**
	 * 设置配置文件设置的转换类以及枚举类内容，供其他方法更便捷高效的实现
	 * 
	 * @param type
	 *            配置文件中设置的转换类
	 */
	public EnumOrderHandler(Class<EnumOrder> type) {
		if (type == null)
			throw new IllegalArgumentException("Type argument cannot be null");
		this.type = type;
		this.enums = type.getEnumConstants();
		if (this.enums == null)
			throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
	}

	@Override
	public EnumOrder getNullableResult(ResultSet rs, String columnName) throws SQLException {
		// 根据数据库存储类型决定获取类型，本例子中数据库中存放INT类型
		String order = rs.getString(columnName);

		if (rs.wasNull()) {
			return null;
		} else {
			// 根据数据库中的code值，定位EnumStatus子类
			return getEnumOrder(order);
		}
	}

	@Override
	public EnumOrder getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		// 根据数据库存储类型决定获取类型，本例子中数据库中存放INT类型
		String order = rs.getString(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			// 根据数据库中的code值，定位EnumStatus子类
			return getEnumOrder(order);
		}
	}

	@Override
	public EnumOrder getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// 根据数据库存储类型决定获取类型，本例子中数据库中存放INT类型
		String order = cs.getString(columnIndex);
		if (cs.wasNull()) {
			return null;
		} else {
			// 根据数据库中的code值，定位EnumStatus子类
			return getEnumOrder(order);
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, EnumOrder parameter, JdbcType jdbcType)
			throws SQLException {
		// baseTypeHandler已经帮我们做了parameter的null判断
		ps.setString(i, parameter.getValue());
	}
	
	/**
	 * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
	 * 
	 * @param code
	 *            数据库中存储的自定义code属性
	 * @return code对应的枚举类
	 */
	private EnumOrder getEnumOrder(String order) {
		for (EnumOrder eo : enums) {
			if (eo.getValue().equals(order)) {
				return eo;
			}
		}
		throw new IllegalArgumentException("未知的枚举类型：" + order + ",请核对" + type.getSimpleName());
	}

}
