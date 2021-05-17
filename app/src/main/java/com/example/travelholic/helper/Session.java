package com.example.travelholic.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public Session(Context context) {
        preferences = context.getSharedPreferences("com.example.travelholic.SESSION", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setSession(String username, String password, String role) {
        editor.clear();
        editor.putString("com.example.travelholic.SESSION_USERNAME", username);
        editor.putString("com.example.travelholic.SESSION_PASSWORD", password);
        editor.putString("com.example.travelholic.SESSION_ROLE", role);
        editor.apply();
    }

    public void unsetSession() {
        editor.clear();
        editor.apply();
    }

    public String getUsername() {
        return preferences.getString("com.example.travelholic.SESSION_USERNAME", null);
    }

    public String getPassword() {
        return preferences.getString("com.example.travelholic.SESSION_PASSWORD", null);
    }

    public void setPassword(String password) {
        editor.remove("com.example.travelholic.SESSION_PASSWORD");
        editor.apply();
        editor.putString("com.example.travelholic.SESSION_PASSWORD", password);
        editor.apply();
    }

    public String getRole() {
        return preferences.getString("com.example.travelholic.SESSION_ROLE", null);
    }
}
