// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.parts;

import org.eclipse.emf.common.notify.Notification;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;

/**
 * DOC talend class global comment. Detailled comment
 */
public class AbstractInOutTreeEditPart extends BaseEditPart {

    @Override
    public void notifyChanged(Notification notification) {
        int type = notification.getEventType();
        int featureId = notification.getFeatureID(XmlmapPackage.class);
        switch (type) {
        case Notification.ADD:
        case Notification.REMOVE:
        case Notification.REMOVE_MANY:
            switch (featureId) {
            case XmlmapPackage.INPUT_XML_TREE__NODES:
                refreshChildren();
                break;
            }
        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.INPUT_XML_TREE__NODES:
                refreshChildren();
                break;
            }
        }
    }
}
