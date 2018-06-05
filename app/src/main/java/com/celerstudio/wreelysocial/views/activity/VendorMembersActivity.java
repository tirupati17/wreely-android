package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.Company;
import com.celerstudio.wreelysocial.models.Member;
import com.celerstudio.wreelysocial.models.RestError;
import com.celerstudio.wreelysocial.models.Vendor;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.adapter.CompanyAdapter;
import com.celerstudio.wreelysocial.views.adapter.MemberAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class VendorMembersActivity extends BaseActivity {

    public static final String VENDOR = "vendor";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    Vendor vendor;

    List<Member> items = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CompositeSubscription compositeSubscription;
    private MemberAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        setContentView(R.layout.activity_vendor_members);
        ButterKnife.bind(this);

        vendor = getIntent().getParcelableExtra(VENDOR);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        itemsAdapter = new MemberAdapter(items, this);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(32, true));
        itemsAdapter.setOnItemClickListener(new MemberAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Member member = items.get(position);
                if (!Util.textIsEmpty(member.getName())) {
                    Intent intent = new Intent(VendorMembersActivity.this, MemberDetailActivity.class);
                    intent.putExtra(MemberDetailActivity.MEMBER, member);
                    startActivity(intent);
                }

            }
        });


        initFetching();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setTitle(vendor.getName() + " Members");
    }

    private void fetchData() {
        items = new ArrayList<>();
        String token = vendor.getAccessToken();
        setProgressDialog(vendor.getName(), "Fetching members");
        compositeSubscription.add(getAPIService().getVendorMembers(token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Response<BasicResponse>>() {
            @Override
            public void call(Response<BasicResponse> response) {
                dismissDialog();
                if (response.isSuccessful()) {
                    items = response.body().getMembers();
                    itemsAdapter.addItems(items);
                } else {
                    RestError restError = Util.handleError(response.errorBody());
                    UiUtils.showSnackbar(findViewById(android.R.id.content), restError.getMessage(), Snackbar.LENGTH_LONG);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                dismissDialog();
                internet.setVisibility(View.VISIBLE);
                internet.setText(getString(R.string.something_went_wrong));
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
}
