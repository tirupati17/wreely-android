package com.celerstudio.wreelysocial.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.util.Util;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SplashActivity extends BaseActivity {

    private static final int TIMER_VALUE = 1;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d("SHA1", Util.getCertificateSHA1Fingerprint(this));
        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(waitForSplash());
    }

    private Subscription waitForSplash() {
        return Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Long>() {
                    public void call(Long x) {
                        Log.d("Interval", String.valueOf(x));
                    }
                })
                .takeUntil(aLong -> aLong == TIMER_VALUE)
                .doOnCompleted(() -> {
                    navigate();
                }).subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    private void navigate() {
        if (getApp().getPreferences().isUserLoggedIn()) {
            startActivity(new Intent(SplashActivity.this, VendorsActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

}
