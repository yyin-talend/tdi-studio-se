
package com.netsuite.webservices.platform.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.netsuite.webservices.platform.core.CurrencyRateFilter;


/**
 * <p>Java class for GetCurrencyRateRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCurrencyRateRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="currencyRateFilter" type="{urn:core_2014_2.platform.webservices.netsuite.com}CurrencyRateFilter"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCurrencyRateRequest", propOrder = {
    "currencyRateFilter"
})
public class GetCurrencyRateRequest {

    @XmlElement(required = true)
    protected CurrencyRateFilter currencyRateFilter;

    /**
     * Gets the value of the currencyRateFilter property.
     * 
     * @return
     *     possible object is
     *     {@link CurrencyRateFilter }
     *     
     */
    public CurrencyRateFilter getCurrencyRateFilter() {
        return currencyRateFilter;
    }

    /**
     * Sets the value of the currencyRateFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurrencyRateFilter }
     *     
     */
    public void setCurrencyRateFilter(CurrencyRateFilter value) {
        this.currencyRateFilter = value;
    }

}
