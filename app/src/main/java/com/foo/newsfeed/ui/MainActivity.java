package com.foo.newsfeed.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.foo.newsfeed.R;
import com.foo.newsfeed.data.NewsFeed;
import com.foo.newsfeed.data.source.local.AppDataBase;
import com.foo.newsfeed.data.source.local.NewsLocalDataSource;
import com.foo.newsfeed.ui.news.NewsActionsListener;
import com.foo.newsfeed.ui.news.NewsFragment;
import com.foo.newsfeed.ui.newsfeedDetails.NewsDetailsActivity;
import com.foo.newsfeed.ui.readlater.ReadLaterFragment;
import com.foo.newsfeed.util.AppExecutors;
import com.foo.newsfeed.util.Constants;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements NewsActionsListener {

    private NewsFragment newsFragment;
    private ReadLaterFragment readLaterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (newsFragment == null) {
            // Create the fragment
            newsFragment = NewsFragment.newInstance();
            checkNotNull(
                    getSupportFragmentManager());
            checkNotNull(newsFragment);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFrame, newsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }

    public void setToolBarTitle(String toolBarTitle) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(toolBarTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.read_later:
                if (newsFragment != null) {

                    // Create the fragment
                    readLaterFragment = ReadLaterFragment.newInstance();
                    checkNotNull(
                            getSupportFragmentManager());
                    checkNotNull(readLaterFragment);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.contentFrame, readLaterFragment);

                    transaction.commit();
                }
//                startActivity(new Intent(this, ReadLaterActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.contentFrame) instanceof NewsFragment) {
            finish();

        } else if (getSupportFragmentManager().findFragmentById(R.id.contentFrame) instanceof ReadLaterFragment) {
            if (newsFragment == null)
                newsFragment = NewsFragment.newInstance();
            checkNotNull(
                    getSupportFragmentManager());
            checkNotNull(newsFragment);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFrame, newsFragment);
            transaction.commit();
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public void onNewFeedClicked(NewsFeed newsFeed, View view) {
        ImageView imageView = view.findViewById(R.id.iv_new);

        Intent intent = new Intent(MainActivity.this, NewsDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_NEWS, newsFeed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Pair<View, String> pair1 = Pair.create((View) imageView, imageView.getTransitionName());

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, pair1);


            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    @Override
    public void onShareClicked(NewsFeed newsFeed) {

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, newsFeed.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, newsFeed.getShortUrl());

        startActivity(Intent.createChooser(share, "Share link!"));
    }

    @Override
    public void onReadLaterClicked(NewsFeed newsFeed) {

        AppDataBase appDataBase = AppDataBase.getInstance(getApplicationContext());
        NewsLocalDataSource newsLocalDataSource = NewsLocalDataSource.getInstance(new AppExecutors(), appDataBase.newsDao());
        newsLocalDataSource.setIsreadlater(newsFeed);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);

        postponeEnterTransition();

    }

}
