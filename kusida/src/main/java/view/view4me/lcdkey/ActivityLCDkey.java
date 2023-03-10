package view.view4me.lcdkey;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.blue.BluePermission;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.time.TimeDelayTask;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.OToastInput;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.toast.ToastTxt;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCar;
import ctrl.OCtrlRegLogin;
import model.ManagerCarList;
import model.ManagerLoginReg;
import model.carlist.DataCarInfo;
import view.ActivityKulalaMain;
import view.find.BasicParamCheckPassWord;
import view.view4me.myblue.DataReceive;
import view.view4me.myblue.MyLcdBlueAdapter;
import view.view4me.set.ClipSetItem;
import view.view4me.set.ClipTitleMeSet;

import static com.kulala.linkscarpods.blue.ConvertHexByte.bytesToHexString;
import static common.GlobalContext.getCurrentActivity;

//import com.kulala.staticsview.ActivityBase;

public class ActivityLCDkey extends ActivityBase {
	private static String TAG=ActivityLCDkey.class.getSimpleName();
	private ClipTitleMeSet title_head;
	private ClipSetItem txt_isBind;
	private ClipSetItem txt_Lcd_Tips;
	private ImageView imageisBind;
	private long sendBindDataTime;
	private long connectFaildTime;
	private 	String isBindSucsess;
	public static ActivityLCDkey ActivityLCDkeyThis;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 if (BuildConfig.DEBUG) Log.e(TAG, "ActivityLCDkey: onCreate");
		setContentView(R.layout.activity_lcdkey);
		GlobalContext.setIsInLcdKeyPage(true);
		Intent intent=getIntent();
		if(intent!=null){
			 isBindSucsess=intent.getStringExtra("?????????");
//			if(!TextUtils.isEmpty(isBindSucsess)&&isBindSucsess.equals("????????????")){
////				 if (BuildConfig.DEBUG) Log.e("????????????", "invalidateUI: "+"????????????");
////				new ToastTxt(ActivityLCDkey.this,null).withInfo("????????????").show();
//			}
		}
		title_head =  findViewById(R.id.title_head);
		txt_isBind = findViewById(R.id.txt_isBind);
		txt_Lcd_Tips = findViewById(R.id.txt_lcd_tips);
		imageisBind = (ImageView) findViewById(R.id.image_isbind);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.LCDKEY_AUTO_OPEN,this);
		ODispatcher.addEventListener(OEventName.CHECK_PASSWORD_RESULTBACK, this);
//		autoLinkLCDKeyBlue(ActivityLCDkey.this);
		if(!TextUtils.isEmpty(isBindSucsess)&&isBindSucsess.equals("????????????")){
			new ToastTxt(ActivityLCDkey.this,null).withInfo("????????????").show();
			isBindSucsess="";
		}
	}
	protected void initViews() {
		DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
		if(currentCar==null) return;
		if(currentCar.isKeyBind==0){
			txt_isBind.txt_left.setText("??????");
		}else{
			txt_isBind.txt_left.setText("??????");
		}
		if(currentCar.isKeyOpen==0){
			imageisBind.setImageResource(R.drawable.switch_off);
		}else{
			imageisBind.setImageResource(R.drawable.switch_on);
		}
	}

	@Override
	protected void onDestroy() {
		GlobalContext.setIsInLcdKeyPage(false);
		ActivityLCDkeyThis=null;
//		if(MyLcdBlueAdapter.getInstance().getIsConnectted()){
//			MyLcdBlueAdapter.getInstance().closeBlueReal();
//		}
		ODispatcher.removeEventListener(OEventName.LCDKEY_BIND_OPRATION,this);
		ODispatcher.removeEventListener(OEventName.LCDKEY_AUTO_OPEN,this);
		ODispatcher.removeEventListener(OEventName.CHECK_PASSWORD_RESULTBACK, this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		ActivityLCDkeyThis=this;
		ODispatcher.addEventListener(OEventName.LCDKEY_BIND_OPRATION,this);
		super.onResume();
		 if (BuildConfig.DEBUG) Log.e(TAG, "ActivityLCDkey: onResume");
	}
	@Override
	public void onBackPressed() {
		title_head.img_left.callOnClick();
	}

	public void initEvents() {
		//back
		txt_Lcd_Tips.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityLCDkey.this, ActivityLcdKeyTips.class);
				startActivity(intent);
			}
		});
		title_head.img_left.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityLCDkey.this, ActivityKulalaMain.class);
				startActivity(intent);
				finish();
			}
		});
		//btn_confirm
		imageisBind.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				//??????????????????????????????????????????????????????
				DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
				if(currentCar==null)return;
				if(currentCar.isKeyOpen==0){
					OCtrlCar.getInstance().ccmd1250_LcdKey_autoOpen(currentCar.ide,1);
				}else{
					OCtrlCar.getInstance().ccmd1250_LcdKey_autoOpen(currentCar.ide,0);
				}

			}
		});
		txt_isBind.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
			final	DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
				if(currentCar==null)return;
				if(currentCar.isKeyBind==0){
					if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
						BluePermission.openBlueTooth(ActivityLCDkey.this);
						return;
					}
					//????????????????????????????????????
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						if (BluePermission.checkPermission(getCurrentActivity()) != 1) return;
						int permissionCamera = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.CAMERA);
						//????????????
						if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
							GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
						} else {
							Intent intent = new Intent();
							intent.setClass(GlobalContext.getContext(), BindLcdKeyActivity.class);
							startActivity(intent);
							finish();
						}
					} else {
						Intent intent = new Intent();
						intent.setClass(GlobalContext.getContext(), BindLcdKeyActivity.class);
						startActivity(intent);
						finish();
						 if (BuildConfig.DEBUG) Log.e(TAG, "activitylcd: finish ");
					}
				}else{
					OToastInput.getInstance().showInput(title_head, "??????????????????", "?????????????????????:", new String[]{OToastInput.PASS}, "unActiveLcd", ActivityLCDkey.this);
//					new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
//							.withInfo("?????????????????????????????????")
//							.withClick(new ToastConfirmNormal.OnButtonClickListener() {
//								@Override
//								public void onClickConfirm(boolean isClickConfirm) {
//
//									if (isClickConfirm){
//										if(currentCar!=null&&!TextUtils.isEmpty(currentCar.keyBlueName)){
//											OCtrlCar.getInstance().ccmd1249_isBindLcdKey(currentCar.ide,2,currentCar.keyBlueName);
//										}
//									}
//								}
//							})
//							.show();
				}
			}
		});
	}



	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		super.receiveEvent(eventName, paramObj);
		if(eventName.equals(OEventName.LCDKEY_AUTO_OPEN)){
			DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
			long userId= ManagerLoginReg.getInstance().getCurrentUser().userId;
			BlueLinkReceiver.needChangeLcdKey(currentCar.ide,currentCar.keyBlueName,currentCar.keySig,currentCar.isKeyBind,currentCar.isKeyOpen,userId);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
					if(currentCar.isKeyOpen==0){
						imageisBind.setImageResource(R.drawable.switch_off);
					}else{
						imageisBind.setImageResource(R.drawable.switch_on);
					}
				}
			});
		}else if(eventName.equals(OEventName.LCDKEY_BIND_OPRATION)){
			 if (BuildConfig.DEBUG) Log.e(TAG, "????????????  ????????????");
			handleChangeData();
//			unBindSendBlueTooth();
		}else if (eventName.equals(OEventName.CHECK_PASSWORD_RESULTBACK)) {
			boolean check = (Boolean) paramObj;
			if (check) {
				if(BasicParamCheckPassWord.isFindMain==6){
					new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
							.withTitle("??????:")
							.withInfo("????????????????????????????????????????????????????????????????")
							.withClick(new ToastConfirmNormal.OnButtonClickListener() {
								@Override
								public void onClickConfirm(boolean isClickConfirm) {
									if (isClickConfirm ) {
										unBindSendBlueTooth();
										DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
										if(currentCar!=null&&!TextUtils.isEmpty(currentCar.keyBlueName)){
											OCtrlCar.getInstance().ccmd1249_isBindLcdKey(currentCar.ide,2,currentCar.keyBlueName);
										}
									}
								}
							})
							.show();
				}
			}
	}
}

	//	public void receiveEvent(String key, Object paramObj) {
//		super.receiveEvent(key,paramObj);
//		if(key.equals(OEventName.LCDKEY_AUTO_OPEN)){
//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
//					if(currentCar.isKeyOpen==0){
//						imageisBind.setImageResource(R.drawable.switch_off);
//					}else{
//						imageisBind.setImageResource(R.drawable.switch_on);
//					}
//				}
//			});
//		}else if(key.equals(OEventName.LCDKEY_BIND_OPRATION)){
//			 if (BuildConfig.DEBUG) Log.e("activitylcd", "????????????  ????????????");
//			handleChangeData();
//		BlueLinkReceiver.getInstance().sendMessagelcd(bytesToHexString(unBind),true);	unBindSendBlueTooth();
//		}
//	}
	private void unBindSendBlueTooth(){
		DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
		if(currentCar.isKeyBind==0){
			byte[] unBind=new byte[4];
			unBind[0]=0x01;
			unBind[1]=1;
			unBind[2]=0x02;
			unBind[3] = 0;
			for (int i = 0; i < 3; i++) {
				unBind[3] += unBind[i];
			}
			unBind[3] ^= 0xff;
			if(BlueLinkReceiver.getIsBlueConnOK()){
				BlueLinkReceiver.getInstance().sendMessagelcd(bytesToHexString(unBind),true);
			}
//			if(MyLcdBlueAdapter.current_blue_state>= OnBlueStateListenerRoll.STATE_CONNECT_OK){
//				MyLcdBlueAdapter.getInstance().sendMessage(unBind);//????????????????????????????????????
//
//			}
		}
	}
	@Override
	public void callback(String key, Object value) {
		if(key.equals("unActiveLcd")){
			JsonObject obj  = (JsonObject) value;
			String     pass = OJsonGet.getString(obj, OToastInput.PASS);
			BasicParamCheckPassWord.isFindMain=6;
			OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass);
		}
	}
	@Override
	public void invalidateUI()  {
		DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
		if(currentCar.isKeyBind==0){
			txt_isBind.txt_left.setText("??????");
			new ToastTxt(ActivityLCDkey.this,null).withInfo("????????????").show();
		}else{
			txt_isBind.txt_left.setText("??????");
		}
	}

    /**
     * ???????????????????????????????????????????????????
     */

    private void autoLinkLCDKeyBlue( Activity activity) {
        DataCarInfo carInfo= ManagerCarList.getInstance().getCurrentCar();
        if(carInfo==null)return;
        int isBind=carInfo.isKeyBind;
        String blueAderess=carInfo.keyBlueName;
        String keySig=carInfo.keySig;
         if (BuildConfig.DEBUG) Log.e(TAG, "????????????"+blueAderess+"  isBind"+isBind+"  ?????????keySig"+keySig);
        if (!TextUtils.isEmpty(blueAderess)&&isBind==1&&!TextUtils.isEmpty(keySig)) {
            int isPermision = BluePermission.checkPermission(activity);
			if (isPermision != 1) {
				return;
			}
            MyLcdBlueAdapter.getInstance().initializeOK(GlobalContext.getContext());
            MyLcdBlueAdapter.getInstance().setOnBlueStateListener(new MyOnBlueStateListenerRoll(new MyOnBlueStateListenerRoll.OnonDescriptorWriteLister() {
                @Override
                public void onDescriptorWrite() {
//                    DataCarInfo currentCar= ManagerCarList.getInstance().getCurrentCar();
                    //???????????????
                    DataCarInfo carInfo= ManagerCarList.getInstance().getCurrentCar();
                    String keySig=carInfo.keySig;
					 if (BuildConfig.DEBUG) Log.e(TAG, "??????"+keySig);
                    if(!TextUtils.isEmpty(keySig)){
                        byte[] bytesig = keySig.getBytes();
                        byte[] mess = DataReceive.newBlueMessage((byte) 1, (byte) 1, bytesig);
                        String datasend=bytesToHexString(mess);
                        MyLcdBlueAdapter.getInstance().sendMessage(bytesToHexString(mess));
						sendBindDataTime=System.currentTimeMillis();
                         if (BuildConfig.DEBUG) Log.e(TAG, "???????????????"+datasend);
                        new TimeDelayTask().runTask(1000L, new TimeDelayTask.OnTimeEndListener() {
							@Override
							public void onTimeEnd() {
								if(MyLcdBlueAdapter.getInstance().getIsConnectted()){
									if(!TextUtils.isEmpty(isBindSucsess)&&isBindSucsess.equals("????????????")){
										new ToastTxt(ActivityLCDkey.this,null).withInfo("????????????").show();
									}
								}
							}
						});
                    }
//                        MyLcdBlueAdapter.getInstance().sendMessage("01 02 01 03 F8");
                }

                @Override
                public void onConnectedFailed() {
					connectFaildTime=System.currentTimeMillis();
					if(!TextUtils.isEmpty(isBindSucsess)&&isBindSucsess.equals("????????????")){
						if((connectFaildTime-sendBindDataTime)<1000L){
							new ToastTxt(ActivityLCDkey.this,null).withInfo("????????????").show();
						}
					}
                }
            }));
            String reealMacAderess= StringToMacUtil.collapseString(blueAderess, 2, ":");
            if(BluetoothAdapter.checkBluetoothAddress(reealMacAderess)){
				MyLcdBlueAdapter.getInstance().gotoConnDeviceAddress(reealMacAderess);
//				if(MyLcdBlueAdapter.current_blue_state>= OnBlueStateListenerRoll.STATE_CONNECT_OK){
//
//					MyLcdBlueAdapter.getInstance().gotoConnDeviceAddress(reealMacAderess);
//				}else{
//					MyLcdBlueAdapter.getInstance().gotoConnDeviceAddress(reealMacAderess);
//				}
            }
        }
//        else{
//            //   //????????????  ??????????????????????????????
//            if (MyLcdBlueAdapter.getInstance().getIsConnectted()) {
//                MyLcdBlueAdapter.getInstance().closeBlueReal();
//            }
//        }
    }

	@Override
	protected void popView(int resId) {

	}

}