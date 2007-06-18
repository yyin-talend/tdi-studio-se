package org.talend.designer.abstractmap.ui.properties;

import org.talend.designer.abstractmap.i18n.Messages;

/**
 * 
 * Style of links. <br/>
 */
public enum LINK_STYLE {
    AUTO(Messages.getString("prefs.configuration.LINK_STYLE.AUTO")),
    BEZIER_CURVE(Messages.getString("prefs.configuration.LINK_STYLE.BEZIER_CURVE")),
    LINE(Messages.getString("prefs.configuration.LINK_STYLE.LINE")),
    ;

    private String displayName;

    LINK_STYLE(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return this.toString();
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
