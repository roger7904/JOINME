package com.roger.joinme;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;

//    public MyItem(double lat, double lng) {
//        mPosition = new LatLng(lat, lng);
//    }

    public MyItem(double lat, double lng, String mtitle, String msnippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = mtitle;
        mSnippet = msnippet;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Nullable
    @Override
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
