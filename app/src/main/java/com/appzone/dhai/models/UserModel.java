package com.appzone.dhai.models;

import java.io.Serializable;

public class UserModel implements Serializable {

    private int id;
    private String token;
    private String name;
    private String email;
    private String avatar;
    private String image_cv;
    private String phone;
    private String fire_base_token;
    private double balance;


    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPhone() {
        return phone;
    }

    public String getFire_base_token() {
        return fire_base_token;
    }

    public double getBalance() {
        return balance;
    }

    public String getImage_cv() {
        return image_cv;
    }
}
