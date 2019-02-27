package com.appzone.dhai.models;

import java.io.Serializable;
import java.util.List;

public class ServiceDataModel implements Serializable {

    private List<ServiceModel> data;
    private OfferDataModel.Meta meta;

    public List<ServiceModel> getData() {
        return data;
    }

    public OfferDataModel.Meta getMeta() {
        return meta;
    }
    public class ServiceModel implements Serializable
    {
        private int id;
        private String title_ar;
        private String title_en;
        private String description_ar;
        private String description_en;
        private double sale_price;
        private String image;

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

        public double getSale_price() {
            return sale_price;
        }

        public String getImage() {
            return image;
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
