package com.appzone.dhai.singletone;

import android.content.Context;

import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.preferences.Preferences;

public class UserSingleTone {

    private static UserSingleTone instance = null;
    private UserModel userModel;
    private UserSingleTone() {
    }

    public static UserSingleTone getInstance()
    {
        if (instance ==null)
        {
            instance = new UserSingleTone();
        }
        return instance;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
    public void clear(Context context)
    {
        this.userModel=null;
        Preferences preferences = Preferences.getInstance();
        preferences.Clear(context);
    }

}
