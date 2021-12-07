
import 'dart:async';

import 'package:flutter/services.dart';

class NativeRunchen {
  static const MethodChannel _methodchannel = MethodChannel('native_runchen');
  static const EventChannel _eventChannel = EventChannel("native_runchen_event");
  
  static Future<String?> get platformVersion async {
    final String? version = await _methodchannel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<dynamic> setVoiceStep(int? step) async {
    _methodchannel.invokeMethod('audio.setVoiceStep',{"step":step});
  }

  static Future<int?> get getVolume async {
    final int? volume = await _methodchannel.invokeMethod('getVolume');
    return volume;
  }

  static Future<int?> setVolume(int? num) async {
    final int? volume = await _methodchannel.invokeMethod('audio.setVolume',{"num":num});
    return volume;
  }

  static Future<int?> get volumeUp async {
    final int? volume = await _methodchannel.invokeMethod('audio.volumeUp');
    return volume;
  }

  static Future<int?> get volumeDown async {
    final int? volume = await _methodchannel.invokeMethod('audio.volumeDown');
    return volume;
  }

  static Future<dynamic> get muteOn async {
    _methodchannel.invokeMethod('audio.muteOn');
  }

  static Future<dynamic> get muteOff async {
    _methodchannel.invokeMethod('audio.muteOff');
  }

  static Future<dynamic> showMessage(String? message) async {
    _methodchannel.invokeMethod('toast.showMessage',{"message":message});
  }

  static Future<String?> i2cRead(String? fileName,int? length) async {
   final String? data = await _methodchannel.invokeMethod('i2c.read',{"fileName":fileName,"length":length});
   return data;
  }

  static Future<dynamic> i2cWrite(String? fileName,String? data) async {
    _methodchannel.invokeMethod('i2c.write',{"fileName":fileName,"data":data});
  }

  static Future<dynamic> openSerialPort(String? fileName,String? baudRate,int? dataBits,String? parity,int? stopBits) async {
    _methodchannel.invokeMethod('serialPort.open',{"fileName":fileName,"baudRate":baudRate,"dataBits":dataBits,"parity":parity,"stopBits":stopBits});
  }

  static Future<dynamic> closeSerialPort(String? fileName) async {
    _methodchannel.invokeMethod('serialPort.close',{"fileName":fileName});
  }

  static Future<dynamic> serialPortSend(String? fileName) async {
    _methodchannel.invokeMethod('serialPort.sendData',{"fileName":fileName});
  }

  /**
   * 数据格式json
   * type 通讯类型
   * device 设备路径
   * data  数据体
   */
  static void onListenStreamData(onEvent, onError) {
    _eventChannel.receiveBroadcastStream().listen(onEvent, onError: onError);
  }
}
