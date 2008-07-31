// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.scd.ui;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.designer.scd.ScdManager;
import org.talend.designer.scd.model.Versioning;
import org.talend.designer.scd.util.SWTResourceManager;

/**
 * DOC hcw class global comment. Detailled comment
 */
public class PerlScdDialog extends AbstractScdDialog {

    /**
     * DOC hcw PerlScdDialog constructor comment.
     * 
     * @param shell
     * @param scdManager
     */
    public PerlScdDialog(Shell shell, ScdManager scdManager) {
        super(shell);
        this.scdManager = scdManager;
    }

    /**
     * DOC hcw Comment method "createContents".
     * 
     * @param composite
     */
    @Override
    Control createScdContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(composite);
        GridDataFactory.fillDefaults().applyTo(composite);

        Composite filterUnusedComposite = new Composite(composite, SWT.NONE);
        GridLayoutFactory.swtDefaults().margins(0, 0).spacing(0, 0).applyTo(filterUnusedComposite);
        GridDataFactory.swtDefaults().hint(SECTION_WIDTH, SECTION_HEIGHT * 2).applyTo(filterUnusedComposite);

        ViewerFilter filter = createFilter(filterUnusedComposite);

        unusedFields = new FieldSection(filterUnusedComposite, SECTION_WIDTH, SECTION_HEIGHT);
        unusedFields.setTitle("Unused", SWTResourceManager.getColor(IColorConstants.RED));
        unusedFields.setTableInput(scdManager.getUnusedFields());
        unusedFields.getTableViewer().addFilter(filter);
        GridDataFactory.swtDefaults().hint(SECTION_WIDTH - 3, SECTION_HEIGHT * 2 - 35).applyTo(unusedFields.getControl());
        addContextHelp(unusedFields.getTableViewer().getTable(), "org.talend.designer.scd.unused");

        type1Fields = new FieldSection(composite, SECTION_WIDTH, SECTION_HEIGHT * 2);
        type1Fields.setTitle("Type 1 fields", SWTResourceManager.getColor(IColorConstants.YELLOW));
        // GridDataFactory.swtDefaults().hint(SECTION_WIDTH, SECTION_HEIGHT).applyTo(type1Fields.getControl());
        type1Fields.setTableInput(scdManager.getType1Table());
        addContextHelp(type1Fields.getTableViewer().getTable(), "org.talend.designer.scd.type1");

        sourceKeys = new FieldSection(composite, SECTION_WIDTH, SECTION_HEIGHT * 2 + 40);
        sourceKeys.setTitle("Source Keys", SWTResourceManager.getColor(IColorConstants.YELLOW));
        // GridDataFactory.swtDefaults().hint(SECTION_WIDTH, SECTION_HEIGHT).applyTo(sourceKeys.getControl());
        sourceKeys.setTableInput(scdManager.getSourceKeys());
        addContextHelp(sourceKeys.getTableViewer().getTable(), "org.talend.designer.scd.sourceKey");

        type2Fields = new Type2Section(composite, SECTION_WIDTH, SECTION_HEIGHT * 2 + 40);
        type2Fields.setTitle("Type 2 fields", SWTResourceManager.getColor(IColorConstants.YELLOW));
        // GridDataFactory.swtDefaults().hint(SECTION_WIDTH, SECTION_HEIGHT).applyTo(type2Fields.getControl());
        type2Fields.setTableInput(scdManager.getType2Table());
        type2Fields.setSupportCreationType(false);
        if (scdManager.getVersionData() != null) {
            type2Fields.setVersionInput(scdManager.getVersionData());
        } else {
            type2Fields.setVersionInput(new Versioning());
        }
        addContextHelp(type2Fields.getTableViewer().getTable(), "org.talend.designer.scd.type2");

        return composite;
    }

    @Override
    protected void okPressed() {
        scdManager.saveUIData(sourceKeys.getTableData(), null, null, type1Fields.getTableData(), type2Fields.getTableData(),
                type2Fields.getVersionData(), null);

        super.okPressed();
    }

    /**
     * Return the initial size of the dialog
     */
    @Override
    protected Point getInitialSize() {
        return new Point(850, 550);
    }

}
