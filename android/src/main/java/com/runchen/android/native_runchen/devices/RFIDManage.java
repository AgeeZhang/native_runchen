package com.runchen.android.native_runchen.devices;

import android.content.Context;
import android.util.Log;

import com.android.usbserial.client.AndroidUsbSerialClient;
import com.android.usbserial.client.OnUsbSerialDeviceListener;
import com.gg.reader.api.dal.GClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.EventChannel;

public class RFIDManage {

    private final String TAG = "RFIDManage";
    private volatile static RFIDManage instance;
    private EventChannel.EventSink eventSink = null;
    private GClient client = null;

    public static RFIDManage getInstance() {
        if (instance == null) {
            instance = new RFIDManage();
        }
        return instance;
    }

    public void onListen(EventChannel.EventSink eventSink) {
        this.eventSink = eventSink;
    }

    public boolean openAndroidUsbSerial(Context context) {
        if (client == null)
            client = new GClient();
        List<AndroidUsbSerialClient> usbDevicesList = AndroidUsbSerialClient.getUsbDevicesList(context);
        if (usbDevicesList != null) {
            AndroidUsbSerialClient usbClient = usbDevicesList.get(0);
            usbClient.deviceListener = new OnUsbSerialDeviceListener() {
                @Override
                public void onDeviceConnected() {
                    Log.i(TAG, "onDeviceConnected: 连接成功");
                }

                @Override
                public void onDeviceConnectFailed() {
                    Log.i(TAG, "onDeviceConnectFailed: 连接失败");
                }
            };
            if (client.openAndroidUsbSerial(usbClient)) {
                client.onTagEpcLog = (readerName, info) -> {
                    Log.i(TAG, "openAndroidUsbSerial: " + new Gson().toJson(info));
                    Map<String, Object> body = new HashMap<>();
                    body.put("type", "RFID");
                    body.put("device", usbClient.getUsbName());
                    body.put("data", info);
                    if (eventSink != null) {
                        eventSink.success(new Gson().toJson(body));
                    }
                };
                client.onTagEpcOver = (s, logBaseEpcOver) -> Log.i(TAG, "openAndroidUsbSerial: 6C 标签主动上报结束.");
                return true;
            }
        }
        return false;
    }

    public void close() {
        if (client != null)
            client.close();
    }
}
