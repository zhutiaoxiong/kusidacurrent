package com.kulala.staticsfunc.static_view_change;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageHttpLoader {
    private static ImageHttpLoader _instance;
    private ImageHttpLoader() {
    }
    public static ImageHttpLoader getInstance() {
        if (_instance == null)
            _instance = new ImageHttpLoader();
        return _instance;
    }
    @SuppressLint("HandlerLeak")
    public void asyncloadImage(final Context context,final ImageView image, final int resid) {
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    Uri uri = (Uri) msg.obj;
                    if (image != null && uri != null) {
                        image.setImageURI(uri);
                    }
                }
            }
        };
        // 子线程，开启子线程去下载或者去缓存目录找图片，并且返回图片在缓存目录的地址
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Uri uri = Uri.parse("android.resource://"+ context.getPackageName()+"/"+resid);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = uri;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
    @SuppressLint("HandlerLeak")
    public void asyncloadImageLocal(final ImageView image, final String path) {
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    Uri uri = (Uri) msg.obj;
                    if (image != null && uri != null) {
                        image.setImageURI(uri);
//                        image.setTag(path);
                    }
                }
            }
        };
        // 子线程，开启子线程去下载或者去缓存目录找图片，并且返回图片在缓存目录的地址
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Uri uri = Uri.parse(path);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = uri;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
    /**
     * 采用普通方式异步的加载图片,final 异步只能加一个,此方式有问题，不要用
     */
//    @SuppressLint("HandlerLeak")
//    public void asyncloadImage(final ImageView iv_header, final String http, final String local) {
//        final Handler mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (msg.what == 1) {
//                    Uri uri = (Uri) msg.obj;
//                    if (iv_header != null && uri != null) {
//                        iv_header.setImageURI(uri);
//                    }
//
//                }
//            }
//        };
//        // 子线程，开启子线程去下载或者去缓存目录找图片，并且返回图片在缓存目录的地址
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // 这个URI是图片下载到本地后的缓存目录中的URI
//                    Uri uri = getImageURI(http, local);
//                    Message msg = new Message();
//                    msg.what = 1;
//                    msg.obj = uri;
//                    mHandler.sendMessage(msg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        new Thread(runnable).start();
//    }
    /**
     * 从网络上获取图片，如果图片在本地存在的话就直接拿，如果不存在再去服务器上下载图片 这里的path是图片的地址
     */
    private Uri getImageURI(Context context,String path, String localname) throws Exception {
        // 如果图片存在本地缓存目录，则不去服务器下载.getCacheDir()

        File file = context.getCacheDir();
//		File file = Environment.getExternalStorageDirectory();
        file = new File(file,"/data/MessageFiles");
        file.mkdirs();
        file = new File(file, localname);
        if (file.exists()) {
            return Uri.fromFile(file);// Uri.fromFile(path)这个方法能得到文件的URI
        } else {
            // 从网络上获取图片
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                file.createNewFile();
                FileOutputStream out;
                out = new FileOutputStream(file, false);// （文件路径和文件的写入方式如果为真则说明文件以尾部追加的方式写入，当为假时则写入的文件覆盖之前的内容）
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                is.close();
                out.close();
                // 返回一个URI对象
                Uri uuu = Uri.fromFile(file);
                file = null;
                return uuu;
            }
        }
        return null;
    }
    // =================================================
}
