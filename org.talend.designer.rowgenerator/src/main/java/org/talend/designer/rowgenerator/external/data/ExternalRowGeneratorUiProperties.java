// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.rowgenerator.external.data;

import java.io.Serializable;

import org.eclipse.swt.graphics.Rectangle;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExternalRowGeneratorUiProperties.java,v 1.2 2007/01/31 05:20:52 pub Exp $
 * 
 */
public class ExternalRowGeneratorUiProperties implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 145468748235L;

    // must be null by default to unmarshal correctly
    private static int[] weightsMainSashForm = new int[0];

    public static final int[] DEFAULT_WEIGHTS_MAIN_SASH_FORM = new int[] { 70, 30 };

    // must be null by default to unmarshal correctly
    private static int[] weightsDatasFlowViewSashForm = new int[0];

    public static final int[] DEFAULT_WEIGHTS_DATAS_FLOW_SASH_FORM = new int[] { 33, 33, 34 };

    private static Rectangle boundsRowGen = new Rectangle(50, 50, 800, 600);

    private static boolean shellMaximized = false;

    private static String[] showColumnsList;

    public static boolean isShellMaximized() {
        return shellMaximized;
    }

    public static void setShellMaximized(boolean shellMaximize) {
        shellMaximized = shellMaximize;
    }

    public static String[] getShowColumnsList() {
        return showColumnsList;
    }

    
    public static void setShowColumnsList(String[] showColumnsList) {
        ExternalRowGeneratorUiProperties.showColumnsList = showColumnsList;
    }

    
    public static Rectangle getBoundsRowGen() {
        return boundsRowGen;
    }

    
    public static void setBoundsRowGen(Rectangle boundsRowGen) {
        ExternalRowGeneratorUiProperties.boundsRowGen = boundsRowGen;
    }

    
    public static int[] getWeightsDatasFlowViewSashForm() {
        return weightsDatasFlowViewSashForm;
    }

    
    public static void setWeightsDatasFlowViewSashForm(int[] weightsDatasFlowViewSashForm) {
        ExternalRowGeneratorUiProperties.weightsDatasFlowViewSashForm = weightsDatasFlowViewSashForm;
    }

    
    public static int[] getWeightsMainSashForm() {
        return weightsMainSashForm;
    }

    
    public static void setWeightsMainSashForm(int[] weightsMainSashForm) {
        ExternalRowGeneratorUiProperties.weightsMainSashForm = weightsMainSashForm;
    }
}
