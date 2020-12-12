package com.sophia.store.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lizhe
 * @date 2020/12/3 17:49
 **/
public final class ObjectUtils {
    /**
     * 把origin的非null的值拷贝的哦target中
     *
     * @param origin 原始类
     * @param target 目标类
     */
    @SneakyThrows
    public static void copyFiledValue(Object origin, Object target) {
        if (origin.getClass() != target.getClass()) {
            throw new RuntimeException("class o1 is not equal o2");
        }
        Class<?> originClass = origin.getClass();
        Class<?> targetClass = target.getClass();
        Field[] originClassDeclaredFields = originClass.getDeclaredFields();
        Field[] targetClassDeclaredFields = targetClass.getDeclaredFields();
        for (Field originField : originClassDeclaredFields) {
            String originFieldName = originField.getName();
            originField.setAccessible(true);
            for (Field targetField : targetClassDeclaredFields) {
                String targetFieldName = targetField.getName();
                targetField.setAccessible(true);
                if (originFieldName.equals(targetFieldName)) {
                    Object value = originField.get(origin);
                    // 需要判断Collection有数据才设置
                    if (null != value) {
                        if (value instanceof HashSet) {
                            Set<?> set = (Set<?>) value;
                            if (set.size() > 0) {
                                targetField.set(target, value);
                            }
                        } else {
                            targetField.set(target, value);
                        }
                    }
                }
            }
        }
    }
}
