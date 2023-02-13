package model;

import android.net.Uri;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_assistant.UtilFileSave;
import com.kulala.staticsfunc.static_system.MD5;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import common.GlobalContext;

public class ManagerFlash {
    private static String splashAddress = "";//闪屏页地址,唯一的
    private static Uri flashUri;
    private static String adventAddress;//广告页地址
    private static Uri adventUri;
    // ========================out======================
    private static ManagerFlash _instance;

    private ManagerFlash() {
    }

    public static ManagerFlash getInstance() {
        if (_instance == null)
            _instance = new ManagerFlash();
        return _instance;
    }

    // =================================================
    // =======================falsh==========================
    public void saveAdvent(String address) {
        String dir = UtilFileSave.getBaseDir(GlobalContext.getContext());
        String filename = "advent" + UtilFileSave.PNG_CHANGE_NAME;//MD5.MD5generator(address)
        if (address == null || address.length() == 0) {//是无其它闪屏，用默认闪屏,删掉使用的闪屏
            //保存值删
            ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("adventCheck", "");
            //flash.png删
            UtilFileSave.RecursionDeleteFile(new File(dir + "/" + filename));
            Log.i("1303", "delete falsh");
            return;
        }
        if (address.equals(adventAddress)) return;//与之前的闪屏一致
        final String checkValue = MD5.MD5generator(address);
        String check = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("adventCheck");
        if (check != null && check.equals(checkValue) && UtilFileSave.isFileInDisk(dir, filename)) {
            //相同值不处理
        } else {
            FileDownloader.getImpl().create(address)
                    .setPath(dir + File.separator + filename + ".zip")
                    .setForceReDownload(true)
                    .setListener(new FileDownloadListener() {
                        @Override//等待
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }
                        @Override//下载进度回调
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }
                        @Override//完成下载
                        protected void completed(BaseDownloadTask task) {
                            ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("adventCheck", checkValue);
                        }
                        @Override//暂停
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }
                        @Override//下载出错
                        protected void error(BaseDownloadTask task, Throwable e) {
                            if (BuildConfig.DEBUG) Log.e("下载", "Flash:下载出错:"+e.toString());
                        }
                        @Override//已存在相同下载
                        protected void warn(BaseDownloadTask task) {
                            if (BuildConfig.DEBUG) Log.e("下载", "Flash:已存在相同下载");
                        }
                    }).start();
        }
    }

    public void saveFlash(String address) {
        if(address.equals("-1"))return;
        String dir = UtilFileSave.getBaseDir(GlobalContext.getContext());
        String filename = "flash" + UtilFileSave.PNG_CHANGE_NAME;//MD5.MD5generator(address)
        if (address == null || address.length() == 0) {//是无其它闪屏，用默认闪屏,删掉使用的闪屏
            //保存值删
            ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("flashCheck", "");
            //flash.png删
            UtilFileSave.RecursionDeleteFile(new File(dir + "/" + filename));
            Log.i("1303", "delete falsh");
            return;
        }
        if (address.equals(splashAddress)) return;//与之前的闪屏一致
        final String checkValue = MD5.MD5generator(address);
        String check = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("flashCheck");
        if (check != null && check.equals(checkValue) && UtilFileSave.isFileInDisk(dir, filename)) {
            //相同值不处理
        } else {
            FileDownloader.getImpl().create(address)
                    .setPath(dir + File.separator + filename + ".zip")
                    .setForceReDownload(true)
                    .setListener(new FileDownloadListener() {
                        @Override//等待
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }
                        @Override//下载进度回调
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }
                        @Override//完成下载
                        protected void completed(BaseDownloadTask task) {
                            ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("flashCheck", checkValue);
                        }
                        @Override//暂停
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }
                        @Override//下载出错
                        protected void error(BaseDownloadTask task, Throwable e) {
                            if (BuildConfig.DEBUG) Log.e("下载", "Flash:下载出错:"+e.toString());
                        }
                        @Override//已存在相同下载
                        protected void warn(BaseDownloadTask task) {
                            if (BuildConfig.DEBUG) Log.e("下载", "Flash:已存在相同下载");
                        }
                    }).start();
        }
    }

    public Uri getFlashUri() {
        if (flashUri != null) return flashUri;
        String dir = UtilFileSave.getBaseDir(GlobalContext.getContext());
        String filename = "flash" + UtilFileSave.PNG_CHANGE_NAME;//MD5.MD5generator(address)
        if (UtilFileSave.isFileInDisk(dir, filename)) {//不同的皮肤了
            flashUri = Uri.fromFile(UtilFileSave.getFileFromDisk(dir, filename));
        }
        return flashUri;
    }

    public Uri getAdventUri() {
        if (adventUri != null) return adventUri;
        String dir = UtilFileSave.getBaseDir(GlobalContext.getContext());
        String filename = "advent" + UtilFileSave.PNG_CHANGE_NAME;//MD5.MD5generator(address)
        if (UtilFileSave.isFileInDisk(dir, filename)) {//不同的皮肤了
            adventUri = Uri.fromFile(UtilFileSave.getFileFromDisk(dir, filename));
        }
        return adventUri;
    }
}
