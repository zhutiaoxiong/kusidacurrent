package common.map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;


//import com.iflytek.cloud.ErrorCode;
//import com.iflytek.cloud.InitListener;
//import com.iflytek.cloud.SpeechConstant;
//import com.iflytek.cloud.SpeechError;
//import com.iflytek.cloud.SpeechSynthesizer;
//import com.iflytek.cloud.SpeechUtility;
//import com.iflytek.cloud.SynthesizerListener;

import java.io.File;

/**
 * 百度地图之全屏地图,导航功能
 * BNRoutePlanNode startNode = new BNRoutePlanNode(113.315207, 23.107005, "测开始", null, BNRoutePlanNode.CoordinateType.BD09LL);
 * BNRoutePlanNode endNode = new BNRoutePlanNode(113.355207, 23.118205, "测结束", null, BNRoutePlanNode.CoordinateType.BD09LL);
 * Intent intent = new Intent(getContext(), ActivityNaviMap.class);
 * Bundle bundle = new Bundle();
 * bundle.putSerializable(ActivityNaviMap.START_NODE,startNode);
 * bundle.putSerializable(ActivityNaviMap.END_NODE,endNode);
 * intent.putExtras(bundle);
 * getContext().startActivity(intent);
 */
public class ActivityNaviMap extends Activity {
    public static String START_NODE = "START_NODE";
    public static String END_NODE   = "END_NODE";
//    private BNRoutePlanNode startNode;
//    private BNRoutePlanNode endNode;

    private static String NAVI_FOLDER_DIR = "NAVI_FOLDER";
    private        String mSDCardPath     = null;

//    private BaiduNaviCommonModule baiduNaviCommonModule;
    //==========================================================================
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        BNOuterLogUtil.setLogSwitcher(true);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                startNode = (BNRoutePlanNode) bundle.getSerializable(START_NODE);
//                endNode = (BNRoutePlanNode) bundle.getSerializable(END_NODE);
//            }
        }
//        if (!LoadPermissions.isOpenGps(getApplicationContext())) {
//            LoadPermissions.openGPS(getApplicationContext());
//        }
//        LoadPermissions.getInstance().requestWriteSettings(this);
//        SpeechUtility.createUtility(ActivityNaviMap.this,"appid="+"58ef3d68");
//        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
//        if (xfTTs == null) {
//            xfTTs = SpeechSynthesizer.createSynthesizer(GlobalContext.getContext().getApplicationContext(), new InitListener() {
//                @Override
//                public void onInit(int code) {
//                    if (code != ErrorCode.SUCCESS) {
//                        showToastMsg("初始化失败,错误码：" + code);
//                    } else {
//                        // 初始化成功，之后可以调用startSpeaking方法
//                        // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
//                        // 正确的做法是将onCreate中的startSpeaking调用移至这里
//                        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
//                        xfTTs.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
//                        xfTTs.setParameter(SpeechConstant.SPEED, "50");//设置语速
//                        xfTTs.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
//                        xfTTs.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
//                        //如果不需要保存合成音频，注释该行代码
//                        xfTTs.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
//                    }
//                }
//            });
//
//            if(SpeechSynthesizer.getSynthesizer()!=null)
//                xfTTs = SpeechSynthesizer.getSynthesizer();
//        }
        initDir();
        initSetting();
        initNavi();
    }
    private void initDir() {
        mSDCardPath = getExternalFilesDir(null).getAbsolutePath();
        File f = new File(mSDCardPath, NAVI_FOLDER_DIR);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void initSetting() {
//        //日夜模式 自动
//        BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_AUTO);
//        // 设置导航播报模式 Veteran老手模式 Novice新手模式
//        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Novice);
//        // 是否开启路况
//        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
//        // 设置是否显示路况开关
//        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
    }
    private void initNavi() {
//        BaiduNaviManager.getInstance().init(this, mSDCardPath, NAVI_FOLDER_DIR, new BaiduNaviManager.NaviInitListener() {
//            @Override
//            public void onAuthResult(int status, String msg) {
//                //错误提示
//                final String authinfo = (0 == status) ? "key校验成功!" : "key校验失败, " + msg;
//                ActivityNaviMap.this.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Toast.makeText(ActivityNaviMap.this, authinfo, Toast.LENGTH_LONG).show();
//                    }
//                });
            }
            public void initSuccess() {
//                Toast.makeText(ActivityNaviMap.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
//                ActivityNaviMap.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        startNavi();
////                        initView();
//                    }
//                });
            }
            public void initStart() {
//                Toast.makeText(ActivityNaviMap.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }
            public void initFailed() {
//                Toast.makeText(ActivityNaviMap.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
//                ActivityNaviMap.this.finish();
//            }
//        }, mTTSCallback, ttsHandler, ttsPlayStateListener);//后三个为语音参数
    }
    private void initView() {
//        //使用通用接口
//        View view = null;
//        OnNavigationListener onNavigationListener = new OnNavigationListener() {
//            @Override
//            public void onNaviGuideEnd() {
//                //退出导航
//                finish();
//            }
//            @Override
//            public void notifyOtherAction(int actionType, int i1, int i2, Object o) {
//                if (actionType == 0) {//导航到达目的地 自动退出
//                }
//            }
//        };
//        baiduNaviCommonModule = NaviModuleFactory.getNaviModuleManager().getNaviCommonModule(
//                NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE, this,
//                BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE, onNavigationListener);
//        if (baiduNaviCommonModule != null) {
//            baiduNaviCommonModule.onCreate();
//            view = baiduNaviCommonModule.getView();
//            if (view == null)
//                view = BNRouteGuideManager.getInstance().onCreate(this, onNavigationListener);
//        }
//        if (view == null) {
//            Toast.makeText(ActivityNaviMap.this, "获取不到地图", Toast.LENGTH_SHORT).show();
//            ActivityNaviMap.this.finish();
//            return;//获取不到地图
//        }
//        setContentView(view);
    }
    //==========================================================================
    public void startNavi() {
//        if (BaiduNaviManager.isNaviInited()) {
//            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
//            list.add(startNode);
//            list.add(endNode);
//            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(endNode));
//        }
    }

//    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {
//        private BNRoutePlanNode mBNRoutePlanNode = null;
//        public DemoRoutePlanListener(BNRoutePlanNode node) {
//            mBNRoutePlanNode = node;
//        }
//        @Override
//        public void onJumpToNavigator() {//设置途径点以及resetEndNode会回调该接口
//            initView();
//        }
//        @Override
//        public void onRoutePlanFailed() {
//            // TODO Auto-generated method stub
//            Toast.makeText(ActivityNaviMap.this, "算路失败", Toast.LENGTH_SHORT).show();
//            ActivityNaviMap.this.finish();
//        }
//    }
    //==========================================================================

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) finish();
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onStart() {
        super.onStart();
//        if (baiduNaviCommonModule != null) baiduNaviCommonModule.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
//        if (baiduNaviCommonModule != null) baiduNaviCommonModule.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
//        if (baiduNaviCommonModule != null) baiduNaviCommonModule.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
//        if (baiduNaviCommonModule != null) baiduNaviCommonModule.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (baiduNaviCommonModule != null) baiduNaviCommonModule.onDestroy();
//        if (xfTTs != null) xfTTs.destroy();
//        xfTTs = null;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (baiduNaviCommonModule != null) baiduNaviCommonModule.onConfigurationChanged(newConfig);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (baiduNaviCommonModule != null) baiduNaviCommonModule.onBackPressed(false);
    }
    //===============================tts========================================
//    private SpeechSynthesizer xfTTs;
//    private BNOuterTTSPlayerCallback              mTTSCallback         = new BNOuterTTSPlayerCallback() {
//
//        @Override
//        public void stopTTS() {
//            // TODO Auto-generated method stub
////            textToSpeech.shutdown();
//            if (xfTTs != null) xfTTs.stopSpeaking();
//        }
//
//        @Override
//        public void resumeTTS() {
//            // TODO Auto-generated method stub
//            if (xfTTs != null) xfTTs.resumeSpeaking();
//        }
//
//        @Override
//        public void releaseTTSPlayer() {
//            // TODO Auto-generated method stub
//             if (BuildConfig.DEBUG) Log.e("test_TTS", "releaseTTSPlayer");
//        }
//
//        @Override
//        public int playTTSText(String speech, int bPreempt) {
//            // TODO Auto-generated method stub
//            if(xfTTs!=null)xfTTs.startSpeaking(speech, new SynthesizerListener() {
//                @Override
//                public void onSpeakBegin() {
//
//                }
//                @Override
//                public void onBufferProgress(int i, int i1, int i2, String s) {
//
//                }
//                @Override
//                public void onSpeakPaused() {
//
//                }
//                @Override
//                public void onSpeakResumed() {
//
//                }
//                @Override
//                public void onSpeakProgress(int i, int i1, int i2) {
//
//                }
//                @Override
//                public void onCompleted(SpeechError speechError) {
//                    showToastMsg(speechError.toString());
//                }
//                @Override
//                public void onEvent(int i, int i1, int i2, Bundle bundle) {
//
//                }
//            });
//            return 1;
//        }
//
//        @Override
//        public void phoneHangUp() {
//            // TODO Auto-generated method stub
//             if (BuildConfig.DEBUG) Log.e("test_TTS", "phoneHangUp openTTs");
//            if (xfTTs != null) xfTTs.resumeSpeaking();
//        }
//
//        @Override
//        public void phoneCalling() {
//            // TODO Auto-generated method stub
//            if (xfTTs != null) xfTTs.pauseSpeaking();
//        }
//
//        @Override
//        public void pauseTTS() {
//            // TODO Auto-generated method stub
//        }
//
//        @Override
//        public void initTTSPlayer() {
//            // TODO Auto-generated method stub
//        }
//
//        @Override
//        public int getTTSState() {
//            // TODO Auto-generated method stub
//            return 1;
//        }
//    };
    /**
     * 内部TTS播报状态回传handler
     */
//    private Handler                               ttsHandler           = new Handler() {
//        public void handleMessage(Message msg) {
//            int type = msg.what;
//            switch (type) {
//                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
//                    showToastMsg("Handler : TTS play start");
//                    break;
//                }
//                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
//                    showToastMsg("Handler : TTS play end");
//                    break;
//                }
//                default:
//                    break;
//            }
//        }
//    };
    /**
     * 内部TTS播报状态回调接口
     */
//    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {
//
//        @Override
//        public void playEnd() {
//            showToastMsg("TTSPlayStateListener : TTS play end");
//        }
//
//        @Override
//        public void playStart() {
//            showToastMsg("TTSPlayStateListener : TTS play start");
//        }
//    };
    public void showToastMsg(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(ActivityNaviMap.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //==========================================================================

}
