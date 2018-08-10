package com.foo.newsfeed.data.source.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.foo.newsfeed.data.NewsFeed;
import com.foo.newsfeed.data.source.NewsDataSource;
import com.foo.newsfeed.data.source.remote.ApiPojo.News;
import com.foo.newsfeed.data.source.remote.ApiPojo.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRemoteDataSource implements NewsDataSource {

    private static NewsRemoteDataSource INSTANCE;
    private Api api;


    private static int CURRENT_PAGE = 1;
    private static int NUMBER_PAGES;
    private static int PAGE_SIZE = 10;
    private static String SHOW_FIELDS = "starRating,headline,thumbnail,short-url";
        private static final String API_KEY = "612dae07-b232-4eb7-b036-f08a434998c0";
//    private static final String API_KEY = "test";

    public static NewsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewsRemoteDataSource();
        }
        return INSTANCE;
    }

    private NewsRemoteDataSource() {
        api = ApiClient.getClient().create(Api.class);
    }


    @Override
    public void deleteAllTasks() {

    }

    @Override
    public void saveTask(NewsFeed newsFeed) {

    }


    @Override
    public LiveData<List<NewsFeed>> getNews(boolean forceUpdate) {
        final MutableLiveData<List<NewsFeed>> listLiveData = new MutableLiveData<>();
        Call<News> responseCall =
                api.getResponse(CURRENT_PAGE + "",
                        SHOW_FIELDS,
                        API_KEY);

        responseCall.enqueue(new Callback<com.foo.newsfeed.data.source.remote.ApiPojo.News>() {
            @Override
            public void onResponse(Call<com.foo.newsfeed.data.source.remote.ApiPojo.News> call, Response<News> response) {

                if (response.isSuccessful()) {

                    List<NewsFeed> newsFeeds = new ArrayList<>();
                    com.foo.newsfeed.data.source.remote.ApiPojo.News news = response.body();

                    com.foo.newsfeed.data.source.remote.ApiPojo.Response response1 = news.getResponse();
                    setCurrentPage(response1.getCurrentPage());
                    setNumberPages(response1.getPages());
                    setPageSize(response1.getPageSize());

                    List<Result> results = response1.getResults();
                    for (Result res : results) {

                        NewsFeed newsFeed = new NewsFeed(res.getId(), res.getWebTitle(), res.getFields().getThumbnail(), res.getSectionName(),
                                res.getWebPublicationDate(), Integer.parseInt(res.getFields().getStarRating() == null ? "-1" : res.getFields().getStarRating()), res.getFields().getShortUrl(), false);
                        newsFeeds.add(newsFeed);

                    }

                    if (!newsFeeds.isEmpty()) {

                        listLiveData.postValue(newsFeeds);

                    } else {
                        listLiveData.setValue(new ArrayList<NewsFeed>());
                    }
                } else {
                    listLiveData.setValue(new ArrayList<NewsFeed>());
                }


            }

            @Override
            public void onFailure(Call<com.foo.newsfeed.data.source.remote.ApiPojo.News> call, Throwable t) {
                listLiveData.setValue(null);
            }
        });
        return listLiveData;
    }

    public static int getPageSize() {
        return PAGE_SIZE;
    }

    private static void setPageSize(int pageSize) {
        PAGE_SIZE = pageSize;
    }

    public static int getCurrentPage() {
        return CURRENT_PAGE;
    }

    public static void setCurrentPage(int currentPage) {

        CURRENT_PAGE = currentPage;
    }

    public static int getNumberPages() {
        return NUMBER_PAGES;
    }

    public static void setNumberPages(int numberPages) {
        NUMBER_PAGES = numberPages;
    }


}
