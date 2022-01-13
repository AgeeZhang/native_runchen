import 'dart:async';

import 'package:flutter/services.dart';

class NativeRunchen {
  static const MethodChannel _methodchannel = MethodChannel('native_runchen');
  static const EventChannel _eventChannel =
      EventChannel("native_runchen_event");

  // 获取平台版本用于验证插件是否加载
  static Future<String?> get platformVersion async {
    final String? version =
        await _methodchannel.invokeMethod('getPlatformVersion');
    return version;
  }

  // 设置音量步长（按最大值100算
  static Future<dynamic> setVoiceStep(int? step) async {
    _methodchannel.invokeMethod('audio.setVoiceStep', {"step": step});
  }

  // 获取当前设备音量
  static Future<int?> get getVolume async {
    final int? volume = await _methodchannel.invokeMethod('getVolume');
    return volume;
  }

  // 设置音量 最大值100
  static Future<int?> setVolume(int? num) async {
    final int? volume =
        await _methodchannel.invokeMethod('audio.setVolume', {"num": num});
    return volume;
  }

  // 控制音量增大
  static Future<int?> get volumeUp async {
    final int? volume = await _methodchannel.invokeMethod('audio.volumeUp');
    return volume;
  }

  // 控制音量减小
  static Future<int?> get volumeDown async {
    final int? volume = await _methodchannel.invokeMethod('audio.volumeDown');
    return volume;
  }

  // 开启静音
  static Future<dynamic> get muteOn async {
    _methodchannel.invokeMethod('audio.muteOn');
  }

  // 关闭静音
  static Future<dynamic> get muteOff async {
    _methodchannel.invokeMethod('audio.muteOff');
  }

  // 显示Toast提醒框
  static Future<dynamic> showMessage(String? message) async {
    _methodchannel.invokeMethod('toast.showMessage', {"message": message});
  }

  // i2c数据读取
  static Future<String?> i2cRead(String? fileName, int? length) async {
    final String? data = await _methodchannel
        .invokeMethod('i2c.read', {"fileName": fileName, "length": length});
    return data;
  }

  // i2c数据写入
  static Future<dynamic> i2cWrite(String? fileName, String? data) async {
    _methodchannel
        .invokeMethod('i2c.write', {"fileName": fileName, "data": data});
  }

  // 打开串口通讯
  static Future<dynamic> openSerialPort(String? fileName, String? baudRate,
      int? dataBits, String? parity, int? stopBits) async {
    _methodchannel.invokeMethod('serialPort.open', {
      "fileName": fileName,
      "baudRate": baudRate,
      "dataBits": dataBits,
      "parity": parity,
      "stopBits": stopBits
    });
  }

  // 关闭串口通讯
  static Future<dynamic> closeSerialPort(String? fileName) async {
    _methodchannel.invokeMethod('serialPort.close', {"fileName": fileName});
  }

  // 向串口发送数据
  static Future<dynamic> serialPortSend(String? fileName, String? data) async {
    _methodchannel.invokeMethod('serialPort.sendData', {"fileName": fileName, "data": data});
  }

  //用于接收改插件的所有事件消息
  //数据格式json
  //type 通讯类型
  //device 设备名称
  //data  数据体
  static void onListenStreamData(onEvent, onError) {
    _eventChannel.receiveBroadcastStream().listen(onEvent, onError: onError);
  }
}
