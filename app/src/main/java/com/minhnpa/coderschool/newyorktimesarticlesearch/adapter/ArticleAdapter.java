package com.minhnpa.coderschool.newyorktimesarticlesearch.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhnpa.coderschool.newyorktimesarticlesearch.R;
import com.minhnpa.coderschool.newyorktimesarticlesearch.model.Article;
import com.minhnpa.coderschool.newyorktimesarticlesearch.model.Media;
import com.minhnpa.coderschool.newyorktimesarticlesearch.utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MINH NPA on 21 Oct 2016.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int NORMAL = 0;
    private final int UNNORMAL = 1;
    private List<Article> mArticles;
    private Listener mListener;

    public interface Listener {
        void onLoadMore();
    }

    public ArticleAdapter() {
        this.mArticles = new ArrayList<>();
    }

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public void setArticle(List<Article> articles) {
        mArticles.clear();
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    public void addArticle(List<Article> articles) {
        int position = mArticles.size();
        mArticles.addAll(articles);
        notifyItemRangeInserted(position, articles.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case UNNORMAL:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_article_no_cover, parent, false);
                return new NoCoverViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_article, parent, false);
                return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = mArticles.get(position);
        if (holder instanceof NoCoverViewHolder) {
            bindUnNormal(article, (NoCoverViewHolder) holder);
        } else {
            bindNormal(article, (ViewHolder) holder);
        }
        if (position == mArticles.size() - 1 && mListener != null) {
            mListener.onLoadMore();
        }
    }

    private void bindNormal(Article article, ViewHolder holder) {
        holder.tvContent.setText(article.getSnippet());
        Media media = article.getMultimedia().get(0);
        ViewGroup.LayoutParams layoutParams = holder.ivCover.getLayoutParams();
        layoutParams.height = UIUtils.covertPixelToDp(media.getHeight(),
                holder.itemView.getContext());
        holder.ivCover.setLayoutParams(layoutParams);
        Picasso.with(holder.itemView.getContext())
                .load(media.getUrl())
                .into(holder.ivCover);
    }

    private void bindUnNormal(Article article, NoCoverViewHolder holder) {
        holder.tvContent.setText(article.getSnippet());
    }

    @Override
    public int getItemViewType(int position) {
        Article article = mArticles.get(position);
        if (article.getMultimedia() != null && !article.getMultimedia().isEmpty()) {
            return NORMAL;
        }

        return UNNORMAL;
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivCover)
        ImageView ivCover;

        @BindView(R.id.tvContent)
        TextView tvContent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class NoCoverViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvContent)
        TextView tvContent;

        NoCoverViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
