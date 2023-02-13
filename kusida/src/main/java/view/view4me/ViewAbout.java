package view.view4me;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.core.content.FileProvider;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_assistant.UrlHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.static_system.SystemMe;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import com.kulala.staticsview.toast.ToastResult;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import common.GlobalContext;
import common.VersionNewDownloadApk;
import ctrl.OCtrlCommon;
import view.ActivityWeb;
import view.clip.ClipLineBtnInptxt;
import view.view4me.set.ClipTitleMeSet;

public class ViewAbout extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private ClipLineBtnInptxt txt_intro, txt_suggest, txt_update;
    private TextView txt_licence, txt_version, txt_update_change;
    private static long    lastCheckUpdateTime = 0;//上回检查更新的时间
    private static boolean needUpdate          = false;
    public static  boolean needShowResult      = false;
    private String         downLoadUrl;
    private DownLoadThread downLoadThread;
    private MyHandler handler = new MyHandler();
    public ViewAbout(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_about, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_intro = (ClipLineBtnInptxt) findViewById(R.id.txt_intro);
        txt_suggest = (ClipLineBtnInptxt) findViewById(R.id.txt_suggest);
        txt_update = (ClipLineBtnInptxt) findViewById(R.id.txt_update);
        txt_licence = (TextView) findViewById(R.id.txt_licence);
        txt_version = (TextView) findViewById(R.id.txt_version);
        txt_update_change = (TextView) findViewById(R.id.txt_update_change);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GETVERSIONINFO_RESULTBACK, this);
    }

    public void initViews() {
        handleChangeData();
        txt_version.setText(SystemMe.getVersionName(getContext()));
        txt_update_change.setText(SystemMe.getVersionName(getContext()));
        long now = System.currentTimeMillis();
        if (now - lastCheckUpdateTime > 30000) {
            OCtrlCommon.getInstance().ccmd1302_getVersionInfo(1);
            lastCheckUpdateTime = now;
        }
        if (needShowResult) {
            needShowResult = false;
            handleShowResult();
        }
    }

    public void initEvents() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_setup);
            }
        });
        txt_intro.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_about_intro);
            }
        });
        txt_licence.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_about_licence);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(ActivityWeb.TITLE_NAME, "用户使用协议");
                String address;
                try {
                    ApplicationInfo appInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
                    address = "http://manage.kcmoco.com/protocol_kusida.html";
                } catch (PackageManager.NameNotFoundException e) {
                    address = "http://manage.kcmoco.com/protocol_kusida.html";
                }
                bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
                intent.putExtras(bundle);
                intent.setClass(getContext(), ActivityWeb.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        txt_update.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                long now = System.currentTimeMillis();
                if (now - lastCheckUpdateTime > 30000) {
                    OCtrlCommon.getInstance().ccmd1302_getVersionInfo(1);
                    lastCheckUpdateTime = now;
                } else if (needUpdate) {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withInfo("有新版本，现在就更新吗?")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm) handleStartUpdate();
                                }
                            }).show();
                } else {
                    Toast.makeText(getContext(), "已是最新版本!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txt_suggest.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_about_suggest);
            }
        });
    }

    public void receiveEvent(String key, Object paramObj) {
        if (key.equals(OEventName.GETVERSIONINFO_RESULTBACK)) {
            JsonObject obj = (JsonObject) paramObj;
            if (obj == null) {
                needUpdate = false;
            } else {
                int isUpdate = OJsonGet.getInteger(obj, "isUpdate");
//				String info = OJsonGet.getString(obj, "comment");
                if (isUpdate == 1) {
                    needUpdate = true;
                    downLoadUrl = OJsonGet.getString(obj, "downLoadUrl");
                }
            }
//            handleChangeData();
        }
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

    @Override
    public void invalidateUI() {
//        if (needUpdate) {
//            txt_update_change.setText(getResources().getString(R.string.with_the_new_version));
//        } else {
//            txt_update_change.setText(getResources().getString(R.string.is_the_latest_version));
//        }
    }
    // ==============================================================

    public void handleShowResult() {
        Message message = new Message();
        message.what = 911;
        handler.sendMessage(message);
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
                    txt_update_change.setText("正在下载...");
                    if(VersionNewDownloadApk.checkInstallPermission(getContext())) {
                        new VersionNewDownloadApk(getContext(), downLoadUrl, UrlHelper.getFileName(downLoadUrl) + ".apk");
                    }else{
                        txt_update_change.setText("请30秒后再检测更新!");
                    }
//                    ProgressDialog pd; // 进度条对话框
//                    pd = new ProgressDialog(getContext());
//                    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    pd.setCanceledOnTouchOutside(false);
//                    pd.setMessage(getResources().getString(R.string.is_download_the_update));
//                    pd.show();
//                    if (downLoadThread == null) downLoadThread = new DownLoadThread(pd);
//                    downLoadThread.start();
                    break;
                case 911:
                    ToastResult.getInstance().show(title_head, true, null);
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
        URL               url  = new URL(path);
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
        FileOutputStream    fos    = new FileOutputStream(file);
        BufferedInputStream bis    = new BufferedInputStream(is);
        byte[]              buffer = new byte[1024];
        int                 len;
        int                 total  = 0;
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
        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_DESTORY);//退出
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            Uri contentUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName(), file);
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
