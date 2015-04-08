
package com.netsuite.webservices.platform.common.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PostingPeriodDate.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PostingPeriodDate">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_firstFiscalQuarterOfLastFy"/>
 *     &lt;enumeration value="_firstFiscalQuarterOfThisFy"/>
 *     &lt;enumeration value="_fiscalQuarterBeforeLast"/>
 *     &lt;enumeration value="_fiscalYearBeforeLast"/>
 *     &lt;enumeration value="_fourthFiscalQuarterOfLastFy"/>
 *     &lt;enumeration value="_fourthFiscalQuarterOfThisFy"/>
 *     &lt;enumeration value="_lastFiscalQuarter"/>
 *     &lt;enumeration value="_lastFiscalQuarterOneFiscalYearAgo"/>
 *     &lt;enumeration value="_lastFiscalQuarterToPeriod"/>
 *     &lt;enumeration value="_lastFiscalYear"/>
 *     &lt;enumeration value="_lastFiscalYearToPeriod"/>
 *     &lt;enumeration value="_lastPeriod"/>
 *     &lt;enumeration value="_lastPeriodOneFiscalQuarterAgo"/>
 *     &lt;enumeration value="_lastPeriodOneFiscalYearAgo"/>
 *     &lt;enumeration value="_lastRolling18periods"/>
 *     &lt;enumeration value="_lastRolling6fiscalQuarters"/>
 *     &lt;enumeration value="_periodBeforeLast"/>
 *     &lt;enumeration value="_sameFiscalQuarterOfLastFy"/>
 *     &lt;enumeration value="_sameFiscalQuarterOfLastFyToPeriod"/>
 *     &lt;enumeration value="_samePeriodOfLastFiscalQuarter"/>
 *     &lt;enumeration value="_samePeriodOfLastFy"/>
 *     &lt;enumeration value="_secondFiscalQuarterOfLastFy"/>
 *     &lt;enumeration value="_secondFiscalQuarterOfThisFy"/>
 *     &lt;enumeration value="_thirdFiscalQuarterOfLastFy"/>
 *     &lt;enumeration value="_thirdFiscalQuarterOfThisFy"/>
 *     &lt;enumeration value="_thisFiscalQuarter"/>
 *     &lt;enumeration value="_thisFiscalQuarterToPeriod"/>
 *     &lt;enumeration value="_thisFiscalYear"/>
 *     &lt;enumeration value="_thisFiscalYearToPeriod"/>
 *     &lt;enumeration value="_thisPeriod"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PostingPeriodDate", namespace = "urn:types.common_2014_2.platform.webservices.netsuite.com")
@XmlEnum
public enum PostingPeriodDate {

    @XmlEnumValue("_firstFiscalQuarterOfLastFy")
    FIRST_FISCAL_QUARTER_OF_LAST_FY("_firstFiscalQuarterOfLastFy"),
    @XmlEnumValue("_firstFiscalQuarterOfThisFy")
    FIRST_FISCAL_QUARTER_OF_THIS_FY("_firstFiscalQuarterOfThisFy"),
    @XmlEnumValue("_fiscalQuarterBeforeLast")
    FISCAL_QUARTER_BEFORE_LAST("_fiscalQuarterBeforeLast"),
    @XmlEnumValue("_fiscalYearBeforeLast")
    FISCAL_YEAR_BEFORE_LAST("_fiscalYearBeforeLast"),
    @XmlEnumValue("_fourthFiscalQuarterOfLastFy")
    FOURTH_FISCAL_QUARTER_OF_LAST_FY("_fourthFiscalQuarterOfLastFy"),
    @XmlEnumValue("_fourthFiscalQuarterOfThisFy")
    FOURTH_FISCAL_QUARTER_OF_THIS_FY("_fourthFiscalQuarterOfThisFy"),
    @XmlEnumValue("_lastFiscalQuarter")
    LAST_FISCAL_QUARTER("_lastFiscalQuarter"),
    @XmlEnumValue("_lastFiscalQuarterOneFiscalYearAgo")
    LAST_FISCAL_QUARTER_ONE_FISCAL_YEAR_AGO("_lastFiscalQuarterOneFiscalYearAgo"),
    @XmlEnumValue("_lastFiscalQuarterToPeriod")
    LAST_FISCAL_QUARTER_TO_PERIOD("_lastFiscalQuarterToPeriod"),
    @XmlEnumValue("_lastFiscalYear")
    LAST_FISCAL_YEAR("_lastFiscalYear"),
    @XmlEnumValue("_lastFiscalYearToPeriod")
    LAST_FISCAL_YEAR_TO_PERIOD("_lastFiscalYearToPeriod"),
    @XmlEnumValue("_lastPeriod")
    LAST_PERIOD("_lastPeriod"),
    @XmlEnumValue("_lastPeriodOneFiscalQuarterAgo")
    LAST_PERIOD_ONE_FISCAL_QUARTER_AGO("_lastPeriodOneFiscalQuarterAgo"),
    @XmlEnumValue("_lastPeriodOneFiscalYearAgo")
    LAST_PERIOD_ONE_FISCAL_YEAR_AGO("_lastPeriodOneFiscalYearAgo"),
    @XmlEnumValue("_lastRolling18periods")
    LAST_ROLLING_18_PERIODS("_lastRolling18periods"),
    @XmlEnumValue("_lastRolling6fiscalQuarters")
    LAST_ROLLING_6_FISCAL_QUARTERS("_lastRolling6fiscalQuarters"),
    @XmlEnumValue("_periodBeforeLast")
    PERIOD_BEFORE_LAST("_periodBeforeLast"),
    @XmlEnumValue("_sameFiscalQuarterOfLastFy")
    SAME_FISCAL_QUARTER_OF_LAST_FY("_sameFiscalQuarterOfLastFy"),
    @XmlEnumValue("_sameFiscalQuarterOfLastFyToPeriod")
    SAME_FISCAL_QUARTER_OF_LAST_FY_TO_PERIOD("_sameFiscalQuarterOfLastFyToPeriod"),
    @XmlEnumValue("_samePeriodOfLastFiscalQuarter")
    SAME_PERIOD_OF_LAST_FISCAL_QUARTER("_samePeriodOfLastFiscalQuarter"),
    @XmlEnumValue("_samePeriodOfLastFy")
    SAME_PERIOD_OF_LAST_FY("_samePeriodOfLastFy"),
    @XmlEnumValue("_secondFiscalQuarterOfLastFy")
    SECOND_FISCAL_QUARTER_OF_LAST_FY("_secondFiscalQuarterOfLastFy"),
    @XmlEnumValue("_secondFiscalQuarterOfThisFy")
    SECOND_FISCAL_QUARTER_OF_THIS_FY("_secondFiscalQuarterOfThisFy"),
    @XmlEnumValue("_thirdFiscalQuarterOfLastFy")
    THIRD_FISCAL_QUARTER_OF_LAST_FY("_thirdFiscalQuarterOfLastFy"),
    @XmlEnumValue("_thirdFiscalQuarterOfThisFy")
    THIRD_FISCAL_QUARTER_OF_THIS_FY("_thirdFiscalQuarterOfThisFy"),
    @XmlEnumValue("_thisFiscalQuarter")
    THIS_FISCAL_QUARTER("_thisFiscalQuarter"),
    @XmlEnumValue("_thisFiscalQuarterToPeriod")
    THIS_FISCAL_QUARTER_TO_PERIOD("_thisFiscalQuarterToPeriod"),
    @XmlEnumValue("_thisFiscalYear")
    THIS_FISCAL_YEAR("_thisFiscalYear"),
    @XmlEnumValue("_thisFiscalYearToPeriod")
    THIS_FISCAL_YEAR_TO_PERIOD("_thisFiscalYearToPeriod"),
    @XmlEnumValue("_thisPeriod")
    THIS_PERIOD("_thisPeriod");
    private final String value;

    PostingPeriodDate(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PostingPeriodDate fromValue(String v) {
        for (PostingPeriodDate c: PostingPeriodDate.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
