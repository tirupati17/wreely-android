package com.celerstudio.wreelysocial.views.activity;

import android.content.Context;
import android.content.Intent;
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

import com.celerstudio.wreelysocial.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.User;
import com.celerstudio.wreelysocial.util.UiUtils;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.forgot_password)
    TextView forgotPassword;

    CompositeSubscription compositeSubscription;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        this.mAuth = FirebaseAuth.getInstance();
        myRef = getApp().getFirebaseDBRef("users");
        compositeSubscription = new CompositeSubscription();
        if (!BuildConfig.BUILD_TYPE.equalsIgnoreCase("release")) {
            etEmail.setText("tirupati.balan@gmail.com");
            etPassword.setText("123456789");
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
        if (isLoginValid()) {
            User user = new User();
            user.setEmail(etEmail.getText().toString());
            signinUserOnFirebase(user);
        }
    }

    private boolean isLoginValid() {
        String errorMessage = "";

        String emailStr = etEmail.getText().toString();
        String passwordStr = etPassword.getText().toString();

        if (emailStr.equalsIgnoreCase("")) {
            errorMessage = "please enter email";
        } else if (passwordStr.equalsIgnoreCase("")) {
            errorMessage = "please enter password";
        }

        if (!errorMessage.equalsIgnoreCase(""))
            UiUtils.showSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG);

        return errorMessage.equalsIgnoreCase("");
    }

    @OnClick(R.id.forgot_password)
    void onForgotPassword() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void signinUserOnFirebase(User user) {
        showDialog(getString(R.string.app_name), "Wait while we log you in");
        mAuth.signInWithEmailAndPassword(user.getEmail(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dismissDialog();
                if (task.isSuccessful()) {
                    String id = task.getResult().getUser().getUid();
                    user.setAccessToken(id);
                    myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                User user = dataSnapshot.getValue(User.class);
                                user.setAccessToken(id);
                                try {
                                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                                    user.setCompanyKey((String) mapMessage.get("company_key"));
                                    user.setMobile((String) mapMessage.get("contact_no"));
                                    user.setName((String) mapMessage.get("full_name"));
                                    user.setCompanyKey((String) mapMessage.get("company_key"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                saveUserAndNavigate(user);
                            } else {
                                UiUtils.showSnackbar(findViewById(android.R.id.content), "There was some problem while login in", Snackbar.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            UiUtils.showSnackbar(findViewById(android.R.id.content), "There was some problem while login in", Snackbar.LENGTH_SHORT);
                        }
                    });
                } else {
                    UiUtils.showSnackbar(findViewById(android.R.id.content), "There was some problem while login in", Snackbar.LENGTH_SHORT);
                }
            }
        });
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
