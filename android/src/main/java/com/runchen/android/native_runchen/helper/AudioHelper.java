package com.runchen.android.native_runchen.helper;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class AudioHelper {

    private final String TAG = "AudioHelper";
    private volatile static AudioHelper instance;
    private AudioManager mAudioManager;
    private Context context;
    private int VOICE_STEP = 2;

    private AudioHelper(){

    }

    public static AudioHelper getInstance() {
        if (instance == null) {
            instance = new AudioHelper();
        }
        return instance;
    }

    private int getSystemMaxVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    private int getSystemCurrentVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void init(Context context){
        if(mAudioManager == null) {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        this.context = context;
    }

    public void setVoiceStep(int step) {
        VOICE_STEP = step;
    }

    public int getVolume(){
        return 100*getSystemCurrentVolume()/getSystemMaxVolume();
    }

    public int setVolume(int num){
        if(mAudioManager!=null) {
            int a = (int) Math.ceil((num) * getSystemMaxVolume() * 0.01);
            a = a <= 0 ? 0 : a;
            a = a >= 100 ? 100 : a;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, a, 0);
            return getVolume();
        }
        return 0;
    }

    public int volumeUp(){
        if(mAudioManager !=null) {
            int a = (int) Math.ceil((VOICE_STEP + getVolume()) * getSystemMaxVolume() * 0.01);
            a = a <= 0 ? 0 : a;
            a = a >= 100 ? 100 : a;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, a, 0);
            return getVolume();
        }
        return 0;
    }

    public int volumeDown(){
        if(mAudioManager!=null) {
            int a = (int) Math.floor((getVolume() - VOICE_STEP) * getSystemMaxVolume() * 0.01);
            a = a <= 0 ? 0 : a;
            a = a >= 100 ? 100 : a;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, a, 0);
            return getVolume();
        }
        return 0;
    }

    public void muteOn(){
        if(mAudioManager!=null){
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.i(TAG,"开启静音");
        }
    }

    public void muteOff(){
        if(mAudioManager!=null){
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.i(TAG,"取消静音");
        }
    }
}
