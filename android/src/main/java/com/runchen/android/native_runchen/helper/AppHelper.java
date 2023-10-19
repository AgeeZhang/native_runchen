package com.runchen.android.native_runchen.helper;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class AppHelper {

    private final String TAG = getClass().getSimpleName();
    private volatile static AppHelper instance;
    private Context context;

    private AppHelper() {

    }

    public static AppHelper getInstance() {
        if (instance == null) {
            instance = new AppHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public boolean openAppToFront() {
        String packageName = context.getPackageName();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName)) {
                Intent intent =new Intent(Intent.ACTION_MAIN);
                ComponentName cn =new ComponentName(packageName,info.topActivity.getClassName());
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
        return true;
    }
}
