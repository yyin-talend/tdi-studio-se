// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hsqldb.lib.StringUtil;
import org.talend.core.CorePlugin;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.designer.runprocess.JobInfo;

/**
 * DOC xye class global comment. Detailled comment
 */
public class JavaProcessUtil {

    public static Set<String> getNeededLibraries(final IProcess process, boolean withChildrens) {
        // see bug 4939: making tRunjobs work loop will cause a error of "out of memory"
        Set<ProcessItem> searchItems = new HashSet<ProcessItem>();
        ProcessItem processItem = null;
        if (process.getProperty().getVersion() != null) {
            processItem = ItemCacheManager.getProcessItem(process.getProperty().getId(), process.getProperty().getVersion());
        } else {
            processItem = ItemCacheManager.getProcessItem(process.getProperty().getId());
        }
        if (processItem != null) {
            searchItems.add(processItem);
        }
        return getNeededLibraries(process, withChildrens, searchItems);
    }

    private static Set<String> getNeededLibraries(final IProcess process, boolean withChildrens, Set<ProcessItem> searchItems) {
        Set<String> neededLibraries = new HashSet<String>();
        List<? extends INode> nodeList = process.getGeneratingNodes();
        for (INode node : nodeList) {
            List<ModuleNeeded> moduleList = node.getComponent().getModulesNeeded();
            for (ModuleNeeded needed : moduleList) {
                if (needed.isRequired()) {
                    neededLibraries.add(needed.getModuleName());
                }
            }
            for (IElementParameter curParam : node.getElementParameters()) {
                if (curParam.getField().equals(EParameterFieldType.MODULE_LIST)) {
                    if (!"".equals(curParam.getValue())) { // if the parameter //$NON-NLS-1$
                        // is not empty.
                        String moduleValue = (String) curParam.getValue();

                        if (ContextParameterUtils.isContainContextParam(moduleValue)) {
                            String var = ContextParameterUtils.getVariableFromCode(moduleValue);
                            if (var != null) {
                                IContext selectedContext = CorePlugin.getDefault().getRunProcessService().getSelectedContext();
                                if (selectedContext == null) {
                                    selectedContext = process.getContextManager().getDefaultContext();
                                }
                                IContextParameter param = selectedContext.getContextParameter(var);
                                if (param != null) {
                                    // add only the file name without path
                                    String paramvalue = param.getValue();
                                    int a = paramvalue.lastIndexOf("\\"); //$NON-NLS-1$
                                    String filename = paramvalue.substring(a + 1, paramvalue.length());
                                    neededLibraries.add(filename);
                                }
                            }
                        } else {
                            neededLibraries.add(moduleValue.replaceAll(TalendTextUtils.QUOTATION_MARK, "").replaceAll( //$NON-NLS-1$
                                    TalendTextUtils.SINGLE_QUOTE, "")); //$NON-NLS-1$
                        }
                    }
                }

                // see feature 4720 Add libraries for different version DB components and tMomInput components
                // IElementParameter elementParameter = node.getElementParameter("USE_EXISTING_CONNECTION");
                // if (elementParameter != null && elementParameter.isShow(node.getElementParameters())
                // && Boolean.TRUE.equals(elementParameter.getValue())) {
                if (curParam.isShow(node.getElementParameters())) {
                    findMoreLibraries(neededLibraries, curParam, true);
                } else {
                    findMoreLibraries(neededLibraries, curParam, false);
                }
            }

            if (withChildrens) {
                if (node.getComponent().getName().equals("tRunJob")) { //$NON-NLS-1$
                    IElementParameter processIdparam = node.getElementParameter("PROCESS_TYPE_PROCESS"); //$NON-NLS-1$
                    IElementParameter processVersionParam = node.getElementParameter(EParameterName.PROCESS_TYPE_VERSION
                            .getName());

                    ProcessItem processItem = null;
                    if (processVersionParam != null) {
                        processItem = ItemCacheManager.getProcessItem((String) processIdparam.getValue(),
                                (String) processVersionParam.getValue());
                    } else {
                        processItem = ItemCacheManager.getProcessItem((String) processIdparam.getValue());
                    }

                    String context = (String) node.getElementParameter("PROCESS_TYPE_CONTEXT").getValue(); //$NON-NLS-1$
                    if (processItem != null && !searchItems.contains(processItem)) {
                        // avoid dead loop of method call
                        searchItems.add(processItem);
                        JobInfo subJobInfo = new JobInfo(processItem, context);
                        // achen modify to fix 0006107
                        IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
                        IProcess child = service.getProcessFromItem(subJobInfo.getProcessItem());
                        // Process child = new Process(subJobInfo.getProcessItem().getProperty());
                        // child.loadXmlFile();
                        neededLibraries.addAll(JavaProcessUtil.getNeededLibraries(child, true, searchItems));
                    }
                }
            }
        }
        return neededLibraries;

    }

    /**
     * DOC YeXiaowei Comment method "findMoreLibraries".
     * 
     * @param neededLibraries
     * @param curParam
     */
    public static void findMoreLibraries(Set<String> neededLibraries, IElementParameter curParam, boolean flag) {

        if (curParam.getName().equals("DB_VERSION")) { //$NON-NLS-1$
            String jdbcName = (String) curParam.getValue();
            if (jdbcName != null) {
                if (jdbcName.contains("11g")) { //$NON-NLS-1$
                    if (System.getProperty("java.version").startsWith("1.6")) { //$NON-NLS-1$ //$NON-NLS-2$
                        jdbcName = jdbcName.replace('5', '6');
                    } else {
                        jdbcName = jdbcName.replace('6', '5');
                    }
                }

                if (flag == true) {
                    neededLibraries.add((jdbcName).replaceAll(TalendTextUtils.QUOTATION_MARK, "").replaceAll( //$NON-NLS-1$
                            TalendTextUtils.SINGLE_QUOTE, ""));//$NON-NLS-1$
                }
            }
        }

        String separator = ";"; //$NON-NLS-1$
        if (curParam.getName().equals("MQ_DERVIERS")) { //$NON-NLS-1$
            String path = (String) curParam.getValue();

            if (path == null || path.equals("")) { //$NON-NLS-1$
                return;
            }

            for (String jar : path.split(separator)) {
                neededLibraries.add(jar);
            }
        }
        if (curParam.getName().equals("HOTLIBS")) { //$NON-NLS-1$
            List<Map<String, Object>> tableValues = (List<Map<String, Object>>) curParam.getValue();

            for (Map<String, Object> line : tableValues) {
                if (line.containsKey("LIBPATH") && !StringUtil.isEmpty((String) line.get("LIBPATH"))) {
                    String path = (String) line.get("LIBPATH");
                    neededLibraries.add(TalendTextUtils.removeQuotes(path));
                }
            }
        }
    }
}
