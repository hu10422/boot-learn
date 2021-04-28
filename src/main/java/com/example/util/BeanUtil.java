package com.example.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeanUtil {

    private final static String STRINGCLASS = "";
    private final static Long LONGCLASS = new Long(0);
    private final static Date DATECLASS = new Date();


    /**
     * [字符串]下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String ul2Hump(String str) {
        StringBuilder result = new StringBuilder();
        String a[] = str.split("_");
        for (String s : a) {
            if (!str.contains("_")) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


    /**
     * <p class="detail">
     * 功能：将大写转为下划线,且小写 如: userName > user_name
     * </p>
     *
     * @param name
     * @return
     * @throws
     * @author
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if ((name != null) && (name.length() > 0)) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int i = 1; i < name.length(); ++i) {
                String s = name.substring(i, i + 1);
                if (s.equals(s.toUpperCase())) {
                    result.append("_");
                    result.append(s.toLowerCase());
                } else {
                    result.append(s);
                }
            }
        }
        return result.toString();
    }


    /**
     * 将Map集合中的数据封装到JavaBean对象中
     * map的key为全小写
     * bean属性驼峰
     * @param map           集合
     * @param classType     封装javabean对象
     * @return
     * @throws Exception
     */
    public static <T> T map2bean(Map map, Class<T> classType) throws Exception {
        // 1 采用反射动态创建对象
        T obj = classType.newInstance();

        // 2 获取对象字节码信息,不要Object的属性
        BeanInfo beanInfo = Introspector.getBeanInfo(classType,Object.class);

        // 3 获取bean对象中的所有属性
        PropertyDescriptor[] list = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : list) {
            // 3.1 获取属性名
            String key = pd.getName();

            // 3.2 获取属性值:属性值在map中全部存的小写
            Object value = map.get(key.toLowerCase());

            // 3.3 转换数据类型
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                Class<?> propertyType = pd.getPropertyType();
                if (propertyType.isInstance(STRINGCLASS)) { // 转换成String类型
                    value = (String) value;
                }
                if (propertyType.isInstance(LONGCLASS)) { // 转换成Long类型
                    value = Long.parseLong((String) value);
                }
                if (propertyType.isInstance(DATECLASS)) {// 转换成日期类型
                    value = DateUtils.parseDate((String) value);
                }

                pd.getWriteMethod().invoke(obj, propertyType.cast(value));     //调用属性setter()方法,设置到javabean对象当中
            }
        }
        return obj;
    }
}
