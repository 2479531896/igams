package com.matridx.springboot.util.reflect;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * @author Administrator
 *	反射工具
 */
public class ReflectHelper {
	/**
	 * 获取obj对象fieldName的Field
	 */
	public static Field getFieldByFieldName(Object obj, String fieldName) {
		if(null == obj || StringUtils.isBlank(fieldName)) return null;
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
			}
		}
		return null;
	}

	/**
	 * 获取obj对象fieldName的属性值
	 */
	public static Object getValueByFieldName(Object obj, String fieldName)
			throws SecurityException,
			IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if(field!=null){
			if (field.isAccessible()) {
				value = field.get(obj);
			} else {
				field.setAccessible(true);
				value = field.get(obj);
				field.setAccessible(false);
			}
		}
		return value;
	}

	/**
	 * 设置obj对象fieldName的属性值
	 */
	public static void setValueByFieldName(Object obj, String fieldName,
			Object value) throws SecurityException,
			IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		if (field.isAccessible()) {
			field.set(obj, value);
		} else {
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		}
	}
}
