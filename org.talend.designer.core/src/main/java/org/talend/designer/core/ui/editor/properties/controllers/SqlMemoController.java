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

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.colorstyledtext.ColorStyledText;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.dialogs.ModelSelectionDialog;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.cmd.RepositoryChangeQueryCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty;
import org.talend.sqlbuilder.SqlBuilderPlugin;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: SQLEditorController.java 1 2006-12-12 上午11:24:40 +0000 (上午11:24:40) yzhang $
 * 
 */
public class SqlMemoController extends AbstractElementPropertySectionController {

    private IElementParameter switchParam;

    /**
     * DOC yzhang SqlMemoController constructor comment.
     * 
     * @param dtp
     */
    public SqlMemoController(IDynamicProperty dp) {
        super(dp);
    }

    private Button openSQLEditorButton;

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub

    }

    SelectionListener listenerSelection = new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent e) {

        }

        public void widgetSelected(SelectionEvent e) {
            Command cmd = createCommand();
            executeCommand(cmd);
        }
    };

    private ColorStyledText queryText;

    private Command createCommand() {
        initConnectionParameters();
        String repositoryType = (String) elem.getPropertyValue(EParameterName.PROPERTY_TYPE.getName());
        String propertyName = (String) openSQLEditorButton.getData(PARAMETER_NAME);
        String query = (String) elem.getPropertyValue(propertyName);
        String qoute = (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) ? TalendTextUtils.QUOTATION_MARK
                : TalendTextUtils.SINGLE_QUOTE;

        boolean isCanOpen = !ContextParameterUtils.isContainContextParam(query) && query.startsWith(qoute)
                && query.endsWith(qoute) && query.length() > 1;
        isCanOpen = isCanOpen || "".equals(query);
        if (!isCanOpen) {
            String pid = SqlBuilderPlugin.PLUGIN_ID;
            String mainMsg = Messages.getString("SqlMemoController.QueryError.mainMsg");
            String infoMsg = Messages.getString("SqlMemoController.QueryError.infoMsg", qoute);
            new ErrorDialogWidthDetailArea(composite.getShell(), pid, mainMsg, infoMsg);
            return null;
        }

        query = this.removeStrInQuery(query);
        initConnectionParametersWithContext(elem, part == null ? new EmptyContextManager().getDefaultContext() : part
                .getTalendEditor().getProcess().getContextManager().getDefaultContext());
        String sql = openSQLBuilder(repositoryType, propertyName, query);
        if (sql != null) {
            queryText.setText(sql);
            return new PropertyChangeCommand(elem, propertyName, sql);
        }
        return null;
    }

    /**
     * DOC ftang Comment method "removeStrInQuery".
     * 
     * @param input
     * @return
     */
    private String removeStrInQuery(String input) {
        String out = removeSlash(input);
        return TalendTextUtils.removeQuotes(out);
    }

    /**
     * DOC ftang Comment method "removeSlash".
     * 
     * @param input
     * @return
     */
    private String removeSlash(String input) {
        String out = input.replaceAll("\\\\", ""); //$NON-NLS-1$ //$NON-NLS-2$
        return out;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createControl(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter, int, int, int, org.eclipse.swt.widgets.Control)
     */
    @Override
    public Control createControl(Composite subComposite, IElementParameter param, int numInRow, int nbInRow, int top,
            Control lastControl) {
        this.curParameter = param;
        this.paramFieldType = param.getField();
        switchParam = elem.getElementParameter(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName());

        final DecoratedField dField1 = new DecoratedField(subComposite, SWT.PUSH, new IControlCreator() {

            public Control createControl(Composite parent, int style) {
                return new Button(parent, style);
            }
        });

        Control buttonControl = dField1.getLayoutControl();
        openSQLEditorButton = (Button) dField1.getControl();
        openSQLEditorButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        openSQLEditorButton.setImage(ImageProvider.getImage(CorePlugin.getImageDescriptor(DOTS_BUTTON)));
        buttonControl.setBackground(subComposite.getBackground());
        openSQLEditorButton.setEnabled(true);
        openSQLEditorButton.setData(NAME, SQLEDITOR);
        openSQLEditorButton.setData(PARAMETER_NAME, param.getName());
        openSQLEditorButton.addSelectionListener(listenerSelection);

        FormData data1 = new FormData();
        data1.right = new FormAttachment(100, -ITabbedPropertyConstants.HSPACE);
        data1.left = new FormAttachment(100, -(ITabbedPropertyConstants.HSPACE + STANDARD_BUTTON_WIDTH));
        data1.top = new FormAttachment(0, top);

        buttonControl.setLayoutData(data1);

        int nbLines = param.getNbLines();

        IControlCreator txtCtrl = new IControlCreator() {

            public Control createControl(final Composite parent, final int style) {
                ColorStyledText colorText = new ColorStyledText(parent, style, CorePlugin.getDefault().getPreferenceStore(),
                        "tsql"); //$NON-NLS-1$
                Font font = new Font(parent.getDisplay(), "courier", 8, SWT.NONE); //$NON-NLS-1$
                colorText.setFont(font);
                return colorText;
            }
        };
        DecoratedField dField = null;
        if (param.getNbLines() != 1) {
            dField = new DecoratedField(subComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, txtCtrl);
        } else {
            dField = new DecoratedField(subComposite, SWT.BORDER, txtCtrl);
        }
        if (param.isRequired()) {
            FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                    FieldDecorationRegistry.DEC_REQUIRED);
            dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.TOP, false);
        }
        Control cLayout = dField.getLayoutControl();
        queryText = (ColorStyledText) dField.getControl();
        queryText.setData(PARAMETER_NAME, param.getName());
        editionControlHelper.register(param.getName(), queryText, true);

        FormData d = (FormData) queryText.getLayoutData();
        if (getAdditionalHeightSize() != 0) {
            nbLines += this.getAdditionalHeightSize() / queryText.getLineHeight();
        }
        d.height = queryText.getLineHeight() * nbLines;
        FormData data;
        queryText.getParent().setSize(subComposite.getSize().x, queryText.getLineHeight() * nbLines);
        cLayout.setBackground(subComposite.getBackground());
        queryText.setEnabled(!param.isRepositoryValueUsed());
        if (elem instanceof Node) {
            queryText.setToolTipText(VARIABLE_TOOLTIP + param.getVariableName());
        }
        queryText.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                if (switchParam != null) {
                    switchParam.setValue(Boolean.FALSE);
                }

            }

            public void keyReleased(KeyEvent e) {
            }

        });

        if (param.isReadOnly() && param.isRepositoryValueUsed()) {
            cLayout.addMouseListener(listenerClick);
        }
        addDragAndDropTarget(queryText);

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
        data.right = new FormAttachment(buttonControl, -5, SWT.LEFT);
        data.top = new FormAttachment(0, top);
        cLayout.setLayoutData(data);
        // **********************
        hashCurControls.put(param.getName(), queryText);

        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        // curRowSize = initialSize.y + ITabbedPropertyConstants.VSPACE;
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
        IControlCreator txtCtrl = new IControlCreator() {

            public Control createControl(final Composite parent, final int style) {
                ColorStyledText colorText = new ColorStyledText(parent, style, CorePlugin.getDefault().getPreferenceStore(),
                        "tsql");
                Font font = new Font(parent.getDisplay(), "courier", 8, SWT.NONE); //$NON-NLS-1$
                colorText.setFont(font);
                return colorText;
            }
        };

        DecoratedField dField = null;
        if (param.getNbLines() != 1) {
            dField = new DecoratedField(subComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, txtCtrl);
        } else {
            dField = new DecoratedField(subComposite, SWT.BORDER, txtCtrl);
        }
        ColorStyledText text = (ColorStyledText) dField.getControl();
        FormData d = (FormData) text.getLayoutData();
        d.height = text.getLineHeight() * param.getNbLines();
        text.getParent().setSize(subComposite.getSize().x, text.getLineHeight() * param.getNbLines());

        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        dField.getLayoutControl().dispose();

        return initialSize.y + ITabbedPropertyConstants.VSPACE;
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

    @Override
    public void refresh(IElementParameter param, boolean checkErrorsWhenViewRefreshed) {
        ColorStyledText labelText = (ColorStyledText) hashCurControls.get(param.getName());
        if (labelText == null || labelText.isDisposed()) {
            return;
        }
        Object value = param.getValue();

        boolean valueChanged = false;
        if (value == null) {
            labelText.setText(""); //$NON-NLS-1$
        } else {
            if (!value.equals(labelText.getText())) {
                labelText.setText((String) value);
                valueChanged = true;
            }
        }
        if (checkErrorsWhenViewRefreshed || valueChanged) {
            checkErrorsForPropertiesOnly(labelText);
        }
        fixedCursorPosition(param, labelText, value, valueChanged);
    }

    MouseListener listenerClick = new MouseAdapter() {

        public void mouseDown(MouseEvent e) {

            ModelSelectionDialog modelSelect = new ModelSelectionDialog(((Control) e.getSource()).getShell());

            if (modelSelect.open() == ModelSelectionDialog.OK) {
                if (modelSelect.getOptionValue() == 0) {
                    executeCommand(changeToBuildInCommand());
                }
                if (modelSelect.getOptionValue() == 1) {
                    executeCommand(refreshConnectionCommand());
                }
            }
        }
    };

    private Command changeToBuildInCommand() {

        return new RepositoryChangeQueryCommand(this.elem, null, "QUERYSTORE_TYPE", "BUILT_IN");

    }

    private Command refreshConnectionCommand() {

        return createCommand();
    }
}
