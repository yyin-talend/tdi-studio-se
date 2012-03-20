// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.viewer.label;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryLabelProvider;

public class RepositoryViewLabelProvider implements ICommonLabelProvider {

    /**
     * DOC sgandon class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
     * 
     */
    private final class CommonNavigatorRepositoryViewForLabelProvider implements IRepositoryView {

        private final String viewerId;

        /**
         * DOC sgandon JobsLabelProvider.CommonNavigatorRepositoryView constructor comment.
         */
        public CommonNavigatorRepositoryViewForLabelProvider(String viewerId) {
            this.viewerId = viewerId;
        }

        @Override
        public Object getAdapter(Class adapter) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setFocus() {
            // TODO Auto-generated method stub

        }

        @Override
        public void removePropertyListener(IPropertyListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public String getTitleToolTip() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Image getTitleImage() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getTitle() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IWorkbenchPartSite getSite() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void dispose() {
            // TODO Auto-generated method stub

        }

        @Override
        public void createPartControl(Composite parent) {
            // TODO Auto-generated method stub

        }

        @Override
        public void addPropertyListener(IPropertyListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public void saveState(IMemento memento) {
            // TODO Auto-generated method stub

        }

        @Override
        public void init(IViewSite site, IMemento memento) throws PartInitException {
            // TODO Auto-generated method stub

        }

        @Override
        public void init(IViewSite site) throws PartInitException {
            // TODO Auto-generated method stub

        }

        @Override
        public IViewSite getViewSite() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void refreshView() {
            // TODO Auto-generated method stub

        }

        @Override
        public void refreshAllChildNodes(RepositoryNode rootNode) {
            // TODO Auto-generated method stub

        }

        @Override
        public void refresh(ERepositoryObjectType type) {
            // TODO Auto-generated method stub

        }

        @Override
        public void refresh(Object object) {
            // TODO Auto-generated method stub

        }

        @Override
        public void refresh() {
            // TODO Auto-generated method stub

        }

        @Override
        public StructuredViewer getViewer() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IRepositoryNode getRoot() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean getExpandedState(Object object) {
            boolean result = false;

            IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewerId);
            if (view instanceof CommonNavigator) {
                result = ((CommonNavigator) view).getCommonViewer().getExpandedState(object);
            }// else default is false
            return result;
        }

        @Override
        public List<IRepositoryViewObject> getAll(ERepositoryObjectType type) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String[] gatherMetadataChildenLabels() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void expand(Object object, boolean state) {
            // TODO Auto-generated method stub

        }

        @Override
        public void expand(Object object) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean containsRepositoryType(ERepositoryObjectType type) {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.repository.ui.views.IRepositoryView#isFakeView()
         */
        @Override
        public boolean isFakeView() {
            // TODO Auto-generated method stub
            return false;
        }
    }

    RepositoryLabelProvider repoLabelProv;

    @Override
    public Image getImage(Object element) {
        return repoLabelProv.getImage(element);
    }

    @Override
    public String getText(Object element) {
        if (element instanceof RepositoryNode && !(element instanceof ProjectRepositoryNode)) {// Only handle repository
                                                                                               // node
            return repoLabelProv.getText(element);
        } else {
            return ""; //$NON-NLS-1$
        }
    }

    @Override
    public void addListener(ILabelProviderListener listener) {
        repoLabelProv.addListener(listener);
    }

    @Override
    public void dispose() {
        repoLabelProv.dispose();
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return isLabelProperty(element, property);
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
        repoLabelProv.removeListener(listener);
    }

    @Override
    public void restoreState(IMemento aMemento) {
        // no state tp restore
    }

    @Override
    public void saveState(IMemento aMemento) {
        // no state to save.
    }

    @Override
    public String getDescription(Object anElement) {
        return null;
    }

    @Override
    public void init(ICommonContentExtensionSite aConfig) {
        String viewerId = aConfig.getService().getViewerId();
        repoLabelProv = new RepositoryLabelProvider(new CommonNavigatorRepositoryViewForLabelProvider(viewerId));
    }

}
