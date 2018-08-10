package com.foo.newsfeed.data.source.remote;

import com.foo.newsfeed.data.source.remote.ApiPojo.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://content.guardianapis.com/";


//    @GET("/search?q=12%20years%20a%20slave&format=json&tag=film/film,tone/reviews&from-date=2010-01-01&show-tags=contributor&show-fields=starRating,headline,thumbnail,short-url&order-by=relevance&api-key=test")
//    Call<News> getResponse();

    @GET("/search?")
    Call<News> getResponse(@Query("page") String currentPage ,
                           @Query("show-fields") String ShowFields,@Query("api-key") String apiKey);



}
