
import 'dart:async';

import 'package:flutter/services.dart';

class NativeRunchen {
  static const MethodChannel _channel = MethodChannel('native_runchen');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<dynamic> setVoiceStep(int? step) async {
    _channel.invokeMethod('audio.setVoiceStep',{"step":step});
  }

  static Future<int?> get getVolume async {
    final int? volume = await _channel.invokeMethod('getVolume');
    return volume;
  }

  static Future<int?> setVolume(int? num) async {
    final int? volume = await _channel.invokeMethod('audio.setVolume',{"num":num});
    return volume;
  }

  static Future<int?> get volumeUp async {
    final int? volume = await _channel.invokeMethod('audio.volumeUp');
    return volume;
  }

  static Future<int?> get volumeDown async {
    final int? volume = await _channel.invokeMethod('audio.volumeDown');
    return volume;
  }

  static Future<dynamic> get muteOn async {
    _channel.invokeMethod('audio.muteOn');
  }

  static Future<dynamic> get muteOff async {
    _channel.invokeMethod('audio.muteOff');
  }

  static Future<dynamic> showMessage(String? message) async {
    _channel.invokeMethod('toast.showMessage',{"message":message});
  }
}
