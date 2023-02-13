package common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/7/19.
 */
public class LoadPermissions {
    private static WeakReference<LoadPermissions> WeakReferenceInstance;
    private static final int      REQUEST_MULTIPLE_PERMISSIONS = 124;
    private String[] permissionArr = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };
    // Manifest.permission.READ_PHONE_STATE,
    //        Manifest.permission.RECORD_AUDIO,
    //    Manifest.permission.WRITE_SETTINGS, 写系统权限，无法调用 需要用系统签名或者成为系统预装软件才能够申请此权限
    private OnAllPermissionGranted onAllPermissionGranted;

    public interface OnAllPermissionGranted {
        void onAllGranted();
        void onDined();
    }
    public void setOnAllPermissionGranted(OnAllPermissionGranted listener) {
        this.onAllPermissionGranted = listener;
    }
    //=================================================
    private static common.LoadPermissions _instance;
    public LoadPermissions() {
    }
    public static common.LoadPermissions getInstance() {
        if (_instance == null)
            _instance = new common.LoadPermissions();
        return _instance;
    }


    //=================================================

    private static boolean isChecking = false;
    public void loadPermissionAll(Activity activity) {
        if (isChecking) return;
        isChecking = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsNeeded = new ArrayList<String>();
            for (String per : permissionArr) {
                int permission = activity.checkSelfPermission(per);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    permissionsNeeded.add(per);
                }
            }
            if (permissionsNeeded.size() > 0) {
                //首次安装，先关蓝牙，不然一直扫，监控不到
//                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                if (bluetoothAdapter != null)bluetoothAdapter.disable();
                activity.requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), REQUEST_MULTIPLE_PERMISSIONS);
            } else {//已有所有权限
                if (onAllPermissionGranted != null)
                    onAllPermissionGranted.onAllGranted();//6.0以下的不用请求
            }
        } else {
            if (onAllPermissionGranted != null) onAllPermissionGranted.onAllGranted();//6.0以下的不用请求
        }
        isChecking = false;
    }
    private int denedCount;
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, Activity activity) {
        if(BuildConfig.DEBUG){
            Log.e("权限", ""+ Arrays.toString(permissions)+Arrays.toString(grantResults));
        }
        boolean isHaveAllPermision=true;
        switch (requestCode) {
            case common.LoadPermissions.REQUEST_MULTIPLE_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    for (String permiss : permissionArr) {
                        if (permiss.equals(permissions[i])) {//是需要确定的权限
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//有权限没有确认
                                isHaveAllPermision=false;
//                                loadPermissionAll(activity);
//                                return;
                            }
                        }
                    }
                }
                if(isHaveAllPermision){
                    if (onAllPermissionGranted != null) onAllPermissionGranted.onAllGranted();//所有权限都已确认
                }else{
                    denedCount++;
                    if(denedCount==1){
                        loadPermissionAll(activity);
                    }else{
                        if (onAllPermissionGranted != null) onAllPermissionGranted.onDined();//所有权限有部分没通过
                    }
                }
            }
            break;
        }
    }
    public void checkPermissionIO(Activity activity) {
        if(activity == null)return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionRead   = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionWrite  = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //读写权限
            if (permissionRead != PackageManager.PERMISSION_GRANTED|| permissionWrite != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    public static final boolean isOpenGps(final Context context) {
        if(context!=null){
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gps || network) return true;
        }
        return false;
    }
    public static final void openGPS(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent); // 设置完成后返回到原来的界面
    }
}