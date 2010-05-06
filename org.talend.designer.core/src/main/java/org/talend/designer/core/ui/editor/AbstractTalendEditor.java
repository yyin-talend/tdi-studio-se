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
package org.talend.designer.core.ui.editor;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.Disposable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.utils.workbench.preferences.GlobalConstant;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.ISubjobContainer;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.NodePartKeyHander;
import org.talend.designer.core.ui.action.ConnectionSetAsMainRef;
import org.talend.designer.core.ui.action.GEFCopyAction;
import org.talend.designer.core.ui.action.GEFDeleteAction;
import org.talend.designer.core.ui.action.GEFPasteAction;
import org.talend.designer.core.ui.action.ModifyConnectionOrderAction;
import org.talend.designer.core.ui.action.ModifyMergeOrderAction;
import org.talend.designer.core.ui.action.ModifyOutputOrderAction;
import org.talend.designer.core.ui.action.TalendConnectionCreationTool;
import org.talend.designer.core.ui.action.ToggleSubjobsAction;
import org.talend.designer.core.ui.editor.cmd.MoveNodeCommand;
import org.talend.designer.core.ui.editor.connections.ConnectionPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.outline.NodeTreeEditPart;
import org.talend.designer.core.ui.editor.outline.ProcessTreePartFactory;
import org.talend.designer.core.ui.editor.palette.TalendDrawerEditPart;
import org.talend.designer.core.ui.editor.palette.TalendFlyoutPaletteComposite;
import org.talend.designer.core.ui.editor.palette.TalendPaletteDrawer;
import org.talend.designer.core.ui.editor.palette.TalendPaletteViewerProvider;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.designer.core.ui.editor.process.TalendEditorDropTargetListener;
import org.talend.designer.core.ui.views.jobsettings.JobSettings;
import org.talend.designer.core.ui.views.properties.ComponentSettingsView;
import org.talend.designer.core.ui.views.properties.IComponentSettingsView;
import org.talend.designer.runprocess.JobInfo;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.editor.JobEditorInput;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.job.deletion.IJobResourceProtection;
import org.talend.repository.job.deletion.JobResource;
import org.talend.repository.job.deletion.JobResourceManager;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.RepositoryConstants;

/**
 * DOC qzhang class global comment. Detailled comment
 */
public abstract class AbstractTalendEditor extends GraphicalEditorWithFlyoutPalette implements
        ITabbedPropertySheetPageContributor, IJobResourceProtection {

    // @Override
    public void createPartControl(Composite parent) {

        splitter = new TalendFlyoutPaletteComposite(parent, SWT.NONE, getSite().getPage(), getPaletteViewerProvider(),
                getPalettePreferences());
        createGraphicalViewer(splitter);
        splitter.setGraphicalControl(getGraphicalControl());
        if (page != null) {
            splitter.setExternalViewer(page.getPaletteViewer());
            page = null;
        }
    }

    private OutlinePage outlinePage;

    protected FigureCanvas getEditor() {
        return (FigureCanvas) getGraphicalViewer().getControl();
    }

    public void selectPaletteEntry(String componentName) {
        PaletteViewer paletteViewer = getPaletteViewerProvider().getEditDomain().getPaletteViewer();
        PaletteRoot root = getPaletteRoot();
        RootEditPart part = paletteViewer.getRootEditPart();
        collapsePalette(part.getChildren());
        selectPaletteEntry(componentName, paletteViewer, root.getChildren());
    }

    public PaletteViewer getPaletteViewer2() {
        return getPaletteViewerProvider().getEditDomain().getPaletteViewer();
    }

    /**
     * DOC hcw Comment method "collapsePalette".
     * 
     * @param children
     */
    private void collapsePalette(List children) {
        for (Object object : children) {
            if (object instanceof TalendDrawerEditPart) {
                TalendDrawerEditPart part = (TalendDrawerEditPart) object;
                part.setExpanded(false);
                collapsePalette(part.getChildren());
            } else if (object instanceof EditPart) {
                collapsePalette(((EditPart) object).getChildren());
            }
        }
    }

    private boolean selectPaletteEntry(String componentName, PaletteViewer paletteViewer, List entries) {
        for (Object entry : entries) {

            if (entry instanceof PaletteContainer) {
                PaletteContainer container = (PaletteContainer) entry;
                if (selectPaletteEntry(componentName, paletteViewer, container.getChildren())) {
                    return true;
                }

            } else if (entry instanceof TalendPaletteDrawer) {
                TalendPaletteDrawer drawer = (TalendPaletteDrawer) entry;
                if (selectPaletteEntry(componentName, paletteViewer, drawer.getChildren())) {
                    return true;
                }
            } else if (entry instanceof ToolEntry) {
                ToolEntry paletteEntry = (ToolEntry) entry;
                if (paletteEntry.getLabel().equals(componentName)) {
                    ToolEntryEditPart part = getToolEntryEditPart(paletteViewer, paletteEntry);
                    expandPaletteDrawer(paletteViewer, paletteEntry);
                    // paletteViewer.setSelection(new StructuredSelection(part));
                    // paletteViewer.setFocus(part);
                    paletteViewer.select(part);
                    paletteViewer.reveal(part);
                    paletteViewer.setActiveTool(paletteEntry);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * DOC hcw Comment method "expandPaletteDrawer".
     * 
     * @param paletteViewer
     * @param paletteEntry
     */
    private void expandPaletteDrawer(PaletteViewer paletteViewer, ToolEntry paletteEntry) {
        List<TalendDrawerEditPart> parts = new ArrayList<TalendDrawerEditPart>();
        PaletteContainer parent = paletteEntry.getParent();
        while (parent != null) {
            if (parent instanceof TalendPaletteDrawer) {
                Object editpart = paletteViewer.getEditPartRegistry().get(parent);
                if (editpart instanceof TalendDrawerEditPart) {
                    parts.add((TalendDrawerEditPart) editpart);
                }
            }
            parent = parent.getParent();
        }
        for (int i = parts.size() - 1; i >= 0; i--) {
            parts.get(i).setExpanded(true);
        }
    }

    private ToolEntryEditPart getToolEntryEditPart(PaletteViewer paletteViewer, ToolEntry entry) {
        return (ToolEntryEditPart) paletteViewer.getEditPartRegistry().get(entry);
    }

    public void savePaletteState() {
        PaletteViewer paletteViewer = getPaletteViewerProvider().getEditDomain().getPaletteViewer();
        if (paletteViewer != null) {
            TalendEditorPaletteFactory.saveFamilyState(paletteViewer);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
     */
    @Override
    protected PaletteRoot getPaletteRoot() {
        return ComponentUtilities.getPaletteRoot();
    }

    @Override
    public Object getAdapter(final Class type) {

        // this will select the TabbedPropertyPage instead of the classic property view
        // available in Eclipse 3.2
        if (type == IPropertySheetPage.class) {
            IPropertySheetPage properties = new TalendTabbedPropertySheetPage(this);
            return properties;
        }

        if (type == IContentOutlinePage.class) {
            outlinePage = new OutlinePage(new TreeViewer());
            return outlinePage;
        }
        if (type == ZoomManager.class) {
            return getGraphicalViewer().getProperty(ZoomManager.class.toString());
        }

        return super.getAdapter(type);
    }

    /**
     * yzhang Comment method "updatePaletteContent".
     * 
     * have moved to UpdateManager(feature 3232).
     */
    // public void updateGraphicalNodes(PropertyChangeEvent evt) {
    // String propertyName = evt.getPropertyName();
    // if (components == null) {
    // components = ComponentsFactoryProvider.getInstance();
    // }
    // if (propertyName.equals(ComponentUtilities.NORMAL)) {
    // for (Node node : (List<Node>) process.getGraphicalNodes()) {
    // IComponent newComponent = components.get(node.getComponent().getName());
    // if (newComponent == null) {
    // continue;
    // }
    // Map<String, Object> parameters = new HashMap<String, Object>();
    //
    // if (node.getComponent().getComponentType() != EComponentType.JOBLET) {
    // if (node.getExternalData() != null) {
    // parameters.put(INode.RELOAD_PARAMETER_EXTERNAL_BYTES_DATA, node.getExternalBytesData());
    // }
    // parameters.put(INode.RELOAD_PARAMETER_METADATA_LIST, node.getMetadataList());
    // parameters.put(INode.RELAOD_PARAMETER_ELEMENT_PARAMETERS, node.getElementParameters());
    // } else {
    // parameters.put(INode.RELOAD_NEW, true);
    // }
    //
    // parameters.put(INode.RELOAD_PARAMETER_CONNECTORS, node.getListConnector());
    //
    // node.reloadComponent(newComponent, parameters);
    // // System.out.println(node.getUniqueName() + " reload.");
    // }
    //
    // for (Node node : (List<Node>) process.getGraphicalNodes()) {
    // node.checkAndRefreshNode();
    // }
    //
    // } else if (propertyName.equals(ComponentUtilities.JOBLET_NAME_CHANGED)) {
    // String oldName = (String) evt.getOldValue();
    // String newName = (String) evt.getNewValue();
    //
    // for (Node node : (List<Node>) process.getGraphicalNodes()) {
    // if (node.getComponent().getName().equals(oldName) || node.getLabel().contains(oldName)) {
    //
    // IComponent newComponent = components.get(newName);
    // if (newComponent == null) {
    // continue;
    // }
    // Map<String, Object> parameters = new HashMap<String, Object>();
    // node.reloadComponent(newComponent, parameters);
    // }
    // }
    //
    // } else if (propertyName.equals(ComponentUtilities.JOBLET_SCHEMA_CHANGED)) {
    // updateGraphicalNodesSchema(evt);
    // }
    // // else if (propertyName.equals(ComponentUtilities.JOBLET_INPUTOUTPUT_REDUCE)) {
    // // reduceGraphicalConnections(evt);
    // // }
    // process.setProcessModified(true);
    //
    // }
    /**
     * DOC qzhang Comment method "reduceGraphicalConnections".
     * 
     * @param evt
     */
    // private void reduceGraphicalConnections(PropertyChangeEvent evt) {
    // String oldName = ((IProcess) evt.getSource()).getName();
    // if (process.getName().equals(oldName)) {
    // return;
    // }
    // Object[] oldMetadataTables = (Object[]) evt.getOldValue();
    // List<IMetadataTable> oldInputTableList = (List<IMetadataTable>) oldMetadataTables[0];
    // List<IMetadataTable> oldOutputTableList = (List<IMetadataTable>) oldMetadataTables[1];
    //
    // Object[] newMetadataTables = (Object[]) evt.getNewValue();
    // List<IMetadataTable> newInputTableList = (List<IMetadataTable>) newMetadataTables[0];
    // List<IMetadataTable> newOutputTableList = (List<IMetadataTable>) newMetadataTables[1];
    //
    // List<String> removedInputcons = new ArrayList<String>();
    // List<String> removedOutputcons = new ArrayList<String>();
    //
    // for (int i = 0; i < oldInputTableList.size(); i++) {
    // IMetadataTable newTable = oldInputTableList.get(i);
    // String attachedConnector = newTable.getAttachedConnector();
    // String tableName = newTable.getTableName();
    // boolean isRemove = true;
    // for (IMetadataTable table : newInputTableList) {
    // if (attachedConnector.equals(table.getAttachedConnector()) && tableName.equals(table.getTableName())) {
    // isRemove = false;
    // break;
    // }
    // }
    // if (isRemove) {
    // removedInputcons.add(attachedConnector);
    // }
    // }
    //
    // for (int i = 0; i < oldOutputTableList.size(); i++) {
    // IMetadataTable newTable = oldOutputTableList.get(i);
    // String attachedConnector = newTable.getAttachedConnector();
    // String tableName = newTable.getTableName();
    // boolean isRemove = true;
    // for (IMetadataTable table : newOutputTableList) {
    // if (attachedConnector.equals(table.getAttachedConnector()) && tableName.equals(table.getTableName())) {
    // isRemove = false;
    // break;
    // }
    // }
    // if (isRemove) {
    // removedOutputcons.add(tableName);
    // }
    // }
    //
    // boolean isChanged = false;
    // for (Node node : (List<Node>) process.getGraphicalNodes()) {
    // if (node.getComponent().getName().equals(oldName) || node.getLabel().contains(oldName)) {
    // IComponent newComponent = components.get(oldName);
    // if (newComponent == null) {
    // continue;
    // }
    // List<? extends IConnection> incomingConnections = node.getIncomingConnections();
    // for (int i = 0; i < incomingConnections.size(); i++) {
    // IConnection connection = incomingConnections.get(i);
    // IMetadataTable metaTable = connection.getMetadataTable();
    // String string = metaTable.getAttachedConnector();
    // if (removedInputcons.contains(string)) {
    // ((org.talend.designer.core.ui.editor.connections.Connection) connection).disconnect();
    // isChanged = true;
    // }
    // }
    //
    // List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
    // for (int i = 0; i < outgoingConnections.size(); i++) {
    // IConnection connection = outgoingConnections.get(i);
    // IMetadataTable metaTable = connection.getMetadataTable();
    // if (metaTable != null) {
    // String string = metaTable.getAttachedConnector();
    // if (removedOutputcons.contains(string)) {
    // ((org.talend.designer.core.ui.editor.connections.Connection) connection).disconnect();
    // isChanged = true;
    // }
    // }
    // }
    // }
    // }
    // if (isChanged) {
    // getCommandStack().execute(new Command() {
    // });
    // }
    // }
    /**
     * DOC qzhang Comment method "updateGraphicalNodesSchema".
     * 
     * have moved to UpdateManager.
     * 
     * @param evt
     */
    // private void updateGraphicalNodesSchema(PropertyChangeEvent evt) {
    // String oldName = ((IProcess) evt.getSource()).getName();
    // Object[] newMetadataTables = (Object[]) evt.getNewValue();
    // List<IMetadataTable> newInputTableList = (List<IMetadataTable>) newMetadataTables[0];
    //
    // List<IMetadataTable> newOutputTableList = (List<IMetadataTable>) newMetadataTables[1];
    //
    // for (Node node : (List<Node>) process.getGraphicalNodes()) {
    // if (node.getComponent().getName().equals(oldName) || node.getLabel().contains(oldName)) {
    // IComponent newComponent = components.get(oldName);
    // if (newComponent == null) {
    // continue;
    // }
    // List<IElementParameter> outputElemParams = new ArrayList<IElementParameter>();
    //
    // IElementParameter outputElemParam = null;
    //
    // List<? extends IElementParameter> elementParameters = node.getElementParameters();
    // for (IElementParameter elementParameter : elementParameters) {
    // if (EParameterFieldType.SCHEMA_TYPE.equals(elementParameter.getField())) {
    // outputElemParams.add(elementParameter);
    // }
    // }
    // ChangeMetadataCommand command;
    // List<? extends IConnection> incomingConnections = node.getIncomingConnections();
    // for (int i = 0; i < incomingConnections.size(); i++) {
    // IConnection connection = incomingConnections.get(i);
    // Node source = (Node) connection.getSource();
    // IMetadataTable metadataTable = connection.getMetadataTable();
    // IMetadataTable newInputMetadataTable = getNewInputTableForConnection(newInputTableList, metadataTable
    // .getAttachedConnector());
    // if (newInputMetadataTable != null && !metadataTable.sameMetadataAs(newInputMetadataTable)) {
    // IElementParameter elementParam = source.getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE);
    // command = new ChangeMetadataCommand(source, elementParam, metadataTable, newInputMetadataTable);
    // command.execute(Boolean.FALSE);
    // }
    // }
    // List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
    // for (int i = 0; i < outgoingConnections.size(); i++) {
    // IConnection connection = outgoingConnections.get(i);
    // Node target = (Node) connection.getTarget();
    // IMetadataTable metadataTable = connection.getMetadataTable();
    // if (metadataTable != null) {
    // IMetadataTable newOutputMetadataTable = getNewOutputTableForConnection(newOutputTableList, metadataTable
    // .getAttachedConnector());
    // if (newOutputMetadataTable != null && !metadataTable.sameMetadataAs(newOutputMetadataTable)) {
    // IElementParameter elementParam = target.getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE);
    // command = new ChangeMetadataCommand(target, elementParam, target
    // .getMetadataFromConnector(metadataTable.getAttachedConnector()), newOutputMetadataTable);
    // command.execute(Boolean.FALSE);
    // }
    // }
    // }
    //
    // List<IMetadataTable> metadataList = node.getMetadataList();
    // for (IMetadataTable metadataTable : metadataList) {
    // IMetadataTable newInputMetadataTable = getNewInputTableForConnection(newInputTableList, metadataTable
    // .getAttachedConnector());
    // IMetadataTable newOutputMetadataTable = getNewOutputTableForConnection(newOutputTableList, metadataTable
    // .getAttachedConnector());
    // outputElemParam = getElemParam(outputElemParams, metadataTable.getAttachedConnector());
    //
    // if (outputElemParam != null && newInputMetadataTable != null) {
    // command = new ChangeMetadataCommand(node, outputElemParam, (IMetadataTable) outputElemParam.getValue(),
    // newInputMetadataTable);
    // command.execute(Boolean.FALSE);
    // IMetadataTable metadataFromConnector = node.getMetadataFromConnector(outputElemParam.getContext());
    // MetadataTool.copyTable(newInputMetadataTable, metadataFromConnector);
    // } else if (outputElemParam != null && newOutputMetadataTable != null) {
    // command = new ChangeMetadataCommand(node, outputElemParam, (IMetadataTable) outputElemParam.getValue(),
    // newOutputMetadataTable);
    // command.execute(Boolean.FALSE);
    // IMetadataTable metadataFromConnector = node.getMetadataFromConnector(outputElemParam.getContext());
    // MetadataTool.copyTable(newOutputMetadataTable, metadataFromConnector);
    // }
    // }
    //
    // getCommandStack().execute(new Command() {
    // });
    // }
    // }
    // }
    /**
     * DOC qzhang Comment method "getNewOutputTableForConnection".
     * 
     * @param newOutputTableList
     * @param attachedConnector
     * @return
     */
    // private IMetadataTable getNewOutputTableForConnection(List<IMetadataTable> newOutputTableList, String tableName)
    // {
    // for (IMetadataTable metadataTable : newOutputTableList) {
    // if (tableName != null && tableName.equals(metadataTable.getTableName())
    // || tableName.equals(metadataTable.getTableName())) {
    // return metadataTable;
    // }
    // }
    // return null;
    // }
    /**
     * DOC qzhang Comment method "getElemParam".
     * 
     * @param elemParams
     * @param string
     * 
     * @return
     */
    // private IElementParameter getElemParam(List<IElementParameter> elemParams, String string) {
    // for (IElementParameter elementParameter : elemParams) {
    // if (string != null && string.equals(elementParameter.getContext())) {
    // return elementParameter;
    // }
    // }
    // return null;
    // }
    /**
     * DOC qzhang Comment method "getNewInputTableForConnection".
     * 
     * @param newInputTableList
     * @param connector
     * 
     * @return
     */
    // private IMetadataTable getNewInputTableForConnection(List<IMetadataTable> newInputTableList, String connector) {
    // for (IMetadataTable metadataTable : newInputTableList) {
    // if (connector != null
    // && (connector.equals(metadataTable.getAttachedConnector()) || connector.equals(metadataTable.getTableName()))) {
    // return metadataTable;
    // }
    // }
    // return null;
    // }
    protected AbstractMultiPageTalendEditor parent;

    protected boolean savePreviouslyNeeded;

    protected IProcess2 process;

    private RulerComposite rulerComp;

    protected IComponentsFactory components;

    protected Property property;

    protected boolean readOnly;

    public static final int GRID_SIZE = 32;

    private boolean dirtyState = false;

    protected JobResource currentJobResource;

    // protected final String projectName;

    protected Project rootProject;

    protected final Map<String, JobResource> protectedJobs;

    /** The verify key listener for activation code triggering. */
    public ActivationCodeTrigger fActivationCodeTrigger = new ActivationCodeTrigger();

    public AbstractTalendEditor() {
        this(false);

    }

    public AbstractTalendEditor(boolean readOnly) {

        ProcessorUtilities.addOpenEditor(this);

        // an EditDomain is a "session" of editing which contains things
        // like the CommandStack
        setEditDomain(new TalendEditDomain(this));
        this.readOnly = readOnly;

        // projectName = ((RepositoryContext)
        // CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject()
        // .getLabel();

        currentJobResource = new JobResource();
        protectedJobs = new HashMap<String, JobResource>();
        initializeKeyBindingScopes();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        monitor.beginTask("begin save job...", 100); //$NON-NLS-1$
        monitor.worked(10);
        savePreviewPictures();

        try {
            if (getEditorInput() instanceof JobEditorInput) {
                boolean saved = ((JobEditorInput) getEditorInput()).saveProcess(new SubProgressMonitor(monitor, 80), null);
                if (!saved) {
                    monitor.setCanceled(true);
                    throw new InterruptedException();
                }
            }

            getCommandStack().markSaveLocation();
            setDirty(false);

            // / See bug 4821
            ((ILibrariesService) GlobalServiceRegister.getDefault().getService(ILibrariesService.class))
                    .updateModulesNeededForCurrentJob(this.process);

            monitor.worked(10);

        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
            monitor.setCanceled(true);
        } finally {
            monitor.done();
        }

    }

    @Override
    protected FlyoutPreferences getPalettePreferences() {
        return TalendEditorPaletteFactory.createPalettePreferences();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#setFocus()
     */
    @Override
    public void setFocus() {
        IComponentSettingsView viewer = (IComponentSettingsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().findView(IComponentSettingsView.ID);
        if (viewer != null) {
            IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();

            if (selection.size() == 1
                    && (selection.getFirstElement() instanceof NodePart || selection.getFirstElement() instanceof ConnectionPart)) {

                viewer.setElement((Element) ((AbstractEditPart) selection.getFirstElement()).getModel());

            } else {
                viewer.cleanDisplay();
            }
        }

        JobSettings.switchToCurJobSettingsView();

        super.setFocus();

        if (!readOnly) {
            // When gain focus, check read-only to disable read-only mode if process has been restore while opened :
            // 1. Enabled/disabled components :

            // process.checkReadOnly();

            // 2. Set backgroung color :
            List children = getViewer().getRootEditPart().getChildren();
            if (!children.isEmpty()) {
                ProcessPart rep = (ProcessPart) children.get(0);
                rep.ajustReadOnly();
            }
        }
    }

    protected String[] fKeyBindingScopes;

    /**
     * Sets the key binding scopes for this editor.
     * 
     * @param scopes a non-empty array of key binding scope identifiers
     * @since 2.1
     */
    protected void setKeyBindingScopes(String[] scopes) {
        Assert.isTrue(scopes != null && scopes.length > 0);
        fKeyBindingScopes = scopes;
    }

    // ------------------------------------------------------------------------
    // Overridden from EditorPart

    @Override
    protected void setInput(final IEditorInput input) {
        super.setInput(input);

        try {
            if (input instanceof JobEditorInput) {
                process = ((JobEditorInput) input).getLoadedProcess();
                property = ((JobEditorInput) input).getItem().getProperty();
                rootProject = CorePlugin.getDefault().getProxyRepositoryFactory().getProject(
                        ((RepositoryEditorInput) input).getItem());
            }
        } catch (Exception e) {
            MessageBoxExceptionHandler.process(e);
            return;
        }

        if (property.getItem() instanceof ProcessItem) {
            currentJobResource.setJobInfo(new JobInfo((ProcessItem) property.getItem(), process.getContextManager()
                    .getDefaultContext().getName()));
            currentJobResource.setProjectName(rootProject.getLabel());

            JobResourceManager.getInstance().addProtection(this);
        }
    }

    /*
     * @see ITextEditor#setAction(String, IAction)
     */
    public void setAction(String actionID, IAction action) {
        Assert.isNotNull(actionID);
        if (action == null) {
            action = getActionRegistry().getAction(actionID);
            if (action != null) {
                fActivationCodeTrigger.unregisterActionFromKeyActivation(action);
            }
        } else {
            // getActionRegistry().registerAction(action);
            fActivationCodeTrigger.registerActionForKeyActivation(action);
        }
    }

    /**
     * Initializes the key binding scopes of this editor.
     */
    protected void initializeKeyBindingScopes() {
        setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" }); //$NON-NLS-1$
    }

    /**
     * Internal key verify listener for triggering action activation codes.
     */
    protected class ActivationCodeTrigger implements VerifyKeyListener {

        /** Indicates whether this trigger has been installed. */
        private boolean fIsInstalled = false;

        /**
         * The key binding service to use.
         * 
         * @since 2.0
         */
        private IKeyBindingService fKeyBindingService;

        /*
         * @see VerifyKeyListener#verifyKey(org.eclipse.swt.events.VerifyEvent)
         */
        public void verifyKey(VerifyEvent event) {

            // ActionActivationCode code = null;
            // int size = fActivationCodes.size();
            // for (int i = 0; i < size; i++) {
            // code = (ActionActivationCode) fActivationCodes.get(i);
            // if (code.matches(event)) {
            // IAction action = getAction(code.fActionId);
            // if (action != null) {
            //
            // if (action instanceof IUpdate)
            // ((IUpdate) action).update();
            //
            // if (!action.isEnabled() && action instanceof IReadOnlyDependent) {
            // IReadOnlyDependent dependent = (IReadOnlyDependent) action;
            // boolean writable = dependent.isEnabled(true);
            // if (writable) {
            // event.doit = false;
            // return;
            // }
            // } else if (action.isEnabled()) {
            // event.doit = false;
            // action.run();
            // return;
            // }
            // }
            // }
            // }
        }

        /**
         * Installs this trigger on the editor's text widget.
         * 
         * @since 2.0
         */
        public void install() {
            if (!fIsInstalled) {

                // if (fSourceViewer instanceof ITextViewerExtension) {
                // ITextViewerExtension e = (ITextViewerExtension) fSourceViewer;
                // e.prependVerifyKeyListener(this);
                // } else {
                // StyledText text = fSourceViewer.getTextWidget();
                // text.addVerifyKeyListener(this);
                // }

                fKeyBindingService = getEditorSite().getKeyBindingService();
                fIsInstalled = true;
            }
        }

        /**
         * Uninstalls this trigger from the editor's text widget.
         * 
         * @since 2.0
         */
        public void uninstall() {
            if (fIsInstalled) {

                // if (fSourceViewer instanceof ITextViewerExtension) {
                // ITextViewerExtension e = (ITextViewerExtension) fSourceViewer;
                // e.removeVerifyKeyListener(this);
                // } else if (fSourceViewer != null) {
                // StyledText text = fSourceViewer.getTextWidget();
                // if (text != null && !text.isDisposed())
                // text.removeVerifyKeyListener(fActivationCodeTrigger);
                // }

                fIsInstalled = false;
                fKeyBindingService = null;
            }
        }

        /**
         * Registers the given action for key activation.
         * 
         * @param action the action to be registered
         * @since 2.0
         */
        public void registerActionForKeyActivation(IAction action) {
            if (action.getActionDefinitionId() != null) {
                fKeyBindingService.registerAction(action);
            }
        }

        /**
         * The given action is no longer available for key activation
         * 
         * @param action the action to be unregistered
         * @since 2.0
         */
        public void unregisterActionFromKeyActivation(IAction action) {
            if (action.getActionDefinitionId() != null) {
                fKeyBindingService.unregisterAction(action);
            }
        }

        /**
         * Sets the key binding scopes for this editor.
         * 
         * @param keyBindingScopes the key binding scopes
         * @since 2.1
         */
        public void setScopes(String[] keyBindingScopes) {
            if (keyBindingScopes != null && keyBindingScopes.length > 0) {
                fKeyBindingService.setScopes(keyBindingScopes);
            }
        }
    }

    /**
     * Representation of action activation codes.
     */
    protected static class ActionActivationCode {

        /** The action id. */
        public String fActionId;

        /** The character. */
        public char fCharacter;

        /** The key code. */
        public int fKeyCode = -1;

        /** The state mask. */
        public int fStateMask = SWT.DEFAULT;

        /**
         * Creates a new action activation code for the given action id.
         * 
         * @param actionId the action id
         */
        public ActionActivationCode(String actionId) {
            fActionId = actionId;
        }

        /**
         * Returns <code>true</code> if this activation code matches the given verify event.
         * 
         * @param event the event to test for matching
         * @return whether this activation code matches <code>event</code>
         */
        public boolean matches(VerifyEvent event) {
            return (event.character == fCharacter && (fKeyCode == -1 || event.keyCode == fKeyCode) && (fStateMask == SWT.DEFAULT || event.stateMask == fStateMask));
        }
    }

    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        /** * Manage the view in the Outline ** */
        ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer) getGraphicalViewer();
        viewer.setSelectionManager(new TalendSelectionManager());

        TalendScalableFreeformRootEditPart root = new TalendScalableFreeformRootEditPart(getEditorInput());

        List<String> zoomLevels = new ArrayList<String>();
        zoomLevels.add(ZoomManager.FIT_ALL);
        zoomLevels.add(ZoomManager.FIT_WIDTH);
        zoomLevels.add(ZoomManager.FIT_HEIGHT);
        root.getZoomManager().setZoomLevelContributions(zoomLevels);
        // root.getZoomManager().setZoomAnimationStyle(ZoomManager.ANIMATE_NEVER);

        IAction zoomIn = new ZoomInAction(root.getZoomManager());
        IAction zoomOut = new ZoomOutAction(root.getZoomManager());
        getActionRegistry().registerAction(zoomIn);
        getActionRegistry().registerAction(zoomOut);
        IHandlerService service = (IHandlerService) getEditorSite().getService(IHandlerService.class);
        service.activateHandler(zoomIn.getActionDefinitionId(), new ActionHandler(zoomIn));

        service.activateHandler(zoomOut.getActionDefinitionId(), new ActionHandler(zoomOut));

        IAction directEditAction = new DirectEditAction(this);
        getActionRegistry().registerAction(directEditAction);
        getSelectionActions().add(directEditAction.getId());

        IAction copyAction = new GEFCopyAction(this);
        getActionRegistry().registerAction(copyAction);
        getSelectionActions().add(copyAction.getId());
        // setAction(copyAction.getId(), copyAction);

        IAction pasteAction = new GEFPasteAction(this);
        getActionRegistry().registerAction(pasteAction);
        getSelectionActions().add(pasteAction.getId());
        // setAction(pasteAction.getId(), pasteAction);

        IAction deleteAction = new GEFDeleteAction(this);
        getActionRegistry().registerAction(deleteAction);
        getSelectionActions().add(deleteAction.getId());
        // setAction(deleteAction.getId(), deleteAction);

        IAction setRefAction = new ConnectionSetAsMainRef(this);
        getActionRegistry().registerAction(setRefAction);
        getSelectionActions().add(setRefAction.getId());

        IAction modifyMergeAction = new ModifyMergeOrderAction(this);
        getActionRegistry().registerAction(modifyMergeAction);
        getSelectionActions().add(modifyMergeAction.getId());

        IAction modifyOutputOrderAction = new ModifyOutputOrderAction(this);
        getActionRegistry().registerAction(modifyOutputOrderAction);
        getSelectionActions().add(modifyOutputOrderAction.getId());

        List<IAction> calcOrderActions = ModifyConnectionOrderAction.getOrderActions(this);
        for (IAction orderAction : calcOrderActions) {
            getActionRegistry().registerAction(orderAction);
            getSelectionActions().add(orderAction.getId());
        }

        viewer.setRootEditPart(root);

        PartFactory partFactory = new PartFactory();
        // set the factory to use for creating EditParts for elements in the model
        getGraphicalViewer().setEditPartFactory(partFactory);
        getGraphicalViewer().setKeyHandler(new NodePartKeyHander(getGraphicalViewer()).setParent(getCommonKeyHandler()));
        // GraphicalViewer graViewer = getGraphicalViewer();
        // graViewer.setKeyHandler(new GraphicalViewerKeyHandler(graViewer));
        initializeActivationCodeTrigger();

        /** * Management of the context menu ** */

        ContextMenuProvider cmProvider = new TalendEditorContextMenuProvider(this, viewer, getActionRegistry());
        viewer.setContextMenu(cmProvider);

        /** * Management of the Zoom ** */
        /*
         * ZoomManager manager = (ZoomManager) getGraphicalViewer().getProperty(ZoomManager.class.toString()); if
         * (manager != null) { manager.setZoom(getProcess().getZoom()); }
         */
        // Scroll-wheel Zoom
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);

        /** * Snap To Grid ** */
        // Grid properties
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING,
                new Dimension(AbstractTalendEditor.GRID_SIZE, AbstractTalendEditor.GRID_SIZE));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, new Boolean(true/*
                                                                                            * getProcess().isGridEnabled(
                                                                                            * )
                                                                                            */));
        // We keep grid visibility and enablement in sync
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, new Boolean(true/*
                                                                                            * getProcess().isGridEnabled(
                                                                                            * )
                                                                                            */));
        IAction showGrid = new ToggleGridAction(getGraphicalViewer());
        getActionRegistry().registerAction(showGrid);

        /** * Snap To Geometry ** */
        getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, new Boolean(false/*
                                                                                                 * getProcess().isSnapToGeometryEnabled
                                                                                                 * ()
                                                                                                 */));
        IAction snapAction = new ToggleSnapToGeometryAction(getGraphicalViewer());
        getActionRegistry().registerAction(snapAction);

        // toggle subjobs action
        IAction toggleSubjobsAction = ToggleSubjobsAction.getDefault();
        getActionRegistry().registerAction(toggleSubjobsAction);

        for (Iterator iterator = getSelectionActions().iterator(); iterator.hasNext();) {
            String actionID = (String) iterator.next();
            IAction action = getActionRegistry().getAction(actionID);
            setAction(actionID, action);
        }
    }

    /**
     * Initializes the activation code trigger.
     * 
     * @since 2.1
     */
    public void initializeActivationCodeTrigger() {
        fActivationCodeTrigger.install();
        fActivationCodeTrigger.setScopes(fKeyBindingScopes);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public IProcess2 getProcess() {
        return this.process;
    }

    public Property getProperty() {
        return this.property;
    }

    /**
     * Sets the property.
     * 
     * @param property the property to set
     */
    public void setProperty(Property property) {
        this.property = property;
    }

    @SuppressWarnings("unchecked")
    public List<String> getActions() {
        return getSelectionActions();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#getActionRegistry()
     */
    @Override
    public ActionRegistry getActionRegistry() {
        return super.getActionRegistry();
    }

    /**
     * Save the outline picture for this editor.
     * 
     * @param viewer
     */
    protected void saveOutlinePicture(ScrollingGraphicalViewer viewer) {
        GlobalConstant.generatingScreenShoot = true;
        LayerManager layerManager = (LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID);
        // save image using swt
        // get root figure
        IFigure backgroundLayer = layerManager.getLayer(LayerConstants.GRID_LAYER);
        IFigure contentLayer = layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);
        // create image from root figure
        Image img = new Image(null, contentLayer.getSize().width, contentLayer.getSize().height);
        GC gc = new GC(img);
        Graphics graphics = new SWTGraphics(gc);
        Point point = contentLayer.getBounds().getTopLeft();
        graphics.translate(-point.x, -point.y);
        process.setPropertyValue(IProcess.SCREEN_OFFSET_X, String.valueOf(-point.x));
        process.setPropertyValue(IProcess.SCREEN_OFFSET_Y, String.valueOf(-point.y));
        backgroundLayer.paint(graphics);
        contentLayer.paint(graphics);
        graphics.dispose();
        gc.dispose();
        // add by hshen
        ImageDescriptor imagedes = ImageDescriptor.createFromImage(img);
        process.setScreenshot(imagedes);
        // IRepositoryService service = CorePlugin.getDefault().getRepositoryService();
        // String outlinePicturePath = getOutlinePicturePath();
        // ImageLoader il = new ImageLoader();
        // il.data = new ImageData[] { img.getImageData() };
        //        IPath filePath = service.getPathFileName(outlinePicturePath, ""); //$NON-NLS-1$
        // String outlineFileName = process.getName();
        // String outlineFileVersion = process.getVersion();
        //        filePath = filePath.append(outlineFileName + "_" + outlineFileVersion + "_old.png"); //$NON-NLS-1$ //$NON-NLS-2$
        // filePath.toFile().deleteOnExit();
        // il.save(filePath.toPortableString(), SWT.IMAGE_PNG);
        // ImageUtils.save(img, "e:/aa", 5);
        img.dispose();

        // service.getProxyRepositoryFactory().refreshJobPictureFolder(outlinePicturePath);
        GlobalConstant.generatingScreenShoot = false;
    }

    /**
     * DOC qzhang Comment method "getOutlinePicturePath".
     * 
     * @return
     */
    protected String getOutlinePicturePath() {
        return RepositoryConstants.IMG_DIRECTORY_OF_JOB_OUTLINE;
    }

    @Override
    protected Control getGraphicalControl() {
        return rulerComp;
    }

    @Override
    protected void createGraphicalViewer(final Composite parent) {
        rulerComp = new RulerComposite(parent, SWT.NONE);
        super.createGraphicalViewer(rulerComp);
        rulerComp.setGraphicalViewer((ScrollingGraphicalViewer) getGraphicalViewer());
    }

    @Override
    public void commandStackChanged(final EventObject event) {
        if (isDirty()) {
            if (!this.savePreviouslyNeeded) {
                this.savePreviouslyNeeded = true;
                firePropertyChange(IEditorPart.PROP_DIRTY);
            }
        } else {
            savePreviouslyNeeded = false;
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
        super.commandStackChanged(event);
    }

    /**
     * This function is used only for the TabbedPropertySheetPage.
     * 
     * @return contributorId String
     */
    public String getContributorId() {
        return "org.talend.repository.views.repository"; //$NON-NLS-1$
    }

    /**
     * ftang Comment method "savePreviewPictures".
     */
    protected void savePreviewPictures() {
        // getGraphicalViewer().getControl().getDisplay().asyncExec(new Runnable() {
        //
        // public void run() {
        saveOutlinePicture((ScrollingGraphicalViewer) getGraphicalViewer());
        // }
        //
        // });
    }

    protected RepositoryEditorInput getRepositoryEditorInput() {
        return (RepositoryEditorInput) getEditorInput();
    }

    @Override
    public void doSaveAs() {
        SaveAsDialog dialog = new SaveAsDialog(getSite().getWorkbenchWindow().getShell());
        dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
        dialog.open();
        IPath path = dialog.getResult();

        if (path == null) {
            return;
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IFile file = workspace.getRoot().getFile(path);

        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

            @Override
            public void execute(final IProgressMonitor monitor) throws CoreException {
                try {
                    savePreviewPictures();
                    process.saveXmlFile();
                    // file.refreshLocal(IResource.DEPTH_ONE, monitor);
                } catch (Exception e) {
                    // e.printStackTrace();
                    ExceptionHandler.process(e);
                }
            }
        };

        try {
            new ProgressMonitorDialog(getSite().getWorkbenchWindow().getShell()).run(false, true, op);
            // setInput(new FileEditorInput((IFile) file));
            getCommandStack().markSaveLocation();
            setDirty(false);

        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
    }

    @Override
    public void dispose() {
        ProcessorUtilities.editorClosed(this);
        talendPaletteViewerProvider = null;

        // achen modify to fix bug 0006107
        // if (!getParent().isKeepPropertyLocked()) {
        // JobResourceManager manager = JobResourceManager.getInstance();
        // manager.removeProtection(this);
        // for (JobResource r : protectedJobs.values()) {
        // manager.deleteResource(r);
        // }
        // }
        ComponentSettingsView viewer = (ComponentSettingsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().findView(ComponentSettingsView.ID);
        if (viewer != null) {
            viewer.cleanDisplay();
        }

        for (Iterator iterator = getSelectionActions().iterator(); iterator.hasNext();) {
            String actionID = (String) iterator.next();
            IAction action = getActionRegistry().getAction(actionID);
            if (action != null) {
                fActivationCodeTrigger.unregisterActionFromKeyActivation(action);
                getActionRegistry().removeAction(action);
                if (action instanceof Disposable) {
                    ((Disposable) action).dispose();
                }
            }
        }
        fActivationCodeTrigger.uninstall();
        fActivationCodeTrigger = null;
        getSelectionActions().clear();

        getGraphicalViewer().removeDropTargetListener(talendEditorDropTargetListener);

        if (getGraphicalViewer().getContents() != null) {
            getGraphicalViewer().getContents().deactivate();
            getGraphicalViewer().getContents().removeNotify();
            getGraphicalViewer().getRootEditPart().deactivate();
            getGraphicalViewer().getRootEditPart().removeNotify();
        }
        getGraphicalViewer().setEditPartFactory(null);
        getGraphicalViewer().setContextMenu(null);
        getGraphicalViewer().setContents(null);

        // rulerComp.dispose();

        if (sharedKeyHandler != null) {
            sharedKeyHandler.remove(KeyStroke.getPressed(SWT.F1, 0));
            sharedKeyHandler.remove(KeyStroke.getPressed(SWT.DEL, 0));
        }

        super.setInput(null);

        // getGraphicalViewer().setContents(null);
        // if (getGraphicalViewer().getControl() != null && !getGraphicalViewer().getControl().isDisposed()) {
        // getGraphicalViewer().getControl().dispose();
        // }

        talendEditorDropTargetListener.setEditor(null);
        talendEditorDropTargetListener = null;
        // TalendScalableFreeformRootEditPart rootEditPart = (TalendScalableFreeformRootEditPart) getGraphicalViewer()
        // .getRootEditPart();
        // rootEditPart.setEditorInput(null);
        // rootEditPart.deactivate();

        super.dispose();
        if (!getParent().isKeepPropertyLocked()) {
            ((Process) process).dispose();
        }
        process = null;
        parent = null;

        getEditDomain().getCommandStack().dispose();
        getEditDomain().setActiveTool(null);
        getEditDomain().setPaletteRoot(null);
        getEditDomain().setPaletteViewer(null);
        getEditDomain().setCommandStack(null);
        getEditDomain().setDefaultTool(null);
        getSelectionSynchronizer().removeViewer(getGraphicalViewer());
        getSite().setSelectionProvider(null);
    }

    public void gotoMarker(final IMarker marker) {
    }

    @Override
    public boolean isDirty() {
        // rely on the command stack to determine dirty flag
        return dirtyState || getCommandStack().isDirty();
    }

    public void setDirty(boolean dirty) {
        dirtyState = dirty;
        if (dirtyState) {
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    @Override
    public boolean isSaveAsAllowed() {
        // allow Save As
        return true;
    }

    TalendPaletteViewerProvider talendPaletteViewerProvider;

    @Override
    protected PaletteViewerProvider createPaletteViewerProvider() {
        if (talendPaletteViewerProvider == null) {
            talendPaletteViewerProvider = new TalendPaletteViewerProvider(getEditDomain());
        }
        return talendPaletteViewerProvider;
    }

    public IComponent getComponent(String name) {
        if (components == null) {
            components = ComponentsFactoryProvider.getInstance();
        }
        return components.get(name);
    }

    public ProcessPart getProcessPart() {
        RootEditPart rootEditPart = ((ScrollingGraphicalViewer) getGraphicalViewer()).getRootEditPart();
        return (ProcessPart) rootEditPart.getChildren().get(0);
    }

    /**
     * Outline view page. <br/>
     * 
     * $Id: TalendEditor.java 7516 2007-12-11 05:23:18Z ftang $
     * 
     */

    /**
     * This function will allow the use of the Delete in the Multipage Editor.
     */
    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        super.selectionChanged(part, selection);
        if (this.equals(part)) { // Propagated from MyMultiPageEditor.
            updateActions(getSelectionActions());
        }
    }

    /**
     * Get the viewer in the editor.
     * 
     * @return
     */
    public GraphicalViewer getViewer() {
        return getGraphicalViewer();
    }

    public AbstractMultiPageTalendEditor getParent() {
        return this.parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.job.deletion.IResourceProtection#getProtectedIds()
     */
    public String[] calculateProtectedIds() {
        if (!(process.getProperty().getItem() instanceof ProcessItem)) {
            return new String[] {};
        }
        // achen modify to fix bug 6462
        // Set<JobInfo> subJobs = ProcessorUtilities.getChildrenJobInfo((ProcessItem) process.getProperty().getItem());

        String projectName = rootProject.getLabel();
        // for (JobInfo jobInfo : subJobs) {
        //            String protectedJob = "subjob_of_" + process.getLabel() + "_" + //$NON-NLS-1$ //$NON-NLS-2$ 
        //                    projectName + "_" + jobInfo.getJobName() + "_" + jobInfo.getJobVersion(); //$NON-NLS-1$ //$NON-NLS-2$
        // protectedJobs.put(protectedJob, new JobResource(projectName, jobInfo));
        // }
        String currentJobId = "talend_editor_" + projectName + "_" + process.getLabel() //$NON-NLS-1$ //$NON-NLS-2$
                + "_" + process.getVersion(); //$NON-NLS-1$
        protectedJobs.put(currentJobId, currentJobResource);

        Set<String> set = protectedJobs.keySet();

        return set.toArray(new String[set.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.job.deletion.IResourceProtection#getProjectedIds()
     */
    public String[] getProtectedIds() {
        Set<String> set = protectedJobs.keySet();
        return set.toArray(new String[set.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.job.deletion.IResourceProtection#getJobResource()
     */
    public JobResource getJobResource(String id) {
        return protectedJobs.get(id);
    }

    /**
     * Remove all the protected resources.
     * 
     * yzhang Comment method "resetJobResources".
     */
    public void resetJobResources() {
        String[] ids = getProtectedIds();
        for (String id : ids) {
            protectedJobs.remove(id);
        }
    }

    /**
     * Getter for currentJobResource.
     * 
     * @return the currentJobResource
     */
    public JobResource getCurrentJobResource() {
        return this.currentJobResource;
    }

    /**
     * bqian class global comment. Detailled comment <br/>
     * 
     * $Id: TalendEditor.java 7860 2008-01-02 06:56:40Z qzhang $
     * 
     */
    class TalendEditDomain extends DefaultEditDomain {

        /**
         * DOC bqian TalendEditor.TalendEditDomain class global comment. Detailled comment <br/>
         * 
         * $Id: TalendEditor.java 7860 2008-01-02 06:56:40Z qzhang $
         * 
         */
        class DragProcessor {

            int x;

            int y;

        }

        private DragProcessor processor = null;

        private boolean createConnection = false;

        private static final int DEFAULT_MOVE_OFFSET = GRID_SIZE;

        private Point startPoint = null;

        private int moveOffset = DEFAULT_MOVE_OFFSET;

        /**
         * bqian TalendEditDomain constructor comment.
         * 
         * @param editorPart
         */
        public TalendEditDomain(IEditorPart editorPart) {
            super(editorPart);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.gef.EditDomain#mouseDown(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
         */
        @Override
        public void mouseDown(org.eclipse.swt.events.MouseEvent mouseEvent, EditPartViewer viewer) {
            TalendEditorContextMenuProvider.setEnableContextMenu(true);
            createConnection = false;
            if (viewer.getSelectionManager() instanceof TalendSelectionManager) {
                Point point = new Point(mouseEvent.x, mouseEvent.y);
                ((TalendSelectionManager) viewer.getSelectionManager()).setSelectPoint(point);
            }
            if (mouseEvent.button == 2) {
                getEditor().setCursor(Cursors.HAND);
                processor = new DragProcessor();
                processor.x = mouseEvent.x;
                processor.y = mouseEvent.y;
            } else if (mouseEvent.button == 3) {
                startPoint = new Point(mouseEvent.x, mouseEvent.y);

                RootEditPart rep = getGraphicalViewer().getRootEditPart();
                if (rep instanceof ScalableFreeformRootEditPart) {
                    ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
                    Viewport viewport = (Viewport) root.getFigure();
                    Point viewOriginalPosition = viewport.getViewLocation();
                    startPoint.translate(viewOriginalPosition);
                }
                createConnection = true;
                super.mouseDown(mouseEvent, viewer);
            } else {
                super.mouseDown(mouseEvent, viewer);
            }
        }

        public void updateViewport(int offX, int offY) {
            RootEditPart rep = getGraphicalViewer().getRootEditPart();

            if (rep instanceof ScalableFreeformRootEditPart) {
                ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
                Viewport viewport = (Viewport) root.getFigure();
                Point viewOriginalPosition = viewport.getViewLocation();
                viewOriginalPosition.x -= offX;
                viewOriginalPosition.y -= offY;

                viewport.setViewLocation(viewOriginalPosition.x, viewOriginalPosition.y);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.gef.EditDomain#mouseDrag(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
         */
        @Override
        public void mouseDrag(org.eclipse.swt.events.MouseEvent mouseEvent, EditPartViewer viewer) {
            super.mouseDrag(mouseEvent, viewer);
            if (processor != null) {
                int offX = mouseEvent.x - processor.x;
                int offY = mouseEvent.y - processor.y;

                updateViewport(offX, offY);
                processor.x = mouseEvent.x;
                processor.y = mouseEvent.y;
            } else if (createConnection) {
                handleCreateDefaultConnection(new Point(mouseEvent.x, mouseEvent.y));
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.gef.EditDomain#mouseUp(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
         */
        @Override
        public void mouseUp(org.eclipse.swt.events.MouseEvent mouseEvent, EditPartViewer viewer) {
            createConnection = false;
            if (mouseEvent.button != 2) {
                super.mouseUp(mouseEvent, viewer);
            } else {
                getEditor().setCursor(null);
                processor = null;
            }
        }

        @Override
        public void keyDown(org.eclipse.swt.events.KeyEvent keyEvent, EditPartViewer viewer) {
            int keyCode = keyEvent.keyCode;// 

            if (keyEvent.stateMask == SWT.CTRL
                    && (keyCode == SWT.ARROW_UP || keyCode == SWT.ARROW_DOWN || keyCode == SWT.ARROW_LEFT || keyCode == SWT.ARROW_RIGHT)) {
                List<EditPart> parts = viewer.getSelectedEditParts();
                if (parts == null) {
                    return;
                }
                for (EditPart part : parts) {
                    if (part instanceof NodePart) {
                        Node node = (Node) part.getModel();
                        if (node != null) {
                            moveShape(keyCode, node, moveOffset);
                        }
                    }
                }
                moveOffset++;
            } else {
                super.keyDown(keyEvent, viewer);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.gef.EditDomain#keyUp(org.eclipse.swt.events.KeyEvent, org.eclipse.gef.EditPartViewer)
         */
        @Override
        public void keyUp(KeyEvent keyEvent, EditPartViewer viewer) {
            super.keyUp(keyEvent, viewer);
            if (moveOffset != DEFAULT_MOVE_OFFSET) {
                moveOffset = DEFAULT_MOVE_OFFSET;
            }
        }

        /**
         * DOC bqian Comment method "moveShape".
         * 
         * @param keyCode
         * @param shape
         */
        private void moveShape(int keyCode, Node node, int offset) {

            Point location = node.getLocation().getCopy();
            if (location == null)
                return;
            switch (keyCode) {
            case SWT.ARROW_UP:
                location.y = location.y - offset;
                break;
            case SWT.ARROW_DOWN:
                location.y = location.y + offset;
                break;
            case SWT.ARROW_LEFT:
                location.x = location.x - offset;
                break;
            case SWT.ARROW_RIGHT:
                location.x = location.x + offset;
                break;
            default:
                // do nothing
            }
            MoveNodeCommand locationCommand = new MoveNodeCommand(node, location);
            getCommandStack().execute(locationCommand);
        }

        public void handleCreateDefaultConnection(Point currentLocation) {
            if (getActiveTool() instanceof TalendConnectionCreationTool) {
                // if a connection is already in creation, no need to create it again
                return;
            }

            if (getViewer().getSelectedEditParts().size() != 1) {
                return;
            }

            EditPart part = (EditPart) getViewer().getSelectedEditParts().get(0);
            if (!(part instanceof NodePart)) {
                return;
            }

            IFigure figure = ((NodePart) part).getFigure();

            ZoomManager zoomManager = (ZoomManager) AbstractTalendEditor.this.getAdapter(ZoomManager.class);
            double zoom = zoomManager.getZoom();
            if (zoom > 1e-7) {
                startPoint.scale(1 / zoom);
            }

            if (!figure.getBounds().contains(startPoint)) {
                // if the start location of the drag is not in the component, ignore it
                return;
            }

            if (startPoint.getDistance2(currentLocation) < 30) {
                // need to move a minimum the mouse for the drag, if not enough, display the context menu
                return;
            }

            Node node = (Node) part.getModel();

            final INodeConnector mainConnector;
            if (node.isELTComponent()) {
                mainConnector = node.getConnectorFromType(EConnectionType.TABLE);
            } else {
                mainConnector = node.getConnectorFromType(EConnectionType.FLOW_MAIN);
            }

            if (mainConnector == null) {
                return;
            }
            if (mainConnector.getMaxLinkOutput() != -1) {
                if (mainConnector.getCurLinkNbOutput() >= mainConnector.getMaxLinkOutput()) {
                    return;
                }
            }
            if (mainConnector.getMaxLinkOutput() == 0) {
                return;
            }

            figure.translateToAbsolute(startPoint);

            Canvas canvas = (Canvas) getViewer().getControl();
            Event event = new Event();
            event.button = 3;
            event.count = 0;
            event.detail = 0;
            event.end = 0;
            event.height = 0;
            event.keyCode = 0;
            event.start = 0;
            event.stateMask = 0;
            event.time = 9516624; // any old time... doesn't matter
            event.type = 3;
            event.widget = canvas;
            event.width = 0;
            event.x = startPoint.x + 3;
            event.y = startPoint.y + 3;
            /**
             * Set the connection tool to be the current tool
             */

            final List<Object> listArgs = new ArrayList<Object>();

            listArgs.add(null);
            listArgs.add(null);
            listArgs.add(null);

            TalendConnectionCreationTool myConnectTool = new TalendConnectionCreationTool(new CreationFactory() {

                public Object getNewObject() {
                    return listArgs;
                }

                public Object getObjectType() {
                    return mainConnector.getName();
                }
            }, false);
            myConnectTool.performConnectionStartWith(part);
            getViewer().getEditDomain().setActiveTool(myConnectTool);
            canvas.notifyListeners(3, event);
            TalendEditorContextMenuProvider.setEnableContextMenu(false);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#getCommandStack()
     */
    @Override
    public CommandStack getCommandStack() {
        return super.getCommandStack();
    }

    /**
     * Comment method "getAction".
     * 
     * @param actionID
     * @return
     */
    public IAction getAction(String actionID) {
        return getActionRegistry().getAction(actionID);
    }

    private KeyHandler sharedKeyHandler;

    /**
     * Sets the parent.
     * 
     * @param parent the parent to set
     */
    public void setParent(AbstractMultiPageTalendEditor parent) {
        this.parent = parent;
    }

    /**
     * DOC qzhang Comment method "getCommonKeyHandler".
     * 
     * @return
     */
    public KeyHandler getCommonKeyHandler() {
        if (sharedKeyHandler == null) {
            sharedKeyHandler = new KeyHandler();
            sharedKeyHandler.put(KeyStroke.getPressed(SWT.F1, 0), new Action() {

                @Override
                public void run() {
                    ISelection selection = getGraphicalViewer().getSelection();
                    if (selection != null) {
                        if (selection instanceof IStructuredSelection) {

                            Object input = ((IStructuredSelection) selection).getFirstElement();
                            Node node = null;
                            if (input instanceof NodeTreeEditPart) {
                                NodeTreeEditPart nTreePart = (NodeTreeEditPart) input;
                                node = (Node) nTreePart.getModel();
                            } else {
                                if (input instanceof NodePart) {
                                    EditPart editPart = (EditPart) input;
                                    node = (Node) editPart.getModel();
                                }
                            }
                            if (node != null) {
                                String helpLink = (String) node.getPropertyValue(EParameterName.HELP.getName());
                                String requiredHelpLink = "org.talend.help." + node.getComponent().getName();
                                if (helpLink == null || "".equals(helpLink) || !requiredHelpLink.equals(helpLink)) {
                                    helpLink = "org.talend.help." + node.getComponent().getName();
                                }
                                PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpLink);
                            }
                        }
                    }
                }
            });
            sharedKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
            // deactivate the F2 shortcut as it's not used anymore
            // sharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
            // getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        }
        return sharedKeyHandler;
    }

    TalendEditorDropTargetListener talendEditorDropTargetListener = null;

    // ------------------------------------------------------------------------
    // Abstract methods from GraphicalEditor

    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        // this uses the PartFactory set in configureGraphicalViewer
        // to create an EditPart for the diagram and sets it as the
        // content for the viewer
        getGraphicalViewer().setContents(this.process);

        // containers are not correctly updated by default, so update them after all nodes have been added
        for (ISubjobContainer subjobContainer : process.getSubjobContainers()) {
            subjobContainer.updateSubjobContainer();
        }
        getGraphicalViewer().getControl().addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                updateActions(getSelectionActions());
            }
        });
        talendEditorDropTargetListener = new TalendEditorDropTargetListener(this);
        getGraphicalViewer().addDropTargetListener(talendEditorDropTargetListener);
    }

    // ------------------------------------------------------------------------
    // Abstract methods from EditorPart

    /**
     * Outline view page. <br/>
     * 
     * $Id: TalendEditor.java 7860 2008-01-02 06:56:40Z qzhang $
     * 
     */
    class OutlinePage extends ContentOutlinePage implements IAdaptable {

        private PageBook pageBook;

        private Control outline;

        private Canvas overview;

        private IAction showOutlineAction, showOverviewAction;

        static final int ID_OUTLINE = 0;

        static final int ID_OVERVIEW = 1;

        static final int BORDER_SIZE = 3;

        private Thumbnail thumbnail;

        private DisposeListener disposeListener;

        public OutlinePage(EditPartViewer viewer) {
            super(viewer);
        }

        @Override
        public void init(final IPageSite pageSite) {
            super.init(pageSite);
            ActionRegistry registry = getActionRegistry();
            IActionBars bars = pageSite.getActionBars();
            String id = ActionFactory.UNDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            id = ActionFactory.REDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            id = ActionFactory.DELETE.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            bars.updateActionBars();
        }

        protected void configureOutlineViewer() {
            getViewer().setEditDomain(getEditDomain());
            getViewer().setEditPartFactory(new ProcessTreePartFactory());
            getViewer().setKeyHandler(getCommonKeyHandler());
            IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
            showOutlineAction = new Action() {

                @Override
                public String getId() {
                    return "ID_OUTLINE"; //$NON-NLS-1$
                }

                @Override
                public void run() {
                    showPage(ID_OUTLINE);
                }
            };
            showOutlineAction.setImageDescriptor(ImageDescriptor.createFromFile(DesignerPlugin.class, "/icons/outline.gif")); //$NON-NLS-1$
            tbm.add(showOutlineAction);
            showOverviewAction = new Action() {

                @Override
                public String getId() {
                    return "ID_OUTLINE"; //$NON-NLS-1$
                }

                @Override
                public void run() {
                    showPage(ID_OVERVIEW);
                }
            };
            showOverviewAction.setImageDescriptor(ImageDescriptor.createFromFile(DesignerPlugin.class, "/icons/overview.gif")); //$NON-NLS-1$
            tbm.add(showOverviewAction);
            getSite().getActionBars().updateActionBars();

            showPage(ID_OUTLINE);
        }

        @Override
        public void createControl(final Composite parent) {

            pageBook = new PageBook(parent, SWT.NONE);
            outline = getViewer().createControl(pageBook);
            overview = new Canvas(pageBook, SWT.NONE);
            pageBook.showPage(outline);
            configureOutlineViewer();
            hookOutlineViewer();
            initializeOutlineViewer();
        }

        @Override
        public void dispose() {
            unhookOutlineViewer();
            if (thumbnail != null) {
                thumbnail.deactivate();
                thumbnail = null;
            }
            super.dispose();
            AbstractTalendEditor.this.outlinePage = null;
            outlinePage = null;
            // if (AbstractTalendEditor.this.fActivationCodeTrigger != null) {
            // fActivationCodeTrigger.uninstall();
            // fActivationCodeTrigger = null;
            // }
        }

        public Object getAdapter(final Class type) {
            if (type == ZoomManager.class) {
                return getGraphicalViewer().getProperty(ZoomManager.class.toString());
            }
            return null;
        }

        @Override
        public Control getControl() {
            return pageBook;
        }

        protected void hookOutlineViewer() {
            // getSelectionSynchronizer().addViewer(getViewer());
        }

        protected void initializeOutlineViewer() {
            setContents(getProcess());
        }

        protected void initializeOverview() {
            LightweightSystem lws = new LightweightSystem(overview);
            RootEditPart rep = getGraphicalViewer().getRootEditPart();
            if (rep instanceof ScalableFreeformRootEditPart) {
                ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
                thumbnail = new ScrollableThumbnail((Viewport) root.getFigure());
                thumbnail.setBorder(new SimpleRaisedBorder());
                thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
                lws.setContents(thumbnail);
                disposeListener = new DisposeListener() {

                    public void widgetDisposed(final DisposeEvent e) {
                        if (thumbnail != null) {
                            thumbnail.deactivate();
                            thumbnail = null;
                        }
                    }
                };
                getEditor().addDisposeListener(disposeListener);
            }
        }

        public void setContents(final Object contents) {
            getViewer().setContents(contents);
        }

        protected void showPage(final int id) {
            if (id == ID_OUTLINE) {
                showOutlineAction.setChecked(true);
                showOverviewAction.setChecked(false);
                pageBook.showPage(outline);
                if (thumbnail != null) {
                    thumbnail.setVisible(false);
                }
            } else if (id == ID_OVERVIEW) {
                if (thumbnail == null) {
                    initializeOverview();
                }
                showOutlineAction.setChecked(false);
                showOverviewAction.setChecked(true);
                pageBook.showPage(overview);
                thumbnail.setVisible(true);
            }
        }

        protected void unhookOutlineViewer() {
            // getSelectionSynchronizer().removeViewer(getViewer());
            if (disposeListener != null && getEditor() != null && !getEditor().isDisposed()) {
                getEditor().removeDisposeListener(disposeListener);
            }
        }
    }
}
