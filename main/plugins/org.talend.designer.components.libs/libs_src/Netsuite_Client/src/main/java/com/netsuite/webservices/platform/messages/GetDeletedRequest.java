
package com.netsuite.webservices.platform.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.netsuite.webservices.platform.core.GetDeletedFilter;


/**
 * <p>Java class for GetDeletedRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDeletedRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getDeletedFilter" type="{urn:core_2014_2.platform.webservices.netsuite.com}GetDeletedFilter"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDeletedRequest", propOrder = {
    "getDeletedFilter"
})
public class GetDeletedRequest {

    @XmlElement(required = true)
    protected GetDeletedFilter getDeletedFilter;

    /**
     * Gets the value of the getDeletedFilter property.
     * 
     * @return
     *     possible object is
     *     {@link GetDeletedFilter }
     *     
     */
    public GetDeletedFilter getGetDeletedFilter() {
        return getDeletedFilter;
    }

    /**
     * Sets the value of the getDeletedFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetDeletedFilter }
     *     
     */
    public void setGetDeletedFilter(GetDeletedFilter value) {
        this.getDeletedFilter = value;
    }

}
