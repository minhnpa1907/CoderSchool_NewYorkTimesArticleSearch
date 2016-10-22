package com.minhnpa.coderschool.newyorktimesarticlesearch.adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.minhnpa.coderschool.newyorktimesarticlesearch.R;
import com.minhnpa.coderschool.newyorktimesarticlesearch.model.Article;
import com.minhnpa.coderschool.newyorktimesarticlesearch.model.Media;
import com.minhnpa.coderschool.newyorktimesarticlesearch.utils.UIUtils;

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
                return new NoCoverViewHolder(parent.getContext(), itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_article, parent, false);
                return new ViewHolder(parent.getContext(), itemView);
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
        Glide.with(holder.itemView.getContext())
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivCover)
        ImageView ivCover;

        @BindView(R.id.tvContent)
        TextView tvContent;

        private Context context;
        private Bitmap bitmap;
        int requestCode = 100;

        ViewHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;

            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_share);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Article article = mArticles.get(position);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, "share");

                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
                builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl((Activity) context, Uri.parse(article.getWebUrl()));
            }
        }
    }

    public class NoCoverViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvContent)
        TextView tvContent;

        private Context context;
        private Bitmap bitmap;
        int requestCode = 100;

        NoCoverViewHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;

            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_share);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Article article = mArticles.get(position);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, "share");

                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
                builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl((Activity) context, Uri.parse(article.getWebUrl()));
            }
        }
    }
}
