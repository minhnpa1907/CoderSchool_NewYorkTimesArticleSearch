package com.minhnpa.coderschool.newyorktimesarticlesearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MINH NPA on 20 Oct 2016.
 */

public class SearchResult {
    @SerializedName("docs")
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }
}
