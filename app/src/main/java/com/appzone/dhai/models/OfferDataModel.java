package com.appzone.dhai.models;

import java.io.Serializable;
import java.util.List;

public class OfferDataModel implements Serializable {

    List<OfferModel> data;
    private Meta meta;
    public Meta getMeta() {
        return meta;
    }

    public List<OfferModel> getData() {
        return data;
    }

    public class OfferModel implements Serializable
    {
        private String title_ar;
        private String title_en;
        private String image;
        private String description_ar;
        private String description_en;


        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getImage() {
            return image;
        }

        public String getDescription_ar() {
            return description_ar;
        }

        public String getDescription_en() {
            return description_en;
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
