package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/**
 *  Wrapper around a login status, a user email, and
 *  either a link to login or a link to logout. 
 */

public class LoginStatusResponse {
    private boolean isLoggedIn;
    private String userEmail;
    private String loginLink;
    private String logoutLink;

    public LoginStatusResponse(boolean isLoggedIn, String userEmail, String loginLink, String logoutLink) {
        this.isLoggedIn = isLoggedIn;
        this.userEmail = userEmail;
        this.loginLink = loginLink;
        this.logoutLink = logoutLink;
    }

    @Override
    public String toString() {
        return "LoginResponse [is-logged-in=" + isLoggedIn + ", userEmail=" + userEmail + ", loginLink=" + loginLink + ", logoutLink=" + logoutLink +"]";
    }
}
