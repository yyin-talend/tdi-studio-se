package org.talend.designer.dataprep.component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.job.JobResource;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.editor.CustomExternalActions;
import org.talend.designer.component.preview.PreviewSocketServer;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.dataprep.component.preview.DataprepPreviewComponentDataProcess;
import org.talend.designer.dataprep.component.preview.ViewDataprepDataMain;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorUtilities;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;
import us.monoid.web.TextResource;

public class TDataprepViewProfileAction extends CustomExternalActions {

    private Node graphicalNode;

    private PreviewSocketServer previewServer;

    protected List<String[]> listData = new ArrayList<String[]>();

    protected String[] colNames;

    public static final int DEFAULT_LIMIT = 1000;

    protected int limit = DEFAULT_LIMIT;

    private String idPreparation;

    private String jobName;

    private String contextName;

    private String projectName;

    public TDataprepViewProfileAction() {

    }

    @Override
    public int getComponentType() {
        return INPUT;
    }

    @Override
    protected boolean calculateEnabled() {
        Node node = null;
        List<?> selectedObjects = getSelectedObjects();
        if (selectedObjects == null || selectedObjects.size() == 0) {
            return false;
        }
        Object object = selectedObjects.get(0);
        if (object == null) {
            return false;
        }
        if (object instanceof NodePart) {
            NodePart nodePart = (NodePart) object;
            node = (Node) nodePart.getModel();
        }

        if (!isEnableForCurrentPaletteType(node)) {
            return false;
        }

        if (node != null && node.getComponent().getName().equals(getAdaptComponectName())) {
            return true;
        }
        return false;
    }

    private Object getAdaptComponectName() {
        return "tDataprepRun";
    }

    private boolean isEnableForCurrentPaletteType(Node node) {
        if (node == null) {
            return false;
        }
        if (node.getComponent().getPaletteType() == null) {
            return false;
        }
        if (node.getComponent().getPaletteType().equalsIgnoreCase(ComponentCategory.CATEGORY_4_MAPREDUCE.getName())) {
            return false;
        }
        return true;
    }

    public static boolean isJavaProject() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        return (codeLanguage == ECodeLanguage.JAVA);
    }

    public List<String[]> getListData() {
        return listData;
    }

    public String[] getColNames() {
        return colNames;
    }

    public String getIdPreparation() {
        return idPreparation;
    }

    @Override
    public void run() {

        if (graphicalNode == null) {
            NodePart object = (NodePart) getSelectedObjects().get(0);
            graphicalNode = (Node) object.getModel();
            colNames = new String[graphicalNode.getInputs().get(0).getMetadataTable().getListColumns().size()];
            // get columns names
            int i = 0;
            for (IMetadataColumn iMetadataColumn : graphicalNode.getInputs().get(0).getMetadataTable().getListColumns()) {
                IMetadataColumn columns = iMetadataColumn;
                colNames[i++] = columns.getLabel();
            }
            // get the limit
            setLimits(Integer.parseInt((String) graphicalNode.getPropertyValue("LIMIT_PREVIEW")));
        }

        JobResource te = ((TalendEditor) getWorkbenchPart()).getCurrentJobResource();
        projectName = te.getProjectName();
        contextName = te.getJobInfo().getContextName();
        jobName = te.getJobInfo().getJobName() + "_" + graphicalNode.getUniqueName() + "_" + te.getJobInfo().getJobVersion();

        if (graphicalNode.getIncomingConnections().size() == 0) {
            MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", //$NON-NLS-1$
                    // **Messages.getString("ViewBlockProfileDataDialog.PreviewUnavailable")**//
                    "error"); //$NON-NLS-1$
            return;
        }
        makeFakeNodeAndLoad(graphicalNode);
        processAndOpenPreviewDialog(graphicalNode);

    }

    public void makeFakeNodeAndLoad(final Node node) {
        final ProgressMonitorDialog pmd = new ProgressMonitorDialog(Display.getDefault().getActiveShell()) {

            // stop process and scoket server
            @Override
            protected void cancelPressed() {
                if (previewServer != null) {
                    previewServer.cancel();
                }
                super.cancelPressed();
            }
        };
        try {
            pmd.run(true, false, new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask(getTaskName(), IProgressMonitor.UNKNOWN);

                    // process data preview
                    Process previewProcess = null;
                    previewProcess = runPreviewDataColumnProcess(node);

                    pmd.setCancelable(true);

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

                        String url = ((String) graphicalNode.getPropertyValue("URL")).replace("\"", "");
                        String tempIdPreparation = ((String) graphicalNode.getPropertyValue("PREPARATION_ID")).replace("\"", "");
                        Resty r = new Resty();
                        Boolean createDataset = true;

                        if (tempIdPreparation != null && !tempIdPreparation.isEmpty()) {
                            try {
                                String dataSetId = r.json(url + ":8888/api/preparations/" + tempIdPreparation + "/details")
                                        .object().getString("dataSetId");
                                r.text(url + ":8888/api/datasets/" + dataSetId + "?name=" + getDatasetName(),
                                        Resty.put(Resty.content(listDataToString())));
                                createDataset = false;
                            } catch (JSONException e) {
                                createDataset = true;
                            }
                        }
                        if (createDataset) {
                            TextResource resource = r.text(url + ":8888/api/datasets?name=" + getDatasetName(),
                                    Resty.content(listDataToString()));
                            JSONObject js = new JSONObject();
                            js.accumulate("dataSetId", resource.toString());
                            js.accumulate("author", graphicalNode.getPropertyValue("USERNAME"));
                            js.accumulate("name", getDatasetName());
                            String preparationId = "\"" + r.text(url + ":8888/api/preparations", Resty.content(js)).toString()
                                    + "\"";
                            graphicalNode.setPropertyValue("PREPARATION_ID", preparationId);

                        }

                        // TODO: DO NOT DO THAT (ask to nico for proper implementation)
                        ((TalendEditor) getWorkbenchPart()).setDirty(true);

                    } catch (IOException e) {
                        ExceptionHandler.process(e);
                    } catch (JSONException e) {
                        ExceptionHandler.process(e);
                    }

                    if (listData == null && !StringUtils.isEmpty(dataPreviewErrorOutput.toString())) {
                        Display.getDefault().syncExec(new Runnable() {

                            @Override
                            public void run() {
                                MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", //$NON-NLS-1$
                                        dataPreviewErrorOutput.toString());
                            }

                        });
                    }

                    monitor.done();
                }

                private String getTaskName() {
                    return "en attente ..."; //Messages.getString("ViewBlockProfileDataDialog.viewBlockProfile"); //$NON-NLS-1$
                }
            });
        } catch (Exception e1) {
            ExceptionHandler.process(e1);
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
        if (isJavaProject()) {
            List<IContextParameter> params = new ArrayList<IContextParameter>();
            JobContextParameter contextParameter = new JobContextParameter();
            contextParameter.setContext(mockContext);
            contextParameter.setName("Default"); //$NON-NLS-1$
            contextParameter.setValue(ViewDataprepDataMain.PREVIEW);
            contextParameter.setType("String"); //$NON-NLS-1$
            params.add(contextParameter);
            mockContext.setContextParameterList(params);
        }

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

    // ADD yyi 2011-10-13 monitor
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

    private void processAndOpenPreviewDialog(final Node node) {

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

    public void setLimits(int limits) {
        this.limit = limits;
    }

    private String listDataToString() {
        StringBuilder data = new StringBuilder();

        // Add headers
        for (int i = 0; i < colNames.length; i++) {
            data.append(colNames[i]);
            if (colNames.length > i + 1) {
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

    private String getDatasetName() {
        return this.projectName + "_" + this.jobName + "_" + this.contextName;
    }
}
