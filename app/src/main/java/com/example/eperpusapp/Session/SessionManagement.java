package com.example.eperpusapp.Session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.eperpusapp.Model.DataItemUser;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREFS_NAME = "session";
    String SESSION_KEY = "session_user";
    String SESSION_NAME = "session_username";
    String SESSION_NIM = "session_nim";
    String SESSION_PHOTO = "session_photo";
    String SESSION_ANGKATAN = "session_angkatan";
    String SESSION_STATUS = "session_status";
    String SESSION_PRODI = "session_prodi";
    String SESSION_FAKULTAS = "session_fakultas";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(DataItemUser user){
        int id = user.getIduser();
        String name = user.getNamaLengkap();
        String nim = user.getUsername();
        int angkatan = user.getAngkatan();
        String status = user.getStatus();
        String photo = user.getFotoUser();
        String prodi = user.getJurusan();
        String fakultas = user.getFakultas();

        editor.putInt(SESSION_KEY, id).commit();
        editor.putString(SESSION_NAME, name).commit();
        editor.putString(SESSION_NIM, nim).commit();
        editor.putInt(SESSION_ANGKATAN, angkatan).commit();
        editor.putString(SESSION_STATUS, status).commit();
        editor.putString(SESSION_PRODI, prodi).commit();
        editor.putString(SESSION_FAKULTAS, fakultas).commit();
        editor.putString(SESSION_PHOTO, photo).commit();
    }

    public int getSession(){
        return sharedPreferences.getInt(SESSION_KEY, -1);
    }

    public String getSessionName(){
        return sharedPreferences.getString(SESSION_NAME, "NULL");
    }

    public String getSessionNim(){
        return sharedPreferences.getString(SESSION_NIM, "NULL");
    }

    public int getSessionAngkatan(){
        return sharedPreferences.getInt(SESSION_ANGKATAN, -1);
    }

    public String getSessionStatus(){
        return sharedPreferences.getString(SESSION_STATUS, "NULL");
    }

    public String getSessionProdi(){
        return sharedPreferences.getString(SESSION_PRODI, "NULL");
    }

    public String getSessionPhoto(){
        return sharedPreferences.getString(SESSION_PHOTO, "NULL");
    }

    public String getSessionFakultas(){
        return sharedPreferences.getString(SESSION_FAKULTAS, "NULL");
    }

    public void removeSession(){
        editor.putInt(SESSION_KEY, -1).commit();
    }
}
