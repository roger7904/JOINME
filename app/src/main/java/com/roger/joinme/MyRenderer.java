package com.roger.joinme;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MyRenderer extends DefaultClusterRenderer<MyItem> {

    public MyRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {

    }

    @Override
    protected void onClusterItemRendered(MyItem clusterItem,Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        //other stuff......
    }
}
