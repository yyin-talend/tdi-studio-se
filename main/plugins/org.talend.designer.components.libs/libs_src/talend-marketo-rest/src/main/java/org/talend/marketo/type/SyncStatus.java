package org.talend.marketo.type;

import java.util.List;
import java.util.Map;

public class SyncStatus {

    private Integer id;
    private String status;
    private List<Map<String, String>> reasons;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Map<String, String>> getReasons() {
        return reasons;
    }

    public void setReasons(List<Map<String, String>> reasons) {
        this.reasons = reasons;
    }

    @Override
    public String toString() {
        return "SyncStatus [id=" + id + ", status=" + status + ", reasons="
                + reasons + "]";
    }

}
