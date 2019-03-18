package com.appzone.dhai.models;

import java.io.Serializable;
import java.util.List;

public class JobsDataModel implements Serializable {

    private List<JobsModel> data;
    private Meta meta;
    public Meta getMeta() {
        return meta;
    }

    public List<JobsModel> getData() {
        return data;
    }

    public class JobsModel implements Serializable
    {
        private int id;
        private String title_ar;
        private String title_en;
        private String description_ar;
        private String description_en;
        private String address_ar;
        private String address_en;
        private String image;
        private long created_at;
        private double sale_price;
        private int user_registered;


        public int getId() {
            return id;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getDescription_ar() {
            return description_ar;
        }

        public String getDescription_en() {
            return description_en;
        }

        public String getImage() {
            return image;
        }

        public long getCreated_at() {
            return created_at;
        }

        public String getAddress_ar() {
            return address_ar;
        }

        public String getAddress_en() {
            return address_en;
        }

        public double getSale_price() {
            return sale_price;
        }

        public int getUser_registered() {
            return user_registered;
        }

        public void setUser_registered(int user_registered) {
            this.user_registered = user_registered;
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
