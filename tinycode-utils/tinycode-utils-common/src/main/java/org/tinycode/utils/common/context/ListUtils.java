package org.tinycode.utils.common.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * List工具
 * @author littlehui
 * @date 2021/11/15 14:44
 * @version 1.0
 */
public class ListUtils {

	/**
	 * 获取list里面单个对象的单个属性重新组装成一个list
	 * @param list
     * @param columnName
     * @param columnClass
	 * @author littlehui
	 * @date 2021/11/15 15:45
	 * @return java.util.List<T>
	 */
	public static <T> List<T> getListItemsSingleColumnList(List list, String columnName, Class<T> columnClass) {
		List<T> returnList = new ArrayList<T>();
		if (!ListUtils.isEmpty(list)) {
			for (Object object : list) {
				Object columnObject = getPropValueByName(object, columnName);
				if (columnObject != null) {
					returnList.add((T) columnObject);
				}
			}
		}
		return returnList;
	}

	/**
	 * 获取字段属性值
	 * @param object
     * @param propName
	 * @author littlehui
	 * @date 2021/11/15 15:44
	 * @return java.lang.Object
	 */
	public static Object getPropValueByNameSimple(Object object, String propName) {
		try {
			// 优先从方法获取，get+属性(属性第一个字母为转换成大写)
			Object result = getPropValueByMethod(object, propName);
			if (result == null) {
				Field field = null;
				try {
					field = object.getClass().getDeclaredField(propName);
				} catch (NoSuchFieldException e) {
					if (object.getClass().getSuperclass() != null) {
						field = object.getClass().getSuperclass().getDeclaredField(propName);
					}
				}
				if (field == null) {
					return null;
				}
				// 获取原来的访问控制权限
				boolean accessFlag = field.isAccessible();
				// 修改访问控制权限
				field.setAccessible(true);
				result = field.get(object);
				field.setAccessible(accessFlag);
			}
			return result;
		} catch (Exception e) {
			// 异常返回空
			return getPropValueByMethod(object, propName);
		}
	}

	/**
	 * 根据属性名称获取属性值.
	 * @param object
     * @param propName
	 * @author littlehui
	 * @date 2021/11/15 15:45
	 * @return java.lang.Object
	 */
	public static Object getPropValueByName(Object object, String propName) {
		try {
			String[] props = propName.split("\\.");
			Object o = object;
			for (String prop : props) {
				o = getPropValueByNameSimple(o, prop);
				if (o == null) {
					break;
				}
			}
			return o;
		} catch (Exception e) {
			// 异常返回空
			return null;
		}
	}

	/**
	 * 根据属性名称获取属性值.
	 * @param object
     * @param propName
     * @param params
	 * @author littlehui
	 * @date 2021/11/15 15:45
	 * @return java.lang.Object
	 */
	public static Object getPropValueByMethod(Object object, String propName, Object... params) {
		try {
			StringBuffer sb = new StringBuffer(propName);
			sb.setCharAt(0, Character.toUpperCase(propName.charAt(0)));

			Method method = object.getClass().getDeclaredMethod("get" + sb.toString());
			if (method == null) {
				return null;
			}
			// 获取原来的访问控制权限
			boolean accessFlag = method.isAccessible();
			// 修改访问控制权限
			method.setAccessible(true);
			Object result = method.invoke(object);
			method.setAccessible(accessFlag);
			return result;
		} catch (Exception e) {
			// 异常返回空
			return null;
		}
	}

	/**
	 * 判断容器是否为空
	 * @param c
	 * @author littlehui
	 * @date 2021/11/15 15:45
	 * @return boolean
	 */
	public static boolean isEmpty(Collection c) {
		if (c == null || c.size() < 1) {
			return true;
		}
		return false;
	}

	/**
	 * 是否不为空
	 * @param c
	 * @author littlehui
	 * @date 2021/11/15 15:46
	 * @return boolean
	 */
	public static boolean isNotEmpty(Collection c) {
		return !isEmpty(c);
	}

	/**
	 * 拼装list成String
	 * @param list
     * @param separator
	 * @author littlehui
	 * @date 2021/11/15 15:46
	 * @return java.lang.String
	 */
	public static String list2String(List list, String separator) {
		StringBuffer returnStr = new StringBuffer("");
		if (isNotEmpty(list)) {
			int i = 0;
			for (Object o : list) {
				returnStr.append(o);
				i++;
				if (i < list.size()) {
					returnStr.append(separator);
				}
			}
		}
		return returnStr.toString();
	}
}
