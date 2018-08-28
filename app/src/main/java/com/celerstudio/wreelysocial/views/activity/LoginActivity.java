package com.celerstudio.wreelysocial.views.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.celerstudio.wreelysocial.BuildConfig;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.MobileVerification;
import com.celerstudio.wreelysocial.network.CallbackWrapper;
import com.celerstudio.wreelysocial.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.et_otp)
    EditText etOtp;

    @BindView(R.id.otp_cont)
    TextInputLayout otpCont;

    @BindView(R.id.et_mobile)
    EditText etMobile;

    @BindView(R.id.mobile_cont)
    TextInputLayout mobileCont;

    @BindView(R.id.edit_number)
    TextView editNumber;

    @BindView(R.id.resend)
    TextView resend;

    CompositeSubscription compositeSubscription;

    MobileVerification mvr = new MobileVerification();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        compositeSubscription = new CompositeSubscription();

        mobileView();

        if (!BuildConfig.BUILD_TYPE.equalsIgnoreCase("release")) {
            etMobile.setText("9967247265");
        }

        setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @OnClick(R.id.submit)
    void onClick(View view) {
        if (submit.getText().toString().equalsIgnoreCase("verify mobile")) {
            if (isMobileValid()) {
                mvr = new MobileVerification();
                mvr.setMobileNumber(etMobile.getText().toString());
                mvr.setCountryCode("+91");
                login(mvr);
            }
        } else if (submit.getText().toString().equalsIgnoreCase("verify otp")) {
            if (isOtpValid()) {
                mvr.setLoginCode(etOtp.getText().toString());
                loginConfirmation(mvr);
            }

        }
    }

    private boolean isMobileValid() {
        String errorMessage = "";

        String emailStr = etMobile.getText().toString();

        if (Util.textIsEmpty(emailStr)) {
            errorMessage = "please enter mobile";
        } else if (emailStr.length() != 10) {
            errorMessage = "Mobile number must be 10 digits long";
        }

        if (!errorMessage.equalsIgnoreCase(""))
            UiUtils.showSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG);

        return errorMessage.equalsIgnoreCase("");
    }

    private boolean isOtpValid() {
        String errorMessage = "";

        String emailStr = etOtp.getText().toString();

        if (Util.textIsEmpty(emailStr)) {
            errorMessage = "please enter otp";
        } else if (emailStr.length() != 4) {
            errorMessage = "OTP must be 4 digits long";
        }

        if (!errorMessage.equalsIgnoreCase(""))
            UiUtils.showSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG);

        return errorMessage.equalsIgnoreCase("");
    }

    @OnClick(R.id.edit_number)
    void onEditNumber() {
        mobileView();
    }

    @OnClick(R.id.resend)
    void onResend() {
        login(mvr);
    }


    private void mobileView() {
        otpCont.setVisibility(View.GONE);
        mobileCont.setVisibility(View.VISIBLE);
        etMobile.setText("");
        submit.setText("Verify Mobile");
        editNumber.setVisibility(View.GONE);
        resend.setVisibility(View.GONE);
    }

    private void otpView() {
        otpCont.setVisibility(View.VISIBLE);
        etOtp.setText("");
        mobileCont.setVisibility(View.GONE);
        etMobile.setText("");
        submit.setText("Verify OTP");
        editNumber.setVisibility(View.VISIBLE);
        resend.setVisibility(View.VISIBLE);
    }

    private void login(final MobileVerification mvr) {
        showDialog(getString(R.string.app_name), "Wait while we log you in");
        compositeSubscription.add(getAPIService().login(mvr).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                dismissDialog();
                mvr.setMemberId(response.body().getMemberId());
                otpView();
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));

    }

    private void loginConfirmation(final MobileVerification mvr) {
        showDialog(getString(R.string.app_name), "Wait while we verify otp");
        compositeSubscription.add(getAPIService().loginConfirmation(mvr).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse>>(this) {
            @Override
            protected void onSuccess(Response<BasicResponse> response) {
                dismissDialog();

            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));

    }

    private void saveUserAndNavigate(User user) {
//        OneSignal.setEmail(user.getEmail());
        getMixpanelEvents(user).createUserInMixpanel();
        getApp().getPreferences().setUser(user);
        getApp().setUser();
        startActivity(new Intent(LoginActivity.this, VendorsActivity.class));
        finish();
    }
}
