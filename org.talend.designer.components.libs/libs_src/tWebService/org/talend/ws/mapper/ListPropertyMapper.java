/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.talend.ws.mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.talend.ws.exception.IllegalPropertyAccessException;
import org.talend.ws.exception.InvocationTargetPropertyAccessor;
import org.talend.ws.exception.LocalizedException;

/**
 *
 * @author rlamarche
 */
public class ListPropertyMapper implements PropertyMapper {

    private TypeMapper xmlBeanMapper;
    private String propertyName;
    private PropertyDescriptor propertyDescriptor;

    public ListPropertyMapper(Class<?> clazz, TypeMapper xmlBeanMapper, String propertyName) {
        this.xmlBeanMapper = xmlBeanMapper;

        try {
            propertyDescriptor = PropertyUtils.getPropertyDescriptor(clazz.
                    newInstance(), propertyName);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to get propertyDescriptor for bean " + xmlBeanMapper.
                    getClazz().
                    getName() + " and property " + propertyName);
        }
    }

    public Class<?> getMappedClass() {
        return xmlBeanMapper.getClazz();
    }

    public String getMappedPropertyName() {
        return propertyName;
    }

    public void setValueTo(Object destination, Object value) throws LocalizedException {
        if (value == null) {
            return;
        }
        List values;
        if (value instanceof List) {
            values = (List) value;
        } else {
            values = Arrays.asList(value);
        }

        List target;
        try {
            target = (List) propertyDescriptor.getReadMethod().invoke(destination);
        } catch (IllegalAccessException ex) {
            throw new IllegalPropertyAccessException(propertyDescriptor.getName(), destination.
                    getClass().getName(), ex);
        } catch (InvocationTargetException ex) {
            throw new InvocationTargetPropertyAccessor(propertyDescriptor.
                    getName(), destination.getClass().getName(), ex);
        }

        for (Object val : values) {
            target.add(xmlBeanMapper.convertToType(val));
        }
    }

    public Object getValueFrom(Object source) throws LocalizedException {
        List values;
        try {
            values = (List) propertyDescriptor.getReadMethod().invoke(source);
        } catch (IllegalAccessException ex) {
            throw new IllegalPropertyAccessException(propertyDescriptor.getName(), source.
                    getClass().getName(), ex);
        } catch (InvocationTargetException ex) {
            throw new InvocationTargetPropertyAccessor(propertyDescriptor.
                    getName(), source.getClass().getName(), ex);
        }

        List newValues = new ArrayList(values.size());
        for (Object value : values) {
            newValues.add(xmlBeanMapper.typeToValue(value));
        }

        return newValues;
    }

    public Object createProperty(Object value) throws LocalizedException {
        if (value == null) {
            return null;
        }
        List values;
        if (value instanceof List) {
            values = (List) value;
        } else {
            values = Arrays.asList(value);
        }

        List property = new ArrayList(values.size());
        for (Object val : values) {
            property.add(xmlBeanMapper.convertToType(value));
        }

        return property;
    }

    public Object createValue(Object property) throws LocalizedException {
        if (property == null) {
            return null;
        }
        if (!(property instanceof List)) {
            throw new IllegalArgumentException("You must provide a list of properties");
        }
        List<Object> properties = (List<Object>) property;
        List<Object> values = new ArrayList<Object>(properties.size());

        for (Object prop : properties) {
            values.add(xmlBeanMapper.typeToValue(prop));
        }

        return values;
    }
}
