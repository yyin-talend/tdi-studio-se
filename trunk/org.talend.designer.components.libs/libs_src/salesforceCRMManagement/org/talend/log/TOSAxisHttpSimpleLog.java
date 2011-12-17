package org.talend.log;

import org.apache.commons.logging.impl.SimpleLog;

public class TOSAxisHttpSimpleLog extends SimpleLog {

    private StringBuffer buffer = new StringBuffer();

    public String getLogContent() {
        return buffer.toString();
    }

    public void clearBuffer() {
        buffer.delete(0, buffer.length());
    }

    public TOSAxisHttpSimpleLog(String name) {
        super(name);
    }

    protected void write(StringBuffer buffer) {
        this.buffer.append(buffer);
    }

    private static final long serialVersionUID = 1L;
}
