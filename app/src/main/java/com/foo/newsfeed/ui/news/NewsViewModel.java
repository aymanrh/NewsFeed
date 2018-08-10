package com.foo.newsfeed.ui.news;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.foo.newsfeed.data.NewsFeed;
import com.foo.newsfeed.data.source.NewsRepository;
import com.foo.newsfeed.data.source.local.AppDataBase;
import com.foo.newsfeed.data.source.local.NewsLocalDataSource;
import com.foo.newsfeed.data.source.remote.NewsRemoteDataSource;
import com.foo.newsfeed.util.AppExecutors;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class NewsViewModel extends AndroidViewModel {

    private final NewsRepository mNewsRepository;

    public final ObservableBoolean empty = new ObservableBoolean(false);


    public final ObservableField<String> emptyLabel = new ObservableField<>();
    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private LiveData<List<NewsFeed>> newListLiveData;
    private LiveData<List<NewsFeed>> readLaterLiveData;
    public final ObservableList<NewsFeed> items = new ObservableArrayList<>();
    private Context mContext;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        mContext = application;
        AppDataBase appDataBase = AppDataBase.getInstance(application);
        mNewsRepository = NewsRepository.getInstance(NewsRemoteDataSource.getInstance(),
                NewsLocalDataSource.getInstance(new AppExecutors(), appDataBase.newsDao()));
        newListLiveData = appDataBase.newsDao().getNews();
        readLaterLiveData = appDataBase.newsDao().getReadLaterLive();
    }

    public LiveData<List<NewsFeed>> getReadLaterLiveData() {
        return readLaterLiveData;
    }

    public LiveData<List<NewsFeed>> getNewListLiveData() {
        return newListLiveData;
    }

    public void start() {
        loadNews(false, true);
        Timer mTimer = new Timer();
//        mTimer.cancel();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                NewsRemoteDataSource.setCurrentPage((1));
                loadNews(true, true);
            }
        }, 3000, 30000);
    }

    public void loadNews(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            dataLoading.set(true);
        }

        newListLiveData = mNewsRepository.getNews(forceUpdate);
        if (showLoadingUI) {
            dataLoading.set(false);
        }
    }

}
