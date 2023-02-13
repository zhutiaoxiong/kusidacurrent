package common.linkbg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkscarpods.service.KulalaServiceC;
import com.kulala.staticsfunc.static_system.OJsonGet;

import common.GlobalContext;
import common.PHeadSocket;
import ctrl.OCtrlSocketMsg;
import model.ManagerSwitchs;

//import static com.kulala.linksankula.blue.BlueStaticValue.IS_ACTIVITYMAIN_FORGROUND;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SERVICE_INIT_NOTIFI_GET;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SERVICE_INIT_NOTIFI_POST;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SERVICE_INIT_SOCKET_GET;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SERVICE_INIT_SOCKET_POST;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SERVICE_IS_CONNECT;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SERVICE_RECEIVE_MESSAGE;
import static com.kulala.linkscarpods.blue.BlueStaticValue.SERVICE_SEND_MESSAGE;

public class BootBroadcastReceiver extends BroadcastReceiver {

    private Context mContext;
    private static boolean alreadyRegCanDestory = false;
    //    private static SocketConnNio          sock;
    private static BootBroadcastReceiver BootBroadcastReceiverThis;
    //=================================================================
    private static BootBroadcastReceiver _instance;
    public static BootBroadcastReceiver getInstance() {
        if (_instance == null)
            _instance = new BootBroadcastReceiver();
        return _instance;
    }
    /**
     * 8.0取消了静态注删，只能动态
     */
    public void initReceiver(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!alreadyRegCanDestory) {//Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                    BootBroadcastReceiverThis = new BootBroadcastReceiver();
                    try {
                        IntentFilter filter = new IntentFilter();
                        filter.addAction(SERVICE_INIT_SOCKET_POST);
                        filter.addAction(SERVICE_INIT_NOTIFI_POST);
                        filter.addAction(SERVICE_RECEIVE_MESSAGE);
                        filter.addAction(SERVICE_IS_CONNECT);

                        mContext = context;
                        context.registerReceiver(BootBroadcastReceiverThis, filter,MytoolsGetPackageName.getBroadCastPermision(),null);
//                        mContext.registerReceiver(BootBroadcastReceiverThis, filter);
                        alreadyRegCanDestory = true;
                         if (BuildConfig.DEBUG) Log.e("BootBroadcastReceiver","initReceiver");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BootBroadcastReceiver.initNotification();
            }
        }).start();
    }
    public void unRegReceiver(){
        if(alreadyRegCanDestory && BootBroadcastReceiverThis!=null && mContext!=null){
            try {
                mContext.unregisterReceiver(BootBroadcastReceiverThis);
                alreadyRegCanDestory = false;
                BootBroadcastReceiverThis = null;
                mContext = null;
                if (BuildConfig.DEBUG) Log.e("BootBroadcastReceiver","unRegReceiver");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public static void startC() {
        try {
            Intent intentA = new Intent(GlobalContext.getContext(), KulalaServiceC.class);
            String packageName=GlobalContext.getContext().getPackageName();
             if (BuildConfig.DEBUG) Log.e("包名", packageName );
            intentA.setPackage(packageName);
            GlobalContext.getContext().startService(intentA);
        } catch (Exception e) {
        }
    }
    @Override
    public void onReceive(Context content, Intent intent) {
        if (BootBroadcastReceiverThis == null) {
            BootBroadcastReceiverThis = this;
        }
        if (SERVICE_INIT_SOCKET_POST.equals(intent.getAction())) {
             if (BuildConfig.DEBUG) Log.e("BootBroadcastReceiver", "收到请求初始化数据" );
            initOrChangeSocket();
        } else if (SERVICE_INIT_NOTIFI_POST.equals(intent.getAction())) {
            initNotification();
        } else if (SERVICE_RECEIVE_MESSAGE.equals(intent.getAction())) {
//            if(!ActivityKulalaMain.IS_ON_RESUME)return;
             if (BuildConfig.DEBUG) Log.e("BootBroadcastReceiver", "SERVICE_RECEIVE_MESSAGE" );
            if (GlobalContext.getCurrentActivity() == null) return;
            String     json = intent.getStringExtra("service_receive_obj");
            Gson       gson = new Gson();
            JsonObject obj  = gson.fromJson(json, JsonObject.class);
            int cmd   = OJsonGet.getInteger(obj, "cmd");
            OCtrlSocketMsg.getInstance().processResult(cmd,obj);
        }else if (SERVICE_IS_CONNECT.equals(intent.getAction())) {
            String     isConnect = intent.getStringExtra("isConnect");
            Log.e("isconnect", isConnect );
            ODispatcher.dispatchEvent(OEventName.SERVICE_IS_CONNECT,isConnect);
        }else{
             if (BuildConfig.DEBUG) Log.e("BootBroadcastReceiver", "onReceive" + intent.getAction());
        }
    }
    //==========================================
    //module new change
    public static void initOrChangeSocket() {
        Intent broadcast = new Intent(SERVICE_INIT_SOCKET_GET);
        broadcast.putExtra("jsonObjPHead", PHeadSocket.getPHeadSocketAllInit(GlobalContext.getContext()).toString());
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        GlobalContext.getContext().sendBroadcast(broadcast);
         if (BuildConfig.DEBUG) Log.e("BootBroadcastReceiver", "发送请求初始化数据" +PHeadSocket.getPHeadSocketAllInit(GlobalContext.getContext()).toString());
    }
    //module new change
    public static void initNotification() {
        Intent broadcast = new Intent(SERVICE_INIT_NOTIFI_GET);//以下二项service自已提供
//        broadcast.putExtra("IconId", R.drawable.kulala_icon);
//        broadcast.putExtra("projName", GlobalContext.getContext().getResources().getString(R.string.app_name));
        broadcast.putExtra("soundOpen", ManagerSwitchs.getInstance().getSoundOpen());
        broadcast.putExtra("vitratorOpen", ManagerSwitchs.getInstance().getVibratorOpen());
        broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        GlobalContext.getContext().sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        GlobalContext.getContext().sendBroadcast(broadcast);
    }

    public static void sendMessage(Context globalContext, int cmd, String json) {
        Intent broadcast = new Intent(SERVICE_SEND_MESSAGE);
        broadcast.putExtra("cmd", cmd);
        broadcast.putExtra("json", json);
       broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
        globalContext.sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        globalContext.sendBroadcast(broadcast);
    }

//    public static void notyfyActivityMainIsForground(Context globalContext,boolean isForground){
//        Intent broadcast = new Intent(IS_ACTIVITYMAIN_FORGROUND);
//        broadcast.putExtra("isForground", isForground);
//       broadcast.setPackage(MytoolsGetPackageName.getPackageNameMy());
////        globalContext.sendBroadcast(broadcast,MytoolsGetPackageName.getBroadCastPermision());
//        globalContext.sendBroadcast(broadcast);
//    }
    //=================================================================

}
