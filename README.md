# native_runchen

Flutter与Android原生层交互的插件，内容还在持续扩展中.
现有方法 1、音量控制（音量加、音量减、设置音量、获取当前音量、静音、取消静音
        2、Toast 显示方法
        3、串口通讯（串口开启、串口发送、串口读取、串口关闭
        4、i2c通讯接口（i2c写入、i2c读取
## USB2SerialPortManage
If the app should be notified when a device is attached, add device_filter.xml to your project's res/xml/ directory and configure in your AndroidManifest.xml.

<activity
    android:name="..."
    ...>
    <intent-filter>
        <action android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED" />
    </intent-filter>
    <meta-data
        android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED"
        android:resource="@xml/device_filter" />
</activity>



## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.

