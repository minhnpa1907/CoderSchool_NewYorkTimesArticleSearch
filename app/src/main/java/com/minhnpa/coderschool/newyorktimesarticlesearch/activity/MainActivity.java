package com.minhnpa.coderschool.newyorktimesarticlesearch.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.minhnpa.coderschool.newyorktimesarticlesearch.R;
import com.minhnpa.coderschool.newyorktimesarticlesearch.adapter.ArticleAdapter;
import com.minhnpa.coderschool.newyorktimesarticlesearch.api.ArticleApi;
import com.minhnpa.coderschool.newyorktimesarticlesearch.model.SearchRequest;
import com.minhnpa.coderschool.newyorktimesarticlesearch.model.SearchResult;
import com.minhnpa.coderschool.newyorktimesarticlesearch.utils.RetrofitUtils;

import java.util.Calendar;

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
    private SearchView mSearchView;
    private String sort = "Newest";
    private boolean filterArts = false;
    private boolean filterFashionStyle = false;
    private boolean filterSports = false;
    private int day = 21;
    private int month = 6;
    private int year = 2015;
    private boolean networkConnected;

    public final int REQUEST_FILTER = 9001;

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

        networkConnected = isNetworkAvailable();
        if (networkConnected) {
            setUpApi();
            setUpViews();
            search();
        } else {
            Toast.makeText(this, "Can't connect to the network", Toast.LENGTH_LONG).show();
        }
    }

    private void setUpViews() {
        mArticleAdapter = new ArticleAdapter();
        mArticleAdapter.setListener(this::searchMore);
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
        fetchArticles(searchResult -> {
            mArticleAdapter.setArticle(searchResult.getArticles());
            rvArticle.scrollToPosition(0);
        });
    }

    private void searchMore() {
        mSearchRequest.nextPage();
        pbLoadMore.setVisibility(View.VISIBLE);
        fetchArticles(searchResult -> mArticleAdapter.addArticle(searchResult.getArticles()));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mSearchRequest.setQuery(null);
                search();
                return true;
            }
        });
        setUpSearchView(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpSearchView(MenuItem menuItem) {
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                mSearchRequest.setQuery(query);
                search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                Bundle bundle = new Bundle();

                bundle.putInt("day", day);
                bundle.putInt("month", month);
                bundle.putInt("year", year);
                bundle.putString("sort", sort);
                bundle.putBoolean("filter_arts", filterArts);
                bundle.putBoolean("filter_fashion_style", filterFashionStyle);
                bundle.putBoolean("filter_sports", filterSports);

                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_FILTER);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FILTER && resultCode == RESULT_OK) {
            day = data.getExtras().getInt("day_picked");
            month = data.getExtras().getInt("month_picked");
            year = data.getExtras().getInt("year_picked");
            sort = data.getExtras().getString("sorted");
            filterArts = data.getExtras().getBoolean("filtered_arts");
            filterFashionStyle = data.getExtras().getBoolean("filtered_fashion_style");
            filterSports = data.getExtras().getBoolean("filtered_sports");

            StringBuilder date = new StringBuilder();
            date.append(year);
            if (month < 10) {
                date.append("0").append(month);
            } else {
                date.append(month);
            }
            if (day < 10) {
                date.append("0").append(day);
            } else {
                date.append(day);
            }
            mSearchRequest.setBeginDate(date.toString());
            mSearchRequest.setSort(sort);
            mSearchRequest.setFilterArts(filterArts);
            mSearchRequest.setFilterFashionStyle(filterFashionStyle);
            mSearchRequest.setFilterSports(filterSports);
            search();
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
