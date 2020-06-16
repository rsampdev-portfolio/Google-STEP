package com.google.sps.data;

import java.time.Instant;
import com.google.appengine.api.datastore.Entity;

/**
 *  Class representing a comment with the commenter's email,
 *  the comment's text, and the comment's time of creation.
 *  
 *  <p>The id variable stores the Datastore Entity ID.
 *  
 *  <p>This class also handles conversion
 *  to and from Datastore Entities.
 */

public final class Comment {
    private final long id ;
    private final String email;
    private final String text;
    private final Instant time;
    
    private Comment(long id, String email, String text, Instant time) {
        this.email = email;
        this.text = text;
        this.time = time;
        this.id = id;
    }
    
    public static Entity createDatastoreCommentEntity(String email, String text, Instant time) {
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("email", email);
        commentEntity.setProperty("text", text);
        commentEntity.setProperty("time", time.toString());
        return commentEntity;
    }
    
    public static Comment buildCommentFromDatastoreCommentEntity(Entity entity) {
        Instant time = Instant.parse((String) entity.getProperty("time"));
        String email = (String) entity.getProperty("email");
        String text = (String) entity.getProperty("text");
        long id = entity.getKey().getId();
        return new Comment(id, email, text, time);
    }
    
    @Override
    public String toString() {
        return "Comment [id=" + id + ", email=" + email + ", text=" + text +", time=" + time + "]";
    }
}
