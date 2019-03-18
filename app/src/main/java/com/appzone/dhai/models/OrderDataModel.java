package com.appzone.dhai.models;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel implements Serializable {

    private List<OrderModel> data;
    private Meta meta;

    public List<OrderModel> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public class OrderModel implements Serializable
    {
        private int id;
        private int user_id;
        private int service_id;
        private long created_at;
        private String title_ar;
        private String title_en;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getService_id() {
            return service_id;
        }

        public long getCreated_at() {
            return created_at;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
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
