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
import javax.servlet.http.HttpServlet;
import com.google.gson.reflect.TypeToken;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 *    Servlet that handles comment data.
 */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Comment> comments = new ArrayList<>();
        
        Query query = new Query("Comment").addSort("time", SortDirection.DESCENDING);
        PreparedQuery results = datastore.prepare(query);

        for (Entity entity : results.asIterable()) {
            long id = entity.getKey().getId();
            String name = (String) entity.getProperty("name");
            String text = (String) entity.getProperty("text");
            Instant time = Instant.parse((String) entity.getProperty("time"));

            Comment comment = new Comment(id, name, text, time);
            comments.add(comment);
        }

        String json = convertToJSON(comments);

        response.setContentType("application/json;");
        response.getWriter().println(json);
      }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("comment-name");
        String text = request.getParameter("comment-text");
        Instant time = Instant.now();

        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("name", name);
        commentEntity.setProperty("text", text);
        commentEntity.setProperty("time", time);

        datastore.put(commentEntity);

        response.sendRedirect("/index.html");
    }

    private String convertToJSON(List<Comment> comments) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>(){}.getType();
        String json = gson.toJson(comments, type);
        return json;
      }

}
