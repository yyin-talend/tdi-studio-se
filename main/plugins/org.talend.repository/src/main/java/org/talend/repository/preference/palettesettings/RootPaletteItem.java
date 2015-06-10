// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.preference.palettesettings;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.component.preference.provider.IPaletteItem;
import org.talend.core.ui.images.CoreImageProvider;

/**
 * created by nrousseau on Aug 11, 2014 Detailled comment
 * 
 */
public class RootPaletteItem extends AbstractPaletteItem implements IPaletteItem {

    public RootPaletteItem(ComponentCategory category) {
        setCategory(category);
        ERepositoryObjectType type;
        switch (category) {
        case CATEGORY_4_CAMEL:
            type = ERepositoryObjectType.valueOf("ROUTES"); //$NON-NLS-1$
            break;
        case CATEGORY_4_MAPREDUCE:
            type = ERepositoryObjectType.valueOf("PROCESS_MR"); //$NON-NLS-1$
            break;
        case CATEGORY_4_STORM:
            type = ERepositoryObjectType.valueOf("PROCESS_STORM"); //$NON-NLS-1$
            break;
        case CATEGORY_4_SPARK:
            type = ERepositoryObjectType.valueOf("PROCESS_MR"); //$NON-NLS-1$
            break;
        case CATEGORY_4_SPARKSTREAMING:
            type = ERepositoryObjectType.valueOf("PROCESS_STORM"); //$NON-NLS-1$
            break;
        case CATEGORY_4_DI:
        default:
            type = ERepositoryObjectType.PROCESS;
            break;
        }
        Assert.isNotNull(type);
        String desc = category.getDesc();
        setLabel(type.getLabel() + ("".equals(desc) ? "" : " (" + desc + ")")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        ImageDescriptor imageDesc = CoreImageProvider.getImageDesc(type);
        setImageDesc(imageDesc);
    }
}
