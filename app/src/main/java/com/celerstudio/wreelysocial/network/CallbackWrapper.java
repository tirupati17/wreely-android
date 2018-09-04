package com.celerstudio.wreelysocial.network;

import android.app.Activity;
import android.util.Log;

import com.celerstudio.wreelysocial.Constants;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.RestError;
import com.celerstudio.wreelysocial.views.activity.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class CallbackWrapper<T> implements Observer<T> {
    protected abstract void onSuccess(T t);

    protected abstract void onFailure(String message);


    BaseActivity baseActivity;

    public CallbackWrapper(Activity activity) {
        baseActivity = (BaseActivity) activity;
    }

    public CallbackWrapper() {
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        //404 Not Found
//        500 Internal Server Error
        if (baseActivity != null)
            baseActivity.dismissDialog();
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            String error = getErrorMessage(responseBody);
            onFailure(error);
            Log.d("HttpException", error);
        } else if (e instanceof SocketTimeoutException) {
            onFailure("Socket Timeout Exception");
        } else if (e instanceof IOException) {
            onFailure("Network Error (IOException)");
        } else if (e instanceof JsonSyntaxException) {
            onFailure("JsonSyntaxException Or Internal Server Error");
        } else {
//            if (e.getCause() != null)
//                Log.d("GenericFailure", e.getCause().getMessage());
//            onFailure(e.getMessage());
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
            int code = br.code();
            if (code == Constants.HTTPStatusCodes.NOT_FOUND) {
                onFailure(code + " Not Found");
            } else if (code == Constants.HTTPStatusCodes.SERVER_ERROR) {
                onFailure(code + " Internal Server Error");
            } else if (code == Constants.HTTPStatusCodes.UNAUTHORIZED) {
                if (baseActivity != null)
                    baseActivity.invalidSession();
                else {
                    RestError restError = handleError(br.errorBody());
                    onFailure(restError.getMessage());
                }
            } else {
                RestError restError = handleError(br.errorBody());
                onFailure(restError.getMessage());
            }
        }
    }

    public static RestError handleError(ResponseBody response) {
        RestError restError = new RestError();
        restError.setError(false);
        restError.setMessage("Something Went Wrong!");
        if (response instanceof ResponseBody) {
            ResponseBody responseBody = response;
            try {
//                JSONObject jsonObject = new JSONObject(responseBody.string());
//                if (jsonObject.has("message") && jsonObject.get("message") instanceof String) {
//                    restError = new RestError();
//                    restError.setMessage(new String[]{jsonObject.getString("message")});
//                } else {
//
//                }
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

    public static Observable<Response<BasicResponse>> call(Observable observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}