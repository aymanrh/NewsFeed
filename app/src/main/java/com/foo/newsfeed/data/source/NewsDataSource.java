package com.foo.newsfeed.data.source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.foo.newsfeed.data.NewsFeed;

import java.util.List;

public interface NewsDataSource {

    interface LoadNewsCallback {

        void onNewsLoaded(LiveData<List<NewsFeed>> tasks);

        void onDataNotAvailable();
    }

    void deleteAllTasks();

    void saveTask(NewsFeed newsFeed);

    LiveData<List<NewsFeed>> getNews(boolean forceUpodate);
}
