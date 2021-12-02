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
              }),
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
            ],
        ),
      ),
    );
  }
}
