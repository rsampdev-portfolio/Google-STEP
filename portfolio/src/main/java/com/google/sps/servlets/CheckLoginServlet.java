package com.google.sps.servlets;

import java.io.IOException;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import javax.servlet.http.HttpServlet;
import com.google.gson.reflect.TypeToken;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.LoginStatusResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 *  Servlet that checks if a user is logged in or not.
 */

@WebServlet("/check-login")
public class CheckLoginServlet extends HttpServlet {

    private UserService userService = UserServiceFactory.getUserService();

    /**
     *  Returns a LoginStatusResponse object in JSON format.
     */

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isLoggedIn = false;
        String email = "";
        String link;

        if (userService.isUserLoggedIn()) {
            String urlToRedirectToAfterUserLogsOut = "/";
            String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
            email = userService.getCurrentUser().getEmail();
            isLoggedIn = true;
            link = logoutUrl;
        } else {
            String urlToRedirectToAfterUserLogsIn = "/";
            String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
            link = loginUrl;
        }

        response.setContentType("application/json");

        LoginStatusResponse loginStatusResponse = new LoginStatusResponse(isLoggedIn, email, link);
        String json = convertToJSON(loginStatusResponse);
        response.getWriter().println(json);
    }

    /**
     *  Converts a LoginStatusResponse object into JSON using Gson.
     */

    private String convertToJSON(LoginStatusResponse loginStatusResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<LoginStatusResponse>() {}.getType();
        String json = gson.toJson(loginStatusResponse, type);
        return json;
    }

}
