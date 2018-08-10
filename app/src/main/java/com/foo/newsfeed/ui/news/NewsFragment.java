package com.foo.newsfeed.ui.news;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foo.newsfeed.R;
import com.foo.newsfeed.data.NewsFeed;
import com.foo.newsfeed.data.source.remote.NewsRemoteDataSource;
import com.foo.newsfeed.databinding.FragmentNewsBinding;
import com.foo.newsfeed.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {


    private FragmentNewsBinding mBinding;
    private NewsViewModel mViewModel;
    private NewsAdapter mNewsAdapter;
    private NewsActionsListener mListener;


    public NewsFragment() {
        // Requires empty public constructor
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewsActionsListener) {
            mListener = (NewsActionsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentNewsBinding.inflate(inflater, container, false);

        mViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        mBinding.setViewmodel(mViewModel);

        setupListAdapter();


        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getNewListLiveData()
                .observe(this, new Observer<List<NewsFeed>>() {
                    @Override
                    public void onChanged(@Nullable List<NewsFeed> newsFeeds) {
                        mViewModel.dataLoading.set(false);
                        if (newsFeeds == null || newsFeeds.isEmpty()) {
                            mViewModel.empty.set(true);
                            mViewModel.emptyLabel.set(getResources().getString(R.string.no_data));
                        } else {
                            mViewModel.empty.set(false);

                            mViewModel.items.clear();
                            mViewModel.items.addAll(newsFeeds);
                            if (NewsRemoteDataSource.getCurrentPage() < NewsRemoteDataSource.getNumberPages()) {
                                mViewModel.items.add(null);
                            }
                        }


                    }
                });
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolBarTitle("News Feed");
        }

    }

    private void setupListAdapter() {

        RecyclerView recyclerView = mBinding.newsList;
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        mNewsAdapter = new NewsAdapter(
                new ArrayList<NewsFeed>(0),
                mListener);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {


                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!mNewsAdapter.isLoading() && totalItemCount <= (lastVisibleItem + NewsRemoteDataSource.getPageSize())) {
                        if (mViewModel != null) {

                            double currentVisible = (double) totalItemCount / NewsRemoteDataSource.getPageSize();
                            double factor = Math.pow(1, 1);
                            double current = Math.round(currentVisible * factor) / factor;

                            NewsRemoteDataSource.setCurrentPage((int) (current + 1));
                            mViewModel.loadNews(true, false);
                        }
                        mNewsAdapter.setLoading(true);
                    }
                    mNewsAdapter.setLoaded();
                }
            }
        });
        recyclerView.setAdapter(mNewsAdapter);
    }


}
