package com.foo.newsfeed.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.foo.newsfeed.data.NewsFeed;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class NewsRepository implements NewsDataSource {

    private volatile static NewsRepository INSTANCE = null;


    private final NewsDataSource mNewsRemoteDataSource;

    private final NewsDataSource mNewsLocalDataSource;


    private NewsRepository(@NonNull NewsDataSource newsRemoteDataSource,
                           @NonNull NewsDataSource newsLocalDataSource) {
        mNewsRemoteDataSource = checkNotNull(newsRemoteDataSource);
        mNewsLocalDataSource = checkNotNull(newsLocalDataSource);
    }

    public static NewsRepository getInstance(NewsDataSource newsRemoteDataSource,
                                             NewsDataSource newsLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (NewsRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NewsRepository(newsRemoteDataSource, newsLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void deleteAllTasks() {
        mNewsRemoteDataSource.deleteAllTasks();
        mNewsLocalDataSource.deleteAllTasks();

    }

    @Override
    public void saveTask(@NonNull NewsFeed newsFeed) {
        checkNotNull(newsFeed);
        mNewsLocalDataSource.saveTask(newsFeed);
    }

    @Override
    public LiveData<List<NewsFeed>> getNews(boolean forceUpodate) {

        LiveData<List<NewsFeed>> remoteLiveData;
        LiveData<List<NewsFeed>> localLiveData;

        if (forceUpodate) {
            remoteLiveData = mNewsRemoteDataSource.getNews(forceUpodate);

            remoteLiveData.observeForever(new Observer<List<NewsFeed>>() {
                @Override
                public void onChanged(@Nullable List<NewsFeed> newsFeeds) {
                    if (newsFeeds != null && !newsFeeds.isEmpty())
                        refreshLocalDataSource(newsFeeds);
                }
            });

        }
        return mNewsLocalDataSource.getNews(forceUpodate);


    }


    private void refreshLocalDataSource(List<NewsFeed> newsFeeds) {
//        mNewsLocalDataSource.deleteAllTasks();
        for (NewsFeed newsFeed : newsFeeds) {

            mNewsLocalDataSource.saveTask(newsFeed);
        }
    }
}
