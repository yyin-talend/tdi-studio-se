// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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

package org.talend.help.perl.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.help.perl.model.EImage;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ÐÇÆÚÎå, 29 ¾ÅÔÂ 2006) nrousseau $
 * 
 */
public class BackForwardBar {

    private Label backLabel = null;

    private Label forwardLabel = null;

    private Browser browser;

    public BackForwardBar(Composite parent) {

        initBackForwardLabel(parent);
    }

    public void setBrowser(Browser htmlBrowser) {
        this.browser = htmlBrowser;
    }

    private static final Image ENTER_BACKIMAGE = ImageProvider.getImage(EImage.ENTER_BACKIMAGE);

    private static final Image OUT_BACKIMAGE = ImageProvider.getImage(EImage.OUT_BACKIMAGE);

    private static final Image DISABLE_BACKIMAGE = ImageProvider.getImage(EImage.DISABLE_BACKIMAGE);

    private static final Image ENTER_FORWARDIMAGE = ImageProvider.getImage(EImage.ENTER_FORWARDIMAGE);

    private static final Image OUT_FORWARDIMAGE = ImageProvider.getImage(EImage.OUT_FORWARDIMAGE);

    private static final Image DISABLE_FORWARDIMAGE = ImageProvider.getImage(EImage.DISABLE_FORWARDIMAGE);

    private void initBackForwardLabel(Composite parentComp) {
        backLabel = new Label(parentComp, SWT.None);
        backLabel.setImage(DISABLE_BACKIMAGE);
        backLabel.setLayoutData(new GridData());
        backLabel.addMouseTrackListener(new MouseTrackAdapter() {

            public void mouseEnter(MouseEvent e) {
                if (currentNavIndex > 0) {
                    backLabel.setImage(ENTER_BACKIMAGE);
                }
            }

            public void mouseExit(MouseEvent e) {
                if (currentNavIndex > 0) {
                    backLabel.setImage(OUT_BACKIMAGE);
                }
            }

        });
        backLabel.addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent e) {
                if (0 == globalNavigation.size()) {
                    return;
                }
                int backIndex = currentNavIndex > 0 ? --currentNavIndex : currentNavIndex;
                if (currentNavIndex == 0) {
                    backLabel.setImage(DISABLE_BACKIMAGE);
                }
                browser.setText(globalNavigation.get(backIndex));
                fireNavItemState();
            }

        });
        forwardLabel = new Label(parentComp, SWT.None);
        forwardLabel.setImage(DISABLE_FORWARDIMAGE);
        forwardLabel.setLayoutData(new GridData());
        forwardLabel.addMouseTrackListener(new MouseTrackAdapter() {

            public void mouseEnter(MouseEvent e) {
                if (currentNavIndex < (globalNavigation.size() - 1)) {
                    forwardLabel.setImage(ENTER_FORWARDIMAGE);
                }
            }

            public void mouseExit(MouseEvent e) {
                if (currentNavIndex < (globalNavigation.size() - 1)) {
                    forwardLabel.setImage(OUT_FORWARDIMAGE);
                }
            }

        });
        forwardLabel.addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent e) {
                if (0 == globalNavigation.size()) {
                    return;
                }
                int forwardIndex = currentNavIndex < (globalNavigation.size() - 1) ? ++currentNavIndex
                        : currentNavIndex;
                if (currentNavIndex == globalNavigation.size() - 1) {
                    forwardLabel.setImage(DISABLE_FORWARDIMAGE);
                }
                browser.setText(globalNavigation.get(forwardIndex));
                fireNavItemState();
            }

        });
    }

    private List<String> globalNavigation = new ArrayList<String>();

    private int currentNavIndex = -1;

    /**
     * add the selected html page to navigation list.
     * 
     * @param htmlContent the content of selected html page
     */
    public void addToNav(String htmlContent) {
        int size = globalNavigation.size();
        for (int i = size - 1; i > currentNavIndex; i--) {
            globalNavigation.remove(i);
        }
        globalNavigation.add(htmlContent);
        currentNavIndex++;
        fireNavItemState();
    }

    private void fireNavItemState() {
        if (currentNavIndex > 0) {
            backLabel.setImage(OUT_BACKIMAGE);
        }
        if (currentNavIndex < (globalNavigation.size() - 1)) {
            forwardLabel.setImage(OUT_FORWARDIMAGE);
        }
    }
}
