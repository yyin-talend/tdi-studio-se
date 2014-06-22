package org.talend.designer.components.exchange.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.designer.components.exchange.ExchangePlugin;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class ReviewComponentDialog extends TitleAreaDialog {

    private Text titleText;

    private Text reviewText;

    private Label rateLabel;

    private Button submitBtn;

    private Map<Integer, Image> imageMap = new HashMap<Integer, Image>();

    private BidiMap rateBtnMap = new DualHashBidiMap();

    private boolean done = false;

    private int rating = 0;

    private String title;

    private String review;

    public ReviewComponentDialog(Shell shell) {
        super(shell);
    }

    protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        setTitle("Review the component");
        setMessage("Please give a review for the component.", IMessageProvider.INFORMATION);
        return contents;
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        Composite container = new Composite(composite, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.marginBottom = 15;
        gridLayout.marginLeft = 15;
        gridLayout.marginRight = 15;
        gridLayout.marginTop = 15;
        container.setLayout(gridLayout);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));

        final Label titleLabel = new Label(container, SWT.NONE);
        titleLabel.setText("Title:*");

        titleText = new Text(container, SWT.BORDER);
        titleText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        titleText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                checkInput();
            }
        });

        createRatingComp(container);

        final Label reviewLabel = new Label(container, SWT.NONE);
        reviewLabel.setText("Review:*");

        reviewText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
        reviewText.setLayoutData(new GridData(GridData.FILL_BOTH));
        reviewText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                checkInput();
            }
        });

        checkInput();

        return composite;
    }

    private void createRatingComp(Composite parent) {
        initImages();
        final Composite rateBtnComp = new Composite(parent, SWT.NONE);
        final GridLayout rateLayout = new GridLayout();
        rateLayout.numColumns = 5;
        rateBtnComp.setLayout(rateLayout);
        rateBtnComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        for (int i = 0; i < 5; i++) {
            final Button rateButton = new Button(rateBtnComp, SWT.NONE);
            rateButton.setLayoutData(new GridData(30, SWT.DEFAULT));
            rateButton.setImage(imageMap.get(1));
            rateButton.setData(0);
            rateBtnMap.put(i, rateButton);
            rateButton.addMouseTrackListener(new MouseTrackListener() {

                public void mouseHover(MouseEvent e) {
                    handleMouseHover(rateButton);
                }

                public void mouseExit(MouseEvent e) {
                    handleMouseExit(rateButton);
                }

                public void mouseEnter(MouseEvent e) {
                }
            });
            rateButton.addMouseListener(new MouseListener() {

                public void mouseUp(MouseEvent e) {
                }

                public void mouseDown(MouseEvent e) {
                    handleMouseDown(rateButton);
                    checkInput();
                }

                public void mouseDoubleClick(MouseEvent e) {
                }
            });
        }

        rateLabel = new Label(parent, SWT.NONE);
        rateLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        rateLabel.setText("Please select your rating*");
    }

    private void handleMouseHover(Button button) {
        if (done) {
            return;
        }
        int i = (Integer) rateBtnMap.getKey(button);
        for (int j = 0; j < i + 1; j++) {
            Button btn = (Button) rateBtnMap.get(j);
            btn.setImage(imageMap.get(2));
        }
        displayRateLabel(i + 1);
    }

    private void handleMouseExit(Button button) {
        if (done) {
            return;
        }
        Collection btns = rateBtnMap.values();
        for (Object obj : btns) {
            Button btn = (Button) obj;
            btn.setImage(imageMap.get(1));
        }
        displayRateLabel(0);
    }

    private void handleMouseDown(Button button) {
        // done = !done;
        done = true;

        int i = (Integer) rateBtnMap.getKey(button);
        List<Button> btns = new ArrayList<Button>(rateBtnMap.values());
        for (int j = 0; j < btns.size(); j++) {
            Button btn = btns.get(j);
            if (j <= i) {
                btn.setImage(imageMap.get(3));
            } else {
                btn.setImage(imageMap.get(1));
            }
        }
        rating = i + 1;
        displayRateLabel(rating);
    }

    private void displayRateLabel(int rating) {
        switch (rating) {
        case 1:
            rateLabel.setText("Very Poor: 1 star out of 5");
            break;
        case 2:
            rateLabel.setText("Fair: 2 stars out of 5");
            break;
        case 3:
            rateLabel.setText("Average: 3 stars out of 5");
            break;
        case 4:
            rateLabel.setText("Good: 4 stars out of 5");
            break;
        case 5:
            rateLabel.setText("Excellent!: 5 stars out of 5");
            break;

        default:
            rateLabel.setText("Please select your rating*");
            break;
        }
    }

    private void checkInput() {
        setError(null);
        String title = StringUtils.trimToNull(titleText.getText());
        String review = StringUtils.trimToNull(reviewText.getText());
        if (title == null) {
            setError("Title can not be null.");
            return;
        }
        if (rating == 0) {
            setError("Please give a rating.");
            return;
        }
        if (review == null) {
            setError("Review can not be null.");
            return;
        }
    }

    private void setError(String s) {
        setErrorMessage(s);
        if (submitBtn != null) {
            submitBtn.setEnabled(s == null);
        }
    }

    private void initImages() {
        imageMap.put(1, ExchangePlugin.getImageDescriptor("icons/star1.png").createImage());
        imageMap.put(2, ExchangePlugin.getImageDescriptor("icons/star2.png").createImage());
        imageMap.put(3, ExchangePlugin.getImageDescriptor("icons/star3.png").createImage());
    }

    protected int getShellStyle() {
        return super.getShellStyle() | SWT.RESIZE | SWT.MAX | SWT.MIN;
    }

    protected void createButtonsForButtonBar(Composite parent) {
        submitBtn = createButton(parent, IDialogConstants.OK_ID, "Submit Review", true);
        submitBtn.setEnabled(false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
    }

    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            title = titleText.getText();
            review = reviewText.getText();
        }
        super.buttonPressed(buttonId);
    }

    protected Point getInitialSize() {
        return new Point(550, 500);
    }

    public String getTitle() {
        return title;
    }

    public String getReview() {
        return review;
    }

    public int getRating() {
        return rating;
    }

}
