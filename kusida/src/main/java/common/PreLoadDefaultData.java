package common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;

import model.ManagerSkins;

/**
 * Created by Administrator on 2017/4/26.
 * 一般用于Flash页面，加载初始数据
 */

public class PreLoadDefaultData {
    // ========================out======================
    private static PreLoadDefaultData                    _instance;
    private PreLoadDefaultData() {
    }
    public static PreLoadDefaultData getInstance() {
        if (_instance == null)
            _instance = new PreLoadDefaultData();
        return _instance;
    }
    public interface OnPreLoadedListener{
        void loadCompleted();
    }
    // =================================================
    public void loadDefault(final Context context,final OnPreLoadedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //1.加载控制面板皮肤
                ManagerSkins.getInstance().loadTemp(context, "","control_normal_0", new ManagerSkins.OnLoadPngListener() {
                    @Override
                    public void loadCompleted(Drawable image) {
                         if (BuildConfig.DEBUG) Log.e("LOAD_DEFAULT", "控制面板皮肤加载完成>>>");
                        //2.加载车辆默认皮肤
                        ManagerSkins.getInstance().loadSkin(context, "","body",null);
                        if (listener != null) listener.loadCompleted();
                    }

                    @Override
                    public void loadFail(String errorInfo) {
                         if (BuildConfig.DEBUG) Log.e("LOAD_DEFAULT", "黑认皮肤加载失败>>>");
                    }
                });
            }
        }).start();
    }

}
