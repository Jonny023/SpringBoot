package com.example.domain;

public class Book {

    public String aa;

    private String name;
    private String author;

    private Integer count;

    public Book() {
    }

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    private String test() {
        return "test method";
    }

    public Book(String name, String author, Integer count) {
        this.name = name;
        this.author = author;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", count=" + count +
                '}';
    }
}
