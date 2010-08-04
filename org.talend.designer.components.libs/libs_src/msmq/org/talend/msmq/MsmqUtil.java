package org.talend.msmq;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;

public class MsmqUtil {

    private Queue Msmq = null;

    private String host;

    private String queueName;

    private String msgContent;

    boolean bTried = false;

    String ipAddr = "";

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
        msgu.setHost("localhost");
        msgu.setQueue("f3333");
        msgu.createIfNotExists(true);
        msgu.open();
        msgu.setMsg("aaaa");
        msgu.send();
        if (msgu.isOpen())
            System.out.println(msgu.receive());
    }

    public void peek() {
        try {
            checkOpen();
            System.out.println("peek");
            Message msg = Msmq.peek(2000); // timeout= 2000 ms
            System.out.println(" ==> message: " + msg.getMessage());
            System.out.println("     label:   " + msg.getLabel());

        } catch (MessageQueueException ex1) {
            System.out.println("Peek failure: " + ex1);
        }
    }

    public void close() {
        try {
            checkOpen();
            Msmq.close();
            Msmq = null;
        } catch (MessageQueueException ex1) {
            System.out.println("close failure: " + ex1);
        }
    }

    private void delete() {
        try {
            String fullname = getQueueFullName(".", queueName);
            ionic.Msmq.Queue.delete(fullname);
        } catch (MessageQueueException ex1) {
            System.out.println("Queue deletion failure: " + ex1);
        }
    }

    public void open() {
        try {
            if (Msmq != null) {
                Msmq.close();
                Msmq = null;
            }
            String fullname = getQueueFullName(host, queueName);
            Msmq = new Queue(fullname);
            // Msmq= new Queue(fullname, 0x02); // 0x02 == SEND only
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
            Msmq.send(msg);
        } catch (MessageQueueException ex1) {
            System.out.println("Send failure: " + ex1);
        }
    }

    public String receive() {
        try {
            checkOpen();
            // System.out.println("receive");
            Message msg = Msmq.receive(2000); // timeout= 2000 ms
            // System.out.println(" ==> message: " + msg.getMessage());
            // System.out.println("     label:   " + msg.getLabel());
            return msg.getMessage();
        } catch (MessageQueueException ex1) {
            System.out.println("Receive failure: " + ex1);
        }
        return null;
    }

    private String getQueueFullName(String hostname, String queueShortName) {
        String h1 = hostname;
        String a1 = "OS";
        if ((h1 == null) || h1.equals(""))
            h1 = ".";
        char[] c = h1.toCharArray();
        if ((c[0] >= '1') && (c[0] <= '9'))
            a1 = "TCP";

        return "DIRECT=" + a1 + ":" + h1 + "\\private$\\" + queueShortName;
    }

    private void create() {
        try {
            if (!(host == null || "".equals(host) || "localhost".equalsIgnoreCase(host) || host.equals(ipAddr) || "127.0.0.1"
                    .equals(host))) {
                throw new MessageQueueException("can only create queue locally", -1); // can only create locally.
            }
            String fullname = ".\\private$\\" + queueName;
            String qLabel = "Created by " + this.getClass().getName() + ".java";
            boolean transactional = false; // should the queue be transactional
            Msmq = Queue.create(fullname, qLabel, transactional);
        } catch (MessageQueueException ex1) {
            System.out.println("Queue creation failure: " + ex1);
        }
    }

    private void checkOpen() throws MessageQueueException {
        if (Msmq == null)
            throw new MessageQueueException("Open a queue first!\n", -1);
    }

    public boolean isOpen() {
        return Msmq != null;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setQueue(String queueName) {
        this.queueName = queueName;
    }

    public void setMsg(String msg) {
        this.msgContent = msg;
    }

    public void createIfNotExists(boolean bool) {
        bTried = true;
    }
}
