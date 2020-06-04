package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/**
 *  Class that is a  wrapper around a login status,
 *  a possible user email, and either a link to login
 *  or a link to logout. 
 */

public class LoginStatusResponse {
    private boolean isLoggedIn;
    private String email;
    private String link;

    public LoginStatusResponse(boolean isLoggedIn, String email, String link) {
        this.isLoggedIn = isLoggedIn;
        this.email = email;
        this.link = link;
    }

    @Override
    public String toString() {
        return "LoginResponse [is-logged-in=" + isLoggedIn + ", email=" + email + ", link=" + link + "]";
    }
}
