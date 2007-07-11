package org.talend.designer.business.diagram.custom.properties;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.image.OverlayImage;
import org.talend.commons.ui.image.OverlayImage.EPosition;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.business.diagram.custom.commands.ChangeTalendItemLabelCommand;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.designer.business.model.business.BusinessAssignment;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.ProxyRepositoryFactory;

public class RepositoryFactoryProxyLabelProvider extends AdapterFactoryLabelProvider {

    public RepositoryFactoryProxyLabelProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    public Image getColumnImage(Object object, int columnIndex) {
        Image image = super.getColumnImage(object, columnIndex);

        if (columnIndex == 0) {
            IRepositoryObject lastVersion = getLastVersion(object);
            if (lastVersion == null) {
                return new OverlayImage(image, ImageProvider.getImageDesc(ECoreImage.DELETED_OVERLAY),
                        EPosition.BOTTOM_RIGHT).createImage();
            } else if (isDeleted(lastVersion)) {
                return new OverlayImage(image, ImageProvider.getImageDesc(ECoreImage.RECYCLE_BIN_OVERLAY),
                        EPosition.BOTTOM_RIGHT).createImage();
            }
        }

        return image;
    }

    @Override
    public String getColumnText(Object object, int columnIndex) {
        String columnText = super.getColumnText(object, columnIndex);

        if (columnIndex == 0) {
            IRepositoryObject lastVersion = getLastVersion(object);
            if (lastVersion == null) {
                columnText += Messages.getString("RepositoryFactoryProxyLabelProvider.NotFound"); //$NON-NLS-1$
            } else if (isDeleted(lastVersion)) {
                columnText += Messages.getString("RepositoryFactoryProxyLabelProvider.Deleted"); //$NON-NLS-1$
            }
        }

        if (columnIndex == 1) {
            IRepositoryObject lastVersion = getLastVersion(object);
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

    private IRepositoryObject getLastVersion(Object object) {
        BusinessAssignment businessAssignment = (BusinessAssignment) object;
        try {
            return ProxyRepositoryFactory.getInstance().getLastVersion(businessAssignment.getTalendItem().getId());
        } catch (PersistenceException e) {
        }
        return null;
    }

    private boolean isDeleted(IRepositoryObject repositoryObject) {
        return ProxyRepositoryFactory.getInstance().getStatus(repositoryObject).equals(ERepositoryStatus.DELETED);
    }
}
