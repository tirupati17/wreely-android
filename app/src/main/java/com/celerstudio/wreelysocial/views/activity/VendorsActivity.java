package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.models.Member;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.models.Vendor;
import com.celerstudio.wreelysocial.views.CustomPopoverView;
import com.celerstudio.wreelysocial.views.adapter.VendorAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

public class VendorsActivity extends BaseActivity implements BaseActivity.OptionMenuListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    List<Vendor> items = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CompositeSubscription compositeSubscription;
    private VendorAdapter commonItemAdapter;
    private User user;
    private boolean error = true;
    private DatabaseReference myRef;
    private Query getDataQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        setContentView(R.layout.activity_vendors);
        ButterKnife.bind(this);
        user = getApp().getPreferences().getUser();
        myRef = getApp().getFirebaseDBRef("vendors");
//        getMixpanelEvents(null).journalList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        commonItemAdapter = new VendorAdapter(items, this);
        recyclerView.setAdapter(commonItemAdapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(32, true));
        commonItemAdapter.setOnItemClickListener(new VendorAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Vendor item = items.get(position);
                Intent intent = new Intent(VendorsActivity.this, VendorsDashboardActivity.class);
                if (item.getName().equalsIgnoreCase("nearby workspaces")) {
                    intent = new Intent(VendorsActivity.this, NearbyWorkspacesActivity.class);
                } else {
                    intent.putExtra(VendorsDashboardActivity.VENDOR, items.get(position));
                }

                startActivity(intent);
            }
        });


        initFetching();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.setTitle("Your Workspaces");

        setOptionMenu(R.menu.main, this);

        MenuItem item = toolbar.getMenu().findItem(R.id.action_not);
//        boolean enabled = notifications != null && notifications.size() > 0;
        TextView count = (TextView) item.getActionView().findViewById(R.id.badge);
//        count.setText(enabled ? String.valueOf(notifications.size()) : "0");
        count.setVisibility(View.GONE);


    }

    @Override
    public void onMenuClicked(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {

            Member member = new Member();
            member.setName(user.getName());
            member.setContactNo(user.getMobile());
            member.setOccupation(user.getOccupation());
            member.setEmailId(user.getEmail());

            Intent intent = new Intent(this, MemberDetailActivity.class);
            intent.putExtra(MemberDetailActivity.MEMBER, member);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_logout) {
            confirmLogout();
        }
    }

    public void confirmLogout() {
        CustomPopoverView customPopoverView = CustomPopoverView.builder(this)
                .withNegativeTitle("Cancel")
                .withPositiveTitle("OK")
                .withTitle("Confirm Logout")
                .withMessage("Are you sure you want to logout?")
                .setDialogButtonClickListener(new CustomPopoverView.DialogButtonClickListener() {
                    @Override
                    public void positiveButtonClicked(View view, AlertDialog alertDialog) {
                        getApp().getPreferences().removeUserSession();
                        startActivity(new Intent(VendorsActivity.this, SplashActivity.class));
                        finish();
                    }

                    @Override
                    public void negativeButtonClicked(View view, AlertDialog alertDialog) {

                    }

                    @Override
                    public void neutralButtonClicked(View view, AlertDialog alertDialog) {

                    }
                })
                .build();

        customPopoverView.show();
    }

    private void fetchData() {
        showDialog(getString(R.string.app_name), "Wait while we fetch your work spaces");
        if (user.getVendors() != null && user.getVendors().size() > 0) {
            for (String vendorId : user.getVendors()) {
                myRef.child(vendorId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dismissDialog();
                        if (dataSnapshot.getValue() != null) {
                            error = false;
                            Vendor vendor = dataSnapshot.getValue(Vendor.class);
                            HashMap values = (HashMap) dataSnapshot.getValue();
                            vendor.setFirebaseId(vendorId);
                            try {
                                vendor.setAccessToken((String) values.get("access_token"));
                                vendor.setDomainNameReference((String) values.get("domain_name_reference"));
                                vendor.setVendorLogoUrl((String) values.get("vendor_logo_url"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            items.add(vendor);
                            setList();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dismissDialog();
                        databaseError.toException().printStackTrace();
                    }
                });
            }
        }
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

    private void setList() {
        if (items.size() == 0) {
            internet.setVisibility(View.VISIBLE);
            internet.setText(error ? getString(R.string.something_went_wrong) : "You don't have any work spaces");
        } else {
            commonItemAdapter.addItems(items);
        }
    }

    @Override
    public void onBackPressed() {
        showExitSnackBar();
    }

    private void showExitSnackBar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), "Press 'Exit' to close Wreely!", Snackbar.LENGTH_LONG)
                .setAction("Exit", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

        snackbar.show();
    }
}
