package com.puzzle.industries.inumbr.repositores;

import com.puzzle.industries.inumbr.dataModels.PreferenceModel;

public class CredentialsRepository {

    private final PreferenceModel PREF_MODEL = new PreferenceModel();
    private static final CredentialsRepository instance = new CredentialsRepository();

    public static CredentialsRepository getInstance(){
        return instance;
    }

    public void setCredentials(String phone, String password){
        PREF_MODEL.setCredentials(phone, password);
    }

    public String getPhone(){
        return PREF_MODEL.getMobileNumber();
    }

    public String getPassword(){
        return PREF_MODEL.getPassword();
    }

    public boolean isCredentialsSet(){
        return !PREF_MODEL.getMobileNumber().isEmpty() && !PREF_MODEL.getPassword().isEmpty();
    }
}
