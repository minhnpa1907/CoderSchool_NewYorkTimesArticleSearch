package com.minhnpa.coderschool.newyorktimesarticlesearch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.minhnpa.coderschool.newyorktimesarticlesearch.utils.Constant;

public class Media implements Parcelable {
    private int width;
    private String url;
    private int height;
    private String type;

    protected Media(Parcel in) {
        width = in.readInt();
        url = in.readString();
        height = in.readInt();
        type = in.readString();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public int getWidth() {
        return width;
    }

    public String getUrl() {
        return Constant.BASE_URL + url;
    }

    public int getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeString(url);
        dest.writeInt(height);
        dest.writeString(type);
    }
}
