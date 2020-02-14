package com.mfa.rumeet.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public static final String SP_RumeetApp = "spRumeetApp";

    public static final String SP_NAMA = "spNama";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_UNIQUE_ID = "spUniqueId";
    public static final String SP_ID_RUANGAN ="spIdRuangan";
    public static final String SP_TELEPON = "spTelepon";
    public static final String SP_DIVISI = "spDivisi";
    public static final String SP_FOTO_PROFILE = "spFotoProfile";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context) {
        sp = context.getSharedPreferences(SP_RumeetApp, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpUniqueId(){
        return sp.getString(SP_UNIQUE_ID, "");
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

    public String getSpIdRuangan(){
        return sp.getString(SP_ID_RUANGAN, "");
    }

    public String getSpTelepon() {
        return sp.getString(SP_TELEPON, "");
    }

    public String getSpDivisi() {
        return sp.getString(SP_DIVISI, "");
    }

    public String getSpFotoProfile() {
        return sp.getString(SP_FOTO_PROFILE, "");
    }
}
