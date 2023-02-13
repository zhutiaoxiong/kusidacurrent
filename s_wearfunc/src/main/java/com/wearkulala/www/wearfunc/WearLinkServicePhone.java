package com.wearkulala.www.wearfunc;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

/**
 * Created by Administrator on 2018/1/16.
 */

public class WearLinkServicePhone extends WearableListenerService {
    private static GoogleApiClient googleApiClient;
    public static boolean isLinked = false;
    private static final String TAG            = "YXH";
    private static final String PATH_DATA_FROM = "/start-datalink";

//    private static WearLinkServicePhone _instance;
//    public static WearLinkServicePhone getInstance() {
//        return _instance;
//    }
    public void onCreate() {
        Log.e(TAG, "<<<<<onCreate>>>>");
        super.onCreate();
//        _instance = this;
        //init
        if(googleApiClient == null)googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    //连接成功
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.e(TAG, "onConnected: " + connectionHint);
                        isLinked = true;
                        ODispatcher.dispatchEvent(OEventName.WEAR_LINK_STATE_CHANGE,true);
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.e(TAG, "onConnectionSuspended暂停,悬挂: " + cause);
                        isLinked = false;
                        ODispatcher.dispatchEvent(OEventName.WEAR_LINK_STATE_CHANGE,false);
                    }
                })
                //连接失败
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.e(TAG, "onConnectionFailed: " + result.toString());
                        isLinked = false;
                        ODispatcher.dispatchEvent(OEventName.WEAR_LINK_STATE_CHANGE,false);
                        if (!result.hasResolution()) {
                            Log.e("YXH", "onConnectionFailed. CODE:"+result.getErrorCode());
//                            GoogleApiAvailability.getInstance().getErrorDialog(WearReg.getInstance().getCurrentActivity(), result.getErrorCode(), 0).show();
                            return;
                        }
                        try {
                            result.startResolutionForResult(WearReg.getInstance().getCurrentActivity(), 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Exception while starting resolution activity", e);
                        }
                    }
                })
                //因为有可能手表并没有连接上，所以最好使用addApiIfAvailable() 而不是addApi()
//                .addApiIfAvailable(Wearable.API)
                .addApi(Wearable.API)
                .build();
        StartConnect();
    }

    private void StartConnect(){
        if(googleApiClient!=null && !googleApiClient.isConnected()){
            Log.e(TAG, "StartConnect: ");
            googleApiClient.connect();
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "<<<<<onStartCommand>>>>");
        return Service.START_STICKY;
    }
    @Override
    public void onDestroy() {
//        _instance = null;
        if(googleApiClient != null && googleApiClient.isConnected()){
            googleApiClient.disconnect();
            ODispatcher.dispatchEvent(OEventName.WEAR_LINK_STATE_CHANGE,false);
        }
        Log.e(TAG, "<<<<<onDestroy>>>>");
        super.onDestroy();
    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.e(TAG, "<<<<<onDataChanged>>>>");
        super.onMessageReceived(messageEvent);
    }
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.e(TAG, "<<<<<onDataChanged>>>>");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_DELETED) {
            } else if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                Log.e(TAG, "DataItem changed: " + dataMap);
                checkIsNeedLogin(dataMap);
                checkManualExitWear(dataMap);
            }
        }
    }
    private long preCheck = 0;
    private void checkIsNeedLogin(DataMap dataMap){
        if (dataMap == null || WearReg.isCanGetLoginState == false) return;
        if (dataMap.containsKey("messageName")&& dataMap.getString("messageName").equals("HeartJump")) {
            long now = System.currentTimeMillis();
            if(now - preCheck<10000)return;
            int wearOpen = WearReg.getInstance().getSwitchWearsOpen();
            long userId = WearReg.getInstance().getUserID();
            if(userId == -1 || wearOpen == 0 || wearOpen == -1){
                changeUser(0,"");
            }else {
                changeUser(userId, WearReg.getInstance().getWatchToken());
            }
        }
    }
    //手表退出登录
    private void checkManualExitWear(DataMap dataMap){
        if (dataMap == null) return;
        if (dataMap.containsKey("messageName")&& dataMap.getString("messageName").equals("WearManualExit")) {
            int wearOpen = WearReg.getInstance().getSwitchWearsOpen();
            if(wearOpen == -1)return;
            WearReg.getInstance().setSwitchWearsOpen(0);
            long userId = WearReg.getInstance().getUserID();
            if(userId != -1){
                changeUser(0,"");
                WearReg.getInstance().changeUserWatchToken(userId,"");
                if(ViewWear.viewWearThis!=null)ViewWear.viewWearThis.handlerChangeSwitch();
            }
        }
    }
    /**
     * @param inputData messageName:LoginIn uid:123 token:16516951691565169151
     */
    public static boolean sendMesssage(String messageName,DataMap inputData) {
        boolean canSend = (googleApiClient == null || isLinked == false || inputData==null) ? false : true;
        if(!canSend)return false;
        PutDataMapRequest dataMap = PutDataMapRequest.create(PATH_DATA_FROM);
        inputData.putString("messageName",messageName);
        inputData.putLong("messageTime",System.currentTimeMillis());
        Log.e(TAG, "sendMesssage:"+inputData);
        dataMap.getDataMap().putAll(inputData);
        PutDataRequest                        request       = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(googleApiClient, request);
        return true;
    }
    public static void changeUser(long uid,String token){
        DataMap inputData = new DataMap();
        inputData.putLong("uid", uid);
        inputData.putString("token",token);
        sendMesssage("ChangeUser",inputData);
    }
    public static boolean openBlueIfNeed(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) return false;//没有蓝牙模块
        if(!bluetoothAdapter.isEnabled()){
            //可以去打开系统蓝牙界面
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置蓝牙可见性，最多300秒
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            WearReg.getInstance().getCurrentActivity().startActivity(intent);
            return false;
        }
        return true;
    }
}