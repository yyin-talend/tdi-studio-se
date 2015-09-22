/**
 * ExceptionCode.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner.fault;

/**
 * ExceptionCode bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class ExceptionCode implements org.apache.axis2.databinding.ADBBean {

    public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com",
            "ExceptionCode", "ns3");

    /**
     * field for ExceptionCode
     */

    protected java.lang.String localExceptionCode;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor

    protected ExceptionCode(java.lang.String value, boolean isRegisterValue) {
        localExceptionCode = value;
        if (isRegisterValue) {

            _table_.put(localExceptionCode, this);

        }

    }

    public static final java.lang.String _APEX_TRIGGER_COUPLING_LIMIT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("APEX_TRIGGER_COUPLING_LIMIT");

    public static final java.lang.String _API_CURRENTLY_DISABLED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("API_CURRENTLY_DISABLED");

    public static final java.lang.String _API_DISABLED_FOR_ORG = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("API_DISABLED_FOR_ORG");

    public static final java.lang.String _ARGUMENT_OBJECT_PARSE_ERROR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("ARGUMENT_OBJECT_PARSE_ERROR");

    public static final java.lang.String _ASYNC_OPERATION_LOCATOR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("ASYNC_OPERATION_LOCATOR");

    public static final java.lang.String _BATCH_PROCESSING_HALTED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("BATCH_PROCESSING_HALTED");

    public static final java.lang.String _BIG_OBJECT_UNSUPPORTED_OPERATION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("BIG_OBJECT_UNSUPPORTED_OPERATION");

    public static final java.lang.String _CANNOT_DELETE_ENTITY = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CANNOT_DELETE_ENTITY");

    public static final java.lang.String _CANNOT_DELETE_OWNER = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CANNOT_DELETE_OWNER");

    public static final java.lang.String _CANT_ADD_STANDADRD_PORTAL_USER_TO_TERRITORY = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CANT_ADD_STANDADRD_PORTAL_USER_TO_TERRITORY");

    public static final java.lang.String _CANT_ADD_STANDARD_PORTAL_USER_TO_TERRITORY = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CANT_ADD_STANDARD_PORTAL_USER_TO_TERRITORY");

    public static final java.lang.String _CIRCULAR_OBJECT_GRAPH = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CIRCULAR_OBJECT_GRAPH");

    public static final java.lang.String _CLIENT_NOT_ACCESSIBLE_FOR_USER = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CLIENT_NOT_ACCESSIBLE_FOR_USER");

    public static final java.lang.String _CLIENT_REQUIRE_UPDATE_FOR_USER = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CLIENT_REQUIRE_UPDATE_FOR_USER");

    public static final java.lang.String _CONTENT_HUB_AUTHENTICATION_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CONTENT_HUB_AUTHENTICATION_EXCEPTION");

    public static final java.lang.String _CONTENT_HUB_FILE_DOWNLOAD_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CONTENT_HUB_FILE_DOWNLOAD_EXCEPTION");

    public static final java.lang.String _CONTENT_HUB_FILE_NOT_FOUND_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CONTENT_HUB_FILE_NOT_FOUND_EXCEPTION");

    public static final java.lang.String _CONTENT_HUB_INVALID_OBJECT_TYPE_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CONTENT_HUB_INVALID_OBJECT_TYPE_EXCEPTION");

    public static final java.lang.String _CONTENT_HUB_INVALID_PAGE_NUMBER_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CONTENT_HUB_INVALID_PAGE_NUMBER_EXCEPTION");

    public static final java.lang.String _CONTENT_HUB_OPERATION_NOT_SUPPORTED_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CONTENT_HUB_OPERATION_NOT_SUPPORTED_EXCEPTION");

    public static final java.lang.String _CONTENT_HUB_SECURITY_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CONTENT_HUB_SECURITY_EXCEPTION");

    public static final java.lang.String _CONTENT_HUB_TIMEOUT_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CONTENT_HUB_TIMEOUT_EXCEPTION");

    public static final java.lang.String _CONTENT_HUB_UNEXPECTED_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CONTENT_HUB_UNEXPECTED_EXCEPTION");

    public static final java.lang.String _CUSTOM_METADATA_LIMIT_EXCEEDED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CUSTOM_METADATA_LIMIT_EXCEEDED");

    public static final java.lang.String _CUSTOM_SETTINGS_LIMIT_EXCEEDED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("CUSTOM_SETTINGS_LIMIT_EXCEEDED");

    public static final java.lang.String _DATACLOUD_API_CLIENT_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("DATACLOUD_API_CLIENT_EXCEPTION");

    public static final java.lang.String _DATACLOUD_API_DISABLED_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("DATACLOUD_API_DISABLED_EXCEPTION");

    public static final java.lang.String _DATACLOUD_API_SERVER_BUSY_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("DATACLOUD_API_SERVER_BUSY_EXCEPTION");

    public static final java.lang.String _DATACLOUD_API_SERVER_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("DATACLOUD_API_SERVER_EXCEPTION");

    public static final java.lang.String _DATACLOUD_API_TIMEOUT_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("DATACLOUD_API_TIMEOUT_EXCEPTION");

    public static final java.lang.String _DATACLOUD_API_UNAVAILABLE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("DATACLOUD_API_UNAVAILABLE");

    public static final java.lang.String _DUPLICATE_ARGUMENT_VALUE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("DUPLICATE_ARGUMENT_VALUE");

    public static final java.lang.String _DUPLICATE_VALUE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("DUPLICATE_VALUE");

    public static final java.lang.String _EMAIL_BATCH_SIZE_LIMIT_EXCEEDED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EMAIL_BATCH_SIZE_LIMIT_EXCEEDED");

    public static final java.lang.String _EMAIL_TO_CASE_INVALID_ROUTING = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EMAIL_TO_CASE_INVALID_ROUTING");

    public static final java.lang.String _EMAIL_TO_CASE_LIMIT_EXCEEDED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EMAIL_TO_CASE_LIMIT_EXCEEDED");

    public static final java.lang.String _EMAIL_TO_CASE_NOT_ENABLED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EMAIL_TO_CASE_NOT_ENABLED");

    public static final java.lang.String _ENVIRONMENT_HUB_MEMBERSHIP_CONFLICT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("ENVIRONMENT_HUB_MEMBERSHIP_CONFLICT");

    public static final java.lang.String _EXCEEDED_ID_LIMIT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EXCEEDED_ID_LIMIT");

    public static final java.lang.String _EXCEEDED_LEAD_CONVERT_LIMIT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EXCEEDED_LEAD_CONVERT_LIMIT");

    public static final java.lang.String _EXCEEDED_MAX_SIZE_REQUEST = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EXCEEDED_MAX_SIZE_REQUEST");

    public static final java.lang.String _EXCEEDED_MAX_TYPES_LIMIT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EXCEEDED_MAX_TYPES_LIMIT");

    public static final java.lang.String _EXCEEDED_QUOTA = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EXCEEDED_QUOTA");

    public static final java.lang.String _EXTERNAL_OBJECT_AUTHENTICATION_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EXTERNAL_OBJECT_AUTHENTICATION_EXCEPTION");

    public static final java.lang.String _EXTERNAL_OBJECT_CONNECTION_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EXTERNAL_OBJECT_CONNECTION_EXCEPTION");

    public static final java.lang.String _EXTERNAL_OBJECT_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EXTERNAL_OBJECT_EXCEPTION");

    public static final java.lang.String _EXTERNAL_OBJECT_UNSUPPORTED_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("EXTERNAL_OBJECT_UNSUPPORTED_EXCEPTION");

    public static final java.lang.String _FEDERATED_SEARCH_ERROR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("FEDERATED_SEARCH_ERROR");

    public static final java.lang.String _FEED_NOT_ENABLED_FOR_OBJECT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("FEED_NOT_ENABLED_FOR_OBJECT");

    public static final java.lang.String _FUNCTIONALITY_NOT_ENABLED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("FUNCTIONALITY_NOT_ENABLED");

    public static final java.lang.String _FUNCTIONALITY_TEMPORARILY_UNAVAILABLE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("FUNCTIONALITY_TEMPORARILY_UNAVAILABLE");

    public static final java.lang.String _ILLEGAL_QUERY_PARAMETER_VALUE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("ILLEGAL_QUERY_PARAMETER_VALUE");

    public static final java.lang.String _INACTIVE_OWNER_OR_USER = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INACTIVE_OWNER_OR_USER");

    public static final java.lang.String _INACTIVE_PORTAL = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INACTIVE_PORTAL");

    public static final java.lang.String _INSUFFICIENT_ACCESS = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INSUFFICIENT_ACCESS");

    public static final java.lang.String _INTERNAL_CANVAS_ERROR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INTERNAL_CANVAS_ERROR");

    public static final java.lang.String _INVALID_ASSIGNMENT_RULE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_ASSIGNMENT_RULE");

    public static final java.lang.String _INVALID_BATCH_REQUEST = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_BATCH_REQUEST");

    public static final java.lang.String _INVALID_BATCH_SIZE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_BATCH_SIZE");

    public static final java.lang.String _INVALID_CLIENT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_CLIENT");

    public static final java.lang.String _INVALID_CROSS_REFERENCE_KEY = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_CROSS_REFERENCE_KEY");

    public static final java.lang.String _INVALID_DATE_FORMAT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_DATE_FORMAT");

    public static final java.lang.String _INVALID_FIELD = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_FIELD");

    public static final java.lang.String _INVALID_FILTER_LANGUAGE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_FILTER_LANGUAGE");

    public static final java.lang.String _INVALID_FILTER_VALUE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_FILTER_VALUE");

    public static final java.lang.String _INVALID_ID_FIELD = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_ID_FIELD");

    public static final java.lang.String _INVALID_INPUT_COMBINATION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_INPUT_COMBINATION");

    public static final java.lang.String _INVALID_LOCALE_LANGUAGE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_LOCALE_LANGUAGE");

    public static final java.lang.String _INVALID_LOCATOR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_LOCATOR");

    public static final java.lang.String _INVALID_LOGIN = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_LOGIN");

    public static final java.lang.String _INVALID_MULTIPART_REQUEST = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_MULTIPART_REQUEST");

    public static final java.lang.String _INVALID_NEW_PASSWORD = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_NEW_PASSWORD");

    public static final java.lang.String _INVALID_OPERATION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_OPERATION");

    public static final java.lang.String _INVALID_OPERATION_WITH_EXPIRED_PASSWORD = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_OPERATION_WITH_EXPIRED_PASSWORD");

    public static final java.lang.String _INVALID_PACKAGE_VERSION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_PACKAGE_VERSION");

    public static final java.lang.String _INVALID_PAGING_OPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_PAGING_OPTION");

    public static final java.lang.String _INVALID_QUERY_FILTER_OPERATOR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_QUERY_FILTER_OPERATOR");

    public static final java.lang.String _INVALID_QUERY_LOCATOR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_QUERY_LOCATOR");

    public static final java.lang.String _INVALID_QUERY_SCOPE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_QUERY_SCOPE");

    public static final java.lang.String _INVALID_REPLICATION_DATE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_REPLICATION_DATE");

    public static final java.lang.String _INVALID_SEARCH = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_SEARCH");

    public static final java.lang.String _INVALID_SEARCH_SCOPE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_SEARCH_SCOPE");

    public static final java.lang.String _INVALID_SESSION_ID = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_SESSION_ID");

    public static final java.lang.String _INVALID_SOAP_HEADER = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_SOAP_HEADER");

    public static final java.lang.String _INVALID_SORT_OPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_SORT_OPTION");

    public static final java.lang.String _INVALID_SSO_GATEWAY_URL = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_SSO_GATEWAY_URL");

    public static final java.lang.String _INVALID_TYPE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_TYPE");

    public static final java.lang.String _INVALID_TYPE_FOR_OPERATION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("INVALID_TYPE_FOR_OPERATION");

    public static final java.lang.String _JIGSAW_ACTION_DISABLED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("JIGSAW_ACTION_DISABLED");

    public static final java.lang.String _JIGSAW_IMPORT_LIMIT_EXCEEDED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("JIGSAW_IMPORT_LIMIT_EXCEEDED");

    public static final java.lang.String _JIGSAW_REQUEST_NOT_SUPPORTED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("JIGSAW_REQUEST_NOT_SUPPORTED");

    public static final java.lang.String _JSON_PARSER_ERROR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("JSON_PARSER_ERROR");

    public static final java.lang.String _KEY_HAS_BEEN_DESTROYED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("KEY_HAS_BEEN_DESTROYED");

    public static final java.lang.String _LICENSING_UNKNOWN_ERROR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("LICENSING_UNKNOWN_ERROR");

    public static final java.lang.String _LIMIT_EXCEEDED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("LIMIT_EXCEEDED");

    public static final java.lang.String _LOGIN_CHALLENGE_ISSUED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("LOGIN_CHALLENGE_ISSUED");

    public static final java.lang.String _LOGIN_CHALLENGE_PENDING = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("LOGIN_CHALLENGE_PENDING");

    public static final java.lang.String _LOGIN_DURING_RESTRICTED_DOMAIN = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("LOGIN_DURING_RESTRICTED_DOMAIN");

    public static final java.lang.String _LOGIN_DURING_RESTRICTED_TIME = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("LOGIN_DURING_RESTRICTED_TIME");

    public static final java.lang.String _LOGIN_MUST_USE_SECURITY_TOKEN = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("LOGIN_MUST_USE_SECURITY_TOKEN");

    public static final java.lang.String _MALFORMED_ID = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("MALFORMED_ID");

    public static final java.lang.String _MALFORMED_QUERY = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("MALFORMED_QUERY");

    public static final java.lang.String _MALFORMED_SEARCH = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("MALFORMED_SEARCH");

    public static final java.lang.String _MISSING_ARGUMENT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("MISSING_ARGUMENT");

    public static final java.lang.String _MISSING_RECORD = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("MISSING_RECORD");

    public static final java.lang.String _MUTUAL_AUTHENTICATION_FAILED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("MUTUAL_AUTHENTICATION_FAILED");

    public static final java.lang.String _NOT_ACCEPTABLE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("NOT_ACCEPTABLE");

    public static final java.lang.String _NOT_MODIFIED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("NOT_MODIFIED");

    public static final java.lang.String _NO_SOFTPHONE_LAYOUT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("NO_SOFTPHONE_LAYOUT");

    public static final java.lang.String _NUMBER_OUTSIDE_VALID_RANGE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("NUMBER_OUTSIDE_VALID_RANGE");

    public static final java.lang.String _OPERATION_TOO_LARGE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("OPERATION_TOO_LARGE");

    public static final java.lang.String _ORG_IN_MAINTENANCE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("ORG_IN_MAINTENANCE");

    public static final java.lang.String _ORG_IS_DOT_ORG = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("ORG_IS_DOT_ORG");

    public static final java.lang.String _ORG_IS_SIGNING_UP = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("ORG_IS_SIGNING_UP");

    public static final java.lang.String _ORG_LOCKED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("ORG_LOCKED");

    public static final java.lang.String _ORG_NOT_OWNED_BY_INSTANCE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("ORG_NOT_OWNED_BY_INSTANCE");

    public static final java.lang.String _PASSWORD_LOCKOUT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("PASSWORD_LOCKOUT");

    public static final java.lang.String _PORTAL_NO_ACCESS = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("PORTAL_NO_ACCESS");

    public static final java.lang.String _POST_BODY_PARSE_ERROR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("POST_BODY_PARSE_ERROR");

    public static final java.lang.String _QUERY_TIMEOUT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("QUERY_TIMEOUT");

    public static final java.lang.String _QUERY_TOO_COMPLICATED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("QUERY_TOO_COMPLICATED");

    public static final java.lang.String _REQUEST_LIMIT_EXCEEDED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("REQUEST_LIMIT_EXCEEDED");

    public static final java.lang.String _REQUEST_RUNNING_TOO_LONG = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("REQUEST_RUNNING_TOO_LONG");

    public static final java.lang.String _SERVER_UNAVAILABLE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("SERVER_UNAVAILABLE");

    public static final java.lang.String _SERVICE_DESK_NOT_ENABLED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("SERVICE_DESK_NOT_ENABLED");

    public static final java.lang.String _SOCIALCRM_FEEDSERVICE_API_CLIENT_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("SOCIALCRM_FEEDSERVICE_API_CLIENT_EXCEPTION");

    public static final java.lang.String _SOCIALCRM_FEEDSERVICE_API_SERVER_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("SOCIALCRM_FEEDSERVICE_API_SERVER_EXCEPTION");

    public static final java.lang.String _SOCIALCRM_FEEDSERVICE_API_UNAVAILABLE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("SOCIALCRM_FEEDSERVICE_API_UNAVAILABLE");

    public static final java.lang.String _SSO_SERVICE_DOWN = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("SSO_SERVICE_DOWN");

    public static final java.lang.String _SST_ADMIN_FILE_DOWNLOAD_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("SST_ADMIN_FILE_DOWNLOAD_EXCEPTION");

    public static final java.lang.String _TOO_MANY_APEX_REQUESTS = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("TOO_MANY_APEX_REQUESTS");

    public static final java.lang.String _TOO_MANY_RECIPIENTS = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("TOO_MANY_RECIPIENTS");

    public static final java.lang.String _TOO_MANY_RECORDS = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("TOO_MANY_RECORDS");

    public static final java.lang.String _TRIAL_EXPIRED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("TRIAL_EXPIRED");

    public static final java.lang.String _TXN_SECURITY_END_A_SESSION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("TXN_SECURITY_END_A_SESSION");

    public static final java.lang.String _TXN_SECURITY_NO_ACCESS = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("TXN_SECURITY_NO_ACCESS");

    public static final java.lang.String _TXN_SECURITY_TWO_FA_REQUIRED = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("TXN_SECURITY_TWO_FA_REQUIRED");

    public static final java.lang.String _UNABLE_TO_LOCK_ROW = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("UNABLE_TO_LOCK_ROW");

    public static final java.lang.String _UNKNOWN_ATTACHMENT_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("UNKNOWN_ATTACHMENT_EXCEPTION");

    public static final java.lang.String _UNKNOWN_EXCEPTION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("UNKNOWN_EXCEPTION");

    public static final java.lang.String _UNSUPPORTED_API_VERSION = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("UNSUPPORTED_API_VERSION");

    public static final java.lang.String _UNSUPPORTED_ATTACHMENT_ENCODING = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("UNSUPPORTED_ATTACHMENT_ENCODING");

    public static final java.lang.String _UNSUPPORTED_CLIENT = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("UNSUPPORTED_CLIENT");

    public static final java.lang.String _UNSUPPORTED_MEDIA_TYPE = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("UNSUPPORTED_MEDIA_TYPE");

    public static final java.lang.String _XML_PARSER_ERROR = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("XML_PARSER_ERROR");

    public static final ExceptionCode APEX_TRIGGER_COUPLING_LIMIT = new ExceptionCode(_APEX_TRIGGER_COUPLING_LIMIT, true);

    public static final ExceptionCode API_CURRENTLY_DISABLED = new ExceptionCode(_API_CURRENTLY_DISABLED, true);

    public static final ExceptionCode API_DISABLED_FOR_ORG = new ExceptionCode(_API_DISABLED_FOR_ORG, true);

    public static final ExceptionCode ARGUMENT_OBJECT_PARSE_ERROR = new ExceptionCode(_ARGUMENT_OBJECT_PARSE_ERROR, true);

    public static final ExceptionCode ASYNC_OPERATION_LOCATOR = new ExceptionCode(_ASYNC_OPERATION_LOCATOR, true);

    public static final ExceptionCode BATCH_PROCESSING_HALTED = new ExceptionCode(_BATCH_PROCESSING_HALTED, true);

    public static final ExceptionCode BIG_OBJECT_UNSUPPORTED_OPERATION = new ExceptionCode(_BIG_OBJECT_UNSUPPORTED_OPERATION,
            true);

    public static final ExceptionCode CANNOT_DELETE_ENTITY = new ExceptionCode(_CANNOT_DELETE_ENTITY, true);

    public static final ExceptionCode CANNOT_DELETE_OWNER = new ExceptionCode(_CANNOT_DELETE_OWNER, true);

    public static final ExceptionCode CANT_ADD_STANDADRD_PORTAL_USER_TO_TERRITORY = new ExceptionCode(
            _CANT_ADD_STANDADRD_PORTAL_USER_TO_TERRITORY, true);

    public static final ExceptionCode CANT_ADD_STANDARD_PORTAL_USER_TO_TERRITORY = new ExceptionCode(
            _CANT_ADD_STANDARD_PORTAL_USER_TO_TERRITORY, true);

    public static final ExceptionCode CIRCULAR_OBJECT_GRAPH = new ExceptionCode(_CIRCULAR_OBJECT_GRAPH, true);

    public static final ExceptionCode CLIENT_NOT_ACCESSIBLE_FOR_USER = new ExceptionCode(_CLIENT_NOT_ACCESSIBLE_FOR_USER, true);

    public static final ExceptionCode CLIENT_REQUIRE_UPDATE_FOR_USER = new ExceptionCode(_CLIENT_REQUIRE_UPDATE_FOR_USER, true);

    public static final ExceptionCode CONTENT_HUB_AUTHENTICATION_EXCEPTION = new ExceptionCode(
            _CONTENT_HUB_AUTHENTICATION_EXCEPTION, true);

    public static final ExceptionCode CONTENT_HUB_FILE_DOWNLOAD_EXCEPTION = new ExceptionCode(
            _CONTENT_HUB_FILE_DOWNLOAD_EXCEPTION, true);

    public static final ExceptionCode CONTENT_HUB_FILE_NOT_FOUND_EXCEPTION = new ExceptionCode(
            _CONTENT_HUB_FILE_NOT_FOUND_EXCEPTION, true);

    public static final ExceptionCode CONTENT_HUB_INVALID_OBJECT_TYPE_EXCEPTION = new ExceptionCode(
            _CONTENT_HUB_INVALID_OBJECT_TYPE_EXCEPTION, true);

    public static final ExceptionCode CONTENT_HUB_INVALID_PAGE_NUMBER_EXCEPTION = new ExceptionCode(
            _CONTENT_HUB_INVALID_PAGE_NUMBER_EXCEPTION, true);

    public static final ExceptionCode CONTENT_HUB_OPERATION_NOT_SUPPORTED_EXCEPTION = new ExceptionCode(
            _CONTENT_HUB_OPERATION_NOT_SUPPORTED_EXCEPTION, true);

    public static final ExceptionCode CONTENT_HUB_SECURITY_EXCEPTION = new ExceptionCode(_CONTENT_HUB_SECURITY_EXCEPTION, true);

    public static final ExceptionCode CONTENT_HUB_TIMEOUT_EXCEPTION = new ExceptionCode(_CONTENT_HUB_TIMEOUT_EXCEPTION, true);

    public static final ExceptionCode CONTENT_HUB_UNEXPECTED_EXCEPTION = new ExceptionCode(_CONTENT_HUB_UNEXPECTED_EXCEPTION,
            true);

    public static final ExceptionCode CUSTOM_METADATA_LIMIT_EXCEEDED = new ExceptionCode(_CUSTOM_METADATA_LIMIT_EXCEEDED, true);

    public static final ExceptionCode CUSTOM_SETTINGS_LIMIT_EXCEEDED = new ExceptionCode(_CUSTOM_SETTINGS_LIMIT_EXCEEDED, true);

    public static final ExceptionCode DATACLOUD_API_CLIENT_EXCEPTION = new ExceptionCode(_DATACLOUD_API_CLIENT_EXCEPTION, true);

    public static final ExceptionCode DATACLOUD_API_DISABLED_EXCEPTION = new ExceptionCode(_DATACLOUD_API_DISABLED_EXCEPTION,
            true);

    public static final ExceptionCode DATACLOUD_API_SERVER_BUSY_EXCEPTION = new ExceptionCode(
            _DATACLOUD_API_SERVER_BUSY_EXCEPTION, true);

    public static final ExceptionCode DATACLOUD_API_SERVER_EXCEPTION = new ExceptionCode(_DATACLOUD_API_SERVER_EXCEPTION, true);

    public static final ExceptionCode DATACLOUD_API_TIMEOUT_EXCEPTION = new ExceptionCode(_DATACLOUD_API_TIMEOUT_EXCEPTION, true);

    public static final ExceptionCode DATACLOUD_API_UNAVAILABLE = new ExceptionCode(_DATACLOUD_API_UNAVAILABLE, true);

    public static final ExceptionCode DUPLICATE_ARGUMENT_VALUE = new ExceptionCode(_DUPLICATE_ARGUMENT_VALUE, true);

    public static final ExceptionCode DUPLICATE_VALUE = new ExceptionCode(_DUPLICATE_VALUE, true);

    public static final ExceptionCode EMAIL_BATCH_SIZE_LIMIT_EXCEEDED = new ExceptionCode(_EMAIL_BATCH_SIZE_LIMIT_EXCEEDED, true);

    public static final ExceptionCode EMAIL_TO_CASE_INVALID_ROUTING = new ExceptionCode(_EMAIL_TO_CASE_INVALID_ROUTING, true);

    public static final ExceptionCode EMAIL_TO_CASE_LIMIT_EXCEEDED = new ExceptionCode(_EMAIL_TO_CASE_LIMIT_EXCEEDED, true);

    public static final ExceptionCode EMAIL_TO_CASE_NOT_ENABLED = new ExceptionCode(_EMAIL_TO_CASE_NOT_ENABLED, true);

    public static final ExceptionCode ENVIRONMENT_HUB_MEMBERSHIP_CONFLICT = new ExceptionCode(
            _ENVIRONMENT_HUB_MEMBERSHIP_CONFLICT, true);

    public static final ExceptionCode EXCEEDED_ID_LIMIT = new ExceptionCode(_EXCEEDED_ID_LIMIT, true);

    public static final ExceptionCode EXCEEDED_LEAD_CONVERT_LIMIT = new ExceptionCode(_EXCEEDED_LEAD_CONVERT_LIMIT, true);

    public static final ExceptionCode EXCEEDED_MAX_SIZE_REQUEST = new ExceptionCode(_EXCEEDED_MAX_SIZE_REQUEST, true);

    public static final ExceptionCode EXCEEDED_MAX_TYPES_LIMIT = new ExceptionCode(_EXCEEDED_MAX_TYPES_LIMIT, true);

    public static final ExceptionCode EXCEEDED_QUOTA = new ExceptionCode(_EXCEEDED_QUOTA, true);

    public static final ExceptionCode EXTERNAL_OBJECT_AUTHENTICATION_EXCEPTION = new ExceptionCode(
            _EXTERNAL_OBJECT_AUTHENTICATION_EXCEPTION, true);

    public static final ExceptionCode EXTERNAL_OBJECT_CONNECTION_EXCEPTION = new ExceptionCode(
            _EXTERNAL_OBJECT_CONNECTION_EXCEPTION, true);

    public static final ExceptionCode EXTERNAL_OBJECT_EXCEPTION = new ExceptionCode(_EXTERNAL_OBJECT_EXCEPTION, true);

    public static final ExceptionCode EXTERNAL_OBJECT_UNSUPPORTED_EXCEPTION = new ExceptionCode(
            _EXTERNAL_OBJECT_UNSUPPORTED_EXCEPTION, true);

    public static final ExceptionCode FEDERATED_SEARCH_ERROR = new ExceptionCode(_FEDERATED_SEARCH_ERROR, true);

    public static final ExceptionCode FEED_NOT_ENABLED_FOR_OBJECT = new ExceptionCode(_FEED_NOT_ENABLED_FOR_OBJECT, true);

    public static final ExceptionCode FUNCTIONALITY_NOT_ENABLED = new ExceptionCode(_FUNCTIONALITY_NOT_ENABLED, true);

    public static final ExceptionCode FUNCTIONALITY_TEMPORARILY_UNAVAILABLE = new ExceptionCode(
            _FUNCTIONALITY_TEMPORARILY_UNAVAILABLE, true);

    public static final ExceptionCode ILLEGAL_QUERY_PARAMETER_VALUE = new ExceptionCode(_ILLEGAL_QUERY_PARAMETER_VALUE, true);

    public static final ExceptionCode INACTIVE_OWNER_OR_USER = new ExceptionCode(_INACTIVE_OWNER_OR_USER, true);

    public static final ExceptionCode INACTIVE_PORTAL = new ExceptionCode(_INACTIVE_PORTAL, true);

    public static final ExceptionCode INSUFFICIENT_ACCESS = new ExceptionCode(_INSUFFICIENT_ACCESS, true);

    public static final ExceptionCode INTERNAL_CANVAS_ERROR = new ExceptionCode(_INTERNAL_CANVAS_ERROR, true);

    public static final ExceptionCode INVALID_ASSIGNMENT_RULE = new ExceptionCode(_INVALID_ASSIGNMENT_RULE, true);

    public static final ExceptionCode INVALID_BATCH_REQUEST = new ExceptionCode(_INVALID_BATCH_REQUEST, true);

    public static final ExceptionCode INVALID_BATCH_SIZE = new ExceptionCode(_INVALID_BATCH_SIZE, true);

    public static final ExceptionCode INVALID_CLIENT = new ExceptionCode(_INVALID_CLIENT, true);

    public static final ExceptionCode INVALID_CROSS_REFERENCE_KEY = new ExceptionCode(_INVALID_CROSS_REFERENCE_KEY, true);

    public static final ExceptionCode INVALID_DATE_FORMAT = new ExceptionCode(_INVALID_DATE_FORMAT, true);

    public static final ExceptionCode INVALID_FIELD = new ExceptionCode(_INVALID_FIELD, true);

    public static final ExceptionCode INVALID_FILTER_LANGUAGE = new ExceptionCode(_INVALID_FILTER_LANGUAGE, true);

    public static final ExceptionCode INVALID_FILTER_VALUE = new ExceptionCode(_INVALID_FILTER_VALUE, true);

    public static final ExceptionCode INVALID_ID_FIELD = new ExceptionCode(_INVALID_ID_FIELD, true);

    public static final ExceptionCode INVALID_INPUT_COMBINATION = new ExceptionCode(_INVALID_INPUT_COMBINATION, true);

    public static final ExceptionCode INVALID_LOCALE_LANGUAGE = new ExceptionCode(_INVALID_LOCALE_LANGUAGE, true);

    public static final ExceptionCode INVALID_LOCATOR = new ExceptionCode(_INVALID_LOCATOR, true);

    public static final ExceptionCode INVALID_LOGIN = new ExceptionCode(_INVALID_LOGIN, true);

    public static final ExceptionCode INVALID_MULTIPART_REQUEST = new ExceptionCode(_INVALID_MULTIPART_REQUEST, true);

    public static final ExceptionCode INVALID_NEW_PASSWORD = new ExceptionCode(_INVALID_NEW_PASSWORD, true);

    public static final ExceptionCode INVALID_OPERATION = new ExceptionCode(_INVALID_OPERATION, true);

    public static final ExceptionCode INVALID_OPERATION_WITH_EXPIRED_PASSWORD = new ExceptionCode(
            _INVALID_OPERATION_WITH_EXPIRED_PASSWORD, true);

    public static final ExceptionCode INVALID_PACKAGE_VERSION = new ExceptionCode(_INVALID_PACKAGE_VERSION, true);

    public static final ExceptionCode INVALID_PAGING_OPTION = new ExceptionCode(_INVALID_PAGING_OPTION, true);

    public static final ExceptionCode INVALID_QUERY_FILTER_OPERATOR = new ExceptionCode(_INVALID_QUERY_FILTER_OPERATOR, true);

    public static final ExceptionCode INVALID_QUERY_LOCATOR = new ExceptionCode(_INVALID_QUERY_LOCATOR, true);

    public static final ExceptionCode INVALID_QUERY_SCOPE = new ExceptionCode(_INVALID_QUERY_SCOPE, true);

    public static final ExceptionCode INVALID_REPLICATION_DATE = new ExceptionCode(_INVALID_REPLICATION_DATE, true);

    public static final ExceptionCode INVALID_SEARCH = new ExceptionCode(_INVALID_SEARCH, true);

    public static final ExceptionCode INVALID_SEARCH_SCOPE = new ExceptionCode(_INVALID_SEARCH_SCOPE, true);

    public static final ExceptionCode INVALID_SESSION_ID = new ExceptionCode(_INVALID_SESSION_ID, true);

    public static final ExceptionCode INVALID_SOAP_HEADER = new ExceptionCode(_INVALID_SOAP_HEADER, true);

    public static final ExceptionCode INVALID_SORT_OPTION = new ExceptionCode(_INVALID_SORT_OPTION, true);

    public static final ExceptionCode INVALID_SSO_GATEWAY_URL = new ExceptionCode(_INVALID_SSO_GATEWAY_URL, true);

    public static final ExceptionCode INVALID_TYPE = new ExceptionCode(_INVALID_TYPE, true);

    public static final ExceptionCode INVALID_TYPE_FOR_OPERATION = new ExceptionCode(_INVALID_TYPE_FOR_OPERATION, true);

    public static final ExceptionCode JIGSAW_ACTION_DISABLED = new ExceptionCode(_JIGSAW_ACTION_DISABLED, true);

    public static final ExceptionCode JIGSAW_IMPORT_LIMIT_EXCEEDED = new ExceptionCode(_JIGSAW_IMPORT_LIMIT_EXCEEDED, true);

    public static final ExceptionCode JIGSAW_REQUEST_NOT_SUPPORTED = new ExceptionCode(_JIGSAW_REQUEST_NOT_SUPPORTED, true);

    public static final ExceptionCode JSON_PARSER_ERROR = new ExceptionCode(_JSON_PARSER_ERROR, true);

    public static final ExceptionCode KEY_HAS_BEEN_DESTROYED = new ExceptionCode(_KEY_HAS_BEEN_DESTROYED, true);

    public static final ExceptionCode LICENSING_UNKNOWN_ERROR = new ExceptionCode(_LICENSING_UNKNOWN_ERROR, true);

    public static final ExceptionCode LIMIT_EXCEEDED = new ExceptionCode(_LIMIT_EXCEEDED, true);

    public static final ExceptionCode LOGIN_CHALLENGE_ISSUED = new ExceptionCode(_LOGIN_CHALLENGE_ISSUED, true);

    public static final ExceptionCode LOGIN_CHALLENGE_PENDING = new ExceptionCode(_LOGIN_CHALLENGE_PENDING, true);

    public static final ExceptionCode LOGIN_DURING_RESTRICTED_DOMAIN = new ExceptionCode(_LOGIN_DURING_RESTRICTED_DOMAIN, true);

    public static final ExceptionCode LOGIN_DURING_RESTRICTED_TIME = new ExceptionCode(_LOGIN_DURING_RESTRICTED_TIME, true);

    public static final ExceptionCode LOGIN_MUST_USE_SECURITY_TOKEN = new ExceptionCode(_LOGIN_MUST_USE_SECURITY_TOKEN, true);

    public static final ExceptionCode MALFORMED_ID = new ExceptionCode(_MALFORMED_ID, true);

    public static final ExceptionCode MALFORMED_QUERY = new ExceptionCode(_MALFORMED_QUERY, true);

    public static final ExceptionCode MALFORMED_SEARCH = new ExceptionCode(_MALFORMED_SEARCH, true);

    public static final ExceptionCode MISSING_ARGUMENT = new ExceptionCode(_MISSING_ARGUMENT, true);

    public static final ExceptionCode MISSING_RECORD = new ExceptionCode(_MISSING_RECORD, true);

    public static final ExceptionCode MUTUAL_AUTHENTICATION_FAILED = new ExceptionCode(_MUTUAL_AUTHENTICATION_FAILED, true);

    public static final ExceptionCode NOT_ACCEPTABLE = new ExceptionCode(_NOT_ACCEPTABLE, true);

    public static final ExceptionCode NOT_MODIFIED = new ExceptionCode(_NOT_MODIFIED, true);

    public static final ExceptionCode NO_SOFTPHONE_LAYOUT = new ExceptionCode(_NO_SOFTPHONE_LAYOUT, true);

    public static final ExceptionCode NUMBER_OUTSIDE_VALID_RANGE = new ExceptionCode(_NUMBER_OUTSIDE_VALID_RANGE, true);

    public static final ExceptionCode OPERATION_TOO_LARGE = new ExceptionCode(_OPERATION_TOO_LARGE, true);

    public static final ExceptionCode ORG_IN_MAINTENANCE = new ExceptionCode(_ORG_IN_MAINTENANCE, true);

    public static final ExceptionCode ORG_IS_DOT_ORG = new ExceptionCode(_ORG_IS_DOT_ORG, true);

    public static final ExceptionCode ORG_IS_SIGNING_UP = new ExceptionCode(_ORG_IS_SIGNING_UP, true);

    public static final ExceptionCode ORG_LOCKED = new ExceptionCode(_ORG_LOCKED, true);

    public static final ExceptionCode ORG_NOT_OWNED_BY_INSTANCE = new ExceptionCode(_ORG_NOT_OWNED_BY_INSTANCE, true);

    public static final ExceptionCode PASSWORD_LOCKOUT = new ExceptionCode(_PASSWORD_LOCKOUT, true);

    public static final ExceptionCode PORTAL_NO_ACCESS = new ExceptionCode(_PORTAL_NO_ACCESS, true);

    public static final ExceptionCode POST_BODY_PARSE_ERROR = new ExceptionCode(_POST_BODY_PARSE_ERROR, true);

    public static final ExceptionCode QUERY_TIMEOUT = new ExceptionCode(_QUERY_TIMEOUT, true);

    public static final ExceptionCode QUERY_TOO_COMPLICATED = new ExceptionCode(_QUERY_TOO_COMPLICATED, true);

    public static final ExceptionCode REQUEST_LIMIT_EXCEEDED = new ExceptionCode(_REQUEST_LIMIT_EXCEEDED, true);

    public static final ExceptionCode REQUEST_RUNNING_TOO_LONG = new ExceptionCode(_REQUEST_RUNNING_TOO_LONG, true);

    public static final ExceptionCode SERVER_UNAVAILABLE = new ExceptionCode(_SERVER_UNAVAILABLE, true);

    public static final ExceptionCode SERVICE_DESK_NOT_ENABLED = new ExceptionCode(_SERVICE_DESK_NOT_ENABLED, true);

    public static final ExceptionCode SOCIALCRM_FEEDSERVICE_API_CLIENT_EXCEPTION = new ExceptionCode(
            _SOCIALCRM_FEEDSERVICE_API_CLIENT_EXCEPTION, true);

    public static final ExceptionCode SOCIALCRM_FEEDSERVICE_API_SERVER_EXCEPTION = new ExceptionCode(
            _SOCIALCRM_FEEDSERVICE_API_SERVER_EXCEPTION, true);

    public static final ExceptionCode SOCIALCRM_FEEDSERVICE_API_UNAVAILABLE = new ExceptionCode(
            _SOCIALCRM_FEEDSERVICE_API_UNAVAILABLE, true);

    public static final ExceptionCode SSO_SERVICE_DOWN = new ExceptionCode(_SSO_SERVICE_DOWN, true);

    public static final ExceptionCode SST_ADMIN_FILE_DOWNLOAD_EXCEPTION = new ExceptionCode(_SST_ADMIN_FILE_DOWNLOAD_EXCEPTION,
            true);

    public static final ExceptionCode TOO_MANY_APEX_REQUESTS = new ExceptionCode(_TOO_MANY_APEX_REQUESTS, true);

    public static final ExceptionCode TOO_MANY_RECIPIENTS = new ExceptionCode(_TOO_MANY_RECIPIENTS, true);

    public static final ExceptionCode TOO_MANY_RECORDS = new ExceptionCode(_TOO_MANY_RECORDS, true);

    public static final ExceptionCode TRIAL_EXPIRED = new ExceptionCode(_TRIAL_EXPIRED, true);

    public static final ExceptionCode TXN_SECURITY_END_A_SESSION = new ExceptionCode(_TXN_SECURITY_END_A_SESSION, true);

    public static final ExceptionCode TXN_SECURITY_NO_ACCESS = new ExceptionCode(_TXN_SECURITY_NO_ACCESS, true);

    public static final ExceptionCode TXN_SECURITY_TWO_FA_REQUIRED = new ExceptionCode(_TXN_SECURITY_TWO_FA_REQUIRED, true);

    public static final ExceptionCode UNABLE_TO_LOCK_ROW = new ExceptionCode(_UNABLE_TO_LOCK_ROW, true);

    public static final ExceptionCode UNKNOWN_ATTACHMENT_EXCEPTION = new ExceptionCode(_UNKNOWN_ATTACHMENT_EXCEPTION, true);

    public static final ExceptionCode UNKNOWN_EXCEPTION = new ExceptionCode(_UNKNOWN_EXCEPTION, true);

    public static final ExceptionCode UNSUPPORTED_API_VERSION = new ExceptionCode(_UNSUPPORTED_API_VERSION, true);

    public static final ExceptionCode UNSUPPORTED_ATTACHMENT_ENCODING = new ExceptionCode(_UNSUPPORTED_ATTACHMENT_ENCODING, true);

    public static final ExceptionCode UNSUPPORTED_CLIENT = new ExceptionCode(_UNSUPPORTED_CLIENT, true);

    public static final ExceptionCode UNSUPPORTED_MEDIA_TYPE = new ExceptionCode(_UNSUPPORTED_MEDIA_TYPE, true);

    public static final ExceptionCode XML_PARSER_ERROR = new ExceptionCode(_XML_PARSER_ERROR, true);

    public java.lang.String getValue() {
        return localExceptionCode;
    }

    public boolean equals(java.lang.Object obj) {
        return (obj == this);
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public java.lang.String toString() {

        return localExceptionCode.toString();

    }

    /**
     *
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {

        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME);
        return factory.createOMElement(dataSource, MY_QNAME);

    }

    public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
        serialize(parentQName, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
            boolean serializeType) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {

        // We can safely assume an element has only one type associated with it

        java.lang.String namespace = parentQName.getNamespaceURI();
        java.lang.String _localName = parentQName.getLocalPart();

        writeStartElement(null, namespace, _localName, xmlWriter);

        // add the type details if this is used in a simple type
        if (serializeType) {
            java.lang.String namespacePrefix = registerPrefix(xmlWriter, "urn:fault.partner.soap.sforce.com");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":ExceptionCode",
                        xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "ExceptionCode", xmlWriter);
            }
        }

        if (localExceptionCode == null) {

            throw new org.apache.axis2.databinding.ADBException("ExceptionCode cannot be null !!");

        } else {

            xmlWriter.writeCharacters(localExceptionCode);

        }

        xmlWriter.writeEndElement();

    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("urn:fault.partner.soap.sforce.com")) {
            return "ns3";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
            javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
        if (writerPrefix != null) {
            xmlWriter.writeStartElement(namespace, localPart);
        } else {
            if (namespace.length() == 0) {
                prefix = "";
            } else if (prefix == null) {
                prefix = generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(java.lang.String prefix, java.lang.String namespace, java.lang.String attName,
            java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        if (xmlWriter.getPrefix(namespace) == null) {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        xmlWriter.writeAttribute(namespace, attName, attValue);
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeAttribute(java.lang.String namespace, java.lang.String attName, java.lang.String attValue,
            javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName, javax.xml.namespace.QName qname,
            javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }
        java.lang.String attributeValue;
        if (attributePrefix.trim().length() > 0) {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        } else {
            attributeValue = qname.getLocalPart();
        }

        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attributeValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attributeValue);
        }
    }

    /**
     * method to handle Qnames
     */

    private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException {
        java.lang.String namespaceURI = qname.getNamespaceURI();
        if (namespaceURI != null) {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
            if (prefix == null) {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0) {
                xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            } else {
                // i.e this is the default namespace
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException {

        if (qnames != null) {
            // we have to store this data until last moment since it is not possible to write any
            // namespace data after writing the charactor data
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;

            for (int i = 0; i < qnames.length; i++) {
                if (i > 0) {
                    stringToWrite.append(" ");
                }
                namespaceURI = qnames[i].getNamespaceURI();
                if (namespaceURI != null) {
                    prefix = xmlWriter.getPrefix(namespaceURI);
                    if ((prefix == null) || (prefix.length() == 0)) {
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0) {
                        stringToWrite.append(prefix).append(":")
                                .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                } else {
                    stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                }
            }
            xmlWriter.writeCharacters(stringToWrite.toString());
        }

    }

    /**
     * Register a namespace prefix
     */
    private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace)
            throws javax.xml.stream.XMLStreamException {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);
        if (prefix == null) {
            prefix = generatePrefix(namespace);
            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
            while (true) {
                java.lang.String uri = nsContext.getNamespaceURI(prefix);
                if (uri == null || uri.length() == 0) {
                    break;
                }
                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     *
     */
    public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
            throws org.apache.axis2.databinding.ADBException {

        // We can safely assume an element has only one type associated with it
        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(MY_QNAME, new java.lang.Object[] {
                org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
                org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExceptionCode) }, null);

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory {

        public static ExceptionCode fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
            ExceptionCode enumeration = (ExceptionCode)

            _table_.get(value);

            if ((enumeration == null) && !((value == null) || (value.equals("")))) {
                throw new java.lang.IllegalArgumentException();
            }
            return enumeration;
        }

        public static ExceptionCode fromString(java.lang.String value, java.lang.String namespaceURI)
                throws java.lang.IllegalArgumentException {
            try {

                return fromValue(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(value));

            } catch (java.lang.Exception e) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        public static ExceptionCode fromString(javax.xml.stream.XMLStreamReader xmlStreamReader, java.lang.String content) {
            if (content.indexOf(":") > -1) {
                java.lang.String prefix = content.substring(0, content.indexOf(":"));
                java.lang.String namespaceUri = xmlStreamReader.getNamespaceContext().getNamespaceURI(prefix);
                return ExceptionCode.Factory.fromString(content, namespaceUri);
            } else {
                return ExceptionCode.Factory.fromString(content, "");
            }
        }

        /**
         * static method to create the object Precondition: If this object is an element, the current or next start element starts this
         * object and any intervening reader events are ignorable If this object is not an element, it is a complex type and the reader is
         * at the event just after the outer start element Postcondition: If this object is an element, the reader is positioned at its end
         * element If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static ExceptionCode parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            ExceptionCode object = null;
            // initialize a hash map to keep values
            java.util.Map attributeMap = new java.util.HashMap();
            java.util.List extraAttributeList = new java.util.ArrayList<org.apache.axiom.om.OMAttribute>();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                while (!reader.isEndElement()) {
                    if (reader.isStartElement() || reader.hasText()) {

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                            throw new org.apache.axis2.databinding.ADBException("The element: " + "ExceptionCode"
                                    + "  cannot be null");
                        }

                        java.lang.String content = reader.getElementText();

                        if (content.indexOf(":") > 0) {
                            // this seems to be a Qname so find the namespace and send
                            prefix = content.substring(0, content.indexOf(":"));
                            namespaceuri = reader.getNamespaceURI(prefix);
                            object = ExceptionCode.Factory.fromString(content, namespaceuri);
                        } else {
                            // this seems to be not a qname send and empty namespace incase of it is
                            // check is done in fromString method
                            object = ExceptionCode.Factory.fromString(content, "");
                        }

                    } else {
                        reader.next();
                    }
                }  // end of while loop

            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }// end of factory class

}
