package com.kulala.staticsview.tools;


import android.app.Activity;
import android.content.Intent;

public class ActivityUtils {
    public static void startActivity(Activity from,Class<? extends Activity> clazz) {
        if (from == null) return;
        Intent intent = new Intent(from, clazz);
        from.startActivity(intent);
    }
    public static void startActivityTakeData(Activity from,Class<? extends Activity> clazz,String condition) {
        if (from == null) return;
        Intent intent = new Intent(from, clazz);
        intent.putExtra("condition",condition);
        from.startActivity(intent);
    }
    public static void startActivityTakeData(Activity from,Class<? extends Activity> to,long condition) {
        if (from == null) return;
        Intent intent = new Intent(from, to);
        intent.putExtra("condition",condition);
        from.startActivity(intent);
    }
    public static void startActivityTakeData(Activity from,Class<? extends Activity> clazz,String condition,String phoneNum) {
        if (from == null) return;
        Intent intent = new Intent(from, clazz);
        intent.putExtra("condition",condition);
        intent.putExtra("phoneNum",phoneNum);
        from.startActivity(intent);
    }
    public static void startActivityTakeOneData(Activity from,Class<? extends Activity> clazz,String key,String value) {
        if (from == null) return;
        Intent intent = new Intent(from, clazz);
        intent.putExtra(key,value);
        from.startActivity(intent);
    }
}
