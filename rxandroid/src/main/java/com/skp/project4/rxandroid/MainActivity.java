package com.skp.project4.rxandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.observables.BlockingObservable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> simpleObservable =
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("Hello RxAndroid !!");
                        subscriber.onCompleted();
                    }
                });

        simpleObservable
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "complete!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(String text) {
                        ((TextView) findViewById(R.id.textView)).setText(text);
                    }
                });

        Observable<String> lefts = RxView.clicks(findViewById(R.id.leftButton))
                .map(event -> "left");

        Observable<String> rights = RxView.clicks(findViewById(R.id.rightButton))
                .map(event -> "right");

        Observable<String> together = Observable.merge(lefts, rights);

        together.subscribe(text -> ((TextView) findViewById(R.id.textView2)).setText(text));


//        together.map(text -> text.toUpperCase())
//                .subscribe(text -> Toast.makeText(this, text, Toast.LENGTH_SHORT).show());

        //new Presenter().presenter2(this);
        new Presenter().presenter(this).subscribe(new Subscriber<JSONObject>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError(error=" + e + ")");

            }

            @Override
            public void onNext(JSONObject jsonObject) {
                Log.d(TAG, "onNext(response=" + jsonObject + ")");
            }
        });

        new Presenter().countObservable()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError(error=" + e + ")");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext(response=" + integer + ")");
                    }
                });

        new Presenter().countObservable()
                .skip(10)
                .take(5)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "" + integer + "_xform";
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError(error=" + e + ")");
                    }

                    @Override
                    public void onNext(String integer) {
                        Log.d(TAG, "onNext(response=" + integer + ")");
                    }
                });
    }
}
