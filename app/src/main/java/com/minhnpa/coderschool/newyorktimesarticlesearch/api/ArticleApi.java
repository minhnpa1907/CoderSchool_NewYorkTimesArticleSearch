package com.minhnpa.coderschool.newyorktimesarticlesearch.api;

import com.minhnpa.coderschool.newyorktimesarticlesearch.model.SearchResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by MINH NPA on 21 Oct 2016.
 */

public interface ArticleApi {
    @GET("articlesearch.json")
    Call<SearchResult> searchResult(@QueryMap(encoded = true) Map<String, String> options);
}
