package com.example.sandysaju.openlibrarysearch;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface Favourites_DAO {

    @Insert
    void insert(Favourite emp);

    @Query("Delete from Favourites_Table")
    void deleteAllFavourite();

    @Query("Select * from Favourites_Table order by isbn ASC")
    LiveData<List<Favourite>> getFavouriteList();

    @Query("Select count(*) from Favourites_Table where isbn = :isbn")
    int countFav(String isbn);

    @Query("Delete from Favourites_Table where isbn = :isbn")
    int deleteFav(String isbn);

}
