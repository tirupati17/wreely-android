package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.databinding.Bindable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.assist.DetailSharedElementEnterCallback;
import com.celerstudio.wreelysocial.assist.IntentUtil;
import com.celerstudio.wreelysocial.models.NearbyWorkspace;
import com.celerstudio.wreelysocial.util.Util;
import com.celerstudio.wreelysocial.views.pager.NearbyWorkspaceDetailViewPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearbyWorkspaceViewPagerActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager viewpager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String STATE_INITIAL_ITEM = "initial";
    private ViewPager viewPager;
    private int initialItem;
    private final View.OnClickListener navigationOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAfterTransition();
                }
            };
    private DetailSharedElementEnterCallback sharedElementCallback;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_nearby_workspace_view_pager);
        ButterKnife.bind(this);

        postponeEnterTransition();

        TransitionSet transitions = new TransitionSet();
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(AnimationUtils.loadInterpolator(this,
                android.R.interpolator.linear_out_slow_in));
        slide.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        transitions.addTransition(slide);
        transitions.addTransition(new Fade());
        getWindow().setEnterTransition(transitions);

        Intent intent = getIntent();
        sharedElementCallback = new DetailSharedElementEnterCallback(intent);
        setEnterSharedElementCallback(sharedElementCallback);
        initialItem = intent.getIntExtra(IntentUtil.SELECTED_ITEM_POSITION, 0);
        setUpViewPager(intent.<NearbyWorkspace>getParcelableArrayListExtra(IntentUtil.PHOTO));

        toolbar.setNavigationOnClickListener(navigationOnClickListener);
        toolbar.setNavigationIcon(R.drawable.ic_action_dark_back);

        super.onCreate(savedInstanceState);
    }

    private void setUpViewPager(ArrayList<NearbyWorkspace> photos) {
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new NearbyWorkspaceDetailViewPagerAdapter(this, photos, sharedElementCallback, savedInstanceState));
        viewPager.setCurrentItem(initialItem);

        viewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (viewPager.getChildCount() > 0) {
                    viewPager.removeOnLayoutChangeListener(this);
                    startPostponedEnterTransition();
                }
            }
        });
        viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.item_spacing));
        viewPager.setPageMarginDrawable(R.drawable.page_margin);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_INITIAL_ITEM, initialItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        initialItem = savedInstanceState.getInt(STATE_INITIAL_ITEM, 0);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void finishAfterTransition() {
        setActivityResult();
        super.finishAfterTransition();
    }

    private void setActivityResult() {
        if (initialItem == viewPager.getCurrentItem()) {
            setResult(RESULT_OK);
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(IntentUtil.SELECTED_ITEM_POSITION, viewPager.getCurrentItem());
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        super.onBackPressed();
    }
}
