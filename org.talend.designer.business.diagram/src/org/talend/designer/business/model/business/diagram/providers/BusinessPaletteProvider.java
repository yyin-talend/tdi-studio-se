package org.talend.designer.business.model.business.diagram.providers;

import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.IPaletteProvider;
import org.eclipse.ui.IEditorPart;
import org.talend.designer.business.model.business.diagram.part.BusinessPaletteFactory;

/**
 * @generated
 */
public class BusinessPaletteProvider extends AbstractProvider implements IPaletteProvider {

    /**
     * @generated
     */
    public void contributeToPalette(IEditorPart editor, Object content, PaletteRoot root, Map predefinedEntries) {
        BusinessPaletteFactory factory = new BusinessPaletteFactory();
        factory.fillPalette(root);
    }

    /**
     * @generated
     */
    public void setContributions(IConfigurationElement configElement) {
        // no configuration
    }

    /**
     * @generated
     */
    public boolean provides(IOperation operation) {
        return false; // all logic is done in the service
    }
}
