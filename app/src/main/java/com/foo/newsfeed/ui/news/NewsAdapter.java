package com.foo.newsfeed.ui.news;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.foo.newsfeed.R;
import com.foo.newsfeed.data.NewsFeed;
import com.foo.newsfeed.databinding.ItemNewsBinding;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean isLoading = false;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    private List<NewsFeed> mNews;
    private NewsActionsListener newsActionsListener;

    public NewsAdapter(List<NewsFeed> newsFeeds,
                        NewsActionsListener newsActionsListener) {

        this.newsActionsListener = newsActionsListener;
        setList(newsFeeds);

    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void replaceData(List<NewsFeed> newsFeeds) {
        setList(newsFeeds);
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return mNews.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater layoutInflater =
                    LayoutInflater.from(parent.getContext());
            ItemNewsBinding itemBinding =
                    ItemNewsBinding.inflate(layoutInflater, parent, false);
            return new NewsViewHolder(itemBinding);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolder) {
            NewsViewHolder vh = (NewsViewHolder) holder;
            NewsFeed item = getItemForPosition(position);

            vh.bind(item, newsActionsListener);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    private NewsFeed getItemForPosition(int position) {
        return mNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mNews == null ? 0 : mNews.size();
    }

    private void setList(List<NewsFeed> newsFeeds) {
        mNews = newsFeeds;
        notifyDataSetChanged();
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        private final ItemNewsBinding binding;

        public NewsViewHolder(ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(NewsFeed newsFeed, NewsActionsListener actionsListener) {
            binding.ivBookMark.setSelected(newsFeed.isReadLater());
            binding.setNews(newsFeed);
            binding.setListener(actionsListener);
            binding.executePendingBindings();
        }
    }
}
