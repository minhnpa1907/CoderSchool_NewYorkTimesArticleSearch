package com.minhnpa.coderschool.newyorktimesarticlesearch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MINH NPA on 23 Oct 2016.
 */

public class HeadLine implements Parcelable {
    private String main;

    private HeadLine(Parcel in) {
        main = in.readString();
    }

    public static final Creator<HeadLine> CREATOR = new Creator<HeadLine>() {
        @Override
        public HeadLine createFromParcel(Parcel in) {
            return new HeadLine(in);
        }

        @Override
        public HeadLine[] newArray(int size) {
            return new HeadLine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(main);
    }

    public String getMain() {
        return main;
    }
}
