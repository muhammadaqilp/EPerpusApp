package com.example.eperpusapp.Model;

import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("username")
    private String username;

    @SerializedName("pass")
    private String pass;

    public Login() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.pass = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return pass;
    }
}
