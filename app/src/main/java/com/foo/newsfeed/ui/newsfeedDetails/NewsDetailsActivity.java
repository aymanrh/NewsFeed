package com.foo.newsfeed.ui.newsfeedDetails;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.foo.newsfeed.R;
import com.foo.newsfeed.data.NewsFeed;
import com.foo.newsfeed.databinding.ActivityNewsDetailsBinding;
import com.foo.newsfeed.util.Constants;

public class NewsDetailsActivity extends AppCompatActivity {

    private static final String TAG = "NewsDetailsActivity";

    private ActivityNewsDetailsBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_details);
        NewsFeed newsFeed = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            newsFeed = (NewsFeed) getIntent().getExtras().get(Constants.EXTRA_NEWS);
            mBinding.setNews(newsFeed);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBinding.ivNew.setTransitionName(getString(R.string.activity_image_trans));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        scheduleStartPostponedTransition(mBinding.ivNew);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("News Details");
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startPostponedEnterTransition();
                        }
                        return true;
                    }
                });
    }
}
