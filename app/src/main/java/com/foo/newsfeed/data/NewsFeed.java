package com.foo.newsfeed.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "news")
public class NewsFeed implements Serializable {

    @NonNull
    @PrimaryKey
    private String Id;
    private String title;
    private String image;
    private String category;
    private String date;
    private int starRating;
    private String shortUrl;
    private boolean isReadLater;


    public NewsFeed() {
    }

    public NewsFeed(@NonNull String id, String title, String image, String category, String date,
                    int starRating, String shortUrl, boolean isReadLater) {
        Id = id;
        this.title = title;
        this.image = image;
        this.category = category;
        this.date = date;
        this.starRating = starRating;
        this.shortUrl = shortUrl;
        this.isReadLater = isReadLater;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isReadLater() {
        return isReadLater;
    }

    public void setReadLater(boolean readLater) {
        isReadLater = readLater;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsFeed newsFeed = (NewsFeed) o;
        return Objects.equals(Id, newsFeed.Id) &&
                Objects.equals(title, newsFeed.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Id);
    }
}
