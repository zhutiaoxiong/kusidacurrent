package com.kulala.staticsfunc.static_assistant;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.kulala.staticsfunc.BuildConfig;

import java.util.HashMap;

public class SoundHelper {
    private static SoundPool                 soundPool;
    private static HashMap<Integer, Integer> voiceList;
    private static long prePlayTime;
    private static int prePlayId;
    public static void playSoundPool(Context context, int resId) {
        if(resId  == 0)return;//无此声音
        if (voiceList == null) {
            voiceList = new HashMap<Integer, Integer>();
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);//设置音频流的合适属性
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(500)
                    .setAudioAttributes(attrBuilder.build())
                    .build();
        }
        Integer val = voiceList.get(resId);
        if (val == null) {
            int soundID = soundPool.load(context, resId, 10);
            voiceList.put(resId, soundID);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    long now = System.currentTimeMillis();
                    if(now - prePlayTime < 10000L && prePlayId!=0){
                         Log.e("SoundHelper","stop:"+prePlayId);
                        soundPool.stop(prePlayId);
                    }
                    prePlayId = soundPool.play(sampleId, 1, 1, 1, 0, 1);
                    prePlayTime = now;
                      Log.e("SoundHelper","play:"+sampleId+" return:"+prePlayId);
                }
            });
        }else{
            long now = System.currentTimeMillis();
            if(now - prePlayTime < 10000L && prePlayId!=0){
                 Log.e("SoundHelper","stop:"+prePlayId);
                soundPool.stop(prePlayId);
            }
            prePlayId = soundPool.play(val, 1, 1, 1, 0, 1);
            prePlayTime = now;
             if (BuildConfig.DEBUG) Log.e("SoundHelper","play:"+val+" return:"+prePlayId);
        }
    }
}
