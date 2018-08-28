package com.celerstudio.wreelysocial.views.pager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.assist.DetailSharedElementEnterCallback;
import com.celerstudio.wreelysocial.databinding.NearbyWorkspaceDetailBinding;
import com.celerstudio.wreelysocial.models.NearbyWorkspace;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.activity.NearbyWorkspaceViewPagerActivity;
import com.celerstudio.wreelysocial.views.activity.NearbyWorkspacesActivity;
import com.celerstudio.wreelysocial.views.activity.WorkSpaceLocationActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for paging detail views.
 */

public class NearbyWorkspaceDetailViewPagerAdapter extends PagerAdapter {

    private final List<NearbyWorkspace> allPhotos;
    private final LayoutInflater layoutInflater;
    private final int photoWidth;
    private final Activity host;
    private Bundle savedInstanceState;
    private DetailSharedElementEnterCallback sharedElementCallback;

    public NearbyWorkspaceDetailViewPagerAdapter(@NonNull Activity activity, @NonNull List<NearbyWorkspace> photos,
                                                 @NonNull DetailSharedElementEnterCallback callback, Bundle savedInstanceState) {
        layoutInflater = LayoutInflater.from(activity);
        allPhotos = photos;
        photoWidth = activity.getResources().getDisplayMetrics().widthPixels;
        host = activity;
        sharedElementCallback = callback;
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    public int getCount() {
        return allPhotos.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        NearbyWorkspaceDetailBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.nearby_workspace_detail, container, false);
        binding.setInfo(allPhotos.get(position));

        onViewBound(binding);
        binding.executePendingBindings();
        container.addView(binding.getRoot());
        return binding;
    }

    private void onViewBound(NearbyWorkspaceDetailBinding binding) {
        NearbyWorkspace item = binding.getInfo();
        Picasso.with(host).load(item.getImage()).into(binding.pic);
        if (!Util.textIsEmpty(item.getStarRating()) && !item.getStarRating().equalsIgnoreCase("null")) {
            binding.ratingBar.setRating(Float.parseFloat(item.getStarRating()));
        }
        locationPermission(binding, item);
        binding.locationDisabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationPermission(binding, item);
            }
        });

        float dp = Util.textIsEmpty(item.getDayPrice()) ? 0 : Float.parseFloat(item.getDayPrice());
        float wp = Util.textIsEmpty(item.getWeekPrice()) ? 0 : Float.parseFloat(item.getWeekPrice());
        float mp = Util.textIsEmpty(item.getMonthPrice()) ? 0 : Float.parseFloat(item.getMonthPrice());
        if (dp <= 0)
            binding.dayPrice.setText("N/A");
        else
            binding.dayPrice.setText(item.getCurrency() + " " + Math.round(dp));

        if (wp <= 0) {
            binding.weekPrice.setText("N/A");
        } else
            binding.weekPrice.setText(item.getCurrency() + " " + Math.round(wp));

        if (mp <= 0) {
            binding.monthPrice.setText("N/A");
        } else
            binding.monthPrice.setText(item.getCurrency() + " " + Math.round(mp));

    }

    private void locationPermission(NearbyWorkspaceDetailBinding binding, NearbyWorkspace item) {
        Dexter.withActivity(host).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    binding.map.setVisibility(View.VISIBLE);
                    binding.locationDisabled.setVisibility(View.GONE);
                    binding.map.onCreate(savedInstanceState);
                    binding.map.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            UiSettings mUiSettings = googleMap.getUiSettings();
                            // Keep the UI Settings state in sync with the checkboxes.
                            mUiSettings.setZoomControlsEnabled(true);
                            mUiSettings.setCompassEnabled(true);
                            mUiSettings.setMyLocationButtonEnabled(true);
                            mUiSettings.setScrollGesturesEnabled(true);
                            mUiSettings.setZoomGesturesEnabled(true);
                            mUiSettings.setTiltGesturesEnabled(true);
                            mUiSettings.setRotateGesturesEnabled(true);

                            if (ActivityCompat.checkSelfPermission(host, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(host, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            googleMap.setMyLocationEnabled(true);
                            LatLng latLng = new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));
                            googleMap.addMarker(new MarkerOptions().position(latLng).title(item.getName()));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latLng).zoom(15).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            binding.map.onResume();
                        }
                    });
                } else {
                    binding.locationDisabled.setVisibility(View.VISIBLE);
                    binding.map.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (object instanceof NearbyWorkspaceDetailBinding) {
            sharedElementCallback.setBinding((NearbyWorkspaceDetailBinding) object);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object instanceof NearbyWorkspaceDetailBinding
                && view.equals(((NearbyWorkspaceDetailBinding) object).getRoot());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((NearbyWorkspaceDetailBinding) object).getRoot());
    }
}
