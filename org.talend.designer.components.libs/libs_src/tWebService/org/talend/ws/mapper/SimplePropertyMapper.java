package org.talend.ws.mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.beanutils.PropertyUtils;
import org.talend.ws.exception.IllegalPropertyAccessException;
import org.talend.ws.exception.InvocationTargetPropertyAccessor;
import org.talend.ws.exception.LocalizedException;
import org.talend.ws.exception.NoSuchPropertyException;

/**
 * 
 * @author rlamarche
 */
public class SimplePropertyMapper implements PropertyMapper {

    private TypeMapper xmlBeanMapper;

    private String propertyName;

    private PropertyDescriptor propertyDescriptor;

    public SimplePropertyMapper(Class<?> clazz, TypeMapper xmlBeanMapper, String propertyName) throws LocalizedException {
        this.xmlBeanMapper = xmlBeanMapper;
        this.propertyName = propertyName;
        try {
            Object newInstance = clazz.newInstance();
            propertyDescriptor = PropertyUtils.getPropertyDescriptor(clazz.newInstance(), propertyName);
            // bchen, try again ignore the character case
            if (propertyDescriptor == null) {
                PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(newInstance);
                for (PropertyDescriptor pd : pds) {
                    if (pd.getName().equalsIgnoreCase(propertyName.toLowerCase())) {
                        propertyName = pd.getName();
                        propertyDescriptor = pd;
                        break;
                    }
                }
                // propertyDescriptor = PropertyUtils.getPropertyDescriptor(newInstance, propertyName);
            }
            // bchen end
        } catch (IllegalAccessException ex) {
            throw new IllegalPropertyAccessException(propertyName, clazz.getName(), ex);
        } catch (InvocationTargetException ex) {
            throw new InvocationTargetPropertyAccessor(propertyName, clazz.getName(), ex.getTargetException());
        } catch (NoSuchMethodException ex) {
            throw new NoSuchPropertyException(propertyName, clazz.getName(), ex);
        } catch (InstantiationException ex) {
            throw new LocalizedException("org.talend.ws.exception.Instantiation", new String[] { clazz.getName() }, ex);
        }
    }

    public String getMappedPropertyName() {
        return propertyName;
    }

    public void setValueTo(Object destination, Object value) throws LocalizedException {
        try {
            Method method = propertyDescriptor.getWriteMethod();
            if (method.getParameterTypes()[0].equals(JAXBElement.class)) {
                value = new JAXBElement(new QName(getMappedPropertyName()), getMappedClass(), xmlBeanMapper.convertToType(value));
                propertyDescriptor.getWriteMethod().invoke(destination, value);
            } else {
                propertyDescriptor.getWriteMethod().invoke(destination, xmlBeanMapper.convertToType(value));
            }
        } catch (IllegalAccessException ex) {
            throw new IllegalPropertyAccessException(propertyDescriptor.getName(), destination.getClass().getName(), ex);
        } catch (InvocationTargetException ex) {
            throw new InvocationTargetPropertyAccessor(propertyDescriptor.getName(), destination.getClass().getName(), ex
                    .getTargetException());
        } catch (IllegalArgumentException ex) {
            throw ex;
        }
    }

    public Object getValueFrom(Object source) throws LocalizedException {
        try {
            Method method = propertyDescriptor.getReadMethod();
            if (method.getReturnType().equals(JAXBElement.class)) {
                JAXBElement jAXBElement = (JAXBElement) method.invoke(source);
                return xmlBeanMapper.typeToValue(jAXBElement.getValue());
            } else {
                return xmlBeanMapper.typeToValue(propertyDescriptor.getReadMethod().invoke(source));
            }
        } catch (IllegalAccessException ex) {
            throw new IllegalPropertyAccessException(propertyDescriptor.getName(), source.getClass().getName(), ex);
        } catch (InvocationTargetException ex) {
            throw new InvocationTargetPropertyAccessor(propertyDescriptor.getName(), source.getClass().getName(), ex
                    .getTargetException());
        }
    }

    public Class<?> getMappedClass() {
        return xmlBeanMapper.getClazz();
    }

    public Object createProperty(Object value) throws LocalizedException {
        return xmlBeanMapper.convertToType(value);
    }

    public Object createValue(Object property) throws LocalizedException {
        return xmlBeanMapper.typeToValue(property);
    }
}
