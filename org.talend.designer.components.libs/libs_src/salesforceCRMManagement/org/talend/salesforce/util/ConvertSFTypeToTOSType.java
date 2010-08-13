package org.talend.salesforce.util;

public class ConvertSFTypeToTOSType {

    public static final String string_value = "string";

    public static final String picklist_value = "picklist";

    public static final String multipicklist_value = "multipicklist";

    public static final String combobox_value = "combobox";

    public static final String reference_value = "reference";

    public static final String base64_value = "base64";

    public static final String boolean_value = "boolean";

    public static final String currency_value = "currency";

    public static final String textarea_value = "textarea";

    public static final String int_value = "int";

    public static final String double_value = "double";

    public static final String percent_value = "percent";

    public static final String phone_value = "phone";

    public static final String id_value = "id";

    public static final String date_value = "date";

    public static final String datetime_value = "datetime";

    public static final String time_value = "time";

    public static final String url_value = "url";

    public static final String email_value = "email";

    public static final String encryptedstring_value = "encryptedstring";

    public static final String anyType_value = "anyType";

    public static final String ID_BOOLEAN = "id_Boolean";

    public static final String ID_BIGDECIMAL = "id_Bigdecimal";

    public static final String ID_BYTE = "id_Byte";

    public static final String ID_INTEGER = "id_Integer";

    public static final String ID_BYTEARRAY = "id_Bytearray";

    public static final String ID_LONG = "id_Long";

    public static final String ID_CHARACTER = "id_Character";

    public static final String ID_OBJECT = "id_Object";

    public static final String ID_DATE = "id_Date";

    public static final String ID_SHORT = "id_Short";

    public static final String ID_DOUBLE = "id_Double";

    public static final String ID_STRING = "id_String";

    public static final String ID_FLOAT = "id_Float";

    public static final String ID_LIST = "id_List";

    public static String getTOSValue(String SFType) {

        if (string_value.equals(SFType))
            return ID_STRING;
        else if (picklist_value.equals(SFType))
            return ID_STRING;
        else if (multipicklist_value.equals(SFType))
            return ID_STRING;
        else if (combobox_value.equals(SFType))
            return ID_STRING;
        else if (reference_value.equals(SFType))
            return ID_STRING;
        else if (base64_value.equals(SFType))
            return ID_STRING;
        else if (boolean_value.equals(SFType))
            return ID_BOOLEAN;
        else if (currency_value.equals(SFType))
            return ID_STRING;
        else if (textarea_value.equals(SFType))
            return ID_STRING;
        else if (int_value.equals(SFType))
            return ID_INTEGER;
        else if (double_value.equals(SFType))
            return ID_DOUBLE;
        else if (percent_value.equals(SFType))
            return ID_DOUBLE;
        else if (phone_value.equals(SFType))
            return ID_STRING;
        else if (id_value.equals(SFType))
            return ID_STRING;
        else if (date_value.equals(SFType))
            return ID_DATE;
        else if (datetime_value.equals(SFType))
            return ID_DATE;
        else if (time_value.equals(SFType))
            return ID_DATE;
        else if (url_value.equals(SFType))
            return ID_STRING;
        else if (email_value.equals(SFType))
            return ID_STRING;
        else if (encryptedstring_value.equals(SFType))
            return ID_STRING;
        else if (anyType_value.equals(SFType))
            return ID_STRING;

        return null;
    }

    public static String getPattern(String type) {
        if (type.equals("date")) {
            return "yyyy-MM-dd";
        } else if (type.equals("datetime")) {
            return "yyyy-MM-dd&apos;T&apos;HH:mm:ss&apos;.000Z&apos;";
        } else if (type.equals("time")) {
            return "HH:mm:ss&apos;.000Z&apos;";
        } else {
            return null;
        }
    }
}
