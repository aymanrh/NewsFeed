package com.foo.newsfeed.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.foo.newsfeed.data.NewsFeed;

@Database(entities = {NewsFeed.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;

    public abstract NewsDao newsDao();

    private static final Object sLock = new Object();

    public static AppDataBase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, "News.db")
                        .build();
            }
            return INSTANCE;
        }
    }

}
