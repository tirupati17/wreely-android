package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.models.Vendor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.viewModel.SkyAppDataViewModel;
import com.celerstudio.wreelysocial.views.CustomPopoverView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

public class VendorsDashboardActivity extends BaseActivity {

    public static final String VENDOR = "vendor";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        vendor = getIntent().getParcelableExtra(VENDOR);
        toolbar.setTitle(vendor.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick(R.id.companies)
    void onVendors(View view) {
        Intent intent = new Intent(this, VendorCompaniesActivity.class);
        intent.putExtra(VendorCompaniesActivity.VENDOR, vendor);
        startActivity(intent);
    }

    @OnClick(R.id.members)
    void onLibrary(View view) {
        Intent intent = new Intent(this, VendorMembersActivity.class);
        intent.putExtra(VendorMembersActivity.VENDOR, vendor);
        startActivity(intent);
    }

    @OnClick(R.id.events)
    void onMedicalNews(View view) {
//        startActivity(new Intent(this, MedicalNewsActivity.class));
    }

    @OnClick(R.id.workspace)
    void onConnectPeers(View view) {
//        Intent intent = new Intent(this, ChatHomeActivity.class);
//        intent.putExtra("page_name", "Connect To Peers");
//        startActivity(intent);

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("http://www.africau.edu/images/default/sample.pdf"));
//        startActivity(intent);

    }

    @OnClick(R.id.group_chat)
    void onCaseDiscussion(View view) {
        User u = new User();
        u.setAccessToken(vendor.getAccessToken());
        u.setName("Group Chat");
        u.setId(vendor.getFirebaseId());
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("friend", u);
        startActivity(intent);

//        Intent intent = new Intent(this, CaseDiscussionListActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.book_meeting)
    void omMeetingRoom(View view) {
        Intent intent = new Intent(this, VendorMeetingRoomsActivity.class);
        intent.putExtra(VendorMeetingRoomsActivity.VENDOR, vendor);
        startActivity(intent);

//        Intent intent = new Intent(this, CaseDiscussionListActivity.class);
//        startActivity(intent);
    }

//    private void validateSession() {
//        String token = getApp().getUser() != null ? getApp().getUser().getAccessToken() : "";
//        compositeSubscription.add(getAPIService().validateSession(token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Response<BasicResponse>>() {
//            @Override
//            public void call(Response<BasicResponse> response) {
//                if (!response.isSuccessful()) {
//                    sessionExpired();
//                }
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//            }
//        }));
//    }

    public void sessionExpired() {
        CustomPopoverView customPopoverView = CustomPopoverView.builder(this)
                .withPositiveTitle("OK")
                .withTitle("Sky App")
                .setCancelable(false)
                .withMessage("Your session has expired")
                .setDialogButtonClickListener(new CustomPopoverView.DialogButtonClickListener() {
                    @Override
                    public void positiveButtonClicked(View view, AlertDialog alertDialog) {
                        getApp().getPreferences().removeUserSession();
                        startActivity(new Intent(VendorsDashboardActivity.this, SplashActivity.class));
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
}
