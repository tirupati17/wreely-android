package com.celerstudio.wreelysocial.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EventMembersFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.internet)
    TextView internet;

    List<Member> items = new ArrayList<>();
    private MemberAdapter itemsAdapter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static EventMembersFragment newInstance(List<Member> members) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("members", (ArrayList<? extends Parcelable>) members);
        EventMembersFragment fragment = new EventMembersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_event_members, container, false);
        ButterKnife.bind(this, contentView);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("members")) {
            items = bundle.getParcelableArrayList("members");
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
//                Member member = items.get(position);
//                if (!Util.textIsEmpty(member.getName())) {
//                    Intent intent = new Intent(getActivity(), MemberDetailActivity.class);
//                    intent.putExtra(MemberDetailActivity.MEMBER, member);
//                    startActivity(intent);
//                }
            }
        });

        if (items.size() == 0) {
            internet.setVisibility(View.VISIBLE);
            internet.setText("Members list is empty");
        } else {
            internet.setVisibility(View.GONE);
        }

        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
