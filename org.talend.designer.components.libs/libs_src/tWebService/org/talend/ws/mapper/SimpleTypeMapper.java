/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.talend.ws.mapper;

import org.apache.commons.beanutils.ConvertUtils;

/**
 *
 * @author rlamarche
 */
public class SimpleTypeMapper implements TypeMapper {

    private Class<?> clazz;

    public SimpleTypeMapper(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Object convertToType(Object value) {
        if (value == null) {
            return null;
        } else {
            return ConvertUtils.convert(ConvertUtils.convert(value), clazz);
        }
    }

    public Object typeToValue(Object bean) {
        return bean;
    }
}
