package view.view4me.myblue;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by Administrator on 2017/7/14.
 *
 int result = BluePermission.checkPermission(GlobalContext.getCurrentActivity());
 switch (result) {
 case 0:BluePermission.openBlueTooth(GlobalContext.getCurrentActivity());return 0;//请求打开蓝牙
 case 1:break;//正常
 case 9:return 9;//打开权限
 }
 */

public class LcdBluePermission {
    /**
     * 没有权限就主动开权限
     * 1.成功
     * 2."设备需要android5.0以上手机"
     * 3."没有蓝牙模块"
     * 4."传值错"
     * 5. 没有蓝牙模块
     * 6. 没有打开蓝牙
     * 9.activity去请求权限
     */
    public static int checkPermission(Activity context){
        int result = checkPermissions(context);
        if(result == 9) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0+
                context.requestPermissions(new String[]{
                        android.Manifest.permission.BLUETOOTH
                        , android.Manifest.permission.BLUETOOTH_ADMIN
                        , android.Manifest.permission.ACCESS_COARSE_LOCATION
                        , android.Manifest.permission.ACCESS_FINE_LOCATION
                }, 1);
            }
            return 9;
        }
        return result;
    }
    private static int checkPermissions(Context context){
        if(context == null)return 4;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)return 2;//"android版本过低，请升级至5.0以上";
        if (!context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le"))return 3;//"没有硬件蓝牙"
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) return 5;//没有蓝牙模块
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0+
            int permission1 = context.checkSelfPermission(android.Manifest.permission.BLUETOOTH);
            int permission2 = context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_ADMIN);
            int permission4 = context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int permission5 = context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            //蓝牙权限
            if (permission1 != PackageManager.PERMISSION_GRANTED ||
                    permission2 != PackageManager.PERMISSION_GRANTED
                    || permission4 != PackageManager.PERMISSION_GRANTED
                    || permission5 != PackageManager.PERMISSION_GRANTED
                    ) {
                return 9;//没有权限
            }
        }
        if (!bluetoothAdapter.isEnabled())return 6;//没有打开蓝牙
        return 1;
    }
    public static void openBlueTooth(Activity context) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // 设置蓝牙可见性，最多300秒
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);//[1<->3600] s ，为 0 时，表示打开 Bluetooth 之后一直可见，设置小于0或者大于3600 时，系统会默认为 120s。
        context.startActivity(intent);
    }
}
