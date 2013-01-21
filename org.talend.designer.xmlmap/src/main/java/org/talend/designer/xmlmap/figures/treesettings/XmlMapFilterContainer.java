// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.figures.treesettings;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.designer.gefabstractmap.figures.manager.TableManager;
import org.talend.designer.gefabstractmap.figures.treesettings.FilterContainer;
import org.talend.designer.gefabstractmap.part.directedit.DirectEditType;
import org.talend.designer.xmlmap.commands.TreeSettingDirectEditCommand;

/**
 * created by Administrator on 2013-1-16 Detailled comment
 * 
 */
public class XmlMapFilterContainer extends FilterContainer {

    /**
     * DOC Administrator XmlMapFilterContainer constructor comment.
     * 
     * @param tableManager
     * @param parent
     */
    public XmlMapFilterContainer(TableManager tableManager, Composite parent) {
        super(tableManager, parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.newabstractmap.figures.treesettings.FilterContainer#getFilterChangeCommand()
     */
    @Override
    protected Command getFilterChangeCommand(Object model, String newValue) {
        TreeSettingDirectEditCommand command = new TreeSettingDirectEditCommand(model, DirectEditType.EXPRESSION_FILTER, newValue);
        return null;
    }

}
