package org.talend.msmq;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;

public class MsmqUtil {

    private Queue msmqHandle = null;

    private String host;

    private String queueName;

    private String msgContent;

    boolean bTried = false;

    String ipAddr = "";

    final String LOCALIP = "127.0.0.1";

    public MsmqUtil() {
        try {
            java.net.InetAddress thisIp = java.net.InetAddress.getLocalHost();
            ipAddr = thisIp.getHostAddress();
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
    }

    public static void main(String[] args) throws java.lang.Exception {
        org.talend.msmq.MsmqUtil msgu = new org.talend.msmq.MsmqUtil();
        msgu.setHost("127.0.0.1");
        msgu.setQueue("ytao4");
        msgu.createIfNotExists(true);
        msgu.open();
        String str = "ÊÇµÄabc";
        // str = CharacterSetToolkit.toUnicode(str, true);
        // System.out.println(str);
        msgu.setMsg(str);
        msgu.send();
        if (msgu.isOpen()) {
            str = msgu.receive();
            // str = CharacterSetToolkit.fromUnicode(str);
            System.out.println(str);
        }
        msgu.close();
    }

    // message remain in queue
    public void peek() {
        try {
            checkOpen();
            System.out.println("peek");
            Message msg = msmqHandle.peek(2000); // timeout= 2000 ms
            System.out.println(" ==> message: " + msg.getMessage());
            System.out.println("     label:   " + msg.getLabel());
        } catch (MessageQueueException ex1) {
            System.out.println("Peek failure: " + ex1);
        }
    }

    // close an open queue
    public void close() {
        try {
            checkOpen();
            msmqHandle.close();
            msmqHandle = null;
        } catch (MessageQueueException ex1) {
            System.out.println("close failure: " + ex1);
        }
    }

    // delete the queue
    private void delete() {
        try {
            String fullname = getQueueFullName(".", queueName);
            ionic.Msmq.Queue.delete(fullname);
        } catch (MessageQueueException ex1) {
            System.out.println("Queue deletion failure: " + ex1);
        }
    }

    // open the queue, if it not exists, and creating is required, try to create a queue with the name.
    public void open() {
        try {
            if (msmqHandle != null) {
                msmqHandle.close();
                msmqHandle = null;
            }
            String fullname = getQueueFullName(host, queueName);
            msmqHandle = new Queue(fullname);
            // msmqHandle= new Queue(fullname, 0x02); // 0x02 == SEND only
        } catch (MessageQueueException ex1) {
            System.out.println("Queue open failure: " + ex1);

            if (bTried) {
                bTried = false;
                create();
            }
        }
    }

    public void send() {
        try {
            checkOpen();
            // the transaction flag must agree with the transactional flavor of the queue.
            int transactionFlag = 0; // 0 = NO TRANSACTION, 1= MTS, 2= XA, 3= SINGLE_MESSAGE
            String mLabel = "inserted by " + this.getClass().getName() + ".java";
            String correlationID = "L:none";
            Message msg = new Message(msgContent, mLabel, correlationID, transactionFlag);
            msmqHandle.send(msg);
        } catch (MessageQueueException ex1) {
            System.out.println("Send failure: " + ex1);
        }
    }

    public String receive() {
        try {
            checkOpen();
            // System.out.println("receive");
            Message msg = msmqHandle.receive(2000); // timeout= 2000 ms
            // System.out.println(" ==> message: " + msg.getMessage());
            // System.out.println("     label:   " + msg.getLabel());
            return CharacterSetToolkit.fromUnicode(msg.getMessage());
        } catch (MessageQueueException ex1) {
            System.out.println("Receive failure: " + ex1);
        }
        return null;
    }

    private String getQueueFullName(String hostname, String queueShortName) {
        String h1 = hostname;
        String a1 = "OS";
        if ((h1 == null) || h1.equals("") || LOCALIP.equals(h1))
            h1 = ".";
        char[] c = h1.toCharArray();
        if ((c[0] >= '1') && (c[0] <= '9'))
            a1 = "TCP";

        return "DIRECT=" + a1 + ":" + h1 + "\\private$\\" + queueShortName;
    }

    private void create() {
        try {
            if (!(host == null || "".equals(host) || "localhost".equalsIgnoreCase(host) || host.equals(ipAddr) || LOCALIP
                    .equals(host))) {
                throw new MessageQueueException("can only create queue locally", -1); // can only create locally.
            }
            String fullname = ".\\private$\\" + queueName;
            String qLabel = "Created by " + this.getClass().getName() + ".java";
            boolean transactional = false; // should the queue be transactional
            msmqHandle = Queue.create(fullname, qLabel, transactional);
        } catch (MessageQueueException ex1) {
            System.out.println("Queue creation failure: " + ex1);
        }
    }

    private void checkOpen() throws MessageQueueException {
        if (msmqHandle == null)
            throw new MessageQueueException("Open a queue first!\n", -1);
    }

    public boolean isOpen() {
        return msmqHandle != null;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setQueue(String queueName) {
        this.queueName = queueName;
    }

    public void setMsg(String msg) {
        this.msgContent = CharacterSetToolkit.toUnicode(msg, true);
    }

    public void createIfNotExists(boolean bool) {
        bTried = true;
    }
}
