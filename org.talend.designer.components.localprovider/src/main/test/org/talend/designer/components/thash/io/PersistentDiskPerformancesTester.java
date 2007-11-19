// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
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
package org.talend.designer.components.thash.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.jfree.data.xy.DefaultXYDataset;
import org.talend.designer.components.thash.io.MultiPointersMultiHashFiles.MultiReadPointersFileHandler;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class PersistentDiskPerformancesTester {

    String folderStatsPath = "/home/amaumont/hash_benchs/";

    String fileHashBenchsBaseName = "PerfDisk";

    /**
     * DOC amaumont PersistentDiskPerformancesTester constructor comment.
     * 
     * @throws IOException
     */
    public PersistentDiskPerformancesTester() throws IOException {
        super();
    }

    public static void main(String[] args) throws IOException {
        new PersistentDiskPerformancesTester().run2();
    }

    /**
     * DOC amaumont Comment method "run".
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void run() throws IOException {
        
        String containerFilePath = "/home/amaumont/hash_benchs/talend_hash_perfs2";

        RandomAccessFile ra = new RandomAccessFile(containerFilePath, "r");
        long length = ra.length();

        long mediumPosition = length / 2;
        long limitPosition = length / 50;

        ra.seek(0);

        int loop = 100000;

        long seekStart = 0;
        long seekMedium = 0;
        long seekEnd = 0;

        long time = 0;

        byte[] buffer = new byte[130];

        Random random = new Random(System.currentTimeMillis());

        DefaultXYDataset dataset = new DefaultXYDataset();

        List<Double> x1 = new ArrayList<Double>();
        List<Double> y1 = new ArrayList<Double>();

//        int randomValue = (int) length / 40;

        System.out.println("run : for 1,2 Go");
        
        for (int randomValue = 80000000; randomValue < length; randomValue += 10000000) { // 1,2 Go
//        for (int randomValue = 1000000; randomValue < length; randomValue += 1000000) { // 120 Mo
//            for (int randomValue = 1000000; randomValue < length; randomValue += 10000000) { // 120 Mo

            long seekTime = 0;

            System.out.println("randomValue=" + randomValue);
            System.out.println("loop=" + loop);
            for (int i = 0; i < loop; i++) {
                // time = System.currentTimeMillis();
                // ra.seek(i);
                // ra.read(buffer);
                // // System.out.println(Arrays.toString(buffer));
                // seekStart += System.currentTimeMillis() - time;
                //
                // time = System.currentTimeMillis();
                // ra.seek(length - (i + mediumPosition - 1024));
                // ra.read(buffer);
                // // System.out.println(Arrays.toString(buffer));
                // seekMedium += System.currentTimeMillis() - time;
                //
                // time = System.currentTimeMillis();
                // ra.seek(length - i - 1024);
                // ra.read(buffer);
                // // System.out.println(Arrays.toString(buffer));
                // seekEnd += System.currentTimeMillis() - time;

                time = System.currentTimeMillis();
                int nextInt = random.nextInt(randomValue);
                // System.out.println(nextInt);
                ra.seek(nextInt);
                ra.read(buffer);
                // System.out.println(Arrays.toString(buffer));
                seekTime += System.currentTimeMillis() - time;
                
                if(false) {
                    break;
                }

            }
            x1.add((double) randomValue);
            y1.add((double) seekTime);
            System.out.println(seekTime + " ms");
            System.out.println((double) (((double) loop / (double) seekTime)) + " items/ms");
            System.out.println((double) (((double) loop) / (double) seekTime * 1000d) + " items/s");
        }

        double[] x11 = new double[x1.size()];
        double[] y11 = new double[y1.size()];
        for (int i = 0; i < x1.size(); i++) {
            x11[i] = x1.get(i);
            y11[i] = y1.get(i);
        }

        double[][] data1 = new double[][] { x11, y11 };
        dataset.addSeries("Test", data1);

        new PersistentDiskBenchsChart("Disk Bench").createDemoPanel(dataset);

        // System.out.println(seekStart);
        // System.out.println(seekMedium);
        // System.out.println(seekEnd);

        ra.close();
    }

    /**
     * DOC amaumont Comment method "run".
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void run2() throws IOException {
        String containerFilePath = "/home/amaumont/hash_benchs/talend_hash_perfs";
//        String containerFilePath = "/home/amaumont/hash_benchs/talend_hash";
        
        MultiPointersMultiHashFiles mpfh = new MultiPointersMultiHashFiles(containerFilePath, 1);
        
        mpfh.setFilePointersNumber(100);
        
        mpfh.initGet(containerFilePath);
        
        int loop = 100000;
        
        long seekStart = 0;
        long seekMedium = 0;
        long seekEnd = 0;
        
        long time = 0;
        
        byte[] buffer = new byte[130];
        
        Random random = new Random(System.currentTimeMillis());
        
        DefaultXYDataset dataset = new DefaultXYDataset();
        
        List<Double> x1 = new ArrayList<Double>();
        List<Double> y1 = new ArrayList<Double>();
        
        

        System.out.println("run2");

        long timeOut = 250000;
        long lastTime = System.currentTimeMillis();
//        int randomValue = (int) length / 40;
        

        int length = 2000000000;
        for (int randomValue = 80000000; randomValue < length; randomValue += 10000000) { // 2 Go
//        for (int randomValue = 80000000; randomValue < length; randomValue += 10000000) { // 1,2 Go
//        for (int randomValue = 1000000; randomValue < length; randomValue += 1000000) { // 120 Mo
//        int length = 120000000;
//        for (int randomValue = 10000000; randomValue < length; randomValue += 5000000) { // 120 Mo
            
            long seekTime = 0;
            
            System.out.println("randomValue=" + randomValue);
            System.out.println("loop=" + loop);
            for (int i = 0; i < loop; i++) {
                // time = System.currentTimeMillis();
                // ra.seek(i);
                // ra.read(buffer);
                // // System.out.println(Arrays.toString(buffer));
                // seekStart += System.currentTimeMillis() - time;
                //
                // time = System.currentTimeMillis();
                // ra.seek(length - (i + mediumPosition - 1024));
                // ra.read(buffer);
                // // System.out.println(Arrays.toString(buffer));
                // seekMedium += System.currentTimeMillis() - time;
                //
                // time = System.currentTimeMillis();
                // ra.seek(length - i - 1024);
                // ra.read(buffer);
                // // System.out.println(Arrays.toString(buffer));
                // seekEnd += System.currentTimeMillis() - time;

//                MultiReadPointersFileHandler fh = mpfh.getFileHandler(random.nextInt(10));
                MultiReadPointersFileHandler fh = mpfh.getFileHandler(0);

                time = System.currentTimeMillis();
                int cursorPosition = random.nextInt(randomValue);

                RandomAccessFile ra = fh.getPointer(cursorPosition);
                
                // System.out.println(nextInt);
                ra.seek(cursorPosition);
                ra.read(buffer);
                // System.out.println(Arrays.toString(buffer));
                seekTime += System.currentTimeMillis() - time;
               

                
            }
            x1.add((double) randomValue);
            y1.add((double) seekTime);
            System.out.println(seekTime + " ms");
            System.out.println((double) (((double) loop / (double) seekTime)) + " items/ms");
            System.out.println((double) (((double) loop) / (double) seekTime * 1000d) + " items/s");
            
            if(System.currentTimeMillis() - lastTime > timeOut) {
                break;
            }
            lastTime = System.currentTimeMillis();
            
        }
        
        double[] x11 = new double[x1.size()];
        double[] y11 = new double[y1.size()];
        for (int i = 0; i < x1.size(); i++) {
            x11[i] = x1.get(i);
            y11[i] = y1.get(i);
        }
        
        double[][] data1 = new double[][] { x11, y11 };
        dataset.addSeries("Test", data1);
        
        new PersistentDiskBenchsChart("Disk Bench").createDemoPanel(dataset);
        
        // System.out.println(seekStart);
        // System.out.println(seekMedium);
        // System.out.println(seekEnd);
        
        mpfh.endGet(containerFilePath);
    }
    
    private FileOutputStream fileData;

    /**
     * DOC amaumont Comment method "openDataFile".
     * 
     * @throws IOException
     */
    private void openDataFile() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH'h'mm'm'ss's'");

        String filePath = folderStatsPath
        // + sdf.format(new Date())
                + "_" + fileHashBenchsBaseName + ".csv";

        File file = new File(filePath);
        boolean exists = file.exists();

        try {
            this.fileData = new FileOutputStream(filePath, true);
            if (!exists) {
                this.fileData.write(DataBench.getFileHeader().getBytes());
                this.fileData.write('\n');
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * DOC amaumont Comment method "closeDataFile".
     */
    private void closeDataFile() {
        try {
            this.fileData.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DOC amaumont Comment method "writeInFile".
     * 
     * @param dataReadWrite
     * @throws IOException
     */
    private void writeData(DataBench dataReadWrite) throws IOException {
        this.fileData.write(dataReadWrite.toFileRow().getBytes());
        this.fileData.write('\n');
    }

}
