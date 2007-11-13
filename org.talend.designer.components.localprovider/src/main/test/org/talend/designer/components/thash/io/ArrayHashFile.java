// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.components.thash.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * DOC slanglois class global comment. Detailled comment <br/>
 * 
 * 
24422 milliseconds for 1000000 beans to STORE using ArrayHashFile. 40000 items/s. 133 bytes per item in storage.
21391 milliseconds for 1000000 beans to STORE using SimpleHashFile. 46000 items/s. 124 bytes per item in storage.
42500 milliseconds for 1000000 big beans to STORE using ArrayHashFile. 23000 items/s. 335 bytes per item in storage.
36594 milliseconds for 1000000 beans to STORE using SimpleHashFile. 27000 items/s. 321 bytes per item in storage. 
 * 
 */
class ArrayHashFile implements MapHashFile {

    private static ArrayHashFile instance;

    private ArrayHashFile() {
    }

    /**
     * getInstance.
     * 
     * @return the instance if this project handler
     */
    public static synchronized ArrayHashFile getInstance() {
        if (instance == null) {
            instance = new ArrayHashFile();
        }
        return instance;
    }

    RandomAccessFile bw = null;

    boolean readonly;

    final int START_POSITION = 0;

    long position;

    RandomAccessFile ra = null;

    Object lastRetrievedObject;

    long lastRetrievedCursorPosition = -1;

    public Object get(String container, long cursorPosition) throws IOException, ClassNotFoundException {
        if (cursorPosition != lastRetrievedCursorPosition) {
            ra.seek(cursorPosition);
            byte[] byteArray = new byte[ra.readInt()];
            ra.read(byteArray);
            lastRetrievedObject = new ObjectInputStream(new DataInputStream(new ByteArrayInputStream(byteArray))).readObject();
            lastRetrievedCursorPosition = cursorPosition;
        }
        return lastRetrievedObject;
    }

    public long put(String container, Object object) throws IOException {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bao);
        ObjectOutputStream oos = new ObjectOutputStream(dos);
        oos.writeObject(object);

        int sizeBytes = bao.size();

        if (!readonly) {
            bw.writeInt(sizeBytes);
            bw.write(bao.toByteArray());
        }

        bao.close();

        long returnPosition = position;

        position += (4 + sizeBytes);

        return returnPosition;
    }

    public void initPut(String container) throws IOException {
        if (!readonly) {
            File file = new File(container);
            file.delete();
            position = START_POSITION;
            bw = new RandomAccessFile(container, "rw");
        }
    }

    public void endPut() throws IOException {
        if (!readonly) {
            bw.close();
        }
    }

    public void initGet(String container) throws FileNotFoundException {
        ra = new RandomAccessFile(container, "r");
    }

    public void endGet(String container) throws IOException {
        if (!readonly) {
            if (ra != null) {
                ra.close();
            }
            File file = new File(container);
            file.delete();
        }
    }

    public static void main(String[] args) throws IOException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, ClassNotFoundException, InstantiationException {
        int loop = 1000000;

        // test bean write using ArrayHashFile
        ArrayHashFile nihf = ArrayHashFile.getInstance();
        List<Long> cursors = new ArrayList<Long>();
        long start = System.currentTimeMillis();

        nihf.initPut("D:/cache0");
        for (int i = 1; i < loop; i++) {
            Bean bean = new Bean(i, String.valueOf(i));
            cursors.add(nihf.put("", bean.toArray()));
        }
        nihf.endPut();

        long end = System.currentTimeMillis();
        long deltaTime = (end - start);
        System.out.print(deltaTime + " milliseconds for " + loop + " beans to STORE using ArrayHashFile. "
                + (loop / deltaTime * 1000) + " items/s. ");
        File file = new File("D:/cache0");
        System.out.println((file.length() / loop) + " bytes per item in storage.");

        // nihf.initGet("D:/cache0");
        //        
        // for(int i = 0; i < cursors.size(); i++){
        // Bean be = new Bean((Object[])nihf.get("", cursors.get(i)));
        // }
        // nihf.endGet("D:/cache0");

        // test bean write using SimpleHashFile
        SimpleHashFile shf = SimpleHashFile.getInstance();
        cursors = new ArrayList<Long>();
        start = System.currentTimeMillis();

        shf.initPut("D:/cache1");
        for (int i = 1; i < loop; i++) {
            Bean bean = new Bean(i, String.valueOf(i));
            cursors.add(shf.put("", bean));
        }
        shf.endPut();

        end = System.currentTimeMillis();
        deltaTime = (end - start);
        System.out.print(deltaTime + " milliseconds for " + loop + " beans to STORE using SimpleHashFile. "
                + (loop / deltaTime * 1000) + " items/s. ");
        file = new File("D:/cache1");
        System.out.println((file.length() / loop) + " bytes per item in storage.");

        // test big bean write using ArrayHashFile
        nihf = ArrayHashFile.getInstance();
        cursors = new ArrayList<Long>();
        start = System.currentTimeMillis();

        nihf.initPut("D:/cache2");
        for (int i = 1; i < loop; i++) {
            BigBean bean = new BigBean(i, String.valueOf(i));
            cursors.add(nihf.put("", bean.toArray()));
        }
        nihf.endPut();

        end = System.currentTimeMillis();
        deltaTime = (end - start);
        System.out.print(deltaTime + " milliseconds for " + loop + " big beans to STORE using ArrayHashFile. "
                + (loop / deltaTime * 1000) + " items/s. ");
        file = new File("D:/cache2");
        System.out.println((file.length() / loop) + " bytes per item in storage.");

        // nihf.initGet("D:/cache2");
        //        
        // for(int i = 0; i < cursors.size(); i++){
        // BigBean be = new BigBean((Object[])nihf.get("", cursors.get(i)));
        // }
        // nihf.endGet("D:/cache2");

        // test big bean write using SimpleHashFile
        shf = SimpleHashFile.getInstance();
        cursors = new ArrayList<Long>();
        start = System.currentTimeMillis();

        shf.initPut("D:/cache3");
        for (int i = 1; i < loop; i++) {
            BigBean bean = new BigBean(i, String.valueOf(i));
            cursors.add(shf.put("", bean));
        }
        shf.endPut();

        end = System.currentTimeMillis();
        deltaTime = (end - start);
        System.out.print(deltaTime + " milliseconds for " + loop + " beans to STORE using SimpleHashFile. "
                + (loop / deltaTime * 1000) + " items/s. ");
        file = new File("D:/cache3");
        System.out.println((file.length() / loop) + " bytes per item in storage.");
    }

}


class Bean implements Serializable {

    int primitiveInt;

    String name;

    transient int hashcode = -1;
    
    /**
     * DOC amaumont Bean constructor comment.
     * 
     * @param primitiveInt
     * @param name
     */
    public Bean(int primitiveInt, String name) {
        super();
        this.primitiveInt = primitiveInt;
        this.name = name;
    }
    
    public Bean(){
        super();
    }
    
    public Bean(Object[] objs) {
        super();
        this.primitiveInt = (Integer)objs[0];
        this.name = (String)objs[1];
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if(hashcode == -1) {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
            result = prime * result + this.primitiveInt;
            hashcode = result;
        }
        return hashcode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        final KeyForMap other = (KeyForMap) obj;

        if(this.hashCode() != other.hashcode) {
            return false;
        }
        
        Object o = null;
        try {
            o = ReliabilityHashMapFileTest.hashFile.get("", (long)other.cursorPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (o == null) {
            return false;
        }
        Bean bean = (Bean) o;

        if (this.name == null) {
            if ((String) bean.name != null)
                return false;
        } else if (!this.name.equals((String) bean.name))
            return false;
        if (this.primitiveInt != bean.primitiveInt)
            return false;
        return true;
    }

    
    public int getPrimitiveInt() {
        return primitiveInt;
    }

    
    public void setPrimitiveInt(int primitiveInt) {
        this.primitiveInt = primitiveInt;
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }
    
    public Object[] toArray(){
        Object[] result = new Object[2];
        result[0] = primitiveInt;
        result[1] = name;
        return result;
    }

}


class BigBean implements Serializable {

    int primitiveInt;

    String name;
    
    String address;
    
    float price;

    Date date = null;
    
    Date date2 = null;
    
    byte[] b= null;
    
    boolean flag;
    
    transient int hashcode = -1;

    /**
     * DOC amaumont Bean constructor comment.
     * 
     * @param primitiveInt
     * @param name
     */
    public BigBean(int primitiveInt, String name) {
        super();
        this.primitiveInt = primitiveInt;
        this.name = name;
        this.address = "Address" + name;
        this.b = address.getBytes();
        this.date = new Date();
        this.date2 = new Date();
        this.price = (float)primitiveInt;
        this.flag = true;
    }
    
    public BigBean(){
        super();
    }
    
    public BigBean(Object[] objs) {
        super();
        this.primitiveInt = (Integer)objs[0];
        this.name = (String)objs[1];
        this.address = (String)objs[2];
        this.price = (Float)objs[3];
        this.date = (Date)objs[4];
        this.date2 = (Date)objs[5];
        this.b = (byte[])objs[6];
        this.flag = (Boolean)objs[7];
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (hashcode == -1) {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
            result = prime * result + this.primitiveInt;
            hashcode = result;
        }
        return hashcode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        final KeyForMap other = (KeyForMap) obj;

        if (this.hashCode() != other.hashcode) {
            return false;
        }

        Object o = null;
        try {
            o = ReliabilityHashMapFileTest.hashFile.get("", (long) other.cursorPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (o == null) {
            return false;
        }
        BigBean bean = (BigBean) o;

        if (this.name == null) {
            if ((String) bean.name != null)
                return false;
        } else if (!this.name.equals((String) bean.name))
            return false;
        if (this.primitiveInt != bean.primitiveInt)
            return false;
        return true;
    }

    public int getPrimitiveInt() {
        return primitiveInt;
    }

    public void setPrimitiveInt(int primitiveInt) {
        this.primitiveInt = primitiveInt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public Date getDate() {
        return date;
    }

    
    public void setDate(Date date) {
        this.date = date;
    }

    
    public byte[] getB() {
        return b;
    }

    
    public void setB(byte[] b) {
        this.b = b;
    }

    
    public String getAddress() {
        return address;
    }

    
    public void setAddress(String address) {
        this.address = address;
    }

    
    public float getPrice() {
        return price;
    }

    
    public void setPrice(float price) {
        this.price = price;
    }

    
    public Date getDate2() {
        return date2;
    }

    
    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    
    public boolean isFlag() {
        return flag;
    }

    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    
    public Object[] toArray(){
        Object[] result = new Object[8];
        result[0] = primitiveInt;
        result[1] = name;
        result[2] = address;
        result[3] = price;
        result[4] = date;
        result[5] = date2;
        result[6] = b;
        result[7] = flag;
        return result;
    }

}


