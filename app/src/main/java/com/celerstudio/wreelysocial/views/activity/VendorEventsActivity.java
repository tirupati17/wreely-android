package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.Event;
import com.celerstudio.wreelysocial.models.Member;
import com.celerstudio.wreelysocial.models.RestError;
import com.celerstudio.wreelysocial.models.Vendor;
import com.celerstudio.wreelysocial.network.CallbackWrapper;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.adapter.EventAdapter;
import com.celerstudio.wreelysocial.views.adapter.MemberAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class VendorEventsActivity extends BaseActivity {

    public static final String VENDOR = "vendor";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search)
    EditText search;

    Vendor vendor;
    List<Event> items = new ArrayList<>();
    private CompositeSubscription compositeSubscription;
    private EventAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);

        vendor = getIntent().getParcelableExtra(VENDOR);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        itemsAdapter = new EventAdapter(items, this);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(32, true));
        itemsAdapter.setOnItemClickListener(new EventAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Event event = items.get(position);
//                if (!Util.textIsEmpty(member.getName())) {
                Intent intent = new Intent(VendorEventsActivity.this, EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.EVENT, event);
                startActivity(intent);
//                }

            }
        });


        initFetching();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setTitle(vendor.getName() + " Events");
    }

    private void fetchData() {
        items = new ArrayList<>();
        setProgressDialog(vendor.getName(), "Fetching members");
        compositeSubscription.add(getAPIService().getVendorEvents(vendor.getId(), 1, 1, getApp().getUser().getAccessToken()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                items = response.body().getEvents();
                itemsAdapter.addItems(items);
                if (items.size() == 0) {
                    internet.setVisibility(View.VISIBLE);
                    internet.setText("Events not available");
                }
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
            internet.setVisibility(View.GONE);
            fetchData();
        }
    }

    @OnClick(R.id.internet)
    void onInternetClick(View view) {
        TextView v = (TextView) view;
        if (v.getText().toString().toLowerCase().contains("settings")) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        } else {
            internet.setVisibility(View.GONE);
            fetchData();
        }
    }

    @OnTextChanged(R.id.search)
    void onSearch() {
        internet.setVisibility(View.GONE);
        internet.setText(getString(R.string.network_not_available));
        String searchStr = search.getText().toString();
        List<Event> searchedMembers = new ArrayList<>();
        if (Util.textIsEmpty(searchStr)) {
            itemsAdapter.addItems(items);
        } else {
            for (Event event : items) {
                if (event.getTitle().toLowerCase().contains(searchStr.toLowerCase())) {
                    searchedMembers.add(event);
                }
            }

            if (searchedMembers.size() == 0) {
                internet.setText("No results found for '" + searchStr + "'");
                internet.setVisibility(View.VISIBLE);
            }
            itemsAdapter.addItems(searchedMembers);
        }
    }
}
