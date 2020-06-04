// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.util.List;
import java.time.Instant;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.sps.data.Comment;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServlet;
import com.google.gson.reflect.TypeToken;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 *  Servlet that handles creating and also returning comment data.
 */

@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    private final UserService userService = UserServiceFactory.getUserService();

    /**
     *  Returns all comment objects in the Datastore in JSON format in descending order.
     */

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int maxNumberOfComments = 20;
        
        Query query = new Query("Comment").addSort("time", SortDirection.DESCENDING);
        PreparedQuery results = datastore.prepare(query);

        int numberOfComments = 0;

        if (request.getParameter("max-comments") != null) {
            String maxNumberOfCommentsString = request.getParameter("max-comments");
            maxNumberOfComments = Integer.parseInt(maxNumberOfCommentsString);
        }
        
        response.setContentType("application/json;");

        List<Comment> comments = results.asList(FetchOptions.Builder.withLimit(maxNumberOfComments)).stream().map(Comment::fromDatastoreEntity).collect(Collectors.toList());
        String json = convertToJSON(comments);
        response.getWriter().println(json);
    }

    /**
     *  Create a new comment object and add it to the Datastore.
     */

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = userService.getCurrentUser().getEmail();
        String text = request.getParameter("comment-text");
        Instant time = Instant.now();

        Comment comment = new Comment(email, text, time);
        Entity commentEntity = comment.toDatastoreEntity();

        datastore.put(commentEntity);

        response.sendRedirect("/index.html");
    }

    /**
     *  Converts a List of Comment objects into JSON using Gson.
     */

    private String convertToJSON(List<Comment> comments) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>() {}.getType();
        String json = gson.toJson(comments, type);
        return json;
    }
    
}
