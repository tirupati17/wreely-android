package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.MeetingRoomSlot;
import com.celerstudio.wreelysocial.models.Member;
import com.celerstudio.wreelysocial.models.RestError;
import com.celerstudio.wreelysocial.models.Vendor;
import com.celerstudio.wreelysocial.network.CallbackWrapper;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.adapter.MeetingRoomSlotHistoryAdapter;
import com.celerstudio.wreelysocial.views.adapter.MemberAdapter;
import com.celerstudio.wreelysocial.views.fragments.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class VendorMeetingRoomHistoryActivity extends BaseActivity {

    public static final String VENDOR = "vendor";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.previous)
    ImageView previous;

    @BindView(R.id.next)
    ImageView next;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.total_count)
    TextView totalCount;

    @BindView(R.id.count_cont)
    LinearLayout countCont;

    Vendor vendor;

    List<MeetingRoomSlot> items = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CompositeSubscription compositeSubscription;
    private MeetingRoomSlotHistoryAdapter itemsAdapter;

    private int dateCounter = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        setContentView(R.layout.activity_meeting_history);
        ButterKnife.bind(this);

        vendor = getIntent().getParcelableExtra(VENDOR);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        itemsAdapter = new MeetingRoomSlotHistoryAdapter(items, this);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(32, true));
        itemsAdapter.setOnItemClickListener(new MeetingRoomSlotHistoryAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }
        });

        countCont.setVisibility(View.GONE);

        setDatecounter(true);

        initFetching();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setTitle(vendor.getName() + " Meeting Room History");
    }

    private void fetchData() {
        items = new ArrayList<>();
        String token = vendor.getAccessToken();
        setProgressDialog("Meeting Rooms", "Fetching meeting rooms history");
        internet.setVisibility(View.GONE);
        compositeSubscription.add(getAPIService().meetingHistory(Long.parseLong(getApp().getUser().getId()), setDate()[0], setDate()[1], token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                items = response.body().getMeetingRoomHistory();
                if (items.size() == 0) {
                    internet.setVisibility(View.VISIBLE);
                    internet.setText("Meeting Rooms not available");
                } else {
                    countCont.setVisibility(View.VISIBLE);
                    totalCount.setText(String.valueOf(items.size()));
                }
                itemsAdapter.addItems(items);
            }

            @Override
            protected void onFailure(String message) {
                //UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
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
        } else if (v.getText().toString().toLowerCase().contains("try again")) {
            internet.setVisibility(View.GONE);
            fetchData();
        }
    }


    private void setDatecounter(boolean increment) {
        if (increment)
            dateCounter += 1;
        else
            dateCounter -= 1;
//        if (dateCounter <= 0) {
//            dateCounter = 0;
//            previous.setEnabled(false);
//            previous.setAlpha(0.2f);
//            next.setEnabled(true);
//            next.setAlpha(1f);
//        } else {
//            previous.setEnabled(true);
//            previous.setAlpha(1f);
//            next.setEnabled(true);
//            next.setAlpha(1f);
//        }
        name.setText(setDate()[2]);
    }

    private String[] setDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, dateCounter);

        String startDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-01";
        String endDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        Date date = new Date(cal.getTimeInMillis());
        String displayDate = new SimpleDateFormat("MMM").format(date) + ", " + cal.get(Calendar.YEAR);

        return new String[]{startDate, endDate, displayDate};
    }

    @OnClick(R.id.previous)
    void onPrevious() {
        setDatecounter(false);
        fetchData();
    }

    @OnClick(R.id.next)
    void onNext() {
        setDatecounter(true);
        fetchData();
    }

    @OnClick(R.id.name)
    void onName() {
        DatePickerFragment.newInstance(new DatePickerFragment.DatePickerFragmentListener() {
            @Override
            public void onDateSet(String displayDate, Date date) {
                String formattedDate = new SimpleDateFormat("MMM").format(date) + ", " + date.getYear();
//                String formattedDate = new SimpleDateFormat("EEE, MMM d, ''yyyy").format(date);
                name.setText(formattedDate);
            }
        }).show(getSupportFragmentManager(), "Select Date");
    }
}
