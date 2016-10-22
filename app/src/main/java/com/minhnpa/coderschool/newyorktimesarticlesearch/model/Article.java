package com.minhnpa.coderschool.newyorktimesarticlesearch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Article implements Parcelable {
    @SerializedName("web_url")
    private String webUrl;

    @SerializedName("snippet")
    private String snippet;

    @SerializedName("multimedia")
    private List<Media> multimedia;

    private Article(Parcel in) {
        webUrl = in.readString();
        snippet = in.readString();
        multimedia = in.createTypedArrayList(Media.CREATOR);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webUrl);
        dest.writeString(snippet);
        dest.writeTypedList(multimedia);
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public List<Media> getMultimedia() {
        return multimedia;
    }
}
