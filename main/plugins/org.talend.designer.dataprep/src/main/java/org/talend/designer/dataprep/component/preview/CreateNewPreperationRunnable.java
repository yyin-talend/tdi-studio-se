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
package org.talend.designer.dataprep.component.preview;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.designer.component.preview.PreviewSocketServer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.designer.runprocess.RunProcessContext;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.repository.ProjectManager;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;
import us.monoid.web.TextResource;

/**
 * created by wchen on 2015年9月21日 Detailled comment
 *
 */
public class CreateNewPreperationRunnable implements IRunnableWithProgress {

    private Node node;

    private PreviewSocketServer previewServer;

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
            String tempIdPreparation = ((String) node.getPropertyValue("PREPARATION_ID")).replace("\"", "");
            Resty r = new Resty();
            Boolean createDataset = true;

            if (tempIdPreparation != null && !tempIdPreparation.isEmpty()) {
                try {
                    String dataSetId = r.json(url + ":8888/api/preparations/" + tempIdPreparation + "/details").object()
                            .getString("dataSetId");
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
                js.accumulate("author", node.getPropertyValue("USERNAME"));
                js.accumulate("name", getDatasetName());
                String preparationId = "\"" + r.text(url + ":8888/api/preparations", Resty.content(js)).toString() + "\"";
                node.setPropertyValue("PREPARATION_ID", preparationId);

            }

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
