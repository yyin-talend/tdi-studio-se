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

import java.io.IOException;

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.help.perl.model.EProperty;
import org.talend.help.perl.model.EType;
import org.talend.help.perl.model.Node;
import org.talend.help.perl.reader.DocParser;
import org.talend.help.perl.reader.IndexParser;

/**
 * PerlHelpComposite.java.
 * 
 */
public class PerlHelpComposite extends Composite {

    /**
     * FunctionFilter.
     * 
     */
    private final class FunctionFilter extends ViewerFilter {

        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            Node node = (Node) element;
            // if (node.getType() == EType.FUNCTION) {
            // String search = searchText.getText();
            // if (!search.equals("")) {
            // String label = node.getProperties().get(EProperty.LABEL);
            // return label != null && label.contains(search);
            // }
            // }
            // if(LABLE_SEARCH.equals(searchText.getText())){
            // return
            // }
            String searchStr = searchButton.getText();
            boolean flag = (LABEL_BLANK.equals(searchStr) || LABEL_SEARCH.equals(searchStr)) ? true : node.isSearchMatchFlag();
            return flag;
        }
    }

    private Text searchText;

    private Button searchButton;

    private static final String LABEL_SEARCH = "search";

    private static final String LABEL_BLANK = "";

    private static final String HEADTAG_HTML = "<html><head></head><body>";

    private static final String TAILTAG_HTML = "</body></html>";

    private SearchAdapter searchAdapter = null;

    private Browser htmlText;

    public PerlHelpComposite(Composite parent, int style) {
        super(parent, style);
        init();
    }

    private Composite createLeftComponent() {
        Composite treeComposite = new Composite(this, SWT.None);
        treeComposite.setLayout(new GridLayout());
        Composite searchComposite = new Composite(treeComposite, SWT.None);
        searchComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        searchComposite.setLayout(new GridLayout(2, false));
        searchText = new Text(searchComposite, SWT.BORDER);
        GridData textGd = new GridData();
        textGd.widthHint = 150;
        searchText.setLayoutData(textGd);
        searchButton = new Button(searchComposite, SWT.None);
        GridData buttonGd = new GridData();
        // buttonGd.horizontalSpan = 1;
        searchButton.setLayoutData(buttonGd);
        searchButton.setText(LABEL_SEARCH);
        final TreeViewer viewer = new TreeViewer(treeComposite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

        // viewer.setUseHashlookup(true);
        viewer.setLabelProvider(new TreeLabelProvider());
        viewer.setContentProvider(new TreeContentProvider());
        viewer.addFilter(new FunctionFilter());
        searchAdapter = new SearchAdapter(viewer, searchButton, searchText);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                StructuredSelection selection = (StructuredSelection) event.getSelection();
                Node node = (Node) selection.getFirstElement();
                if (node != null && node.getType() == EType.FUNCTION) {
                    Node anchorNode = node.getChildren().get(0);
                    String anchor = anchorNode.getProperties().get(EProperty.VALUE);
                    String htmlContent;
                    try {
                        if (searchAdapter.getMatchTextFlag()) {
                            htmlContent = HEADTAG_HTML + searchAdapter.getHtmlByKey(node) + TAILTAG_HTML;
                            htmlText.setText(htmlContent);
//                            searchAdapter.setMatchTextFlag(false);
                        } else {
                            String midContent = DocParser.getInstance().getDoc(anchor);
                            htmlContent = HEADTAG_HTML + midContent + TAILTAG_HTML;
                            htmlText.setText(htmlContent);
                        }
                        return;
                    } catch (IOException e) {
                        openError(e);
                    }
                }
                htmlText.setText("");
            }
        });
        try {
            viewer.setInput(IndexParser.parse());
        } catch (Exception e1) {
            openError(e1);
        }
        searchText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                searchButton.setText(LABEL_SEARCH);
                viewer.refresh();
                searchAdapter.setMatchTextFlag(false);
                searchAdapter.clearSearchCache();
            }
        });
        searchButton.addSelectionListener(searchAdapter);
        this.getShell().setDefaultButton(searchButton);
        return treeComposite;
    }

    private static void openError(Exception e1) {
        Display workbenchDisplay = PlatformUI.getWorkbench().getDisplay();
        ErrorDialog.openError(workbenchDisplay.getActiveShell(), "Error occured", e1.getMessage(), null);
    }

    private void init() {

        Composite treeComposite = createLeftComponent();
        final Sash sash = new Sash(this, SWT.VERTICAL);
        htmlText = new Browser(this, SWT.BORDER);
        htmlText.setText("");

        final FormLayout form = new FormLayout();
        this.setLayout(form);

        FormData viewerData = new FormData();
        viewerData.left = new FormAttachment(0, 0);
        viewerData.right = new FormAttachment(sash, 0);
        viewerData.top = new FormAttachment(0, 0);
        viewerData.bottom = new FormAttachment(100, 0);
        treeComposite.setLayoutData(viewerData);

        final int limit = 20, percent = 45;
        final FormData sashData = new FormData();
        sashData.left = new FormAttachment(percent, 0);
        sashData.top = new FormAttachment(0, 0);
        sashData.bottom = new FormAttachment(100, 0);
        sash.setLayoutData(sashData);
        sash.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                Rectangle sashRect = sash.getBounds();
                Rectangle shellRect = getClientArea();
                int right = shellRect.width - sashRect.width - limit;
                e.x = Math.max(Math.min(e.x, right), limit);
                if (e.x != sashRect.x) {
                    sashData.left = new FormAttachment(0, e.x);
                    layout();
                }
            }
        });

        FormData textData = new FormData();
        textData.left = new FormAttachment(sash, 0);
        textData.right = new FormAttachment(100, 0);
        textData.top = new FormAttachment(0, 0);
        textData.bottom = new FormAttachment(100, 0);
        htmlText.setLayoutData(textData);

    }

    @Override
    public void dispose() {
        htmlText = null;
        searchText = null;
        super.dispose();
    }

}
