package com.bookie.models;

public class Author {
	
	private int authorID;
	private String name;
	
	public Author(String name) {
		super();
		this.name = name;
	}
	
	public Author(int authorID, String name) {
		super();
		this.authorID = authorID;
		this.name = name;
	}
	
	public void setAuthorID(int authorID) {
		this.authorID = authorID;
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
