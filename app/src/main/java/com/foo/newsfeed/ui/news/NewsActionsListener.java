package com.foo.newsfeed.ui.news;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.ImageView;

import com.foo.newsfeed.data.NewsFeed;

public interface NewsActionsListener {

    void onNewFeedClicked(NewsFeed newsFeed,View imageView);

    void onShareClicked(NewsFeed newsFeed);

    void onReadLaterClicked(NewsFeed newsFeed);

}
