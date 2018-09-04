package com.celerstudio.wreelysocial.views.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.assist.FragmentAdapter;
import com.celerstudio.wreelysocial.models.Company;
import com.celerstudio.wreelysocial.models.Event;
import com.celerstudio.wreelysocial.models.Vendor;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.fragments.CompanyInfoFragment;
import com.celerstudio.wreelysocial.views.fragments.CompanyMembersFragment;
import com.celerstudio.wreelysocial.views.fragments.EventInfoFragment;
import com.celerstudio.wreelysocial.views.fragments.EventMembersFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends BaseActivity {

    public static final String EVENT = "event";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.sliding_tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    private Event event;

    List<Fragment> tabsFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        event = getIntent().getParcelableExtra(EVENT);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        title.setText(event.getTitle());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
// generate color based on a key (same key returns the same color), useful for list/grid views
//        int color2 = generator.getColor(item.getContactPersonEmailId());

        TextDrawable drawable = null;
        if (!Util.textIsEmpty(event.getTitle())) {
            drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(event.getTitle().trim().charAt(0)), color1);
        } else {
            drawable = TextDrawable.builder()
                    .buildRound("N", color1);
        }
        logo.setImageDrawable(drawable);
        description.setText(Util.textIsEmpty(event.getDescription()) ? "Website - N/A" : event.getDescription());

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    toolbar.setTitle(event.getTitle());
                } else {
                    toolbar.setTitle("");
                    collapsingToolbar.setTitle("");
                }
            }
        });

        setupBottomTabs();

    }

    private void setupBottomTabs() {
        tabsFragments = new ArrayList<>();

        List<String> titles = new ArrayList<>();

        titles.add("Event Info");
        titles.add("Attendees");

        if (tabs.getTabCount() == 0) {
            for (int i = 0; i < tabs.getTabCount(); i++) {
                tabs.getTabAt(i).setText(titles.get(i));
            }
            tabs.setTabMode(TabLayout.MODE_FIXED);
            tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        }

        EventInfoFragment eventInfoFragment = EventInfoFragment.newInstance(event);
        EventMembersFragment eventMembersFragment = EventMembersFragment.newInstance(event.getAttendees());

        tabsFragments.add(eventInfoFragment);
        tabsFragments.add(eventMembersFragment);

        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), tabsFragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabsFromPagerAdapter(adapter);
        int[] dIcons = {R.drawable.ic_info, R.drawable.ic_member};
        for (int i = 0; i < tabs.getTabCount(); i++) {
            View tabView = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
            TextView t = (TextView) tabView.findViewById(R.id.title);
            ImageView img = (ImageView) tabView.findViewById(R.id.icon);
            img.setImageDrawable(this.getResources().getDrawable(dIcons[i]));
            t.setText(titles.get(i));
            tabs.getTabAt(i).setCustomView(tabView);
        }
    }

}


