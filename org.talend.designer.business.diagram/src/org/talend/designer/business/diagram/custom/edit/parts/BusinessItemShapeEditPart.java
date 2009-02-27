// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.business.diagram.custom.edit.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.image.OverlayImage;
import org.talend.commons.ui.image.OverlayImage.EPosition;
import org.talend.commons.utils.image.ImageUtils;
import org.talend.commons.utils.image.ImageUtils.ICON_SIZE;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.business.diagram.custom.edit.policies.BusinessItemDragDropEditPolicy;
import org.talend.designer.business.diagram.custom.figures.BusinessTooltipFigure;
import org.talend.designer.business.model.business.BusinessAssignment;
import org.talend.designer.business.model.business.BusinessItem;
import org.talend.designer.business.model.business.TalendItem;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessProcessEditPart;
import org.talend.designer.business.model.business.provider.BusinessAssignmentItemProvider;
import org.talend.designer.business.model.business.provider.BusinessItemProviderAdapterFactory;
import org.talend.repository.ui.views.RepositoryLabelProvider;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class BusinessItemShapeEditPart extends ShapeNodeEditPart {

    private BusinessTooltipFigure tooltipFigure;

    /**
     * DOC mhelleboid BusinessItemShapeEditPart constructor comment.
     * 
     * @param view
     */
    public BusinessItemShapeEditPart(View view) {
        super(view);
        tooltipFigure = new BusinessTooltipFigure();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#createDefaultEditPolicies()
     */
    @Override
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new BusinessItemDragDropEditPolicy());
    }

    /**
     * DOC Administrator Comment method "getTooltipFigure".
     * 
     * @param figure
     */
    private void getTooltipFigure(NodeFigure figure) {
        List assignements = ((BusinessItem) ((Node) getModel()).getElement()).getAssignments();
        BusinessItemProviderAdapterFactory adapterFactory = new BusinessItemProviderAdapterFactory();
        BusinessAssignmentItemProvider provider = (BusinessAssignmentItemProvider) adapterFactory
                .createBusinessAssignmentAdapter();
        Image img = null;
        Label label = null;
        List<Label> labels = new ArrayList();
        try {
            for (Object assignment : assignements) {
                if (assignment instanceof BusinessAssignment) {
                    TalendItem talendItem = ((BusinessAssignment) assignment).getTalendItem();
                    IRepositoryObject obj = CorePlugin.getDefault().getProxyRepositoryFactory()
                            .getLastVersion(talendItem.getId());
                    if (obj != null) {
                        ERepositoryObjectType type = obj.getType();
                        Item item = obj.getProperty().getItem();
                        if (item instanceof JobletProcessItem) {
                            JobletProcessItem jobletItem = (JobletProcessItem) item;
                            Image jobletCustomIcon = RepositoryLabelProvider.getJobletCustomIcon(jobletItem.getProperty());
                            if (jobletCustomIcon != null) {
                                img = ImageUtils.scale(jobletCustomIcon, ICON_SIZE.ICON_16);
                            }

                        } else {
                            img = CoreImageProvider.getImage(type);
                        }
                        label = new Label(talendItem.getLabel() + " (" + provider.getColumnText(assignment, 0) + ")", img);
                        labels.add(label);
                    } else {
                        MetadataTable table = MetadataTool.getMetadataTableFromRepository(talendItem.getId());
                        Query query = MetadataTool.getQueryFromRepository(talendItem.getId());
                        if (table != null) {
                            img = ImageDescriptor.createFromFile(ECoreImage.class, ECoreImage.METADATA_TABLE_ICON.getPath())
                                    .createImage();
                            label = new Label(talendItem.getLabel() + " (" + provider.getColumnText(assignment, 0) + ")", img);
                            labels.add(label);
                        } else if (query != null) {
                            img = ImageDescriptor.createFromFile(ECoreImage.class, ECoreImage.METADATA_QUERY_ICON.getPath())
                                    .createImage();
                            label = new Label(talendItem.getLabel() + " (" + provider.getColumnText(assignment, 0) + ")", img);
                            labels.add(label);
                        } else {
                            img = (Image) provider.getImage(assignment);
                            img = new OverlayImage(img, ImageProvider.getImageDesc(ECoreImage.DELETED_OVERLAY),
                                    EPosition.BOTTOM_RIGHT).createImage();
                            String text = provider.getText(assignment) + " (" + provider.getColumnText(assignment, 0) + ")";
                            label = new Label(text, img);
                            labels.add(label);

                        }

                    }
                }

            }
            if (labels.size() > 0) {
                if (tooltipFigure == null) {
                    tooltipFigure = new BusinessTooltipFigure();
                }
                tooltipFigure.buildFigures(labels);
                figure.setToolTip(tooltipFigure);
            }
        } catch (PersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void handleNotificationEvent(Notification notification) {
        super.handleNotificationEvent(notification);
        getTooltipFigure(getNodeFigure());
    }

    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        getTooltipFigure(getNodeFigure());
    }

    protected void setDefaultColor(Object model, RGB color) {
        if (getModel() == ((BusinessProcessEditPart) getParent()).getLastAddedItem()) {
            ENamedElement element = PackageUtil.getElement(Properties.ID_FILLCOLOR);
            EStructuralFeature feature = (EStructuralFeature) PackageUtil.getElement(Properties.ID_FILLCOLOR);
            if (element instanceof EStructuralFeature) {
                setStructuralFeatureValue(feature, FigureUtilities.RGBToInteger(color));
            }
        }
        ((BusinessProcessEditPart) getParent()).setLastAddedItem(null);

    }
}
