package com.foo.newsfeed.ui.readlater;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foo.newsfeed.R;
import com.foo.newsfeed.data.NewsFeed;
import com.foo.newsfeed.databinding.FragmentNewsBinding;
import com.foo.newsfeed.ui.MainActivity;
import com.foo.newsfeed.ui.news.NewsActionsListener;
import com.foo.newsfeed.ui.news.NewsAdapter;
import com.foo.newsfeed.ui.news.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReadLaterFragment extends Fragment {


    private FragmentNewsBinding mBinding;
    private NewsViewModel mViewModel;
    private NewsAdapter mNewsAdapter;
    private NewsActionsListener mListener;


    public ReadLaterFragment() {
        // Requires empty public constructor
    }

    public static ReadLaterFragment newInstance() {
        return new ReadLaterFragment();
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
    public void onResume() {
        super.onResume();
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
        mViewModel.getReadLaterLiveData()
                .observe(this, new Observer<List<NewsFeed>>() {
                    @Override
                    public void onChanged(@Nullable List<NewsFeed> newsFeeds) {
                        if (newsFeeds == null || newsFeeds.isEmpty()) {
                            mViewModel.empty.set(true);
                            mViewModel.emptyLabel.set(getResources().getString(R.string.no_data));
                        } else {
                            mNewsAdapter.replaceData(newsFeeds);
                        }

                    }
                });

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolBarTitle("Read Later");
        }

    }

    private void setupListAdapter() {
        RecyclerView recyclerView = mBinding.newsList;
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        mNewsAdapter = new NewsAdapter(
                new ArrayList<NewsFeed>(0),
                mListener);
        recyclerView.setAdapter(mNewsAdapter);
    }
}
