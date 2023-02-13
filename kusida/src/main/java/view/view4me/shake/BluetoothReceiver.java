package view.view4me.shake;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.kulala.linkscarpods.MytoolsGetPackageName;
import com.kulala.linkscarpods.blue.BlueAdapter;
import com.kulala.staticsview.toast.ToastTxt;

import java.util.Set;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import model.ManagerCarList;
import model.carlist.DataCarInfo;


public class BluetoothReceiver extends BroadcastReceiver{
	private Context mContext;
	private static boolean alreadyRegCanDestory = false;
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	String pin = "123456";  //�˴�Ϊ��Ҫ���ӵ������豸�ĳ�ʼ��Կ��һ��Ϊ1234��0000
	public BluetoothReceiver() {
		
	}
	public static BluetoothReceiver BluetoothReceiverThis;
	private static BluetoothReceiver _instance;

	public static BluetoothReceiver getInstance() {
		if (_instance == null) {
			_instance = new BluetoothReceiver();
		}
		return _instance;
	}
	public void initReceiver(Context context) {
		if (!alreadyRegCanDestory) {//Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
			BluetoothReceiverThis = new BluetoothReceiver();
			try {
				IntentFilter filter = new IntentFilter();
				filter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
				filter.addAction("android.bluetooth.device.action.FOUND");
				mContext = context;
                context.registerReceiver(BluetoothReceiverThis, filter, MytoolsGetPackageName.getBroadCastPermision(),null);
//				context.registerReceiver(BluetoothReceiverThis, filter);
				alreadyRegCanDestory = true;
				if (BuildConfig.DEBUG) Log.e("BluetoothReceiver", "initReceiver");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//        Intent intentA = new Intent(GlobalContext.getContext(), KulalaServiceA.class);
//        intentA.setPackage(GlobalContext.getContext().getPackageName());
//        GlobalContext.getContext().bindService(intentA,conn,BIND_AUTO_CREATE);
	}
	public void unRegReceiver() {
		if (alreadyRegCanDestory && BluetoothReceiverThis != null && mContext != null) {
			mContext.unregisterReceiver(BluetoothReceiverThis);
			BluetoothReceiverThis = null;
			alreadyRegCanDestory = false;
			mContext = null;
			if (BuildConfig.DEBUG) Log.e("BluetoothReceiver", "unRegReceiver");
		}
	}
	//�㲥����������Զ�������豸������ʱ���ص�����onReceiver()�ᱻִ�� 
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction(); //�õ�action
		Log.e("action1=", action);
		BluetoothDevice btDevice=null;  //����һ������device����
		 // ��Intent�л�ȡ�豸����
		btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		BluetoothDevice device=bluetoothAdapter.getRemoteDevice(btDevice.getAddress());
		if(BluetoothDevice.ACTION_FOUND.equals(action)){  //�����豸
			if(!TextUtils.isEmpty(btDevice.getName())){
				Log.e("pppaskdakoda", "["+btDevice.getName()+"]"+":"+btDevice.getAddress());
			}
//			if(btDevice.getName().contains("NFC"))//NFC�豸����ж������һ���ѵ����Ǹ��ᱻ���ԡ�
	DataCarInfo currentCar=		ManagerCarList.getInstance().getCurrentCar();
		if(currentCar!=null&& !TextUtils.isEmpty(currentCar.keyBlueName)&&!TextUtils.isEmpty(btDevice.getName())){
			if(btDevice.getName().equals(currentCar.bluetoothName))
			{
				if (btDevice.getBondState() == BluetoothDevice.BOND_NONE) {

					Log.e("ywq", "attemp to bond:"+"["+btDevice.getName()+"]");
					try {
						//ͨ��������ClsUtils,����createBond����
						DataCarInfo car=	ManagerCarList.getInstance().getCurrentCar();
						Set<BluetoothDevice> deviceList1=BlueAdapter.getInstance().getBondedDevice(context);
						boolean isHaveBond=false;
						for (BluetoothDevice devices : deviceList1) {
							if (BuildConfig.DEBUG)   Log.e("主机蓝牙", "已配对的蓝牙名字" +devices.getName()+"已配对的蓝牙地址"+devices.getAddress());
							if(!TextUtils.isEmpty( car.bluetoothName)&&!TextUtils.isEmpty(devices.getName())&&car.bluetoothName.equals(devices.getName())){
								isHaveBond=true;
								new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("当前设备已配对过").quicklyShow();
								break;
							}
						}
						if(!isHaveBond){
							ClsUtils.createBond(btDevice.getClass(), btDevice);
							BlueLinkReceiver.getInstance().needChangeCar(car.ide,car.bluetoothName,car.blueCarsig,car.isBindBluetooth,car.carsig,car.isMyCar);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		}else if(action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) //�ٴεõ���action�������PAIRING_REQUEST
		{
			Log.e("action2=", action);
			DataCarInfo currentCar=		ManagerCarList.getInstance().getCurrentCar();
			if(currentCar==null|| TextUtils.isEmpty(currentCar.keyBlueName)){
				return;
			}
			if(btDevice.getName().equals(currentCar.bluetoothName))
			{
				Log.e("here", "OKOKOK");
				
				try {
					
					//1.ȷ�����
					ClsUtils.setPairingConfirmation(btDevice.getClass(), btDevice, true);
					//2.��ֹ����㲥
					Log.i("order...", "isOrderedBroadcast:"+isOrderedBroadcast()+",isInitialStickyBroadcast:"+isInitialStickyBroadcast());
					abortBroadcast();//���û�н��㲥��ֹ��������һ��һ����������Կ�
					//3.����setPin�����������...
//					boolean ret = ClsUtils.setPin(btDevice.getClass(), btDevice, pin);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else
				Log.e("��ʾ��Ϣ", "����豸����Ŀ�������豸");
			
		}
	}
}