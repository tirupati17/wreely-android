package com.celerstudio.wreelysocial.views.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.NearbyWorkspace;
import com.celerstudio.wreelysocial.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearbyWorkspaceDetailActivity extends BaseActivity {

    public static final String NEARBY_WORKSPACE = "nearby_workspace";

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.image)
    ImageView image;

    NearbyWorkspace nearbyWorkspace;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_workspace_detail);
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


        nearbyWorkspace = getIntent().getParcelableExtra(NEARBY_WORKSPACE);

        name.setText(nearbyWorkspace.getName());

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    toolbar.setTitle(nearbyWorkspace.getName());
                } else {
                    toolbar.setTitle("");
                    collapsingToolbar.setTitle("");
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (!Util.textIsEmpty(nearbyWorkspace.getImage())) {
            Picasso.with(this).load(nearbyWorkspace.getImage()).into(image);
        }
    }
}
