package com.runchen.android.native_runchen.devices;

import android.util.Log;

import com.google.gson.Gson;

import org.agee.serialporthelper.PortData;
import org.agee.serialporthelper.SerialPortConfig;
import org.agee.serialporthelper.SerialPortHelper;
import org.agee.serialporthelper.SphResultCallback;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.EventChannel;

public class SerialPortManage {

    private final String TAG = "SerialPortManage";
    private volatile static SerialPortManage instance;
    private Map<String, Object> deviceMap = new HashMap<>();
    private Map<String, Object> statusMap = new HashMap<>();
    private EventChannel.EventSink eventSink = null;

    public static SerialPortManage getInstance() {
        if (instance == null) {
            instance = new SerialPortManage();
        }
        return instance;
    }


    /**
     * 查询HashMap中是否已经创建过此对象
     *
     * @param fileName
     * @return
     */
    private SerialPortHelper getDevice(String fileName) {
        SerialPortHelper serialPortHelper = (SerialPortHelper) deviceMap.get(fileName);
        if (serialPortHelper != null) {
            return serialPortHelper;
        } else {
            Log.e(TAG, "找不到对应串口设备");
            return null;
        }
    }

    public void onListen(EventChannel.EventSink eventSink) {
        this.eventSink = eventSink;
    }

    public void openSerialPort(String fileName, int baudRate) {
        SerialPortHelper serialPortHelper = getDevice(fileName);
        if (serialPortHelper == null) {
            SerialPortConfig config = new SerialPortConfig();
            config.mode = 0;// 是否使用原始模式(Raw Mode)方式来通讯 默认
            config.path = fileName;// 串口地址 [ttyS0 ~ ttyS6, ttyUSB0 ~ ttyUSB4]
            config.baudRate = baudRate;// 波特率
            config.dataBits = 8;// 数据位 [7, 8]
            config.parity = 'n';// 检验类型 [N(无校验) ,E(偶校验), O(奇校验)] (大小写随意)
            config.stopBits = 1;// 停止位 [1, 2]

            serialPortHelper = new SerialPortHelper(32);//接收的数据长度
            serialPortHelper.setConfigInfo(config);
        }
        boolean isOpen = serialPortHelper.openDevice();
        Log.d(TAG, isOpen ? "串口打开成功" : "串口打开失败");
        if (isOpen) {
            deviceMap.put(fileName, serialPortHelper);
            readMessage(fileName);
        }
    }

    public void openSerialPort(int mode, String fileName, int baudRate, int dataBits, char parity, int stopBits) {
        SerialPortHelper serialPortHelper = getDevice(fileName);
        if (serialPortHelper == null) {
            SerialPortConfig config = new SerialPortConfig();
            config.mode = mode;// 是否使用原始模式(Raw Mode)方式来通讯 默认
            config.path = fileName;// 串口地址 [ttyS0 ~ ttyS6, ttyUSB0 ~ ttyUSB4]
            config.baudRate = baudRate;// 波特率
            config.dataBits = dataBits;// 数据位 [7, 8]
            config.parity = parity;// 检验类型 [N(无校验) ,E(偶校验), O(奇校验)] (大小写随意)
            config.stopBits = stopBits;// 停止位 [1, 2]

            serialPortHelper = new SerialPortHelper(32);//接收的数据长度
            serialPortHelper.setConfigInfo(config);
        }
        boolean isOpen = serialPortHelper.openDevice();
        Log.d(TAG, isOpen ? "串口打开成功" : "串口打开失败");
        if (isOpen) {
            deviceMap.put(fileName, serialPortHelper);
            readMessage(fileName);
        }
    }

    public void sendData(String fileName, String data) {
        SerialPortHelper serialPortHelper = getDevice(fileName);
        if (serialPortHelper != null) {
            serialPortHelper.send(data);
        } else {
            Log.e(TAG, "串口未打开");
        }
    }

    public void close(String fileName) {
        SerialPortHelper serialPortHelper = getDevice(fileName);
        if (serialPortHelper != null) {
            serialPortHelper.closeDevice();
            deviceMap.remove(fileName);
        }
    }

    private void readMessage(String fileName) {
        SerialPortHelper serialPortHelper = getDevice(fileName);
        if (serialPortHelper != null) {
            serialPortHelper.setSphResultCallback(new SphResultCallback() {
                @Override
                public void onReceiveData(PortData data) {
                    Map<String, Object> body = new HashMap<>();
                    body.put("type", "serialPort");
                    body.put("device", fileName);
                    body.put("data", data.getCommandsHex());
                    Log.i(TAG, body.toString());
                    if (eventSink != null) {
                        Gson gson = new Gson();
                        eventSink.success(gson.toJson(body));
                    }
                }

                @Override
                public void onComplete() {
                    Log.i(TAG, "onComplete");
                }
            });
        } else {
            Log.e(TAG, "串口未打开");
        }
    }


}
