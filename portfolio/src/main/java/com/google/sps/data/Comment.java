package com.google.sps.data;

/**
 *  Class representing a comment with a commenter name and the comment's text.
 */

public class Comment {
    private int id;
    private String name;
    private String text;

    public Comment(int id, String name, String text) {
        this.id = id;
    	this.name = name;
        this.text = text;
    }

    public int getID() {
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

    @Override
	public String toString() {
        return "Comment [id=" + id + ", name=" + name + ", text=" + text +"]";
    }
}
