package com.celerstudio.wreelysocial.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapClusteItem implements ClusterItem {
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private NearbyWorkspace nearbyWorkspace;

    public MapClusteItem(LatLng latLng, String title, String snippet) {
        mPosition = latLng;
        mTitle = title;
        mSnippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public NearbyWorkspace getNearbyWorkspace() {
        return nearbyWorkspace;
    }

    public void setNearbyWorkspace(NearbyWorkspace nearbyWorkspace) {
        this.nearbyWorkspace = nearbyWorkspace;
    }
}