package com.appzone.dhai.models;

import java.io.Serializable;

public class AboutModel implements Serializable {

    private Site_About_Us site_about_us;

    public Site_About_Us getSite_about_us() {
        return site_about_us;
    }

    public class Site_About_Us implements Serializable
    {
        private String ar;
        private String en;

        public String getAr() {
            return ar;
        }

        public String getEn() {
            return en;
        }
    }
}
