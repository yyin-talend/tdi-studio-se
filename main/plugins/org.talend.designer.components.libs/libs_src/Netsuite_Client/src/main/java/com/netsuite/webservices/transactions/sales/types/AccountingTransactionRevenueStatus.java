
package com.netsuite.webservices.transactions.sales.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountingTransactionRevenueStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AccountingTransactionRevenueStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_completed"/>
 *     &lt;enumeration value="_inProgress"/>
 *     &lt;enumeration value="_onRevCommitment"/>
 *     &lt;enumeration value="_pending"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AccountingTransactionRevenueStatus", namespace = "urn:types.sales_2014_2.transactions.webservices.netsuite.com")
@XmlEnum
public enum AccountingTransactionRevenueStatus {

    @XmlEnumValue("_completed")
    COMPLETED("_completed"),
    @XmlEnumValue("_inProgress")
    IN_PROGRESS("_inProgress"),
    @XmlEnumValue("_onRevCommitment")
    ON_REV_COMMITMENT("_onRevCommitment"),
    @XmlEnumValue("_pending")
    PENDING("_pending");
    private final String value;

    AccountingTransactionRevenueStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AccountingTransactionRevenueStatus fromValue(String v) {
        for (AccountingTransactionRevenueStatus c: AccountingTransactionRevenueStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
