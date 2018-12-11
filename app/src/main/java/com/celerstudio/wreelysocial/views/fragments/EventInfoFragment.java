package com.celerstudio.wreelysocial.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.Company;
import com.celerstudio.wreelysocial.models.Event;
import com.celerstudio.wreelysocial.models.RestError;
import com.celerstudio.wreelysocial.network.CallbackWrapper;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.activity.BaseActivity;
import com.celerstudio.wreelysocial.views.activity.EventDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class EventInfoFragment extends BaseFragment {

    @BindView(R.id.attending)
    EditText attending;
    @BindView(R.id.time)
    EditText time;
    @BindView(R.id.total_rsvp)
    EditText totalRsvp;

    @BindView(R.id.attend)
    Button attend;

    Event event;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static EventInfoFragment newInstance(Event event) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("event", event);
        EventInfoFragment fragment = new EventInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_event_info, container, false);
        ButterKnife.bind(this, contentView);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("event")) {
            event = bundle.getParcelable("event");
        }
        setData();
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @OnClick(R.id.attend)
    void onAttend() {

        EventDetailActivity activity = (EventDetailActivity) getActivity();
        activity.showDialog(getString(R.string.app_name), "Processing this request");
        if (attend.getText().toString().equalsIgnoreCase("attend")) {
            compositeSubscription.add(getApp().getAPIService().attendEvent(event.getId().intValue(), Integer.parseInt(getApp().getUser().getId()), getApp().getUser().getAccessToken()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(activity) {
                @Override
                protected void onSuccess(Response<BasicResponse> response) {
                    event.setAttending(true);
                    setData();
                }

                @Override
                protected void onFailure(String message) {
                    UiUtils.showSnackbar(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
                }
            }));
        } else {
            compositeSubscription.add(getApp().getAPIService().withdrawEvent(event.getId().intValue(), Integer.parseInt(getApp().getUser().getId()), getApp().getUser().getAccessToken()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(activity) {
                @Override
                protected void onSuccess(Response<BasicResponse> response) {
                    event.setAttending(false);
                    setData();
                }

                @Override
                protected void onFailure(String message) {
                    UiUtils.showSnackbar(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
                }
            }));
        }
    }

    private void setData() {
        String dateStr = Util.getEventTimeDuration(event.getStartTime(), event.getEndTime());

        time.setText(dateStr);
        totalRsvp.setText(event.getTotalRsvp().toString());

        if (event.getAttending()) {
            attend.setBackground(UiUtils.selectorBackground(getActivity().getResources().getColor(R.color.light_blue), getActivity().getResources().getColor(R.color.colorAccent)));
            attending.setText("Yes");
            attend.setText("Cancel");
        } else {
            attend.setBackground(UiUtils.selectorBackground(getActivity().getResources().getColor(R.color.colorPrimary), getActivity().getResources().getColor(R.color.colorPrimaryDark)));
            attending.setText("I'm Not");
            attend.setText("Attend");
        }

    }

}
