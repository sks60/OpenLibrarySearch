package com.example.sandysaju.openlibrarysearch;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "History_Table")
public class History {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String search;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public History(String search) {
        this.search = search;
    }
}
