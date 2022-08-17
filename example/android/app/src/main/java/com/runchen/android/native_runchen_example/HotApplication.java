package com.runchen.android.native_runchen_example;

import java.io.File;

import io.flutter.app.FlutterApplication;

public class HotApplication extends FlutterApplication {

    @Override
    public void onCreate() {
        if (!new File(getFilesDir().getParentFile().getAbsolutePath() + "/lib/libapp.so").exists()) {
            FileUtil.copySo(getApplicationInfo().sourceDir, "lib/" + FileUtil.getArc(getApplicationInfo().nativeLibraryDir) + "/libapp.so",
                    getFilesDir().getParentFile().getAbsolutePath() + "/lib/libapp.so");
            File sof = new File(getFilesDir().getParentFile().getAbsolutePath() + "/lib/libapp.so");
            sof.setExecutable(true);
            sof.setReadable(true);
        }

        File lib1 = new File(getFilesDir().getParentFile().getAbsolutePath() + "/lib/libapp1.so");
        if (lib1.exists()) {
            lib1.renameTo(new File(getFilesDir().getParentFile().getAbsolutePath() + "/lib/libapp.so"));
            lib1.deleteOnExit();
        }
        super.onCreate();
    }
}
