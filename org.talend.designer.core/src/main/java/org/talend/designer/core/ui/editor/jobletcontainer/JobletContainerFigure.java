package org.talend.designer.core.ui.editor.jobletcontainer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.utils.image.ColorUtils;
import org.talend.commons.ui.utils.workbench.gef.SimpleHtmlFigure;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.properties.Project;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.repository.ProjectManager;

public class JobletContainerFigure extends Figure {

    private ImageFigure errorFigure, warningFigure;

    private SimpleHtmlFigure htmlStatusHint;

    private JobletContainer jobletContainer;

    private RoundedRectangle outlineFigure;

    private SimpleHtmlFigure titleFigure;

    private RoundedRectangle rectFig;

    private JobletCollapseFigure collapseFigure;

    private boolean showTitle;

    private String title;

    private RGB subjobTitleColor;

    private RGB red = new RGB(250, 72, 80);

    private RGB green = new RGB(130, 240, 100);

    private Map<String, SimpleHtmlFigure> mrFigures = new HashMap<String, SimpleHtmlFigure>();

    /**
     * DOC hwang JobletContainerFigure constructor comment.
     * 
     * @param model
     */
    public JobletContainerFigure(final JobletContainer jobletContainer) {
        setLayoutManager(new FreeformLayout());
        this.jobletContainer = jobletContainer;

        outlineFigure = new RoundedRectangle();
        rectFig = new RoundedRectangle();
        titleFigure = new SimpleHtmlFigure();
        titleFigure.setOpaque(true);
        collapseFigure = new JobletCollapseFigure();

        collapseFigure.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                doCollapse();
            }
        });
        errorFigure = new ImageFigure();
        errorFigure.setImage(ImageProvider.getImage(EImage.ERROR_SMALL));
        errorFigure.setVisible(false);
        errorFigure.setSize(errorFigure.getPreferredSize());
        this.add(errorFigure);

        warningFigure = new ImageFigure();
        warningFigure.setImage(ImageProvider.getImage(EImage.WARNING_SMALL));
        warningFigure.setVisible(false);
        warningFigure.setSize(warningFigure.getPreferredSize());
        this.add(warningFigure);

        initMRFigures();

        htmlStatusHint = new SimpleHtmlFigure();

        initSubJobTitleColor();

        updateData();

        initializejobletContainer(jobletContainer.getJobletContainerRectangle());
        if (jobletContainer.getNode().isMapReduceStart()) {
            refreshNodes(false);
        }
    }

    public void doCollapse() {
        if (jobletContainer != null && !jobletContainer.isReadOnly()) {
            PropertyChangeCommand ppc = new PropertyChangeCommand(jobletContainer, EParameterName.COLLAPSED.getName(),
                    !jobletContainer.isCollapsed());
            IProcess ipro = jobletContainer.getNode().getProcess();
            ppc.execute();
            reSelection();
        }
    }

    /**
     * hwang Comment method "initSubJobTitleColor".
     */
    private void initSubJobTitleColor() {
        IElementParameter colorParameter = jobletContainer.getElementParameter(EParameterName.SUBJOB_TITLE_COLOR.getName());
        // Color titleColor = ColorUtils.SUBJOB_TITLE_COLOR;
        //        if (jobletContainer.getJobletStartNode().getComponent().getName().equals("tPrejob") //$NON-NLS-1$
        //                || jobletContainer.getJobletStartNode().getComponent().getName().equals("tPostjob")) { //$NON-NLS-1$
        // // titleColor = ColorUtils.SPECIAL_SUBJOB_TITLE_COLOR;
        // }
        // RGB defaultSubjobColor =
        // DesignerColorUtils.getPreferenceSubjobRGB(DesignerColorUtils.SUBJOB_TITLE_COLOR_NAME,
        // DesignerColorUtils.SUBJOB_TITLE_COLOR);
        // if (colorParameter.getValue() == null) {
        // subjobTitleColor = defaultSubjobColor;
        // String colorValue = ColorUtils.getRGBValue(subjobTitleColor);
        // colorParameter.setValue(colorValue);
        // } else {
        // String strRgb = (String) colorParameter.getValue();
        // subjobTitleColor = ColorUtils.parseStringToRGB(strRgb, defaultSubjobColor);
        // }
    }

    private void reSelection() {
        // select the start node.
        if (jobletContainer.isCollapsed()) {
            IProcess2 process = jobletContainer.getProcess();
            if (process == null) {
                return;
            }
            AbstractMultiPageTalendEditor editor = (AbstractMultiPageTalendEditor) process.getEditor();
            Node startNode = jobletContainer.getJobletStartNode();
            if (startNode != null && editor != null) {
                editor.selectNode(startNode);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
     */
    @Override
    public void paint(Graphics graphics) {
        graphics.setAlpha(100);
        if (errorFigure.isVisible()) {
            errorFigure.setLocation(jobletContainer.getErrorLocation());
        }
        if (warningFigure.isVisible()) {
            warningFigure.setLocation(jobletContainer.getWarningLocation());
        }
        super.paint(graphics);
        refreshNodes(false);
    }

    boolean lastJobletRedState = false;

    public void refreshNodes(boolean isClear) {
        if (this.jobletContainer.getNode().isJoblet()) {
            boolean isRed = new JobletUtil().isRed(this.jobletContainer);
            Project refProject = ProjectManager.getInstance().getProject(
                    this.jobletContainer.getProcess().getProperty().getItem());
            if (!ProjectManager.getInstance().isInCurrentMainProject(refProject)) {
                if (!this.jobletContainer.isCollapsed()) {
                    isRed = true;
                }
            }
            if (this.jobletContainer.isCollapsed() && lastJobletRedState == isRed) {
                return;
            }
            lastJobletRedState = isRed;
            if (isRed && rectFig != null) {
                rectFig.setBackgroundColor(ColorUtils.getCacheColor(red));
            } else if (rectFig != null) {
                rectFig.setBackgroundColor(ColorUtils.getCacheColor(green));
            }
            if (isRed && outlineFigure != null) {
                outlineFigure.setBackgroundColor(ColorUtils.getCacheColor(red));
            } else if (outlineFigure != null) {
                outlineFigure.setBackgroundColor(ColorUtils.getCacheColor(green));
            }

            if (!jobletContainer.isCollapsed()) {
                for (Object ele : jobletContainer.getElements()) {
                    if (ele instanceof Node) {
                        ((Node) ele).setReadOnly(isRed);
                    }
                }
            }
        } else if (this.jobletContainer.getNode().isMapReduce()) {
            if (!this.jobletContainer.getNode().isMapReduceStart()) {
                rectFig.setVisible(false);
                outlineFigure.setVisible(false);
                return;
            }
            this.jobletContainer.updateJobletNodes(true);
            if (rectFig != null) {
                rectFig.setBackgroundColor(ColorUtils.getCacheColor(green));
                rectFig.setVisible(true);
            }

            if (outlineFigure != null) {
                outlineFigure.setVisible(false);
            }

            Iterator<Entry<String, SimpleHtmlFigure>> ite = mrFigures.entrySet().iterator();
            while (ite.hasNext()) {
                Entry<String, SimpleHtmlFigure> entry = ite.next();
                String key = entry.getKey();
                SimpleHtmlFigure value = entry.getValue();
                Double percent = new Double(0);
                if (key.startsWith("map_")) {
                    if (!"".equals(jobletContainer.getMrName()) && jobletContainer.getMrName() != null) {
                        percent = jobletContainer.getPercentMap() * 10;
                        value.setVisible(true);
                    }
                }

                if (key.startsWith("reduce_")) {
                    if (!"".equals(jobletContainer.getMrName()) && jobletContainer.getMrName() != null) {
                        percent = jobletContainer.getPercentReduce() * 10;
                        value.setVisible(true);
                    }
                }

                if (value.isVisible()) {
                    Integer i = Integer.parseInt(key.substring(key.indexOf("_") + 1)) + 1;
                    if (i.toString().equals(jobletContainer.getMrName()) || isClear) {
                        List object = value.getChildren();
                        for (Object o : object) {
                            if (o instanceof RectangleFigure) {
                                setProgressData((RectangleFigure) o, percent, 8);
                            }
                        }
                    }
                }

            }

        }

    }

    public void initializejobletContainer(Rectangle rectangle) {
        // disposeColors();
        Point location = this.getLocation();
        collapseFigure.setCollapsed(jobletContainer.isCollapsed());
        collapseFigure.setVisible(this.jobletContainer.getNode().isJoblet());
        titleFigure.setText("<b> " + title + "</b>"); //$NON-NLS-1$ //$NON-NLS-2$
        Dimension preferedSize = titleFigure.getPreferredSize();
        preferedSize = preferedSize.getExpanded(0, 3);
        // rectangle.width += 32;

        collapseFigure.setLocation(new Point(location.x, location.y));
        collapseFigure.setSize(preferedSize.height, preferedSize.height);

        titleFigure.setSize(preferedSize.width, preferedSize.height - 2);
        titleFigure.setLocation(new Point((rectangle.width - preferedSize.width) / 2 + location.x, location.y));
        titleFigure.setVisible(showTitle);

        outlineFigure.setLocation(new Point(location.x, location.y));
        outlineFigure.setVisible(showTitle);
        outlineFigure.setForegroundColor(ColorUtils.getCacheColor(new RGB(220, 120, 120)));
        outlineFigure.setSize(rectangle.width, preferedSize.height);

        Iterator<Entry<String, SimpleHtmlFigure>> ite = mrFigures.entrySet().iterator();
        int i = 0;
        while (ite.hasNext()) {
            Entry<String, SimpleHtmlFigure> entry = ite.next();
            String key = entry.getKey();
            SimpleHtmlFigure value = entry.getValue();
            Integer count = this.jobletContainer.getNode().getMrJobInGroupCount();
            i = Integer.parseInt(key.substring(key.indexOf("_") + 1));
            int mry = preferedSize.height * i;
            if (key.startsWith("map_")) {
                value.setLocation(new Point(location.x+10, location.y + rectangle.height - count * preferedSize.height + mry));
            }
            if (key.startsWith("reduce_")) {
                value.setLocation(new Point(location.x + 120, location.y + rectangle.height - count * preferedSize.height + mry));
            }

        }

        // collapseFigure.setBackgroundColor(new Color(null, 50, 50, 250));

        rectFig.setLocation(new Point(location.x, /* preferedSize.height + */location.y));
        rectFig.setSize(new Dimension(rectangle.width, rectangle.height /*- preferedSize.height*/));
        if (this.jobletContainer.getNode().isJoblet()) {
            if (new JobletUtil().isRed(this.jobletContainer)) {
                rectFig.setBackgroundColor(ColorUtils.getCacheColor(red));
                outlineFigure.setBackgroundColor(ColorUtils.getCacheColor(red));
            } else {
                rectFig.setBackgroundColor(ColorUtils.getCacheColor(green));
                outlineFigure.setBackgroundColor(ColorUtils.getCacheColor(green));
            }
        } else {
            rectFig.setBackgroundColor(ColorUtils.getCacheColor(green));
            outlineFigure.setBackgroundColor(ColorUtils.getCacheColor(green));
            // progressFigure.setBackgroundColor(new Color(Display.getDefault(), red));
            if (!this.jobletContainer.getNode().isMapReduceStart()) {
                rectFig.setVisible(false);
                outlineFigure.setVisible(false);
            }

        }

        rectFig.setForegroundColor(ColorUtils.getCacheColor(new RGB(220, 120, 120)));
    }

    public void disposeColors() {
        if (rectFig.getForegroundColor() != null && !rectFig.getForegroundColor().isDisposed()) {
            rectFig.getForegroundColor().dispose();
        }
        if (rectFig.getBackgroundColor() != null && !rectFig.getBackgroundColor().isDisposed()) {
            rectFig.getBackgroundColor().dispose();
        }
        if (outlineFigure.getForegroundColor() != null && !outlineFigure.getForegroundColor().isDisposed()) {
            outlineFigure.getForegroundColor().dispose();
        }
        if (outlineFigure.getBackgroundColor() != null && !outlineFigure.getBackgroundColor().isDisposed()) {
            outlineFigure.getBackgroundColor().dispose();
        }

        Iterator<Entry<String, SimpleHtmlFigure>> ite = mrFigures.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, SimpleHtmlFigure> entry = ite.next();
            SimpleHtmlFigure value = entry.getValue();
            if (value.getForegroundColor() != null && !value.getForegroundColor().isDisposed()) {
                value.getForegroundColor().dispose();
                // for (Object o : value.getChildren()) {
                // if (o instanceof Figure) {
                // if (((Figure) o).getForegroundColor() != null && !((Figure) o).getForegroundColor().isDisposed()) {
                // ((Figure) o).getForegroundColor().dispose();
                // }
                // }
                // }
            }
            if (value.getBackgroundColor() != null && !value.getBackgroundColor().isDisposed()) {
                value.getBackgroundColor().dispose();
                // for (Object o : value.getChildren()) {
                // if (o instanceof Figure) {
                // if (((Figure) o).getBackgroundColor() != null && !((Figure) o).getBackgroundColor().isDisposed()) {
                // ((Figure) o).getBackgroundColor().dispose();
                // }
                // }
                // }
            }
        }
    }

    /**
     * hwang Comment method "updateSubJobTitleColor".
     */
    public void updateSubJobTitleColor() {
        String rgb = (String) jobletContainer.getPropertyValue(EParameterName.SUBJOB_TITLE_COLOR.getName());
        if (rgb != null && !"".equals(rgb)) { //$NON-NLS-1$
            subjobTitleColor = ColorUtils.parseStringToRGB(rgb);
        } else {
            initSubJobTitleColor();
        }
        updateData();
    }

    /**
     * hwang Comment method "updateData".
     */
    public void updateData() {

        showTitle = !jobletContainer.isCollapsed();

        title = (String) jobletContainer.getPropertyValue(EParameterName.SUBJOB_TITLE.getName());

        this.getChildren().remove(outlineFigure);
        this.getChildren().remove(rectFig);
        outlineFigure.getChildren().clear();
        rectFig.getChildren().clear();

        if (showTitle) {
            outlineFigure.add(titleFigure);
            outlineFigure.add(collapseFigure);
            add(rectFig, null, 0);
            add(outlineFigure, null, 1);
        } else {
            outlineFigure.add(titleFigure);
            rectFig.add(collapseFigure);
            add(outlineFigure, null, 0);
            add(rectFig, null, 1);
        }

        Iterator<Entry<String, SimpleHtmlFigure>> ite = mrFigures.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, SimpleHtmlFigure> entry = ite.next();
            SimpleHtmlFigure value = entry.getValue();
            add(value, null, 2);
        }

        if (!this.jobletContainer.getNode().isMapReduceStart() && !this.jobletContainer.getNode().isJoblet()) {
            rectFig.setVisible(false);
            outlineFigure.setVisible(false);
        }
    }

    public void updateStatus(int status) {
        if ((status & Process.ERROR_STATUS) != 0) {
            warningFigure.setVisible(false);
            errorFigure.setVisible(true);
        } else {
            errorFigure.setVisible(false);
            errorFigure.setToolTip(null);
        }

        if (((status & Process.WARNING_STATUS) != 0) && !errorFigure.isVisible()) {
            warningFigure.setVisible(true);
        } else {
            warningFigure.setVisible(false);
            warningFigure.setToolTip(null);
        }

        if (warningFigure.isVisible() || errorFigure.isVisible()) {
            List<String> problemsList;

            String text = "<b>" + jobletContainer.getNode().getUniqueName() + "</b><br><br>"; //$NON-NLS-1$ //$NON-NLS-2$

            if ((status & Process.WARNING_STATUS) != 0) {
                text += "<i>Warnings:</i><br>"; //$NON-NLS-1$

                problemsList = Problems.getStatusList(ProblemStatus.WARNING, jobletContainer.getNode());
                for (String str : problemsList) {
                    text += "\t- " + str + "<br>"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            if ((status & Process.ERROR_STATUS) != 0) {
                text += "<i>Errors:</i><br>"; //$NON-NLS-1$
                problemsList = Problems.getStatusList(ProblemStatus.ERROR, jobletContainer.getNode());
                for (String str : problemsList) {
                    text += "\t- " + str + "<br>"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            htmlStatusHint.setText(text);
            if (errorFigure.isVisible()) {
                errorFigure.setToolTip(htmlStatusHint);
            } else if (warningFigure.isVisible()) {
                warningFigure.setToolTip(htmlStatusHint);
            }
        } else {
            errorFigure.setVisible(false);
            errorFigure.setToolTip(null);
        }
    }

    private void initMRFigures() {
        Integer mrCount = this.jobletContainer.getNode().getMrJobInGroupCount();
        if (!jobletContainer.getNode().isMapReduceStart()) {
            return;
        }
        if (this.jobletContainer.getNode().getMrGroupId() == null) {
            return;
        }
        if (mrCount == null) {
            mrCount = 1;
        }
        if (mrCount != null) {
            for (int i = 0; i < mrCount; i++) {
                SimpleHtmlFigure progressMap = new SimpleHtmlFigure();
                // progressMap.setOutline(false);
                progressMap.setOpaque(false);
                Label mapTip = new Label();
                mapTip.setText("Map ");
                progressMap.setToolTip(mapTip);
                progressMap.setLayoutManager(new ToolbarLayout(true));
                progressMap.setVisible(false);

                SimpleHtmlFigure mapTitle = new SimpleHtmlFigure();
                mapTitle.setText("<font color='#000000'> <b> " + "Map " + "</b></font>");
                mapTitle.setOpaque(false);
                mapTitle.setBackgroundColor(ColorUtils.getCacheColor(green));
                mapTitle.setForegroundColor(ColorUtils.getCacheColor(green));

                RectangleFigure mapGreen = new RectangleFigure();
                mapGreen.setSize(60, mapTitle.getPreferredSize().height);
                mapGreen.setPreferredSize(60, mapTitle.getPreferredSize().height + 5);
                mapGreen.setBorder(new LineBorder(ColorConstants.black, 1));
                mapGreen.setLayoutManager(new ToolbarLayout(true));
                mapGreen.setLocation(new Point(progressMap.getLocation().x + mapTitle.getPreferredSize().width, progressMap
                        .getLocation().y));
                mapGreen.setVisible(true);
                mapGreen.setOpaque(true);
                progressMap.add(mapTitle, 0);
                progressMap.add(mapGreen, 1);

                progressMap.setSize(mapTitle.getPreferredSize().width + mapGreen.getPreferredSize().width,
                        mapTitle.getPreferredSize().height + 1);
                progressMap.setPreferredSize(mapTitle.getPreferredSize().width + mapGreen.getPreferredSize().width,
                        mapTitle.getPreferredSize().height + 1);
                mrFigures.put("map_" + i, progressMap);
                // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                SimpleHtmlFigure progressReduce = new SimpleHtmlFigure();
                // progressReduce.setOutline(false);
                progressReduce.setOpaque(false);
                Label reduceTip = new Label();
                reduceTip.setText("Reduce ");
                progressReduce.setToolTip(reduceTip);
                progressReduce.setLayoutManager(new ToolbarLayout(true));
                progressReduce.setVisible(false);

                SimpleHtmlFigure reduceTitle = new SimpleHtmlFigure();
                reduceTitle.setText("<font color='#000000'> <b> " + "Reduce " + "</b></font>");
                reduceTitle.setOpaque(false);
                reduceTitle.setBackgroundColor(ColorUtils.getCacheColor(green));
                reduceTitle.setForegroundColor(ColorUtils.getCacheColor(green));

                RectangleFigure reduceGreen = new RectangleFigure();
                reduceGreen.setSize(60, reduceTitle.getPreferredSize().height);
                reduceGreen.setPreferredSize(60, reduceTitle.getPreferredSize().height + 5);
                reduceGreen.setBorder(new LineBorder(ColorConstants.black, 1));
                reduceGreen.setLayoutManager(new ToolbarLayout(true));
                reduceGreen.setLocation(new Point(progressReduce.getLocation().x + reduceTitle.getPreferredSize().width,
                        progressReduce.getLocation().y));
                reduceGreen.setVisible(true);
                reduceGreen.setOpaque(true);
                progressReduce.add(reduceTitle, 0);
                progressReduce.add(reduceGreen, 1);

                progressReduce.setSize(reduceTitle.getPreferredSize().width + reduceGreen.getPreferredSize().width,
                        reduceTitle.getPreferredSize().height + 1);
                progressReduce.setPreferredSize(reduceTitle.getPreferredSize().width + reduceGreen.getPreferredSize().width,
                        reduceTitle.getPreferredSize().height + 1);
                mrFigures.put("reduce_" + i, progressReduce);
            }
        }
    }

    public void setProgressData(RectangleFigure progressBarFigure, Double extentString, int extent) {
        progressBarFigure.getChildren().clear();
        int nodeX = progressBarFigure.getLocation().x;
        int nodeY = progressBarFigure.getLocation().y;

        if (extentString == 10) {
            ImageFigure progressDataFigure = new ImageFigure();
            Image image = ImageProvider.getImage(ECoreImage.MRGREEBAR);
            progressDataFigure.setImage(image);
            progressDataFigure.setVisible(true);
            progressBarFigure.add(progressDataFigure);
        } else if (extentString > 0 && extentString < 10) {
            for (int i = 0; i < extentString; i++) {
                ImageFigure progressDataFigure = new ImageFigure();
                Image image = ImageProvider.getImage(ECoreImage.MRREDBAR);
                progressDataFigure.setImage(image);
                progressDataFigure.setVisible(true);
                progressBarFigure.add(progressDataFigure);
                int imageWith = image.getImageData().width;
                if (i != 0) {
                    Point point = new Point(nodeX + i * imageWith, nodeY);
                    progressDataFigure.setLocation(point);
                }
            }
            for (int j = 0; j < (10 - extentString); j++) {
                ImageFigure progressDataFigure = new ImageFigure();
                Image image = ImageProvider.getImage(ECoreImage.MRGRAYBAR);
                progressDataFigure.setImage(image);
                progressDataFigure.setVisible(true);
                progressBarFigure.add(progressDataFigure);
                int imageWith = image.getImageData().width;
                if (j != 0) {
                    Point point = new Point(nodeX + extentString * imageWith + j * imageWith, nodeY);
                    progressDataFigure.setLocation(point);
                }
            }
        }
    }
}
