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
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    
	private ArrayList<Comment> comments = new ArrayList<Comment>();

	// comments.add("Wow! Your portfolio is super cool.");
	// comments.add("Neat! I have six siblings as well.");
	// comments.add("Awesome! We go to the same school.");

	@Override
  	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String json = convertToJson(comments);

    	response.setContentType("application/json;");
    	response.getWriter().println(json);
  	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("comment-name");
		String text = request.getParameter("comment-text");

        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("name", name);
        commentEntity.setProperty("text", text);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);

		addComment(name, text);

		response.sendRedirect("/index.html");
	}

	public void addComment(String name, String text) {
        int id = 0;

        if (comments.size() > 0) {
            id = comments.get(0).getID() + 1;
        }

        Comment comment = new Comment(id, name, text);
        comments.add(0, comment);
    }

    private String convertToJson(ArrayList<Comment> comments) {
        Gson gson = new Gson();
		Type type = new TypeToken<List<Comment>>(){}.getType();
    	String json = gson.toJson(comments, type);
    	return json;
  	}

}
