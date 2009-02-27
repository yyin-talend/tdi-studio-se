// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.views.jobsettings;

import org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance.ConnectionAppearancePropertySection;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance.DiagramColorsAndFontsPropertySection;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance.ShapeColorsAndFontsPropertySection;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance.TextAlignmentPropertySection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.core.CorePlugin;
import org.talend.core.model.business.BusinessType;
import org.talend.designer.core.ui.views.jobsettings.tabs.AbstractTabComposite;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class BusinessAppearanceComposite extends AbstractTabComposite {

    public BusinessAppearanceComposite(Composite parent, int style, TabbedPropertySheetWidgetFactory widgetFactory,
            ISelection selection) {
        super(parent, style, widgetFactory, null);

        Composite composite = widgetFactory.createFlatFormComposite(this);

        FormLayout layout = new FormLayout();
        setLayout(layout);

        FormData thisFormData = new FormData();
        thisFormData.left = new FormAttachment(0, 0);
        thisFormData.right = new FormAttachment(100, 0);
        thisFormData.top = new FormAttachment(0, 0);
        thisFormData.bottom = new FormAttachment(100, 0);
        setLayoutData(thisFormData);

        FormData compositeData = new FormData();
        compositeData.left = new FormAttachment(0, 0);
        compositeData.right = new FormAttachment(100, 0);
        compositeData.top = new FormAttachment(0, 0);
        compositeData.bottom = new FormAttachment(100, 0);
        composite.setLayoutData(thisFormData);
        BusinessType type = CorePlugin.getDefault().getDiagramModelService().getBusinessModelType(
                ((IStructuredSelection) selection).getFirstElement());
        if (type != null) {
            switch (type) {
            case PROCESS:
                selection = new StructuredSelection(CorePlugin.getDefault().getDiagramModelService().getBusinessEditorProcess());
                DiagramAppearanceSection diagramAppearance = new DiagramAppearanceSection();
                diagramAppearance.setInput(getEditor(), selection);
                diagramAppearance.createControls(composite, null);
                diagramAppearance.refresh();

                break;
            case CONNECTION:
                AppearanceSection appearance = new AppearanceSection();
                appearance.setInput(getEditor(), selection);
                appearance.createControls(composite, null);
                appearance.refresh();
                break;
            case SHAP: {
                ShapAppearance colorsAndFontsAndLine = new ShapAppearance();
                colorsAndFontsAndLine.setInput(getEditor(), selection);
                colorsAndFontsAndLine.createControls(composite, null);
                colorsAndFontsAndLine.refresh();
            }
                break;
            case NOTE:
            default: {
                Composite sectionComposite = widgetFactory.createComposite(composite, SWT.NO_FOCUS);
                sectionComposite.setLayout(new GridLayout());

                ShapAppearance colorsAndFontsAndLine = new ShapAppearance();
                colorsAndFontsAndLine.setInput(getEditor(), selection);
                colorsAndFontsAndLine.createControls(sectionComposite, null);
                colorsAndFontsAndLine.refresh();

                TextShapAppearance textAppearance = new TextShapAppearance();
                textAppearance.createControls(sectionComposite, null);
                textAppearance.setInput(getEditor(), selection);
                textAppearance.refresh();
                break;
            }
            }
        } else {
            selection = new StructuredSelection(CorePlugin.getDefault().getDiagramModelService().getBusinessEditorProcess());
            DiagramAppearanceSection diagramAppearance = new DiagramAppearanceSection();
            diagramAppearance.setInput(getEditor(), selection);
            diagramAppearance.createControls(composite, null);
            diagramAppearance.refresh();
        }

    }

    private class AppearanceSection extends ConnectionAppearancePropertySection {

        @Override
        public TabbedPropertySheetWidgetFactory getWidgetFactory() {
            return widgetFactory;
        }

    }

    private class DiagramAppearanceSection extends DiagramColorsAndFontsPropertySection {

        @Override
        public TabbedPropertySheetWidgetFactory getWidgetFactory() {
            return widgetFactory;
        }
    }

    private class ShapAppearance extends ShapeColorsAndFontsPropertySection {

        @Override
        public TabbedPropertySheetWidgetFactory getWidgetFactory() {
            return widgetFactory;
        }
    }

    private class TextShapAppearance extends TextAlignmentPropertySection {

        @Override
        public TabbedPropertySheetWidgetFactory getWidgetFactory() {
            return widgetFactory;
        }

    }

}
