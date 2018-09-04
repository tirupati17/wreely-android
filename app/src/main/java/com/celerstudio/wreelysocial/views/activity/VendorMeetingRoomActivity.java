package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.Day;
import com.celerstudio.wreelysocial.models.MeetingRoom;
import com.celerstudio.wreelysocial.models.MeetingRoomSlot;
import com.celerstudio.wreelysocial.models.RestError;
import com.celerstudio.wreelysocial.models.Vendor;
import com.celerstudio.wreelysocial.network.CallbackWrapper;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.CustomPopoverView;
import com.celerstudio.wreelysocial.views.adapter.DayAdapter;
import com.celerstudio.wreelysocial.views.adapter.MeetingRoomAdapter;
import com.celerstudio.wreelysocial.views.adapter.MeetingRoomSlotAdapter;
import com.celerstudio.wreelysocial.views.fragments.DatePickerFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

public class VendorMeetingRoomActivity extends BaseActivity {

    public static final String VENDOR = "vendor";
    public static final String MEETING_ROOM = "meeting_room";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.recycler_view_days)
    RecyclerView recyclerViewDays;

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

//    @BindView(R.id.previous)
//    ImageView previous;
//
//    @BindView(R.id.next)
//    ImageView next;

//    @BindView(R.id.name)
//    TextView name;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.image)
    ImageView image;

    Vendor vendor;
    MeetingRoom meetingRoom;

    List<MeetingRoomSlot> items = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CompositeSubscription compositeSubscription;
    private MeetingRoomSlotAdapter itemsAdapter;
    private DayAdapter daysAdapter;

    private int dateCounter = 0;
    private int previouslySelectedDate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        setContentView(R.layout.activity_vendor_meeting_room_slots);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }


        vendor = getIntent().getParcelableExtra(VENDOR);
        meetingRoom = getIntent().getParcelableExtra(MEETING_ROOM);

        daysAdapter = new DayAdapter(days(), this);
        daysAdapter.setOnItemClickListener(new DayAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (previouslySelectedDate == position)
                    return;

                Day tDay = daysAdapter.mDataset.get(previouslySelectedDate);
                tDay.setChecked(false);
                daysAdapter.notifyItemChanged(previouslySelectedDate, tDay);

                Day mDay = daysAdapter.mDataset.get(position);
                mDay.setChecked(true);
                daysAdapter.notifyItemChanged(position, mDay);
                previouslySelectedDate = position;

                Log.d("Selected Date", new Gson().toJson(mDay));

                fetchData(mDay.getDate());

            }
        });
        setLayoutManager(recyclerViewDays, LinearLayoutManager.HORIZONTAL);
        recyclerViewDays.setAdapter(daysAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        itemsAdapter = new MeetingRoomSlotAdapter(items, this, getApp().getUser());
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(32, true));
        itemsAdapter.setOnItemClickListener(new MeetingRoomSlotAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                MeetingRoomSlot slot = items.get(position);
                if (slot.isAvailable()) {
                    confirmBooking(slot, position);
                } else {
                    if (slot.getBookedByMemberId() == Long.valueOf(getApp().getUser().getId())) {
                        if (Util.isMeetingCancelable(slot)) {
                            confirmBookingCancel(slot, position);
                        } else {
                            UiUtils.showSnackbar(findViewById(android.R.id.content), "Cannot cancel this meeting room", Snackbar.LENGTH_LONG);
                        }
                    }
                }
            }
        });

        title.setText(meetingRoom.getName());

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    toolbar.setTitle(meetingRoom.getName());
                } else {
                    toolbar.setTitle("");
                    collapsingToolbar.setTitle("");
                }
            }
        });


//        setDatecounter(true);

        initFetching();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (meetingRoom.getImages() != null && meetingRoom.getImages().length > 0) {
            Picasso.with(this).load(meetingRoom.getImages()[0]).into(image);
        }
    }

//    private void fetchData(Calendar cal) {
//        items = new ArrayList<>();
//        String token = vendor.getAccessToken();
//        setProgressDialog(vendor.getName(), "Fetching meeting room slots");
//        internet.setVisibility(View.GONE);
//        compositeSubscription.add(getAPIService().getMeetingRoomSlots(meetingRoom.getId(), setDate(cal), setDate(cal), token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
//            @Override
//            protected void onSuccess(Response<BasicResponse> response) {
//                items = response.body().getMeetingRoomSlots();
//                if (items.size() == 0) {
//                    internet.setVisibility(View.VISIBLE);
//                    internet.setText("Meeting Room Slots not available");
//                } else {
//                    List<MeetingRoomSlot> tempItems = new ArrayList<>();
////                    int scrollToIndex = 0;
//                    for (int i = 0; i < items.size(); i++) {
//                        MeetingRoomSlot slot = items.get(i);
//                        boolean slotExpired = Util.isMeetingTimeExpired(slot);
//                        if (!slotExpired) {
//                            tempItems.add(slot);
//                        }
//                    }
//                    items = tempItems;
//                    itemsAdapter.addItems(items);
////                    recyclerView.scrollToPosition(scrollToIndex);
//                }
//            }
//
//            @Override
//            protected void onFailure(String message) {
//                internet.setVisibility(View.VISIBLE);
//                internet.setText(message);
//            }
//        }));
//    }

    private void fetchData(String date) {
        items = new ArrayList<>();
        String token = getApp().getUser().getAccessToken();
        setProgressDialog(vendor.getName(), "Fetching meeting room slots");
        internet.setVisibility(View.GONE);
        compositeSubscription.add(getAPIService().getMeetingRoomSlots(vendor.getId().toString(), meetingRoom.getId(), date, date, token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                items = response.body().getMeetingRoomSlots();
                if (items.size() == 0) {
                    internet.setVisibility(View.VISIBLE);
                    internet.setText("Meeting Room Slots not available");
                } else {
                    List<MeetingRoomSlot> tempItems = new ArrayList<>();
//                    int scrollToIndex = 0;
                    for (int i = 0; i < items.size(); i++) {
                        MeetingRoomSlot slot = items.get(i);
                        boolean slotExpired = Util.isMeetingTimeExpired(slot);
                        if (!slotExpired) {
                            tempItems.add(slot);
                        }
                    }
                    items = tempItems;
                    itemsAdapter.addItems(items);
//                    recyclerView.scrollToPosition(scrollToIndex);
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
            fetchData(getCurrentDate());
        }
    }

    @OnClick(R.id.internet)
    void onInternetClick(View view) {
        TextView v = (TextView) view;
        if (v.getText().toString().toLowerCase().contains("settings")) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        } else {
            internet.setVisibility(View.GONE);
            fetchData(getCurrentDate());
        }
    }

    private void setDatecounter(boolean increment) {
//        if (increment)
//            dateCounter += 1;
//        else
//            dateCounter -= 1;
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
    }

    private String setDate(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, dateCounter);

        if (cal != null)
            calendar = cal;

        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String m = String.valueOf(month > 9 ? month : "0" + month);
        String d = String.valueOf(day > 9 ? day : "0" + day);
        String date = calendar.get(Calendar.YEAR) + "-" + m + "-" + d;

        Date da = new Date(calendar.getTimeInMillis());
        String displayDate = new SimpleDateFormat("EEE, MMM d, ''yyyy").format(da);
//        name.setText(displayDate);
        return date;
    }

//    @OnClick(R.id.previous)
//    void onPrevious() {
//        setDatecounter(false);
//        fetchData(null);
//    }
//
//    @OnClick(R.id.next)
//    void onNext() {
//        setDatecounter(true);
//        fetchData(null);
//    }

    public void confirmBooking(MeetingRoomSlot meetingRoomSlot, int pos) {
        CustomPopoverView customPopoverView = CustomPopoverView.builder(this)
                .withNegativeTitle("Cancel")
                .withPositiveTitle("Confirm")
                .withTitle("Confirm booking")
                .withMessage("Are you sure you want to book this slot?")
                .setDialogButtonClickListener(new CustomPopoverView.DialogButtonClickListener() {
                    @Override
                    public void positiveButtonClicked(View view, AlertDialog alertDialog) {
                        bookMeetingRoom(meetingRoomSlot, pos);
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

    public void confirmBookingCancel(MeetingRoomSlot meetingRoomSlot, int pos) {

        CustomPopoverView customPopoverView = CustomPopoverView.builder(this)
                .withNegativeTitle("Cancel")
                .withPositiveTitle("OK")
                .withTitle("Cancel booking")
                .withMessage("Are you sure you want to cancel this slot?")
                .setDialogButtonClickListener(new CustomPopoverView.DialogButtonClickListener() {
                    @Override
                    public void positiveButtonClicked(View view, AlertDialog alertDialog) {
                        cancelMeetingRoom(meetingRoomSlot, pos);
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

    private void bookMeetingRoom(MeetingRoomSlot meetingRoomSlot, int pos) {
        String token = getApp().getUser().getAccessToken();
        meetingRoomSlot.setMemberId(Long.valueOf(getApp().getUser().getId()));
        meetingRoomSlot.setRoomId(Long.valueOf(meetingRoomSlot.getMeetingRoomId()));
        setProgressDialog("Meeting room booking", "wait while we book this meeting room for you");
        compositeSubscription.add(getAPIService().bookMeetingRoom(vendor.getId().toString(), meetingRoomSlot, token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                if (!Util.textIsEmpty(response.body().getMessage())) {
                    UiUtils.showSnackbar(findViewById(android.R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG);
                }
                if (response.body().getMeetingRoomSlots() != null && response.body().getMeetingRoomSlots().size() > 0) {
                    MeetingRoomSlot m = response.body().getMeetingRoomSlots().get(0);
                    items.set(pos, m);
                    itemsAdapter.notifyItemChanged(pos, m);
                }
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
            }
        }));
    }

    private void cancelMeetingRoom(MeetingRoomSlot meetingRoomSlot, int pos) {
        String token = getApp().getUser().getAccessToken();
        meetingRoomSlot.setMemberId(Long.valueOf(getApp().getUser().getId()));
        meetingRoomSlot.setRoomId(Long.valueOf(meetingRoomSlot.getMeetingRoomId()));
        setProgressDialog("Meeting room cancel", "wait while we cancel this meeting room for you");
        compositeSubscription.add(getAPIService().cancelMeetingRoom(vendor.getId().toString(), meetingRoomSlot.getId(), meetingRoomSlot.getBookedByMemberId(), token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                if (!Util.textIsEmpty(response.body().getMessage())) {
                    UiUtils.showSnackbar(findViewById(android.R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG);
                }
                meetingRoomSlot.setAvailable(1);
                meetingRoomSlot.setId(0l);
                items.set(pos, meetingRoomSlot);
                itemsAdapter.notifyItemChanged(pos, meetingRoomSlot);
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));
    }

//    @OnClick(R.id.name)
//    void onName() {
//        DatePickerFragment.newInstance(new DatePickerFragment.DatePickerFragmentListener() {
//            @Override
//            public void onDateSet(String displayDate, Date date) {
//                String formattedDate = new SimpleDateFormat("EEE, MMM d, ''yyyy").format(date);
//                name.setText(formattedDate);
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(date);
//                fetchData(cal);
//            }
//        }).show(getSupportFragmentManager(), "Select Date");
//    }

    private List<Day> days() {
        List<Day> days = new ArrayList<>();
        days.addAll(getMonthDays(0));
        days.addAll(getMonthDays(1));
        if (days.size() > 0)
            days.get(0).setChecked(true);
        return days;
    }

    private List<Day> getMonthDays(int monthId) {
        List<Day> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthId);
        int currentDay = monthId == 0 ? calendar.get(Calendar.DAY_OF_MONTH) : 1;
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = currentDay; i < lastDay + 1; i++) {
            Day day = new Day();
            day.setDayOfMonth(i);
            calendar.set(Calendar.DAY_OF_MONTH, i);
            day.setMonth(new SimpleDateFormat("MMM").format(calendar.getTime()));
            day.setDay(new SimpleDateFormat("EEE").format(calendar.getTime()));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            day.setDate(format.format(calendar.getTime()));
            days.add(day);
        }
        return days;
    }


    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }
}
