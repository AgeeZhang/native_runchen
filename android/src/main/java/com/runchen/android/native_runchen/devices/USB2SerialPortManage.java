package com.runchen.android.native_runchen.devices;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;
import io.flutter.plugin.common.EventChannel;

public class USB2SerialPortManage {

    private final String TAG = "USB2SerialPortManage";
    private volatile static USB2SerialPortManage instance;

    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";
    private CH34xUARTDriver driver;
    private boolean isOpen = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Map<String, Object> body = new HashMap<>();
            body.put("type", "USB2SerialPort");
            body.put("device", "fileName");
            body.put("data", msg.obj);
            if (eventSink != null) {
                Gson gson = new Gson();
                eventSink.success(gson.toJson(body));
            }
        }
    };

    private EventChannel.EventSink eventSink = null;

    public static USB2SerialPortManage getInstance() {
        if (instance == null) {
            instance = new USB2SerialPortManage();
        }
        return instance;
    }

    /**
     * 初始化USB设备信息
     *
     * @param context
     * @return
     */
    public boolean initDriver(Context context) {
        driver = new CH34xUARTDriver(
                (UsbManager) context.getSystemService(Context.USB_SERVICE), context,
                ACTION_USB_PERMISSION);
        return driver.UsbFeatureSupported();
    }

    public boolean setConfig(int baudRate, byte dataBit, byte stopBit, byte parity, byte flowControl) {
        if (driver != null)
            return driver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);//配置串口波特率，函数说明可参照编程手册
        else
            return false;
    }

    public boolean writeData(String text) {
        byte[] to_send = toByteArray(text);
        if (driver != null) {
            int retval = driver.WriteData(to_send, to_send.length);
            return retval > 0;
        } else {
            return false;
        }
    }

    public boolean openDevice() {
        if (!isOpen) {
            int retval = driver.ResumeUsbPermission();
            if (retval == 0) {
                retval = driver.ResumeUsbList();
                if (retval == -1) {
                    closeDevice();
                } else if (retval == 0) {
                    if (driver.mDeviceConnection != null) {
                        if (!driver.UartInit()) {
                            return isOpen;
                        }
                        isOpen = true;
                        new readThread().start();
                        Log.i(TAG, "串口打开成功");
                    } else {
                        return isOpen;
                    }
                } else {
                    return isOpen;
                }
            }
        } else {
            Log.e(TAG, "串口已打开，请勿重复开启");
        }
        return isOpen;
    }

    public boolean closeDevice() {
        isOpen = false;
        driver.CloseDevice();
        return true;
    }

    public void onListen(EventChannel.EventSink eventSink) {
        this.eventSink = eventSink;
    }

    private class readThread extends Thread {
        public void run() {
            byte[] buffer = new byte[4096];
            while (true) {
                Message msg = Message.obtain();
                if (!isOpen) {
                    break;
                }
                int length = driver.ReadData(buffer, 4096);
                if (length > 0) {
                    String recv = toHexString(buffer, length);        //以16进制输出
                    msg.obj = recv;
                    handler.sendMessage(msg);
                }
            }
        }
    }

    /**
     * 将byte[]数组转化为String类型
     *
     * @param arg    需要转换的byte[]数组
     * @param length 需要转换的数组长度
     * @return 转换后的String队形
     */
    private String toHexString(byte[] arg, int length) {
        String result = new String();
        if (arg != null) {
            for (int i = 0; i < length; i++) {
                result = result
                        + (Integer.toHexString(
                        arg[i] < 0 ? arg[i] + 256 : arg[i]).length() == 1 ? "0"
                        + Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                        : arg[i])
                        : Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                        : arg[i])) + " ";
            }
            return result;
        }
        return "";
    }

    /**
     * 将String转化为byte[]数组
     *
     * @param arg 需要转换的String对象
     * @return 转换后的byte[]数组
     */
    private byte[] toByteArray(String arg) {
        if (arg != null) {
            /* 1.先去除String中的' '，然后将String转换为char数组 */
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != ' ') {
                    NewArray[length] = array[i];
                    length++;
                }
            }
            /* 将char数组中的值转成一个实际的十进制数组 */
            int EvenLength = (length % 2 == 0) ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i = 0; i < length; i++) {
                    if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                        data[i] = NewArray[i] - '0';
                    } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                        data[i] = NewArray[i] - 'a' + 10;
                    } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                        data[i] = NewArray[i] - 'A' + 10;
                    }
                }
                /* 将 每个char的值每两个组成一个16进制数据 */
                byte[] byteArray = new byte[EvenLength / 2];
                for (int i = 0; i < EvenLength / 2; i++) {
                    byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                }
                return byteArray;
            }
        }
        return new byte[]{};
    }

}
