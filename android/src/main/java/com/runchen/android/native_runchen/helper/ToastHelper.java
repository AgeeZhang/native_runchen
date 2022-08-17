package com.runchen.android.native_runchen.helper;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

    private final String TAG = getClass().getSimpleName();
    private volatile static ToastHelper instance;
    private Context context;

    private ToastHelper() {

    }

    public static ToastHelper getInstance() {
        if (instance == null) {
            instance = new ToastHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
