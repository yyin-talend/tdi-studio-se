//
// Queue.java
// ------------------------------------------------------------------
//
// Copyright (c) 2006-2015 Dino Chiesa.
// All rights reserved.
//
// This code module is part of MsmqJava, a JNI library that provides
// access to MSMQ for Java on Windows.
//
// ------------------------------------------------------------------
//
// This code is licensed under the Microsoft Public License.
// See the file License.txt for the license details.
// More info on: http://dotnetzip.codeplex.com
//
// ------------------------------------------------------------------
//
// last saved (in emacs):
// Time-stamp: <2010-March-28 15:51:58>
//
// ------------------------------------------------------------------
//
// This module provides the Java Queue object, representing an MSMQ Queue.
//
// ------------------------------------------------------------------

package ionic.Msmq;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Queue class represents a message queue in MSMQ.
 *
 * <p>
 *
 * Applications can instantiate a Queue, then perform send and receive
 * operations on the queue, using instances of the Message type.
 *
 * </p>
 * <p>
 *
 * Queue also exposes several static methods for Queue management, to
 * support creation and deletion of message queues.
 * </p>
 *
 */
public class Queue
{

    /**
     * <p>
     * The Queue.Access enum provides options for access to the MSMQ
     * Queue: Either Receive, Send, or both. Specify this when opening a
     * queue.
     * </p>
     *
     * <p>
     *
     * Applications may wish to open queues for only the access they
     * need, in order to save resources and memory.
     * </p>
     *
     */
    public enum Access
    {
        /**
         * The queue will be accessible for Receive or READ (or GET)
         * operations.
         *
         **/
        RECEIVE(1),

        /**
         * The queue will be accessible for Send or WRITE (or PUT)
         * operations.
         *
         **/
            SEND(2),

        /**
         * The queue will be accessible for both Send ad Receive
         * operations.
         *
         **/
            SEND_AND_RECEIVE(3);

        int _accessFlag;

        Access(int value)
        {
            _accessFlag = value;
        }

        int getValue()   { return _accessFlag; }
    }

    private static final Logger LOG = Logger.getLogger(Queue.class.getName());
    private static boolean initialized;

    /**
     * <p>Call this constructor to open a queue by name for SEND and
     * RECEIVE operations.</p>
     *
     * <p>Here's an example of how to use it, to send a simple message:</p>
     *
     * <blockquote class='code'><pre>
     *   Queue queue= new Queue(fullname);
     *   String body = "Hello, World!";
     *   String label = "Greeting";
     *   String correlationId= "L:none";
     *   Message msg= new Message(body, label, correlationId);
     *   queue.send(msg);
     * </pre></blockquote>
     *
     **/
    public Queue(String queueName)
        throws  MessageQueueException
    {
        _init(queueName, 0x03);  // open with both SEND and RECEIVE access
    }


    /**
     * Call this constructor to open a queue with the specified access
     *
     **/
    public Queue(String queueName, Queue.Access access)
        throws  MessageQueueException
    {
        _init(queueName, access.getValue());
    }


    private void _init(String queueName, int access)
        throws  MessageQueueException
    {
        initialize();
        // the openQueue native method causes the _queueSlot to be set.
        int rc = 0;
        if (access == 0x01) // RECEIVE
        {
            rc= nativeOpenQueueForReceive(queueName);
        }
        else if (access == 0x02) // SEND
        {
            rc= nativeOpenQueueForSend(queueName);
        }
        else if (access == 0x03) // SEND+RECEIVE
        {
            rc= nativeOpenQueue(queueName);
        }
        else { rc= 0xC00E0006; /* MQ_INVALID_PARAMETER */ }

        if (rc!=0) {
            throw new MessageQueueException("Cannot open queue.", rc);
        }

        _name= queueName;
        _formatName= "unknown";
        _label= "need to set this";
        _isTransactional= false; // TODO: get actual value in "openQueue"
    }



    /**
     * <p>
     * Create a queue by name, with the given queue label and transactional access.
     * </p>
     *
     * <p>Example:</p>
     *
     * <blockquote class='code'><pre>
     *   String fullname= ".\\private$\\" + qname;
     *   String qLabel="Created by " + this.getClass().getName() + ".java";
     *   boolean transactional= false;  // should the queue be transactional
     *   queue= Queue.create(fullname, qLabel, transactional);
     * </pre></blockquote>
     **/
    public static Queue create(String queuePath, String queueLabel, boolean isTransactional)
        throws  MessageQueueException
    {
        initialize();
        int rc= nativeCreateQueue( queuePath,  queueLabel,  (isTransactional)?1:0);
        if (rc!=0) {
            throw new  MessageQueueException("Cannot create queue.", rc);
        }
        // DIRECT=OS  ?  or DIRECT=TCP ?
        String a1= "OS";
        char[] c= queuePath.toCharArray();
        if ((c[0]>='1') && (c[0]<='9')) {
            a1= "TCP"; // assume ip address
        }

        Queue q= new Queue("DIRECT=" + a1 + ":" + queuePath);
        q._name= queuePath;
        //    q._formatName=queueFormatName;
        q._label=queueLabel;
        q._isTransactional= isTransactional;
        return q;
    }


    /**
     * Delete a queue by the given name.
     *
     **/
    public static void delete(String queuePath)
        throws  MessageQueueException
    {
        initialize();
        int rc= nativeDeleteQueue( queuePath );
        if (rc!=0) {
            throw new  MessageQueueException("Cannot delete queue.", rc);
        }
    }


    // -------------------------------------------------------
    // Sending methods
    // -------------------------------------------------------

    /**
     * Send a Message, with the given transaction type, and with the
     * given setting for high priority.
     **/
    public void send(Message msg, boolean highPriority, TransactionType t)
        throws  MessageQueueException
    {
        initialize();
        int rc= nativeSendBytes(msg.getBody(),
                                msg.getLabel(),
                                msg.getCorrelationId(),
                                t.getValue(),
                                highPriority
                                );
        if (rc!=0) {
            throw new MessageQueueException("Cannot send.", rc);
        }
    }


    /**
     * Send a Message, with the given transaction type.
     *
     **/
    public void send(Message msg, TransactionType t)
        throws  MessageQueueException
    {
        send(msg, false, t);
    }

    /**
     * Send a Message, with the given value for high priority.
     *
     **/
    public void send(Message msg, boolean highPriority)
        throws  MessageQueueException
    {
        send(msg, highPriority, TransactionType.None);
    }


    /**
     * Send a Message on the queue.
     *
     **/
    public void send(Message msg)
        throws  MessageQueueException
    {
        send(msg, false, TransactionType.None);
    }


    /**
     * Send a string as a Message, using UTF-8 encoding.
     * The label used will be blank, and the correlationId
     * will be null (none).
     *
     **/
    public void send(String s)
        throws  MessageQueueException, java.io.UnsupportedEncodingException

    {
        initialize();
        int rc= nativeSendBytes(s.getBytes("UTF-8"), // bytes of string
                                "",                  // empty label
                                null,                // empty correlationId
                                0,                   // outside any transaction
                                false                // false = not high priority
                                );
        if (rc!=0) {
            throw new MessageQueueException("Cannot send.", rc);
        }
    }


    /**
     * Send a byte array as a Message.
     * The label used will be blank, and the correlationId
     * will be null (none).
     *
     **/
    public void send(byte[] b)
        throws  MessageQueueException
    {
        initialize();
        int rc= nativeSendBytes(b,
                                "",                 // empty label
                                null,                 // empty correlationId
                                0,                  // outside any transaction
                                false               // false = not high priority
                                );
        if (rc!=0) {
            throw new MessageQueueException("Cannot send.", rc);
        }
    }


    // -------------------------------------------------------
    // Receiving methods
    // -------------------------------------------------------
    private ionic.Msmq.Message _internal_receive(int timeout, int ReadOrPeek)
        throws  MessageQueueException
    {
        initialize();
        Message msg = new Message();

        int rc = nativeReceiveBytes(msg, timeout, ReadOrPeek);
        //int rc = nativeReceiveBytes(timeout, ReadOrPeek);

        if (rc!=0) {
            throw new MessageQueueException("Cannot receive.", rc);
        }

        return msg;
    }

    /**
     * Poll the queue to receive one message, with the given timeout.
     *
     * <p>
     *
     * If the timeout expires before a message becomes available,
     * the method will throw an exception.
     **/
    public ionic.Msmq.Message receive(int timeout)
        throws  MessageQueueException
    {
        return _internal_receive(timeout, 1);
    }


    /**
     * Poll the queue to receive one message, with an infinite timeout.
     *
     **/
    public ionic.Msmq.Message receive()
        throws  MessageQueueException
    {
        return _internal_receive(0,1); // infinite timeout
    }

    /**
     * Peek at the queue and return a message without dequeueing it.
     *
     **/
    public ionic.Msmq.Message peek()
        throws  MessageQueueException
    {
        return _internal_receive(0,0); // infinite timeout
    }

    /**
     * Peek at the queue and return a message without dequeueing it,
     *
     * <p>
     *
     * If the timeout expires before a message becomes available,
     * the method will throw an exception.
     **/
    public ionic.Msmq.Message peek(int timeout)
        throws  MessageQueueException
    {
        return _internal_receive(timeout,0);
    }




    /**
     * Close the queue.
     *
     **/
    public void close()
        throws  MessageQueueException
    {
        initialize();
        int rc=nativeClose();
        if (rc!=0) {
            throw new MessageQueueException("Cannot close.", rc);
        }
    }



    // --------------------------------------------
    // getters on the Queue properties


    /**
     * Gets the name of the queue.
     *
     * @return the name of the queue.
     */
    public String getName(){ return _name; }


    /**
     * Gets the label on the queue.
     *
     * @return the label on the queue.
     */
    public String getLabel(){ return _label; }


    /**
     * Gets the formatname on the queue.
     *
     * @return the formatname of the queue.
     */
    public String getFormatName(){ return _formatName; }


    /**
     * Gets the transactional setting for the queue.
     *
     * @return the transactional setting for the queue.
     */
    public boolean isTransactional(){ return _isTransactional; }



    // --------------------------------------------
    // native methods
    private static native int nativeInit();
    private static native int nativeCreateQueue(String queuePath, String queueLabel, int isTransactional);
    private static native int nativeDeleteQueue(String queuePath);
    private native int nativeOpenQueue(String queueString);
    private native int nativeOpenQueueForSend(String queueString);
    private native int nativeOpenQueueForReceive(String queueString);
    private native int nativeSend(String messageString, int length, String label, String correlationId, int transactionFlag);
    //private native int nativeReceiveBytes(int timeout, int ReadOrPeek);
    private native int nativeReceiveBytes(Message msg, int timeout, int ReadOrPeek);
    private native int nativeSendBytes(byte [] messageBytes, String label, byte[] correlationId, int tflag, boolean priority );
    private native int nativeClose();


    // --------------------------------------------
    // private members
    private int   _queueSlot = 0;
    private String _name;
    private String _formatName;
    private String _label;
    private boolean _isTransactional;

    // --------------------------------------------
    // static initializer
    private static void initialize() {
        if (initialized) {
            return;
        }
        synchronized (Queue.class) {
            if (initialized) {
                return;
            }
            try {
    	        loadLib();
                nativeInit();
                initialized = true;
            } catch (Throwable e) {
                LOG.log(Level.SEVERE, "Error during library initialization. ", e);
                if (e instanceof Error) {
                    throw (Error) e;
                }
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new IllegalStateException("Initialization failure. ", e);
            }
        }
    }
    
    /* Try extracting and loading library from jar */
    
    private static void loadLib() throws Exception {
        String libName = "MsmqJava";
        String dllName = libName + ".dll";
        String TMP_HOME = System.getProperty("java.io.tmpdir");
        String SEPARATOR = System.getProperty("file.separator");
        String fileName = TMP_HOME + SEPARATOR + dllName;
        java.io.File file = new java.io.File(fileName);
        java.io.FileOutputStream os = null;
        java.io.InputStream is = null;
        boolean extracted = false;
        boolean useLocalLib = true;
        boolean loadSuccess = true;

        try {
            if (!file.exists()) {
                is = Queue.class.getResourceAsStream("/" + dllName);
                if (is != null) {
                    LOG.info("Unpacking local native library.");
                    extracted = true;
                    int read;
                    byte[] buffer = new byte[4096];
                    os = new java.io.FileOutputStream(fileName);
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                } else {
                    LOG.info("No local native library found.");
                    useLocalLib = false;
                }
            }
            loadSuccess = false;
            if (useLocalLib) {
                LOG.info("Using previously unpacked local native library.");
                System.load(fileName);
            } else {
                LOG.info("Using native library from system.");
                System.loadLibrary(libName);
            }
            loadSuccess = true;
        } catch (Throwable e) {
            if (loadSuccess) {
                throw e; // throw exception if unrelated to library loading
            }
            if (useLocalLib) {
                LOG.warning("Unable to load local native library \"" + dllName
                        + "\" with system path \"" + System.getenv("PATH")
                        + "\", trying to load from system. ");
                try {
                    System.loadLibrary(libName);
                    loadSuccess = true; // successfully recovered from error condition
                    LOG.info("Native library \"" + dllName + "\" loaded from system.");
                } catch (Throwable t) {
                    LOG.log(Level.WARNING, "Loading native library failed. ", t);
                }
            }
            if (!loadSuccess) {
                LOG.severe("Unable to load native library \"" + dllName
                        + "\" with library path \"" + System.getProperty("java.library.path")
                        + "\" and system path \"" + System.getenv("PATH") + "\"");
                throw e;
            }
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (Throwable e) {
                LOG.log(Level.WARNING, "Closing library file output stream failed. ", e);
            }
            try {
                if (is != null)
                    is.close();
            } catch (Throwable e) {
                LOG.log(Level.WARNING, "Closing library resource stream failed. ", e);
            }
            try {
                if (extracted && file.exists()) {
                    file.delete();
                }
            } catch (Throwable e) {
                LOG.log(Level.WARNING, "Removing library file failed. ", e);
            }
        }
    }
}
