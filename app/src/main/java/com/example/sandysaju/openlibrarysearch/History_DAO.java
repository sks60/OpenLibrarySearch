package com.example.sandysaju.openlibrarysearch;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface History_DAO{

    @Insert
    void insert(History history);

    @Query("Delete from History_Table")
    void deleteAllHistory();

    @Query("Select distinct search from History_Table order by id ASC")
    LiveData<List<String>> getHistoryList();

}
