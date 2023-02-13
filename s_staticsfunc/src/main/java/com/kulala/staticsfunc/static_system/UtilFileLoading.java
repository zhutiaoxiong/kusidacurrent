package com.kulala.staticsfunc.static_system;

import android.util.Log;

import com.kulala.staticsfunc.static_assistant.UtilFileSave;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 用于从服务端下载文件，并保存到指定位置
 */
public class UtilFileLoading {
//    String preUrlServer = "";
//    private long preLoadTime = 0;//会有可能网络不好，无任何反应,只有重下同一个东西
//    private        SetLoadProgressListener setLoadProgressListener;
//    // ========================out======================
//    private static UtilFileLoading         _instance;
//    private UtilFileLoading() {
//    }
//    public static UtilFileLoading getInstance() {
//        if (_instance == null)
//            _instance = new UtilFileLoading();
//        return _instance;
//    }
//    // ========================public======================
//    public interface SetLoadProgressListener {
//        void setMaxProgress(int max);
//
//        void setProgress(int progress);
//
//        void onCompleted(File savedFile) throws Exception;
//
//        void onLoadFailed(String errorInfo);
//    }
//    /**
//     * saveLoc 目录地址 saveDir 目录 Suffix 后缀名
//     */
//    public void loadFileFromServer(String urlServer, String saveDir, String fileName, SetLoadProgressListener listener) {
//        if (urlServer == null || urlServer.length() == 0) return;
//        this.setLoadProgressListener = listener;
//        new DownLoadThread(urlServer,saveDir,fileName).start();
//    }
//    // ========================private======================
//
//    private class DownLoadThread extends Thread {
//        private String urlServer, saveDir, fileName;
//        public DownLoadThread(String urlServer, String saveDir, String fileName) {
//            this.urlServer = urlServer;
//            this.saveDir = saveDir;
//            this.fileName = fileName;
//        }
//        public void run() {
//            if(preUrlServer.equals(urlServer))return;//下载同一个东西
//            if(UtilFileSave.isFileInDisk(saveDir,fileName)){
//                if(setLoadProgressListener!=null) {
//                    try {
//                        preUrlServer = "";
//                        setLoadProgressListener.onCompleted(new File(saveDir,fileName));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                return;
//            }
//            preUrlServer = urlServer;
//             if (BuildConfig.DEBUG) Log.e("下载", "正在下载"+urlServer);
//            try {
//                File file = getFileFromServer(urlServer,saveDir,fileName);
//                sleep(500L);//3473e5432072d5105ead00dc09f6910b.zip
//                if (file.exists()) {
//                    preUrlServer = "";
//                    if (setLoadProgressListener != null) setLoadProgressListener.onCompleted(file);
//                }
//            } catch (Exception e) {
//                preUrlServer = "";
//                if (setLoadProgressListener != null)
//                    setLoadProgressListener.onLoadFailed("下载" + fileName + "失败" + e.toString());
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private File getFileFromServer(String urlServer, String saveDir, String fileName) throws Exception {
//        URL               url  = new URL(urlServer);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setConnectTimeout(20000);
//        // 获取到文件的大小
//        if (setLoadProgressListener != null)
//            setLoadProgressListener.setMaxProgress(conn.getContentLength());
//        InputStream is   = conn.getInputStream();
//        File        file = new File(saveDir);
//        file.mkdirs();
//        file = new File(file, fileName);
//        FileOutputStream    fos    = new FileOutputStream(file);
//        BufferedInputStream bis    = new BufferedInputStream(is);
//        byte[]              buffer = new byte[1024];
//        int                 len;
//        int                 total  = 0;
//        while ((len = bis.read(buffer)) != -1) {
//            fos.write(buffer, 0, len);
//            total += len;
//            // 获取当前下载量
//            if (setLoadProgressListener != null) setLoadProgressListener.setProgress(total);
//        }
//        fos.close();
//        bis.close();
//        is.close();
//        return file;
//    }
}
