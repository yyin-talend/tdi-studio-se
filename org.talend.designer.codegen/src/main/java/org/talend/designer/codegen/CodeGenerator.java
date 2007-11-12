// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.codegen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.codegen.jet.JETException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.IComponentFileNaming;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.temp.ECodePart;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.codegen.config.CloseBlocksCodeArgument;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.designer.codegen.config.EInternalTemplate;
import org.talend.designer.codegen.config.JetBean;
import org.talend.designer.codegen.config.NodesSubTree;
import org.talend.designer.codegen.config.NodesTree;
import org.talend.designer.codegen.config.SubTreeArgument;
import org.talend.designer.codegen.config.TemplateUtil;
import org.talend.designer.codegen.exception.CodeGeneratorException;
import org.talend.designer.codegen.i18n.Messages;
import org.talend.designer.codegen.model.CodeGeneratorEmittersPoolFactory;
import org.talend.designer.codegen.proxy.JetProxy;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * CodeGenerator.
 * 
 * $Id$
 * 
 */
public class CodeGenerator implements ICodeGenerator {

    private static Logger log = Logger.getLogger(CodeGenerator.class);

    private IProcess process;

    private boolean statistics;

    private boolean trace;

    private String interpreterPath;

    private String libPath;

    private String runtimeFilePath;

    private String currentProjectName;

    private String jobName;

    private boolean checkingSyntax = false;

    private String contextName;

    private ECodeLanguage language;

    private List<? extends INode> nodes;

    private NodesTree processTree;

    private static final long INIT_TIMEOUT = 15000; // 15s

    private static final long INIT_PAUSE = 1000; // 1s

    /**
     * Constructor : use the process and laguage to initialize internal components.
     * 
     * @param process
     * @param language
     */
    @SuppressWarnings("unchecked")
    public CodeGenerator(IProcess process, boolean statistics, boolean trace, String... options) {
        if (process == null) {
            throw new NullPointerException();
        } else {
            this.process = process;
            this.statistics = statistics;
            this.trace = trace;
            this.jobName = process.getLabel();
            this.contextName = process.getContextManager().getDefaultContext().getName();
            this.checkingSyntax = false;

            if ((options != null) && (options.length == 4)) {
                this.interpreterPath = options[0];
                this.libPath = options[1];
                this.runtimeFilePath = options[2];
                this.currentProjectName = options[3];
            } else {
                this.interpreterPath = "";
                this.libPath = "";
                this.runtimeFilePath = "";
                this.currentProjectName = "";
            }

            nodes = process.getGeneratingNodes();
            processTree = new NodesTree(nodes, true);
            RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY);
            language = repositoryContext.getProject().getLanguage();
        }
    }

    public CodeGenerator() {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        language = repositoryContext.getProject().getLanguage();
    }

    /**
     * Generate the code for the process given to the constructor.
     * 
     * @return the generated code
     * @throws CodeGeneratorException if an error occurs during Code Generation
     */
    @SuppressWarnings("unchecked")
    public String generateProcessCode() throws CodeGeneratorException {
        // Parse Process, generate Code for Individual Components
        // generate Assembly Code for individual Components
        StringBuffer componentsCode = new StringBuffer();

        long startTimer = Calendar.getInstance().getTimeInMillis();
        long endTimer = startTimer;
        try {
            while ((!CodeGeneratorEmittersPoolFactory.isInitialized()) && ((endTimer - startTimer) < INIT_TIMEOUT)) {
                Thread.sleep(INIT_PAUSE);
                endTimer = Calendar.getInstance().getTimeInMillis();
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new CodeGeneratorException(e);
        }
        if ((endTimer - startTimer) > INIT_TIMEOUT) {
            throw new CodeGeneratorException(Messages.getString("CodeGenerator.JET.TimeOut")); //$NON-NLS-1$
        } else {
            if ((processTree.getSubTrees() != null) && (processTree.getSubTrees().size() > 0)) {

                Vector headerArgument = new Vector(2);
                headerArgument.add(process);
                headerArgument.add(CodeGeneratorActivator.getDefault().getBundle().getHeaders().get(
                        org.osgi.framework.Constants.BUNDLE_VERSION));
                componentsCode.append(generateTypedComponentCode(EInternalTemplate.HEADER, headerArgument));
                for (NodesSubTree subTree : processTree.getSubTrees()) {
                    if (!subTree.isMergeSubTree()) {
                        componentsCode.append(generateTypedComponentCode(EInternalTemplate.SUBPROCESS_HEADER, subTree));
                        componentsCode.append(generateComponentsCode(subTree, subTree.getRootNode(), ECodePart.BEGIN, null));
                        componentsCode.append(generateComponentsCode(subTree, subTree.getRootNode(), ECodePart.MAIN, null));
                        componentsCode.append(generateTypedComponentCode(EInternalTemplate.PART_ENDMAIN, subTree.getRootNode()));
                        componentsCode.append(generateComponentsCode(subTree, subTree.getRootNode(), ECodePart.END, null));
                        componentsCode.append(generateTypedComponentCode(EInternalTemplate.SUBPROCESS_FOOTER, subTree));
                    } else {
                        componentsCode.append(generateTypedComponentCode(EInternalTemplate.SUBPROCESS_HEADER, subTree));
                        componentsCode.append(generateComponentsCode(subTree, subTree.getMergeNode(), ECodePart.BEGIN, null));

                        List<INode> sortedMergeBranchStarts = subTree.getSortedMergeBranchStarts();
                        for (INode startNode : sortedMergeBranchStarts) {
                            componentsCode.append(generateComponentsCode(subTree, startNode, ECodePart.BEGIN, null));
                            componentsCode.append(generateComponentsCode(subTree, startNode, ECodePart.MAIN, null));

                            componentsCode.append(generateComponentsCode(subTree, startNode, ECodePart.END, null));
                        }

                        componentsCode.append(generateTypedComponentCode(EInternalTemplate.PART_ENDMAIN, subTree.getRootNode()));

                        componentsCode.append(generateComponentsCode(subTree, subTree.getMergeNode(), ECodePart.END, null));

                        componentsCode.append(generateTypedComponentCode(EInternalTemplate.SUBPROCESS_FOOTER, subTree));
                    }

                }
                Vector footerArgument = new Vector(2);
                footerArgument.add(process);
                footerArgument.add(processTree.getRootNodes());
                componentsCode.append(generateTypedComponentCode(EInternalTemplate.FOOTER, footerArgument));
                componentsCode.append(generateTypedComponentCode(EInternalTemplate.PROCESSINFO, componentsCode.length()));
            }

            return componentsCode.toString();
        }
    }

    /**
     * get the incomingName matching with inputId. Purpose: It will generate different parts for the merge node
     * according the different incomingName.
     * 
     * @param branchStartNode
     * @param mergeNode
     * @return
     */
    private String getIncomingNameForMerge(INode branchStartNode, INode mergeNode) {

        Map<INode, Integer> mergeInfo = branchStartNode.getLinkedMergeInfo();

        int inputId = ((Integer) mergeInfo.values().toArray()[0]).intValue();

        List<? extends IConnection> incomingConnections = mergeNode.getIncomingConnections();

        for (int i = 0; i < incomingConnections.size(); i++) {
            IConnection connec = incomingConnections.get(i);
            if (connec.isActivate()) {

                if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MERGE)) {
                    // if find, then return
                    if (connec.getInputId() == inputId) {
                        return connec.getName();
                    }
                }
            }
        }

        return null;
    }

    /**
     * Parse Process, and generate Code for Context Variables.
     * 
     * @param designerContext the context to generate code from
     * @return the generated code
     * @throws CodeGeneratorException if an error occurs during Code Generation
     */
    public String generateContextCode(IContext designerContext) throws CodeGeneratorException {
        if (process != null) {
            if (designerContext == null) {
                designerContext = process.getContextManager().getDefaultContext();
            }
            List<IContextParameter> listParameters = designerContext.getContextParameterList();

            if (listParameters != null) {
                CodeGeneratorArgument codeGenArgument = new CodeGeneratorArgument();
                codeGenArgument.setNode(listParameters);
                codeGenArgument.setContextName(designerContext.getName());
                codeGenArgument.setCurrentProjectName(currentProjectName);
                codeGenArgument.setJobName(jobName);
                codeGenArgument.setIsRunInMultiThread(getRunInMultiThread());
                codeGenArgument.setPauseTime(CorePlugin.getDefault().getRunProcessService().getPauseTime());

                JetBean jetBean = initializeJetBean(codeGenArgument);

                jetBean.setTemplateRelativeUri(TemplateUtil.RESOURCES_DIRECTORY + TemplateUtil.DIR_SEP
                        + EInternalTemplate.CONTEXT + TemplateUtil.EXT_SEP + language.getExtension() + TemplateUtil.TEMPLATE_EXT);

                JetProxy proxy = new JetProxy(jetBean);
                String content;
                try {
                    content = proxy.generate();
                } catch (JETException e) {
                    log.error(e.getMessage(), e);
                    throw new CodeGeneratorException(e);
                } catch (CoreException e) {
                    log.error(e.getMessage(), e);
                    throw new CodeGeneratorException(e);
                }
                return content;
            }
        }
        return "";
    }

    /**
     * Generate Code for a given Component.
     * 
     * @param type the internal component template
     * @param argument the bean
     * @return the generated code
     * @throws CodeGeneratorException if an error occurs during Code Generation
     */
    private StringBuffer generateTypedComponentCode(EInternalTemplate type, Object argument) throws CodeGeneratorException {
        return generateTypedComponentCode(type, argument, null);
    }

    /**
     * Generate Code Part for a given Component.
     * 
     * @param type the internal component template
     * @param argument the bean
     * @param part part of code to generate
     * @return the genrated code
     * @throws CodeGeneratorException if an error occurs during Code Generation
     */
    private StringBuffer generateTypedComponentCode(EInternalTemplate type, Object argument, ECodePart part)
            throws CodeGeneratorException {
        return generateTypedComponentCode(type, argument, part, null);
    }

    /**
     * Generate Code Part for a given Component.
     * 
     * @param type the internal component template
     * @param argument the bean
     * @param part part of code to generate
     * @return the genrated code
     * @throws CodeGeneratorException if an error occurs during Code Generation
     */
    private StringBuffer generateTypedComponentCode(EInternalTemplate type, Object argument, ECodePart part, String incomingName)
            throws CodeGeneratorException {
        CodeGeneratorArgument codeGenArgument = new CodeGeneratorArgument();
        codeGenArgument.setNode(argument);
        codeGenArgument.setCodePart(part);
        codeGenArgument.setStatistics(statistics);
        codeGenArgument.setTrace(trace);
        codeGenArgument.setInterpreterPath(interpreterPath);
        codeGenArgument.setLibPath(libPath);
        codeGenArgument.setRuntimeFilePath(runtimeFilePath);
        codeGenArgument.setCurrentProjectName(currentProjectName);
        codeGenArgument.setContextName(contextName);
        codeGenArgument.setJobName(jobName);
        codeGenArgument.setCheckingSyntax(checkingSyntax);
        codeGenArgument.setIncomingName(incomingName);
        codeGenArgument.setIsRunInMultiThread(getRunInMultiThread());
        codeGenArgument.setPauseTime(CorePlugin.getDefault().getRunProcessService().getPauseTime());
        JetBean jetBean = initializeJetBean(codeGenArgument);

        jetBean.setTemplateRelativeUri(TemplateUtil.RESOURCES_DIRECTORY + TemplateUtil.DIR_SEP + type + TemplateUtil.EXT_SEP
                + language.getExtension() + TemplateUtil.TEMPLATE_EXT);

        JetProxy proxy = new JetProxy(jetBean);
        StringBuffer content = new StringBuffer();
        try {
            content.append(proxy.generate());
        } catch (JETException e) {
            log.error(e.getMessage(), e);
            throw new CodeGeneratorException(e);
        } catch (CoreException e) {
            log.error(e.getMessage(), e);
            throw new CodeGeneratorException(e);
        }
        return content;
    }

    private boolean getRunInMultiThread() {
        // this preferencestore initialize epic preferencestore
        // and the epic's preferencestore initializer instanciate swt objects !
        // so in headless mode, we'll always use MultiThreading by default
        if (!CorePlugin.getContext().isHeadless()) {
            return CorePlugin.getDefault().getPreferenceStore().getBoolean(ITalendCorePrefConstants.RUN_IN_MULTI_THREAD);
        } else {
            return false;
        }
    }

    /**
     * Generate Code Parts for a Sub Process .
     * 
     * @param subProcess the suprocess
     * @param node the subprocess root
     * @param part the part of code to generate
     * @return the generated code
     * @throws CodeGeneratorException if an error occurs during Code Generation
     */
    private StringBuffer generateComponentsCode(NodesSubTree subProcess, INode node, ECodePart part, String incomingName)
            throws CodeGeneratorException {
        StringBuffer codeComponent = new StringBuffer();
        Boolean isMarked = subProcess.isMarkedNode(node, part);
        boolean isIterate = isIterateNode(node);
        if ((isMarked != null) && (!isMarked)) {
            switch (part) {
            case BEGIN:
                if (isIterate) {
                    codeComponent.append(generateComponentCode(node, ECodePart.BEGIN, incomingName));
                }
                codeComponent.append(generatesTreeCode(subProcess, node, part));
                if (!isIterate) {
                    codeComponent.append(generateComponentCode(node, ECodePart.BEGIN, incomingName));
                }
                break;
            case MAIN:
                codeComponent.append(generateComponentCode(node, ECodePart.MAIN, incomingName));
                codeComponent.append(generatesTreeCode(subProcess, node, part));
                break;
            case END:
                if (!isIterate) {
                    codeComponent.append(generateComponentCode(node, ECodePart.END, incomingName));
                }
                codeComponent.append(generatesTreeCode(subProcess, node, part));
                if (isIterate) {
                    codeComponent.append(generateComponentCode(node, ECodePart.END, incomingName));
                }
                break;
            default:
                // do nothing
            }
            subProcess.markNode(node, part);
        }

        return codeComponent;
    }

    /**
     * Return Type of Node to correctly sort the encapsulated code.
     * 
     * @param node the node to check
     * @return true if the node is an iterate node
     */
    private boolean isIterateNode(INode node) {
        boolean result = false;
        if (node != null) {
            List<? extends IConnection> outGoingConnections = node.getOutgoingConnections();
            if ((outGoingConnections != null) && (outGoingConnections.size() > 0)) {
                for (IConnection connection : outGoingConnections) {
                    if (connection.getLineStyle() == EConnectionType.ITERATE) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Generate this tree Code.
     * 
     * @param subProcess the tree
     * @param node the tree root
     * @param part the part of code to generate
     * @return the generated code
     * @throws CodeGeneratorException if an error occurs during Code Generation
     */
    private StringBuffer generatesTreeCode(NodesSubTree subProcess, INode node, ECodePart part) throws CodeGeneratorException {
        StringBuffer code = new StringBuffer();
        if (node != null) {
            SubTreeArgument subTreeArgument = new SubTreeArgument();

            // Conditional Outputs
            boolean sourceHasConditionnalBranches = node.hasConditionalOutputs() && part == ECodePart.MAIN;
            subTreeArgument.setSourceComponentHasConditionnalOutputs(sourceHasConditionnalBranches);

            // Multiplying Output Rows
            if (part == ECodePart.MAIN) {
                subTreeArgument.setMultiplyingOutputComponents(node.isMultiplyingOutputs());
            }

            for (IConnection connection : node.getOutgoingConnections()) {
                INode targetNode = connection.getTarget();
                if ((targetNode != null) && (subProcess != null)) {

                    if (!connection.getLineStyle().hasConnectionCategory(IConnectionCategory.MERGE)) {
                        subTreeArgument.setInputSubtreeConnection(connection);
                        code.append(generateTypedComponentCode(EInternalTemplate.SUBTREE_BEGIN, subTreeArgument));
                        code.append(generateComponentsCode(subProcess, targetNode, part, null));
                        code.append(generateTypedComponentCode(EInternalTemplate.SUBTREE_END, subTreeArgument));
                    } else if (part == ECodePart.MAIN) {
                        subTreeArgument.setInputSubtreeConnection(connection);
                        code.append(generateTypedComponentCode(EInternalTemplate.SUBTREE_BEGIN, subTreeArgument));
                        code.append(generateComponentsCode(subProcess, targetNode, ECodePart.MAIN, getIncomingNameForMerge(node,
                                targetNode)));
                        code.append(generateTypedComponentCode(EInternalTemplate.SUBTREE_END, subTreeArgument));
                    }

                }
            }

            if (part == ECodePart.MAIN && node.getBlocksCodeToClose() != null) {
                CloseBlocksCodeArgument closeBlocksArgument = new CloseBlocksCodeArgument();
                closeBlocksArgument.setBlocksCodeToClose(node.getBlocksCodeToClose());
                code.append(generateTypedComponentCode(EInternalTemplate.CLOSE_BLOCKS_CODE, closeBlocksArgument));
            }
        }
        return code;
    }

    /**
     * Generate Part Code for a given Component.
     * 
     * @param node the component
     * @param part the component's part
     * @return the generated code
     * @throws CodeGeneratorException if an error occurs during Code Generation
     */
    public String generateComponentCode(INode node, ECodePart part, String incomingName) throws CodeGeneratorException {
        CodeGeneratorArgument argument = new CodeGeneratorArgument();
        argument.setNode(node);
        argument.setCodePart(part);
        argument.setStatistics(statistics);
        argument.setTrace(trace);
        argument.setInterpreterPath(interpreterPath);
        argument.setLibPath(libPath);
        argument.setRuntimeFilePath(runtimeFilePath);
        argument.setCurrentProjectName(currentProjectName);
        argument.setContextName(contextName);
        argument.setJobName(jobName);
        argument.setCheckingSyntax(checkingSyntax);
        argument.setIncomingName(incomingName);
        argument.setIsRunInMultiThread(getRunInMultiThread());
        argument.setPauseTime(CorePlugin.getDefault().getRunProcessService().getPauseTime());

        JetBean jetBean = initializeJetBean(argument);

        StringBuffer content = new StringBuffer();
        try {
            content.append(generateTypedComponentCode(EInternalTemplate.PART_HEADER, node, part, incomingName));

            IComponentFileNaming componentFileNaming = ComponentsFactoryProvider.getFileNamingInstance();
            String templateURI = node.getComponent().getPathSource() + TemplateUtil.DIR_SEP + node.getComponent().getName()
                    + TemplateUtil.DIR_SEP
                    + componentFileNaming.getJetFileName(node.getComponent(), language.getExtension(), part);

            jetBean.setTemplateRelativeUri(templateURI);
            JetProxy proxy = new JetProxy(jetBean);
            content.append(proxy.generate());
            content.append(generateTypedComponentCode(EInternalTemplate.PART_FOOTER, node, part, incomingName));
        } catch (JETException jetException) {
            log.error(jetException.getMessage(), jetException);
            throw new CodeGeneratorException(jetException);
        } catch (CoreException coreException) {
            log.error(coreException.getMessage(), coreException);
            throw new CodeGeneratorException(coreException);
        }
        return content.toString();
    }

    /**
     * Generate Part Code for a given Component.
     * 
     * @param node the component
     * @param part the component's part
     * @return the generated code
     * @throws CodeGeneratorException if an error occurs during Code Generation
     */
    public String generateComponentCode(INode node, ECodePart part) throws CodeGeneratorException {
        CodeGeneratorArgument argument = new CodeGeneratorArgument();
        argument.setNode(node);
        argument.setCodePart(part);
        argument.setStatistics(statistics);
        argument.setTrace(trace);
        argument.setInterpreterPath(interpreterPath);
        argument.setLibPath(libPath);
        argument.setRuntimeFilePath(runtimeFilePath);
        argument.setCurrentProjectName(currentProjectName);
        argument.setContextName(contextName);
        argument.setJobName(jobName);
        argument.setCheckingSyntax(checkingSyntax);
        argument.setIsRunInMultiThread(getRunInMultiThread());
        argument.setPauseTime(CorePlugin.getDefault().getRunProcessService().getPauseTime());
        JetBean jetBean = initializeJetBean(argument);

        StringBuffer content = new StringBuffer();
        try {
            content.append(generateTypedComponentCode(EInternalTemplate.PART_HEADER, node, part));

            IComponentFileNaming componentFileNaming = ComponentsFactoryProvider.getFileNamingInstance();
            String templateURI = node.getComponent().getPathSource() + TemplateUtil.DIR_SEP + node.getComponent().getName()
                    + TemplateUtil.DIR_SEP
                    + componentFileNaming.getJetFileName(node.getComponent(), language.getExtension(), part);

            jetBean.setTemplateRelativeUri(templateURI);
            JetProxy proxy = new JetProxy(jetBean);
            content.append(proxy.generate());
            content.append(generateTypedComponentCode(EInternalTemplate.PART_FOOTER, node, part));
        } catch (JETException jetException) {
            log.error(jetException.getMessage(), jetException);
            throw new CodeGeneratorException(jetException);
        } catch (CoreException coreException) {
            log.error(coreException.getMessage(), coreException);
            throw new CodeGeneratorException(coreException);
        }
        return content.toString();
    }

    /**
     * Initialize Jet Bean to pass to the Jet Generator.
     * 
     * @param argument the node to convert
     * @return the initialized JetBean
     */
    private JetBean initializeJetBean(Object argument) {
        JetBean jetBean = new JetBean();

        if (argument == null) {
            jetBean.setJetPluginRepository(CodeGeneratorActivator.PLUGIN_ID);
        } else {
            if (argument instanceof CodeGeneratorArgument) {
                CodeGeneratorArgument codeArgument = (CodeGeneratorArgument) argument;
                if (codeArgument.getArgument() instanceof INode) {
                    jetBean.setJetPluginRepository(IComponentsFactory.COMPONENTS_LOCATION);
                } else {
                    jetBean.setJetPluginRepository(CodeGeneratorActivator.PLUGIN_ID);
                }
            } else {
                jetBean.setJetPluginRepository(CodeGeneratorActivator.PLUGIN_ID);
            }
        }

        jetBean.setArgument(argument);
        return jetBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.ICodeGenerator#generateComponentCodeWithRows(java.lang.String, java.lang.Object)
     */
    public String generateComponentCodeWithRows(String nodeName, IAloneProcessNodeConfigurer nodeConfigurer) {
        StringBuffer componentsCode = new StringBuffer();
        if (process == null) {
            throw new NullPointerException();
        } else {
            List<? extends INode> allProcessNodes = process.getGeneratingNodes();
            INode node = extractNodeFromProcess(nodeName);
            if (node != null) {
                List<INode> lightProcessNodes = new ArrayList<INode>();
                lightProcessNodes.add(node);

                NodesTree lightProcess = new NodesTree(lightProcessNodes, false);
                lightProcess.addRootNode(node);
                lightProcess.buildSubTrees(false);

                Vector headerArgument = new Vector(2);
                headerArgument.add(process);
                headerArgument.add(CodeGeneratorActivator.getDefault().getBundle().getHeaders().get(
                        org.osgi.framework.Constants.BUNDLE_VERSION));

                this.checkingSyntax = true;

                try {
                    if (nodeConfigurer != null) {
                        nodeConfigurer.configure(node);
                    }
                    componentsCode.append(generateTypedComponentCode(EInternalTemplate.HEADER, headerArgument));
                    for (NodesSubTree subTree : processTree.getSubTrees()) {
                        INode subTreeNode = subTree.getNode(node.getUniqueName());
                        if (subTreeNode != null && nodeConfigurer != null) {
                            nodeConfigurer.configure(subTreeNode);
                        }
                        componentsCode.append(generateTypedComponentCode(EInternalTemplate.SUBPROCESS_HEADER, subTree));
                        if (subTreeNode != null && (lightProcess.getSubTrees().size() > 0)) {
                            componentsCode.append(generateComponentsCode(lightProcess.getSubTrees().get(0), lightProcess
                                    .getSubTrees().get(0).getRootNode(), ECodePart.BEGIN, null));
                            componentsCode.append(generateComponentsCode(lightProcess.getSubTrees().get(0), lightProcess
                                    .getSubTrees().get(0).getRootNode(), ECodePart.MAIN, null));
                            componentsCode.append(generateTypedComponentCode(EInternalTemplate.PART_ENDMAIN, null));
                            componentsCode.append(generateComponentsCode(lightProcess.getSubTrees().get(0), lightProcess
                                    .getSubTrees().get(0).getRootNode(), ECodePart.END, null));
                        }
                        componentsCode.append(generateTypedComponentCode(EInternalTemplate.SUBPROCESS_FOOTER, subTree));
                    }
                    Vector footerArgument = new Vector(2);
                    footerArgument.add(process);
                    footerArgument.add(processTree.getRootNodes());
                    componentsCode.append(generateTypedComponentCode(EInternalTemplate.FOOTER, footerArgument));
                    componentsCode.append(generateTypedComponentCode(EInternalTemplate.PROCESSINFO, componentsCode.length()));
                } catch (CodeGeneratorException ce) {
                    ce.printStackTrace();
                    componentsCode = new StringBuffer();
                }
            } else {
                throw new TypeNotPresentException(Messages.getString("CodeGenerator.Node.NotFound"), null); //$NON-NLS-1$
            }
        }
        return componentsCode.toString();
    }

    /**
     * DOC mhirt Comment method "extractNodeFromProcess".
     * 
     * @param nodeName
     * @return
     */
    private INode extractNodeFromProcess(String nodeName) {
        List<? extends INode> allProcessNodes = process.getGeneratingNodes();
        for (INode node : allProcessNodes) {
            if (node.getUniqueName().compareTo(nodeName) == 0) {
                return node;
            }
        }
        return null;
    }
}
