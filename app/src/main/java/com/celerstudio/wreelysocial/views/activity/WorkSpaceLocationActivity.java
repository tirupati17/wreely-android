package com.celerstudio.wreelysocial.views.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.annotation.Keep;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.NearbyWorkspace;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WorkSpaceLocationActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static String NEARBY_WORKSPACE = "nearby_workspace";
    public NearbyWorkspace nearbyWorkspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_space_location);

        if (getIntent().getExtras() != null && getIntent().hasExtra(NEARBY_WORKSPACE)) {
            nearbyWorkspace = getIntent().getParcelableExtra(NEARBY_WORKSPACE);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setProgressDialog("Wreely", "Fetching " + nearbyWorkspace.getName() + " location");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        Observable.timer(2, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {
                dismissDialog();
                UiSettings mUiSettings = mMap.getUiSettings();
                // Keep the UI Settings state in sync with the checkboxes.
                mUiSettings.setZoomControlsEnabled(true);
                mUiSettings.setCompassEnabled(true);
                mUiSettings.setMyLocationButtonEnabled(true);
                mUiSettings.setScrollGesturesEnabled(true);
                mUiSettings.setZoomGesturesEnabled(true);
                mUiSettings.setTiltGesturesEnabled(true);
                mUiSettings.setRotateGesturesEnabled(true);

                if (ActivityCompat.checkSelfPermission(WorkSpaceLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WorkSpaceLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                LatLng latLng = new LatLng(Double.parseDouble(nearbyWorkspace.getLatitude()), Double.parseDouble(nearbyWorkspace.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(latLng).title(nearbyWorkspace.getName()));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(15).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {

            }
        });
    }
}
