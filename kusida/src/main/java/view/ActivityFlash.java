package view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.core.content.FileProvider;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_assistant.UrlHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.static_system.SystemMe;
import com.kulala.staticsfunc.static_system.WhiteListCheck;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.toast.ToastConfirmNormal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import common.GlobalContext;
import common.LanguageChoose;
import common.LoadPermissions;
import common.PHeadHttp;
import common.PreLoadDefaultData;
import common.VersionNewDownloadApk;
import common.http.HttpConn;
import ctrl.OCtrlBaseHttp;
import model.ManagerCommon;
import model.ManagerLoginReg;
import model.loginreg.DataUser;
import view.view4me.ActivityPermisionTips;
import view.view4me.ActivityUserAgreeMentPravate;


public class ActivityFlash extends ActivityBase {
    private SmartImageView image_flash;
    private TextView txt_download;
    private String downLoadUrl;
    private DownLoadThread downLoadThread;
    private RelativeLayout layer_tips;
    private Button b1;
    private TextView t5,t3;
    private boolean isVerCheckResultBack = false;
    public static boolean notNeedUodate = false;//是否二次进系统
    private int isUpdate = 0;
    private MyHandler handler = new MyHandler();
    private boolean isFlashInVisible = false;//表示flash页面退出的情况下计时的线程是否执行退出方法
    private LoadPermissions loadPermissions=new LoadPermissions();

    protected void onCreate(Bundle savedInstanceState) {
//         if (BuildConfig.DEBUG) Log.e("ActivityFlash", "onCreate" + "  " + System.currentTimeMillis());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falsh);
        image_flash = (SmartImageView) findViewById(R.id.image_flash);
        txt_download = (TextView) findViewById(R.id.txt_download);
        image_flash.setImageResource(R.drawable.img_start_flash);
        layer_tips = (RelativeLayout) findViewById(R.id.layer_tips);
        b1 = (Button) findViewById(R.id.b1);
        t5 = (TextView) findViewById(R.id.t5);
        t3 = (TextView) findViewById(R.id.t3);
        initViews();
        initEvents();
        //第一部：加载权限
//         if (BuildConfig.DEBUG) Log.e("ActivityFlash", "onCreate");


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         if (BuildConfig.DEBUG) Log.e("loadPermission", "onRequestPermissionsResult:" +requestCode+ "  " + permissions.toString());
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       //loadPermissions  LoadPermissions.getInstance()
        loadPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, ActivityFlash.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (BuildConfig.DEBUG) Log.e("loadPermission", "onActivityResult:" +requestCode+ ":" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == WhiteListCheck.REQUEST_IGNORE_BATTERY_CODE){
            /**这是最后一步权限检查*/
            loadPermissions.loadPermissionAll(ActivityFlash.this);
//            LoadPermissions.getInstance().loadPermissionAll(ActivityFlash.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(txt_download.getVisibility() == View.VISIBLE)return;//下载中不进行其它操作
        if (notNeedUodate) exitThis("第二次进系统", true);
    }

    protected void initViews() {
        txtSet();//設置文字
        String firstLoadKulala=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("firstLoadKulala");
        if(!firstLoadKulala.endsWith("false")){
            layer_tips.setVisibility(View.VISIBLE);
        }else{
            layer_tips.setVisibility(View.INVISIBLE);
            loadSystemKulala();
            loadPermissions.loadPermissionAll(ActivityFlash.this);
//            LoadPermissions.getInstance().loadPermissionAll(ActivityFlash.this);
        }
    }
    private void txtSet(){
        t3.setText("在使用我们的产品和服务前，请先阅读并了解");
        SpannableString clickString = new SpannableString("《用户服务协议》");
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(ActivityWeb.TITLE_NAME, "用户使用协议");
                String address;
                try {
                    ApplicationInfo appInfo = GlobalContext.getContext().getPackageManager().getApplicationInfo(GlobalContext.getContext().getPackageName(), PackageManager.GET_META_DATA);
                    address = "http://manage.kcmoco.com/protocol_kusida.html";
                } catch (PackageManager.NameNotFoundException e) {
                    address = "http://manage.kcmoco.com/protocol_kusida.html";
                }
                bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
                intent.putExtras(bundle);
                intent.setClass(GlobalContext.getContext(), ActivityWeb.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GlobalContext.getContext().startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);//设置颜色
            }
        }, 0, clickString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        t3.append(clickString);
        t3.append(new SpannableString("和"));
        SpannableString clickString2 = new SpannableString("《隐私政策》");
        clickString2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent();
                intent.setClass(ActivityFlash.this, ActivityUserAgreeMentPravate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE); //设置颜色
            }
        }, 0, clickString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        t3.append(clickString2);
        t3.append(new SpannableString("以及"));
        SpannableString clickString3 = new SpannableString("《权限说明》");
        clickString3.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent();
                intent.setClass(ActivityFlash.this, ActivityPermisionTips.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE); //设置颜色
            }
        }, 0, clickString3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        t3.append(clickString3);
        t3.append(new SpannableString("。"));
        t3.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
    }

    //加载要用的数据，一般要完成回调
    private void initAppData() {
        ODBHelper.getInstance(getApplicationContext());
        HttpConn.getInstance();
        ManagerCommon.getInstance();
        OCtrlBaseHttp.getInstance();
//        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("position", "1");
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("position");
        int position = ODBHelper.queryResult2Integer(result,0);
        if (position == 0) {
            LanguageChoose.choose(Locale.CHINESE);
        } else if (position == 1) {
            LanguageChoose.choose(Locale.US);
        }
    }


    private int WhiteListCheckTime = 0;//有些手机不给弹窗，5次后就给进去
    private void loadSystemKulala(){
        //LoadPermissions.getInstance()
        loadPermissions.setOnAllPermissionGranted(new LoadPermissions.OnAllPermissionGranted() {
            @Override
            public void onAllGranted() {
                //oppo vivo chuizi 可能的白名单权限禁了
//                if(WhiteListCheckTime<5 && !WhiteListCheck.isIgnoringBatteryOptimizations(getApplicationContext())){
//                    WhiteListCheck.gotoSettingIgnoringBatteryOptimizations(ActivityFlash.this);
//                    WhiteListCheckTime++;
//                    return;
//                }
//                WhiteListCheckTime = 0;
                //1.加载闪屏,已完成
                PreLoadDefaultData.getInstance().loadDefault(getApplicationContext(), new PreLoadDefaultData.OnPreLoadedListener() {
                    @Override
                    public void loadCompleted() {
                        //第二部：加载数据
                        initAppData();
                        //第三部：加载网络打开
                        if (!SystemMe.isNetworkConnected(GlobalContext.getContext())) {
//                             if (BuildConfig.DEBUG) Log.e("ActivityFlash", "网络不良");
                            new ToastConfirmNormal(ActivityFlash.this, null,false)
                                    .withTitle("提示")
                                    .withInfo("网络不良请打开wifi或数据连接")
                                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                        @Override
                                        public void onClickConfirm(boolean isClickConfirm) {
                                            if (isClickConfirm) {
                                                ActivityFlash.this.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                                                ActivityFlash.this.finish();//要停掉，不然回来看不到东西
                                            } else {
                                                notNeedUodate = true;
                                                exitThis("cancelConfirm", false);
                                            }
                                        }
                                    }).show();
                        } else {
//                             if (BuildConfig.DEBUG) Log.e("ActivityFlash", "网络正常");
                            //第四部：网络已开,看升级
                            handler.postDelayed(new TimeEndThread(), 300);//二秒没回包处理
//                            OCtrlCommon.getInstance().ccmd1302_getVersionInfo(0);
//                            ODispatcher.addEventListener(OEventName.GETVERSIONINFO_RESULTBACK, ActivityFlash.this);
                        }
                    }
                });
            }

            @Override
            public void onDined() {
                //当有权限不同意时
                PreLoadDefaultData.getInstance().loadDefault(getApplicationContext(), new PreLoadDefaultData.OnPreLoadedListener() {
                    @Override
                    public void loadCompleted() {
                        //第二部：加载数据
                        initAppData();
                        //第三部：加载网络打开
                        if (!SystemMe.isNetworkConnected(GlobalContext.getContext())) {
//                             if (BuildConfig.DEBUG) Log.e("ActivityFlash", "网络不良");
                            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                                    .withTitle("提示")
                                    .withInfo("网络不良请打开wifi或数据连接")
                                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                        @Override
                                        public void onClickConfirm(boolean isClickConfirm) {
                                            if (isClickConfirm) {
                                                ActivityFlash.this.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                                                ActivityFlash.this.finish();//要停掉，不然回来看不到东西
                                            } else {
                                                notNeedUodate = true;
                                                exitThis("cancelConfirm", false);
                                            }
                                        }
                                    }).show();
                        } else {
//                             if (BuildConfig.DEBUG) Log.e("ActivityFlash", "网络正常");
                            //第四部：网络已开,看升级
                            handler.postDelayed(new TimeEndThread(), 300);//二秒没回包处理
//                            OCtrlCommon.getInstance().ccmd1302_getVersionInfo(0);
//                            ODispatcher.addEventListener(OEventName.GETVERSIONINFO_RESULTBACK, ActivityFlash.this);
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void initEvents() {

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("firstLoadKulala", "false");
                loadSystemKulala();
                loadPermissions.loadPermissionAll(ActivityFlash.this);
//                LoadPermissions.getInstance().loadPermissionAll(ActivityFlash.this);
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);//退出系统
            }
        });
    }


    /**
     * 清除绘制状态的线程
     */
    final class TimeEndThread implements Runnable {
        public void run() {
            //能收包就直接退出，不能收包，网络不良才计时
            if (!isFlashInVisible) {
                if (isVerCheckResultBack) {
                    if (isUpdate == 0) {//无需升级
                        exitThis("无需升级", false);
                    }
                } else {
                    notNeedUodate = true;
                    exitThis("二秒未回包", false);
                }
            }
        }
    }

    //============================开始运行==================================
    @Override
    public void callback(String key, Object value) {
    }

    @Override
    protected void invalidateUI() {
    }

    @Override
    protected void popView(int resId) {
    }

    @Override
    public void receiveEvent(String key, Object paramObj) {
        if (key.equals(OEventName.GETVERSIONINFO_RESULTBACK)) {
//             if (BuildConfig.DEBUG) Log.e("ActivityFlash", "GETVERSIONINFO_RESULTBACK");
            isVerCheckResultBack = true;
            JsonObject obj = (JsonObject) paramObj;
            if (obj == null) {
                notNeedUodate = true;
                exitThis("GETVERSIONINFO_RESULTBACK null", false);
            } else {
                isUpdate = OJsonGet.getInteger(obj, "isUpdate");
                String info = OJsonGet.getString(obj, "comment");
                if (isUpdate == 1) {
                    downLoadUrl = OJsonGet.getString(obj, "downLoadUrl");
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("版本升级")
                            .withInfo(info)
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm) {
                                        handleStartUpdate();//点了升级
                                    } else {
                                        notNeedUodate = true;
                                        exitThis("cancel Update", false);
                                    }
                                }
                            }).show();
                } else {
                    notNeedUodate = true;
                    exitThis("GET_VERSION 无需升级", false);
                }
            }
        }
        super.receiveEvent(key, paramObj);
    }

    private long preExitTime = 0;

    private void exitThis(String testinfo, boolean isSeconderyIntoSystem) {
         if (BuildConfig.DEBUG) Log.e("ActivityFlash", "exitThis:" + testinfo + "  " + System.currentTimeMillis());
        ODispatcher.removeEventListener(OEventName.GETVERSIONINFO_RESULTBACK, this);
        final Intent intent = new Intent();
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        String adventUrl = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("adventUrl");
        if (isSeconderyIntoSystem) {
            String token = PHeadHttp.getToken();
            if (user == null || user.userId == 0 || token == null || token.length() == 0) {
                intent.setClass(this, ActivityLogin.class);//未登录
            } else {
                ActivityKulalaMain.GestureNeed = true;
                intent.setClass(this, ActivityKulalaMain.class);//已登录
            }
        } else if (adventUrl != null && notNeedUodate && !adventUrl.equals("")) {
            intent.setClass(this, ActivityAdvertising.class);//已登录
        } else if (adventUrl == null && notNeedUodate||adventUrl.equals("")) {
            String token = PHeadHttp.getToken();
            if (user == null || user.userId == 0 || token == null || token.length() == 0) {
                intent.setClass(this, ActivityLogin.class);//未登录
            } else {
                ActivityKulalaMain.GestureNeed = true;
                intent.setClass(this, ActivityKulalaMain.class);//已登录
//                intent.setClass(this, ActivityMyHome.class);//已登录
            }
        }
        ActivityFlash.this.startActivity(intent);
        ActivityFlash.this.finish();
//        long now = System.currentTimeMillis();
//
//        if (notNeedUodate && now - preExitTime > 1000L) {
//            preExitTime = now;
//            isFlashInVisible = true;
//            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
//            if(carInfo.getCarSkin().url.contains("ios")){
//                ActivityFlash.this.startActivity(intent);
//                ActivityFlash.this.finish();
//            }else{
//                //carInfo.getCarSkin().url
//                ManagerSkins.getInstance().loadSkin(ActivityFlash.this,carInfo.getCarSkin().url,"body", new ManagerSkins.OnLoadPngListener() {
//                    @Override
//                    public void loadCompleted(Drawable image) {
//                        ActivityFlash.this.startActivity(intent);
//                        ActivityFlash.this.finish();
//                    }
//
//                    @Override
//                    public void loadFail(String errorInfo) {
//                        ActivityFlash.this.startActivity(intent);
//                        ActivityFlash.this.finish();
//                    }
//                });
//            }
//        }
    }

    @Override
    protected void onDestroy() {
//         if (BuildConfig.DEBUG) Log.e("ActivityFlash", "onDestroy");
        ODispatcher.removeEventListener(OEventName.GETVERSIONINFO_RESULTBACK, this);
        super.onDestroy();
    }

    public void handleStartUpdate() {
        Message message = new Message();
        message.what = 110;
        handler.sendMessage(message);
    }

    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 110:
                    txt_download.setVisibility(View.VISIBLE);
                    if(VersionNewDownloadApk.checkInstallPermission(getApplicationContext())) {
                        ODispatcher.removeEventListener(OEventName.GETVERSIONINFO_RESULTBACK, ActivityFlash.this);
                        new VersionNewDownloadApk(getApplicationContext(), downLoadUrl, UrlHelper.getFileName(downLoadUrl) + ".apk");
                    }else{
                        txt_download.setText("请重新打开App检查更新!");
                    }
//                    ProgressDialog pd; // 进度条对话框
//                    pd = new ProgressDialog(ActivityFlash.this);
//                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    pd.setCanceledOnTouchOutside(false);
//                    pd.setMessage(getResources().getString(R.string.is_download_the_update));
//                    pd.show();
//                    if (downLoadThread == null) downLoadThread = new DownLoadThread(pd);
//                    downLoadThread.start();
                    break;
            }
        }
    }

    class DownLoadThread extends Thread {
        private ProgressDialog pd;

        public DownLoadThread(ProgressDialog pd) {
            this.pd = pd;
        }

        public void run() {
            boolean needLoop = false;
            if (Looper.myLooper() != Looper.getMainLooper()) needLoop = true;
            if (needLoop) Looper.prepare();

            try {
                File file = getFileFromServer(downLoadUrl, pd);
                sleep(3000);
                installApk(file);
                pd.dismiss(); // 结束掉进度条对话框
            } catch (Exception e) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.download_server_error_please_download_again));
                pd.dismiss();
                e.printStackTrace();
            }
            if (needLoop) Looper.loop();
        }
    }

    private File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(20000);
        // 获取到文件的大小
        pd.setMax(conn.getContentLength());
        InputStream is = conn.getInputStream();

        File file = new File(GlobalContext.getContext().getExternalFilesDir(null).getAbsolutePath());
//        File file = Environment.getExternalStorageDirectory();
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(file, "updata.apk");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len;
        int total = 0;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            total += len;
            // 获取当前下载量
            pd.setProgress(total);
        }
        fos.close();
        bis.close();
        is.close();
        return file;
//        } else {
//            return null;
//        }
    }

    // 安装apk,测试版是不能更新的，正式版签名才能更新
    protected void installApk(File file) {
//         if (BuildConfig.DEBUG) Log.e("ActivityFlash", "installApk");
        notNeedUodate = true;
        exitThis(getResources().getString(R.string.install_the_apk), false);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            Uri contentUri = FileProvider.getUriForFile(GlobalContext.getContext(), GlobalContext.getContext().getPackageName(), file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            GlobalContext.getContext().startActivity(intent);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            GlobalContext.getContext().startActivity(intent);
        }
    }
}