package org.talend.designer.business.model.business.diagram.part;

import org.eclipse.gmf.runtime.diagram.ui.figures.DiagramColorConstants;
import org.eclipse.gmf.runtime.diagram.ui.preferences.DiagramPreferenceInitializer;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;

/**
 * @generated
 */
public class BusinessDiagramPreferenceInitializer extends DiagramPreferenceInitializer {

    /**
     * @generated
     */
    protected IPreferenceStore getPreferenceStore() {
        return org.talend.designer.business.model.business.diagram.part.BusinessDiagramEditorPlugin.getInstance()
                .getPreferenceStore();
    }

    public void initializeDefaultPreferences() {
        super.initializeDefaultPreferences();

        // see feature 955
        Color lineColor = DiagramColorConstants.black;
        getPreferenceStore().setDefault(IPreferenceConstants.PREF_LINE_COLOR, StringConverter.asString(lineColor.getRGB()));
    }
}
