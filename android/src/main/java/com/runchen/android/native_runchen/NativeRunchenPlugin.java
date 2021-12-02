package com.runchen.android.native_runchen;

import androidx.annotation.NonNull;

import com.runchen.android.native_runchen.helper.AudioHelper;
import com.runchen.android.native_runchen.helper.ToastHelper;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** NativeRunchenPlugin */
public class NativeRunchenPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    AudioHelper.getInstance().init(flutterPluginBinding.getApplicationContext());
    ToastHelper.getInstance().init(flutterPluginBinding.getApplicationContext());
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "native_runchen");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if(call.method.equals("audio.setVoiceStep")){
      String step = call.argument("step");
      AudioHelper.getInstance().setVoiceStep(Integer.valueOf(step));
    } else if(call.method.equals("audio.getVolume")){
      int volume = AudioHelper.getInstance().getVolume();
      result.success(volume);
    } else if(call.method.equals("audio.setVolume")){
      String num = call.argument("num");
      int volume = AudioHelper.getInstance().setVolume(Integer.valueOf(num));
      result.success(volume);
    } else if(call.method.equals("audio.volumeUp")){
      int volume = AudioHelper.getInstance().volumeUp();
      result.success(volume);
    } else if(call.method.equals("audio.volumeDown")){
      int volume = AudioHelper.getInstance().volumeDown();
      result.success(volume);
    } else if(call.method.equals("audio.muteOn")){
        AudioHelper.getInstance().muteOn();
    } else if(call.method.equals("audio.muteOff")){
        AudioHelper.getInstance().muteOff();
    } else if(call.method.equals("toast.showMessage")){
      String message = call.argument("message");
      ToastHelper.getInstance().showMessage(message);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
