// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dataprep.component.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.TextControlCreator;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.ui.properties.tab.IDynamicProperty;
import org.talend.designer.component.preview.PreviewSocketServer;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.controllers.TextController;
import org.talend.designer.dataprep.component.controller.dialog.ChooseExsitingIdDialog;
import org.talend.designer.dataprep.component.controller.dialog.ExistingIdObject;
import org.talend.designer.dataprep.component.preview.DataprepPreviewComponentDataProcess;
import org.talend.designer.dataprep.component.preview.ViewDataprepDataMain;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.designer.runprocess.RunProcessContext;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.repository.ProjectManager;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;
import us.monoid.web.TextResource;

/**
 * created by wchen on 2015年9月17日 Detailled comment
 *
 */
public class PreperationIdController extends TextController {

    private static final String USE_EXSTING_ID = "USE_EXSTING_ID"; //$NON-NLS-1$

    private static final String CREATE_NEW_ID = "CREATE_NEW_ID"; //$NON-NLS-1$

    private static final int VSPACE = 4;

    private PreviewSocketServer previewServer;

    /**
     * DOC wchen PreperationIdController constructor comment.
     * 
     * @param dp
     */
    public PreperationIdController(IDynamicProperty dp) {
        super(dp);
    }

    @Override
    public Control createControl(Composite subComposite, IElementParameter param, int numInRow, int nbInRow, int top,
            Control lastControl) {
        Control lastControlUsed = super.createControl(subComposite, param, numInRow, nbInRow, top, lastControl);
        FormData data = (FormData) lastControlUsed.getLayoutData();
        data.right = new FormAttachment((numInRow * MAX_PERCENT) / nbInRow, -(STANDARD_BUTTON_WIDTH + VSPACE) * 2);
        lastControlUsed = addButtons(subComposite, param, lastControlUsed, numInRow, nbInRow, top);
        return lastControlUsed;
    }

    private Control addButtons(Composite subComposite, IElementParameter param, Control lastControl, int numInRow, int nbInRow,
            int top) {
        Control lastControlUsed = lastControl;

        Button existIdBtn = getWidgetFactory().createButton(subComposite, "", SWT.PUSH); //$NON-NLS-1$
        Point existIdBtnSize = existIdBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        existIdBtnSize = existIdBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        existIdBtn.setImage(ImageProvider.getImage(EImage.THREE_DOTS_ICON));
        existIdBtn.addSelectionListener(listenerSelection);
        existIdBtn.setData(NAME, USE_EXSTING_ID);
        existIdBtn.setData(PARAMETER_NAME, param.getName());
        existIdBtn.setToolTipText("Choose an exsiting preperation id");

        Button newIdBtn = getWidgetFactory().createButton(subComposite, "", SWT.PUSH); //$NON-NLS-1$
        Point newIdBtnSize = newIdBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        newIdBtnSize = newIdBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        newIdBtn.setImage(ImageProvider.getImage(EImage.ADD_ICON));
        newIdBtn.addSelectionListener(listenerSelection);
        newIdBtn.setData(NAME, CREATE_NEW_ID);
        newIdBtn.setData(PARAMETER_NAME, param.getName());
        newIdBtn.setToolTipText("Create a new preperation id");

        FormData data = new FormData();
        data.left = new FormAttachment(lastControl, 0);
        data.right = new FormAttachment(lastControl, STANDARD_BUTTON_WIDTH, SWT.RIGHT);
        data.top = new FormAttachment(0, top);
        data.height = STANDARD_HEIGHT;
        existIdBtn.setLayoutData(data);

        data = new FormData();
        data.left = new FormAttachment(existIdBtn, VSPACE);
        data.right = new FormAttachment(existIdBtn, STANDARD_BUTTON_WIDTH + VSPACE, SWT.RIGHT);
        data.top = new FormAttachment(0, top);
        data.height = STANDARD_HEIGHT;
        newIdBtn.setLayoutData(data);

        int size = Math.max(existIdBtnSize.y, newIdBtnSize.y);
        dynamicProperty.setCurRowSize(size + VSPACE);
        lastControlUsed = newIdBtn;
        return lastControlUsed;
    }

    SelectionListener listenerSelection = new SelectionListener() {

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            // do nothing.
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            createAndExecuteCommand(e);
        }
    };

    private void createAndExecuteCommand(SelectionEvent selectionEvent) {
        if (selectionEvent.getSource() instanceof Button) {
            if (USE_EXSTING_ID.equals(((Button) selectionEvent.getSource()).getData(NAME))) {
                createUseExsitIdCommand(((Button) selectionEvent.getSource()).getShell());
            } else {
                createNewIdCommand();
            }
        }
    }

    private void createUseExsitIdCommand(Shell parentShell) {
        final List<ExistingIdObject> existingIds = getExistingPreperationIds(parentShell);

        ChooseExsitingIdDialog dialog = new ChooseExsitingIdDialog(parentShell, existingIds);
        if (dialog.open() == Window.OK) {
            String selectedId = TalendTextUtils.addQuotes(dialog.getSelectedId());
            elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));
            executeCommand(new PropertyChangeCommand(elem, "PREPARATION_ID", selectedId));//$NON-NLS-1$
        }

    }

    private List<ExistingIdObject> getExistingPreperationIds(Shell parentShell) {
        final List<ExistingIdObject> existingIds = new ArrayList<ExistingIdObject>();
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(parentShell);
        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                IElementParameter urlParam = elem.getElementParameter("URL"); //$NON-NLS-1$
                if (urlParam != null && urlParam.getValue() != null) {
                    Resty resty = new Resty();
                    try {
                        final JSONResource resource = resty.json(TalendTextUtils.removeQuotes(urlParam.getValue().toString())
                                + ":8888/api/preparations");
                        if (resource != null) {
                            final JSONArray jsonArray = resource.array();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ExistingIdObject idObject = new ExistingIdObject();
                                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                                idObject.setId(jsonObject.getString("id"));
                                idObject.setName(jsonObject.getString("name"));
                                idObject.setAuthor(jsonObject.getString("author"));
                                idObject.setLastModificationDate(jsonObject.getString("lastModificationDate"));
                                existingIds.add(idObject);
                            }
                        }
                    } catch (IOException e) {
                        ExceptionHandler.process(e);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                } else {
                    throw new RuntimeException("the dataprep component url parameter is not set");
                }
            }
        };

        try {
            dialog.run(false, true, runnable);
        } catch (InvocationTargetException e) {
            ExceptionHandler.process(e);
        } catch (InterruptedException e) {
            ExceptionHandler.process(e);
        }
        return existingIds;

    }

    private void createNewIdCommand() {
        final ProgressMonitorDialog pmd = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
        try {
            pmd.run(true, false, new CreateNewPreperationRunnable((Node) elem));
        } catch (Exception e1) {
            ExceptionHandler.process(e1);
        }
    }

    @Override
    public int estimateRowSize(Composite subComposite, IElementParameter param) {
        if (!estimateInitialized) {
            final DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, new TextControlCreator());
            Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
            dField.getLayoutControl().dispose();

            Button btn = getWidgetFactory().createButton(subComposite, "", SWT.PUSH); //$NON-NLS-1$
            int buttonSize = btn.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
            btn.dispose();
            rowSize = Math.max(initialSize.y, buttonSize * 2) + VSPACE;
            estimateInitialized = true;

        }
        return rowSize;
    }

    class CreateNewPreperationRunnable implements IRunnableWithProgress {

        private Node node;

        protected List<String[]> listData = new ArrayList<String[]>();

        protected int limit = 1000;

        public CreateNewPreperationRunnable(Node node) {
            this.node = node;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
         */
        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
            monitor.beginTask("create new preperation...", IProgressMonitor.UNKNOWN);
            // process data preview
            Process previewProcess = null;
            previewProcess = runPreviewDataColumnProcess(node);

            final StringBuilder dataPreviewErrorOutput = new StringBuilder();
            try {
                waitForProcessWithMonitor(dataPreviewErrorOutput, previewProcess, previewServer, monitor);
            } catch (InterruptedException ex) {
                ExceptionHandler.process(ex);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
            listData = previewServer.getRows();

            try {
                String url = ((String) node.getPropertyValue("URL")).replace("\"", "");
                Resty r = new Resty();
                TextResource resource = r.text(url + ":8888/api/datasets?name=" + getDatasetName(),
                        Resty.content(listDataToString()));
                JSONObject js = new JSONObject();
                js.accumulate("dataSetId", resource.toString());
                js.accumulate("author", node.getPropertyValue("USERNAME"));
                js.accumulate("name", getDatasetName());
                String preparationId = "\"" + r.text(url + ":8888/api/preparations", Resty.content(js)).toString() + "\"";
                node.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));
                executeCommand(new PropertyChangeCommand(elem, "PREPARATION_ID", preparationId));//$NON-NLS-1$
                // open the web page after new preperation created
                processAndOpenPreview(node);

            } catch (IOException e) {
                ExceptionHandler.process(e);
            } catch (JSONException e) {
                ExceptionHandler.process(e);
            }
        }

        public Process runPreviewDataColumnProcess(Node node) {
            ViewDataprepDataMain dataMain = new ViewDataprepDataMain(node, node.getProcess());
            Property property = PropertiesFactory.eINSTANCE.createProperty();
            property.setLabel(ViewDataprepDataMain.PREVIEW + "_" + "Data"); //$NON-NLS-1$ //$NON-NLS-2$
            property.setId(ViewDataprepDataMain.PREVIEW + "_" + "Data"); //$NON-NLS-1$ //$NON-NLS-2$

            // Get a virtual node (by copying the metadata and parameters)
            DataprepPreviewComponentDataProcess proc = new DataprepPreviewComponentDataProcess(property, dataMain.getDataNode(),
                    node.getProcess());

            proc.initNodesAndConnections(limit);
            return runPreviewProcessHelper(proc, dataMain);
        }

        protected Process runPreviewProcessHelper(DataprepPreviewComponentDataProcess proc, ViewDataprepDataMain dataMain) {
            IContext mockContext = new org.talend.core.model.context.JobContext(ViewDataprepDataMain.PREVIEW);
            List<IContextParameter> params = new ArrayList<IContextParameter>();
            JobContextParameter contextParameter = new JobContextParameter();
            contextParameter.setContext(mockContext);
            contextParameter.setName("Default"); //$NON-NLS-1$
            contextParameter.setValue(ViewDataprepDataMain.PREVIEW);
            contextParameter.setType("String"); //$NON-NLS-1$
            params.add(contextParameter);
            mockContext.setContextParameterList(params);

            IContext context = dataMain.getPreviewContext() == null ? mockContext : dataMain.getPreviewContext();
            // IContext context = mockContext ;
            IProcessor processor = ProcessorUtilities.getProcessor(proc, proc.getProperty(), context);

            try {
                previewServer = new PreviewSocketServer();
                previewServer.setNeedTimeout(false);
                previewServer.setEcnoding(fetchNodeEncoding(dataMain));
                previewServer.start();
                return processor.run(IProcessor.NO_STATISTICS, IProcessor.NO_TRACES, null);
            } catch (Exception e) {
                ExceptionHandler.process(e);
                return null;
            }
        }

        private String fetchNodeEncoding(ViewDataprepDataMain dataMain) {
            if (dataMain.getDataNode() == null) {
                return null;
            }
            IElementParameter encodingParam = dataMain.getDataNode().getElementParameter("ENCODING"); //$NON-NLS-1$
            if (encodingParam != null) {
                String encoding = (String) encodingParam.getValue();
                if (dataMain.getPreviewContext() != null && ContextParameterUtils.isContainContextParam(encoding)) {
                    String var = ContextParameterUtils.getVariableFromCode(encoding);
                    if (var != null) {
                        IContextParameter param = null;
                        for (IContextParameter contextParam : dataMain.getPreviewContext().getContextParameterList()) {
                            if (contextParam.getName().equals(var)) {
                                param = contextParam;
                                break;
                            }
                        }
                        if (param != null) {
                            String value2 = param.getValue();
                            if (value2 != null) {
                                return value2;
                            }
                        }
                        return ""; //$NON-NLS-1$
                    }
                }
                return encoding;
            }
            return null;
        }

        private void waitForProcessWithMonitor(final StringBuilder dataPreviewErrorOutput, Process previewProcess,
                PreviewSocketServer previewSocketServer, IProgressMonitor monitor) throws InterruptedException, IOException {
            long beginMillis = System.currentTimeMillis();
            double cost;
            while (true) {

                Thread.sleep(10);
                try {
                    cost = (double) (System.currentTimeMillis() - beginMillis) / 1000;
                    monitor.subTask(String.format("%s rows in %.2fs", previewSocketServer.getNbLine(), cost)); //$NON-NLS-1$
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!previewSocketServer.isDonetSocketInput()) {
                    if (previewProcess.getErrorStream() != null && previewProcess.getErrorStream().available() > 0) {
                        readErrorStream(dataPreviewErrorOutput, previewProcess);
                    }
                    break;
                }
                if (previewProcess.getErrorStream() != null && previewProcess.getErrorStream().available() > 0) {
                    readErrorStream(dataPreviewErrorOutput, previewProcess);
                    break;
                }
            }
        }

        private void readErrorStream(final StringBuilder dataPreviewErrorOutput, Process previewProcess) throws IOException {
            int read = 0;
            byte[] bytes = new byte[8192];
            BufferedInputStream stream = new BufferedInputStream(previewProcess.getErrorStream(), 8192);
            while (read >= 0) {
                read = stream.read(bytes);
                if (read > 0) {
                    dataPreviewErrorOutput.append(new String(bytes, 0, read));
                }
            }
        }

        private void processAndOpenPreview(Node node) {
            String urlParam = (String) node.getPropertyValue("URL"); //$NON-NLS-1$
            String preparationId = (String) node.getPropertyValue("PREPARATION_ID"); //$NON-NLS-1$

            if (urlParam != null && preparationId != null) {
                String urlValue = urlParam;
                // remove the extra "
                urlValue = urlValue.replace("\"", "") + "/#/home/preparations?prepid=" + preparationId.replace("\"", ""); //$NON-NLS-1$//$NON-NLS-2$
                try {
                    PlatformUI.getWorkbench().getBrowserSupport().createBrowser("dataprep").openURL(new URL(urlValue)); //$NON-NLS-1$
                } catch (PartInitException | MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("the dataprep component url parameter is not set");
            }
        }

        private String getDatasetName() {
            String projectName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
            RunProcessContext activeContext = RunProcessPlugin.getDefault().getRunProcessContextManager().getActiveContext();
            String contextName = activeContext.getSelectedContext().getName();

            String jobName = node.getProcess().getName() + "_" + node.getUniqueName() + "_" + node.getProcess().getVersion();
            return projectName + "_" + jobName + "_" + contextName;
        }

        private String listDataToString() {
            StringBuilder data = new StringBuilder();

            // Add headers
            final List<IMetadataColumn> listColumns = node.getInputs().get(0).getMetadataTable().getListColumns();
            for (int i = 0; i < listColumns.size(); i++) {
                IMetadataColumn columns = listColumns.get(i);
                data.append(columns.getLabel());
                if (listColumns.size() > i + 1) {
                    data.append(";");
                }
            }

            data.append("\n");
            // Add data
            for (Iterator<String[]> iterator = listData.iterator(); iterator.hasNext();) {

                String[] strings = iterator.next();

                for (int i = 0; i < strings.length; i++) {
                    data.append(strings[i]);
                    if (strings.length > i + 1) {
                        data.append(";");
                    }
                }
                if (iterator.hasNext()) {
                    data.append("\n");
                }
            }
            return data.toString();
        }

    }

}
