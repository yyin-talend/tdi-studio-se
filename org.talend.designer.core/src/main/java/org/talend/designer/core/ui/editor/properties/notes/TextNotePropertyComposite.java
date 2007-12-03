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
package org.talend.designer.core.ui.editor.properties.notes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.properties.tab.HorizontalTabFactory;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.cmd.ChangeNoteTextCommand;
import org.talend.designer.core.ui.editor.notes.Note;

/**
 * yzhang class global comment. Detailled comment
 */
public class TextNotePropertyComposite extends AbstractNotePropertyComposite {

    private Text text;

    private Composite composite;

    /**
     * yzhang TextNotePropertyComposite constructor comment.
     * 
     * @param parent
     * @param note
     * @param tabFactory
     */
    public TextNotePropertyComposite(Composite parent, Note note, HorizontalTabFactory tabFactory) {
        super(parent, note, tabFactory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.notes.AbstractNotePropertyComposite#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        composite = getWidgetFactory().createFlatFormComposite(parent);
        FormData data;

        text = getWidgetFactory().createText(composite, "", SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        data.height = 5 * text.getLineHeight(); // 5 lines
        text.setLayoutData(data);

        CLabel labelLabel = getWidgetFactory().createCLabel(composite, Messages.getString("TextNoteSection.Label")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(text, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(text, 0, SWT.TOP);
        labelLabel.setLayoutData(data);

        text.setText(note.getText());

        text.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                if (!text.getText().equals(note.getText())) {
                    ChangeNoteTextCommand command = new ChangeNoteTextCommand(note, text.getText());
                    getCommandStack().execute(command);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.notes.AbstractNotePropertyComposite#getComposite()
     */
    @Override
    public Composite getComposite() {
        return composite;
    }

}
