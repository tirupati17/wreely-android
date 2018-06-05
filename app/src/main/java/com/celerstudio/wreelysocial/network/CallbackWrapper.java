package com.celerstudio.wreelysocial.network;

import android.app.Activity;
import android.util.Log;

import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.RestError;
import com.celerstudio.wreelysocial.views.activity.BaseActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observer;

public abstract class CallbackWrapper<T> implements Observer<T> {
    protected abstract void onSuccess(T t);

    protected abstract void onFailure(String message);

    BaseActivity baseActivity;

    public CallbackWrapper(Activity activity) {
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            String error = getErrorMessage(responseBody);
            onFailure(error);
            Log.d("HttpException", error);
        } else if (e instanceof SocketTimeoutException) {
            onFailure("Socket Timeout Exception");
        } else if (e instanceof IOException) {
            onFailure("Network Error (IOException)");
        } else {
            Log.d("GenericFailure", e.getMessage());
            onFailure(e.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
        Response<BasicResponse> br = (Response<BasicResponse>) t;
        if (baseActivity != null)
            baseActivity.dismissDialog();
        if (br.isSuccessful()) {
            onSuccess(t);
        } else {
            RestError restError = handleError(br.errorBody());
            onFailure(restError.getMessage());
        }
    }

    public static RestError handleError(ResponseBody response) {
        RestError restError = new RestError();
        restError.setError(false);
        restError.setMessage("Something Went Wrong!");
        if (response instanceof ResponseBody) {
            ResponseBody responseBody = response;
            try {
                restError = new Gson().fromJson(new String(responseBody.bytes()), RestError.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return restError;
    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}