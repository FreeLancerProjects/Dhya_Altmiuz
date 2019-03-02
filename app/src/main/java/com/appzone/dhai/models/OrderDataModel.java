package com.appzone.dhai.models;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel implements Serializable {

    private List<OrderModel> date;
    private Meta meta;

    public List<OrderModel> getDate() {
        return date;
    }

    public Meta getMeta() {
        return meta;
    }

    public class OrderModel implements Serializable
    {

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
