package view.basicview;

import android.util.Log;

import com.client.proj.kusida.BuildConfig;

public class MyLog {
    public static void loge(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.e(tag,msg);
        }
    }
}
