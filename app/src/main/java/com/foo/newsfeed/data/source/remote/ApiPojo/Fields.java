package com.foo.newsfeed.data.source.remote.ApiPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("headline")
    @Expose
    private String headline;
    @SerializedName("starRating")
    @Expose
    private String starRating;
    @SerializedName("shortUrl")
    @Expose
    private String shortUrl;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}