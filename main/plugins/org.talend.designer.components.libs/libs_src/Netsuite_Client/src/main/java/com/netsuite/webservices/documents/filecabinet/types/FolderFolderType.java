
package com.netsuite.webservices.documents.filecabinet.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FolderFolderType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FolderFolderType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_appPackages"/>
 *     &lt;enumeration value="_attachmentsReceived"/>
 *     &lt;enumeration value="_attachmentsToSend"/>
 *     &lt;enumeration value="_certificates"/>
 *     &lt;enumeration value="_documentsAndFiles"/>
 *     &lt;enumeration value="_emailTemplates"/>
 *     &lt;enumeration value="_faxTemplates"/>
 *     &lt;enumeration value="_images"/>
 *     &lt;enumeration value="_letterTemplates"/>
 *     &lt;enumeration value="_mailMerge"/>
 *     &lt;enumeration value="_marketingTemplates"/>
 *     &lt;enumeration value="_pdfTemplates"/>
 *     &lt;enumeration value="_suitebundles"/>
 *     &lt;enumeration value="_suitecommerceAdvancedSiteTemplates"/>
 *     &lt;enumeration value="_suitescripts"/>
 *     &lt;enumeration value="_templates"/>
 *     &lt;enumeration value="_webSiteHostingFiles"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FolderFolderType", namespace = "urn:types.filecabinet_2014_2.documents.webservices.netsuite.com")
@XmlEnum
public enum FolderFolderType {

    @XmlEnumValue("_appPackages")
    APP_PACKAGES("_appPackages"),
    @XmlEnumValue("_attachmentsReceived")
    ATTACHMENTS_RECEIVED("_attachmentsReceived"),
    @XmlEnumValue("_attachmentsToSend")
    ATTACHMENTS_TO_SEND("_attachmentsToSend"),
    @XmlEnumValue("_certificates")
    CERTIFICATES("_certificates"),
    @XmlEnumValue("_documentsAndFiles")
    DOCUMENTS_AND_FILES("_documentsAndFiles"),
    @XmlEnumValue("_emailTemplates")
    EMAIL_TEMPLATES("_emailTemplates"),
    @XmlEnumValue("_faxTemplates")
    FAX_TEMPLATES("_faxTemplates"),
    @XmlEnumValue("_images")
    IMAGES("_images"),
    @XmlEnumValue("_letterTemplates")
    LETTER_TEMPLATES("_letterTemplates"),
    @XmlEnumValue("_mailMerge")
    MAIL_MERGE("_mailMerge"),
    @XmlEnumValue("_marketingTemplates")
    MARKETING_TEMPLATES("_marketingTemplates"),
    @XmlEnumValue("_pdfTemplates")
    PDF_TEMPLATES("_pdfTemplates"),
    @XmlEnumValue("_suitebundles")
    SUITEBUNDLES("_suitebundles"),
    @XmlEnumValue("_suitecommerceAdvancedSiteTemplates")
    SUITECOMMERCE_ADVANCED_SITE_TEMPLATES("_suitecommerceAdvancedSiteTemplates"),
    @XmlEnumValue("_suitescripts")
    SUITESCRIPTS("_suitescripts"),
    @XmlEnumValue("_templates")
    TEMPLATES("_templates"),
    @XmlEnumValue("_webSiteHostingFiles")
    WEB_SITE_HOSTING_FILES("_webSiteHostingFiles");
    private final String value;

    FolderFolderType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FolderFolderType fromValue(String v) {
        for (FolderFolderType c: FolderFolderType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
