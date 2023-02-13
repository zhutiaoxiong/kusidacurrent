package com.kulala.staticsfunc;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.ODateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * 使用此方法一定要先初始化init ,并设置要存放的标题setSaveMatchName
 */
public class LogMe {
    private static Context mContext;
    public static void init(Context context){
        mContext = context;
    }
    //==============================================
    //一定加入报告
    public static void e(String logName, String logValue) {
        if(logName ==null || logValue == null || logName.length() == 0)return;
        if(mContext == null)return;
        if(logName.equals("handleException")){
             if (BuildConfig.DEBUG) Log.e("OLog",logName+">>>"+logValue);
            putLog(logName, logValue, false);
        }
//        else if(logName.equals("RefreshViewHeart")){
//             if (BuildConfig.DEBUG) Log.e(logName,logValue);
////            putLog(logName, logValue, false);
//        }
//        else if(logName.equals("blue")){
//             if (BuildConfig.DEBUG) Log.e("OLog",logName+">>>"+logValue);
////            putLog(logName, logValue, false);
//        }
    }
    private static int todaySaveCount = 0;
    private static void putLog(final String logName, final String logValue, final boolean isClear) {
        if(mContext == null)return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isNeedClear = isClear;
                    long now = System.currentTimeMillis();
                    String cachelog = todaySaveCount +":"+ODateTime.time2StringHHmmss(now) + ":" + logName +"__" + logValue +"\n\r";
                    todaySaveCount++;
                    //Environment.getExternalStorageDirectory()
                    File file =  mContext.getExternalCacheDir();
                    file = new File(file, "/MessageFiles");
                    file.mkdirs();
                    file = new File(file, "LogMeSock.txt");
                    FileOutputStream out;
                    if (isNeedClear) {//大于4小时,,now-lastClearTime >14400 &&
                        out = new FileOutputStream(file, false);
                    } else {
                        if (file.exists()) {
                            int size = new FileInputStream(file).available();
                            int sizeKB = size/1024;
                            if(sizeKB>300){
                                out = new FileOutputStream(file, false);
                            }else {
                                out = new FileOutputStream(file, true);
                            }// （文件路径和文件的写入方式如果为真则说明文件以尾部追加的方式写入，当为假时则写入的文件覆盖之前的内容）
                        } else {
                            out = new FileOutputStream(file, false);
                        }
                    }
//					byte[] byts = cachelog.getBytes(HttpUtil.UTF8);
                    byte[] byts = cachelog.getBytes();
                    out.write(byts, 0, byts.length);
                    out.close();
                } catch (FileNotFoundException e) {
                     if (BuildConfig.DEBUG) Log.e("OLog","OLog FileNotFoundException"+e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    // ===========================================================================
}
