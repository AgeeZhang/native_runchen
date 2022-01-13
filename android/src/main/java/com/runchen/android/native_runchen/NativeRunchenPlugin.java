package com.runchen.android.native_runchen;

import androidx.annotation.NonNull;

import com.runchen.android.native_runchen.devices.I2CManage;
import com.runchen.android.native_runchen.devices.SerialPortManage;
import com.runchen.android.native_runchen.helper.AudioHelper;
import com.runchen.android.native_runchen.helper.ToastHelper;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * NativeRunchenPlugin
 */
public class NativeRunchenPlugin implements FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel methodChannel;
    private EventChannel eventChannel;
    private EventChannel.EventSink eventSink = null;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        AudioHelper.getInstance().init(flutterPluginBinding.getApplicationContext());
        ToastHelper.getInstance().init(flutterPluginBinding.getApplicationContext());
        methodChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "native_runchen");
        methodChannel.setMethodCallHandler(this);

        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "native_runchen_event");
        eventChannel.setStreamHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("audio.setVoiceStep")) {
            String step = call.argument("step");
            AudioHelper.getInstance().setVoiceStep(Integer.valueOf(step));
        } else if (call.method.equals("audio.getVolume")) {
            int volume = AudioHelper.getInstance().getVolume();
            result.success(volume);
        } else if (call.method.equals("audio.setVolume")) {
            String num = call.argument("num");
            int volume = AudioHelper.getInstance().setVolume(Integer.valueOf(num));
            result.success(volume);
        } else if (call.method.equals("audio.volumeUp")) {
            int volume = AudioHelper.getInstance().volumeUp();
            result.success(volume);
        } else if (call.method.equals("audio.volumeDown")) {
            int volume = AudioHelper.getInstance().volumeDown();
            result.success(volume);
        } else if (call.method.equals("audio.muteOn")) {
            AudioHelper.getInstance().muteOn();
        } else if (call.method.equals("audio.muteOff")) {
            AudioHelper.getInstance().muteOff();
        } else if (call.method.equals("toast.showMessage")) {
            String message = call.argument("message");
            ToastHelper.getInstance().showMessage(message);
        } else if (call.method.equals("i2c.read")) {
            String fileName = call.argument("fileName");
            int length = Integer.valueOf(call.argument("length"));
            int[] data = I2CManage.getInstance().read(fileName, length);
            result.success(data);
        } else if (call.method.equals("i2c.write")) {
            String fileName = call.argument("fileName");
            String data = call.argument("data");
            I2CManage.getInstance().write(fileName, data);
        } else if (call.method.equals("serialPort.open")) {
//            int mode = call.argument("mode");
            String fileName = call.argument("fileName");
            int baudRate = Integer.valueOf(call.argument("baudRate"));
            int dataBits = call.argument("dataBits");
            String parity = call.argument("parity");
            int stopBits = call.argument("stopBits");
            SerialPortManage.getInstance().openSerialPort(0, fileName, baudRate, dataBits, parity.charAt(0), stopBits);
        } else if (call.method.equals("serialPort.sendData")) {
            String fileName = call.argument("fileName");
            String data = call.argument("data");
            SerialPortManage.getInstance().sendData(fileName, data);
        } else if (call.method.equals("serialPort.close")) {
            String fileName = call.argument("fileName");
            SerialPortManage.getInstance().close(fileName);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        methodChannel.setMethodCallHandler(null);
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        eventSink = events;
        SerialPortManage.getInstance().onListen(this.eventSink);
    }

    @Override
    public void onCancel(Object arguments) {
        eventSink = null;
        SerialPortManage.getInstance().onListen(null);
    }

}
