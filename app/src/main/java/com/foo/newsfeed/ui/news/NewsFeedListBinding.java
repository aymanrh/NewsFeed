package com.foo.newsfeed.ui.news;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.foo.newsfeed.R;
import com.foo.newsfeed.data.NewsFeed;
import com.foo.newsfeed.util.GlideApp;

import java.util.List;

public class NewsFeedListBinding {
    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<NewsFeed> newsFeeds) {
        NewsAdapter adapter = (NewsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(newsFeeds);
        }
    }

    @BindingAdapter({"bind:image"})
    public static void imageLoader(ImageView imageView, String url) {


        imageView.setVisibility(View.VISIBLE);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.news_feeds);
        requestOptions.error(R.drawable.news_feeds);
        requestOptions.centerCrop();
        GlideApp.with(imageView).setDefaultRequestOptions(requestOptions).load(url)
                .centerInside().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);


    }
}
