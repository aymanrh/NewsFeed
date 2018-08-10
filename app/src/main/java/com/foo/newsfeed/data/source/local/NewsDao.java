package com.foo.newsfeed.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.foo.newsfeed.data.NewsFeed;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news order by date desc")
    LiveData<List<NewsFeed>> getNews();


    @Query("SELECT * FROM news where isReadLater=1")
    LiveData<List<NewsFeed>> getReadLaterLive();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(NewsFeed task);

    @Query("Update news set isReadLater=:isreadlater where Id=:id")
    void setIsreadlater(boolean isreadlater, String id);

    @Query("DELETE FROM news")
    void deleteNews();

}
