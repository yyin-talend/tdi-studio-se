// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.rowgenerator.shadow;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.designer.rowgenerator.RowGeneratorComponent;
import org.talend.designer.rowgenerator.RowGeneratorPlugin;
import org.talend.designer.rowgenerator.data.FunctionManager;
import org.talend.designer.rowgenerator.i18n.Messages;
import org.talend.designer.rowgenerator.managers.UIManager;
import org.talend.designer.rowgenerator.ui.editor.MetadataColumnExt;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorUtilities;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: RowGenPreivewCodeMain.java,v 1.3 2007/02/02 07:59:31 pub Exp $
 * 
 */
public class RowGenPreviewCodeMain {

    public static final String PREVIEW = "Preview"; //$NON-NLS-1$

    private boolean running;

    private boolean killing;

    private RowGeneratorComponent component;

    private List<List<String>> results;

    private Process process;

    // private ECodeLanguage language;

    private RowGenProcess proc;

    private String number;

    /**
     * qzhang RowGenPreivewCodeMain constructor comment.
     */
    public RowGenPreviewCodeMain(RowGeneratorComponent component) {
        this.component = component;
        results = new ArrayList<List<String>>();
    }

    private void getProcess() {
        initPerlArray();
        this.component.setNumber(number);
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setLabel(PREVIEW + "_RowGenerator2"); //$NON-NLS-1$
        property.setId(PREVIEW + "_RowGenerator2"); //$NON-NLS-1$
        proc = new RowGenProcess(property, component);
    }

    /**
     * qzhang Comment method "initPerlArray".
     */
    private void initPerlArray() {
        List<Map<String, Object>> map = this.component.getMapList();
        MetadataTable table = (MetadataTable) this.component.getMetadataList().get(0);
        if (map == null || map.isEmpty()) {
            map = new ArrayList<Map<String, Object>>();
            for (IMetadataColumn col : table.getListColumns()) {
                MetadataColumnExt ext = (MetadataColumnExt) col;
                Map<String, Object> value = new HashMap<String, Object>();
                value.put(RowGeneratorComponent.COLUMN_NAME, ext.getLabel());
                value.put(RowGeneratorComponent.ARRAY, FunctionManager.getOneColData(ext));
                map.add(value);
            }
            this.component.setTableElementParameter(map);
        } else {
            for (IMetadataColumn col : table.getListColumns()) {
                MetadataColumnExt ext = (MetadataColumnExt) col;
                this.component.setColumnValue(ext.getLabel(), FunctionManager.getOneColData(ext));
            }
        }
    }

    /**
     * qzhang Comment method "runPreviewCode".
     */
    protected Process runPreviewCode() {
        getProcess();
        IContext context2 = new org.talend.core.model.context.JobContext(PREVIEW);
        if (UIManager.isJavaProject()) {
            List<IContextParameter> params = new ArrayList<IContextParameter>();
            JobContextParameter contextParameter = new JobContextParameter();
            contextParameter.setName(PREVIEW);
            contextParameter.setValue(PREVIEW);
            contextParameter.setType("String");
            params.add(contextParameter);
            context2.setContextParameterList(params);
        }
        IProcessor processor = ProcessorUtilities.getProcessor(proc, context2);
        try {
            return processor.run(IProcessor.NO_STATISTICS, IProcessor.NO_TRACES, null);
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return null;
        }
    }

    public List<List<String>> getResultsByRun(final Button refresh, String number) {
        this.number = number;
        results.clear();
        IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
        try {
            progressService.runInUI(PlatformUI.getWorkbench().getProgressService(), new IRunnableWithProgress() {

                public void run(final IProgressMonitor monitor) {
                    monitor.beginTask(Messages.getString("RowGenPreivewCodeMain.Process.Generate"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                    try {
                        try {
                            process = runPreviewCode();
                            if (process == null) {
                                return;
                            }
                            // process = runTempPerlCode();
                            Thread.sleep(1000);
                            InputStream is = process.getInputStream();
                            int len = is.available();
                            if (len > 0) {
                                byte[] data = new byte[len];
                                is.read(data);
                                convert(new String(data));
                            }
                            InputStream es = process.getErrorStream();
                            len = es.available();
                            if (len > 0) {
                                byte[] data = new byte[len];
                                is.read(data);
                                String mainMsg = Messages.getString("RowGenPreivewCodeMain.PerlRun.Error"); //$NON-NLS-1$
                                new ErrorDialogWidthDetailArea(Display.getCurrent().getActiveShell(),
                                        RowGeneratorPlugin.PLUGIN_ID, mainMsg, Messages
                                                .getString("RowGenPreivewCodeMain.Run.ErrorInfo")
                                                + "\n" + new String(data));
                            }
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                            kill();
                        }
                    } finally {
                        monitor.done();
                        refresh.setText(Messages.getString("RowGenPreivewCodeMain.PreviewBtn.Text")); //$NON-NLS-1$
                    }
                }
            }, null);

        } catch (Exception ex) {
            ExceptionHandler.process(ex);
        } finally {
            proc.reconnection();
        }
        return results;
    }

    /**
     * qzhang Comment method "convert".
     * 
     * @param string
     * @return
     */
    protected void convert(String string) {
        if (UIManager.isJavaProject()) {
            string = string.substring(string.indexOf("\n") + 1);
        }
        string = string.replaceAll("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
        String[] rows = string.split("\n"); //$NON-NLS-1$
        for (int i = 0; i < rows.length; i++) {
            char[] cs = rows[i].toCharArray();
            List<String> cols = new ArrayList<String>();
            cols.add("" + (i + 1)); //$NON-NLS-1$
            StringBuffer col = new StringBuffer();
            for (int j = 0; j < cs.length; j++) {
                if (cs[j] == '|') {
                    cols.add(col.toString());
                    col = new StringBuffer();
                } else {
                    col.append(cs[j]);
                }
            }
            cols.add(col.toString());
            results.add(cols);
        }
    }

    public int kill() {
        int exitCode;
        if (!killing && isRunning()) {
            killing = true;
        }
        if (!killing && isRunning()) {
            killing = true;
            try {
                exitCode = killProcess();
            } finally {
                killing = false;
            }
        } else {
            exitCode = 0;
        }

        setRunning(false);
        return exitCode;
    }

    /**
     * qzhang Comment method "killProcess".
     * 
     * @return
     */
    private int killProcess() {
        int exitCode = 0;
        if (process != null) {
            process.destroy();
            try {
                exitCode = process.exitValue();
            } catch (IllegalThreadStateException itse) {
                // Can be throw on some UNIX system :(
                // but the process is really killed.
            }
            process = null;
        }
        return exitCode;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        if (this.running != running) {
            this.running = running;
        }
    }
}
