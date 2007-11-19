/* -------------------
 * LineChartDemo4.java
 * -------------------
 * (C) Copyright 2003-2005, by Object Refinery Limited.
 *
 */

package org.talend.designer.components.thash.io;

import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple line chart using data from an {@link XYDataset}.
 */
public class PersistentDiskBenchsChart extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public PersistentDiskBenchsChart(String title) {

        super(title);
        
        createDemoPanel();
        
        
    }
    
    private static JFreeChart createChart(XYDataset dataset) {
        // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Persistent Hash Benchs for 1 file 2 Go, 50 pointers by file",      // chart title
            "Max random value for cursor position",                      // x axis label
            "Time in ms",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,  
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setUpperMargin(0.0);
        
        XYLineAndShapeRenderer renderer 
                = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setLegendLine(new Rectangle2D.Double(0.0, 0.0, 8.0, 6.0));
        return chart;
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public void createDemoPanel() {
        DefaultXYDataset dataset = new DefaultXYDataset();
        
        double[] a1 = new double[] {
        
        80000000

        ,118828


        ,90000000

        ,10684


        ,100000000

        ,10060


        ,110000000

        ,10274


        ,120000000

        ,9990


        ,130000000

        ,10361


        ,140000000

        ,11389


        ,150000000

        ,11217


        ,160000000

        ,11463


        ,170000000

        ,11311


        ,180000000

        ,11609


        ,190000000

        ,12192


        ,200000000

        ,12338


        ,210000000

        ,12094


        ,220000000

        ,16743


        ,230000000

        ,12853


        ,240000000

        ,14748


        ,250000000

        ,12662


        ,260000000

        ,13101


        ,270000000

        ,13499


        ,280000000

        ,14647


        ,290000000

        ,14033


        ,300000000

        ,17785


        ,310000000

        ,14682


        ,320000000

        ,14473


        ,330000000

        ,13785


        ,340000000

        ,16690


        ,350000000

        ,15800


        ,360000000

        ,15306


        ,370000000

        ,14442


        ,380000000

        ,15092


        ,390000000

        ,14837


        ,400000000

        ,16306


        ,410000000

        ,15426


        ,420000000

        ,16030


        ,430000000

        ,14776


        ,440000000

        ,15898


        ,450000000

        ,15555


        ,460000000

        ,18149


        ,470000000

        ,16245


        ,480000000

        ,16534


        ,490000000

        ,15809


        ,500000000

        ,16400


        ,510000000

        ,15913


        ,520000000

        ,17137


        ,530000000

        ,16334


        ,540000000

        ,16523


        ,550000000

        ,16247


        ,560000000

        ,16447


        ,570000000

        ,16174


        ,580000000

        ,18602


        ,590000000

        ,18063


        ,600000000

        ,17372


        ,610000000

        ,17515


        ,620000000

        ,16453


        ,630000000

        ,16725


        ,640000000

        ,18110


        ,650000000

        ,16974


        ,660000000

        ,18008


        ,670000000

        ,17433


        ,680000000

        ,18094


        ,690000000

        ,17632


        ,700000000

        ,18214


        ,710000000

        ,19389


        ,720000000

        ,18213


        ,730000000

        ,18465


        ,740000000

        ,17862


        ,750000000

        ,18584


        ,760000000

        ,17886


        ,770000000

        ,18013


        ,780000000

        ,17932


        ,790000000

        ,17716


        ,800000000

        ,17843


        ,810000000

        ,17840


        ,820000000

        ,19743


        ,830000000

        ,18927


        ,840000000

        ,19093


        ,850000000

        ,18809


        ,860000000

        ,18690


        ,870000000

        ,18654


        ,880000000

        ,18399


        ,890000000

        ,18229


        ,900000000

        ,18133


        ,910000000

        ,18126


        ,920000000

        ,17397


        ,930000000

        ,18024


        ,940000000

        ,20104


        ,950000000

        ,19676


        ,960000000

        ,19757


        ,970000000

        ,18822


        ,980000000

        ,18984


        ,990000000

        ,19265


        ,1000000000

        ,20228


        ,1010000000

        ,19450


        ,1020000000

        ,19193


        ,1030000000

        ,18887


        ,1040000000

        ,19429


        ,1050000000

        ,22575


        ,1060000000

        ,28583


        ,1070000000

        ,34156


        ,1080000000

        ,44282


        ,1090000000

        ,54639


        ,1100000000

        ,61963


        ,1110000000

        ,71241


        ,1120000000

        ,81224


        ,1130000000

        ,89446


        ,1140000000

        ,96670


        ,1150000000

        ,106552


        ,1160000000

        ,113916


        ,1170000000

        ,121566


        ,1180000000

        ,125975


        ,1190000000

        ,131409


        ,1200000000

        ,137152


        ,1210000000

        ,144130


        ,1220000000

        ,150530


        ,1230000000

        ,160182


        ,1240000000

        ,166995


        ,1250000000

        ,174495


        ,1260000000

        ,176151


        ,1270000000

        ,176097


        ,1280000000

        ,176772


        ,1290000000

        ,173931


        ,1300000000

        ,170586


        ,1310000000

        ,170874


        ,1320000000

        ,169254


        ,1330000000

        ,171258


        ,1340000000

        ,166625


        ,1350000000

        ,167097


        ,1360000000

        ,163966


        ,1370000000

        ,164508


        ,1380000000

        ,161639


        ,1390000000

        ,162760


        ,1400000000

        ,160263


        ,1410000000

        ,161776


        ,1420000000

        ,156983


        ,1430000000

        ,156789


        ,1440000000

        ,156494


        ,1450000000

        ,153430


        ,1460000000

        ,155770


        ,1470000000

        ,153804


        ,1480000000

        ,151176


        ,1490000000

        ,152034


        ,1500000000

        ,148041


        ,1510000000

        ,149885


        ,1520000000

        ,148503


        ,1530000000

        ,147499


        ,1540000000

        ,145891


        ,1550000000

        ,145148


        ,1560000000

        ,144501


        ,1570000000

        ,144826


        ,1580000000

        ,144300


        ,1590000000

        ,139854


        ,1600000000

        ,143228


        ,1610000000

        ,139304


        ,1620000000

        ,140332


        ,1630000000

        ,140568


        ,1640000000

        ,136043


        ,1650000000

        ,134825


        ,1660000000

        ,137621


        ,1670000000

        ,134891


        ,1680000000

        ,136132


        ,1690000000

        ,134241


        ,1700000000

        ,133381


        ,1710000000

        ,133089


        ,1720000000

        ,132207


        ,1730000000

        ,130506


        ,1740000000

        ,129397


        ,1750000000

        ,129509


        ,1760000000

        ,128392


        ,1770000000

        ,127501


        ,1780000000

        ,126986


        ,1790000000

        ,127258


        ,1800000000

        ,125873


        ,1810000000

        ,124513


        ,1820000000

        ,124928


        ,1830000000

        ,124686


        ,1840000000

        ,124317


        ,1850000000

        ,121953


        ,1860000000

        ,122756


        ,1870000000

        ,123384


        ,1880000000

        ,121840


        ,1890000000

        ,119023


        ,1900000000

        ,120249


        ,1910000000

        ,120025


        ,1920000000

        ,118784


        ,1930000000

        ,117961


        ,1940000000

        ,115967


        ,1950000000

        ,117754


        ,1960000000

        ,117032


        ,1970000000

        ,116669


        ,1980000000

        ,116294


        ,1990000000

        ,113115
        
    };
        
        
        
//        double[] x1 = new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
//        double[] y1 = new double[] {8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0};
        double[] x1 = new double[a1.length/2];
        double[] y1 = new double[a1.length/2];
        
        for (int i = 0; i < a1.length/2; i+= 1) {
            x1[i] = a1[i*2];
            y1[i] = a1[i*2+1];
        }
        
        double[][] data1 = new double[][] {x1, y1};
        dataset.addSeries("Test", data1);
        
//        double[] x2 = new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
//        double[] y2 = new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
//        double[][] data2 = new double[][] {x2, y2};
//        dataset.addSeries("60M", data2);
        
        JFreeChart chart = createChart(dataset);
        JPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }
    
    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public void createDemoPanel(DefaultXYDataset dataset) {
        JFreeChart chart = createChart(dataset);
        JPanel chartPanel =  new ChartPanel(chart);
        
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);

    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        PersistentDiskBenchsChart demo = new PersistentDiskBenchsChart("Persistent Hash Benchs");

        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
