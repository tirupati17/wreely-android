package com.celerstudio.wreelysocial.views.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.assist.CustomClusterRenderer;
import com.celerstudio.wreelysocial.assist.CustomInfoWindowAdapter;
import com.celerstudio.wreelysocial.assist.DetailSharedElementEnterCallback;
import com.celerstudio.wreelysocial.assist.IntentUtil;
import com.celerstudio.wreelysocial.assist.TransitionCallback;
import com.celerstudio.wreelysocial.databinding.ItemNearbyWorkspaceBinding;
import com.celerstudio.wreelysocial.models.MapClusteItem;
import com.celerstudio.wreelysocial.models.Member;
import com.celerstudio.wreelysocial.models.NearbyWorkspace;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.network.CallbackWrapper;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.CustomPopoverView;
import com.celerstudio.wreelysocial.views.adapter.NearbyWorkspacesAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.ClusterManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class NearbyWorkspacesActivity extends BaseActivity implements OnMapReadyCallback {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private GoogleMap mMap;
    ArrayList<NearbyWorkspace> items = new ArrayList<>();
    private CompositeSubscription compositeSubscription;
    public NearbyWorkspacesAdapter itemsAdapter;

    private FusedLocationProviderClient mFusedLocationClient;

    private final Transition.TransitionListener sharedExitListener =
            new TransitionCallback() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    SharedElementCallback cb = null;
                    setExitSharedElementCallback(cb);
                }
            };

    SupportMapFragment mapFragment;

    private ClusterManager<MapClusteItem> mClusterManager;
    private MapClusteItem clickedClusterItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        compositeSubscription = new CompositeSubscription();
        setContentView(R.layout.activity_nearby_workspaces1);
        ButterKnife.bind(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        getWindow().getSharedElementExitTransition().addListener(sharedExitListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        itemsAdapter = new NearbyWorkspacesAdapter(items, this);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(32, true));
        itemsAdapter.setOnItemClickListener(new NearbyWorkspacesAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (v.getId() == R.id.selector) {

                    if (items.size() > 0) {
                        NearbyWorkspace nb = items.get(position);
                        if (nb != null && nb.getId().equalsIgnoreCase("-420"))
                            return;
                        else if (nb != null && nb.getId().equalsIgnoreCase("-421")) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                            return;
                        }

                    }

                    NearbyWorkspacesAdapter.DataObjectHolder holder = (NearbyWorkspacesAdapter.DataObjectHolder) recyclerView.findViewHolderForAdapterPosition(position);
                    if (holder == null) {
                        return;
                    }
                    ItemNearbyWorkspaceBinding binding = holder.getBinding();
                    Intent intent = getDetailActivityStartIntent(NearbyWorkspacesActivity.this,
                            items, position, binding);
                    ActivityOptions activityOptions = getActivityOptions(binding);
                    startActivity(intent, activityOptions.toBundle());
                } else {
                    locationPermission(items.get(position));
                }
            }
        });

        if (savedInstanceState != null) {
            items = savedInstanceState.getParcelableArrayList(IntentUtil.RELEVANT_ITEMS);
            itemsAdapter.addItems(items);
            if (items.size() == 0) {
                internet.setText("No results found for this location");
                internet.setVisibility(View.VISIBLE);
            }
        } else {
            initFetching();
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Nearby Workspaces");
    }

    private void locationPermission(final NearbyWorkspace nearbyWorkspace) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    Intent i = new Intent(NearbyWorkspacesActivity.this, WorkSpaceLocationActivity.class);
                    i.putExtra(WorkSpaceLocationActivity.NEARBY_WORKSPACE, nearbyWorkspace);
                    startActivity(i);
                } else if (report.isAnyPermissionPermanentlyDenied()) {
                    UiUtils.showSnackbar(findViewById(android.R.id.content), "Maps won't work with out location access", Snackbar.LENGTH_LONG);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(IntentUtil.RELEVANT_ITEMS, items);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        postponeEnterTransition();
        // Start the postponed transition when the recycler view is ready to be drawn.
        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });

        if (data == null) {
            return;
        }

        final int selectedItem = data.getIntExtra(IntentUtil.SELECTED_ITEM_POSITION, 0);
        recyclerView.scrollToPosition(selectedItem);

        NearbyWorkspacesAdapter.DataObjectHolder holder = (NearbyWorkspacesAdapter.DataObjectHolder) recyclerView.
                findViewHolderForAdapterPosition(selectedItem);
        if (holder == null) {
            Log.w("NearbyWorkspace", "onActivityReenter: Holder is null, remapping cancelled.");
            return;
        }

        DetailSharedElementEnterCallback callback =
                new DetailSharedElementEnterCallback(getIntent());
        callback.setBinding(holder.getBinding());
        setExitSharedElementCallback(callback);
    }

    private void fetchDataWithCurrenLocationPermission() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {

                    if (ActivityCompat.checkSelfPermission(NearbyWorkspacesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearbyWorkspacesActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(NearbyWorkspacesActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(latLng);
                                        markerOptions.draggable(true);
                                        mMap.addMarker(markerOptions);
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
                                        fetchData(location.getLatitude(), location.getLongitude());
                                    } else {
                                        UiUtils.showSnackbar(findViewById(android.R.id.content), "Location Not found", Snackbar.LENGTH_LONG);
                                    }
                                }
                            });
                } else if (report.isAnyPermissionPermanentlyDenied()) {
                    UiUtils.showSnackbar(findViewById(android.R.id.content), "Maps won't work with out location access", Snackbar.LENGTH_LONG);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void fetchData(double lat, double lng) {
        items = new ArrayList<>();
        setProgressDialog("Wreely", "Fetching nearby workspaces");
//        compositeSubscription.add(getAPIService().getNearbyWorkspaces(19.032800, 72.896357, 10).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<List<NearbyWorkspace>>>() {
        compositeSubscription.add(getAPIService().getNearbyWorkspaces(lat, lng, 10).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<List<NearbyWorkspace>>>() {
            @Override
            protected void onSuccess(Response<List<NearbyWorkspace>> response) {
                dismissDialog();
                items = (ArrayList<NearbyWorkspace>) response.body();
                if (items.size() == 0) {
                    items.add(nearbyWorkspace(false));
                } else {
                    for (int i = 0; i < items.size(); i++) {
                        plotMarkers(items.get(i));
                    }
                }
                itemsAdapter.addItems(items);
            }

            @Override
            protected void onFailure(String message) {
                dismissDialog();
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));
    }

    private void plotMarkers(NearbyWorkspace nearbyWorkspace) {
        MapClusteItem item = new MapClusteItem(new LatLng(Double.valueOf(nearbyWorkspace.getLatitude()), Double.valueOf(nearbyWorkspace.getLongitude())), nearbyWorkspace.getName(), nearbyWorkspace.getAddress());
        item.setNearbyWorkspace(nearbyWorkspace);
        mClusterManager.addItem(item);
    }

    @NonNull
    private static Intent getDetailActivityStartIntent(Activity host, ArrayList<NearbyWorkspace> photos,
                                                       int position, ItemNearbyWorkspaceBinding binding) {
        final Intent intent = new Intent(host, NearbyWorkspaceViewPagerActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putParcelableArrayListExtra(IntentUtil.PHOTO, photos);
        intent.putExtra(IntentUtil.SELECTED_ITEM_POSITION, position);
        intent.putExtra(IntentUtil.PADDING,
                new Rect(16,
                        16,
                        16,
                        16));
        return intent;
    }

    private ActivityOptions getActivityOptions(ItemNearbyWorkspaceBinding binding) {
        Pair authorPair = Pair.create(binding.title, binding.title.getTransitionName());
        Pair cityPair = Pair.create(binding.city, binding.city.getTransitionName());
        Pair photoPair = Pair.create(binding.pic, binding.pic.getTransitionName());
        View decorView = getWindow().getDecorView();
        View statusBackground = decorView.findViewById(android.R.id.statusBarBackground);
        View navBackground = decorView.findViewById(android.R.id.navigationBarBackground);
        Pair statusPair = Pair.create(statusBackground,
                statusBackground.getTransitionName());

        final ActivityOptions options;
        if (navBackground == null) {
            options = ActivityOptions.makeSceneTransitionAnimation(this,
                    cityPair, authorPair, photoPair, statusPair);
        } else {
            Pair navPair = Pair.create(navBackground, navBackground.getTransitionName());
            options = ActivityOptions.makeSceneTransitionAnimation(this,
                    cityPair, authorPair, statusPair, navPair);
        }
        return options;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (items.size() == 0 || (items.size() == 1 && (items.get(0).getId().equalsIgnoreCase("-420") || items.get(0).getId().equalsIgnoreCase("-421"))))
            initFetching();
    }

    private void initFetching() {
        internet.setVisibility(View.GONE);
        if (!ConnectivityReceiver.isConnected(this)) {
            items.add(nearbyWorkspace(true));
            itemsAdapter.addItems(items);
        } else {
            internet.setVisibility(View.GONE);
            mapFragment.getMapAsync(this);
        }
    }

    private NearbyWorkspace nearbyWorkspace(boolean internet) {
        NearbyWorkspace nbw = new NearbyWorkspace();
        nbw.setId(internet ? "-421" : "-420");
        nbw.setCity(internet ? "Internet Not Available" : "No Co-Working space found");
        nbw.setAddress(internet ? "Please check your network settings" : "Change location to search for nearby co-working spaces");
        nbw.setPostalArea("");
        return nbw;
    }

    @OnClick(R.id.internet)
    void onInternetClick(View view) {
        TextView v = (TextView) view;
        if (v.getText().toString().toLowerCase().contains("settings")) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        } else if (v.getText().toString().toLowerCase().contains("try again")) {
            internet.setVisibility(View.GONE);
            fetchDataWithCurrenLocationPermission();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mClusterManager = new ClusterManager<MapClusteItem>(this, mMap);
        final CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setAnimation(true);
        mClusterManager.setRenderer(renderer);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
//        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new CustomInfoWindowAdapter(this));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
//        mMap.setMyLocationEnabled(false);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d("Dragged Pos", marker.getPosition().latitude + " : " + marker.getPosition().longitude);
                fetchData(marker.getPosition().latitude, marker.getPosition().longitude);
            }
        });

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MapClusteItem>() {
            @Override
            public boolean onClusterItemClick(MapClusteItem item) {
                clickedClusterItem = item;
                return false;
            }
        });


        try {
            Boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.googlemap_style_json));
            if (!success) {
                Log.d("GoogleMapStyle", "Style parsing failed.");
            }
        } catch (Exception e) {
            Log.e("GoogleMapStyle", "Can't find style. Error: ", e);
        }
        fetchDataWithCurrenLocationPermission();
    }

//    @OnTextChanged(R.id.search)
//    void onSearch() {
//        internet.setVisibility(View.GONE);
//        internet.setText(getString(R.string.network_not_available));
//        String searchStr = search.getText().toString();
//        List<NearbyWorkspace> searchedItems = new ArrayList<>();
//        if (Util.textIsEmpty(searchStr)) {
//            itemsAdapter.addItems(items);
//        } else {
//            for (NearbyWorkspace item : items) {
//                if (item.getName().toLowerCase().contains(searchStr.toLowerCase())) {
//                    searchedItems.add(item);
//                }
//            }
//
//            if (searchedItems.size() == 0) {
//                internet.setText("No results found for '" + searchStr + "'");
//                internet.setVisibility(View.VISIBLE);
//            }
//            itemsAdapter.addItems(searchedItems);
//        }
//    }

    public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".

        private Context context;

        public CustomInfoWindowAdapter(Context ctx) {
            context = ctx;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            NearbyWorkspace item = clickedClusterItem.getNearbyWorkspace();
            View view = ((Activity) context).getLayoutInflater()
                    .inflate(R.layout.item_nearby_workspace1, null);

            TextView monthPrice = view.findViewById(R.id.month_price);
            TextView title = view.findViewById(R.id.title);
            TextView city = view.findViewById(R.id.city);
            TextView address = view.findViewById(R.id.address);
            ImageView pic = view.findViewById(R.id.pic);
            ImageView direction = view.findViewById(R.id.direction);

            Picasso.with(context).load(item.getImage()).into(pic);
            float mp = Util.textIsEmpty(item.getMonthPrice()) ? 0 : Float.parseFloat(item.getMonthPrice());
            if (mp <= 0) {
                monthPrice.setVisibility(View.GONE);
            } else {
                monthPrice.setText(item.getCurrency() + " " + item.getMonthPrice() + "/Month");
            }

            title.setText(item.getName());
            city.setText(item.getCity());
            address.setText(item.getAddress() + " " + item.getPostalArea());

            return view;
        }


    }


}
