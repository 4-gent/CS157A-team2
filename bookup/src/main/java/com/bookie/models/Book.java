package com.bookie.models;

public class Book {
    private String ISBN;
    private String title;
    private int year;
    private String publisher;
    private boolean isFeatured;

    // Constructor
    public Book(String ISBN, String title, int year, String publisher, boolean isFeatured) {
        this.ISBN = ISBN;
        this.title = title;
        this.year = year;
        this.publisher = publisher;
        this.isFeatured = isFeatured;
    }

    // Getters and Setters
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    @Override
    public String toString() {
        return "Book [ISBN=" + ISBN + ", title=" + title + ", year=" + year + ", publisher=" + publisher + ", isFeatured=" + isFeatured + "]";
    }
}
