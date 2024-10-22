package com.bookie.models;

public class Author {
	
	private int authorID;
	private String name;
	
	public Author(int id, String name) {
		super();
		this.authorID = id;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAuthorID() {
		return authorID;
	}
	

}
