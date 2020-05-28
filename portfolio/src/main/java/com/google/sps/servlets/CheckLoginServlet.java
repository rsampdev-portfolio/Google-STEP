package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 *    Servlet that checks if a user is logged in or not.
 */

@WebServlet("/check-login")
public class CheckLoginServlet extends HttpServlet {

    private UserService userService = UserServiceFactory.getUserService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        if (userService.isUserLoggedIn()) {
            String urlToRedirectToAfterUserLogsOut = "/";
            String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
            String userEmail = userService.getCurrentUser().getEmail();

            response.getWriter().println("<p>Logged In</p>");
            response.getWriter().println("<p>Email: " + userEmail + "</p>");
            response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a></p>");

            response.setHeader("loggedIn", "true");
        } else {
            String urlToRedirectToAfterUserLogsIn = "/";
            String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

            response.getWriter().println("<p>Not Logged In</p>");
            response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a></p>");

            response.setHeader("loggedIn", "false");
        }
    }

}