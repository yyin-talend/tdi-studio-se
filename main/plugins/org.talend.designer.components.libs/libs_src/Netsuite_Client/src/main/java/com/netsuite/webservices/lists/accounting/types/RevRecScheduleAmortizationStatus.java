
package com.netsuite.webservices.lists.accounting.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RevRecScheduleAmortizationStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RevRecScheduleAmortizationStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_notStarted"/>
 *     &lt;enumeration value="_inProgress"/>
 *     &lt;enumeration value="_completed"/>
 *     &lt;enumeration value="_onHold"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RevRecScheduleAmortizationStatus", namespace = "urn:types.accounting_2014_2.lists.webservices.netsuite.com")
@XmlEnum
public enum RevRecScheduleAmortizationStatus {

    @XmlEnumValue("_notStarted")
    NOT_STARTED("_notStarted"),
    @XmlEnumValue("_inProgress")
    IN_PROGRESS("_inProgress"),
    @XmlEnumValue("_completed")
    COMPLETED("_completed"),
    @XmlEnumValue("_onHold")
    ON_HOLD("_onHold");
    private final String value;

    RevRecScheduleAmortizationStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RevRecScheduleAmortizationStatus fromValue(String v) {
        for (RevRecScheduleAmortizationStatus c: RevRecScheduleAmortizationStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
