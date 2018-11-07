package com.example.sandysaju.openlibrarysearch;

import android.arch.persistence.room.Database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {History.class, Favourite.class}, version = 4)

abstract class MyDatabase extends RoomDatabase {

    public abstract Favourites_DAO favourites_dao();
    public abstract History_DAO history_dao();

    private static MyDatabase myDatabaseInstance;
    static MyDatabase getDatabase(final Context context)
    {
        if (myDatabaseInstance == null)
        {
            synchronized (MyDatabase.class)
            {
                if (myDatabaseInstance == null)
                {
                    myDatabaseInstance = Room.databaseBuilder(context.getApplicationContext() , MyDatabase.class , "MyDatabase").build();
                }
            }
        }
        return myDatabaseInstance;
    }

}
