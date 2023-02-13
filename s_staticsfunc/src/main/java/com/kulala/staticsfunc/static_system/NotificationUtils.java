package com.kulala.staticsfunc.static_system;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.kulala.staticsfunc.BuildConfig;
import com.kulala.staticsfunc.R;
/**
 * Created by Administrator on 2018/2/7.
 NotificationUtils notificationUtils = new NotificationUtils(this);
 notificationUtils.sendNotification("测试标题", "测试内容",R.drawable.kulala_icon);
 */

public class NotificationUtils extends ContextWrapper {
    public static final int NOTI_ID = 6413;

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    public NotificationUtils(Context context){
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content,int iconId){
        if (BuildConfig.DEBUG)Log.e("Notification","getChannelNotification>>> iconId:"+iconId);
        Intent        intentjump    = getPackageManager().getLaunchIntentForPackage(getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(this,120,intentjump,PendingIntent.FLAG_UPDATE_CURRENT);
        return new Notification.Builder(getApplicationContext(), id)
//                .setTicker(getApplicationContext().getPackageName()+"通知")// 顶部
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(iconId)
                .setOngoing(true)//true不能滑动删除
                .setAutoCancel(false)//是否点击就消失NN
                .setSound(null,null)
                .setVibrate(new long[]{0})
                .setContentIntent(pendingIntent);
    }
    public NotificationCompat.Builder getNotification_25(String title, String content,int iconId){
        if (BuildConfig.DEBUG)Log.e("Notification","getNotification_25>>>");
        Intent        intentjump    = getPackageManager().getLaunchIntentForPackage(getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(this,120,intentjump,PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext())
//                .setTicker(getApplicationContext().getPackageName()+"通知")// 顶部
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(iconId)
                .setOngoing(true)//true不能滑动删除
                .setAutoCancel(false)//是否点击就消失
                .setSound(null)
                .setVibrate(new long[]{0})
                .setContentIntent(pendingIntent);
    }
    public Notification sendNotification(String title, String content){
        int useIcon = R.drawable.noti_logo;
        if(useIcon == 0)return null;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createNotificationChannel();
            Notification notification = getChannelNotification(title, content,useIcon).build();
            getManager().notify(NOTI_ID,notification);
            return notification;
        }else{
            Notification notification = getNotification_25(title, content,useIcon).build();
            getManager().notify(NOTI_ID,notification);
            return notification;
        }
    }
    public Notification sendNotifi(String title, String content){
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        int useIcon = R.drawable.noti_logo;

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing(true)//true不能滑动删除
                .setAutoCancel(false)//是否点击就消失
                .setSmallIcon(useIcon)
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setVibrate(new long[]{0})
                .setSound(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("to-do", title,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId("to-do");
        }

        Notification notification = builder.build();
        notificationManager.notify(NOTI_ID, notification);
        return notification;
    }
}
