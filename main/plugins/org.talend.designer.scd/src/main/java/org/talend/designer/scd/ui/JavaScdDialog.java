// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.talend.designer.scd.ScdManager;
import org.talend.designer.scd.ScdParameterConstants;
import org.talend.designer.scd.i18n.Messages;
import org.talend.designer.scd.model.Versioning;
import org.talend.designer.scd.util.SWTResourceManager;

/**
 * DOC hcw class global comment. Detailled comment
 */
public class JavaScdDialog extends AbstractScdDialog {

    /**
     * DOC hcw JavaScdDialog constructor comment.
     * 
     * @param shell
     * @param scdManager
     */
    public JavaScdDialog(Shell shell, ScdManager scdManager) {
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
        GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(true).spacing(20, 10).applyTo(composite);
        GridDataFactory.fillDefaults().applyTo(composite);

        Composite filterUnusedComposite = new Composite(composite, SWT.NONE);
        GridLayoutFactory.swtDefaults().margins(0, 0).spacing(0, 0).applyTo(filterUnusedComposite);
        GridDataFactory.fillDefaults().span(1, 2).hint(SECTION_WIDTH, SECTION_HEIGHT * 2 + 20).applyTo(filterUnusedComposite);

        ViewerFilter filter = createFilter(filterUnusedComposite);

        unusedFields = new FieldSection(filterUnusedComposite, SECTION_WIDTH - 5, SECTION_HEIGHT, scdManager, false, false);
        unusedFields.setTitle(Messages.getString("JavaScdDialog.unUsed"), SWTResourceManager.getColor(198, 195, //$NON-NLS-1$
                198));
        unusedFields.setTableInput(scdManager.getUnusedFields());
        unusedFields.getTableViewer().addFilter(filter);
        unusedFields.setSortable(true);
        GridDataFactory.swtDefaults().hint(SECTION_WIDTH - 5, SECTION_HEIGHT * 2 - 20).applyTo(unusedFields.getControl());
        addContextHelp(unusedFields.getTableViewer().getTable(), "org.talend.designer.scd.unused"); //$NON-NLS-1$

        type0Fields = new FieldSection(composite, SECTION_WIDTH, SECTION_HEIGHT, scdManager, false, false);
        type0Fields.setTitle(Messages.getString("JavaScdDialog.type0Field"), SWTResourceManager.getColor(255, //$NON-NLS-1$
                146, 0));
        // GridDataFactory.swtDefaults().hint(SECTION_WIDTH,
        // SECTION_HEIGHT).applyTo(type0Fields.getControl());
        type0Fields.setTableInput(scdManager.getType0Table());
        addContextHelp(type0Fields.getTableViewer().getTable(), "org.talend.designer.scd.type0"); //$NON-NLS-1$

        type1Fields = new FieldSection(composite, SECTION_WIDTH, SECTION_HEIGHT, scdManager, false, false,
                ScdParameterConstants.DROP_COPY_TYPE1FIELDS);
        type1Fields.setTitle(Messages.getString("JavaScdDialog.type1Field"), SWTResourceManager.getColor(255, //$NON-NLS-1$
                203, 0));
        // GridDataFactory.swtDefaults().hint(SECTION_WIDTH,
        // SECTION_HEIGHT).applyTo(type1Fields.getControl());
        type1Fields.setTableInput(scdManager.getType1Table());
        addContextHelp(type1Fields.getTableViewer().getTable(), "org.talend.designer.scd.type1"); //$NON-NLS-1$

        sourceKeys = new FieldSection(composite, SECTION_WIDTH, SECTION_HEIGHT, scdManager, false, false,
                ScdParameterConstants.DROP_COPY_SOURCEKEYS);
        sourceKeys.setTitle(Messages.getString("JavaScdDialog.sourceKey"), SWTResourceManager.getColor(156, 0, //$NON-NLS-1$
                255));
        // GridDataFactory.swtDefaults().hint(SECTION_WIDTH,
        // SECTION_HEIGHT).applyTo(sourceKeys.getControl());
        sourceKeys.setTableInput(scdManager.getSourceKeys());
        addContextHelp(sourceKeys.getTableViewer().getTable(), "org.talend.designer.scd.sourceKey"); //$NON-NLS-1$

        type2Fields = new Type2Section(composite, SECTION_WIDTH, SECTION_HEIGHT * 2 + 40, scdManager,
                ScdParameterConstants.DROP_COPY_TYPE2FIELDS);
        type2Fields.setTitle(Messages.getString("JavaScdDialog.type2Field"), SWTResourceManager.getColor(255, //$NON-NLS-1$
                255, 0));
        GridDataFactory.swtDefaults().span(1, 2).hint(SECTION_WIDTH, SECTION_HEIGHT * 2 + 40).applyTo(type2Fields.getControl());
        type2Fields.setTableInput(scdManager.getType2Table());
        if (scdManager.getVersionData() != null) {
            type2Fields.setVersionInput(scdManager.getVersionData());
        } else {
            type2Fields.setVersionInput(new Versioning());
        }
        addContextHelp(type2Fields.getTableViewer().getTable(), "org.talend.designer.scd.type2"); //$NON-NLS-1$

        surrogateKeys = new SurrogateSection(composite, SECTION_WIDTH, SECTION_HEIGHT + 30, scdManager);
        surrogateKeys.setTitle(Messages.getString("JavaScdDialog.surrogateKey"), SWTResourceManager.getColor( //$NON-NLS-1$
                214, 40, 255));
        GridDataFactory.swtDefaults().hint(SECTION_WIDTH, SECTION_HEIGHT + 30).applyTo(surrogateKeys.getControl());
        surrogateKeys.setTableInput(scdManager.getSurrogateKeys());
        surrogateKeys.addContextHelp(this);

        Label placeHolder = new Label(composite, SWT.NONE);
        GridDataFactory.swtDefaults().hint(SECTION_WIDTH, SECTION_HEIGHT).applyTo(placeHolder);

        type3Fields = new Type3Section(composite, SECTION_WIDTH, SECTION_HEIGHT, scdManager);
        type3Fields.setTitle(Messages.getString("JavaScdDialog.type3Key"), SWTResourceManager.getColor(24, //$NON-NLS-1$
                182, 255));
        GridDataFactory.swtDefaults().hint(SECTION_WIDTH, SECTION_HEIGHT + 20).applyTo(type3Fields.getControl());
        type3Fields.setTableInput(scdManager.getType3Table());
        addContextHelp(type3Fields.getTable(), "org.talend.designer.scd.type3"); //$NON-NLS-1$

        scdManager.setUnusedFieldsSource(unusedFields);
        ScdSection[] sections = { surrogateKeys, sourceKeys, type0Fields, type1Fields, type2Fields, type3Fields };
        for (ScdSection scd : sections) {
            scdManager.addUnusedFieldsTarget(scd);
        }
        return composite;
    }

    @Override
    protected void okPressed() {
        saveState();
        super.okPressed();
    }

    @Override
    public void saveState() {

        scdManager.saveUIData(unusedFields.getTableData(), sourceKeys.getTableData(), surrogateKeys.getTableData(),
                type0Fields.getTableData(), type1Fields.getTableData(), type2Fields.getTableData(), type2Fields.getVersionData(),
                type3Fields.getTableData());

    }

    /**
     * Return the initial size of the dialog
     */
    @Override
    protected Point getInitialSize() {
        return new Point(810, 750);
    }

}
