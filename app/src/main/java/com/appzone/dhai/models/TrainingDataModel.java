package com.appzone.dhai.models;

import java.io.Serializable;
import java.util.List;

public class TrainingDataModel implements Serializable {

    List<TrainingModel> data;
    private Meta meta;
    public Meta getMeta() {
        return meta;
    }

    public List<TrainingModel> getData() {
        return data;
    }

    public class TrainingModel implements Serializable
    {
        private int id;
        private String title_ar;
        private String title_en;
        private String destination_name_ar;
        private String destination_name_en;

        private String description_ar;
        private String description_en;
        private double sale_price;
        private long start;
        private long end;
        private String image;
        private int user_registered;
        private long created_at;

        public int getId() {
            return id;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getDestination_name_ar() {
            return destination_name_ar;
        }

        public String getDestination_name_en() {
            return destination_name_en;
        }

        public String getDescription_ar() {
            return description_ar;
        }

        public String getDescription_en() {
            return description_en;
        }

        public double getSale_price() {
            return sale_price;
        }

        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }

        public String getImage() {
            return image;
        }

        public int getUser_registered() {
            return user_registered;
        }

        public long getCreated_at() {
            return created_at;
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
