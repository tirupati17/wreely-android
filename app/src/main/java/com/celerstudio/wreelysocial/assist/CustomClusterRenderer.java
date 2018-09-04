package com.celerstudio.wreelysocial.assist;

import android.content.Context;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.MapClusteItem;
import com.celerstudio.wreelysocial.views.activity.NearbyWorkspacesActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer<MapClusteItem> {

  private final Context mContext;

  public CustomClusterRenderer(Context context, GoogleMap map,
      ClusterManager<MapClusteItem> clusterManager) {
    super(context, map, clusterManager);

    mContext = context;
  }

  @Override protected void onBeforeClusterItemRendered(MapClusteItem item,
      MarkerOptions markerOptions) {
    final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker);
    markerOptions.icon(markerDescriptor);
  }
}