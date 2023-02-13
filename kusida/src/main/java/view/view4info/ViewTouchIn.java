package view.view4info;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.blue.BlueAdapter;
import com.kulala.linkscarpods.blue.MyBluetoothDevice;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.kulala.staticsfunc.time.CountDownTimerMy;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.toast.ToastTxt;

import java.util.Set;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import common.global.TextViewEC;
import model.BlueInstructionCollection;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.EquipmentManager;
import view.view4me.set.ClipTitleMeSet;
import view.view4me.shake.BlueScannerAllways;
import view.view4me.shake.ClipLeftTxtRightCheckBox;
import view.view4me.shake.ClsUtils;
import view.view4me.shake.OnClickListenerMy3000;
import view.view4me.shake.ProItemLinshi;
import view.view4me.shake.ViewNokeyPopWindow;

public class ViewTouchIn extends RelativeLayoutBase {

    private ClipTitleMeSet title_head;
    private RelativeLayout lin_touch_in;
    private final ClipLeftTxtRightCheckBox txt_touch_in;
    private TextViewEC txt_elc_open;
    private MyHandler handler = new MyHandler();
    private Button btn_set_kaojin_open;
    private ViewNokeyPopWindow nokey_window;
    private RelativeLayout touch_in_pop_layout;
    private RelativeLayout loading_view_layout;
    private TextView loading_view_textview;
    private TextView peidui;
    private ProItemLinshi lock_pamduan_count;
    private RelativeLayout lin_r;
    private TextView tv1;
    private TextView tv;
    private Button btn_know;
    private RelativeLayout re_windows,re2_a;
    private SpannableString msp = null;
    private TextView tv_content;

    public ViewTouchIn(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_me_touch_in, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_elc_open = (TextViewEC) findViewById(R.id.txt_elc_open);
        lin_touch_in = (RelativeLayout) findViewById(R.id.lin_touch_in);
        txt_touch_in = findViewById(R.id.txt_touch_in);
        btn_set_kaojin_open = (Button) findViewById(R.id.btn_set_kaojin_open);
        nokey_window = findViewById(R.id.nokey_window);
        touch_in_pop_layout = findViewById(R.id.touch_in_pop_layout);
        loading_view_layout = findViewById(R.id.loading_view_layout);
        loading_view_textview = findViewById(R.id.loading_view_textview);
        peidui = findViewById(R.id.peidui);
        lock_pamduan_count = findViewById(R.id.lock_pamduan_count);
        lin_r= findViewById(R.id.lin_r);
        tv1= findViewById(R.id.tv1);
        tv= findViewById(R.id.tv);
        btn_know= findViewById(R.id.btn_know);
        re_windows= findViewById(R.id.re_windows);
        re2_a= findViewById(R.id.re2_a);
        tv_content= findViewById(R.id.tv_content);
        ODispatcher.addEventListener(OEventName.TOUCH_IN_SWITCH_RESULT, this);
        ODispatcher.addEventListener(OEventName.TOUCH_IN_RSSI_RESULT, this);
        ODispatcher.addEventListener(OEventName.TOUCH_IN_SET_INFO, this);

        initViews();
        initEvents();

    }

        @Override
    protected void initViews() {
            msp=new SpannableString("1.\t手机蓝牙保持常开;\n\n2.\t发起配对：以便灭屏时自动连蓝牙(如果打开摇一摇时\n\n\t\t已经设置保活则无需再发起配对)");
            //设置字体前景色
            msp.setSpan(new ForegroundColorSpan(Color.parseColor("#e93232")), 0, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
            tv_content.setText(msp);
            tv_content.setMovementMethod(LinkMovementMethod.getInstance());
            if (EquipmentManager.isMini()||EquipmentManager.isStartWithAkl()||EquipmentManager.isMinJiaQiang()||EquipmentManager.isShouweiSix()) {
                peidui.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                tv1.setVisibility(View.VISIBLE);
            }else{
                peidui.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);
                tv1.setVisibility(View.INVISIBLE);
            }
    }

    int messageNum = 0;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        sendQueryState();
    }

    private void sendQueryState() {
        //取开关状态,蓝牙
        new CountDownTimerMy(2000L, 800L) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("钥匙打野", "messageNum" + messageNum);
                if (messageNum == 0) {
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryTouchIn()), false);
                } else if (messageNum == 1) {
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.qurryTouchInRssi()), false);
                }
                messageNum++;
            }

            @Override
            public void onFinish() {
                messageNum = 0;
            }
        }.start();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.TOUCH_IN_SWITCH_RESULT)) {
          handleChangeData();
        }else if (eventName.equals(OEventName.TOUCH_IN_RSSI_RESULT)) {
            handleChangeData();
        }else if (eventName.equals(OEventName.TOUCH_IN_SET_INFO)) {
            int touchInRssi=(Integer)paramObj;
            setTouchInRssi(touchInRssi);
            handler.obtainMessage(NOKEY_SET_WINDOW_SHOW).sendToTarget();
        }
        super.receiveEvent(eventName, paramObj);
    }
    private void setTouchInRssi(int rssi){
        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setTouchInRssi(rssi)), false);
    }

    private byte[] queryLevelLockCount(int type){
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        if(type==1){
            instructions[3] = (byte) 0x0A;//1上锁
        }else{
            instructions[3] = (byte) 0x0B;
        }
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] ) ^ 0xff);
        return instructions;
    }
    private byte[] getLevelLockCount(String s,int type){
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        if(type==1){
            instructions[3] = (byte) 0x0A;//1上锁
        }else{
            instructions[3] = (byte) 0x0B;
        }
        switch (s) {
            case "1":
                instructions[4] = (byte) 0x01;
                break;
            case "2":
                instructions[4] = (byte) 0x02;
                break;
            case "3":
                instructions[4] = (byte) 0x03;
                break;
            case "4":
                instructions[4] = (byte) 0x04;
                break;
            case "5":
                instructions[4] = (byte) 0x05;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    @Override
    public void callback(String key, Object value) {
 if (key.equals("un_lock_panduan")) {
            String oo = (String) value;
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(getLevelLockCount(oo,2)), false);
        } else if (key.equals("lock_panduan")) {
            String oo = (String) value;
     BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(getLevelLockCount(oo,1)), false);
        }
        super.callback(key, value);
    }
    private int clickLockCount;
    @Override
    protected void initEvents() {
        re2_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        re_windows.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                re_windows.setVisibility(View.INVISIBLE);
            }
        });
        peidui.setOnClickListener(new OnClickListenerMy3000() {
            @Override
            public void onClickNoFast(View view) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                Set<BluetoothDevice> deviceList1 = BlueAdapter.getInstance().getBondedDevice(GlobalContext.getCurrentActivity());
                boolean isHaveBond = false;
                if(deviceList1!=null&&deviceList1.size()>0){
                    for (BluetoothDevice devices : deviceList1) {
                        if (BuildConfig.DEBUG)
                            Log.e("主机蓝牙", "已配对的蓝牙名字" + devices.getName() + "已配对的蓝牙地址" + devices.getAddress());
                        if (!TextUtils.isEmpty(car.bluetoothName) && !TextUtils.isEmpty(devices.getName()) && car.bluetoothName.equals(devices.getName())) {
                            isHaveBond = true;
                            new ToastTxt(GlobalContext.getCurrentActivity(), null, false).withInfo("当前设备已配对过").quicklyShow();
                           return;
                        }
                    }
                }

                if (loading_view_layout.getVisibility() == View.INVISIBLE) {
                    loading_view_layout.setVisibility(View.VISIBLE);
                    loading_view_textview.setText("发起配对中请稍后");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loading_view_layout.setVisibility(View.INVISIBLE);
                        }
                    }, 3000L);
                }
                final DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
                byte[] dataClose = new byte[4];
                dataClose[0] = (byte) 0xB2;
                dataClose[1] = (byte) 0x01;
                dataClose[2] = (byte) 0x01;
                dataClose[3] = (byte) ((dataClose[0] + dataClose[1] + dataClose[2]) ^ 0xff);
                BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataClose), false);
                try {
                    Thread.sleep(100L);
                    BlueLinkReceiver.getInstance().needChangeCar(carInfo.ide, "", "", 0, "", 0);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BlueScannerAllways.getInstance().scanLeDevice(GlobalContext.getCurrentActivity(), new BlueScannerAllways.OnScanBlueListener() {
//                                @Override
//                                public void onScanedDevice(BluetoothDevice device) {
//                                    if (!TextUtils.isEmpty(device.getName())) {
//                                        Log.e("pppaskdakoda", "[" + device.getName() + "]" + ":" + device.getAddress());
//                                    }
////			if(btDevice.getName().contains("NFC"))//NFC�豸����ж������һ���ѵ����Ǹ��ᱻ���ԡ�
//                                    DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
//                                    if (currentCar != null && !TextUtils.isEmpty(currentCar.bluetoothName) && !TextUtils.isEmpty(device.getName())) {
//                                        if (device.getName().equals(currentCar.bluetoothName)) {
//                                            Log.e("pppaskdakoda", "attemp to bond:" + "[" + device.getName() + "]");
//                                            try {
//                                                //ͨ��������ClsUtils,����createBond����
//                                                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
////                                                Set<BluetoothDevice> deviceList1 = BlueAdapter.getInstance().getBondedDevice(GlobalContext.getCurrentActivity());
////                                                boolean isHaveBond = false;
////                                                if(deviceList1!=null&&deviceList1.size()>0){
////                                                    for (BluetoothDevice devices : deviceList1) {
////                                                        if (com.kulala.linkscarpods.BuildConfig.DEBUG)
////                                                            Log.e("主机蓝牙", "已配对的蓝牙名字" + devices.getName() + "已配对的蓝牙地址" + devices.getAddress());
////                                                        if (!TextUtils.isEmpty(car.bluetoothName) && !TextUtils.isEmpty(devices.getName()) && car.bluetoothName.equals(devices.getName())) {
////                                                            isHaveBond = true;
////                                                            new ToastTxt(GlobalContext.getCurrentActivity(), null, false).withInfo("当前设备已配对过").quicklyShow();
////                                                            break;
////                                                        }
////                                                    }
////                                                }
////                                                if (!isHaveBond) {
//                                                    Log.e("pppaskdakoda", "绑定");
//                                                    ClsUtils.createBond(device.getClass(), device);
////                                                }
//                                                handler.postDelayed(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        BlueLinkReceiver.needChangeCar(car.ide, car.bluetoothName, car.blueCarsig, car.isBindBluetooth, car.carsig, car.isMyCar);
//                                                    }
//                                                }, 3000L);
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }
//                                }

                                @Override
                                public void onScanedDevice(MyBluetoothDevice device, BluetoothDevice device1) {
                                    if (!TextUtils.isEmpty(device.getName())) {
                                        Log.e("pppaskdakoda", "[" + device.getName() + "]" + ":" + device.getAddress());
                                    }
//			if(btDevice.getName().contains("NFC"))//NFC�豸����ж������һ���ѵ����Ǹ��ᱻ���ԡ�
                                    DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                                    if (currentCar != null && !TextUtils.isEmpty(currentCar.bluetoothName) && !TextUtils.isEmpty(device.getName())) {
                                        if (device.getName().contains(currentCar.bluetoothName)) {
                                            Log.e("pppaskdakoda", "attemp to bond:" + "[" + device.getName() + "]");
                                            try {
                                                //ͨ��������ClsUtils,����createBond����
                                                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
//                                                Set<BluetoothDevice> deviceList1 = BlueAdapter.getInstance().getBondedDevice(GlobalContext.getCurrentActivity());
//                                                boolean isHaveBond = false;
//                                                if(deviceList1!=null&&deviceList1.size()>0){
//                                                    for (BluetoothDevice devices : deviceList1) {
//                                                        if (com.kulala.linkscarpods.BuildConfig.DEBUG)
//                                                            Log.e("主机蓝牙", "已配对的蓝牙名字" + devices.getName() + "已配对的蓝牙地址" + devices.getAddress());
//                                                        if (!TextUtils.isEmpty(car.bluetoothName) && !TextUtils.isEmpty(devices.getName()) && car.bluetoothName.equals(devices.getName())) {
//                                                            isHaveBond = true;
//                                                            new ToastTxt(GlobalContext.getCurrentActivity(), null, false).withInfo("当前设备已配对过").quicklyShow();
//                                                            break;
//                                                        }
//                                                    }
//                                                }
//                                                if (!isHaveBond) {
                                                Log.e("pppaskdakoda", "绑定");
                                                ClsUtils.createBond(device1.getClass(), device1);
//                                                }
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        BlueLinkReceiver.needChangeCar(car.ide, car.bluetoothName, car.blueCarsig, car.isBindBluetooth, car.carsig, car.isMyCar);
                                                    }
                                                }, 3000L);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onScanStop() {

                                }
                            });

                        }
                    }, 1000L);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        loading_view_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        touch_in_pop_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                touch_in_pop_layout.setVisibility(View.INVISIBLE);
            }
        });
        nokey_window.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_set_kaojin_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int touchInRssi=BlueInstructionCollection.getInstance().getTouchInRssi();
                if(touchInRssi!=65&&touchInRssi!=68&&touchInRssi!=71&&touchInRssi!=74&&touchInRssi!=77&&touchInRssi!=80&&touchInRssi!=83){
                    touchInRssi=74;
                }
                nokey_window.setType(3, touchInRssi);
                touch_in_pop_layout.setVisibility(View.VISIBLE);
            }
        });

        //open switch
        txt_touch_in.img_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BlueLinkReceiver.getIsBlueConnOK()) {
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
                    return;
                }
                int isTouchIn=BlueInstructionCollection.getInstance().getIsTouchIn();
                if (isTouchIn==1) {//之前是打开的,发消息去关
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setTouchInSwitch(0)), false);
                } else {
                    re_windows.setVisibility(View.VISIBLE);
                }
            }
        });
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        btn_know.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                re_windows.setVisibility(View.INVISIBLE);
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setTouchInSwitch(1)), false);
            }
        });

    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    @Override
    protected void invalidateUI() {
        setUI();
    }
    private static final int HANDLER_JUMP_OPEN = 10013;
    private static final int HANDLER_JUMP_CLOSE = 10014;
    private static final int NOKEY_SET_WINDOW_SHOW = 10016;


    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOKEY_SET_WINDOW_SHOW:
                    touch_in_pop_layout.setVisibility(View.INVISIBLE);
                    break;
                case 188:
                 lock_pamduan_count.txt_bottom.setText(BlueInstructionCollection.getInstance().getLockCount()+"");
                    break;
                case 189:
                    break;

            }
        }
    }
    private void setUI() {
        loading_view_layout.setVisibility(View.INVISIBLE);
      int isTouchIn=BlueInstructionCollection.getInstance().getIsTouchIn();
      if(isTouchIn==1){
          txt_touch_in.setRightImg(R.drawable.car_set_on);
          lin_touch_in.setVisibility(VISIBLE);
      }else{
          txt_touch_in.setRightImg(R.drawable.car_set_off);
          lin_touch_in.setVisibility(GONE);
      }
      int touchInRssi=BlueInstructionCollection.getInstance().getTouchInRssi();
      Log.e("touchinrssi","值为"+touchInRssi);
        if(touchInRssi!=65&&touchInRssi!=68&&touchInRssi!=71&&touchInRssi!=74&&touchInRssi!=77&&touchInRssi!=80&&touchInRssi!=83&&touchInRssi!=-100){
            touchInRssi=74;
            Log.e("touchinrssi","发送默认"+touchInRssi);
            BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.setTouchInRssi(74)), false);
        }
      setTextValue(touchInRssi);
    }
    private void setTextValue(int value) {
        String openStr = "";
        switch (value) {
            case 65:
                openStr = "最近";
                break;
            case 68:
                openStr = "近";
                break;
            case 71:
                openStr = "稍近";
                break;
            case 74:
                openStr = "适中";
                break;
            case 77:
                openStr = "稍远";
                break;
            case 80:
                openStr = "远";
                break;
            case 83:
                openStr = "最远";
                break;
            case 0:
                openStr = "--";
                break;
        }
        txt_elc_open.setText(openStr);
    }
}
