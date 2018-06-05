package com.celerstudio.wreelysocial.views.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.Company;
import com.celerstudio.wreelysocial.models.Member;
import com.celerstudio.wreelysocial.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemberDetailActivity extends BaseActivity {

    public static final String MEMBER = "member";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.occupation)
    EditText occupation;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logo;

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
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

        member = getIntent().getParcelableExtra(MEMBER);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        title.setText(member.getName());

        occupation.setText(Util.textIsEmpty(member.getOccupation()) ? "Not Available" : member.getOccupation());
        email.setText(Util.textIsEmpty(member.getEmailId()) ? "Not Available" : member.getEmailId());
        phone.setText(Util.textIsEmpty(member.getContactNo()) ? "Not Available" : member.getContactNo());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
// generate color based on a key (same key returns the same color), useful for list/grid views
//        int color2 = generator.getColor(item.getContactPersonEmailId());

        TextDrawable drawable = null;
        if (!Util.textIsEmpty(member.getName())) {
            drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(member.getName().trim().charAt(0)), color1);
        } else {
            drawable = TextDrawable.builder()
                    .buildRound("N", color1);
        }
        logo.setImageDrawable(drawable);
    }


}
