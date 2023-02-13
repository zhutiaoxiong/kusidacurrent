package com.kulala.linkscarpods.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;


import com.kulala.linkspods.BuildConfig;

import java.lang.reflect.Method;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Set;
/**
 * Created by Administrator on 2018/5/11.
 */

public class BlueGet {
    public static BluetoothDevice getBlueByName(String deviceName){
        if(deviceName == null || deviceName.length()==0)return null;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {//得到连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);

            if(state == BluetoothAdapter.STATE_CONNECTED){
                Log.i("BLUETOOTH","BluetoothAdapter.STATE_CONNECTED");
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                Log.i("BLUETOOTH","devices:"+devices.size());

                for(BluetoothDevice device : devices){
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if(isConnected){
                        Log.i("BLUETOOTH","connected:"+device.getName());
                        if(deviceName.equals(device.getName()))return device;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static MyBluetoothDevice getDeviceFromList(String blueName, List<MyBluetoothDevice> listDevice) {
        if (listDevice == null || listDevice.size() == 0 || blueName == null || blueName.length() == 0){
             if (BuildConfig.DEBUG) Log.e("getDeviceFromList", "no listDevice:"+" : "+blueName);
            return null;
        }
        try {
            for (MyBluetoothDevice device : listDevice) {
                if (device != null && device.getName() != null && device.getName().length() > 0) {
//                     if (BuildConfig.DEBUG) Log.e("getDeviceFromList", "device:"+device.getName()+" "+device.getName().length()
//                    +blueName +" "+blueName.length());
                    if (device.getName().contains(blueName)) return device;
                }
            }
        } catch (ConcurrentModificationException | NullPointerException e) {
             if (BuildConfig.DEBUG) Log.e("getDeviceFromList", "no device:"+e.toString());
            return null;
        }
        if (BuildConfig.DEBUG) Log.e("getDeviceFromList", "no device");
        return null;
    }

    public static BluetoothDevice getDeviceAderessFromList(String blueAderess, List<BluetoothDevice> listDevice) {
         if (BuildConfig.DEBUG) Log.e("BlueLinkControlLcdKey", "blueAderess"+blueAderess+"掃出來的設備"+listDevice.size());
        if (listDevice == null || listDevice.size() == 0 || blueAderess == null || blueAderess.length() == 0){
             if (BuildConfig.DEBUG) Log.e("getDeviceFromList", "no listDevice:"+listDevice.size()+" : "+blueAderess);
            return null;
        }
        try {
            for (BluetoothDevice device : listDevice) {
                if (device != null && device.getAddress() != null && device.getAddress().length() > 0) {
                     if (BuildConfig.DEBUG) Log.e("BlueLinkControlLcdKey", "---------------------------------------------------------device.getAddress()"+device.getAddress()+"当前地址"+blueAderess);
//                    +blueName +" "+blueName.length());
                    if (device.getAddress().equals(blueAderess)) return device;
                }
            }
        } catch (ConcurrentModificationException | NullPointerException e) {
             if (BuildConfig.DEBUG) Log.e("BlueLinkControlLcdKey", "no device:"+e.toString());
            return null;
        }
        if (BuildConfig.DEBUG) Log.e("BlueLinkControlLcdKey", "no device");
        return null;
    }
}
