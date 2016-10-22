package com.minhnpa.coderschool.newyorktimesarticlesearch.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.minhnpa.coderschool.newyorktimesarticlesearch.R;
import com.minhnpa.coderschool.newyorktimesarticlesearch.adapter.ArticleAdapter;
import com.minhnpa.coderschool.newyorktimesarticlesearch.api.ArticleApi;
import com.minhnpa.coderschool.newyorktimesarticlesearch.model.SearchRequest;
import com.minhnpa.coderschool.newyorktimesarticlesearch.model.SearchResult;
import com.minhnpa.coderschool.newyorktimesarticlesearch.utils.RetrofitUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SearchRequest mSearchRequest;
    private ArticleApi mArticleApi;
    private ArticleAdapter mArticleAdapter;
    private StaggeredGridLayoutManager mLayoutManager;

    @BindView(R.id.rvArticle)
    RecyclerView rvArticle;

    @BindView(R.id.pbLoading)
    RelativeLayout pbLoading;

    @BindView(R.id.pbLoadMore)
    ProgressBar pbLoadMore;

    private interface Listener {
        void onResult(SearchResult searchResult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpApi();
        setUpViews();
        search();
    }

    private void setUpViews() {
        mArticleAdapter = new ArticleAdapter();
        mArticleAdapter.setListener(new ArticleAdapter.Listener() {
            @Override
            public void onLoadMore() {
                searchMore();
            }
        });
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvArticle.setLayoutManager(mLayoutManager);
        rvArticle.setAdapter(mArticleAdapter);
    }

    private void setUpApi() {
        mSearchRequest = new SearchRequest();
        mArticleApi = RetrofitUtils.get().create(ArticleApi.class);
    }

    private void search() {
        mSearchRequest.resetPage();
        pbLoading.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                mArticleAdapter.setArticle(searchResult.getArticles());
                rvArticle.scrollToPosition(0);
            }
        });
    }

    private void searchMore() {
        mSearchRequest.nextPage();
        pbLoading.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                mArticleAdapter.addArticle(searchResult.getArticles());
            }
        });
    }


    private void fetchArticles(final Listener listener) {
        mArticleApi.searchResult(mSearchRequest.toQueryMap()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.body() != null) {
                    listener.onResult(response.body());
                }
                handleComplete();
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void handleComplete() {
        pbLoading.setVisibility(View.GONE);
        pbLoadMore.setVisibility(View.GONE);
    }
}
