package com.appzone.dhai.models;

import java.io.Serializable;
import java.util.List;

public class BankAccountDataModel implements Serializable {
    private List<BankModel> data;

    public List<BankModel> getData() {
        return data;
    }

    public class BankModel implements Serializable
    {
        private int id;
        private String title_ar;
        private String title_en;
        private String account;

        public int getId() {
            return id;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getAccount() {
            return account;
        }
    }
}
