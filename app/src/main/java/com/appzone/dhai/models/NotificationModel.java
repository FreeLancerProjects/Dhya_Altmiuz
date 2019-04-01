package com.appzone.dhai.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private int service_id;
    private String service_title_ar;
    private String service_title_en;
    private long created_at;
    private int type;
    private int receiver_id;
    private String needed;
    private  int msg_id;

    public NotificationModel(int service_id, String service_title_ar, String service_title_en, long created_at, int type, int receiver_id, String needed, int msg_id) {
        this.service_id = service_id;
        this.service_title_ar = service_title_ar;
        this.service_title_en = service_title_en;
        this.created_at = created_at;
        this.type = type;
        this.receiver_id = receiver_id;
        this.needed = needed;
        this.msg_id = msg_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getService_title_ar() {
        return service_title_ar;
    }

    public void setService_title_ar(String service_title_ar) {
        this.service_title_ar = service_title_ar;
    }

    public String getService_title_en() {
        return service_title_en;
    }

    public void setService_title_en(String service_title_en) {
        this.service_title_en = service_title_en;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getNeeded() {
        return needed;
    }

    public void setNeeded(String needed) {
        this.needed = needed;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }
}
