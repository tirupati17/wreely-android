package com.celerstudio.wreelysocial.views.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.celerstudio.wreelysocial.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.login)
    TextView login;

    @BindView(R.id.et_email)
    EditText etEmail;


    CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        compositeSubscription = new CompositeSubscription();

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
        if (!Util.textIsEmpty(etEmail.getText().toString())) {
            emailRequest();
        } else {
            UiUtils.showToast(this, "Enter email id");
        }
    }

    private void emailRequest() {
        setProgressDialog("Forgot Password", "Send you an email to reset password");
//        FirebaseAuth.getInstance().sendPasswordResetEmail(etEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                dismissDialog();
//                if (task.isSuccessful())
//                    UiUtils.showSnackbar(findViewById(android.R.id.content), "Reset link sent to your email", Snackbar.LENGTH_LONG);
//                else
//                    UiUtils.showSnackbar(findViewById(android.R.id.content), "Email Not found", Snackbar.LENGTH_LONG);
//            }
//        });
    }

    @OnClick(R.id.login)
    void onForgotPassword() {
        onBackPressed();
    }


}
