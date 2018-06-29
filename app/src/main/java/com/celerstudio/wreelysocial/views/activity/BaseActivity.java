package com.celerstudio.wreelysocial.views.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.celerstudio.wreelysocial.AndroidApp;
import com.celerstudio.wreelysocial.ConnectivityReceiver;
import com.celerstudio.wreelysocial.ConnectivityReceiverListener;
import com.celerstudio.wreelysocial.NetworkStateReceiver;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.network.APIService;
import com.celerstudio.wreelysocial.util.MixpanelEvents;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;

import butterknife.BindView;

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiverListener {

    @Nullable
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Nullable
    @BindView(R.id.internet)
    TextView internet;

    protected Context context;
    private ProgressDialog mDialog;
    private OptionMenuListener optionMenuListener;

    @Nullable
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        context = this;
        mDialog = new ProgressDialog(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow()
                    .getDecorView()
                    .setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
        getApp().setConnectivityListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDialog != null && mDialog.isShowing()) {
            dismissDialog();
        }
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void showDialog(String title, String body) {
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.setTitle(title);
        mDialog.setMessage(body);
        mDialog.show();
    }

    public void setOptionMenu(int menu, OptionMenuListener listener) {
        this.optionMenuListener = listener;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                optionMenuListener.onMenuClicked(item);
                return false;
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.d("isConnected", String.valueOf(isConnected));
    }

    public interface OptionMenuListener {
        public void onMenuClicked(MenuItem item);
    }

    public AndroidApp getApp() {
        return ((AndroidApp) getApplication());
    }

    public void setBackNavigation() {
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    public void setToolbarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public void setProgressDialog(String title, String message) {
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.show();
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }

    public void setLayoutManager(RecyclerView recyclerView, int orientation) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(orientation);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public APIService getAPIService() {
        return getApp().getAPIService();
    }

    public MixpanelEvents getMixpanelEvents(User user) {
        return getApp().getMixpanel(user);
    }

    public MixpanelEvents getMixpanelEvents() {
        return getApp().getMixpanel();
    }

}