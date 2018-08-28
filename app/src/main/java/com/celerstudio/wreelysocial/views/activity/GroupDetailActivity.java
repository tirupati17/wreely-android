package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.VerticalSpaceItemDecoration;
import com.celerstudio.wreelysocial.assist.FragmentAdapter;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.Company;
import com.celerstudio.wreelysocial.models.Member;
import com.celerstudio.wreelysocial.models.NearbyWorkspace;
import com.celerstudio.wreelysocial.models.RestError;
import com.celerstudio.wreelysocial.models.Vendor;
import com.celerstudio.wreelysocial.network.CallbackWrapper;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.adapter.MemberAdapter;
import com.celerstudio.wreelysocial.views.fragments.CompanyInfoFragment;
import com.celerstudio.wreelysocial.views.fragments.CompanyMembersFragment;

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

public class GroupDetailActivity extends BaseActivity {

    public static final String COMPANY = "company";
    public static final String VENDOR = "vendor";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.website)
    TextView website;
    @BindView(R.id.internet)
    TextView internet;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.search)
    EditText search;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    private Vendor vendor;

    List<Member> items = new ArrayList<>();
    private CompositeSubscription compositeSubscription;
    private MemberAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ButterKnife.bind(this);

        compositeSubscription = new CompositeSubscription();

        vendor = getIntent().getParcelableExtra(VENDOR);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        title.setText(vendor.getName());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
// generate color based on a key (same key returns the same color), useful for list/grid views
//        int color2 = generator.getColor(item.getContactPersonEmailId());

        TextDrawable drawable = null;
        if (!Util.textIsEmpty(vendor.getName())) {
            drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(vendor.getName().trim().charAt(0)), color1);
        } else {
            drawable = TextDrawable.builder()
                    .buildRound("N", color1);
        }
        logo.setImageDrawable(drawable);
        website.setText("Chat Group Members");

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    toolbar.setTitle(vendor.getName());
                } else {
                    toolbar.setTitle("");
                    collapsingToolbar.setTitle("");
                }
            }
        });

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
                    Intent intent = new Intent(GroupDetailActivity.this, MemberDetailActivity.class);
                    intent.putExtra(MemberDetailActivity.MEMBER, member);
                    startActivity(intent);
                }

            }
        });

        initFetching();

    }

    private void fetchData() {
        items = new ArrayList<>();
        String token = vendor.getAccessToken();
        setProgressDialog(vendor.getName(), "Fetching members");
        compositeSubscription.add(getAPIService().getVendorMembers(token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                items = response.body().getMembers();
                itemsAdapter.addItems(items);
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));

//        compositeSubscription.add(getAPIService().getNearbyWorkspaces(42.3736016, -71.09353620000002, 4, token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<List<NearbyWorkspace>>>() {
//            @Override
//            protected void onSuccess(Response<List<NearbyWorkspace>> listResponse) {
//
//            }
//
//            @Override
//            protected void onFailure(String message) {
//
//            }
//        }));
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (items.size() == 0)
            initFetching();
    }

    @OnTextChanged(R.id.search)
    void onSearch() {
        internet.setVisibility(View.GONE);
        internet.setText(getString(R.string.network_not_available));
        String searchStr = search.getText().toString();
        List<Member> searchedItems = new ArrayList<>();
        if (Util.textIsEmpty(searchStr)) {
            itemsAdapter.addItems(items);
        } else {
            for (Member item : items) {
                if (item.getName().toLowerCase().contains(searchStr.toLowerCase())) {
                    searchedItems.add(item);
                }
            }

            if (searchedItems.size() == 0) {
                internet.setText("No results found for '" + searchStr + "'");
                internet.setVisibility(View.VISIBLE);
            }
            itemsAdapter.addItems(searchedItems);
        }
    }
}


