package com.celerstudio.wreelysocial.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.celerstudio.wreelysocial.AndroidApp;
import com.celerstudio.wreelysocial.ConnectivityReceiverListener;

public class BaseFragment extends Fragment implements ConnectivityReceiverListener {



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onPostCreateView(container);
        getApp().setConnectivityListener(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void onPostCreateView(View view) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    public AndroidApp getApp() {
        return ((AndroidApp) getActivity().getApplication());
    }

}
