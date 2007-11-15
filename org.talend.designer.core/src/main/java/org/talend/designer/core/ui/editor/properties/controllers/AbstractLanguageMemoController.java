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
package org.talend.designer.core.ui.editor.properties.controllers;

import java.beans.PropertyChangeEvent;

import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IControlCreator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.ui.swt.colorstyledtext.ColorManager;
import org.talend.commons.ui.swt.colorstyledtext.ColorStyledText;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.ui.viewer.perl.TalendPerlSourceViewer;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.ContextParameterExtractor;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: SQLEditorController.java 1 2006-12-12 上午11:24:40 +0000 (上午11:24:40) yzhang $
 * 
 */
public abstract class AbstractLanguageMemoController extends AbstractElementPropertySectionController {

    private static boolean estimateInitialized = false;

    private static int rowSizeFixed = 0;

    private static int rowSizeByLine = 0;

    /**
     * DOC dev LanguageMemoController constructor comment.
     * 
     * @param parameterBean
     */
    public AbstractLanguageMemoController(IDynamicProperty dp) {
        super(dp);
        setLanguage();
    }

    private String language;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createControl()
     */
    @Override
    public Control createControl(final Composite subComposite, final IElementParameter param, final int numInRow,
            final int nbInRow, final int top, final Control lastControl) {

        int nbLines = param.getNbLines();

        IControlCreator txtCtrl = new IControlCreator() {

            public Control createControl(final Composite parent, final int style) {
                ColorManager colorManager = new ColorManager(CorePlugin.getDefault().getPreferenceStore());
                StyledText control = new ColorStyledText(parent, style, colorManager, language);
                IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();
                String fontType = preferenceStore.getString(TalendDesignerPrefConstants.MEMO_TEXT_FONT);
                FontData fontData = new FontData(fontType);
                Font font = new Font(parent.getDisplay(), fontData);
                control.setFont(font);
                return control;
            }
        };
        DecoratedField dField = null;
        Control cLayout;
        StyledText text;
        FormData data;
        if (param.getNbLines() != 1) {
            if (language.equals("java")) {
                dField = new DecoratedField(subComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP,
                        txtCtrl);
                cLayout = dField.getLayoutControl();
                text = (StyledText) dField.getControl();
                data = (FormData) text.getLayoutData();
                editionControlHelper.register(param.getName(), text, true);
            } else {
                Composite a = new Composite(subComposite, SWT.NO_FOCUS);
                a.setLayout(new FormLayout());
                Composite b = new Composite(a, SWT.NO_FOCUS);
                b.setLayout(new GridLayout());
                data = new FormData();
                data.left = new FormAttachment(0, 0);
                data.top = new FormAttachment(0, 0);
                data.right = new FormAttachment(100, 0);
                data.bottom = new FormAttachment(100, 0);

                b.setLayoutData(data);
                TalendPerlSourceViewer viewer = (TalendPerlSourceViewer) TalendPerlSourceViewer.createViewer(b, SWT.BORDER
                        | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP, true);
                text = viewer.getTextWidget();

                Process process = null;
                if (elem instanceof Node) {
                    process = (Process) ((Node) elem).getProcess();
                } else if (elem instanceof Connection) {
                    process = (Process) ((Connection) elem).getSource().getProcess();
                }
                if (process != null) {
                    ContextParameterExtractor.installOn(text, process, param.getName(), elem);
                }
                UndoRedoHelper helper = new UndoRedoHelper();
                helper.register(text);
                cLayout = a;
            }

        } else {
            dField = new DecoratedField(subComposite, SWT.BORDER | SWT.WRAP, txtCtrl);
            cLayout = dField.getLayoutControl();
            text = (StyledText) dField.getControl();
            data = (FormData) text.getLayoutData();
            editionControlHelper.register(param.getName(), text, true);
        }
        if (param.isRequired()) {
            FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                    FieldDecorationRegistry.DEC_REQUIRED);
            dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.TOP, false);
        }

        if (getAdditionalHeightSize() != 0) {
            nbLines += this.getAdditionalHeightSize() / text.getLineHeight();
        }
        data.height = text.getLineHeight() * nbLines;
        text.getParent().setSize(subComposite.getSize().x, text.getLineHeight() * nbLines);
        cLayout.setBackground(subComposite.getBackground());
        text.setEnabled(!param.isReadOnly());
        if (elem instanceof Node) {
            text.setToolTipText(VARIABLE_TOOLTIP + param.getVariableName());
        }

        addDragAndDropTarget(text);

        CLabel labelLabel = getWidgetFactory().createCLabel(subComposite, param.getDisplayName());
        data = new FormData();
        if (lastControl != null) {
            data.left = new FormAttachment(lastControl, 0);
        } else {
            data.left = new FormAttachment((((numInRow - 1) * MAX_PERCENT) / nbInRow), 0);
        }
        data.top = new FormAttachment(0, top);
        labelLabel.setLayoutData(data);
        if (numInRow != 1) {
            labelLabel.setAlignment(SWT.RIGHT);
        }
        // *********************
        data = new FormData();
        int currentLabelWidth = STANDARD_LABEL_WIDTH;
        GC gc = new GC(labelLabel);
        Point labelSize = gc.stringExtent(param.getDisplayName());
        gc.dispose();

        if ((labelSize.x + ITabbedPropertyConstants.HSPACE) > currentLabelWidth) {
            currentLabelWidth = labelSize.x + ITabbedPropertyConstants.HSPACE;
        }

        if (numInRow == 1) {
            if (lastControl != null) {
                data.left = new FormAttachment(lastControl, currentLabelWidth);
            } else {
                data.left = new FormAttachment(0, currentLabelWidth);
            }

        } else {
            data.left = new FormAttachment(labelLabel, 0, SWT.RIGHT);
        }
        data.right = new FormAttachment((numInRow * MAX_PERCENT) / nbInRow, 0);
        data.top = new FormAttachment(0, top);
        cLayout.setLayoutData(data);
        // **********************
        hashCurControls.put(param.getName(), text);

        Point initialSize = cLayout.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        dynamicProperty.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#estimateRowSize(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter)
     */
    @Override
    public int estimateRowSize(Composite subComposite, IElementParameter param) {
        if (!estimateInitialized) {
            IControlCreator txtCtrl = new IControlCreator() {

                public Control createControl(final Composite parent, final int style) {
                    ColorManager colorManager = new ColorManager(CorePlugin.getDefault().getPreferenceStore());
                    ColorStyledText colorText = new ColorStyledText(parent, style, colorManager, language);

                    IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();
                    String fontType = preferenceStore.getString(TalendDesignerPrefConstants.MEMO_TEXT_FONT);
                    FontData fontData = new FontData(fontType);
                    Font font = new Font(parent.getDisplay(), fontData);
                    colorText.setFont(font);
                    return colorText;
                }
            };

            DecoratedField dField = null;
            if (param.getNbLines() != 1) {
                dField = new DecoratedField(subComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP,
                        txtCtrl);
            } else {
                dField = new DecoratedField(subComposite, SWT.BORDER | SWT.WRAP, txtCtrl);
            }

            StyledText text = (StyledText) dField.getControl();
            FormData d = (FormData) text.getLayoutData();
            d.height = text.getLineHeight();
            text.getParent().setSize(subComposite.getSize().x, text.getLineHeight());
            rowSizeByLine = text.getLineHeight();
            Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
            dField.getLayoutControl().dispose();
            rowSizeFixed = ITabbedPropertyConstants.VSPACE + (initialSize.y - rowSizeByLine);
            estimateInitialized = true;
        }

        return rowSizeFixed + (rowSizeByLine * param.getNbLines());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#hasDynamicRowSize()
     */
    @Override
    public boolean hasDynamicRowSize() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub

    }

    /**
     * DOC ftang Comment method "setLanguage".
     */
    protected abstract void setLanguage();

    protected void setCurrentLanguage(String language) {
        this.language = language;
    }

    @Override
    public void refresh(IElementParameter param, boolean checkErrorsWhenViewRefreshed) {
        StyledText text = (StyledText) hashCurControls.get(param.getName());
        Object value = param.getValue();
        // boolean valueChanged = false;
        if (value == null) {
            text.setText(""); //$NON-NLS-1$
        } else {
            if (!value.equals(text.getText())) {
                text.setText((String) value);
                // valueChanged = true;
            }
        }
        // if (checkErrorsWhenViewRefreshed || valueChanged) {
        // checkErrorsForPropertiesOnly(text);
        // }
    }
}
