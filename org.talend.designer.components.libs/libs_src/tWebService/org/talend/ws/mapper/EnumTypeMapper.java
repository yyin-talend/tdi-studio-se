/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.talend.ws.mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.ConvertUtils;
import org.talend.ws.exception.InvalidEnumValueException;
import org.talend.ws.exception.LocalizedException;

/**
 *
 * @author rlamarche
 */
public class EnumTypeMapper implements TypeMapper {

    private Class<?> clazz;
    private Method valueOf;

    public EnumTypeMapper(Class<?> clazz) {
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException("You must provide an enum class.");
        }
        this.clazz = clazz;
        try {
            this.valueOf = clazz.getMethod("valueOf", String.class);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Object convertToType(Object value) throws LocalizedException {
        if (value == null) {
            return null;
        }
        if (!clazz.isInstance(value)) {
            String str = ConvertUtils.convert(value);
            try {
                return valueOf.invoke(null, str);
            } catch (IllegalAccessException ex) {
                throw new LocalizedException("org.talend.ws.exception.illegalAccessValueOf", new String[]{clazz.
                            getName()}, ex);
            } catch (InvocationTargetException ex) {
                if (ex.getTargetException() instanceof IllegalArgumentException) {
                    throw new InvalidEnumValueException(str, clazz.getName());
                } else {
                    throw new LocalizedException("org.talend.ws.exception.Unknown", ex.
                            getTargetException());
                }

            }
        } else {
            return value;
        }
    }

    public Object typeToValue(Object bean) throws LocalizedException {
        if (bean == null) {
            return null;
        } else {
            return bean.toString();
        }
    }
}
