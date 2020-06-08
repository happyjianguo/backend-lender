package com.yqg.common.utils;

import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 属性工具(暂时无法解决,null值拷贝问题)
 * Created by gao on 2018/6/22.
 */
public class BeanCoypUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanCoypUtil.class);

    /**
     * 对象字段列表缓存
     */
    private static HashMap<String, List<Field>> classFieldsMap = new HashMap<>();

    /**
     * 获取对象字段列表
     * @param clazz
     * @return
     */
    public static List<Field> getFields(Class<?> clazz) {

        List<Field> fieldList = new ArrayList<>();
        if (classFieldsMap.containsKey(clazz.getName())) {
            fieldList = classFieldsMap.get(clazz.getName());
        } else {
            Field[] beanFields = clazz.getDeclaredFields();//本类
            CollectionUtils.mergeArrayIntoCollection(beanFields, fieldList);
            Class<?> beanSuperClass = clazz.getSuperclass();//父类
            if(beanSuperClass!=null) {
                Field[] beanSuperFields = beanSuperClass.getDeclaredFields();
                CollectionUtils.mergeArrayIntoCollection(beanSuperFields, fieldList);

                Class<?> beanGrandClass = beanSuperClass.getSuperclass();//爷爷类 ^_^
                if(beanGrandClass!=null) {
                    Field[] beanSuper2Fields = beanGrandClass.getDeclaredFields();
                    CollectionUtils.mergeArrayIntoCollection(beanSuper2Fields, fieldList);
                }
            }
            //本地缓存
            classFieldsMap.put(clazz.getName(), fieldList);
        }

        return fieldList;
    }

    private static final Map<String, BeanCopier> BEAN_COPIERS = new HashMap<>();

    /**
     * 泛型对象实例化
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T newClass(Class<T> clazz) throws BusinessException {
        T object = null;
        try {
            object = clazz.newInstance();
        } catch (Exception ex) {
            logger.error("泛型实例化异常{},", ex);
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
        return object;

    }

    /**
     * 转换并创建对象
     *
     * @param srcObj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T copyToNewObject(Object srcObj, Class<T> clazz) throws BusinessException {
        T destObj = newClass(clazz);
        copy(srcObj, destObj, null);
        return destObj;
    }

    /**
     * 不带转换器
     *
     * @param srcObj
     * @param destObj
     */
    public static void copy(Object srcObj, Object destObj) {

        copy(srcObj, destObj, null);
    }

    /**
     * 对象属性拷贝带转换器
     *
     * @param srcObj
     * @param destObj
     * @param converter
     */
    public static void copy(Object srcObj, Object destObj, Converter converter) {
        if (srcObj == null) {
            destObj = srcObj;
        } else {
            String key = genKey(srcObj.getClass(), destObj.getClass());
            BeanCopier copier = null;
            if (!BEAN_COPIERS.containsKey(key)) {
                if (converter != null) {
                    copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), true);
                } else {
                    copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
                }
                BEAN_COPIERS.put(key, copier);
            } else {
                copier = BEAN_COPIERS.get(key);
            }
            if (converter != null) {
                copier.copy(srcObj, destObj, converter);
            } else {
                copier.copy(srcObj, destObj, null);
            }
        }
    }

    private static String genKey(Class<?> srcClazz, Class<?> destClazz) {
        return srcClazz.getName() + destClazz.getName();
    }


    /**
     * 转换器
     */
    static class CommonConverter implements Converter {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public Object convert(Object value, Class target, Object context) {
            if (value instanceof Integer) {
                return (Integer) value;
            } else if (value instanceof Date) {
                Date date = (Date) value;
                return sdf.format(date);
            } else if (value instanceof BigDecimal) {
                BigDecimal bd = (BigDecimal) value;
                return bd.toPlainString();
            }
            return null;
        }
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param clazz 要转化的类型
     * @param map   包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InstantiationException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static <T> T convertMap(Class<T> clazz, Map map) throws BusinessException {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz); // 获取类属性
            T obj = clazz.newInstance(); // 创建 JavaBean 对象

            // 给 JavaBean 对象的属性赋值

            // TODO optimize the method or use another library
            // currently using reflection and might be slow
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                Class<?> propertyTypeName = descriptor.getPropertyType();

                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);

                    Object[] args = new Object[1];
                    if (propertyTypeName.equals(BigDecimal.class)) {
                        Double temp = (Double) value;
                        args[0] = BigDecimal.valueOf(temp);
                    } else {
                        args[0] = value;
                    }
                    descriptor.getWriteMethod().invoke(obj, args);
                }
            }
            return obj;
        } catch (Exception ex) {
            logger.error("Map 转 对象异常,{}", ex);
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean) throws BusinessException {
        try {
            Class type = bean.getClass();
            Map returnMap = new HashMap();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
            return returnMap;
        } catch (Exception ex) {
            logger.error("对象 转 Map异常,{}", ex);
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
    }

}
