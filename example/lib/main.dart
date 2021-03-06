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
  String _deviceName = '/dev/ttyS4';
  String _baudRate = '57600';
  String _sendData = 'EF01FFFFFFFF010003350039';
  String _message = '';
  String _i2cDeviceName = '';

  @override
  void initState() {
    super.initState();
    initPlatformState();
    _onReceiveEventData();
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

  void _onReceiveEventData() {
    NativeRunchen.onListenStreamData((data) {
      setState(() {
          _message = data;
      });
    }, (error) {
      print("event channel error : $error");
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
                child: new Text('?????????'),
                onPressed: (){
                    NativeRunchen.volumeUp;
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('?????????'),
                onPressed: (){
                    NativeRunchen.showMessage("test");
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('??????'),
                onPressed: (){
                    NativeRunchen.muteOn;
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('????????????'),
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
                hintText: '???????????????????????????',
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
                hintText: '??????????????????',
                contentPadding: EdgeInsets.symmetric(horizontal: 15,vertical: 10),
                enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
                focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
              ),
              onChanged: (v){
                _baudRate = v;
                setState(() { });
              }
            ),
            TextField(
              style: const TextStyle(color: Color(0xFFA7ABBB),fontSize: 15),
              controller: TextEditingController.fromValue(TextEditingValue(
                  text: _sendData,
                  selection: TextSelection.fromPosition(TextPosition(
                      affinity: TextAffinity.downstream,
                      offset: _sendData.length)
                  ))
              ),
              decoration: InputDecoration(
                counterText: '',
                filled: true,
                fillColor: Color(0xFF1A1A1A),
                hintStyle: const TextStyle(color: Color(0xFFA7ABBB),fontSize: 15),
                hintText: '?????????????????????',
                contentPadding: EdgeInsets.symmetric(horizontal: 15,vertical: 10),
                enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
                focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
              ),
              onChanged: (v){
                _sendData = v;
                setState(() { });
              }
            ),
            TextField(
              style: const TextStyle(color: Color(0xFFA7ABBB),fontSize: 15),
              controller: TextEditingController.fromValue(TextEditingValue(
                  text: _message,
                  selection: TextSelection.fromPosition(TextPosition(
                      affinity: TextAffinity.downstream,
                      offset: _message.length)
                  ))
              ),
              decoration: InputDecoration(
                counterText: '',
                filled: true,
                fillColor: Color(0xFF1A1A1A),
                hintStyle: const TextStyle(color: Color(0xFFA7ABBB),fontSize: 15),
                hintText: '????????????',
                contentPadding: EdgeInsets.symmetric(horizontal: 15,vertical: 10),
                enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
                focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(6),borderSide: BorderSide.none),
              ),
              onChanged: (v){
                _message = v;
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
                child: new Text('????????????'),
                onPressed: (){
                    NativeRunchen.openSerialPort(_deviceName, _baudRate, 8, 'n', 1);
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('????????????'),
                onPressed: (){
                    NativeRunchen.closeSerialPort(_deviceName);
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('????????????'),
                onPressed: (){
                    NativeRunchen.serialPortSend(_deviceName,_sendData);
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
                hintText: '?????????I2C????????????',
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
                child: new Text('????????????'),
                onPressed: (){
                    NativeRunchen.i2cWrite(_i2cDeviceName, "0xFF");
                }),
                MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: new Text('????????????'),
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
