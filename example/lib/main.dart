import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:native_runchen/native_runchen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _deviceName = '';
  String _baudRate = '9600';
  String _i2cDeviceName = '';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await NativeRunchen.platformVersion ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            Text('Running on: $_platformVersion\n'),
            Row(
              crossAxisAlignment:CrossAxisAlignment.center,
              mainAxisAlignment:MainAxisAlignment.spaceAround,
              children: [
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('音量加'),
                onPressed: (){
                    NativeRunchen.volumeUp;
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('音量减'),
                onPressed: (){
                    NativeRunchen.showMessage("test");
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('静音'),
                onPressed: (){
                    NativeRunchen.muteOn;
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('取消静音'),
                onPressed: (){
                    NativeRunchen.muteOff;
                })
              ]
            ),
            TextField(
              style: const TextStyle(color: Color(0xFFA7ABBB),fontSize: 15),
              controller: TextEditingController.fromValue(TextEditingValue(
                  text: _deviceName,
                  selection: TextSelection.fromPosition(TextPosition(
                      affinity: TextAffinity.downstream,
                      offset: _deviceName.length)
                  ))
              ),
              decoration: InputDecoration(
                counterText: '',
                filled: true,
                fillColor: Color(0xFF1A1A1A),
                hintStyle: const TextStyle(color: Color(0xFFA7ABBB),fontSize: 15),
                hintText: '请输入串口设备名称',
                contentPadding: EdgeInsets.symmetric(horizontal: 15,vertical: 10),
                enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
                focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
              ),
              onChanged: (v){
                _deviceName = v;
                setState(() { });
              }
            ),
            TextField(
              style: const TextStyle(color: Color(0xFFA7ABBB),fontSize: 15),
              controller: TextEditingController.fromValue(TextEditingValue(
                  text: _baudRate,
                  selection: TextSelection.fromPosition(TextPosition(
                      affinity: TextAffinity.downstream,
                      offset: _baudRate.length)
                  ))
              ),
              decoration: InputDecoration(
                counterText: '',
                filled: true,
                fillColor: Color(0xFF1A1A1A),
                hintStyle: const TextStyle(color: Color(0xFFA7ABBB),fontSize: 15),
                hintText: '请输入波特率',
                contentPadding: EdgeInsets.symmetric(horizontal: 15,vertical: 10),
                enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
                focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
              ),
              onChanged: (v){
                _baudRate = v;
                setState(() { });
              }
            ),
            Row(
              crossAxisAlignment:CrossAxisAlignment.center,
              mainAxisAlignment:MainAxisAlignment.spaceAround,
              children: [
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('打开串口'),
                onPressed: (){
                    NativeRunchen.openSerialPort(_deviceName, _baudRate, 8, 'n', 1);
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('关闭串口'),
                onPressed: (){
                    NativeRunchen.closeSerialPort(_deviceName);
                })
              ],
            ),
            TextField(
              style: const TextStyle(color: Color(0xFFA7ABBB),fontSize: 15),
              controller: TextEditingController.fromValue(TextEditingValue(
                  text: _i2cDeviceName,
                  selection: TextSelection.fromPosition(TextPosition(
                      affinity: TextAffinity.downstream,
                      offset: _i2cDeviceName.length)
                  ))
              ),
              decoration: InputDecoration(
                counterText: '',
                filled: true,
                fillColor: Color(0xFF1A1A1A),
                hintStyle: const TextStyle(color: Color(0xFFA7ABBB),fontSize: 15),
                hintText: '请输入I2C设备名称',
                contentPadding: EdgeInsets.symmetric(horizontal: 15,vertical: 10),
                enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
                focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
              ),
              onChanged: (v){
                _i2cDeviceName = v;
                setState(() { });
              }
            ),
            Row(
              crossAxisAlignment:CrossAxisAlignment.center,
              mainAxisAlignment:MainAxisAlignment.spaceAround,
              children: [
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('设备写入'),
                onPressed: (){
                    NativeRunchen.i2cWrite(_i2cDeviceName, "0xFF");
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('设备读取'),
                onPressed: (){
                     NativeRunchen.i2cRead(_i2cDeviceName, 1);
                })
              ],
            )
          ],
        ),
      ),
    );
  }
}
