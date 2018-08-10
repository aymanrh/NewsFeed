package com.foo.newsfeed.data.source.local;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.foo.newsfeed.data.NewsFeed;
import com.foo.newsfeed.data.source.NewsDataSource;
import com.foo.newsfeed.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class NewsLocalDataSource implements NewsDataSource {

    private static volatile NewsLocalDataSource INSTANCE;
    private NewsDao mNewsDao;

    private AppExecutors mAppExecutors;

    private NewsLocalDataSource(@NonNull AppExecutors appExecutors,
                                @NonNull NewsDao newsDao) {
        mAppExecutors = appExecutors;
        mNewsDao = newsDao;
    }

    public static NewsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                  @NonNull NewsDao newsDao) {
        if (INSTANCE == null) {
            synchronized (NewsLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NewsLocalDataSource(appExecutors, newsDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<NewsFeed>> getNews(boolean forceUpodate) {

        return mNewsDao.getNews();
    }

    @Override
    public void saveTask(@NonNull final NewsFeed newsFeed) {
        checkNotNull(newsFeed);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mNewsDao.insertTask(newsFeed);
            }
        };

        mAppExecutors.diskIO().execute(saveRunnable);
    }


    public void setIsreadlater(@NonNull final NewsFeed newsFeed) {
        checkNotNull(newsFeed);

        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                boolean isreadlater = false;
                if (!newsFeed.isReadLater()) {
                    isreadlater = true;
                }

                mNewsDao.setIsreadlater(isreadlater, newsFeed.getId());
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }


    @Override
    public void deleteAllTasks() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mNewsDao.deleteNews();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }


    static void clearInstance() {
        INSTANCE = null;
    }

}
