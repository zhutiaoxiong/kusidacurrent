package view.view4me.lcdkey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.linkscarpods.blue.BluePermission;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.titlehead.ClipTitleHead;
import com.kulala.staticsview.toast.ToastTxt;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.LcdInactivityTimer;
import com.zxing.decoding.LcdKeyActivityHandler;
import com.zxing.view.ViewfinderView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import adapter.BindLcdKeyRecycleViewAdapter;
import adapter.RecycleViewDivider;
import common.GlobalContext;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.view4me.myblue.MyNewBlueScanList;

public class BindLcdKeyActivity extends Activity implements SurfaceHolder.Callback, OEventObject {

    private LcdKeyActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private LcdInactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.50f;
    private boolean vibrate;
    private Button cancelScanButton;
    private ClipTitleHead title_head;
    private TextView input_mac_number;
    private Button btn_confirm;
    private RecyclerView recycleview_equipments;
    private BindLcdKeyRecycleViewAdapter bindLcdKeyAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<BluetoothDevice> scanedDevices;
    private long preScanTime = 0;
    private boolean isVisible = false;
    private boolean isScan;//是扫描扫到的
    private BluetoothDevice CacheScanDevice;
    private String[]    arr;
    RecycleViewDivider driver;
    private long clickOpenScanTime;
    private long viewLoadCompleteTime;
    public static BindLcdKeyActivity BindLcdKeyActivityThis;
//    private TextView send_pic;
    @SuppressLint("HandlerLeak")
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindlcdkey);
        GlobalContext.setIsInBindLcdKey(true);
        // ViewUtil.addTopView(getApplicationContext(), this,
        // R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        title_head = (ClipTitleHead) findViewById(R.id.title_head);
        input_mac_number = (TextView) findViewById(R.id.input_mac_number);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        recycleview_equipments = (RecyclerView) findViewById(R.id.recycleview_equipments);
//        send_pic = (TextView) findViewById(R.id.send_pic);
        hasSurface = false;
        inactivityTimer = new LcdInactivityTimer(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recycleview_equipments.setLayoutManager(mLinearLayoutManager);
        driver=  new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(BindLcdKeyActivity.this,1), Color.parseColor("#000000"));
        initEvent();
        scanList();
        ODispatcher.addEventListener(OEventName.LCDKEY_BIND_OPRATION,this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private InputStream getImageStream(){
        AssetManager assetManager=getAssets();
        InputStream inputStream=null;
        try {
            inputStream=assetManager.open("car_logo_1.png");
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 将InputStream转换成byte[]

    public byte[] InputStream2Bytes(InputStream is) {
        String str = "";
        byte[] readByte = new byte[1024];
        int readCount = -1;
        try {
            while ((readCount = is.read(readByte, 0, 1024)) != -1) {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initEvent() {
//        send_pic.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View v) {
//                InputStream inputStream= getImageStream();
//                if(inputStream!=null){
//                    byte[] byebye=  InputStream2Bytes(inputStream);
//                    //十六进制的圖片分包数据
//              final     List<String> list16picdataStr=  BlePacketUtil.writeEntity(byebye);
//                    for (int i=0;i<list16picdataStr.size();i++){
//
//                            try {
//                                 if (BuildConfig.DEBUG) Log.e("byebye", "的值为"+ list16picdataStr.get(i) );
//                                if(MyLcdBlueAdapter.current_blue_state>= OnBlueStateListenerRoll.STATE_CONNECT_OK) {
//                                    MyLcdBlueAdapter.getInstance().sendMessage(list16picdataStr.get(i));//发送成功后才算是解绑成功
//                                }
//                                Thread.sleep(500);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                    }
//                }
//            }
////                if(MyLcdBlueAdapter.current_blue_state>= OnBlueStateListenerRoll.STATE_CONNECT_OK){
////                    InputStream inputStream= getImageStream();
////                    if(inputStream!=null){
////                        byte[] byebye=  InputStream2Bytes(inputStream);
////                    if(byebye!=null){
////                        MyLcdBlueAdapter.getInstance().sendMessage(byebye);//发送成功后才算是解绑成功
////                    }
////                   }
////            }
//        });
        input_mac_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if((viewLoadCompleteTime-clickOpenScanTime)>1000){
                    scanList();
//                }
                clickOpenScanTime=System.currentTimeMillis();
//                MyBlueScanList.getInstance().scanDeviceList(GlobalContext.getContext(), true, listenerList);
                 if (BuildConfig.DEBUG) Log.e("扫描开始", "輸入框時間: "+System.currentTimeMillis() );
                if(scanedDevices!=null&&scanedDevices.size()>0){
                    if (isVisible) {
                        isVisible = false;
                        recycleview_equipments.setVisibility(View.INVISIBLE);
                         if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "隐藏");
                    } else {
                        isVisible = true;
                        recycleview_equipments.setVisibility(View.VISIBLE);
                         if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "显示");
                    }
                }else{
                    recycleview_equipments.setVisibility(View.INVISIBLE);
                }
            }
        });
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                Intent intent = new Intent();
                intent.setClass(BindLcdKeyActivity.this, ActivityLCDkey.class);
                startActivity(intent);
                finish();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                String noMaohaoAdress = input_mac_number.getText().toString();
                String youMaoHaoadress=StringToMacUtil.collapseString(noMaohaoAdress,2,":");
                if (TextUtils.isEmpty(noMaohaoAdress) || noMaohaoAdress.equals("选择液晶钥匙ID")) {
                    return;
                }
                if ( BluetoothAdapter.checkBluetoothAddress(youMaoHaoadress)) {
                    DataCarInfo currentCar= ManagerCarList.getInstance().getCurrentCar();
                    if(currentCar==null)return;
                    OCtrlCar.getInstance().ccmd1249_isBindLcdKey(currentCar.ide,1,noMaohaoAdress);
                } else {
                    new ToastTxt(BindLcdKeyActivity.this, null).withInfo("当前设备不可用").show();
                    //如果蓝牙扫的不对继续扫描继续扫描
                    if(isScan){
                               if(handler!=null){
                                    if (BuildConfig.DEBUG) Log.e("是否扫描", "扫描无效时候重新识别 " );
                                   handler.restartPreviewAndDecode();
                       }
                    }
                }
            }
        });
    }
    private void scanList() {
        if (BluePermission.checkPermission(BindLcdKeyActivity.this) != 1) {
            BluePermission.openBlueTooth(BindLcdKeyActivity.this);
            return;
        }
        long now = System.currentTimeMillis();
        if (now - preScanTime < 4300L) return;//未到时间不扫15
        preScanTime = now;
//          MyBlueScanList.getInstance().scanDeviceList(GlobalContext.getContext(), true, listenerList);
        MyNewBlueScanList.getInstance().scanDeviceList(GlobalContext.getContext(), true, listenerList);

    }

    private void scanBlue(){
        if (BluePermission.checkPermission(BindLcdKeyActivity.this) != 1) {
            BluePermission.openBlueTooth(BindLcdKeyActivity.this);
            return;
        }


    }

//    MyBlueScanList.OnScanBlueListener listenerList = new MyBlueScanList.OnScanBlueListener() {
//        @Override
//        public void onScanedDeviceList(List<BluetoothDevice> deviceList) {
//             if (BuildConfig.DEBUG) Log.e("查看綫程", "onScanedDeviceList: "+Thread.currentThread() );
//             if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "onScanedDeviceList:" + deviceList.toString());
//        final    List<BluetoothDevice> newDeviceList=new ArrayList<>();
////            if (deviceList != null && deviceList.size() > 0) {
////                scanedDevices = deviceList;
////               if(scanedDevices.size()>0){
////                   for (int i = 0; i <scanedDevices.size() ; i++) {
////                       BluetoothDevice device=scanedDevices.get(i);
////                       if(device!=null&&!TextUtils.isEmpty(device.getName())&&device.getName().startsWith("KCDK")){
////                           newDeviceList.add(scanedDevices.get(i));
////                       }
////                   }
////               }
////                arr = new String[newDeviceList.size()];
////               if(newDeviceList!=null&&newDeviceList.size()>0){
////                   for (int i = 0; i < newDeviceList.size(); i++) {
////                       BluetoothDevice devices=newDeviceList.get(i);
////                       if(devices!=null){
////                           String blueName=devices.getName();
////                           if(!TextUtils.isEmpty(blueName)&&blueName.startsWith("KCDK")){
////                               String device=devices.getAddress();
////                               String noMaoHaoDevice=device.replaceAll(":","");
////                               arr[i]=noMaoHaoDevice;
////                                if (BuildConfig.DEBUG) Log.e("扫描开始", "--------: "+ arr[i] );
////                           }
////                       }
////                   }
////               }
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        initRecycleView();
////                    }
////                });
////            }
//
//
//                        if (deviceList != null && deviceList.size() > 0) {
//                scanedDevices = deviceList;
//               if(scanedDevices.size()>0){
//                   arr = new String[scanedDevices.size()];
//                   for (int i = 0; i <scanedDevices.size() ; i++) {
//                       BluetoothDevice device=scanedDevices.get(i);
//                       arr[i]=device.getAddress();
//                       if(device!=null&&!TextUtils.isEmpty(device.getName())&&device.getName().startsWith("KCDK")){
//                           newDeviceList.add(scanedDevices.get(i));
//
//                       }
//                   }
//               }
////                arr = new String[newDeviceList.size()];
////               if(newDeviceList!=null&&newDeviceList.size()>0){
////                   for (int i = 0; i < newDeviceList.size(); i++) {
////                       BluetoothDevice devices=newDeviceList.get(i);
////                       if(devices!=null){
////                           String blueName=devices.getName();
////                           if(!TextUtils.isEmpty(blueName)&&blueName.startsWith("KCDK")){
////                               String device=devices.getAddress();
////                               String noMaoHaoDevice=device.replaceAll(":","");
////                               arr[i]=noMaoHaoDevice;
////                                if (BuildConfig.DEBUG) Log.e("扫描开始", "--------: "+ arr[i] );
////                           }
////                       }
////                   }
////               }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                         if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "运算完");
//                        initRecycleView();
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void onScanStop(boolean isHave) {
//            if(!isHave){
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        Toast.makeText(BindLcdKeyActivity.this,"没有蓝牙",Toast.LENGTH_SHORT).show();
////                        recycleview_equipments.setVisibility(View.INVISIBLE);
//                    }
//                });
//            }
//
//             if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "停止掃描");
//        }
//    };

    MyNewBlueScanList.OnScanBlueListener listenerList = new MyNewBlueScanList.OnScanBlueListener() {
        @Override
        public void onScanedDeviceList(List<BluetoothDevice> deviceList) {
//             if (BuildConfig.DEBUG) Log.e("查看綫程", "onScanedDeviceList: "+Thread.currentThread() );
             if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "onScanedDeviceList:" + deviceList.toString());
            final    List<BluetoothDevice> newDeviceList=new ArrayList<>();
            if (deviceList != null && deviceList.size() > 0) {
                scanedDevices = deviceList;
               if(scanedDevices.size()>0){
                   for (int i = 0; i <scanedDevices.size() ; i++) {
                       BluetoothDevice device=scanedDevices.get(i);
                        if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "onScanedDeviceList:" + device.getName());
                        //device.getName().startsWith("KCDK")
                       if(device!=null&&!TextUtils.isEmpty(device.getName())&&device.getName().startsWith("KCDK")){
                           newDeviceList.add(scanedDevices.get(i));
                       }
                   }
               }
                arr = new String[newDeviceList.size()];
               if(newDeviceList!=null&&newDeviceList.size()>0){
                   for (int i = 0; i < newDeviceList.size(); i++) {
                       BluetoothDevice devices=newDeviceList.get(i);
                       if(devices!=null){
                           String blueName=devices.getName();
                           if(!TextUtils.isEmpty(blueName)&&blueName.startsWith("KCDK")){
                               String device=devices.getAddress();
                               String noMaoHaoDevice=device.replaceAll(":","");
                               arr[i]=noMaoHaoDevice;
                                if (BuildConfig.DEBUG) Log.e("扫描开始", "--------: "+ arr[i] );
                           }
//                           if(!TextUtils.isEmpty(blueName)){
//                               arr[i]=blueName;
//                           }
                       }
                   }
               }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecycleView();
                    }
                });
            }


//            if (deviceList != null && deviceList.size() > 0) {
//                scanedDevices = deviceList;
//                if(scanedDevices.size()>0){
//                    arr = new String[scanedDevices.size()];
//                    for (int i = 0; i <scanedDevices.size() ; i++) {
//                        BluetoothDevice device=scanedDevices.get(i);
//                        arr[i]=device.getAddress();
//                        if(device!=null&&!TextUtils.isEmpty(device.getName())&&device.getName().startsWith("KCDK")){
//                            newDeviceList.add(scanedDevices.get(i));
//
//                        }
//                    }
//                }

//                arr = new String[newDeviceList.size()];
//               if(newDeviceList!=null&&newDeviceList.size()>0){
//                   for (int i = 0; i < newDeviceList.size(); i++) {
//                       BluetoothDevice devices=newDeviceList.get(i);
//                       if(devices!=null){
//                           String blueName=devices.getName();
//                           if(!TextUtils.isEmpty(blueName)&&blueName.startsWith("KCDK")){
//                               String device=devices.getAddress();
//                               String noMaoHaoDevice=device.replaceAll(":","");
//                               arr[i]=noMaoHaoDevice;
//                                if (BuildConfig.DEBUG) Log.e("扫描开始", "--------: "+ arr[i] );
//                           }
//                       }
//                   }
//               }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                         if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "运算完");
//                        initRecycleView();
//                    }
//                });
//            }
        }

        @Override
        public void onScanStop() {
//            if(!isHave){
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        Toast.makeText(BindLcdKeyActivity.this,"没有蓝牙",Toast.LENGTH_SHORT).show();
////                        recycleview_equipments.setVisibility(View.INVISIBLE);
//                    }
//                });
//            }

             if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "停止掃描");
        }
    };

    private void initRecycleView() {
        if(arr==null||arr.length==0) {
            return;
        }
        // do not change the size of the RecyclerView
        recycleview_equipments.setHasFixedSize(true);
        recycleview_equipments.setItemAnimator(new DefaultItemAnimator());
        if(recycleview_equipments.getItemDecorationCount()==0){
            recycleview_equipments.addItemDecoration(driver);
        }
        if (bindLcdKeyAdapter == null) {
            bindLcdKeyAdapter = new BindLcdKeyRecycleViewAdapter(arr, BindLcdKeyActivity.this);
            recycleview_equipments.setAdapter(bindLcdKeyAdapter);
        } else {
            bindLcdKeyAdapter.setData(arr);
            bindLcdKeyAdapter.notifyDataSetChanged();
        }
//        bindLcdKeyAdapter = new BindLcdKeyRecycleViewAdapter(arr, BindLcdKeyActivity.this);
        recycleview_equipments.setAdapter(bindLcdKeyAdapter);
         if (BuildConfig.DEBUG) Log.e("扫描蓝牙", "显示完");
        viewLoadCompleteTime=System.currentTimeMillis();
        bindLcdKeyAdapter.setOnItemClickListener(new BindLcdKeyRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onMACnumberSelect(View view, int position) {
//                BlueScanList.getInstance().scanDeviceList(null, false, null);
                recycleview_equipments.setVisibility(View.INVISIBLE);
                if(arr!=null&&arr.length>0&&arr.length>position){
                    String macNumber = arr[position];
                    btn_confirm.setTextColor(Color.parseColor("#FFFEFE"));
                    input_mac_number.setText(macNumber);
                    isScan=false;
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        BindLcdKeyActivityThis=this;
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
         if (BuildConfig.DEBUG) Log.e("activitylcd", "onPause: ");
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
        super.onPause();
         if (BuildConfig.DEBUG) Log.e("activitylcd", "onPause: ");
    }

    @Override
    protected void onDestroy() {
//        MyBlueScanList.getInstance().scanDeviceList(BindLcdKeyActivity.this, false, null);
        BindLcdKeyActivityThis=null;
        MyNewBlueScanList.getInstance().scanDeviceList(BindLcdKeyActivity.this, false, null);
        inactivityTimer.shutdown();
        GlobalContext.setIsInBindLcdKey(false);
        myhandler=null;
        ODispatcher.removeEventListener(OEventName.LCDKEY_BIND_OPRATION,this);
         if (BuildConfig.DEBUG) Log.e("activitylcd", "onDestroy: ");
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */

    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        final String resultString = result.getText();
        // FIX_ME
        if (resultString.equals("")) {
//            Toast.makeText(BindLcdKeyActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    String aaa = StringToMacUtil.collapseString(resultString, 2, ":");
                    input_mac_number.setText(resultString);
                    btn_confirm.setTextColor(Color.parseColor("#FFFEFE"));
                    isScan=true;
                    buttonClick(resultString);
                }
            });
        }

    }
    private Handler myhandler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(myhandler!=null){
                if(handler!=null){
//                    myhandler.postDelayed(runnable,2000);
                    handler.restartPreviewAndDecode();
                }
            }
        }
    };


//重新开始扫描

    private void restartCarema() {
        if(handler==null)return;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (handler != null) {
                    handler.quitSynchronously();
                    handler = null;
                }
                CameraManager.get().closeDriver();
                SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();
                initCamera(surfaceHolder);
            }
        },2000);
    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new LcdKeyActivityHandler(this, decodeFormats, characterSet);
        }
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
                viewfinderView.drawViewfinder();
//            }
//        });
    }

    private void buttonClick(String  noMaohaoAdress){
        if (TextUtils.isEmpty(noMaohaoAdress) || noMaohaoAdress.equals("选择液晶钥匙ID")) {
            return;
        }
        String youMaohaoAderess=StringToMacUtil.collapseString(noMaohaoAdress,2,":");
        if ( BluetoothAdapter.checkBluetoothAddress(youMaohaoAderess)) {
            DataCarInfo currentCar= ManagerCarList.getInstance().getCurrentCar();
            if(currentCar==null)return;
            OCtrlCar.getInstance().ccmd1249_isBindLcdKey(currentCar.ide,1,noMaohaoAdress);
        } else {
            new ToastTxt(BindLcdKeyActivity.this, null).withInfo("当前设备不可用").show();
            //如果蓝牙扫的不对继续扫描继续扫描
            if(isScan){
                    if(myhandler!=null){
                       myhandler.postDelayed(runnable,2000);
                    }
            }
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onBackPressed() {
        title_head.img_left.callOnClick();
    }
    //绑定蓝牙
    private void bindBlueTooth( ){
        Intent intent = new Intent();
        intent.setClass(BindLcdKeyActivity.this, ActivityLCDkey.class);
        intent.putExtra("我來了","綁定成功");
        startActivity(intent);
        finish();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
////                    ODispatcher.dispatchEvent(OEventName.LCDKEY_BIND_OPRATION);
////                    Thread.sleep(1500);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        new TimeDelayTask().runTask(350L, new TimeDelayTask.OnTimeEndListener() {
//            @Override
//            public void onTimeEnd() {
//                MyLcdBlueAdapter.getInstance().initializeOK(GlobalContext.getContext());
//                MyLcdBlueAdapter.getInstance().setOnBlueStateListener(new MyOnBlueStateListenerRoll(new MyOnBlueStateListenerRoll.OnonDescriptorWriteLister() {
//                    @Override
//                    public void onDescriptorWrite() {
//
//                        DataCarInfo currentCar= ManagerCarList.getInstance().getCurrentCar();
//                        if(currentCar!=null&&!TextUtils.isEmpty(currentCar.keySig)){
//                            //蓝牙验证串
//                            String blueCarsig=currentCar.keySig;
//                             if (BuildConfig.DEBUG) Log.e("蓝牙绑定", "绑定页面发验证券"+blueCarsig);
//                            if(!TextUtils.isEmpty(blueCarsig)){
//                                byte[] bytesig = blueCarsig.getBytes();
//                                byte[] mess = DataReceive.newBlueMessage((byte) 1, (byte) 1, bytesig);
//                                String datasend=bytesToHexString(mess);
//                                MyLcdBlueAdapter.getInstance().sendMessage(bytesToHexString(mess));
//                                 if (BuildConfig.DEBUG) Log.e("蓝牙绑定", "绑定页面发绑定数据"+datasend);
//                            }
//                        }
//                 finish();
//                    }
//
//                    @Override
//                    public void onConnectedFailed() {
//
//                    }
//                }));
//                //在字符串中每隔两个插入冒号
//                String reealMacAderess= StringToMacUtil.collapseString(cacheBlueAderess, 2, ":");
//                if ( BluetoothAdapter.checkBluetoothAddress(reealMacAderess)) {
//                    MyLcdBlueAdapter.getInstance().gotoConnDeviceAddress(reealMacAderess);
//                } else {
//                    new ToastTxt(BindLcdKeyActivity.this, null).withInfo("当前设备不可用").show();
//                }
//            }
//        });

    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.LCDKEY_BIND_OPRATION)){
            boolean isBindSucsess= (Boolean) o;
            if(!isBindSucsess){
                //如果蓝牙扫描已经绑定过了的地址继续扫描
                if(isScan){
//                  if(myhandler!=null){
//                      myhandler.postDelayed(runnable,3000);
//                       if (BuildConfig.DEBUG) Log.e("是否扫描", "handler= " +handler);
                      if(myhandler!=null){
                           if (BuildConfig.DEBUG) Log.e("是否扫描", "扫描已经用过的地址重新使用" );
                          myhandler.postDelayed(runnable,2000);
//                          handler.restartPreviewAndDecode();
                      }
//                  }
                }
//                myhandler.postDelayed(runnable,5000);
//                restartCarema();
            }else{
                //绑定蓝牙
                bindBlueTooth();
            }
        }
    }
}
