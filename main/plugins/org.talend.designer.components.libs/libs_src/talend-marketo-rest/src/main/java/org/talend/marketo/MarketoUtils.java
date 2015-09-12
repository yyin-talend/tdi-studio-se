package org.talend.marketo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketoUtils {

    public static String formatAsW3C(java.util.Date dt) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String text = df.format(dt);
        return text;
    }

    public static String unionString(String separator, Object... objects) {
        if (objects != null) {
            String value = "";
            boolean isFirst = true;
            for (Object obj : objects) {
                if (isFirst) {
                    value = String.valueOf(obj);
                    isFirst = false;
                } else {
                    value = value + separator + String.valueOf(obj);
                }
            }
            return value;
        } else {
            return null;
        }
    }

    public static Map<String, String> attributeListToMap(
            List<Map<String, String>> attributes) {
        Map<String, String> attrMaps = new HashMap<String, String>();
        for (Map<String, String> attr : attributes) {
            attrMaps.put(attr.get("name"), attr.get("value"));
        }
        return attrMaps;
    }

    public static <T> List<List<T>> splitList(List<T> objList, int subSize) {
        int listMaxSize = objList.size() % subSize == 0 ? objList.size()
                / subSize : objList.size() / subSize + 1;
        List<List<T>> returnList = new ArrayList<List<T>>();
        for (int i = 0; i < listMaxSize; i++) {
            returnList.add(new ArrayList<T>());
        }
        for (int index = 0; index < objList.size(); index++) {
            returnList.get(index / subSize).add(objList.get(index));
        }
        return returnList;
    }

}
