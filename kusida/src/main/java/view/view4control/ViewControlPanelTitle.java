package view.view4control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.service.NetManager;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.static_interface.OnClickListnerZhu;
import com.kulala.staticsview.toast.ToastTxt;
import com.kulala.staticsview.tools.ActivityUtils;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCar;
import model.BlueInstructionCollection;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import view.EquipmentManager;
import view.view4app.lendcartemporary.ActivityLendCarTemporary;

public class ViewControlPanelTitle extends RelativeLayoutBase {
	private final RelativeLayout tiplayout;
	private final TextView tem_one;
	private final TextView tem_two;
	private final ImageView img_icon;
	private final ImageView img_bluetooth;
	private final ImageView img_gps;
	private final ImageView img_add;
	private final ImageView img_temporary_borrow_car;
	private boolean isNetConnect;
	private boolean isBluetoothConnect;
	private final TextView txt_tip;
	private final TextView end_time_one;
	private final TextView end_time_two;
	private  RelativeLayout tip_call_layout;
	private TextView txt_noconnect;
	private Context mContext;
	public ViewControlPanelTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		LayoutInflater.from(context).inflate(R.layout.view_control_panel_title, this, true);
		img_icon = findViewById(R.id.img_icon);
		img_bluetooth = findViewById(R.id.img_bluetooth);
		img_gps = findViewById(R.id.img_gps);
		img_add = findViewById(R.id.img_add);
		tem_one = findViewById(R.id.tem_one);
		tem_two = findViewById(R.id.tem_two);
		tiplayout = findViewById(R.id.tiplayout);
		txt_tip= findViewById(R.id.txt_tip);
		end_time_one= findViewById(R.id.end_time_one);
		end_time_two= findViewById(R.id.end_time_two);
		tip_call_layout= findViewById(R.id.tip_call_layout);
		txt_noconnect= findViewById(R.id.txt_noconnect);
		img_temporary_borrow_car= findViewById(R.id.img_temporary_borrow_car);
		NetManager.instance().init(GlobalContext.getContext());
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.SERVICE_IS_CONNECT,this);
		ODispatcher.addEventListener(OEventName.BLUETOOTH_CONNECTED_STATUS,this);
		ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE,this);
	}
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		ODispatcher.removeEventListener(OEventName.SERVICE_IS_CONNECT,this);
		ODispatcher.removeEventListener(OEventName.BLUETOOTH_CONNECTED_STATUS,this);
		ODispatcher.removeEventListener(OEventName.CAR_STATUS_SECOND_CHANGE,this);
		mContext=null;
	}
	public void initViews() {
		handleChangeData();
	}
	public void initEvents() {
		img_temporary_borrow_car.setOnClickListener(new OnClickListnerZhu() {
			@Override
			public void onClickNoFast(View v) {
				ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityLendCarTemporary.class);
			}
		});
		img_add.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
				if(  EquipmentManager.isMini()||(EquipmentManager.isMinJiaQiang()&&EquipmentManager.isMinNoMozu())||EquipmentManager.isShouweiSix()){//MINI版本
					if(BlueLinkReceiver.getIsBlueConnOK()){
						BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurrySwitch()),false);
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryStartProTect()),false);
							}
						},100);
						ClipPopCarSet.getInstance().show(img_add);
					}else{
						new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("蓝牙未连接请稍后再试").quicklyShow();
					}
				}else{
					OCtrlCar.getInstance().ccmd1203_getcarlist();//刷新车列表
					ClipPopCarSet.getInstance().show(img_add);
				}
			}
		});
		txt_noconnect.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				tip_call_layout.setVisibility(View.VISIBLE);
			}
		});
		tip_call_layout.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				tip_call_layout.setVisibility(View.GONE);
			}
		});
		img_icon.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				clickCount++;
				if(clickCount==10){
					clickCount=0;
					if(MiniDataIsShowLock.isShowLock){
						MiniDataIsShowLock.isChange=1;
						MiniDataIsShowLock.isShowLock=false;
						if (BuildConfig.DEBUG) Log.e("顯示鎖頭", "ViewControlPanelTitle"+MiniDataIsShowLock.isChange);
					}else{
						MiniDataIsShowLock.isChange=2;
						MiniDataIsShowLock.isShowLock=true;
						if (BuildConfig.DEBUG) Log.e("顯示鎖頭", "ViewControlPanelTitle"+MiniDataIsShowLock.isChange);
					}
					ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
				}
			}
		});
	}
	private int clickCount;
	public void receiveEvent(String eventName, Object paramObj) {
		if(eventName.equals(OEventName.SERVICE_IS_CONNECT)){
			setTipLayoutNet(paramObj);
		}else if(eventName.equals(OEventName.BLUETOOTH_CONNECTED_STATUS)){
			setTipLayoutBLueTooth(paramObj);
		}
	}
	private void setTipLayoutNet(Object Connect){
//		String isConnect= (String) Connect;
//		if(isConnect.equals("0")){
//			isNetConnect=false;
//		}else{
//			isNetConnect=true;
//		}
//		Message message=Message.obtain();
//		message.what=5555;
//		message.obj=isConnect;
//		handler.sendMessage(message);
	}
	private void setTipLayoutBLueTooth(Object bLueToothConnect){
		String isConnect= (String) bLueToothConnect;
		if(isConnect.equals("0")){
			isBluetoothConnect=false;
		}else{
			isBluetoothConnect=true;
		}
//		Message message=Message.obtain();
//		message.what=5556;
//		handler.sendMessage(message);
	}

	private final Handler handler=new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(@NonNull Message msg) {
			if(msg.what==5555){
				String isConnect= (String) msg.obj;
				if(isConnect.equals("0")){
					isNetConnect=false;
					tiplayout.setVisibility(View.VISIBLE);
					txt_tip.setText("网络连接不可用，请打开蓝牙并靠近车辆");
				}else if(isConnect.equals("1")){
						isNetConnect=true;
						tiplayout.setVisibility(View.GONE);
				}
			}else if(msg.what==5556){
				tiplayout.setVisibility(View.VISIBLE);
				BluetoothAdapter blueadapter=BluetoothAdapter.getDefaultAdapter();
				if(!isNetConnect){
					if(blueadapter==null){
						txt_tip.setText("您的手机没有蓝牙模块");
					}else{
						if(blueadapter.enable()){
							txt_tip.setText("请重启蓝牙开关并靠近车辆");
						}
					}
				}
			}else if(msg.what==5557){
				invalidateUI();
			}
		}
	};

	@Override
	public void callback(String key, Object value) {
		super.callback(key, value);
	}

	/**
	 * 判断Activity是否Destroy
	 * @param mActivity
	 * @return
	 */
	public static boolean isDestroy(Activity mActivity) {
		if (mActivity== null || mActivity.isFinishing() || mActivity.isDestroyed()) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressLint("CheckResult")
	@Override
	public void invalidateUI()  {
		DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
		if(car!=null&&car.logo!=null&&car.logo.length()>0){
				RequestOptions options = new RequestOptions();
				options.error(R.drawable.img_loginreg_logo).placeholder(R.drawable.img_loginreg_logo);
				if(mContext!=null){
					Glide.with(mContext)
							.load(car.logo)
							.apply(options)
							.into(img_icon);
				}
		}
		setTemAndGps(car);
		setBluetooth(car);
		setEndTime(car);
		setTempoBorrowCarUI(car);
		showTip();
	}
	private void setTempoBorrowCarUI(DataCarInfo info){
		if(info==null||info.isActive==0){
			img_temporary_borrow_car.setImageResource(R.drawable.img_tempor_borrow_car_no);
		}else{
			if(info.authorityEndTime1 > System.currentTimeMillis()) {
				img_temporary_borrow_car.setImageResource(R.drawable.img_tempor_borrow_car_yes);
			}else{
				img_temporary_borrow_car.setImageResource(R.drawable.img_tempor_borrow_car_no);
			}
		}

	}


	private void showTip(){
		if(BlueLinkReceiver.getIsBlueConnOK()){
			tiplayout.setVisibility(View.GONE);
		}else{
			if ( EquipmentManager.isMini()||(EquipmentManager.isMinJiaQiang()&&EquipmentManager.isMinNoMozu())) {
				if(EquipmentManager.isActive()){
					tiplayout.setVisibility(View.VISIBLE);
					txt_tip.setText("请打开蓝牙并靠近车辆");
				}
			}else{
				if(!NetManager.instance().isNetworkConnected()){
					DataCarInfo carInfo=ManagerCarList.getInstance().getCurrentCar();
					if(carInfo==null||carInfo.isActive==0){
						if (BuildConfig.DEBUG)Log.e("查询车辆状态", "车辆没激活不弹 " );
						tiplayout.setVisibility(View.GONE);
					}else{
						tiplayout.setVisibility(View.VISIBLE);
						txt_tip.setText("网络连接不可用，请打开蓝牙并靠近车辆");
					}
				}else{
					DataCarInfo carInfo=ManagerCarList.getInstance().getCurrentCar();
					if(carInfo==null||carInfo.isActive==0){
						if (BuildConfig.DEBUG)Log.e("查询车辆状态", "车辆没激活不弹 " );
						tiplayout.setVisibility(View.GONE);
					}else{
						DataCarStatus dataCarStatus=ManagerCarList.getInstance().getCurrentCarStatus();
						if(dataCarStatus==null||dataCarStatus.isOnline==0&&dataCarStatus.carId==carInfo.ide){
							if (BuildConfig.DEBUG)Log.e("查询车辆状态", "车辆已激活激活弹出 " );
							tiplayout.setVisibility(View.VISIBLE);
							txt_tip.setText("车辆不在线，请打开蓝牙并靠近车辆");
						}else{
							tiplayout.setVisibility(View.GONE);
						}
					}
				}
			}
		}

//		if(!isNetConnect){//没有网
//			tiplayout.setVisibility(View.VISIBLE);
//			txt_tip.setText("网络连接不可用，请打开蓝牙并靠近车辆");
//			if(!isBluetoothConnect){
////				tiplayout.setVisibility(View.VISIBLE);
////				BluetoothAdapter blueadapter=BluetoothAdapter.getDefaultAdapter();
////					if(blueadapter==null){
////						txt_tip.setText("您的手机没有蓝牙模块");
////					}else{
////						if(blueadapter.enable()){
////							txt_tip.setText("请重启蓝牙开关并靠近车辆");
////						}
////					}
//			}
//		}else{
//			DataCarStatus dataCarStatus=ManagerCarList.getInstance().getCurrentCarStatus();
//			if(dataCarStatus==null||dataCarStatus.isOnline==0){
//				DataCarInfo carInfo=ManagerCarList.getInstance().getCurrentCar();
//				if(carInfo==null||carInfo.isActive==0){
//					tiplayout.setVisibility(View.GONE);
//				}else{
//					tiplayout.setVisibility(View.VISIBLE);
//					txt_tip.setText("车辆不在线，请打开蓝牙并靠近车辆");
//				}
//			}else{
//				tiplayout.setVisibility(View.GONE);
//			}
//		}
	}
	private void setBluetooth(DataCarInfo carInfo){
		if(carInfo==null||carInfo.isActive==0){
			img_bluetooth.setImageResource(R.drawable.img_bluetooth_no_top);
		}else{
			if (BlueLinkReceiver.getIsBlueConnOK()) {
				img_bluetooth.setImageResource(R.drawable.img_bluetooth_yes_top);
			}else{
				img_bluetooth.setImageResource(R.drawable.img_bluetooth_no_top);
			}
		}
	}
	private void setEndTime(DataCarInfo car){
		if(car==null||car.isActive==0||car.authorityEndTime1==0){
			end_time_one.setVisibility(View.INVISIBLE);
			end_time_two.setVisibility(View.INVISIBLE);
		}else{
			if(car.authorityEndTime1 > System.currentTimeMillis()) {
				end_time_one.setVisibility(View.VISIBLE);
				end_time_two.setVisibility(View.VISIBLE);
				end_time_two.setText(ODateTime.time2StringWithHH(car.authorityEndTime1));
			}else{
				end_time_one.setVisibility(View.INVISIBLE);
				end_time_two.setVisibility(View.INVISIBLE);
			}
		}
	}
	private void setTemAndGps(DataCarInfo car){
		DataCarStatus currentCarStatus=ManagerCarList.getInstance().getCurrentCarStatus();
		if(car==null||car.isActive==0||currentCarStatus==null){
			tem_one.setVisibility(View.INVISIBLE);
			tem_two.setVisibility(View.INVISIBLE);
			img_gps.setImageResource(R.drawable.img_gps_off_top);
		}else{
			if(car.ide==currentCarStatus.carId){
				if(currentCarStatus.gpsOpen==0){
					img_gps.setImageResource(R.drawable.img_gps_off_top);
				}else{
					img_gps.setImageResource(R.drawable.img_gps_on_top);
				}
			}
//			if(currentCarStatus.temp< -500||currentCarStatus.temp==0){
//				tem_one.setVisibility(View.INVISIBLE);
//				tem_two.setVisibility(View.INVISIBLE);
//			}else{
//				tem_one.setVisibility(View.VISIBLE);
//				tem_two.setVisibility(View.VISIBLE);
//				tem_one.setText(currentCarStatus.temp + "℃");
//			}
		}
	}
}
