package com.bookie.models;

public class Genre {

	private int genreID;
	private String name;
	
	public Genre(int genreID, String name) {
		super();
		this.genreID = genreID;
		this.name = name;
	}

	public int getGenreID() {
		return genreID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
