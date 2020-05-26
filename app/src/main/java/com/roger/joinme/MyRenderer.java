package com.roger.joinme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;


//用來繼承監聽clusterItem內Marker的InfoWindow
public class MyRenderer extends DefaultClusterRenderer<MyItem> {

    public Bitmap bitmap;

    public MyRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {
//        final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        //抓集合
//        db.collection( "activity" )
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                           @Override
//                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                               if (task.isSuccessful()) {
//                                                   for (QueryDocumentSnapshot document : task.getResult()) {
//                                                       try {
//                                                           URL url = new URL(document.getString("activityPhoto"));
//                                                           System.out.println(document.getString("activityPhoto"));
//                                                           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                                                           connection.setDoInput(true);
//                                                           connection.connect();
//                                                           InputStream input = connection.getInputStream();
//                                                           bitmap = BitmapFactory.decodeStream(input);
////                                            BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.fromResource(bitmap);
//                                                       } catch (MalformedURLException e) {
//                                                           e.printStackTrace();
//                                                       } catch (IOException e) {
//                                                           e.printStackTrace();
//                                                       }
//                                                   }
//                                               }
//                                           }
//                                       });
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }

    @Override
    protected void onClusterItemRendered(MyItem clusterItem,Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        //other stuff......
    }
}
