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
package org.talend.designer.mapper.ui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;
import org.eclipse.swt.custom.TextChangingEvent;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.talend.commons.ui.swt.colorstyledtext.UnnotifiableColorStyledText;
import org.talend.commons.ui.swt.proposal.ContentProposalAdapterExtended;
import org.talend.commons.ui.swt.proposal.ProposalUtils;
import org.talend.designer.abstractmap.model.tableentry.ITableEntry;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.ui.color.ColorInfo;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 *
 * $Id$
 *
 */
public class StyledTextHandler {

    private StyledText styledText;

    private ITableEntry currentEntry;

    private MapperManager mapperManager;

    private ContentProposalAdapterExtended contentProposalAdapter;

    private static final int MAX_WIDTH_PROPOSAL_STYLED_TEXT = 300;

    private static final int MAX_HEIGHT_PROPOSAL_STYLED_TEXT = 300;

    public StyledTextHandler(StyledText styledText, MapperManager mapperManager) {
        super();
        this.styledText = styledText;
        this.mapperManager = mapperManager;
        init();
    }

    /**
     * DOC amaumont Comment method "init".
     */
    private void init() {
        if (this.contentProposalAdapter == null && !mapperManager.componentIsReadOnly()) {
            this.contentProposalAdapter = ProposalUtils.getCommonProposal(styledText);
        }
        addListeners();
    }

    /**
     * DOC amaumont Comment method "addListeners".
     */
    private void addListeners() {
        styledText.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                refreshProposalSize();
            }

            public void focusLost(FocusEvent e) {

            }

        });

        styledText.addControlListener(new ControlListener() {

            public void controlMoved(ControlEvent e) {

            }

            public void controlResized(ControlEvent e) {
                refreshProposalSize();
            }

        });

        styledText.addExtendedModifyListener(new ExtendedModifyListener() {

            public void modifyText(ExtendedModifyEvent event) {
                // System.out.println("ExtendedModifyListener modify text");
                updateCellExpression();

            }

        });
        styledText.getContent().addTextChangeListener(new TextChangeListener() {

            public void textChanged(TextChangedEvent event) {
                highlightLineOfCursorPosition(styledText.getSelection());
            }

            public void textChanging(TextChangingEvent event) {
                // System.out.println("textChanging");
            }

            public void textSet(TextChangedEvent event) {
                // System.out.println("textSet");
            }

        });
        styledText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                highlightLineOfCursorPosition(styledText.getSelection());
            }

        });

        styledText.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                highlightLineOfCursorPosition(styledText.getSelection());
            }

            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });

        styledText.addVerifyKeyListener(new VerifyKeyListener() {

            public void verifyKey(VerifyEvent verifyEvent) {
                if (verifyEvent.character == '\r' && contentProposalAdapter != null
                        && contentProposalAdapter.isProposalOpened()) {
                    verifyEvent.doit = false;
                } else {
                    verifyEvent.doit = true;
                }
            }

        });

        styledText.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
                highlightLineOfCursorPosition(styledText.getSelection());
            }

            public void mouseDown(MouseEvent e) {
                highlightLineOfCursorPosition(styledText.getSelection());
            }

            public void mouseUp(MouseEvent e) {
            }

        });
    }

    private void updateCellExpression() {

        if (styledText.getText() != null && currentEntry != null
                && !styledText.getText().equals(currentEntry.getExpression())) {
            mapperManager.changeEntryExpression(currentEntry, styledText.getText());
        }

    }

    public StyledText getStyledText() {
        return this.styledText;
    }

    public ITableEntry getCurrentEntry() {
        return this.currentEntry;
    }

    public void setCurrentEntry(ITableEntry currentEntry) {
        this.currentEntry = currentEntry;
    }

    /**
     * DOC amaumont Comment method "setText".
     *
     * @param text
     * @param cursorPosition
     */
    public void setTextWithoutNotifyListeners(String text) {
        if (!text.equals(this.styledText.getText())) {
            ((UnnotifiableColorStyledText) this.styledText).setTextWithoutNotifyListeners(text);
        }
    }

    public int highlightLineOfCursorPosition(Point cursorPosition) {
        int countCR = 0;
        if (cursorPosition.y > 0 && cursorPosition.y <= this.styledText.getCharCount()) {
            countCR = this.styledText.getLineAtOffset(cursorPosition.y);
        }

        // System.out.println("\n"+countCR);
        int lineCount = this.styledText.getLineCount();
        // System.out.println(lineCount);
        this.styledText.setLineBackground(0, lineCount, null);
        this.styledText.setLineBackground(countCR, 1, ColorInfo.COLOR_HIGHLIGHTED_TEXT_ROW());
        return countCR;
    }

    private void refreshProposalSize() {
        if (contentProposalAdapter != null) {
            Rectangle maxSize = new Rectangle(0, 0, MAX_WIDTH_PROPOSAL_STYLED_TEXT, MAX_HEIGHT_PROPOSAL_STYLED_TEXT);
            Rectangle boundsStyledText = StyledTextHandler.this.getStyledText().getBounds();
            Rectangle intersect = boundsStyledText.intersection(maxSize);
            Point sizeProposal = new Point(intersect.width, intersect.height);
            contentProposalAdapter.setPopupSize(sizeProposal);
        }
    }

    public ContentProposalAdapterExtended getContentProposalAdapter() {
        return this.contentProposalAdapter;
    }

}
