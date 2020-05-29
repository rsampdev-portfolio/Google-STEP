package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/**
 *  Class that is a  wrapper around a login status,
 *  a possible user email, and either a link to login
 *  or a link to logout. 
 */

public class LoginStatusResponse {
    private boolean loginStatus;
    private String email;
    private String link;

    public LoginStatusResponse(boolean loginStatus, String email, String link) {
        this.loginStatus = loginStatus;
        this.email = email;
        this.link = link;
    }

    @Override
    public String toString() {
        return "LoginStatusResponse [login-status=" + loginStatus + ", email=" + email + ", link=" + link + "]";
    }
}
