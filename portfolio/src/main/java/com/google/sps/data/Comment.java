package com.google.sps.data;

/**
 *  Class representing a comment with a commenter name and the comment's text.
 */

public class Comment {
    private long id;
    private String name;
    private String text;
    private long time;

    public Comment(long id, String name, String text, long time) {
        this.id = id;
    	this.name = name;
        this.text = text;
        this.time = time;
    }

    public long getID() {
        return this.id;
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

    public long getTime() {
        return this.time;
    }

    public void setText(long time) {
        this.time = time;
    }

    @Override
	public String toString() {
        return "Comment [id=" + id + ", name=" + name + ", text=" + text +", time=" + time + "]";
    }
}
