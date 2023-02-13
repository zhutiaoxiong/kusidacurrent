package common;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;

import view.ActivityKulalaMain;

public class BuglyInitSet {
    /**
     * 初始化bugly版本升级
     */
    public static void initBuglyUpdate() {
        /***** Beta高级设置 *****/
        /**
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
//        Beta.autoInit = true;
        /**
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
//        Beta.autoCheckUpgrade = true;
        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 60 * 1000;

        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
//        Beta.initDelay = 1 * 1000;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
//        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
//        Beta.showInterruptedStrategy = true;

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(ActivityKulalaMain.class);

        /**
         * 自定义布局
         */
//        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;//关键代码写这个布局添加自己想要的

        /**
         * 设置自定义tip弹窗UI布局
         * 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
         *  标题：beta_title，如：android:tag="beta_title"
         *  提示信息：beta_tip_message 如： android:tag="beta_tip_message"
         *  取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
         *  确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
         *  详见layout/tips_dialog.xml
         */
        //Beta.tipsDialogLayoutId = R.layout.tips_dialog;

//        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
//            @Override
//            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
//
//                // 通过tag方式获取控件，并更改布局内容
//                TextView textView = (TextView) view.findViewWithTag("beta_upgrade_feature");
//                // 更多的操作：比如设置控件的点击事件
//                textView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
//
//            }
//
//            @Override
//            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
//
//                // 注：可通过这个回调方式获取布局的控件，如果设置了id，可通过findViewById方式获取，如果设置了tag，可以通过findViewWithTag，具体参考下面例子:
//
//
//            }
//
//            @Override
//            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {
//
//            }
//
//            @Override
//            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {
//
//            }
//
//            @Override
//            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
//
////                ToastUtils.showGravityToast(MyApplication.this,"更新后注意通知栏下载进度...");
//            }
//
//        };


    }
}
