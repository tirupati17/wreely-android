package com.celerstudio.wreelysocial.views.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.celerstudio.wreelysocial.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComingSoonActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String pageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);
        ButterKnife.bind(this);
        setBackNavigation();
        pageName = getIntent().getStringExtra("page_name");
        toolbar.setTitle(pageName);
    }
}
