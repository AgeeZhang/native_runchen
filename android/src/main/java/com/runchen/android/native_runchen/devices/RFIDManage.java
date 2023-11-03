package com.runchen.android.native_runchen.devices;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.usbserial.client.AndroidUsbSerialClient;
import com.android.usbserial.client.OnUsbSerialDeviceListener;
import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.EnumG;
import com.gg.reader.api.protocol.gx.LogBaseEpcInfo;
import com.gg.reader.api.protocol.gx.MsgBaseInventoryEpc;
import com.gg.reader.api.protocol.gx.MsgBaseStop;
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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: " + msg.what);
            Map<String, Object> body = new HashMap<>();
            body.put("type", "RFID");
            body.put("device", "RFIDDevice");
            body.put("data", msg.obj);
            if (eventSink != null) {
                eventSink.success(new Gson().toJson(body));
            }
        }
    };

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
                    Log.i(TAG, "openAndroidUsbSerial: 6C 标签主动上报开始.");
                    Message msg = Message.obtain();
                    msg.obj = epcInfoToMap(info);
                    handler.sendMessage(msg);
                };
                client.onTagEpcOver = (s, logBaseEpcOver) -> {
                    Log.i(TAG, "openAndroidUsbSerial: 6C 标签主动上报结束.");
                    Message msg = Message.obtain();
                    msg.obj = "onTagEpcOver";
                    handler.sendMessage(msg);
                };
                return true;
            }
        }
        return false;
    }


    public boolean startRead(int mode, int timeout) {
        if (client != null) {
            MsgBaseInventoryEpc msgBaseInventoryEpc = new MsgBaseInventoryEpc();
            msgBaseInventoryEpc.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2 |
                    EnumG.AntennaNo_3 | EnumG.AntennaNo_4);
            msgBaseInventoryEpc.setInventoryMode(mode);
            msgBaseInventoryEpc.setTimeout(timeout);
            client.sendSynMsg(msgBaseInventoryEpc);
            if (0 == msgBaseInventoryEpc.getRtCode()) {
                return true;
            }
        }
        return false;
    }

    public boolean stopRead() {
        if (client != null) {
            MsgBaseStop msgBaseStop = new MsgBaseStop();
            client.sendSynMsg(msgBaseStop);
            if (0 == msgBaseStop.getRtCode()) {
                return true;
            }
        }
        return false;
    }

    public boolean close() {
        if (client != null) {
            stopRead();
            return client.close();
        }
        return false;
    }

    private Map<String, Object> epcInfoToMap(LogBaseEpcInfo info) {
        Map<String, Object> data = new HashMap<>();
        data.put("Epc", info.getEpc());
        data.put("Pc", info.getPc());
        data.put("AntId", info.getAntId());
        data.put("Rssi", info.getRssi());
        data.put("Result", info.getResult());
        data.put("Tid", info.getTid());
        data.put("Userdata", info.getUserdata());
        data.put("Reserved", info.getReserved());
        return data;
    }
}
