package com.appzone.dhai.models;

import java.io.Serializable;
import java.util.List;

public class NotificationDataModel implements Serializable {

    private List<NotificationModel> data;
    private Meta meta;

    public List<NotificationModel> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public class NotificationModel implements Serializable
    {
        private int service_id;
        private String service_title_ar;
        private String service_title_en;
        private long created_at;
        private int type;
        private int receiver_id;

        public NotificationModel(int service_id, String service_title_ar, String service_title_en, long created_at, int type, int receiver_id) {
            this.service_id = service_id;
            this.service_title_ar = service_title_ar;
            this.service_title_en = service_title_en;
            this.created_at = created_at;
            this.type = type;
            this.receiver_id = receiver_id;
        }

        public int getService_id() {
            return service_id;
        }

        public String getService_title_ar() {
            return service_title_ar;
        }

        public String getService_title_en() {
            return service_title_en;
        }

        public long getCreated_at() {
            return created_at;
        }

        public int getType() {
            return type;
        }

        public int getReceiver_id() {
            return receiver_id;
        }
    }
    public class Meta implements Serializable
    {
        private int current_page;
        private int last_page;

        public int getCurrent_page() {
            return current_page;
        }

        public int getLast_page() {
            return last_page;
        }
    }
}
