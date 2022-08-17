package com.runchen.android.native_runchen_example;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileUtil {

    public static void copySo(String apkPath, String libName, String toPath) {
        Log.e("FileUtil", "copy from= " + apkPath + "\nlibName=" + libName + "\n toPath=" + toPath);
        try {
            ZipFile zipFile = new ZipFile(apkPath);
            ZipInputStream zipInput = new ZipInputStream(new FileInputStream(apkPath));
            ZipEntry entry = null;
            File outFile = new File(toPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            FileOutputStream soOutput = new FileOutputStream(toPath);
            while ((entry = zipInput.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(libName)) {
                    Log.e("FileUtil", "copy entry=" + entry.getName());
                    byte[] buffer = new byte[(int) entry.getSize()];
                    int len = -1;
                    InputStream it = zipFile.getInputStream(entry);
                    while ((len = it.read(buffer)) != -1) {
                        soOutput.write(buffer, 0, len);
                    }
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getArc(String nativePath) {
        if (nativePath.endsWith("arm64")) {
            return "arm64-v8a";
        }
        return "armeabi-v7a";
    }


}
