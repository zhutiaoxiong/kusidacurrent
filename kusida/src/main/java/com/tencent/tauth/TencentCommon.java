package com.tencent.tauth;

import android.content.Context;
import android.content.Intent;

/**
 * Created by qq522414074 on 2017/4/7.
 */

public class TencentCommon {
    public static void toTencent(Context context,String title,String info,String url,int flag,String sharePic){
        Intent intent= new Intent();
        intent.setClass(context, ShareTotencent.class);
        intent.putExtra("title",title);
        intent.putExtra("info",info);
        intent.putExtra("url",url);
        intent.putExtra("flag",flag);
        intent.putExtra("sharePic",sharePic);
        context. startActivity(intent);//无返回值的调用,启动一个明确的activity
    }
}
