package com.appzone.dhai.models;

import java.io.Serializable;
import java.util.List;

public class PackageDataModel implements Serializable {
    private List<PackageModel> data;

    public List<PackageModel> getDate() {
        return data;
    }

    public class PackageModel implements Serializable
    {
        private int id;
        private String title_ar;
        private String title_en;
        private double size;

        public int getId() {
            return id;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public double getSize() {
            return size;
        }
    }
}
