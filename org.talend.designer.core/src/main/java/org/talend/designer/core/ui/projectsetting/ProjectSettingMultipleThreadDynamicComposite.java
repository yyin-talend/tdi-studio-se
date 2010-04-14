// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.projectsetting;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.designer.core.ui.views.properties.MultipleThreadDynamicComposite;

/**
 * cli class global comment. Detailled comment
 */
public class ProjectSettingMultipleThreadDynamicComposite extends MultipleThreadDynamicComposite {

    private CommandStack cs;

    private boolean isCommandExcute;

    public ProjectSettingMultipleThreadDynamicComposite(Composite parentComposite, int styles, EComponentCategory section,
            Element element, boolean isCompactView) {
        super(parentComposite, styles, section, element, isCompactView);
    }

    @Override
    public CommandStack getCommandStack() {
        if (cs == null) { // fixed bug 12476
            cs = new CommandStack();
            cs.addCommandStackEventListener(new CommandStackEventListener() {

                public void stackChanged(CommandStackEvent event) {
                    if (event.getDetail() == CommandStack.POST_EXECUTE) {
                        isCommandExcute = true;
                    }
                }
            });
        }
        return cs;
    }

    public boolean isCommandExcute() {
        return this.isCommandExcute;
    }
}
