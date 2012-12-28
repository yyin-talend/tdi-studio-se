package org.talend.designer.core.utils;

import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IProcess;
import org.talend.designer.core.ui.editor.process.Process;

public class ConnectionUtil {

    public static String generateUniqueConnectionName(EConnectionType connectType, IProcess process) {
        if (connectType.hasConnectionCategory(EConnectionType.FLOW)) {
            return process.generateUniqueConnectionName(Process.DEFAULT_ROW_CONNECTION_NAME);
        } else if (connectType.equals(EConnectionType.TABLE)) {
            return process.generateUniqueConnectionName(Process.DEFAULT_TABLE_CONNECTION_NAME);
        } else if (connectType.equals(EConnectionType.ITERATE)) {
            return process.generateUniqueConnectionName(Process.DEFAULT_ITERATE_CONNECTION_NAME);
        } else if (connectType.equals(EConnectionType.ROUTE) || connectType.equals(EConnectionType.ROUTE_ENDBLOCK)) {
            return process.generateUniqueConnectionName(Process.DEFAULT_ROUTE_CONNECTION_NAME);
        } else if (connectType.equals(EConnectionType.ROUTE_WHEN)) {
            return process.generateUniqueConnectionName(Process.DEFAULT_WHEN_CONNECTION_NAME);
        } else if (connectType.equals(EConnectionType.ROUTE_OTHER)) {
            return process.generateUniqueConnectionName(Process.DEFAULT_OTHER_CONNECTION_NAME);
        } else if (connectType.equals(EConnectionType.ROUTE_CATCH)) {
            return process.generateUniqueConnectionName(Process.DEFAULT_CATCH_CONNECTION_NAME);
        } else if (connectType.equals(EConnectionType.ROUTE_FINALLY)) {
            return process.generateUniqueConnectionName(Process.DEFAULT_FINALLY_CONNECTION_NAME);
        } else if (connectType.equals(EConnectionType.ROUTE_TRY)) {
            return process.generateUniqueConnectionName(Process.DEFAULT_TRY_CONNECTION_NAME);
        }
        return null;
    }

}
