package com.google.sps.servlets;

import java.io.IOException;
import com.google.sps.data.Comment;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 *  Servlet that handles deleting all comment data.
 */

@WebServlet("/delete-data")
public final class DeleteDataServlet extends HttpServlet {

    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    /**
     *  Delete all comment objects from the Datastore.
     */

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Comment");
        PreparedQuery results = datastore.prepare(query);

        for (Entity entity : results.asIterable()) {
            Key commentKey = entity.getKey();
            datastore.delete(commentKey);
        }

        response.setContentType("text/html;");
        response.getWriter().println("All comments deleted...");
    }

}
