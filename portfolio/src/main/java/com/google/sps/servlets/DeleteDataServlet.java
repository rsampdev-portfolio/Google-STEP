package com.google.sps.servlets;

@WebServlet("/delete-data")
public class DeleteDataServlet extends HttpServlet {

    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

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