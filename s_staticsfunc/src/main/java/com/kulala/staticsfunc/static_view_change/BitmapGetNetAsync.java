package com.kulala.staticsfunc.static_view_change;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kulala.staticsfunc.BuildConfig;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 从网络上获取图片转为bitmap,如果图片存在本地缓存目录，则不去服务器下载
 */
public class BitmapGetNetAsync {
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT    = 10000;
    private static CacheImage        cacheImage;
    public interface OnGetImageListener{
        void onGetImage(Bitmap bitmap);
    }
    //========================================================
    public void findImage(final Context context, final String url,final OnGetImageListener onGetImageListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //初始化图片缓存类
                if (cacheImage == null) {
                    cacheImage = new CacheImage(context);
                }
                //读取图片依次顺序，内存->本地->网络
                Bitmap bitmap = null;
                if (url != null) {
                    //内存->本地
                    bitmap = cacheImage.get(url);
                    if (bitmap == null) {
                        //网络
                        bitmap = getBitmapFromUrl(url);
                        if (bitmap != null) {
                            //bitmap加入缓存
                            cacheImage.put(url, bitmap);
                        }
                    }
                }
                if(bitmap!=null)onGetImageListener.onGetImage(bitmap);
                else onGetImageListener.onGetImage(null);
            }
        }).start();
    }
    private Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            bitmap = BitmapFactory.decodeStream((InputStream) conn.getContent());
        } catch (Exception e) {
             if (BuildConfig.DEBUG) Log.e("BitmapGetNetAsync", e.toString());
        }
        return bitmap;
    }
}
