package com.skp.project4.rxandroid;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by a1000990 on 2017. 1. 2..
 */

public class Presenter {
    private static final String TAG = Presenter.class.getSimpleName();
    public static final String SERVER_HOST = "http://extapidev.trucking.kr";
    public static final String SERVER_URL_SETTING = "/setting/android";

    public void presenter2(Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                SERVER_HOST + SERVER_URL_SETTING, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse(response=" + response + ")");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse(error" + error + ")");
                    }
                });

        Volleyer.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public Observable<JSONObject> presenter(final Context context) {
        // Volley를 썼기때문에 자동으로 Asynchronous Observable 이 되었다.
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
                    @Override
                    public void call(final Subscriber<? super JSONObject> subscriber) {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                SERVER_HOST + SERVER_URL_SETTING, new JSONObject(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // API호출후 response 오는동안 unsubscribe 했다.
                                        if (subscriber.isUnsubscribed()) {
                                            Log.e(TAG, "subscriber is unsubscribed");
                                            return;
                                        }
                                        subscriber.onNext(response);
                                        subscriber.onCompleted();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (subscriber.isUnsubscribed()) {
                                            Log.e(TAG, "subscriber is unsubscribed");
                                            return;
                                        }
                                        subscriber.onError(error);
                                    }
                                });

                        Volleyer.getInstance(context).addToRequestQueue(jsonObjectRequest);
                    }
                });
    }

    public Observable<Integer> countObservable() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i=0; i < 75; i++) {
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }
                    subscriber.onNext(i);
                }
                if (subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        });
    }
}
