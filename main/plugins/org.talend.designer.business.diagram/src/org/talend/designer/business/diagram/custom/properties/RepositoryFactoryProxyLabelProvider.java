package org.talend.designer.business.diagram.custom.properties;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.image.OverlayImage;
import org.talend.commons.ui.runtime.image.OverlayImage.EPosition;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.SubItemHelper;
import org.talend.designer.business.diagram.custom.commands.ChangeTalendItemLabelCommand;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.designer.business.model.business.BusinessAssignment;
import org.talend.designer.business.model.business.Routine;
import org.talend.designer.business.model.business.SQLPattern;
import org.talend.designer.business.model.business.TalendItem;

public class RepositoryFactoryProxyLabelProvider extends AdapterFactoryLabelProvider {

    public RepositoryFactoryProxyLabelProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    public Image getColumnImage(Object object, int columnIndex) {
        Image image = super.getColumnImage(object, columnIndex);
        BusinessAssignment assignment = (BusinessAssignment) object;
        try {
            if (columnIndex == 0) {
                IRepositoryViewObject lastVersion = getLastVersion(object);
                if (lastVersion == null) {
                    MetadataTable table = MetadataToolHelper.getMetadataTableFromRepository(assignment.getTalendItem().getId());
                    if (table != null) {
                        return image;
                    }
                    Query query = MetadataToolHelper.getQueryFromRepository(assignment.getTalendItem().getId());
                    if (query != null) {
                        return image;
                    }
                    SAPFunctionUnit function = MetadataToolHelper
                            .getSAPFunctionFromRepository(assignment.getTalendItem().getId());
                    if (function != null) {
                        return image;
                    }
                    TalendItem item = assignment.getTalendItem();
                    if (item instanceof SQLPattern) {

                        List<IRepositoryViewObject> list = ProxyRepositoryFactory.getInstance().getAll(
                                ERepositoryObjectType.SQLPATTERNS);
                        for (IRepositoryViewObject obj : list) {
                            if (item.getLabel().equals(obj.getLabel())) {
                                return image;
                            }
                        }

                    } else if (item instanceof Routine) {
                        List<IRepositoryViewObject> list = ProxyRepositoryFactory.getInstance().getAll(
                                ERepositoryObjectType.ROUTINES);
                        for (IRepositoryViewObject obj : list) {
                            if (item.getLabel().equals(obj.getLabel())) {
                                return image;
                            }
                        }
                    }
                    return new OverlayImage(image, ImageProvider.getImageDesc(ECoreImage.DELETED_OVERLAY), EPosition.BOTTOM_RIGHT)
                            .createImage();

                } else if (isDeleted(lastVersion)) {
                    return new OverlayImage(image, ImageProvider.getImageDesc(ECoreImage.RECYCLE_BIN_OVERLAY),
                            EPosition.BOTTOM_RIGHT).createImage();
                }
            }
        } catch (PersistenceException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
        return image;
    }

    @Override
    public String getColumnText(Object object, int columnIndex) {
        String columnText = super.getColumnText(object, columnIndex);
        BusinessAssignment assignment = (BusinessAssignment) object;
        IRepositoryViewObject lastVersion = getLastVersion(object);
        try {
            if (columnIndex == 0) {
                if (lastVersion == null) {
                    MetadataTable table = MetadataToolHelper.getMetadataTableFromRepository(assignment.getTalendItem().getId());
                    if (table != null) {
                        if (SubItemHelper.isDeleted(table)) {
                            columnText += Messages.getString("RepositoryFactoryProxyLabelProvider.Deleted"); //$NON-NLS-1$
                        }
                        return columnText;
                    }
                    Query query = MetadataToolHelper.getQueryFromRepository(assignment.getTalendItem().getId());
                    if (query != null) {
                        if (SubItemHelper.isDeleted(query)) {
                            columnText += Messages.getString("RepositoryFactoryProxyLabelProvider.Deleted"); //$NON-NLS-1$
                        }
                        return columnText;
                    }
                    SAPFunctionUnit function = MetadataToolHelper
                            .getSAPFunctionFromRepository(assignment.getTalendItem().getId());
                    if (function != null) {
                        if (SubItemHelper.isDeleted(function)) {
                            columnText += Messages.getString("RepositoryFactoryProxyLabelProvider.Deleted"); //$NON-NLS-1$
                        }
                        return columnText;
                    }
                    TalendItem item = assignment.getTalendItem();
                    if (item instanceof SQLPattern) {

                        List<IRepositoryViewObject> list = ProxyRepositoryFactory.getInstance().getAll(
                                ERepositoryObjectType.SQLPATTERNS);
                        for (IRepositoryViewObject obj : list) {
                            if (item.getLabel().equals(obj.getLabel())) {
                                return columnText;
                            }
                        }

                    } else if (item instanceof Routine) {
                        List<IRepositoryViewObject> list = ProxyRepositoryFactory.getInstance().getAll(
                                ERepositoryObjectType.ROUTINES);
                        for (IRepositoryViewObject obj : list) {
                            if (item.getLabel().equals(obj.getLabel())) {
                                return columnText;
                            }
                        }
                    }

                    columnText += Messages.getString("RepositoryFactoryProxyLabelProvider.NotFound"); //$NON-NLS-1$

                } else if (isDeleted(lastVersion)) {
                    columnText += Messages.getString("RepositoryFactoryProxyLabelProvider.Deleted"); //$NON-NLS-1$
                }
            }
        } catch (PersistenceException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
        if (columnIndex == 1) {

            if (lastVersion != null) {
                String label = lastVersion.getProperty().getLabel();
                if (!label.equals(columnText)) {
                    ChangeTalendItemLabelCommand command = new ChangeTalendItemLabelCommand(
                            ((BusinessAssignment) object).getTalendItem(), label);
                    try {
                        command.execute(null, null);
                    } catch (ExecutionException e) {
                    }
                    return label;
                }
            }
        }

        return columnText;
    }

    private IRepositoryViewObject getLastVersion(Object object) {
        BusinessAssignment businessAssignment = (BusinessAssignment) object;
        try {
            return ProxyRepositoryFactory.getInstance().getLastVersion(businessAssignment.getTalendItem().getId());
        } catch (PersistenceException e) {
        }
        return null;
    }

    private boolean isDeleted(IRepositoryViewObject repositoryObject) {
        return ProxyRepositoryFactory.getInstance().getStatus(repositoryObject).equals(ERepositoryStatus.DELETED);
    }
}
