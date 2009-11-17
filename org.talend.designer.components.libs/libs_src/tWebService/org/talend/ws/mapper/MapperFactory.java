/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.talend.ws.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.wsdl.Message;
import javax.wsdl.Part;
import javax.xml.namespace.QName;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaComplexContent;
import org.apache.ws.commons.schema.XmlSchemaComplexContentExtension;
import org.apache.ws.commons.schema.XmlSchemaComplexContentRestriction;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaContent;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaObject;
import org.apache.ws.commons.schema.XmlSchemaParticle;
import org.apache.ws.commons.schema.XmlSchemaSequence;
import org.apache.ws.commons.schema.XmlSchemaSimpleType;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeContent;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeRestriction;
import org.apache.ws.commons.schema.XmlSchemaType;
import org.talend.ws.exception.LocalizedException;

/**
 *
 * @author rlamarche
 */
public class MapperFactory {

    private static final Map<String, String> BUILTIN_DATATYPES_MAP;
    private static final Map<String, String> BUILTIN_DATATYPES_MAP_REVERSE;
    private static final QName ANYTYPE_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "anyType");

    static {
        BUILTIN_DATATYPES_MAP = new HashMap<String, String>();
        BUILTIN_DATATYPES_MAP.put("string", "java.lang.String");
        BUILTIN_DATATYPES_MAP.put("integer", "java.math.BigInteger");
        BUILTIN_DATATYPES_MAP.put("int", "java.lang.Integer");
        BUILTIN_DATATYPES_MAP.put("long", "java.lang.Long");
        BUILTIN_DATATYPES_MAP.put("short", "java.lang.Short");
        BUILTIN_DATATYPES_MAP.put("decimal", "java.math.BigDecimal");
        BUILTIN_DATATYPES_MAP.put("float", "java.lang.Float");
        BUILTIN_DATATYPES_MAP.put("double", "java.lang.Double");
        BUILTIN_DATATYPES_MAP.put("boolean", "java.lang.Boolean");
        BUILTIN_DATATYPES_MAP.put("byte", " java.lang.Byte");
        BUILTIN_DATATYPES_MAP.put("QName", "javax.xml.namespace.QName");
        BUILTIN_DATATYPES_MAP.put("dateTime", "javax.xml.datatype.XMLGregorianCalendar");
        BUILTIN_DATATYPES_MAP.put("base64Binary", "byte[]");
        BUILTIN_DATATYPES_MAP.put("hexBinary", "byte[]");
        BUILTIN_DATATYPES_MAP.put("unsignedInt", "java.lang.Long");
        BUILTIN_DATATYPES_MAP.put("unsignedShort", "java.lang.Short");
        BUILTIN_DATATYPES_MAP.put("unsignedByte", "java.lang.Byte");
        BUILTIN_DATATYPES_MAP.put("time", "javax.xml.datatype.XMLGregorianCalendar");
        BUILTIN_DATATYPES_MAP.put("date", "javax.xml.datatype.XMLGregorianCalendar");
        BUILTIN_DATATYPES_MAP.put("gYear", "javax.xml.datatype.XMLGregorianCalendar");
        BUILTIN_DATATYPES_MAP.put("gYearMonth", "javax.xml.datatype.XMLGregorianCalendar");
        BUILTIN_DATATYPES_MAP.put("gMonth", "javax.xml.datatype.XMLGregorianCalendar");
        BUILTIN_DATATYPES_MAP.put("gMonthDay", "javax.xml.datatype.XMLGregorianCalendar");
        BUILTIN_DATATYPES_MAP.put("gDay", "javax.xml.datatype.XMLGregorianCalendar");
        BUILTIN_DATATYPES_MAP.put("duration", "javax.xml.datatype.Duration");
        BUILTIN_DATATYPES_MAP.put("NOTATION", "javax.xml.namespace.QName");
        BUILTIN_DATATYPES_MAP.put("string", "java.lang.String");

        BUILTIN_DATATYPES_MAP_REVERSE = new HashMap<String, String>();
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.String", "string");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.math.BigInteger", "integer");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.Integer", "int");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.Long", "long");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.Short", "short");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.math.BigDecimal", "decimal");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.Float", "float");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.Double", "double");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.Boolean", "boolean");
        BUILTIN_DATATYPES_MAP_REVERSE.put(" java.lang.Byte", "byte");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.namespace.QName", "QName");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.datatype.XMLGregorianCalendar", "dateTime");
        BUILTIN_DATATYPES_MAP_REVERSE.put("byte[]", "base64Binary");
        BUILTIN_DATATYPES_MAP_REVERSE.put("byte[]", "hexBinary");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.Long", "unsignedInt");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.Short", "unsignedShort");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.Byte", "unsignedByte");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.datatype.XMLGregorianCalendar", "time");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.datatype.XMLGregorianCalendar", "date");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.datatype.XMLGregorianCalendar", "gYear");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.datatype.XMLGregorianCalendar", "gYearMonth");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.datatype.XMLGregorianCalendar", "gMonth");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.datatype.XMLGregorianCalendar", "gMonthDay");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.datatype.XMLGregorianCalendar", "gDay");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.datatype.Duration", "duration");
        BUILTIN_DATATYPES_MAP_REVERSE.put("javax.xml.namespace.QName", "NOTATION");
        BUILTIN_DATATYPES_MAP_REVERSE.put("java.lang.String", "string");

    }
    protected ClassMapper classMapper;
    protected XmlSchemaCollection schemaCollection;

    public MapperFactory(ClassMapper classMapper, XmlSchemaCollection schemaCollection) {
        this.classMapper = classMapper;
        this.schemaCollection = schemaCollection;
    }

    public XmlSchemaCollection getSchemaCollection() {
        return schemaCollection;
    }

    public ClassMapper getClassMapper() {
        return classMapper;
    }

    public TypeMapper createTypeMapper(XmlSchemaType xmlSchemaType) throws LocalizedException {

        if (xmlSchemaType instanceof XmlSchemaComplexType) {
            return createComplexTypeMapper((XmlSchemaComplexType) xmlSchemaType);
        } else if (xmlSchemaType instanceof XmlSchemaSimpleType) {
            if (ANYTYPE_QNAME.equals(xmlSchemaType.getQName())) {
                return new AnyTypeMapper(this);
            } else {
                return createSimpleTypeMapper((XmlSchemaSimpleType) xmlSchemaType);
            }
        } else {
            throw new IllegalArgumentException("Type " + xmlSchemaType.getClass().
                    getName() + " is not yes supported.");
        }
    }

    public PropertyMapper createPropertyMapper(XmlSchemaElement xmlSchemaElement, Class<?> clazz) throws LocalizedException {

        XmlSchemaType xmlSchemaType = xmlSchemaElement.getSchemaType();
        TypeMapper xmlBeanMapper = createTypeMapper(xmlSchemaType);

        if (xmlSchemaElement.getMaxOccurs() > 1) {
            return new ListPropertyMapper(clazz, xmlBeanMapper, xmlSchemaElement.getName());
        } else {
            return new SimplePropertyMapper(clazz, xmlBeanMapper, xmlSchemaElement.getName());
        }
    }

    /**
     * Return the message mapper or null if the message does not have any parts or is null
     * @param message
     * @return
     */
    public MessageMapper createMessageMapper(Message message) throws LocalizedException {
        if (message == null) {
            return new EmptyMessageMapper();
        }

        Map<String, Part> parts = message.getParts();
        if (parts == null || parts != null && parts.size() == 0) {
            return new EmptyMessageMapper();
        } else {
            Map<String, TypeMapper> mappers = new HashMap<String, TypeMapper>(parts.size());

            for (Map.Entry<String, Part> entry : parts.entrySet()) {
                String name = entry.getKey();
                Part part = entry.getValue();

                XmlSchemaType xmlSchemaType = null;
                QName elementName = part.getElementName();
                if (elementName == null) {
                    QName typeName = part.getTypeName();
                    if (typeName == null) {
                        throw new IllegalArgumentException("Unable to find type of part " + part.getName() + " for message " + message.getQName());
                    }

                    xmlSchemaType = schemaCollection.getTypeByQName(typeName);
                    if (xmlSchemaType.getName() == null) {
                        xmlSchemaType.setName(typeName.getLocalPart());
                    }
                } else {
                    XmlSchemaElement xmlSchemaElement = schemaCollection.getElementByQName(part.getElementName());
                    xmlSchemaType = xmlSchemaElement.getSchemaType();
                    if (xmlSchemaType.getName() == null) {
                        xmlSchemaType.setName(xmlSchemaElement.getName());
                    }
                }
                if (xmlSchemaType == null) {
                    throw new IllegalArgumentException("Type " + part.getElementName() + " was not found in the schema.");
                }

                mappers.put(name, createTypeMapper(xmlSchemaType));
            }

            return new MessageMapperImpl(mappers, message);
        }
    }

    private TypeMapper createSimpleTypeMapper(XmlSchemaSimpleType xmlSchemaSimpleType) {


        XmlSchemaSimpleTypeContent xmlSchemaSimpleTypeContent = xmlSchemaSimpleType.getContent();

        if (xmlSchemaSimpleTypeContent == null) {
            // simple type
            String className = builtInTypeToJavaType(xmlSchemaSimpleType.getName());
            if (className == null) {
                throw new IllegalArgumentException("Unsupported type " + xmlSchemaSimpleType.getQName());
            }
            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                throw new IllegalArgumentException("Unable to find java type " + className, ex);
            }

            return new SimpleTypeMapper(clazz);
        } else {
            if (xmlSchemaSimpleTypeContent instanceof XmlSchemaSimpleTypeRestriction) {
                Class<?> clazz = classMapper.getClassForType(xmlSchemaSimpleType);
                if (!clazz.isEnum()) {
                    throw new IllegalArgumentException("Class " + clazz.getName() + " should be an enum.");
                }

                return new EnumTypeMapper(clazz);
            } else {
                throw new IllegalArgumentException("Unsupported type " + xmlSchemaSimpleTypeContent.getClass().getName());
            }
        }
    }

    private ComplexTypeMapper createComplexTypeMapper(XmlSchemaComplexType xmlSchemaComplexType) throws LocalizedException {

        Map<String, PropertyMapper> mappers = createPropertyMappers(xmlSchemaComplexType);

        ListOrderedMap orderedMap = (ListOrderedMap) mappers;

        return new ComplexTypeMapper(mappers, classMapper.getClassForType(xmlSchemaComplexType), orderedMap.keyList());
    }

    private Map<String, PropertyMapper> createPropertyMappers(XmlSchemaComplexType xmlSchemaComplexType) throws LocalizedException {

        Class<?> clazz = classMapper.getClassForType(xmlSchemaComplexType);

        XmlSchemaComplexContent xmlSchemaComplexContent = (XmlSchemaComplexContent) xmlSchemaComplexType.getContentModel();

        Map<String, PropertyMapper> mappers = new ListOrderedMap();

        if (xmlSchemaComplexContent != null) {
            XmlSchemaContent xmlSchemaContent = xmlSchemaComplexContent.getContent();
            if (xmlSchemaContent instanceof XmlSchemaComplexContentExtension) {
                XmlSchemaComplexContentExtension xmlSchemaComplexContentExtension = (XmlSchemaComplexContentExtension) xmlSchemaContent;
                XmlSchemaComplexType baseXmlSchemaComplexType = (XmlSchemaComplexType) schemaCollection.getTypeByQName(xmlSchemaComplexContentExtension.getBaseTypeName());
                // First, recursion on parent class (for properties order)
                mappers.putAll(createPropertyMappers(baseXmlSchemaComplexType));

                if (xmlSchemaComplexContentExtension.getParticle() instanceof XmlSchemaSequence) {
                    mappers.putAll(createPropertyMappers((XmlSchemaSequence) xmlSchemaComplexContentExtension.getParticle(), clazz));
                }




            } else if (xmlSchemaContent instanceof XmlSchemaComplexContentRestriction) {
                throw new IllegalArgumentException("XmlSchemaComplexContentRestriction is not yet supported.");
            } else {
                throw new IllegalArgumentException("Invalid XmlSchemaContent for a XmlSchemaComplexContent.");
            }
        } else {
            XmlSchemaParticle xmlSchemaParticle = xmlSchemaComplexType.getParticle();
            if (xmlSchemaParticle instanceof XmlSchemaSequence) {
                mappers.putAll(createPropertyMappers((XmlSchemaSequence) xmlSchemaParticle, clazz));
            }
        }

        return mappers;
    }

    private Map<String, PropertyMapper> createPropertyMappers(XmlSchemaSequence xmlSchemaSequence, Class<?> clazz) throws LocalizedException {

        Map<String, PropertyMapper> map = new ListOrderedMap();

        Iterator<XmlSchemaObject> iterator = xmlSchemaSequence.getItems().
                getIterator();

        while (iterator.hasNext()) {
            XmlSchemaObject xmlSchemaObject = iterator.next();
            if (xmlSchemaObject instanceof XmlSchemaElement) {
                XmlSchemaElement xmlSchemaElement = (XmlSchemaElement) xmlSchemaObject;

                map.put(xmlSchemaElement.getName(), createPropertyMapper(xmlSchemaElement, clazz));
            }
        }

        return map;
    }

    private static String builtInTypeToJavaType(String type) {
        return BUILTIN_DATATYPES_MAP.get(type);
    }
    public static QName javaTypeToBuiltInType(String type) {
        return new QName("http://www.w3.org/2001/XMLSchema", BUILTIN_DATATYPES_MAP_REVERSE.get(type));
    }
}
