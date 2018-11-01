package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.MeetingRoom;
import com.celerstudio.wreelysocial.models.MeetingRoomDashboard;
import com.celerstudio.wreelysocial.models.Member;
import com.celerstudio.wreelysocial.models.RestError;
import com.celerstudio.wreelysocial.models.Vendor;
import com.celerstudio.wreelysocial.network.CallbackWrapper;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.adapter.MeetingRoomAdapter;
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

public class VendorMeetingRoomsActivity extends BaseActivity implements BaseActivity.OptionMenuListener {

    public static final String VENDOR = "vendor";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    Vendor vendor;

    List<MeetingRoom> items = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CompositeSubscription compositeSubscription;
    private MeetingRoomAdapter itemsAdapter;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.paid_booking)
    TextView paidBooking;

    @BindView(R.id.free_remaining)
    TextView freeRemaining;

    @BindView(R.id.out_of)
    TextView outOf;

    @BindView(R.id.total_booking)
    TextView totalBooking;

    @BindView(R.id.dashboard)
    CardView dashboard;

    private MeetingRoomDashboard meetingRoomDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        setContentView(R.layout.activity_vendor_meeting_rooms);
        ButterKnife.bind(this);

        vendor = getIntent().getParcelableExtra(VENDOR);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        itemsAdapter = new MeetingRoomAdapter(items, this);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(32, true));
        itemsAdapter.setOnItemClickListener(new MeetingRoomAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                MeetingRoom member = items.get(position);
                if (!Util.textIsEmpty(member.getName())) {
                    Intent intent = new Intent(VendorMeetingRoomsActivity.this, VendorMeetingRoomActivity.class);
                    intent.putExtra(VendorMeetingRoomActivity.MEETING_ROOM, member);
                    intent.putExtra(VendorMeetingRoomActivity.MEETING_ROOM_DASHBOARD, meetingRoomDashboard);
                    intent.putExtra(VendorMeetingRoomActivity.VENDOR, vendor);
                    startActivity(intent);
                }
            }
        });

        setOptionMenu(R.menu.meeting_room, this);

        initFetching();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setTitle(vendor.getName() + " Meeting Rooms");
    }

    private void fetchData() {
        items = new ArrayList<>();
        setProgressDialog(vendor.getName(), "Fetching meeting rooms");
        compositeSubscription.add(getAPIService().getMeetingRooms(vendor.getId().toString(), getApp().getUser().getAccessToken()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                items = response.body().getMeetingRooms();
                itemsAdapter.addItems(items);
                if (items.size() == 0) {
                    internet.setVisibility(View.VISIBLE);
                    internet.setText("Meeting Rooms not available");
                }
            }

            @Override
            protected void onFailure(String message) {
                internet.setVisibility(View.VISIBLE);
                internet.setText(message);
            }
        }));
        fetchMeetingRoomDashboard();
    }

    private void fetchMeetingRoomDashboard() {
        compositeSubscription.add(getAPIService().meetingRoomDashboard(vendor.getId().toString(), getApp().getUser().getAccessToken()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>() {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                meetingRoomDashboard = response.body().getMeetingRoomDashboard();
                date.setText(meetingRoomDashboard.getMonth());
                freeRemaining.setText(meetingRoomDashboard.getFreeRemaining());
                paidBooking.setText(meetingRoomDashboard.getPaidUsage());
                outOf.setText("Out of " + meetingRoomDashboard.getFreeCredit());
                totalBooking.setText(meetingRoomDashboard.getTotalUsage());
                dashboard.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onFailure(String message) {
                Log.d("onFailure", message);
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

    @Override
    public void onMenuClicked(MenuItem item) {
        Intent intent = new Intent(this, VendorMeetingRoomHistoryActivity.class);
        intent.putExtra(VendorMeetingRoomActivity.VENDOR, vendor);
        startActivity(intent);
    }
}
