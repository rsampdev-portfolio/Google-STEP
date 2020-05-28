package com.google.sps.data;

import java.time.Instant;
import com.google.appengine.api.datastore.Entity;

/**
 *  Class representing a comment with the commenter's name,
 *  the comment's text, and the comment's time of creation.
 *  
 *  This class also handles conversion
 *  to and from Datastore Entities.
 */

public class Comment {
    private long id = 0L;
    private String name;
    private String email;
    private String text;
    private Instant time;

    public Comment(String name, String email, String text, Instant time) {
    	this.name = name;
        this.text = text;
        this.time = time;
        this.email = email;
    }

    public Comment(long id, String name, String email, String text, Instant time) {
        this.id = id;
    	this.name = name;
        this.text = text;
        this.time = time;
        this.email = email;
    }

    public long getID() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
		return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getTime() {
        return this.time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Entity toDatastoreEntity() {
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("name", name);
        commentEntity.setProperty("email", email);
        commentEntity.setProperty("text", text);
        commentEntity.setProperty("time", time.toString());
        return commentEntity;
    }

    public static Comment fromDatastoreEntity(Entity entity) {
        Instant time = Instant.parse((String) entity.getProperty("time"));
        String name = (String) entity.getProperty("name");
        String email = (String) entity.getProperty("email");
        String text = (String) entity.getProperty("text");
        long id = entity.getKey().getId();

        Comment comment = new Comment(id, name, email, text, time);

        return comment;
    }
    
    @Override
	public String toString() {
        return "Comment [id=" + id + ", name=" + name + ", email=" + email + "text=" + text +", time=" + time + "]";
    }
}
