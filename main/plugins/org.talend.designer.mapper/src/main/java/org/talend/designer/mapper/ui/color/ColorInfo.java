// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper.ui.color;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.runtime.ITalendThemeService;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 *
 * $Id$
 *
 */
public enum ColorInfo {

    COLOR_ENTRY_HIGHLIGHTED(102,190,230), 
    COLOR_ENTRY_HIGHLIGHTEDALL(255, 255, 0), // yellow
    COLOR_ENTRY_SEARCH_HIGHLIGHTED(255, 150, 20), // orange
    COLOR_ENTRY_ERROR(100, 200, 255), // ?
    COLOR_ENTRY_WARNING(0, 200, 60), // ?
    COLOR_ENTRY_NORMAL(170, 170, 170), // ?
    COLOR_ENTRY_NONE(255, 255, 255), // white
    
    COLOR_BACKGROUND_LINKS_ZONE(210, 210, 196), // gray

    // COLOR_UNSELECTED_ZONE_TO_ZONE_LINK(235, 235, 0), // light yellow
    COLOR_SELECTED_ZONE_TO_ZONE_LINK(255, 255, 0), // yellow
    COLOR_UNSELECTED_ZONE_TO_ZONE_LINK(196, 196, 180), // light gray

    COLOR_SELECTED_LOOKUP_LINKS(160, 40, 210), // violet
    COLOR_UNSELECTED_LOOKUP_LINKS(200, 186, 225), // pastel violet

    COLOR_SELECTED_FILTER_LINK(255, 150, 20), // orange
    COLOR_UNSELECTED_FILTER_LINK(255, 200, 70), // light gray

    COLOR_SELECTED_GLOBALMAP_LINK(71, 40, 210), // bleu sombre
    COLOR_UNSELECTED_GLOBALMAP_LINK(111, 186, 225), // bleu

    COLOR_HIGHLIGHTED_TEXT_ROW(240, 240, 240), // light gray

    COLOR_BACKGROUND_ERROR_EXPRESSION_CELL(255, 0, 0), // red
    COLOR_BACKGROUND_VALID_EXPRESSION_CELL(255, 255, 255), // white
    COLOR_BACKGROUND_TRANSPRENT(0, 0, 0, 0), // transparent

    COLOR_FOREGROUND_ERROR_EXPRESSION_CELL(255, 255, 255), // white
    COLOR_FOREGROUND_VALID_EXPRESSION_CELL(0, 0, 0), // black

    COLOR_DRAGGING_INSERTION_INDICATOR(0, 78, 152), // blue
    COLOR_TMAP_PREVIEW(235, 234, 230);

    private Color color = null;

    private ColorInfo(int red, int green, int blue) {
        color = new Color(Display.getCurrent(), red, green, blue);
    }

    private ColorInfo(int red, int green, int blue, int alpha) {
        color = new Color(Display.getCurrent(), red, green, blue, alpha);
    }

    public static Color COLOR_ENTRY_HIGHLIGHTED() {
        return ITalendThemeService.getColor("COLOR_ENTRY_HIGHLIGHTED").orElse(COLOR_ENTRY_HIGHLIGHTED.getColor());
    }
    
    public static Color COLOR_ENTRY_HIGHLIGHTEDALL() {
        return ITalendThemeService.getColor("COLOR_ENTRY_HIGHLIGHTEDALL").orElse(COLOR_ENTRY_HIGHLIGHTEDALL.getColor());
    }
    
    public static Color COLOR_ENTRY_SEARCH_HIGHLIGHTED() {
        return ITalendThemeService.getColor("COLOR_ENTRY_SEARCH_HIGHLIGHTED").orElse(COLOR_ENTRY_SEARCH_HIGHLIGHTED.getColor());
    }
    
    public static Color COLOR_ENTRY_ERROR() {
        return ITalendThemeService.getColor("COLOR_ENTRY_ERROR").orElse(COLOR_ENTRY_ERROR.getColor());
    }
    
    public static Color COLOR_ENTRY_WARNING() {
        return ITalendThemeService.getColor("COLOR_ENTRY_WARNING").orElse(COLOR_ENTRY_WARNING.getColor());
    }
    
    public static Color COLOR_ENTRY_NORMAL() {
        return ITalendThemeService.getColor("COLOR_ENTRY_NORMAL").orElse(COLOR_ENTRY_NORMAL.getColor());
    }
    
    public static Color COLOR_ENTRY_NONE() {
        return ITalendThemeService.getColor("COLOR_ENTRY_NONE").orElse(COLOR_ENTRY_NONE.getColor());
    }
    
    public static Color COLOR_BACKGROUND_LINKS_ZONE() {
        return ITalendThemeService.getColor("COLOR_BACKGROUND_LINKS_ZONE").orElse(COLOR_BACKGROUND_LINKS_ZONE.getColor());
    }
    
    public static Color COLOR_SELECTED_ZONE_TO_ZONE_LINK() {
        return ITalendThemeService.getColor("COLOR_SELECTED_ZONE_TO_ZONE_LINK").orElse(COLOR_SELECTED_ZONE_TO_ZONE_LINK.getColor());
    }
    
    public static Color COLOR_UNSELECTED_ZONE_TO_ZONE_LINK() {
        return ITalendThemeService.getColor("COLOR_UNSELECTED_ZONE_TO_ZONE_LINK").orElse(COLOR_UNSELECTED_ZONE_TO_ZONE_LINK.getColor());
    }
    
    public static Color COLOR_SELECTED_LOOKUP_LINKS() {
        return ITalendThemeService.getColor("COLOR_SELECTED_LOOKUP_LINKS").orElse(COLOR_SELECTED_LOOKUP_LINKS.getColor());
    }
    
    public static Color COLOR_UNSELECTED_LOOKUP_LINKS() {
        return ITalendThemeService.getColor("COLOR_UNSELECTED_LOOKUP_LINKS").orElse(COLOR_UNSELECTED_LOOKUP_LINKS.getColor());
    }
    
    public static Color COLOR_SELECTED_FILTER_LINK() {
        return ITalendThemeService.getColor("COLOR_SELECTED_FILTER_LINK").orElse(COLOR_SELECTED_FILTER_LINK.getColor());
    }
    
    public static Color COLOR_UNSELECTED_FILTER_LINK() {
        return ITalendThemeService.getColor("COLOR_UNSELECTED_FILTER_LINK").orElse(COLOR_UNSELECTED_FILTER_LINK.getColor());
    }
    
    public static Color COLOR_SELECTED_GLOBALMAP_LINK() {
        return ITalendThemeService.getColor("COLOR_SELECTED_GLOBALMAP_LINK").orElse(COLOR_SELECTED_GLOBALMAP_LINK.getColor());
    }
    
    public static Color COLOR_UNSELECTED_GLOBALMAP_LINK() {
        return ITalendThemeService.getColor("COLOR_UNSELECTED_GLOBALMAP_LINK").orElse(COLOR_UNSELECTED_GLOBALMAP_LINK.getColor());
    }
    
    public static Color COLOR_HIGHLIGHTED_TEXT_ROW() {
        return ITalendThemeService.getColor("COLOR_HIGHLIGHTED_TEXT_ROW").orElse(COLOR_HIGHLIGHTED_TEXT_ROW.getColor());
    }
    
    public static Color COLOR_BACKGROUND_ERROR_EXPRESSION_CELL() {
        return ITalendThemeService.getColor("COLOR_BACKGROUND_ERROR_EXPRESSION_CELL").orElse(COLOR_BACKGROUND_ERROR_EXPRESSION_CELL.getColor());
    }
    
    public static Color COLOR_BACKGROUND_VALID_EXPRESSION_CELL() {
        return ITalendThemeService.getColor("COLOR_BACKGROUND_VALID_EXPRESSION_CELL").orElse(COLOR_BACKGROUND_VALID_EXPRESSION_CELL.getColor());
    }
    
    public static Color COLOR_BACKGROUND_TRANSPRENT() {
        return ITalendThemeService.getColor("COLOR_BACKGROUND_TRANSPRENT").orElse(COLOR_BACKGROUND_TRANSPRENT.getColor());
    }
    
    public static Color COLOR_FOREGROUND_ERROR_EXPRESSION_CELL() {
        return ITalendThemeService.getColor("COLOR_FOREGROUND_ERROR_EXPRESSION_CELL").orElse(COLOR_FOREGROUND_ERROR_EXPRESSION_CELL.getColor());
    }
    
    public static Color COLOR_FOREGROUND_VALID_EXPRESSION_CELL() {
        return ITalendThemeService.getColor("COLOR_FOREGROUND_VALID_EXPRESSION_CELL").orElse(COLOR_FOREGROUND_VALID_EXPRESSION_CELL.getColor());
    }
    
    public static Color COLOR_DRAGGING_INSERTION_INDICATOR() {
        return ITalendThemeService.getColor("COLOR_DRAGGING_INSERTION_INDICATOR").orElse(COLOR_DRAGGING_INSERTION_INDICATOR.getColor());
    }
    
    public static Color COLOR_TMAP_PREVIEW() {
        return ITalendThemeService.getColor("COLOR_TMAP_PREVIEW").orElse(COLOR_TMAP_PREVIEW.getColor());
    }
    
//    public static Color COLOR_ENTRY_SELECTED() {
//        return ITalendThemeService.getColor("COLOR_ENTRY_SELECTED").orElse(COLOR_ENTRY_SELECTED.getColor());
//    }
//    
//    public static Color COLOR_ENTRY_UNSELECTED() {
//        return ITalendThemeService.getColor("COLOR_ENTRY_UNSELECTED").orElse(COLOR_ENTRY_UNSELECTED.getColor());
//    }
    
    public Color getColor() {
        return color;
    }
}
