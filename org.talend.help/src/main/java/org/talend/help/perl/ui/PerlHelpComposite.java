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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.help.perl.model.EProperty;
import org.talend.help.perl.model.EType;
import org.talend.help.perl.model.Node;
import org.talend.help.perl.reader.DocParser;
import org.talend.help.perl.reader.IndexParser;
import org.talend.help.perl.searcher.FunctionSearcher;
import org.talend.help.perl.searcher.PlainSearcher;
import org.talend.help.perl.searcher.Searcher;

/**
 * PerlHelpComposite.java.
 * 
 */
/**
 * @author xue
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
            String searchStr = searchButton.getText();
            boolean flag = (LABEL_BLANK.equals(searchStr) || LABEL_SEARCH.equals(searchStr)) ? true : node
                    .isSearchMatchFlag();
            return flag;
        }
    }

    private TreeViewer viewer;

    private Text searchText;

    private Button searchButton;

    private static final String LABEL_SEARCH = "search";

    private static final String LABEL_BLANK = "";

    private static final String HEADTAG_HTML = "<html><head></head><body>";

    private static final String TAILTAG_HTML = "</body></html>";

    private Searcher searcher = null;

    private Searcher funcSearcher = null;

    private Searcher plainSearcher = null;

    private Browser htmlBrowser;

    private BackForwardBar backForwardBar;

    private Button funcBtn = null;

    private Button plainBtn = null;

    public PerlHelpComposite(Composite parent, int style) {
        super(parent, style);
        init();
    }

    /**
     * DOC Administrator PerlHelpComposite class global comment. Detailled comment <br/>
     * 
     * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ÐÇÆÚÎå, 29 ¾ÅÔÂ 2006) nrousseau $
     * 
     */
    class RadioSelection extends SelectionAdapter {

        public void widgetSelected(SelectionEvent e) {
            if (searcher != null) {
                searcherReset();
            }
            searcher = funcBtn.getSelection() ? funcSearcher : plainSearcher;
        }
    }

    private Composite creatSearchRadioComp(Composite parentComp) {
        Composite searchRadioComp = new Composite(parentComp, SWT.None);
        searchRadioComp.setLayout(new RowLayout());
        funcBtn = new Button(searchRadioComp, SWT.RADIO);
        funcBtn.setText("Fuction");
        funcBtn.setSelection(true);
        funcBtn.addSelectionListener(new RadioSelection());
        plainBtn = new Button(searchRadioComp, SWT.RADIO);
        plainBtn.setText("Plain Text");
        plainBtn.addSelectionListener(new RadioSelection());
        return searchRadioComp;
    }

    private Composite createLeftComponent() {
        Composite treeComposite = new Composite(this, SWT.None);
        treeComposite.setLayout(new GridLayout());
        Composite searchTextComp = new Composite(treeComposite, SWT.None);
        searchTextComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        searchTextComp.setLayout(new GridLayout(4, false));
        searchText = new Text(searchTextComp, SWT.BORDER);
        GridData textGd = new GridData();
        textGd.widthHint = 150;
        searchText.setLayoutData(textGd);
        searchButton = new Button(searchTextComp, SWT.None);
        GridData buttonGd = new GridData();
        // buttonGd.horizontalSpan = 1;
        searchButton.setLayoutData(buttonGd);
        searchButton.setText(LABEL_SEARCH);
        backForwardBar = new BackForwardBar(searchTextComp);
        creatSearchRadioComp(treeComposite).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        viewer = new TreeViewer(treeComposite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

        // viewer.setUseHashlookup(true);
        viewer.setLabelProvider(new TreeLabelProvider());
        viewer.setContentProvider(new TreeContentProvider());
        viewer.addFilter(new FunctionFilter());
        backForwardBar.setTreeViewer(viewer);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                StructuredSelection selection = (StructuredSelection) event.getSelection();
                Node node = (Node) selection.getFirstElement();
                if (node != null && node.getType() == EType.FUNCTION) {
                    Node anchorNode = node.getChildren().get(0);
                    String anchor = anchorNode.getProperties().get(EProperty.VALUE);
                    String htmlContent;
                    String midContent = "";
                    try {
                        midContent = DocParser.getInstance().getDoc(anchor);
                    } catch (IOException e) {
                        openError(e);
                    }
                    if (searcher.getMatchTextFlag()) {
                        htmlContent = HEADTAG_HTML + searcher.getHtmlByKey(node) + TAILTAG_HTML;
                        htmlBrowser.setText(htmlContent);
                        // searchAdapter.setMatchTextFlag(false);
                    } else {

                        htmlContent = HEADTAG_HTML + midContent + TAILTAG_HTML;
                        htmlBrowser.setText(htmlContent);
                    }
                    setFLText(midContent);
                    // NavNode navNode = getNavNode(node, htmlContent);
                    if (backForwardBar.isSelectTreeFlag()) {
                        backForwardBar.addToNav(node);
                        backForwardBar.setSelectTreeFlag(true);
                    }
                    return;
                }
                htmlBrowser.setText("");
                setFLText("");
            }
        });
        try {
            viewer.setInput(IndexParser.parse());
        } catch (Exception e1) {
            openError(e1);
        }
        searchText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                searcherReset();
                viewer.refresh();
            }
        });
        searchButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                searcher.processSearch();
            }
        });
        this.getShell().setDefaultButton(searchButton);
        funcSearcher = new FunctionSearcher(viewer, searchButton, searchText, backForwardBar);
        plainSearcher = new PlainSearcher(viewer, searchButton, searchText, backForwardBar);
        searcher = funcSearcher;
        return treeComposite;
    }

    /**
     * reset the search result.
     */
    private void searcherReset() {
        searchButton.setText(LABEL_SEARCH);
        searcher.setMatchTextFlag(false);
        searcher.clearSearchCache();
    }

    private static void openError(Exception e1) {
        Display workbenchDisplay = PlatformUI.getWorkbench().getDisplay();
        ErrorDialog.openError(workbenchDisplay.getActiveShell(), "Error occured", e1.getMessage(), null);
    }

    private void init() {

        Composite treeComposite = createLeftComponent();
        final Sash sash = new Sash(this, SWT.VERTICAL);
        Composite rihtComposite = creatRightComp();

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
        rihtComposite.setLayoutData(textData);

    }

    private Composite creatRightComp() {
        Composite rightComp = new Composite(this, SWT.None);
        GridLayout layout = new GridLayout();
        rightComp.setLayout(layout);
        htmlBrowser = new Browser(rightComp, SWT.BORDER);
        htmlBrowser.setText("");
        htmlBrowser.setLayoutData(new GridData(GridData.FILL_BOTH));
        backForwardBar.setBrowser(htmlBrowser);
        creatFLCopyComp(rightComp).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return rightComp;

    }

    private Text flText = null;

    private Composite creatFLCopyComp(Composite parentComp) {
        Composite flCopyComp = new Composite(parentComp, SWT.None);
        GridLayout layout = new GridLayout(3, false);
        flCopyComp.setLayout(layout);
        Label codeLabel = new Label(flCopyComp, SWT.None);
        codeLabel.setText("Code:");
        codeLabel.setLayoutData(new GridData());
        flText = new Text(flCopyComp, SWT.BORDER);
        flText.setText("");
        GridData textGD = new GridData();
        textGD.widthHint = 180;
        flText.setLayoutData(textGD);
        Button copyBtn = new Button(flCopyComp, SWT.None);
        copyBtn.setText("Copy");
        copyBtn.setLayoutData(new GridData());
        copyBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Clipboard clipboard = new Clipboard(null);
                String plainText = flText.getText();
                String rtfText = "{\\rtf1\\b " + plainText + "}";
                TextTransfer textTransfer = TextTransfer.getInstance();
                RTFTransfer rftTransfer = RTFTransfer.getInstance();
                clipboard
                        .setContents(new String[] { plainText, rtfText }, new Transfer[] { textTransfer, rftTransfer });
                clipboard.dispose();
            }
        });
        return flCopyComp;
    }

    private static final String TEXT_REGEX = "<a name=.*>(.*)</a>";

    /**
     * get the first line text content according the content of html page.
     * 
     * @param htmlContent the content of html page
     */
    private void setFLText(String htmlContent) {
        if ("".equals(htmlContent)) {
            flText.setText("");
        } else {
            Matcher matcher = Pattern.compile(TEXT_REGEX).matcher(htmlContent);
            matcher.find();
            String tempText = matcher.group(0);
            String textStr = tempText.replaceAll(TEXT_REGEX, "$1");
            flText.setText(textStr);
        }

    }

    @Override
    public void dispose() {
        htmlBrowser = null;
        searchText = null;
        super.dispose();
    }

}
