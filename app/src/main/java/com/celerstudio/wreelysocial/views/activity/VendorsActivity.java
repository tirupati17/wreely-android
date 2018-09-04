package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.celerstudio.wreelysocial.BuildConfig;
import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.Member;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.models.Vendor;
import com.celerstudio.wreelysocial.network.CallbackWrapper;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.views.CustomPopoverView;
import com.celerstudio.wreelysocial.views.adapter.VendorAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class VendorsActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    List<Vendor> items = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.my_workspaces)
    TextView myWorkspaces;
    private CompositeSubscription compositeSubscription;
    private VendorAdapter commonItemAdapter;
    private User user;
    private boolean error = true;

    @BindView(R.id.nav_view)
    NavigationView navView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.vendors_cont)
    LinearLayout vendorsCont;

    @BindView(R.id.app_info)
    TextView appInfo;

    @BindView(R.id.vendor_dashboard)
    ScrollView vendorDashboard;

    private Vendor selectedVendor;
    private CircleImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        setContentView(R.layout.activity_vendors);
        ButterKnife.bind(this);
        user = getApp().getPreferences().getUser();
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
                if (v.getId() == R.id.check) {
                    CheckBox checkBox = (CheckBox) v;
                    setDefualtVendor(item, checkBox.isChecked(), position);
                } else {
                    selectedVendor = item;
                    showSelectedWorkspaceView();
//                    Intent intent = new Intent(VendorsActivity.this, VendorsDashboardActivity.class);
//                    if (item.getName().equalsIgnoreCase("nearby workspaces")) {
//                        intent = new Intent(VendorsActivity.this, NearbyWorkspacesActivity.class);
//                    } else {
//                        intent.putExtra(VendorsDashboardActivity.VENDOR, items.get(position));
//                    }
//                    startActivity(intent);
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationIcon(R.drawable.ic_action_menu);

        View header = navView.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.name);
        TextView email = (TextView) header.findViewById(R.id.email);
        pic = (CircleImageView) header.findViewById(R.id.pic);
        userName.setText(getApp().getUser().getName());
        email.setText(getApp().getUser().getEmail());


        selectedVendor = getApp().getPreferences().getVendor();
        setVendorView();

        String info = "App Info\nVersion Code - " + BuildConfig.VERSION_CODE + ", Version Name - " + BuildConfig.VERSION_NAME;
        appInfo.setText(info);

    }

    private void setVendorView() {
        if (selectedVendor == null) {
            showListOfMyWorkspaceView();
        } else {
            showSelectedWorkspaceView();
        }
    }

    private void showSelectedWorkspaceView() {
        vendorDashboard.setVisibility(View.VISIBLE);
        vendorsCont.setVisibility(View.GONE);
        myWorkspaces.setVisibility(View.VISIBLE);
        toolbar.setTitle(selectedVendor.getName());
        Picasso.with(context).load(selectedVendor.getVendorLogoUrl()).into(pic);
    }

    private void showListOfMyWorkspaceView() {
        vendorDashboard.setVisibility(View.GONE);
        vendorsCont.setVisibility(View.VISIBLE);
        myWorkspaces.setVisibility(View.GONE);
        initFetching();
        toolbar.setTitle("Your Workspaces");
        Picasso.with(context).load("http://api.wreely.com/services").placeholder(R.mipmap.ic_launcher).into(pic);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showExitSnackBar();
        }
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

    public void setDefualtVendor(final Vendor vendor, final boolean checked, final int position) {
        String label = "Are you sure you want to set '" + vendor.getName() + "' as your default workspace?";
        if (!checked) {
            label = "Are you sure you want to remove '" + vendor.getName() + "' as your default workspace?";
        }

        CustomPopoverView customPopoverView = CustomPopoverView.builder(this)
                .withNegativeTitle("Cancel")
                .withPositiveTitle("OK")
                .withTitle("Wreely")
                .withMessage(label)
                .setDialogButtonClickListener(new CustomPopoverView.DialogButtonClickListener() {
                    @Override
                    public void positiveButtonClicked(View view, AlertDialog alertDialog) {
                        if (checked) {
                            getApp().getPreferences().setVendor(vendor);
                        } else {
                            getApp().getPreferences().clearVendor();
                        }
                        selectedVendor = getApp().getPreferences().getVendor();
                        if (checked)
                            setVendorView();
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

    @OnClick(R.id.profile)
    void onProfile() {
        drawer.closeDrawer(Gravity.LEFT);
        User user = getApp().getUser();
        Member member = new Member();
        member.setName(user.getName());
        member.setContactNo(user.getMobile());
        member.setOccupation(user.getOccupation());
        member.setEmailId(user.getEmail());

        Intent intent = new Intent(this, MemberDetailActivity.class);
        intent.putExtra(MemberDetailActivity.MEMBER, member);
        startActivity(intent);
    }

    @OnClick(R.id.nearby_workspaces)
    void onNearbyWorkspaces() {
        drawer.closeDrawer(Gravity.LEFT);
        Intent intent = new Intent(this, NearbyWorkspacesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.my_workspaces)
    void onMyWorkspaces() {
        drawer.closeDrawer(Gravity.LEFT);
        selectedVendor = getApp().getPreferences().getVendor();
        showListOfMyWorkspaceView();
    }

    @OnClick(R.id.logout)
    void onLogout() {
        drawer.closeDrawer(Gravity.LEFT);
        confirmLogout();
    }

    private void getMembervendors(final String accessToken) {
        showDialog(getString(R.string.app_name), "Fetching member vendors");
        compositeSubscription.add(getAPIService().getMemberVendors(accessToken).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                items = new ArrayList<>();
                items.addAll(response.body().getVendors());
                setList();
            }

            @Override
            protected void onFailure(String message) {
                internet.setVisibility(View.VISIBLE);
                internet.setText(message);
            }
        }));
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
            getMembervendors(user.getAccessToken());
        }
    }

    @OnClick(R.id.internet)
    void onInternetClick(View view) {
        TextView v = (TextView) view;
        if (v.getText().toString().toLowerCase().contains("settings")) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        } else {
            internet.setVisibility(View.GONE);
            getMembervendors(user.getAccessToken());
        }
    }

    private void setList() {

        if (items.size() == 0) {
            internet.setVisibility(View.VISIBLE);
            internet.setText(error ? getString(R.string.something_went_wrong) : "You don't have any work spaces");
        } else {
            for (Vendor vendor : items) {
                vendor.setDefaultWorkspace(false);
                if (selectedVendor != null) {
                    if (vendor.getId().equals(selectedVendor.getId())) {
                        vendor.setDefaultWorkspace(true);
                    }
                }
            }
            commonItemAdapter.addItems(items);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    @OnClick(R.id.companies)
    void onVendors(View view) {
        Intent intent = new Intent(this, VendorCompaniesActivity.class);
        intent.putExtra(VendorCompaniesActivity.VENDOR, selectedVendor);
        startActivity(intent);
    }

    @OnClick(R.id.members)
    void onLibrary(View view) {
        Intent intent = new Intent(this, VendorMembersActivity.class);
        intent.putExtra(VendorMembersActivity.VENDOR, selectedVendor);
        startActivity(intent);
    }

    @OnClick(R.id.events)
    void onMedicalNews(View view) {
        Intent intent = new Intent(this, VendorEventsActivity.class);
        intent.putExtra("vendor", selectedVendor);
        startActivity(intent);
    }

    @OnClick(R.id.book_meeting)
    void omMeetingRoom(View view) {
        Intent intent = new Intent(this, VendorMeetingRoomsActivity.class);
        intent.putExtra(VendorMeetingRoomsActivity.VENDOR, selectedVendor);
        startActivity(intent);

//        Intent intent = new Intent(this, CaseDiscussionListActivity.class);
//        startActivity(intent);
    }
}
