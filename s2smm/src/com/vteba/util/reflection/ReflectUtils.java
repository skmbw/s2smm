package com.vteba.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * 反射工具类。
 * 访问对象(私有)变量，方法，获取泛型类型等。
 * @author yinlei 
 * date 2012-5-7 上午10:28:19
 */
public class ReflectUtils {
	private static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
	private static final String CGLIB_CLASS_SEPARATOR = "$$";
	
	/**
	 * 调用对象obj的某一属性的Getter方法
	 * @param obj 包含某一属性的对象
	 * @param propertyName 某一属性
	 * @return
	 */
	public static Object invokeGetterMethod(Object obj, String propertyName) {
		MethodAccess methodAccess = MethodAccess.get(obj.getClass());
		String getterMethodName = "get" + StringUtils.capitalize(propertyName);//首字母大写
		return methodAccess.invoke(obj, getterMethodName, (Object[])null);
	}
	/**
	 * 调用Setter方法,使用value的Class来查找Setter方法
	 * @param obj 包含某一属性的对象
	 * @param propertyName 某一属性
	 * @param value 备用的对象
	 */
	public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
		MethodAccess methodAccess = MethodAccess.get(obj.getClass());
		String getterMethodName = "set" + StringUtils.capitalize(propertyName);//首字母大写
		methodAccess.invoke(obj, getterMethodName, value);
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数
	 * @param obj 包含要读取的属性的对象
	 * @param fieldName 要读取的属性
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		//FieldAccess fieldAccess = FieldAccess.get(obj.getClass());
		//Field field = getAccessibleField(obj, fieldName);
		//Object object = fieldAccess.get(obj, fieldName);
		//return object;
		//field.setAccessible(false);
		Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.info("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}
	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数
	 * @param obj 包含要设置的属性的对象
	 * @param fieldName 要设置的属性
	 * @param value 要设置的属性的值
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		//AsmUtils.getInstance().setField(obj, fieldName, value);
		
		Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.info("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 获取某一对象某一属性的定义所在的类DeclaredField，并强制设置为可访问
	 * 循环向上转型，如向上转型到Object仍无法找到, 返回null
	 * @param obj 包含某一属性的对象
	 * @param fieldName 某一属性名
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Assert.notNull(obj, "object不能为空");
		Assert.hasText(fieldName, "fieldName");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				//Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}
	
	/**
	 * 直接调用对象方法,无视private/protected修饰符
	 * @param obj 方法所在的对象
	 * @param methodName 要调用的方法名
	 * @param args 调用方法的参数类型
	 * @return 方法的调用结果
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Object[] args) {
		MethodAccess methodAccess = MethodAccess.get(obj.getClass());
		return methodAccess.invoke(obj, methodName, args);
	}
	/**
	 * 直接调用对象方法,无视private/protected修饰符,用于一次性调用的情况.
	 * @param obj 方法所在的对象
	 * @param methodName 要调用的方法名
	 * @param parameterTypes 调用方法的参数类型
	 * @param args 调用方法的参数
	 * @return
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertExceptionToUnchecked(e);
		}
	}
	/**
	 * 获取对象的DeclaredMethod,并强制设置为可访问
	 * 循环向上转型,若向上转型到Object仍无法找到, 返回null
	 * 用于方法需要被多次调用的情况，先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 * @param obj 包含对象的方法
	 * @param methodName 方法名
	 * @param parameterTypes 方法的参数类型
	 * @return
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,final Class<?>... parameterTypes){
		Assert.notNull(obj, "object不能为空");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
				method.setAccessible(true);
				return method;
			} catch (NoSuchMethodException e) {
				//Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 通过反射，获得Class定义中声明的父类的泛型参数的类型，如无法找到，返回Object.class
	 * 如：public UserDao extends HibernateDao&ltUser&gt
	 * @param clazz 泛型参数所在的类的class，如：HibernateDao.class
	 * @return 第一个泛型参数的类型，如果没有找到返回Object.class
	 */
	public static Class<? extends Object> getSuperClassGenericType(Class<? extends Object> clazz) {
		return getSuperClassGenericType(clazz, 0);
	}
	
	/**
	 * 反射，获得Class定义中声明的泛型参数的类型，如无法找到，返回null
	 * 如：public UserDao&ltUser&gt
	 * @param clazz 泛型参数所在的类的class，如：UserDao.class
	 * @return 第一个泛型参数的类型User.class，如果没有找到返回null
	 */
	public static <T> Class<T>  getClassGenericType(Class<? extends Object> clazz) {
		return getClassGenericType(clazz, 0);
	}
	/**
	 * 通过反射，获得Class定义中声明的父类的泛型参数的类型，如无法找到，返回Object.class.
	 * 如public UserDao extends HibernateDao&ltUser, Long&gt
	 * @param 泛型参数所在的类的class
	 * @param index 第几个泛型参数，从 0开始
	 * @return 泛型参数类型，没有返回Objec.class
	 */
	@Deprecated
	public static Class<? extends Object> getSuperClassGenericType(Class<? extends Object> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			//logger.info(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			//logger.info("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			//logger.info(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		@SuppressWarnings("unchecked")
		Class<? extends Object> ret = (Class<? extends Object>) params[index];
		return ret;
	}
	
	/**
	 * 通过反射，获得Class定义中声明的父类的泛型参数的类型，如无法找到，返回null
	 * 如public UserDao&ltUser, Long&gt
	 * @param 泛型参数所在的类的class
	 * @param index 第几个泛型参数，从 0开始
	 * @return 泛型参数类型，没有返回null
	 */
	public static <T> Class<T> getClassGenericType(Class<? extends Object> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			//logger.info(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return null;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			//logger.info("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return null;
		}
		if (!(params[index] instanceof Class)) {
			//logger.info(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return null;
		}
		@SuppressWarnings("unchecked")
		Class<T> ret = (Class<T>) params[index];
		return ret;
	}
	
	public static Class<?> getUserClass(Object instance) {
		Validate.notNull(instance, "Instance must not be null");
		Class<?> clazz = instance.getClass();
		if ((clazz != null) && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if ((superClass != null) && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;

	}
	
	/**
	 * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
				.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}
	
	/**
	 * 将反射时的checked exception转换为unchecked exception
	 * @param e 要转换的异常
	 */
	public static RuntimeException convertExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}
	
}
