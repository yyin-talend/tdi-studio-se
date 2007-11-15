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
package org.talend.designer.core.ui.views;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.text.SimpleJavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.talend.commons.exception.SystemException;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.temp.ECodePart;
import org.talend.core.ui.viewer.java.TalendJavaViewerConfiguration;
import org.talend.core.ui.viewer.perl.TalendPerlSourceViewer;
import org.talend.designer.codegen.ICodeGenerator;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.connections.ConnLabelEditPart;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.connections.ConnectionLabel;
import org.talend.designer.core.ui.editor.connections.ConnectionPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;
import org.talend.designer.core.ui.editor.nodes.NodeLabelEditPart;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.outline.NodeTreeEditPart;

/**
 * View that will show the code of the current component.
 * 
 * $Id$
 * 
 */
public class CodeView extends ViewPart implements ISelectionListener {

    // private StyledText text;

    private IDocument document;

    private ICodeGenerator codeGenerator = null;

    private static final int CODE_START = 0;

    private static final int CODE_MAIN = 1;

    private static final int CODE_END = 2;

    private static final int CODE_ALL = 3;

    private int codeView = CODE_MAIN;

    private int nodeCodeView = -1;

    private static final String ERROR_MESSAGE = Messages.getString("CodeView.Error"); //$NON-NLS-1$

    private static final String MULTIPLE_COMPONENT_ERROR_MESSAGE = Messages.getString("CodeView.MultipleComponentError"); //$NON-NLS-1$

    private INode selectedNode = null;

    private INode generatingNode = null;

    public static final String ID = "org.talend.designer.core.codeView"; //$NON-NLS-1$

    IAction viewStartAction = new Action() {

        @Override
        public String getText() {
            return Messages.getString("CodeView.Start"); //$NON-NLS-1$
        }

        @Override
        public void run() {
            codeView = CODE_START;
            refresh();
        }

    };

    IAction viewMainAction = new Action() {

        @Override
        public String getText() {
            return Messages.getString("CodeView.Main"); //$NON-NLS-1$
        }

        @Override
        public void run() {
            codeView = CODE_MAIN;
            refresh();
        }

    };

    IAction viewEndAction = new Action() {

        @Override
        public String getText() {
            return Messages.getString("CodeView.End"); //$NON-NLS-1$
        }

        @Override
        public void run() {
            codeView = CODE_END;
            refresh();
        }

    };

    IAction viewAllAction = new Action() {

        @Override
        public String getText() {
            return Messages.getString("CodeView.All"); //$NON-NLS-1$
        }

        @Override
        public void run() {
            codeView = CODE_ALL;
            refresh();
        }

    };

    public CodeView() {
    }

    @Override
    public void createPartControl(final Composite parent) {
        parent.setLayout(new FillLayout());
        ECodeLanguage language = LanguageManager.getCurrentLanguage();
        ISourceViewer viewer;
        final StyledText text;
        int styles = SWT.H_SCROLL | SWT.V_SCROLL;
        document = new Document();
        switch (language) {
        case JAVA:
            IPreferenceStore store = JavaPlugin.getDefault().getCombinedPreferenceStore();
            viewer = new JavaSourceViewer(parent, null, null, false, styles, store);
            viewer.setDocument(document);
            JavaTextTools tools = JavaPlugin.getDefault().getJavaTextTools();
            tools.setupJavaDocumentPartitioner(viewer.getDocument(), IJavaPartitions.JAVA_PARTITIONING);
            SimpleJavaSourceViewerConfiguration config = new SimpleJavaSourceViewerConfiguration(tools.getColorManager(), store,
                    null, IJavaPartitions.JAVA_PARTITIONING, true);
            TalendJavaViewerConfiguration config2 = new TalendJavaViewerConfiguration(tools.getColorManager(), store, null);
            viewer.configure(config);
            viewer.getTextWidget().setFont(JFaceResources.getFont(PreferenceConstants.EDITOR_TEXT_FONT));
            document = viewer.getDocument();
            break;
        default: // PERL
            viewer = TalendPerlSourceViewer.createViewer(parent, styles, false);
            document = viewer.getDocument();
        }
        viewer.setEditable(false);
        text = viewer.getTextWidget();
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
        IAction wrapAction = new Action() {

            @Override
            public void run() {
                text.setWordWrap(isChecked());
            }
        };
        wrapAction.setToolTipText("wrap");
        wrapAction.setChecked(false);
        wrapAction.setImageDescriptor(ImageDescriptor.createFromFile(DesignerPlugin.class, "/icons/wrap.gif")); //$NON-NLS-1$
        tbm.add(wrapAction);
        createMenu();

    }

    private void createMenu() {
        IMenuManager manager = getViewSite().getActionBars().getMenuManager();

        manager.add(viewStartAction);
        manager.add(viewMainAction);
        manager.add(viewEndAction);
        manager.add(viewAllAction);
        viewMainAction.setChecked(true);
    }

    @Override
    public void setFocus() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart,
     * org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            Object input = ((IStructuredSelection) selection).getFirstElement();
            // just used to save the state of the node view when we select on a RUN_IF connection
            if (input instanceof NodeTreeEditPart) {
                NodeTreeEditPart nTreePart = (NodeTreeEditPart) input;
                selectedNode = (Node) nTreePart.getModel();
                if (nodeCodeView != -1) {
                    codeView = nodeCodeView;
                    nodeCodeView = -1;
                }
                refresh();
            } else if (input instanceof NodePart) {
                NodePart editPart = (NodePart) input;
                selectedNode = (Node) editPart.getModel();
                if (nodeCodeView != -1) {
                    codeView = nodeCodeView;
                    nodeCodeView = -1;
                }
                refresh();
            } else if (input instanceof NodeLabelEditPart) {
                NodeLabelEditPart editPart = (NodeLabelEditPart) input;
                selectedNode = (Node) ((NodeLabel) editPart.getModel()).getNode();
                if (nodeCodeView != -1) {
                    codeView = nodeCodeView;
                    nodeCodeView = -1;
                }
                refresh();
            } else if (input instanceof ConnectionPart) {
                ConnectionPart editPart = (ConnectionPart) input;
                Connection connection = (Connection) editPart.getModel();
                if (connection.getLineStyle() == EConnectionType.RUN_IF) {
                    selectedNode = connection.getSource();
                    nodeCodeView = codeView;
                    codeView = CODE_END;
                    refresh();
                }
            } else if (input instanceof ConnLabelEditPart) {
                ConnLabelEditPart editPart = (ConnLabelEditPart) input;
                Connection connection = ((ConnectionLabel) editPart.getModel()).getConnection();
                if (connection.getLineStyle() == EConnectionType.RUN_IF) {
                    selectedNode = connection.getSource();
                    nodeCodeView = codeView;
                    codeView = CODE_END;
                    refresh();
                }
            }
        }
    }

    public void refresh() {
        if (selectedNode != null) {
            IExternalNode externalNode = ((Node) selectedNode).getExternalNode();
            if (externalNode != null) {
                generatingNode = externalNode;
                externalNode.setExternalData(((Node) selectedNode).getExternalData());
            } else {
                generatingNode = selectedNode;
            }
            if (generatingNode.getComponent().getMultipleComponentManager() != null) {
                document.set(MULTIPLE_COMPONENT_ERROR_MESSAGE);
                return;
            }
            String generatedCode = ""; //$NON-NLS-1$
            if (codeGenerator == null) {
                ICodeGeneratorService service = DesignerPlugin.getDefault().getCodeGeneratorService();
                codeGenerator = service.createCodeGenerator();
            }
            viewStartAction.setChecked(false);
            viewMainAction.setChecked(false);
            viewEndAction.setChecked(false);
            viewAllAction.setChecked(false);
            try {
                switch (codeView) {
                case CODE_START:
                    viewStartAction.setChecked(true);
                    generatedCode = codeGenerator.generateComponentCode(generatingNode, ECodePart.BEGIN);
                    break;
                case CODE_MAIN:
                    viewMainAction.setChecked(true);
                    generatedCode = codeGenerator.generateComponentCode(generatingNode, ECodePart.MAIN);
                    break;
                case CODE_END:
                    viewEndAction.setChecked(true);
                    generatedCode = codeGenerator.generateComponentCode(generatingNode, ECodePart.END);
                    break;
                case CODE_ALL:
                    viewAllAction.setChecked(true);
                    generatedCode = codeGenerator.generateComponentCode(generatingNode, ECodePart.BEGIN);
                    generatedCode += codeGenerator.generateComponentCode(generatingNode, ECodePart.MAIN);
                    generatedCode += codeGenerator.generateComponentCode(generatingNode, ECodePart.END);
                    break;
                default:
                }
            } catch (SystemException e) {
                document.set(ERROR_MESSAGE);
                throw new RuntimeException(e);
            }
            document.set(generatedCode);
        }
    }
}
