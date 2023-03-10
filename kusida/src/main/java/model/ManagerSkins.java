package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.kulala.staticsfunc.static_assistant.ImageConverHelper;
import com.kulala.staticsfunc.static_assistant.UtilFileSave;
import com.kulala.staticsfunc.static_system.MD5;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import common.AppExecutors;
import common.GlobalContext;
import model.skin.DataCarSkin;
import model.skin.DataTempSetup;

public class ManagerSkins {
    private HashMap<String, Drawable> cacheZipPng;
    private HashMap<String, DataTempSetup> cacheTempSetup;
    private HashMap<String, DataCarSkin> cacheSkinSetup;
    public static final String TRANSPARENT = "TRANSPARENT";//TRANSPARENT
    public static final String DEFAULT_NAME_SKIN = "carskin_default";
    public static final String DEFAULT_NAME_TEMP = "controlskin_default";
    // ========================out======================
    private static ManagerSkins _instance;
    public static ManagerSkins getInstance() {
        if (_instance == null)
            _instance = new ManagerSkins();
        return _instance;
    }
    // =================================================
    public DataTempSetup getTempSetup(String fileMd5Name) {
        if(cacheTempSetup == null)return null;
        return cacheTempSetup.get(fileMd5Name);
    }
    public DataCarSkin getSkinSetup(String fileMd5Name) {
        if(cacheSkinSetup == null)return null;
        return cacheSkinSetup.get(fileMd5Name);
    }

    /**
     * ?????????????????????
     */
    public static  String getSkinZipFileName(String url) {
        if (TextUtils.isEmpty(url)) {//????????????????????????????????????
            return MD5.MD5generator(DEFAULT_NAME_SKIN);
        } else {
            return MD5.MD5generator(url);
        }
    }
    /**
     * ?????????????????????
     */
    public static String getTempZipFileName(String url) {
        if (TextUtils.isEmpty(url)) {//????????????????????????????????????
            return MD5.MD5generator(DEFAULT_NAME_TEMP);
        } else {
            return MD5.MD5generator(url);
        }
    }
    /**
     * url?????????????????????????????????????????????
     */
    public static String getCacheKey(boolean isPng,String url,String name) {
        if (isPng) {//????????????????????????????????????
            return getSkinFolder(GlobalContext.getContext())+"/"+MD5.MD5generator(url);
        } else {
            return getSkinFolder(GlobalContext.getContext())+"/"+MD5.MD5generator(url)+"/"+name;
        }
    }
    /**
     * ??????????????????
     */
    public static  String getSkinFolder(Context context) {
        return UtilFileSave.getSkinBaseDir(context);
    }
    // =======================oub==========================

    /**
     * key : ?????????,no pngName
     */
    public Drawable getPngImage(String key) {
        if(key == null)return null;
        if(key.length() == 0 || key.equals(TRANSPARENT)){
            Drawable trans = cacheZipPng.get("");
            if(trans == null)putPngImage("", new PaintDrawable(Color.TRANSPARENT));
            return cacheZipPng.get("");
        }
        if (cacheZipPng == null || cacheZipPng.get(key) == null) return null;//"?????????????????????");
        return cacheZipPng.get(key);
    }

    private void putPngImage(String key, Drawable image) {
        if (cacheZipPng == null) cacheZipPng = new HashMap<>();
        cacheZipPng.put(key, image);
    }

    // =======================oub==========================
    public interface OnLoadPngListener {
        void loadCompleted(Drawable image);//zipUSE dir+filename+skinname,sticker use dir+filename

        void loadFail(String errorInfo);
    }


    /***
     * skinFolder + fileName+pngName
     * ?????????????????????
     */
    public void loadSkin(final Context context, final String url, final String pngName, final OnLoadPngListener listener) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //????????????
                final String skinFolder = getSkinFolder(context);
                final String fileName = getSkinZipFileName(url);
                final String pngKey = getCacheKey(false,(TextUtils.isEmpty(url) ? DEFAULT_NAME_SKIN : url),pngName);
                //??????????????????
                Drawable cacheImage = getPngImage(pngKey);
                if (cacheImage != null) {
                    if (listener != null) listener.loadCompleted(cacheImage);
                    return;
                }
                if (BuildConfig.DEBUG) Log.e("loadSkin", "url:" + url + " pngName:" + pngName);
                //????????????
                if (UtilFileSave.isFileInDisk(skinFolder, fileName + ".zip")) {//???zip??????
                    if (BuildConfig.DEBUG) Log.e("loadSkin", "????????????");
                    if (BuildConfig.DEBUG) Log.e(".", "loadNextStep:listener "+listener);
                    loadNextStep(skinFolder,fileName,pngKey,listener);
                } else if(TextUtils.isEmpty(url)||url.equals("0")){//????????????,????????????
                    if (BuildConfig.DEBUG) Log.e("loadSkin", "????????????:??????");
                    UtilFileSave.copyResFileToDisk(context, skinFolder, DEFAULT_NAME_SKIN+".zip", fileName + ".zip");
                    loadNextStep(skinFolder,fileName,pngKey,listener);
                } else {//????????????,????????????
                    if (BuildConfig.DEBUG) Log.e("loadSkin", "???????????????:????????????"+url);

                    FileDownloader.getImpl().create(url)
                            .setPath(skinFolder + File.separator + fileName + ".zip")
                            .setForceReDownload(true)
                            .setListener(new FileDownloadListener() {
                                @Override//??????
                                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//??????????????????
                                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//????????????
                                protected void completed(BaseDownloadTask task) {
                                    if (BuildConfig.DEBUG) Log.e("??????", "loadSkin:????????????");
                                    loadNextStep(skinFolder,fileName,pngKey,listener);
                                }
                                @Override//??????
                                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//????????????
                                protected void error(BaseDownloadTask task, Throwable e) {
                                    if (BuildConfig.DEBUG) Log.e("??????", "loadSkin:????????????:"+e.toString());
                                    if (listener != null) listener.loadFail("loadSkin:????????????:"+e.toString());
                                }
                                @Override//?????????????????????
                                protected void warn(BaseDownloadTask task) {
                                    if (BuildConfig.DEBUG) Log.e("??????", "loadSkin:?????????????????????");
                                    if (listener != null) listener.loadFail("loadSkin:?????????????????????");
                                }
                            }).start();
                }
            }
        });

//            }
//        }).start();
    }


    /**
     * ??????
     */
    /***
     * skinFolder + fileName+pngName
     * ?????????????????????
     */
    public void loadTemp(final Context context, final String url, final String pngName, final OnLoadPngListener listener) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //????????????
                final String skinFolder = getSkinFolder(context);
                final String fileName = getTempZipFileName(url);
                final String pngKey = getCacheKey(false,(TextUtils.isEmpty(url) ? DEFAULT_NAME_TEMP : url),pngName);
                //??????????????????
                Drawable cacheImage = getPngImage(pngKey);
                if (cacheImage != null) {
                    if (listener != null) listener.loadCompleted(cacheImage);
                    return;
                }
                if (BuildConfig.DEBUG) Log.e("loadTemp", "url:" + url + " pngName:" + pngName);
                //????????????
                if (UtilFileSave.isFileInDisk(skinFolder, fileName + ".zip")) {//???zip??????
                    if (BuildConfig.DEBUG) Log.e("loadTemp", "????????????");
                    loadNextStep(skinFolder,fileName,pngKey,listener);
                } else if(TextUtils.isEmpty(url)){//????????????,????????????
                    if (BuildConfig.DEBUG) Log.e("loadTemp", "????????????:??????");
                    UtilFileSave.copyResFileToDisk(context, skinFolder, DEFAULT_NAME_TEMP+".zip", fileName + ".zip");
                    loadNextStep(skinFolder,fileName,pngKey,listener);
                } else {//????????????,????????????
                    if (BuildConfig.DEBUG) Log.e("loadTemp", "???????????????:??????");

                    FileDownloader.getImpl().create(url)
                            .setPath(skinFolder + File.separator + fileName + ".zip")
                            .setForceReDownload(true)
                            .setListener(new FileDownloadListener() {
                                @Override//??????
                                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//??????????????????
                                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//????????????
                                protected void completed(BaseDownloadTask task) {
                                    if (BuildConfig.DEBUG) Log.e("??????", "loadTemp:????????????");
                                    loadNextStep(skinFolder,fileName,pngKey,listener);
                                }
                                @Override//??????
                                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//????????????
                                protected void error(BaseDownloadTask task, Throwable e) {
                                    if (BuildConfig.DEBUG) Log.e("??????", "loadTemp:????????????:"+e.toString());
                                    if (listener != null) listener.loadFail("loadTemp:????????????:"+e.toString());
                                }
                                @Override//?????????????????????
                                protected void warn(BaseDownloadTask task) {
                                    if (BuildConfig.DEBUG) Log.e("??????", "loadTemp:?????????????????????");
                                    if (listener != null) listener.loadFail("loadTemp:?????????????????????");
                                }
                            }).start();

                }
            }
        });

//            }
//        }).start();
    }
    public void loadCarBg(final Context context, final String url, final String pngName, final OnLoadPngListener listener) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //????????????
                final String skinFolder = getSkinFolder(context);
                final String fileName = MD5.MD5generator(url);
                final String pngKey = getCacheKey(false,url,pngName);
                //??????????????????
                Drawable cacheImage = getPngImage(pngKey);
                if (cacheImage != null) {
                    if (listener != null) listener.loadCompleted(cacheImage);
                    return;
                }
                if (BuildConfig.DEBUG) Log.e("loadCarBg", "url:" + url + " pngName:" + pngName);
                //????????????
                if (UtilFileSave.isFileInDisk(skinFolder, fileName + ".zip")) {//???zip??????
                    if (BuildConfig.DEBUG) Log.e("loadCarBg", "????????????");
                    loadNextStep(skinFolder,fileName,pngKey,listener);
                }else if(TextUtils.isEmpty(url)||url.equals("0")){//????????????,????????????
                    if (BuildConfig.DEBUG) Log.e("loadSkin", "????????????:??????");
                    UtilFileSave.copyResFileToDisk(context, skinFolder, DEFAULT_NAME_SKIN+".zip", fileName + ".zip");
                    loadNextStep(skinFolder,fileName,pngKey,listener);
                }else {//????????????,????????????
                    if (BuildConfig.DEBUG) Log.e("loadCarBg", "??????");

                    FileDownloader.getImpl().create(url)
                            .setPath(skinFolder + File.separator + fileName + ".zip")
                            .setForceReDownload(true)
                            .setListener(new FileDownloadListener() {
                                @Override//??????
                                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//??????????????????
                                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//????????????
                                protected void completed(BaseDownloadTask task) {
                                    if (BuildConfig.DEBUG) Log.e("??????", "loadCarBg:????????????");
                                    loadNextStep(skinFolder,fileName,pngKey,listener);
                                }
                                @Override//??????
                                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//????????????
                                protected void error(BaseDownloadTask task, Throwable e) {
                                    if (BuildConfig.DEBUG) Log.e("??????", "loadCarBg:????????????:"+e.toString());
                                    if (listener != null) listener.loadFail("loadCarBg:????????????:"+e.toString());
                                }
                                @Override//?????????????????????
                                protected void warn(BaseDownloadTask task) {
                                    if (BuildConfig.DEBUG) Log.e("??????", "loadCarBg:?????????????????????");
                                    if (listener != null) listener.loadFail("loadCarBg:?????????????????????");
                                }
                            }).start();

                }
            }
        });

//            }
//        }).start();
    }


    /**
     * ?????????????????????zip?????????????????????
     */
    public void loadNextStep(String skinFolder,String fileName,String pngKey, OnLoadPngListener listener){
        if (!readZipFile(skinFolder,fileName))return;//??????????????????????????????
        Drawable localImage = getPngImage(pngKey);
        if (localImage != null) {
             if (BuildConfig.DEBUG) Log.e(".", "loadNextStep:listener "+listener);
            if (listener != null) {
                listener.loadCompleted(localImage);
            }
        } else {
            if (listener != null) listener.loadFail("???????????? localImage read Error");
        }
    }

    private String preNameForTestError = "";
    private int countNumNameForTestError = 0;

    private static boolean isUnzipIng = false;
    private static long preUnzipTime = 0;
    public boolean readZipFile(String skinFolder,String fileMd5Name) {
        long now = System.currentTimeMillis();
        if(now - preUnzipTime>30*1000L)isUnzipIng = false;
        preUnzipTime = now;
        if(isUnzipIng)return false;
        isUnzipIng = true;
        String fileDir = skinFolder+"/"+fileMd5Name + ".zip";
        try {
//            if (BuildConfig.DEBUG) Log.e("readZip", "start:" + fileDir);
            ZipFile zf = new ZipFile(fileDir);
            InputStream in = new BufferedInputStream(new FileInputStream(fileDir));
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                } else {
//                    if (BuildConfig.DEBUG) Log.e("readZip", ze.getName());
                    String[] nameArr = ze.getName().split("\\.");
                    if (nameArr != null && nameArr.length == 2 && nameArr[1].equals("png")) {//???png??????
                        if (nameArr[0].equals(preNameForTestError)) {
                            countNumNameForTestError++;
                            if (countNumNameForTestError >= 3) {
                                UtilFileSave.RecursionDeleteFile(new File(fileDir));
                                isUnzipIng = false;
                                return false;
                            }
                        } else {
                            countNumNameForTestError = 0;
                        }
                        preNameForTestError = nameArr[0];
                        if(ze.getName().contains("body")){
                             if (BuildConfig.DEBUG) Log.e("ttt","body");
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(zf.getInputStream(ze));
                        putPngImage(skinFolder+"/"+fileMd5Name + "/" + nameArr[0], new BitmapDrawable(GlobalContext.getContext().getResources(),bitmap));
                    }else if(nameArr != null && nameArr.length == 2 && nameArr[1].equals("txt")){//???????????????
                        InputStream inn = zf.getInputStream(ze);
                        InputStreamReader read        = new InputStreamReader(inn, "gb2312");
                        BufferedReader bufferedReader = new BufferedReader(read);
                        String            lineTxt     = null;
                        boolean isSkin = ze.getName().equals("setup.txt");
                        DataTempSetup tempSetup = new DataTempSetup();
                        DataCarSkin   skinSetup = new DataCarSkin();
                        while ((lineTxt = bufferedReader.readLine()) != null) {
                            if (lineTxt.length() > 0) {
                                if (!lineTxt.contains("//")) {//???????????????
                                    String[] arr = lineTxt.split(":");
                                    if (arr.length > 1) {
                                        String name = arr[0];
                                        if(isSkin)skinSetup.saveSkin(name, arr[1]);
                                        else tempSetup.saveSkin(name, arr[1]);
                                    }
                                }
                            }
                        }
                        if(isSkin){
                            if(cacheSkinSetup == null)cacheSkinSetup = new HashMap<>();
                            cacheSkinSetup.put(fileMd5Name,skinSetup);
                        }else{
                            if(cacheTempSetup == null)cacheTempSetup = new HashMap<>();
                            cacheTempSetup.put(fileMd5Name,tempSetup);
                        }
                    }
                }

            }
            zin.closeEntry();
            zin.close();
            in.close();

        } catch (IOException e) {
             if (BuildConfig.DEBUG) Log.e("readZip", "ERROR:" + e.getMessage());
            UtilFileSave.RecursionDeleteFile(new File(fileDir));
            isUnzipIng = false;
            return false;
        }
        if (BuildConfig.DEBUG) Log.e("readZip", "FIN:" + fileDir);
        isUnzipIng = false;
        return true;
    }
    /**
     * stickerFolder + "/" + fileName + UtilFileSave.PNG_CHANGE_NAME
     */
    public void loadPngFromUrl(final Context context, final String url, final OnLoadPngListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                final String skinFolder = getSkinFolder(context);
                final String fileName = MD5.MD5generator(url);
                final String pngKey = getCacheKey(true,url,"");
                //???????????????
                Drawable cacheImage = getPngImage(pngKey);
                if (cacheImage != null) {
                    if (listener != null) listener.loadCompleted(cacheImage);
                    return;
                }
                //?????????????????????
                if (UtilFileSave.isFileInDisk(skinFolder, fileName + UtilFileSave.PNG_CHANGE_NAME)) {//???png??????
                    Drawable localImage = getPngImage(url);
                    if (localImage != null) {
                        if (listener != null) listener.loadCompleted(localImage);
                    } else {
                        localImage = ImageConverHelper.getImageDrawableFromFile(skinFolder + "/" + fileName + UtilFileSave.PNG_CHANGE_NAME);
                        if (localImage != null) {
                            putPngImage(pngKey, localImage);
                            if (listener != null) listener.loadCompleted(localImage);
                        } else {
                            if (listener != null) listener.loadFail("localImage read Error");
                        }
                    }
                } else {//??????????????????

                    FileDownloader.getImpl().create(url)
                            .setPath(skinFolder + File.separator + fileName + UtilFileSave.PNG_CHANGE_NAME)
                            .setForceReDownload(true)
                            .setListener(new FileDownloadListener() {
                                @Override//??????
                                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//??????????????????
                                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//????????????
                                protected void completed(BaseDownloadTask task) {
                                    Drawable localImage = ImageConverHelper.getImageDrawableFromFile(skinFolder + "/" + fileName + UtilFileSave.PNG_CHANGE_NAME);
                                    if (localImage != null) {
                                        putPngImage(pngKey, localImage);
                                        if (listener != null) listener.loadCompleted(localImage);
                                    } else {
                                        if (listener != null)
                                            listener.loadFail("download Image read Error");
                                    }
                                }
                                @Override//??????
                                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                }
                                @Override//????????????
                                protected void error(BaseDownloadTask task, Throwable e) {
                                    if (listener != null) listener.loadFail("????????????:"+e.toString());
                                }
                                @Override//?????????????????????
                                protected void warn(BaseDownloadTask task) {
                                    if (listener != null) listener.loadFail("?????????????????????");
                                }
                            }).start();
                }
            }
        }).start();
    }
}
