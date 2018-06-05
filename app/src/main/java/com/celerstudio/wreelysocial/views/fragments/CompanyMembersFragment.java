package com.celerstudio.wreelysocial.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.celerstudio.wreelysocial.views.activity.MemberDetailActivity;
import com.celerstudio.wreelysocial.views.activity.VendorMembersActivity;
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

public class CompanyMembersFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.internet)
    TextView internet;

    List<Member> items = new ArrayList<>();
    private MemberAdapter itemsAdapter;

    Company company;
    Vendor vendor;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static CompanyMembersFragment newInstance(Company company, Vendor vendor) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("company", company);
        bundle.putParcelable("vendor", vendor);
        CompanyMembersFragment fragment = new CompanyMembersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_company_members, container, false);
        ButterKnife.bind(this, contentView);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("company")) {
            company = bundle.getParcelable("company");
        }

        if (bundle != null && bundle.containsKey("vendor")) {
            vendor = bundle.getParcelable("vendor");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        itemsAdapter = new MemberAdapter(items, getActivity());
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(32, true));
        itemsAdapter.setOnItemClickListener(new MemberAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Member member = items.get(position);
                if (!Util.textIsEmpty(member.getName())) {
                    Intent intent = new Intent(getActivity(), MemberDetailActivity.class);
                    intent.putExtra(MemberDetailActivity.MEMBER, member);
                    startActivity(intent);
                }

            }
        });

        initFetching();

        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private void fetchData() {
        items = new ArrayList<>();
        String token = vendor.getAccessToken();
        compositeSubscription.add(getApp().getAPIService().getCompanyMembers(String.valueOf(company.getId()), token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Response<BasicResponse>>() {
            @Override
            public void call(Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    items = response.body().getMembers();
                    itemsAdapter.addItems(items);
                } else {
                    RestError restError = Util.handleError(response.errorBody());
                    UiUtils.showSnackbar(getActivity().findViewById(android.R.id.content), restError.getMessage(), Snackbar.LENGTH_LONG);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
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
        if (!ConnectivityReceiver.isConnected(getActivity())) {
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
