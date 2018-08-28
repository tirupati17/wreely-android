package com.celerstudio.wreelysocial.views.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.assist.DetailSharedElementEnterCallback;
import com.celerstudio.wreelysocial.assist.IntentUtil;
import com.celerstudio.wreelysocial.assist.TransitionCallback;
import com.celerstudio.wreelysocial.databinding.ItemNearbyWorkspaceBinding;
import com.celerstudio.wreelysocial.models.NearbyWorkspace;
import com.celerstudio.wreelysocial.network.CallbackWrapper;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.adapter.NearbyWorkspacesAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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

public class NearbyWorkspacesActivity extends BaseActivity {

    public static final String VENDOR = "vendor";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search)
    EditText search;

    ArrayList<NearbyWorkspace> items = new ArrayList<>();
    private CompositeSubscription compositeSubscription;
    public NearbyWorkspacesAdapter itemsAdapter;

    private final Transition.TransitionListener sharedExitListener =
            new TransitionCallback() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    SharedElementCallback cb = null;
                    setExitSharedElementCallback(cb);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        compositeSubscription = new CompositeSubscription();
        setContentView(R.layout.activity_nearby_workspaces);
        ButterKnife.bind(this);

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
                    UiUtils.showSnackbar(findViewById(android.R.id.content), "Maps won't work with location access", Snackbar.LENGTH_LONG);
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

    private void fetchData() {
        items = new ArrayList<>();
        setProgressDialog("Wreely", "Fetching nearby workspaces");
        compositeSubscription.add(getAPIService().getNearbyWorkspaces(19.032800, 72.896357, 16).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<List<NearbyWorkspace>>>(this) {
            @Override
            protected void onSuccess(Response<List<NearbyWorkspace>> response) {
                items = (ArrayList<NearbyWorkspace>) response.body();
                itemsAdapter.addItems(items);
                if (items.size() == 0) {
                    internet.setText("No results found for this location");
                    internet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));

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
        Pair photoPair = Pair.create(binding.pic, binding.pic.getTransitionName());
        View decorView = getWindow().getDecorView();
        View statusBackground = decorView.findViewById(android.R.id.statusBarBackground);
        View navBackground = decorView.findViewById(android.R.id.navigationBarBackground);
        Pair statusPair = Pair.create(statusBackground,
                statusBackground.getTransitionName());

        final ActivityOptions options;
        if (navBackground == null) {
            options = ActivityOptions.makeSceneTransitionAnimation(this,
                    authorPair, photoPair, statusPair);
        } else {
            Pair navPair = Pair.create(navBackground, navBackground.getTransitionName());
            options = ActivityOptions.makeSceneTransitionAnimation(this,
                    authorPair, statusPair, navPair);
        }
        return options;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (items.size() == 0)
            initFetching();
    }

    private void initFetching() {
        internet.setVisibility(View.GONE);
        if (!ConnectivityReceiver.isConnected(this)) {
            internet.setText(getString(R.string.network_not_available));
            internet.setVisibility(View.VISIBLE);
        } else {
            internet.setVisibility(View.GONE);
            fetchData();
        }
    }

    @OnClick(R.id.internet)
    void onInternetClick(View view) {
        TextView v = (TextView) view;
        if (v.getText().toString().toLowerCase().contains("settings")) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        } else if (v.getText().toString().toLowerCase().contains("try again")) {
            internet.setVisibility(View.GONE);
            fetchData();
        }
    }

    @OnTextChanged(R.id.search)
    void onSearch() {
        internet.setVisibility(View.GONE);
        internet.setText(getString(R.string.network_not_available));
        String searchStr = search.getText().toString();
        List<NearbyWorkspace> searchedItems = new ArrayList<>();
        if (Util.textIsEmpty(searchStr)) {
            itemsAdapter.addItems(items);
        } else {
            for (NearbyWorkspace item : items) {
                if (item.getName().toLowerCase().contains(searchStr.toLowerCase())) {
                    searchedItems.add(item);
                }
            }

            if (searchedItems.size() == 0) {
                internet.setText("No results found for '" + searchStr + "'");
                internet.setVisibility(View.VISIBLE);
            }
            itemsAdapter.addItems(searchedItems);
        }
    }
}
