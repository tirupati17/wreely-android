package com.celerstudio.wreelysocial.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.Company;
import com.celerstudio.wreelysocial.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class CompanyInfoFragment extends Fragment {

    @BindView(R.id.person_name)
    EditText personName;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.phone)
    EditText phone;

    Company company;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static CompanyInfoFragment newInstance(Company company) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("company", company);
        CompanyInfoFragment fragment = new CompanyInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_company_info, container, false);
        ButterKnife.bind(this, contentView);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("company")) {
            company = bundle.getParcelable("company");
        }

        personName.setText(Util.textIsEmpty(company.getContactPersonName()) ? "Not Available" : company.getContactPersonName());
        email.setText(Util.textIsEmpty(company.getContactPersonEmailId()) ? "Not Available" : company.getContactPersonEmailId());
        phone.setText(Util.textIsEmpty(company.getContactPersonNumber()) ? "Not Available" : company.getContactPersonNumber());
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
