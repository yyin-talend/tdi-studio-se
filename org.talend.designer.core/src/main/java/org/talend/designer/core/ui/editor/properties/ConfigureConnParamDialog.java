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
package org.talend.designer.core.ui.editor.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.utils.PathExtractor;
import org.talend.designer.core.i18n.Messages;
import org.talend.sqlbuilder.util.EConnectionParameterName;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class ConfigureConnParamDialog extends Dialog {

    private static final String TEXT_CONTROL = "TEXT";

    private static final String CONTEXT_WITH = Messages.getString("ConfigureConnParamDialog.ContextText"); //$NON-NLS-1$

    private static final int LABEL_DEFAULT_X = 103;

    private static final int DEFAULT_HEIGHT = 13;

    private final ConnectionParameters parameters;

    private static IContextManager contextManager;

    private final Map<EConnectionParameterName, String> pvValues = new HashMap<EConnectionParameterName, String>();

    /**
     * qzhang ConfigureConnParamDialog constructor comment.
     * 
     * @param parentShell
     * @param parameters
     * @param contextManager
     */
    public ConfigureConnParamDialog(Shell parentShell, ConnectionParameters parameters, IContextManager contextManager) {
        super(parentShell);
        this.parameters = parameters;
        pvValues.put(EConnectionParameterName.PASSWORD, parameters.getPassword());
        pvValues.put(EConnectionParameterName.PORT, parameters.getPort());
        pvValues.put(EConnectionParameterName.SCHEMA, parameters.getSchema());
        pvValues.put(EConnectionParameterName.SERVER_NAME, parameters.getHost());
        pvValues.put(EConnectionParameterName.USERNAME, parameters.getUserName());
        pvValues.put(EConnectionParameterName.SID, parameters.getDbName());
        pvValues.put(EConnectionParameterName.FILE, parameters.getFilename());
        pvValues.put(EConnectionParameterName.DIRECTORY, parameters.getDirectory());
        pvValues.put(EConnectionParameterName.DATASOURCE, parameters.getDatasource());

        ConfigureConnParamDialog.contextManager = contextManager;
        setShellStyle(SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.DIALOG_TRIM);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ConfigureConnParamDialog.DialogLabel")); //$NON-NLS-1$
        newShell.setSize(350, 440);
        Rectangle screen = Display.getDefault().getBounds();
        newShell.setLocation(screen.width / 3, screen.height / 3);
    }

    private Combo contextCombo;

    private Composite mainComposite;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        mainComposite = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.verticalSpacing = 0;
        mainComposite.setLayout(gridLayout);
        IContext defaultContext = createContextComposite();
        Label hLabel = new Label(mainComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
        hLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        addComponents(defaultContext);

        contextCombo.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                for (Text text : allParamText) {
                    if (text != null && !text.isDisposed()) {
                        Object data = text.getData(TEXT_CONTROL);
                        if (data != null && data instanceof Text) {
                            resetValues(text, (Text) data);
                        }
                    }
                }
            }
        });

        return mainComposite;
    }

    private final List<Text> allParamText = new ArrayList<Text>();

    /**
     * qzhang Comment method "addComponents".
     * 
     * @param defaultContext
     */
    private void addComponents(IContext defaultContext) {
        for (EConnectionParameterName key : EConnectionParameterName.values()) {
            if (parameters.getRepositoryNameParaName().get(key.getName()) != null) {
                if (key.equals(EConnectionParameterName.FILE)) {
                    createFileComponent(key, defaultContext);
                } else if (key.equals(EConnectionParameterName.DIRECTORY)) {
                    createDirectoryComponent(key, defaultContext);
                } else {
                    createTextComponent(key, defaultContext);
                }
            }
        }
    }

    /**
     * qiang.zhang Comment method "createDirectoryComponent".
     * 
     * @param key
     * @param defaultContext
     */
    private void createDirectoryComponent(EConnectionParameterName key, IContext defaultContext) {
        GridLayout gridLayout;
        GridData gridData;
        Composite hostComposite = new Composite(mainComposite, SWT.NONE);
        gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 2;
        gridLayout.marginTop = 5;
        gridLayout.marginBottom = 2;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        hostComposite.setLayout(gridLayout);
        hostComposite.setLayoutData(gridData);
        Label hostLabel = new Label(hostComposite, SWT.NONE);
        hostLabel.setText(key.getDisplayName() + ":");
        GridDataFactory.swtDefaults().hint(LABEL_DEFAULT_X, DEFAULT_HEIGHT).applyTo(hostLabel);

        Composite fileComposite = new Composite(hostComposite, SWT.NONE);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        fileComposite.setLayout(gridLayout);
        fileComposite.setLayoutData(gridData);

        final Text host = new Text(fileComposite, SWT.BORDER);
        host.setText(pvValues.get(key));
        if (host.getText().trim().length() == 0) {
            host.setBackground(ColorConstants.red);
            host.redraw();
        }
        // GridDataFactory.swtDefaults().hint(TEXT_DEFAULT_X, DEFAULT_HEIGHT).applyTo(host);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        host.setLayoutData(gridData);
        Button button = new Button(fileComposite, SWT.PUSH);
        button.setText("...");
        button.addSelectionListener(new SelectionListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetDefaultSelected(SelectionEvent e) {

            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(mainComposite.getShell(), SWT.NONE);
                String path = PathExtractor.extractPath(host.getText());
                dialog.setFilterPath(PathUtils.getOSPath(path));
                String file = dialog.open();
                if (file != null) {
                    if (!file.equals("")) {
                        host.setText(PathUtils.getPortablePath(file));
                    }
                }
            }

        });

        final Text hostText = new Text(hostComposite, SWT.NONE);
        hostText.setEditable(false);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        hostText.setLayoutData(gridData);
        hostText.setText(CONTEXT_WITH + ContextParameterUtils.parseScriptContextCode(host.getText(), defaultContext));
        host.setData(TEXT_CONTROL, hostText);
        host.setData(key);
        allParamText.add(host);
        host.addModifyListener(new ModifyListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
             */
            public void modifyText(ModifyEvent e) {
                if (host.getText().trim().length() == 0) {
                    host.setBackground(ColorConstants.red);
                    host.redraw();
                } else {
                    host.setBackground(ColorConstants.white);
                    host.redraw();
                }
                resetValues(host, hostText);

            }
        });
    }

    /**
     * qiang.zhang Comment method "createFileComponent".
     * 
     * @param key
     * @param defaultContext
     */
    private void createFileComponent(EConnectionParameterName key, IContext defaultContext) {
        GridLayout gridLayout;
        GridData gridData;
        Composite hostComposite = new Composite(mainComposite, SWT.NONE);
        gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 2;
        gridLayout.marginTop = 5;
        gridLayout.marginBottom = 2;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        hostComposite.setLayout(gridLayout);
        hostComposite.setLayoutData(gridData);
        Label hostLabel = new Label(hostComposite, SWT.NONE);
        hostLabel.setText(key.getDisplayName() + ":");
        GridDataFactory.swtDefaults().hint(LABEL_DEFAULT_X, DEFAULT_HEIGHT).applyTo(hostLabel);

        Composite fileComposite = new Composite(hostComposite, SWT.NONE);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        fileComposite.setLayout(gridLayout);
        fileComposite.setLayoutData(gridData);

        final Text host = new Text(fileComposite, SWT.BORDER);
        host.setText(pvValues.get(key));
        if (host.getText().trim().length() == 0) {
            host.setBackground(ColorConstants.red);
            host.redraw();
        }
        // GridDataFactory.swtDefaults().hint(TEXT_DEFAULT_X, DEFAULT_HEIGHT).applyTo(host);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        host.setLayoutData(gridData);
        Button button = new Button(fileComposite, SWT.PUSH);
        button.setText("...");
        button.addSelectionListener(new SelectionListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetDefaultSelected(SelectionEvent e) {

            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(mainComposite.getShell(), SWT.NONE);
                String path = PathExtractor.extractPath(host.getText());
                dialog.setFileName(PathUtils.getOSPath(path));
                String file = dialog.open();
                if (file != null) {
                    if (!file.equals("")) {
                        host.setText(PathUtils.getPortablePath(file));
                    }
                }
            }

        });

        final Text hostText = new Text(hostComposite, SWT.NONE);
        hostText.setEditable(false);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        hostText.setLayoutData(gridData);
        hostText.setText(CONTEXT_WITH + ContextParameterUtils.parseScriptContextCode(host.getText(), defaultContext));
        host.setData(TEXT_CONTROL, hostText);
        host.setData(key);
        allParamText.add(host);
        host.addModifyListener(new ModifyListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
             */
            public void modifyText(ModifyEvent e) {
                if (host.getText().trim().length() == 0) {
                    host.setBackground(ColorConstants.red);
                    host.redraw();
                } else {
                    host.setBackground(ColorConstants.white);
                    host.redraw();
                }
                resetValues(host, hostText);

            }
        });
    }

    private void createTextComponent(EConnectionParameterName key, IContext defaultContext) {
        GridLayout gridLayout;
        GridData gridData;
        Composite hostComposite = new Composite(mainComposite, SWT.NONE);
        gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 2;
        gridLayout.marginTop = 5;
        gridLayout.marginBottom = 2;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        hostComposite.setLayout(gridLayout);
        hostComposite.setLayoutData(gridData);
        Label hostLabel = new Label(hostComposite, SWT.NONE);
        hostLabel.setText(key.getDisplayName() + ":");
        GridDataFactory.swtDefaults().hint(LABEL_DEFAULT_X, DEFAULT_HEIGHT).applyTo(hostLabel);
        final Text host = new Text(hostComposite, SWT.BORDER);
        host.setText(pvValues.get(key));
        if (host.getText().trim().length() == 0) {
            host.setBackground(ColorConstants.red);
            host.redraw();
        }
        // GridDataFactory.swtDefaults().hint(TEXT_DEFAULT_X, DEFAULT_HEIGHT).applyTo(host);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        host.setLayoutData(gridData);
        final Text hostText = new Text(hostComposite, SWT.NONE);
        hostText.setEditable(false);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        hostText.setLayoutData(gridData);
        hostText.setText(CONTEXT_WITH + ContextParameterUtils.parseScriptContextCode(host.getText(), defaultContext));
        host.setData(TEXT_CONTROL, hostText);
        host.setData(key);
        allParamText.add(host);
        host.addKeyListener(new KeyAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
             */
            @Override
            public void keyReleased(KeyEvent e) {
                if (host.getText().trim().length() == 0) {
                    host.setBackground(ColorConstants.red);
                    host.redraw();
                } else {
                    host.setBackground(ColorConstants.white);
                    host.redraw();
                }
                resetValues(host, hostText);
            }
        });
    }

    /**
     * qzhang Comment method "createContextComposite".
     * 
     * @return
     */
    private IContext createContextComposite() {
        Composite contextComposite = new Composite(mainComposite, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        contextComposite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalAlignment = GridData.END;
        contextComposite.setLayoutData(gridData);
        Label contextLabel = new Label(contextComposite, SWT.NONE);
        contextLabel.setText(Messages.getString("ConfigureConnParamDialog.ContextLabel")); //$NON-NLS-1$
        contextCombo = new Combo(contextComposite, SWT.PUSH);
        GridDataFactory.swtDefaults().hint(LABEL_DEFAULT_X, DEFAULT_HEIGHT).applyTo(contextCombo);
        contextCombo.setItems(getContextNames());
        contextCombo.select(0);
        IContext defaultContext = contextManager.getDefaultContext();
        return defaultContext;
    }

    /**
     * qzhang Comment method "getContextNames".
     * 
     * @return
     */
    private String[] getContextNames() {
        List<String> items = new ArrayList<String>();
        if (contextManager != null) {
            final List<IContext> listContext = contextManager.getListContext();
            if (listContext.size() > 0) {
                items.add(contextManager.getDefaultContext().getName());
                for (int i = 0; i < listContext.size(); i++) {
                    IContext context = listContext.get(i);
                    if (!context.getName().equals(items.get(0))) {
                        items.add(context.getName());
                    }
                }
            }
        }
        return items.toArray(new String[] {});
    }

    /*
     * (non-Java)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        for (Text text : allParamText) {
            EConnectionParameterName name = (EConnectionParameterName) text.getData();
            String value = ((Text) text.getData(TEXT_CONTROL)).getText().substring(CONTEXT_WITH.length());
            switch (name) {
            case SID:
                parameters.setDbName(value);
                break;
            case DATASOURCE:
                parameters.setDatasource(value);
                break;
            case PASSWORD:
                parameters.setPassword(value);
                break;
            case PORT:
                parameters.setPort(value);
                break;
            case SCHEMA:
                parameters.setSchema(value);
                break;
            case SERVER_NAME:
                parameters.setHost(value);
                break;
            case USERNAME:
                parameters.setUserName(value);
                break;
            case DIRECTORY:
                parameters.setDirectory(value);
                break;
            case FILE:
                parameters.setFilename(value);
                break;
            default:

            }
        }
        super.okPressed();
    }

    /**
     * qzhang Comment method "resetValues".
     * 
     * @param host
     */
    private void resetValues(final Text key, final Text value) {
        final IContext context = contextManager.getContext(contextCombo.getItem(contextCombo.getSelectionIndex()));
        value.setText(CONTEXT_WITH + ContextParameterUtils.parseScriptContextCode(key.getText(), context));
    }

}
