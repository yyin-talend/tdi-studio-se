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
package org.talend.expressionbuilder.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.RuntimeExceptionHandler;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.expressionbuilder.ExpressionFileOperation;
import org.talend.expressionbuilder.ExpressionPersistance;
import org.talend.expressionbuilder.IExpressionDataBean;
import org.talend.expressionbuilder.i18n.Messages;
import org.talend.expressionbuilder.model.CategoryManager;
import org.talend.expressionbuilder.test.shadow.Expression;
import org.talend.expressionbuilder.test.shadow.Variable;
import org.xml.sax.SAXException;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionBuilderDialog.java ä¸Šå�ˆ10:12:13 2007-7-24 +0000 (2007-7-24) yzhang $
 * 
 */
public class ExpressionBuilderDialog extends TrayDialog implements IExpressionBuilderDialogController {

    private static final int EXPORT_ID = IDialogConstants.CLIENT_ID + 22;

    private static final int IMPORT_ID = IDialogConstants.CLIENT_ID + 21;

    private static TestComposite testComposite;

    private static ExpressionComposite expressionComposite;

    private static CategoryComposite categoryComposite;

    private final CategoryManager manager = new CategoryManager();

    private final IExpressionDataBean dataBean;

    private String defaultExpression = ""; //$NON-NLS-1$

    private List<Variable> defaultVariables;

    private Composite container;

    /**
     * Create the dialog
     * 
     * @param parentShell
     */
    public ExpressionBuilderDialog(Shell parentShell, IExpressionDataBean dataBean) {
        super(parentShell);
        setShellStyle(this.getShellStyle() | SWT.RESIZE);
        this.dataBean = dataBean;
    }

    /**
     * Create contents of the dialog
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        container = (Composite) super.createDialogArea(parent);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.makeColumnsEqualWidth = true;
        container.setLayout(gridLayout);

        final SashForm sashForm = new SashForm(container, SWT.NONE);
        sashForm.setOrientation(SWT.VERTICAL);

        final Composite upperComposite = new Composite(sashForm, SWT.NONE);
        upperComposite.setLayout(new FillLayout());

        final SashForm upperSashform = new SashForm(upperComposite, SWT.NONE);

        expressionComposite = new ExpressionComposite(this, upperSashform, SWT.NONE, dataBean);
        expressionComposite.setExpression(defaultExpression, true);

        testComposite = new TestComposite(upperSashform, SWT.NONE);
        testComposite.addVariables(defaultVariables);
        upperSashform.setWeights(new int[] { 3, 2 });

        final Composite lowerComposite = new Composite(sashForm, SWT.NONE);
        lowerComposite.setLayout(new FillLayout());

        categoryComposite = new CategoryComposite(lowerComposite, SWT.NONE, manager);

        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        sashForm.setLayoutData(gridData);
        sashForm.setWeights(new int[] { 3, 2 });

        return container;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#create()
     */
    @Override
    public void create() {
        super.create();
        addUndoOperationListener(container);
    }

    /**
     * yzhang ExpressionBuilderDialog class global comment. Detailled comment <br/>
     * 
     * $Id: ExpressionBuilderDialog.java ä¸‹å�ˆ08:32:27 2007-9-13 +0000 (2007-9-13) yzhang $
     * 
     */
    class UndoKeyListener extends KeyAdapter {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.KeyAdapter#keyPressed(org.eclipse.swt.events.KeyEvent)
         */
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.stateMask == SWT.CTRL && (e.keyCode == 'z' || e.keyCode == 'Z')) {
                getExpressionComposite().undoOperation();
            }
        }

    }

    /**
     * yzhang Comment method "addUndoOperationListener".
     * 
     * @param composite
     */
    private void addUndoOperationListener(Composite composite) {

        Control controls[] = composite.getChildren();
        for (Control control : controls) {
            if (control instanceof Composite) {
                addUndoOperationListener((Composite) control);
            }
            control.addKeyListener(new UndoKeyListener());
        }

    }

    /**
     * Create contents of the button bar
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {

        ((GridLayout) parent.getLayout()).numColumns++;

        Composite buttons = new Composite(parent, SWT.NONE);
        buttons.setLayout(new GridLayout(2, false));

        final Button importButton = new Button(buttons, SWT.PUSH);
        importButton.setToolTipText(Messages.getString("ExpressionBuilderDialog.import"));//$NON-NLS-1$
        importButton.setImage(ImageProvider.getImage(EImage.IMPORT_ICON));

        final Button exportButton = new Button(buttons, SWT.PUSH);
        exportButton.setToolTipText(Messages.getString("ExpressionBuilderDialog.export"));//$NON-NLS-1$
        exportButton.setImage(ImageProvider.getImage(EImage.EXPORT_ICON));

        createButton(parent, IDialogConstants.OK_ID, Messages.getString("ExpressionBuilderDialog.ok.button"), true); //$NON-NLS-1$
        createButton(parent, IDialogConstants.CANCEL_ID, Messages.getString("ExpressionBuilderDialog.cancel.button"), false); //$NON-NLS-1$

        exportButton.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {

                FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
                dialog.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$

                String filePath = dialog.open();
                if (filePath != null) {
                    String expresionContent = expressionComposite.getExpression();
                    List<Variable> variables = testComposite.getVariableList();
                    File file = new File(filePath);
                    ExpressionFileOperation operation = new ExpressionFileOperation();
                    try {
                        if (file != null) {
                            file.createNewFile();
                        }
                        operation.saveExpressionToFile(file, variables, expresionContent);
                    } catch (IOException e1) {
                        RuntimeExceptionHandler.process(e1);
                    } catch (ParserConfigurationException e1) {
                        RuntimeExceptionHandler.process(e1);
                    }
                }
            }
        });

        importButton.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
                dialog.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$

                String filePath = dialog.open();
                if (filePath != null) {
                    File file = new File(filePath);
                    ExpressionFileOperation operation = new ExpressionFileOperation();
                    try {
                        List list = operation.importExpressionFromFile(file, getShell());
                        if (list != null && list.size() != 0) {
                            expressionComposite.setExpression((String) list.get(0), false);
                            list.remove(0);
                            if (list.size() > 0) {
                                testComposite.addVariables(list);
                            }
                        }
                    } catch (IOException e1) {
                        RuntimeExceptionHandler.process(e1);
                    } catch (ParserConfigurationException e1) {
                        RuntimeExceptionHandler.process(e1);
                    } catch (SAXException e1) {
                        RuntimeExceptionHandler.process(e1);
                    }
                }
            }
        });

    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ExpressionBuilderDialog.expression.builder")); //$NON-NLS-1$
    }

    @Override
    protected Point getInitialSize() {
        return new Point(870, 500);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#close()
     */
    @Override
    public boolean close() {
        testComposite.stopServerThread();
        return super.close();
    }

    /**
     * Getter for testComposite.
     * 
     * @return the testComposite
     */
    public static TestComposite getTestComposite() {
        return testComposite;
    }

    /**
     * Getter for expressionComposite.
     * 
     * @return the expressionComposite
     */
    public static ExpressionComposite getExpressionComposite() {
        return expressionComposite;
    }

    /**
     * Getter for categoryComposite.
     * 
     * @return the categoryComposite
     */
    public static CategoryComposite getCategoryComposite() {
        return categoryComposite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {

        dataBean.setConsumerExpression(expressionComposite.getExpression() + " ");

        ExpressionPersistance.getInstance().saveExpression(
                new Expression(expressionComposite.getExpression(), testComposite.getVariableList()));

        super.okPressed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.ui.IExpressionBuilderDialogController#openDialog()
     */
    public void openDialog(Object obj) {
        if (obj instanceof IExpressionDataBean) {

            List<Variable> vars = new ArrayList<Variable>();
            IExpressionDataBean bean = (IExpressionDataBean) obj;
            setDefaultExpression(bean.getExpression());
            if (bean.getVariables() != null) {
                vars.addAll(bean.getVariables());
            }

            ExpressionPersistance persistance = ExpressionPersistance.getInstance();
            persistance.setOwnerId(bean.getOwnerId());
            persistance.setPath(getExpressionStorePath());
            for (Variable var1 : persistance.loadExpression().getVariables()) {
                boolean needAdd = true;
                for (Variable var2 : vars) {
                    if (var1.getName().equals(var2.getName())) {
                        needAdd = false;
                        break;
                    }
                }
                if (needAdd) {
                    vars.add(var1);
                }
            }

            addVariables(vars);
        }
        open();
        setBlockOnOpen(true);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.ui.IExpressionBuilderDialogController#setDefaultExpression(java.lang.String)
     */
    public void setDefaultExpression(String expression) {
        defaultExpression = expression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.ui.IExpressionBuilderDialogController#setVariables(java.util.List)
     */
    public void addVariables(List<Variable> variables) {
        defaultVariables = variables;
    }

    /**
     * yzhang Comment method "getExpressionStorePath".
     * 
     * @return
     */
    private String getExpressionStorePath() {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();
        IProject p = root.getProject(project.getTechnicalLabel());

        IFolder configurationFolder = p.getFolder(ExpressionPersistance.CONFIGURATION_FOLDER_NAME);
        if (!configurationFolder.exists()) {
            try {
                configurationFolder.create(true, true, null);
            } catch (CoreException e) {
                RuntimeExceptionHandler.process(e);
            }
        }

        IFolder expressionFolder = configurationFolder.getFolder(ExpressionPersistance.EXPRESSION_FOLDER_NAME);
        if (!expressionFolder.exists()) {
            try {
                expressionFolder.create(true, true, null);
            } catch (CoreException e) {
                RuntimeExceptionHandler.process(e);
            }
        }

        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        String jobName = null;
        if (editor instanceof MultiPageTalendEditor) {
            jobName = ((MultiPageTalendEditor) editor).getTalendEditor().getCurrentJobResource().getJobName();
        }
        IPath path = expressionFolder.getLocation().append(jobName + ".xml");
        return path.toOSString();
    }
}
