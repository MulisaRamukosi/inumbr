package com.puzzle.industries.inumbr.dataModels;

import android.content.Context;
import android.content.SharedPreferences;

import com.puzzle.industries.inumbr.iNumber;
import com.puzzle.industries.inumbr.utils.Constants;

public class PreferenceModel {

    private final SharedPreferences mPref;

    public PreferenceModel() {
        mPref = iNumber.getAppContext().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }


    public void setCredentials(String userMobileNumber, String userPass){
        mPref.edit().putString(Constants.KEY_MOBILE_NUM, userMobileNumber)
                .putString(Constants.KEY_PASSWORD, userPass).apply();
    }

    public String getMobileNumber() {
        return mPref.getString(Constants.KEY_MOBILE_NUM, "");
    }

    public String getPassword() {
        return mPref.getString(Constants.KEY_PASSWORD, "");
    }
}
