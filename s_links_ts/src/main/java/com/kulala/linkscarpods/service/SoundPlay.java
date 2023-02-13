package com.kulala.linkscarpods.service;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.Log;


import com.kulala.linkspods.BuildConfig;
import com.kulala.linkspods.R;

import java.util.HashMap;
import java.util.Iterator;

public class SoundPlay {
    private static SoundPool                 pool;
    private static HashMap<Integer, Integer> voiceList;
    private static Vibrator                  vibrator;
    private static SoundPlay _instance;
    public static SoundPlay getInstance() {
        if (_instance == null)
            _instance = new SoundPlay();
        return _instance;
    }
    // =========================================
    public void init(Context context) {
        if (pool != null) return;
        if(voiceList != null)return;
        voiceList = new HashMap<Integer, Integer>();
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);//设置音频流的合适属性
        pool = new SoundPool.Builder()
                .setMaxStreams(80)
                .setAudioAttributes(attrBuilder.build())
                .build();
//        pool = new SoundPool(80, AudioManager.STREAM_MUSIC, 5);
        voiceList.put(1,pool.load(context, R.raw.voice_01_startcar, 10));
        voiceList.put(2,pool.load(context, R.raw.voice_02_openbackpag, 10));
        voiceList.put(3,pool.load(context, R.raw.voice_03_lock, 10));
        voiceList.put(4,pool.load(context, R.raw.voice_04_unlock, 10));
        voiceList.put(5,pool.load(context, R.raw.voice_05_stopcar, 10));
        voiceList.put(6,pool.load(context, R.raw.voice_06_lockfail_lfdoorfail, 10));
        voiceList.put(7,pool.load(context, R.raw.voice_07_lockfail_rfdoorfail, 10));
        voiceList.put(8,pool.load(context, R.raw.voice_08_lockfail_lbdoorfail, 10));
        voiceList.put(9,pool.load(context, R.raw.voice_09_lockfail_rbdoorfail, 10));
        voiceList.put(10,pool.load(context, R.raw.voice_10_lockfail_innerlightfail, 10));
        voiceList.put(11,pool.load(context, R.raw.voice_11_lockfail_backpagfail, 10));
        voiceList.put(12,pool.load(context, R.raw.voice_12_lockok_innerlightfail, 10));
        voiceList.put(13,pool.load(context, R.raw.voice_13_lockok_backpagfail, 10));
        voiceList.put(14,pool.load(context, R.raw.voice_14_start_fail, 10));
        voiceList.put(15,pool.load(context, R.raw.voice_15_stopcar_fail, 10));
        voiceList.put(16,pool.load(context, R.raw.voice_16_areainto, 10));
        voiceList.put(17,pool.load(context, R.raw.voice_17_areaout, 10));
        voiceList.put(18,pool.load(context, R.raw.voice_18_toofast, 10));
        voiceList.put(19,pool.load(context, R.raw.voice_19_lowpower, 10));

        voiceList.put(21,pool.load(context, R.raw.voice_21_uclock_car, 10));
        voiceList.put(22,pool.load(context, R.raw.voice_22_window_lf_unclose, 10));
        voiceList.put(23,pool.load(context, R.raw.voice_23_window_rf_unclose, 10));
        voiceList.put(24,pool.load(context, R.raw.voice_24_window_lb_unclose, 10));
        voiceList.put(25,pool.load(context, R.raw.voice_25_window_rb_unclose, 10));
        voiceList.put(26,pool.load(context, R.raw.voice_26_cutline, 10));
        voiceList.put(27,pool.load(context, R.raw.voice_27_carmove, 10));
        voiceList.put(28,pool.load(context, R.raw.voice_28_dooropen_w, 10));
        voiceList.put(29,pool.load(context, R.raw.voice_29_backpag_open_unconfirm, 10));
        voiceList.put(30,pool.load(context, R.raw.voice_30_startcar_unpermission, 10));
        voiceList.put(31,pool.load(context, R.raw.voice_31_car_online_off, 10));
        voiceList.put(32,pool.load(context, R.raw.voice_32_car_unstoped, 10));
        voiceList.put(33,pool.load(context, R.raw.voice_33_backpag_close, 10));
        voiceList.put(34,pool.load(context, R.raw.voice_34_power_unclose, 10));
//        pool.load(context, R.raw.voice_35_skylight_open, 10);////35 36声音去掉
//        pool.load(context, R.raw.voice_36_skylight_close, 10);////35 36声音去掉
        voiceList.put(37,pool.load(context, R.raw.voice_37_backpag_unclose, 10));
        voiceList.put(39,pool.load(context, R.raw.voice_39_skylight_unclose, 10));
        voiceList.put(40,pool.load(context, R.raw.voice_40_besure_car_window_close, 10));
        voiceList.put(41,pool.load(context, R.raw.lockfail_power_not_close, 10));
        voiceList.put(60,pool.load(context, R.raw.voice_start, 10));
        voiceList.put(61,pool.load(context, R.raw.voice_lock, 10));
        voiceList.put(62,pool.load(context, R.raw.voice_backpag, 10));
        voiceList.put(63,pool.load(context, R.raw.voice_findcar, 10));
        voiceList.put(64,pool.load(context, R.raw.voice_warrning, 10));

        voiceList.put(70,pool.load(context, R.raw.voice_lock, 10));//for keep
         if (BuildConfig.DEBUG) Log.e("SoundPlay","init3 voiceList size:"+voiceList.size());

    }

    public void play_start(Context context) {
        playSoundById(context,60);
    }

    public void play_lock(Context context) {
        playSoundById(context,61);
    }

    public void play_backpag(Context context) {
        playSoundById(context,62);
//        if(pool!=null)pool.play(62, 1, 1, 1, 0, 1);
    }

    public void play_findcar(Context context) {
        playSoundById(context,63);
//        if(pool!=null)pool.play(63, 1, 1, 1, 0, 1);
    }

    public void play_warrning(Context context) {
        playSoundById(context,64);
//        if(pool!=null)pool.play(64, 1, 1, 1, 0, 1);
    }
    /*
     * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
     * */
    public void playVibrator(Context context) {
         if (BuildConfig.DEBUG) Log.e("openVibrator", "context == null:"+(context == null));
        if (context == null) return;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(400L);//重复两次上面的pattern 如果只想震动一次，index设为-1
         if (BuildConfig.DEBUG) Log.e("openVibrator", "已经执行 vibrator");
    }
    public void playSoundById(Context context, int alertId) {
         if (BuildConfig.DEBUG) Log.e("------------", "playSoundById:"+alertId);
         if (BuildConfig.DEBUG) Log.e("------------", "voiceList:"+voiceList+"pool"+pool);
        if(voiceList == null || pool == null)return;
        Iterator iter = voiceList.keySet().iterator();
        while (iter.hasNext()) {
            int key = (Integer) iter.next();
            if (key == alertId) {
                int val = voiceList.get(key);
                 if (BuildConfig.DEBUG) Log.e("------------", "playSoundById:"+val+"pool"+pool);
                if(pool!=null)pool.play(val, 1, 1, 1, 0, 1);
                return;
            }
        }
    }
    public void playKeepVoice(Context context){
        if(voiceList == null || pool == null)return;
        //播放方式错，无效
        Iterator iter = voiceList.keySet().iterator();
        while (iter.hasNext()) {
            int key = (Integer) iter.next();
            if (key == 70) {
                int val = voiceList.get(key);
                pool.play(val, 0.001f, 0.001f, 1, 0, 1);
                return;
            }
        }
    }

    public void stopMediaSoundKeep(Context context) {
//        if(mediaPlayerH!=null && mediaPlayerH.isPlaying()){
//            mediaPlayerH.pause();
//        }
        if(mediaPlayerL!=null && mediaPlayerL.isPlaying()){
            mediaPlayerL.pause();
        }
//         if (BuildConfig.DEBUG) Log.e("<ServiceA>", "<<<<<stopMediaSoundKeep>>>>>>");
    }
//    String defaultPlay= "播放声音";
//    String defaultVolumn = "静音音量";
//    public void setMediaSoundKeep(String control) {//设定时都是在主页，声音都是关了
//        if(control == null)return;
//        if(control.equals("静音音量")){
//            defaultVolumn = "静音音量";
//        }else if(control.equals("正常音量")){
//            defaultVolumn = "正常音量";
//        }else if(control.equals("播放声音")){
//            defaultPlay = "播放声音";
//        }else if(control.equals("不播声音")){
//            defaultPlay = "不播声音";
//        }
//    }
//    private MediaPlayer mediaPlayerH;
    private MediaPlayer mediaPlayerL;
    public void playMediaSoundKeep(Context context) {
//        if(defaultPlay.equals("不播声音"))return;
//        if(defaultVolumn.equals("正常音量")) {
//            if (mediaPlayerH == null) {
//                mediaPlayerH = new MediaPlayer();
//                mediaPlayerH = MediaPlayer.create(context, R.raw.jameslalasong);
//                mediaPlayerH.setLooping(true);
//                mediaPlayerH.setVolume(1f, 1f);
//                mediaPlayerH.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayerH.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        mediaPlayerH.seekTo(0);
//                    }
//                });
//                mediaPlayerH.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        mediaPlayerH.seekTo(0);
//                        mediaPlayerH.start();
//                    }
//                });
//                mediaPlayerH.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                    @Override
//                    public boolean onError(MediaPlayer mp, int what, int extra) {
//                        return false;
//                    }
//                });
//                mediaPlayerH.start();
//                 if (BuildConfig.DEBUG) Log.e("<ServiceA>", "<<<<<playMediaSoundKeepHF>>>>>>");
//            } else {
//                if (!mediaPlayerH.isPlaying()) {
//                     if (BuildConfig.DEBUG) Log.e("<ServiceA>", "<<<<<playMediaSoundKeepHN>>>>>>");
//                    mediaPlayerH.start();
//                }
//            }
//        }else if(defaultVolumn.equals("静音音量")){
            if (mediaPlayerL == null) {
                mediaPlayerL = new MediaPlayer();
                mediaPlayerL = MediaPlayer.create(context, R.raw.slience);
                mediaPlayerL.setLooping(true);
                mediaPlayerL.setVolume(0.01f, 0.01f);
                mediaPlayerL.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayerL.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayerL.seekTo(0);
                    }
                });
                mediaPlayerL.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayerL.seekTo(0);
                        mediaPlayerL.start();
                    }
                });
                mediaPlayerL.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return false;
                    }
                });
                mediaPlayerL.start();
            } else if (!mediaPlayerL.isPlaying()) {
                mediaPlayerL.start();
            }

//        }
    }

}





