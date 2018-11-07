package com.example.sandysaju.openlibrarysearch;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Favourites_Table")
public class Favourite {

    @PrimaryKey(autoGenerate = true)
    private int intID;

    private String isbn;
    private String url;
    private String bookTitle;
    private String year;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Favourite(String isbn, String url, byte[] image, String bookTitle, String year) {
        this.isbn = isbn;
        this.url = url;
        this.image = image;
        this.bookTitle = bookTitle;
        this.year = year;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
