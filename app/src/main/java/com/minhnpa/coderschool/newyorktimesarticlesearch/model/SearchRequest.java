package com.minhnpa.coderschool.newyorktimesarticlesearch.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MINH NPA on 20 Oct 2016.
 */

public class SearchRequest implements Parcelable {
    private String query;
    private String beginDate;
    private String sort;
    private int page;
    private boolean filterArts;
    private boolean filterFashionStyle;
    private boolean filterSports;

    public SearchRequest() {
    }

    protected SearchRequest(Parcel in) {
        query = in.readString();
        beginDate = in.readString();
        sort = in.readString();
        page = in.readInt();
        filterArts = in.readByte() != 0;
        filterFashionStyle = in.readByte() != 0;
        filterSports = in.readByte() != 0;
    }

    public static final Creator<SearchRequest> CREATOR = new Creator<SearchRequest>() {
        @Override
        public SearchRequest createFromParcel(Parcel in) {
            return new SearchRequest(in);
        }

        @Override
        public SearchRequest[] newArray(int size) {
            return new SearchRequest[size];
        }
    };

    public Map<String, String> toQueryMap() {
        Map<String, String> options = new HashMap<>();
        if (null != query) options.put("query", query);
        if (null != beginDate) options.put("beginDate", beginDate);
        if (null != sort) options.put("sort", sort);
        if (null != getNewDesk()) options.put("fq", "news_desk:(" + getNewDesk() + ")");
        options.put("page", String.valueOf(page));

        return options;
    }

    private String getNewDesk() {
        if (!filterArts && !filterFashionStyle && !filterSports)
            return null;
        else {
            String fq = "";
            if (filterArts) fq += "\"Arts\" ";
            if (filterFashionStyle) fq += "\"Fashion & Style\" ";
            if (filterSports) fq += "\"Sports\"";
            return fq.trim();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(query);
        dest.writeString(beginDate);
        dest.writeString(sort);
        dest.writeInt(page);
        dest.writeByte((byte) (filterArts ? 1 : 0));
        dest.writeByte((byte) (filterFashionStyle ? 1 : 0));
        dest.writeByte((byte) (filterSports ? 1 : 0));
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isFilterArts() {
        return filterArts;
    }

    public void setFilterArts(boolean filterArts) {
        this.filterArts = filterArts;
    }

    public boolean isFilterFashionStyle() {
        return filterFashionStyle;
    }

    public void setFilterFashionStyle(boolean filterFashionStyle) {
        this.filterFashionStyle = filterFashionStyle;
    }

    public boolean isFilterSports() {
        return filterSports;
    }

    public void setFilterSports(boolean filterSports) {
        this.filterSports = filterSports;
    }
}
