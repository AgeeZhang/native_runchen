package com.runchen.android.native_runchen.devices;

import org.agee.i2clib.I2cHelper;

import java.util.HashMap;
import java.util.Map;

public class I2CManage {

    private final String TAG = "I2CManage";
    private volatile static I2CManage instance;
    private Map<String, Object> deviceMap = new HashMap<>();
    private Map<String, Object> statusMap = new HashMap<>();

    public static I2CManage getInstance() {
        if (instance == null) {
            instance = new I2CManage();
        }
        return instance;
    }

    public I2cHelper getDevice(String fileName) {
        I2cHelper i2cHelper = (I2cHelper) deviceMap.get(fileName);
        if (i2cHelper == null) {
            try {
                i2cHelper = new I2cHelper(fileName);
                deviceMap.put(fileName, i2cHelper);
                statusMap.put(fileName, System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i2cHelper;
    }

    public int[] read(String fileName, int length) {
        try {
            int[] buf = new int[length];
            I2cHelper i2cHelper = getDevice(fileName);
            statusMap.put(fileName, System.currentTimeMillis());
            return i2cHelper.read(0, buf, length);
        } catch (Exception e) {

        }
        return null;
    }

    public void write(String fileName, String data) {
        try {
            int buf[] = new int[1];
            buf[0] = Integer.parseInt(data, 16);
            I2cHelper i2cHelper = getDevice(fileName);
            i2cHelper.write(0, 0, buf, 1);
            statusMap.put(fileName, System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
