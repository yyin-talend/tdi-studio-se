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

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.properties.tab.HorizontalTabFactory;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.ChangeNoteOpacityCommand;
import org.talend.designer.core.ui.editor.cmd.ChangeNoteTextCommand;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.notes.Note;

/**
 * yzhang class global comment. Detailled comment
 */
public class BasicNotePropertyComposite extends AbstractNotePropertyComposite {

    private Button check;

    private Text text;

    private Button notecolorbutton;

    private Button textcolorbutton;

    private Composite composite;

    private static final int SIZE_X = 50;

    private static final int SIZE_Y = 18;

    private Group adjustTextGroup, adjustLabelGroup;

    private Button leftBtn, rightBtn, centreBtn, centreLabelBtn, topBtn, bottomBtn;

    /**
     * DOC yzhang BasicNotePropertyComposite constructor comment.
     * 
     * @param parent
     * @param note
     * @param tabFactory
     */
    public BasicNotePropertyComposite(Composite parent, Note note, HorizontalTabFactory tabFactory) {
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
        if (composite.getLayout() instanceof FormLayout) {
            FormLayout formLayout = (FormLayout) composite.getLayout();
            formLayout.spacing = 0;
        }
        FormData data;

        check = getWidgetFactory().createButton(composite, "", SWT.CHECK); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        check.setLayoutData(data);

        CLabel labelLabel = getWidgetFactory().createCLabel(composite, Messages.getString("OpaqueNoteSection.Label")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(check);
        data.top = new FormAttachment(check, 0, SWT.TOP);
        labelLabel.setLayoutData(data);

        check.setSelection(note.isOpaque());

        check.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (check.getSelection() != (note.isOpaque())) {
                    ChangeNoteOpacityCommand command = new ChangeNoteOpacityCommand(note, check.getSelection());
                    getCommandStack().execute(command);
                }
            }
        });
        CLabel labelcolor = getWidgetFactory().createCLabel(composite, Messages.getString("BasicNotePropertyComposite.Label")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.top = new FormAttachment(check, 30);
        labelcolor.setLayoutData(data);
        notecolorbutton = getWidgetFactory().createButton(composite, "", SWT.NONE); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(labelcolor);
        data.top = new FormAttachment(labelcolor, 0, SWT.TOP);
        notecolorbutton.setLayoutData(data);
        colorButtonrefresh();
        notecolorbutton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Control ctrl = (Control) e.getSource();
                if (ctrl instanceof Button) {
                    ColorDialog colorDialog = new ColorDialog(ctrl.getShell());
                    colorDialog.setRGB(TalendTextUtils.stringToRGB((String) note.getPropertyValue(EParameterName.NOTE_COLOR
                            .getName())));
                    RGB rgb = colorDialog.open();
                    if (rgb != null) {
                        String value = rgb.red + ";" + rgb.green + ";" + rgb.blue;
                        Command cmd = new PropertyChangeCommand(note, EParameterName.NOTE_COLOR.getName(), value);
                        getCommandStack().execute(cmd);
                        note.refresh();
                        colorButtonrefresh();
                    }
                }
            }
        });

        CLabel textcolor = getWidgetFactory().createCLabel(composite, Messages.getString("BasicNotePropertyComposite.Text")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(notecolorbutton, 30);
        data.top = new FormAttachment(check, 30);
        textcolor.setLayoutData(data);
        textcolorbutton = getWidgetFactory().createButton(composite, "", SWT.NONE); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(textcolor);
        data.top = new FormAttachment(textcolor, 0, SWT.TOP);
        textcolorbutton.setLayoutData(data);
        textcolorButtonrefresh();
        textcolorbutton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Control ctrl = (Control) e.getSource();
                if (ctrl instanceof Button) {
                    ColorDialog colorDialog = new ColorDialog(ctrl.getShell());
                    colorDialog.setRGB(TalendTextUtils.stringToRGB((String) note.getPropertyValue(EParameterName.NOTETXT_COLOR
                            .getName())));
                    RGB rgb = colorDialog.open();
                    if (rgb != null) {
                        String value = rgb.red + ";" + rgb.green + ";" + rgb.blue;
                        Command cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_COLOR.getName(), value);
                        getCommandStack().execute(cmd);
                        note.refresh();
                        text.setForeground(new Color(null, TalendTextUtils.stringToRGB((String) note
                                .getPropertyValue(EParameterName.NOTETXT_COLOR.getName()))));
                        textcolorButtonrefresh();
                    }
                }
            }
        });

        adjustLabelGroup = getWidgetFactory().createGroup(composite, "Adjust horizontal");
        data = new FormData();
        adjustLabelGroup.setLayout(new GridLayout(3, false));
        data.left = new FormAttachment(0, 0);
        data.top = new FormAttachment(notecolorbutton, 30);
        adjustLabelGroup.setLayoutData(data);
        leftBtn = getWidgetFactory().createButton(adjustLabelGroup, "left", SWT.RADIO);
        leftBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        leftBtn.setSelection((Boolean) note.getPropertyValue(EParameterName.NOTETXT_LEFT.getName()));
        rightBtn = getWidgetFactory().createButton(adjustLabelGroup, "right", SWT.RADIO);
        rightBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        rightBtn.setSelection((Boolean) note.getPropertyValue(EParameterName.NOTETXT_RIGHT.getName()));
        centreBtn = getWidgetFactory().createButton(adjustLabelGroup, "centre", SWT.RADIO);
        centreBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        centreBtn.setSelection((Boolean) note.getPropertyValue(EParameterName.NOTETXT_CENTER.getName()));

        adjustTextGroup = getWidgetFactory().createGroup(composite, "Adjust vertical");
        data = new FormData();
        adjustTextGroup.setLayout(new GridLayout(3, false));
        data.left = new FormAttachment(adjustLabelGroup, 10);
        data.top = new FormAttachment(notecolorbutton, 30);
        adjustTextGroup.setLayoutData(data);
        topBtn = getWidgetFactory().createButton(adjustTextGroup, "top", SWT.RADIO);
        topBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        topBtn.setSelection((Boolean) note.getPropertyValue(EParameterName.NOTETXT_TOP.getName()));
        bottomBtn = getWidgetFactory().createButton(adjustTextGroup, "bottom", SWT.RADIO);
        bottomBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        bottomBtn.setSelection((Boolean) note.getPropertyValue(EParameterName.NOTETXT_BOTTOM.getName()));
        centreLabelBtn = getWidgetFactory().createButton(adjustTextGroup, "centre", SWT.RADIO);
        centreLabelBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        centreLabelBtn.setSelection((Boolean) note.getPropertyValue(EParameterName.NOTELABEL_CENTER.getName()));

        CLabel textLabel = getWidgetFactory().createCLabel(composite, Messages.getString("TextNoteSection.Label")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.top = new FormAttachment(adjustTextGroup, 30);
        textLabel.setLayoutData(data);
        text = getWidgetFactory().createText(composite, "", SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(textLabel, 600);
        data.top = new FormAttachment(textLabel, 0, SWT.TOP);
        data.height = 5 * text.getLineHeight(); // 5 lines
        text.setLayoutData(data);
        text.setForeground(new Color(null, TalendTextUtils.stringToRGB((String) note
                .getPropertyValue(EParameterName.NOTETXT_COLOR.getName()))));
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

        text.addKeyListener(new KeyListener() {

            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                // TODO Auto-generated method stub

            }

            public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
                // TODO Auto-generated method stub
                if (!text.getText().equals(note.getText())) {
                    ChangeNoteTextCommand command = new ChangeNoteTextCommand(note, text.getText());
                    getCommandStack().execute(command);
                }
            }

        });
        addListener();
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

    private ImageData createColorImage(Button button, RGB color) {
        int width = SIZE_X - 5;
        int height = SIZE_Y - 5;

        RGB black = new RGB(0, 0, 0);
        PaletteData dataPalette = new PaletteData(new RGB[] { black, black, color });
        ImageData data = new ImageData(width, height, 4, dataPalette);
        data.transparentPixel = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || y == 0 || x == (width - 1) || y == (height - 1)) {
                    data.setPixel(x, y, 1);
                } else {
                    data.setPixel(x, y, 2);
                }
            }
        }

        return data;
    }

    private void colorButtonrefresh() {
        ImageData id = createColorImage(notecolorbutton, TalendTextUtils.stringToRGB((String) note
                .getPropertyValue(EParameterName.NOTE_COLOR.getName())));
        Image image = new Image(notecolorbutton.getDisplay(), id, id.getTransparencyMask());
        notecolorbutton.setImage(image);
    }

    private void textcolorButtonrefresh() {
        ImageData id = createColorImage(textcolorbutton, TalendTextUtils.stringToRGB((String) note
                .getPropertyValue(EParameterName.NOTETXT_COLOR.getName())));
        Image image = new Image(textcolorbutton.getDisplay(), id, id.getTransparencyMask());
        textcolorbutton.setImage(image);
    }

    private void addListener() {
        leftBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Boolean value = leftBtn.getSelection();
                if (value) {
                    Command cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_LEFT.getName(), value);
                    getCommandStack().execute(cmd);
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTETXT_RIGHT.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_RIGHT.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTETXT_CENTER.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_CENTER.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    note.refresh();
                }
            }
        });
        rightBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Boolean value = rightBtn.getSelection();
                if (value) {
                    Command cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_RIGHT.getName(), value);
                    getCommandStack().execute(cmd);
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTETXT_LEFT.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_LEFT.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTETXT_CENTER.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_CENTER.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    note.refresh();
                }
            }
        });
        centreBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Boolean value = centreBtn.getSelection();
                if (value) {
                    Command cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_CENTER.getName(), value);
                    getCommandStack().execute(cmd);
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTETXT_LEFT.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_LEFT.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTETXT_RIGHT.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_RIGHT.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    note.refresh();
                }
            }
        });

        topBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Boolean value = topBtn.getSelection();
                if (value) {
                    Command cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_TOP.getName(), value);
                    getCommandStack().execute(cmd);
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTELABEL_CENTER.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTELABEL_CENTER.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTETXT_BOTTOM.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_BOTTOM.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    note.refresh();
                }
            }
        });
        bottomBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Boolean value = bottomBtn.getSelection();
                if (value) {
                    Command cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_BOTTOM.getName(), value);
                    getCommandStack().execute(cmd);
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTELABEL_CENTER.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTELABEL_CENTER.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTETXT_TOP.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_TOP.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    note.refresh();
                }
            }
        });
        centreLabelBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Boolean value = centreLabelBtn.getSelection();
                if (value) {
                    Command cmd = new PropertyChangeCommand(note, EParameterName.NOTELABEL_CENTER.getName(), value);
                    getCommandStack().execute(cmd);
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTETXT_BOTTOM.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_BOTTOM.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    if ((Boolean) note.getPropertyValue(EParameterName.NOTETXT_TOP.getName())) {
                        cmd = new PropertyChangeCommand(note, EParameterName.NOTETXT_TOP.getName(), !value);
                        getCommandStack().execute(cmd);
                    }
                    note.refresh();
                }
            }
        });
    }

}
