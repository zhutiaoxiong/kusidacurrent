package view.view4me.lcdkey;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;

import view.view4me.myblue.MyLcdBlueAdapter;
import view.view4me.myblue.DataReceive;
import view.view4me.myblue.OnBlueStateListenerRoll;

public class MyOnBlueStateListenerRoll implements OnBlueStateListenerRoll {
    @Override
    public void onConnecting() {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "onConnecting: ");
    }

    @Override
    public void onConnectedOK() {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "onConnectedOK: ");

        MyLcdBlueAdapter.getInstance().gotoDiscoverService();
    }

    @Override
    public void onConnectedFailed(String error, boolean isNodevice) {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "onConnectedFailed: ");
        if(lister!=null){
            lister.onConnectedFailed();
        }
    }

    @Override
    public void onDiscovering() {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "onDiscovering: ");
    }

    @Override
    public void onDiscoverOK() {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "onDiscoverOK: ");

    }

    @Override
    public void onDiscoverFailed(String error, boolean isNoList) {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "onDiscoverFailed: ");

    }

    @Override
    public void onMessageSended(byte[] bytes) {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "onMessageSended: ");
    }

    @Override
    public void onDataBack() {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "onDataBack: ");
    }

    @Override
    public void onDataReceived(DataReceive data) {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "onDataReceived: ");
    }

    @Override
    public void onReadRemoteRssi(int rssi, int status) {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "onReadRemoteRssi: ");
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        //通道设置成功
        //发消息
        if(lister!=null){
            lister.onDescriptorWrite();
        }
    }

    @Override
    public void needLog(String log) {
         if (BuildConfig.DEBUG) Log.e("BindLcdKeyActivity", "needLog: ");
    }
    public interface OnonDescriptorWriteLister{
        void onDescriptorWrite();
        void onConnectedFailed();
    }
    private OnonDescriptorWriteLister lister;
    public MyOnBlueStateListenerRoll(OnonDescriptorWriteLister listerm){
        this.lister=listerm;
    }
}
