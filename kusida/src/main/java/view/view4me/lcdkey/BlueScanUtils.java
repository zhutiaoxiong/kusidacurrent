package view.view4me.lcdkey;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;

public class BlueScanUtils {
    private static BlueScanUtils _instance;
    private Context mContext;
    protected BlueScanUtils() {
    }
    private BluetoothAdapter mBluetoothAdapter;
    public static BlueScanUtils getInstance() {
        if (_instance == null)
            _instance = new BlueScanUtils();
        return _instance;
    }
    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//            mContext.registerReceiver(mBluetoothReceiver, filter);
        }
    }
    /**
     * 开始扫描蓝牙设备
     */
    private void startScan() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.startDiscovery();
        }
    }

    /**
     * 取消扫描
     */
    private void cancelScan() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

//    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                int rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
//                addDevice(device, rssi);
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
//                sendDevices();
//            }
//        }
//    };

//    /**
//     * 添加设备
//     */
//    private void addDevice(BluetoothDevice device, int rssi) {
//        BLeDevice bLeDevice = new BLeDevice(device.getName(), device.getAddress(), rssi);
//        Iterator<BLeDevice> iterator = mDeviceList.iterator();
//        while (iterator.hasNext()) {
//            if (device.getAddress().equals(iterator.next().getAddress()))
//                iterator.remove();  //如果多次扫描到同一台设备，则移除之前的设备
//        }
//        mDeviceList.add(bLeDevice);
//    }
//
//    /**
//     * 将扫描到的设备发送到前台
//     */
//    private void sendDevices() {
//        Message msg = new Message();
//        msg.obj = mDeviceList;
//        mHandler.sendMessage(msg);
//    }

}
