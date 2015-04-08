
package com.netsuite.webservices.platform.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.netsuite.webservices.platform.core.GetDeletedResult;


/**
 * <p>Java class for GetDeletedResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDeletedResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:core_2014_2.platform.webservices.netsuite.com}getDeletedResult"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDeletedResponse", propOrder = {
    "getDeletedResult"
})
public class GetDeletedResponse {

    @XmlElement(namespace = "urn:core_2014_2.platform.webservices.netsuite.com", required = true)
    protected GetDeletedResult getDeletedResult;

    /**
     * Gets the value of the getDeletedResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetDeletedResult }
     *     
     */
    public GetDeletedResult getGetDeletedResult() {
        return getDeletedResult;
    }

    /**
     * Sets the value of the getDeletedResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetDeletedResult }
     *     
     */
    public void setGetDeletedResult(GetDeletedResult value) {
        this.getDeletedResult = value;
    }

}
