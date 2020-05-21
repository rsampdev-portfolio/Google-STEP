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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
<<<<<<< HEAD
    
	private ArrayList<Comment> comments = new ArrayList<Comment>();

	// comments.add("Wow! Your portfolio is super cool.");
	// comments.add("Neat! I have six siblings as well.");
	// comments.add("Awesome! We go to the same school.");
=======

	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
>>>>>>> Implement storing and retrieving comments with Datastore

	@Override
  	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		
		Query query = new Query("Comment").addSort("time", SortDirection.DESCENDING);
		PreparedQuery results = datastore.prepare(query);

		Comment comment;

		for (Entity entity : results.asIterable()) {
			long id = entity.getKey().getId();
      		String name = (String) entity.getProperty("name");
      		String text = (String) entity.getProperty("text");
			long time = (long) entity.getProperty("time");

			comment = new Comment(id, name, text, time);
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
		long time = System.currentTimeMillis();

        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("name", name);
        commentEntity.setProperty("text", text);
        commentEntity.setProperty("time", time);

        datastore.put(commentEntity);

		response.sendRedirect("/index.html");
	}

    private String convertToJSON(ArrayList<Comment> comments) {
        Gson gson = new Gson();
		Type type = new TypeToken<List<Comment>>(){}.getType();
    	String json = gson.toJson(comments, type);
    	return json;
  	}

}
