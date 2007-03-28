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
package org.talend.repository.ui.properties;

import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.i18n.Messages;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.ActionsHelper;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * 
 */
public class VersionSection extends AbstractSection implements ISelectionProvider {

    private Composite composite;

    private TableViewer tableViewer;

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat();

    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);

        composite = getWidgetFactory().createFlatFormComposite(parent);

        tableViewer = new TableViewer(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        Table table = tableViewer.getTable();
        TableLayout tableLayout = new TableLayout();
        table.setLayout(tableLayout);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        final String[] columnProperties = new String[] { Messages.getString("VersionSection.Version"), Messages.getString("VersionSection.CreationDate"), Messages.getString("VersionSection.ModificationDate") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        TableColumn column1 = new TableColumn(table, SWT.NONE);
        tableLayout.addColumnData(new ColumnPixelData(125, true));
        column1.setText(columnProperties[0]);

        TableColumn column2 = new TableColumn(table, SWT.NONE);
        tableLayout.addColumnData(new ColumnPixelData(125, true));
        column2.setText(columnProperties[1]);

        TableColumn column3 = new TableColumn(table, SWT.NONE);
        tableLayout.addColumnData(new ColumnWeightData(1, 150, true));
        column3.setText(columnProperties[2]);

        tableViewer.setColumnProperties(columnProperties);

        Object layoutData = parent.getLayoutData();
        if (layoutData instanceof GridData) {
            GridData gridData = (GridData) layoutData;
            gridData.grabExcessVerticalSpace = true;
            gridData.verticalAlignment = SWT.FILL;
        }

        FormData formData = new FormData();
        formData.left = new FormAttachment(0);
        formData.top = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        formData.bottom = new FormAttachment(100);
        table.setLayoutData(formData);

        tableViewer.setContentProvider(new IStructuredContentProvider() {

            public Object[] getElements(Object inputElement) {
                IRepositoryObject repositoryObject = ((IRepositoryObject) inputElement);
                if (repositoryObject.getProperty() == null) {
                    return null;
                }

                RepositoryNode parentRepositoryNode = getParentRepositoryNode();

                try {
                    List<IRepositoryObject> allVersion = ProxyRepositoryFactory.getInstance().getAllVersion(
                            repositoryObject.getId());
                    Object[] objects = new Object[allVersion.size()];
                    for (int i = 0; i < objects.length; i++) {
                        IRepositoryObject repositoryObjectVersion = allVersion.get(i);
                        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(repositoryObjectVersion.getProperty().getItem());

                        RepositoryNode repositoryNode = new RepositoryNode(repositoryObjectVersion, parentRepositoryNode,
                                ENodeType.REPOSITORY_ELEMENT);
                        repositoryNode.setProperties(EProperties.CONTENT_TYPE, itemType);
                        objects[i] = repositoryNode;
                    }
                    return objects;
                } catch (PersistenceException e) {
                    return null;
                }
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });

        tableViewer.setLabelProvider(new ITableLabelProvider() {

            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            public String getColumnText(Object element, int columnIndex) {
                RepositoryNode repositoryNode = (RepositoryNode) element;
                switch (columnIndex) {
                case 0:
                    return repositoryNode.getObject().getVersion();
                case 1:
                    if (repositoryNode.getObject().getCreationDate() != null) {
                        return FORMATTER.format(repositoryNode.getObject().getCreationDate());
                    } else {
                        return null;
                    }
                case 2:
                    if (repositoryNode.getObject().getModificationDate() != null) {
                        return FORMATTER.format(repositoryNode.getObject().getModificationDate());
                    } else {
                        return null;
                    }
                default:
                    return null;
                }
            }

            public void addListener(ILabelProviderListener listener) {
            }

            public void dispose() {
            }

            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            public void removeListener(ILabelProviderListener listener) {
            }
        });

        MenuManager menuMgr = new MenuManager("#PopUp"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager mgr) {
                ISelection selection = tableViewer.getSelection();
                if (selection instanceof IStructuredSelection) {
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;

                    List<ITreeContextualAction> contextualsActions = ActionsHelper.getRepositoryContextualsActions();
                    for (ITreeContextualAction action : contextualsActions) {
                        if (action.isReadAction() || action.isEditAction()) {
                            action.init(null, structuredSelection);
                            if (action.isVisible()) {
                                mgr.add(action);
                            }
                        }                        
                    }
                }
            }
        });
        Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
        tableViewer.getControl().setMenu(menu);
        
        aTabbedPropertySheetPage.getSite().setSelectionProvider(this);
    }

    @Override
    public void refresh() {
        if (getObject() != null && getObject().getProperty() != null) {
            tableViewer.setInput(getObject());
        } else {
            tableViewer.setInput(null);
        }
    }

    @Override
    protected void beforeSave() {
    }

    @Override
    protected void enableControl(boolean enable) {
    }

    @Override
    protected void showControl(boolean visible) {
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
    }

    public void setSelection(ISelection selection) {
    }
    
    @Override
    public ISelection getSelection() {
        return tableViewer.getSelection();
    }

    private RepositoryNode getParentRepositoryNode() {
        IRepositoryView viewPart = (IRepositoryView) getActivePage().findView(IRepositoryView.VIEW_ID);
        ISelection repositoryViewSelection = viewPart.getViewer().getSelection();
        if (!(repositoryViewSelection instanceof IStructuredSelection)) {
            return null;
        }
        IStructuredSelection structuredSelection = (IStructuredSelection) repositoryViewSelection;
        RepositoryNode selectedRepositoryNode = (RepositoryNode) structuredSelection.getFirstElement();
        
        if (selectedRepositoryNode == null) {
            return null;
        }
        return selectedRepositoryNode.getParent();
    }
    
}
