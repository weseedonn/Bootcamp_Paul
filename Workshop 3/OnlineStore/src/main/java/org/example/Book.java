package org.example;

public class Book {

    private int id;
    private String isbn;
    private String title;
    private String checkedOutTo;
    private boolean isCheckedOut;

    public Book(int id, String isbn, String title) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.checkedOutTo = "";
        this.isCheckedOut = false;
    }

    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getCheckedOutTo() {
        return checkedOutTo;
    }

    public boolean getIsCheckedOut() {
        return isCheckedOut;
    }

    public void checkOut(String name) {
        checkedOutTo = name;
        isCheckedOut = true;
    }

    public void checkIn() {
        checkedOutTo = "";
        isCheckedOut = false;
    }
}