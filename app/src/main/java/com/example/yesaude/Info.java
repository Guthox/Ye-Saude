package com.example.yesaude;

import android.app.Application;

public class Info extends Application {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
