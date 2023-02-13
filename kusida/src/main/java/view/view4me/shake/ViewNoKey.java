package view.view4me.shake;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.blue.BlueAdapter;
import com.kulala.linkscarpods.blue.MyBluetoothDevice;
import com.kulala.staticsfunc.BuildConfig;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.kulala.staticsfunc.static_system.OConver;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.time.CountDownTimerMy;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.toast.ToastTxt;

import java.util.Random;
import java.util.Set;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import common.global.TextViewEC;
import ctrl.OCtrlCar;
import model.BlueInstructionCollection;
import model.GetCarInfoUtils;
import model.ManagerCarList;
import model.ManagerNokey;
import model.carlist.DataCarInfo;
import view.EquipmentManager;
import view.find.OToastButtonBlackNotouchOutDismissStyle;
import view.view4me.set.ClipTitleMeSet;

public class ViewNoKey extends RelativeLayoutBase {

    private ClipTitleMeSet title_head;
    private RelativeLayout lin_nokey_open, lin_nokey_close,re2_a;
    private final ClipLeftTxtRightCheckBox txt_nokey_open;
    private ClipLeftTxtRightCheckBox txt_nokey_close;
    private TextViewEC txt_elc_open, txt_elc_close;
    private Button btn_set_open, btn_set_close, btn_set_close_2;
    private MyHandler handler = new MyHandler();
    public static ViewNoKey viewNoKeyThis;
    private boolean openIsDeaufault = false;
    private boolean closeIsDefault = false;
    private int openDeaufaltValue = 0;
    private int closeDefaultValue = 0;
    private Button btn_set_kaojin_open;
    private Button btn_set_likai_lock;
    private ViewNokeyPopWindow nokey_window;
    private ImageView iv_open_add, iv_open_delete, iv_close_add, iv_close_delete;
    private RelativeLayout no_key_pop_layout;
    private RelativeLayout loading_view_layout;
    private TextView loading_view_textview;
    private TextView peidui;
    private int nummmm;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private boolean isOpenBack;
    private boolean isCloseBack;
    private boolean isOpenDataBack;
    private boolean isCloseDataBack;
    private boolean isSetKaoJInKai;
    private boolean isSetLikaisuo;
    private boolean isSetDisTance;
    private ProItemLinshi lock_pamduan_count;
    private ProItemLinshi unlock_pamduan_count;
    private RelativeLayout lin_r;
    private RelativeLayout lin_r_one;
    private TextView tv1;
    private TextView tv;
    private BlueNotouchInData blueNotouchInData=new BlueNotouchInData();
    private int isFirstIn;
    private BlueNotouchInData firstNotouchIndata;
    private boolean isRecivePush;
    private Button btn_know;
    private RelativeLayout re_windows;
    private int type;//1靠近开2离开锁
    private SpannableString msp = null;
    private TextView tv_content;


    public ViewNoKey(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_nokey_new, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_elc_open = (TextViewEC) findViewById(R.id.txt_elc_open);
        txt_elc_close = (TextViewEC) findViewById(R.id.txt_elc_close);
        lin_nokey_open = (RelativeLayout) findViewById(R.id.lin_nokey_open);
        lin_nokey_close = (RelativeLayout) findViewById(R.id.lin_nokey_close);
        btn_set_open = (Button) findViewById(R.id.btn_set_open);
        btn_set_close = (Button) findViewById(R.id.btn_set_close);
        btn_set_close_2 = (Button) findViewById(R.id.btn_set_close_2);
        iv_open_add = (ImageView) findViewById(R.id.iv_open_add);
        iv_open_delete = (ImageView) findViewById(R.id.iv_open_delete);
        iv_close_add = (ImageView) findViewById(R.id.iv_close_add);
        iv_close_delete = (ImageView) findViewById(R.id.iv_close_delete);
        txt_nokey_open = findViewById(R.id.txt_nokey_open);
        txt_nokey_close = findViewById(R.id.txt_nokey_close);
        btn_set_likai_lock = (Button) findViewById(R.id.btn_set_likai_lock);
        btn_set_kaojin_open = (Button) findViewById(R.id.btn_set_kaojin_open);
        nokey_window = findViewById(R.id.nokey_window);
        no_key_pop_layout = findViewById(R.id.no_key_pop_layout);
        loading_view_layout = findViewById(R.id.loading_view_layout);
        loading_view_textview = findViewById(R.id.loading_view_textview);
        peidui = findViewById(R.id.peidui);
        lock_pamduan_count = findViewById(R.id.lock_pamduan_count);
        unlock_pamduan_count = findViewById(R.id.unlock_pamduan_count);
        lin_r= findViewById(R.id.lin_r);
        lin_r_one= findViewById(R.id.lin_r_one);
        tv1= findViewById(R.id.tv1);
        tv= findViewById(R.id.tv);
        btn_know= findViewById(R.id.btn_know);
        re_windows= findViewById(R.id.re_windows);
        re2_a= findViewById(R.id.re2_a);
        tv_content= findViewById(R.id.tv_content);
        ODispatcher.addEventListener(OEventName.LOCK_COUNT_RESULT, this);
        ODispatcher.addEventListener(OEventName.SOCKET_NOKEY_SET, this);
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
        ODispatcher.addEventListener(OEventName.NOKEY_SET_INFO, this);
        /**进入前先打开蓝牙开关*/
        //1.默认是不显示设置
        handler.obtainMessage(HANDLER_CHANGE_OPEN_STATUS).sendToTarget();//设置值
        handler.obtainMessage(HANDLER_CHANGE_CLOSE_STATUS).sendToTarget();//设置值
    }

    int messageNum = 0;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewNoKeyThis = this;
        sendQueryState();
    }

    public void onBlueConnOK() {
        sendQueryState();
    }

    private void sendQueryState() {
        //取开关状态,蓝牙
        new CountDownTimerMy(5600L, 800L) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("钥匙打野", "messageNum" + messageNum);
                if (messageNum == 0) {
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_ASK_SWITCH_OPEN, false);
                } else if (messageNum == 1) {
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_ASK_SWITCH_CLOSE, false);
                } else if (messageNum == 2) {
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_ASK_DISTANCE_OPEN, false);
                } else if (messageNum == 3) {
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_ASK_DISTANCE_CLOSE, false);
                }else if (messageNum == 4) {
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(queryLevelLockCount(1)), false);
                }else if (messageNum == 5) {
                    BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(queryLevelLockCount(2)), false);
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
        viewNoKeyThis = null;
        ODispatcher.removeEventListener(OEventName.LOCK_COUNT_RESULT,this);
        ODispatcher.removeEventListener(OEventName.SOCKET_NOKEY_SET,this);
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.NOKEY_SET_INFO)) {
            CacheNokeyInfo cacheNokeyInfo = (CacheNokeyInfo) paramObj;
            isSetDisTance = true;
            sendDataMy(cacheNokeyInfo);
            openDeaufaltValue = cacheNokeyInfo.openData;
            closeDefaultValue = cacheNokeyInfo.closeData;
            handler.obtainMessage(NOKEY_SET_WINDOW_SHOW).sendToTarget();
        }else if (eventName.equals(OEventName.LOCK_COUNT_RESULT)) {
            int aa=(Integer)paramObj;
            if(aa==1){
                //lockUI
               handler.sendEmptyMessage(188);
            }else{
                //unlockUI
                handler.sendEmptyMessage(189);
            }
        }else if(eventName.equals(OEventName.SOCKET_NOKEY_SET)){
            if(paramObj!=null){
                JsonObject data=(JsonObject)paramObj;
                String     terminalNum          = OJsonGet.getString(data, "terminalNum");//消息说明
                String     name          = OJsonGet.getString(data, "name");//
                String     instruct           = OJsonGet.getString(data, "instruct");//消息
                Log.e("nokeydata", data.toString());
                socketToChange(terminalNum,name,instruct);
            }
        }
        super.receiveEvent(eventName, paramObj);
    }


    private void socketToChange(String terminalNum,String name,String instruct ){

        String currentTermiNum=ManagerCarList.getInstance().getCurrentCar().terminalNum;
        if(!TextUtils.isEmpty(terminalNum)&&!TextUtils.isEmpty(currentTermiNum)&&currentTermiNum.equals(terminalNum)){
            if(TextUtils.isEmpty(name))return;
            if(TextUtils.isEmpty(instruct))return;
            isRecivePush=true;
            if(name.equals("openDistance")){
                if(blueNotouchInData!=null){
                    isSetDisTance = true;
                    int chazhi=getLockAgile(blueNotouchInData.lockAgile);
                    int openValue=0;
                    int closeValue=0;
                    switch (instruct) {
                        case "0":
                            openValue=65;
                            break;
                        case "1":
                            openValue=68;
                            break;
                        case "2":
                            openValue=71;
                            break;
                        case "3":
                            openValue=74;
                            break;
                        case "4":
                            openValue=77;
                            break;
                        case "5":
                            openValue=80;
                            break;
                        case "6":
                            openValue=83;
                            break;
                    }
                    closeValue=openValue+ chazhi;
                    CacheNokeyInfo cacheNokeyInfo = new CacheNokeyInfo();
                    cacheNokeyInfo.openData=openValue;
                    cacheNokeyInfo.closeData=closeValue;
                    sendDataMy(cacheNokeyInfo);
                }
            }else if(name.equals("lockAgile")){
                if(blueNotouchInData!=null){
                    isSetDisTance = true;
                    int openValue=getOpenDistance(blueNotouchInData.openDistance);
                    int closeValue=0;
                    int lockAgile=0;
                    switch (instruct) {
                        case "0":
                            lockAgile=10;
                            break;
                        case "1":
                            lockAgile=8;
                            break;
                        case "2":
                            lockAgile=6;
                            break;
                        case "3":
                            lockAgile=4;
                            break;
                        case "4":
                            lockAgile=2;
                            break;
                    }
                    closeValue=openValue+ lockAgile;
                    CacheNokeyInfo cacheNokeyInfo = new CacheNokeyInfo();
                    cacheNokeyInfo.openData=openValue;
                    cacheNokeyInfo.closeData=closeValue;
                    sendDataMy(cacheNokeyInfo);
                }
            }else if(name.equals("openQty")){
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(getLevelLockCountSocket(instruct,2)), false);
            }else if(name.equals("lockQty")){
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(getLevelLockCountSocket(instruct,1)), false);
            }else if(name.equals("openNear")){
                isSetKaoJInKai = true;
                if(instruct.equals("0")){
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_OPEN_CLOSE, false);
                }else{
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_OPEN_OPEN, false);
                }
            }else if(name.equals("lockNear")){
                isSetLikaisuo = true;
                if(instruct.equals("0")){
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_CLOSE_CLOSE, false);
                }else{
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_CLOSE_OPEN, false);
                }
            }
        }
    }

    private int getLockAgile(int lockAgile){
        int defaultChazhi=0;
        switch (lockAgile){
            case 0:
                defaultChazhi=10;
                break;
            case 1:
                defaultChazhi=8;
                break;
            case 2:
                defaultChazhi=6;
                break;
            case 3:
                defaultChazhi=4;
                break;
            case 4:
                defaultChazhi=2;
                break;
        }
        return defaultChazhi;
    }

    private int getOpenDistance(int opendistance){
        int openValue=0;
        switch (opendistance){
            case 0:
                openValue=65;
                break;
            case 1:
                openValue=68;
                break;
            case 2:
                openValue=71;
                break;
            case 3:
                openValue=74;
                break;
            case 4:
                openValue=77;
                break;
            case 5:
                openValue=80;
                break;
            case 6:
                openValue=83;
                break;
        }
        return openValue;
    }


    private void faqiLianJie() {
//        if(BlueLinkReceiver.getIsBlueConnOK()){
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();//�첽�ģ�����ȴ������ֱ�ӷ��ء�
        } else {
            bluetoothAdapter.startDiscovery();
        }
//        }
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
    private byte[] getLevelLockCountSocket(String s,int type){
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
            case "0":
                instructions[4] = (byte) 0x01;
                break;
            case "1":
                instructions[4] = (byte) 0x02;
                break;
            case "2":
                instructions[4] = (byte) 0x03;
                break;
            case "3":
                instructions[4] = (byte) 0x04;
                break;
            case "4":
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
    private int clickUnLockCount;
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
////                                                        if (com.kulala.linksankula.BuildConfig.DEBUG)
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
//                                                        if (com.kulala.linksankula.BuildConfig.DEBUG)
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
        //back
//        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View view) {
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
//            }
//        });
        unlock_pamduan_count.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickUnLockCount++;
                if(clickUnLockCount==5){
                    OToastButtonBlackNotouchOutDismissStyle.getInstance().show(title_head, new String[]{"5", "4", "3", "2", "1"}, "un_lock_panduan", ViewNoKey.this, "");
                    clickUnLockCount=0;
                }
            }
        });
        lock_pamduan_count.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLockCount++;
                if(clickLockCount==5){
                    OToastButtonBlackNotouchOutDismissStyle.getInstance().show(title_head, new String[]{"5", "4", "3", "2", "1"}, "lock_panduan", ViewNoKey.this, "");
                    clickLockCount=0;
                }
            }
        });
//        unlock_pamduan_count.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        lock_pamduan_count.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        loading_view_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        no_key_pop_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                no_key_pop_layout.setVisibility(View.INVISIBLE);
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
                nokey_window.setType(1, openDeaufaltValue, closeDefaultValue);
                no_key_pop_layout.setVisibility(View.VISIBLE);
            }
        });
        btn_set_likai_lock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                nokey_window.setType(2, openDeaufaltValue, closeDefaultValue);
                no_key_pop_layout.setVisibility(View.VISIBLE);
            }
        });
        //open switch
        txt_nokey_open.img_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
//                    BluePermission.openBlueTooth(getCurrentActivity());
//                    return;
//                }
                if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
                    return;
                }
                isSetKaoJInKai = true;
                if (ManagerNokey.getInstance().getSwitchOpen()) {//之前是打开的,发消息去关
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_OPEN_CLOSE, false);
                } else {
                    type=1;
                    re_windows.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_know.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                re_windows.setVisibility(View.INVISIBLE);
                if(type==1){
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_OPEN_OPEN, false);
                }else if(type==2){
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_CLOSE_OPEN, false);
                }
            }
        });
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(EquipmentManager.isMini()){
                    if(firstNotouchIndata!=null&&!firstNotouchIndata.equals(blueNotouchInData)){
                        Log.e("是否改变", "不一样");
                        BlueNotouchInDataAndTerminum blueNotouchInDataAndTerminum= GetCarInfoUtils.createDeviceSet(blueNotouchInData);
                        if(!TextUtils.isEmpty(blueNotouchInDataAndTerminum.terminalNum)){
                            OCtrlCar.getInstance().ccmd6001_pushDeviceSet(BlueNotouchInDataAndTerminum.toJsonObject(blueNotouchInDataAndTerminum));
                        }
                    }else{
                        Log.e("是否改变", "一样");
                    }
                }
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        iv_open_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeIsDefault = false;
                if (openIsDeaufault) {
                    openDeaufaltValue = 7;
                    closeDefaultValue = 80;
                } else {
                    if (openDeaufaltValue <= 89) {
                        openDeaufaltValue++;
                        closeDefaultValue++;
                    }
                }
                txt_elc_open.setText(openDeaufaltValue + "");
                txt_elc_close.setText(closeDefaultValue + "");
                sendData();
            }
        });

        iv_open_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeIsDefault = false;
                if (openIsDeaufault) {
                    openDeaufaltValue = 77;
                    closeDefaultValue = 80;
                } else {
                    if (openDeaufaltValue >= 51) {
                        openDeaufaltValue--;
                        closeDefaultValue--;
                    }
                }
                txt_elc_open.setText(openDeaufaltValue + "");
                txt_elc_close.setText(closeDefaultValue + "");
                sendData();
            }
        });
        iv_close_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (closeIsDefault) {
                    closeDefaultValue = 80;
                } else {
                    if ((closeDefaultValue - openDeaufaltValue) <= 8 && closeDefaultValue <= 98) {
                        closeDefaultValue++;
                    } else {
                        new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("已达到最大值").show();
                    }
                }
                txt_elc_close.setText(closeDefaultValue + "");
                sendData();
            }
        });
        iv_close_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (closeIsDefault) {
                    closeDefaultValue = 80;
                } else {
                    if (closeDefaultValue >= 52 && (closeDefaultValue - openDeaufaltValue) >= 4) {
                        closeDefaultValue--;
                    } else {
                        new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("已达到最小值").show();
                    }
                }
                txt_elc_close.setText(closeDefaultValue + "");
                sendData();
            }
        });

        btn_set_close_2.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (openDeaufaltValue == 0 && closeDefaultValue == 0) {
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先设置靠近开锁和远离关锁的距离").show();
                    return;
                }
                String CMD_SET_DISTANCE_OPEN = "0x84 02 04 01 " + openDeaufaltValue;
                String CMD_SET_DISTANCE_CLOSE = "0x84 02 05 01 " + closeDefaultValue;
                if (BuildConfig.DEBUG)
                    Log.e("看数据", "CMD_SET_DISTANCE_OPEN" + CMD_SET_DISTANCE_OPEN + "CMD_SET_DISTANCE_CLOSE" + CMD_SET_DISTANCE_CLOSE);
                if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
                    return;
                }
                byte[] dataOpen = new byte[5];
                dataOpen[0] = (byte) 0x84;
                dataOpen[1] = (byte) 0x02;
                dataOpen[2] = (byte) 0x04;
                dataOpen[3] = (byte) openDeaufaltValue;
                dataOpen[4] = (byte) ((dataOpen[0] + dataOpen[1] + dataOpen[2] + dataOpen[3] + dataOpen[4]) ^ 0xff);
//                BlueAdapter.getInstance().sendMessage(dataOpen);//发送指令
                BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataOpen), false);

                byte[] dataClose = new byte[5];
                dataClose[0] = (byte) 0x84;
                dataClose[1] = (byte) 0x02;
                dataClose[2] = (byte) 0x05;
                dataClose[3] = (byte) closeDefaultValue;
                dataClose[4] = (byte) ((dataClose[0] + dataClose[1] + dataClose[2] + dataClose[3] + dataClose[4]) ^ 0xff);
                BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataClose), false);
//                BlueAdapter.getInstance().sendMessage(dataClose);//发送指令
            }
        });

        //close switch
        txt_nokey_close.img_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
//                    BluePermission.openBlueTooth(getCurrentActivity());
//                    return;
//                }
                if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
                    return;
                }
                isSetLikaisuo = true;
                if (ManagerNokey.getInstance().getSwitchClose()) {//之前是打开的,发消息去关
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_CLOSE_CLOSE, false);
                } else {
                    type=2;
                    re_windows.setVisibility(View.VISIBLE);
                }
            }
        });
        //open 启动搜索
        btn_set_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
//                    BluePermission.openBlueTooth(getCurrentActivity());
//                    return;
//                }
                if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
                    return;
                }
                BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_DISTANCE_OPEN, false);
                //启动跳值
                if (countDownTimerJumpOpen != null) countDownTimerJumpOpen.start();
            }
        });
        //close 启动搜索 2018/08/02去掉离开锁搜索功能
//        btn_set_close.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (BluePermission.checkPermission(getCurrentActivity()) != 1) {
////                    BluePermission.openBlueTooth(getCurrentActivity());
////                    return;
////                }
//                if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
//                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
//                    return;
//                }
//                BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_DISTANCE_CLOSE,false);
//                //启动跳值
//                if(countDownTimerJumpClose!=null)countDownTimerJumpClose.start();
//            }
//        });
//        iv_open_add,iv_open_delete,iv_close_add,iv_close_delete,btn_set_close_2

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

    }

    CountDownTimerMy countDownTimerJumpOpen = new CountDownTimerMy(6000L, 250L) {
        @Override
        public void onTick(long millisUntilFinished) {
            handler.obtainMessage(HANDLER_JUMP_OPEN).sendToTarget();
        }

        @Override
        public void onFinish() {
        }
    };
    CountDownTimerMy countDownTimerJumpClose = new CountDownTimerMy(6000L, 250L) {
        @Override
        public void onTick(long millisUntilFinished) {
            handler.obtainMessage(HANDLER_JUMP_CLOSE).sendToTarget();
        }

        @Override
        public void onFinish() {
        }
    };

    public void onDataReceived(long carId, int dataType, byte[] data) {
        if (data == null) return;
        //自动开关锁状态
        if (dataType == 0x24) {
            if (data.length != 2) return;
            if (BuildConfig.DEBUG)
                Log.e("<<blue>>", "onDataReceive:" + dataType + " " + OConver.bytesToHexString(data));
            int type = data[0];
            int value = data[1];
            if (type == 0) {
                isOpenBack = true;
                ManagerNokey.getInstance().setSwitchOpen((value == 1) ? true : false);
                handler.obtainMessage(HANDLER_CHANGE_OPEN_STATUS).sendToTarget();//设置值
                blueNotouchInData.openNear=value;
                pushToserver();
            } else if (type == 1) {
                isCloseBack = true;
                ManagerNokey.getInstance().setSwitchClose((value == 1) ? true : false);
                handler.obtainMessage(HANDLER_CHANGE_CLOSE_STATUS).sendToTarget();//设置值
                blueNotouchInData.lockNear=value;
                pushToserver();
            } else if (type == 2) {
                isOpenDataBack = true;
                ManagerNokey.getInstance().setSwitchOpenValue(value);
                handler.obtainMessage(HANDLER_CHANGE_OPEN_DATA).sendToTarget();//设置值
                if (countDownTimerJumpOpen != null) countDownTimerJumpOpen.cancel();
            } else if (type == 3) {
                isCloseDataBack = true;
                ManagerNokey.getInstance().setSwitchCloseValue(value);
                handler.obtainMessage(HANDLER_CHANGE_CLOSE_DATA).sendToTarget();//设置值
                if (countDownTimerJumpClose != null) countDownTimerJumpClose.cancel();
            }
        }
    }

    // ===================================================

    private static final int HANDLER_CHANGE_OPEN_STATUS = 10011;
    private static final int HANDLER_CHANGE_CLOSE_STATUS = 10012;
    private static final int HANDLER_JUMP_OPEN = 10013;
    private static final int HANDLER_JUMP_CLOSE = 10014;
    private static final int TIP_WINDOW_SHOW = 10015;
    private static final int NOKEY_SET_WINDOW_SHOW = 10016;
    private static final int HANDLER_CHANGE_CLOSE_DATA = 10017;
    private static final int HANDLER_CHANGE_OPEN_DATA = 10018;

    private void sendDataOpen() {
        if (openDeaufaltValue == 0 && closeDefaultValue == 0) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先设置靠近开锁和远离关锁的距离").show();
            return;
        }
        String CMD_SET_DISTANCE_OPEN = "0x84 02 04 01 " + openDeaufaltValue;
        String CMD_SET_DISTANCE_CLOSE = "0x84 02 05 01 " + closeDefaultValue;
        if (BuildConfig.DEBUG)
            Log.e("看数据", "CMD_SET_DISTANCE_OPEN" + CMD_SET_DISTANCE_OPEN + "CMD_SET_DISTANCE_CLOSE" + CMD_SET_DISTANCE_CLOSE);
        if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
            return;
        }
        byte[] dataOpen = new byte[5];
        dataOpen[0] = (byte) 0x84;
        dataOpen[1] = (byte) 0x02;
        dataOpen[2] = (byte) 0x04;
        dataOpen[3] = (byte) openDeaufaltValue;
        dataOpen[4] = (byte) ((dataOpen[0] + dataOpen[1] + dataOpen[2] + dataOpen[3] + dataOpen[4]) ^ 0xff);
//                BlueAdapter.getInstance().sendMessage(dataOpen);//发送指令
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataOpen), false);
    }

    private void sendDataClose() {
        if (openDeaufaltValue == 0 && closeDefaultValue == 0) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先设置靠近开锁和远离关锁的距离").show();
            return;
        }
        String CMD_SET_DISTANCE_OPEN = "0x84 02 04 01 " + openDeaufaltValue;
        String CMD_SET_DISTANCE_CLOSE = "0x84 02 05 01 " + closeDefaultValue;
        if (BuildConfig.DEBUG)
            Log.e("看数据", "CMD_SET_DISTANCE_OPEN" + CMD_SET_DISTANCE_OPEN + "CMD_SET_DISTANCE_CLOSE" + CMD_SET_DISTANCE_CLOSE);
        if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
            return;
        }
        byte[] dataClose = new byte[5];
        dataClose[0] = (byte) 0x84;
        dataClose[1] = (byte) 0x02;
        dataClose[2] = (byte) 0x05;
        dataClose[3] = (byte) closeDefaultValue;
        dataClose[4] = (byte) ((dataClose[0] + dataClose[1] + dataClose[2] + dataClose[3] + dataClose[4]) ^ 0xff);
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataClose), false);
    }

    private void sendData() {
        if (openDeaufaltValue == 0 && closeDefaultValue == 0) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先设置靠近开锁和远离关锁的距离").show();
            return;
        }
        String CMD_SET_DISTANCE_OPEN = "0x84 02 04 01 " + openDeaufaltValue;
        String CMD_SET_DISTANCE_CLOSE = "0x84 02 05 01 " + closeDefaultValue;
        if (BuildConfig.DEBUG)
            Log.e("看数据", "CMD_SET_DISTANCE_OPEN" + CMD_SET_DISTANCE_OPEN + "CMD_SET_DISTANCE_CLOSE" + CMD_SET_DISTANCE_CLOSE);
        if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
            return;
        }
        byte[] dataOpen = new byte[5];
        dataOpen[0] = (byte) 0x84;
        dataOpen[1] = (byte) 0x02;
        dataOpen[2] = (byte) 0x04;
        dataOpen[3] = (byte) openDeaufaltValue;
        dataOpen[4] = (byte) ((dataOpen[0] + dataOpen[1] + dataOpen[2] + dataOpen[3] + dataOpen[4]) ^ 0xff);
//                BlueAdapter.getInstance().sendMessage(dataOpen);//发送指令
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataOpen), false);
        byte[] dataClose = new byte[5];
        dataClose[0] = (byte) 0x84;
        dataClose[1] = (byte) 0x02;
        dataClose[2] = (byte) 0x05;
        dataClose[3] = (byte) closeDefaultValue;
        dataClose[4] = (byte) ((dataClose[0] + dataClose[1] + dataClose[2] + dataClose[3] + dataClose[4]) ^ 0xff);
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataClose), false);
    }

    private void sendDataMy(CacheNokeyInfo cacheNokeyInfo) {
        if (cacheNokeyInfo == null || cacheNokeyInfo.openData == 0 && cacheNokeyInfo.closeData == 0) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先设置靠近开锁和远离关锁的距离").show();
            return;
        }
        String CMD_SET_DISTANCE_OPEN = "0x84 02 04 01 " + cacheNokeyInfo.openData;
        String CMD_SET_DISTANCE_CLOSE = "0x84 02 05 01 " + cacheNokeyInfo.closeData;
        if (BuildConfig.DEBUG)
            Log.e("看数据", "CMD_SET_DISTANCE_OPEN" + CMD_SET_DISTANCE_OPEN + "CMD_SET_DISTANCE_CLOSE" + CMD_SET_DISTANCE_CLOSE);
        if (!BlueLinkReceiver.getIsBlueConnOK()) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
            return;
        }
        byte[] dataOpen = new byte[5];
        dataOpen[0] = (byte) 0x84;
        dataOpen[1] = (byte) 0x02;
        dataOpen[2] = (byte) 0x04;
        dataOpen[3] = (byte) cacheNokeyInfo.openData;
        dataOpen[4] = (byte) ((dataOpen[0] + dataOpen[1] + dataOpen[2] + dataOpen[3] + dataOpen[4]) ^ 0xff);
//                BlueAdapter.getInstance().sendMessage(dataOpen);//发送指令
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataOpen), false);
        final byte[] dataClose = new byte[5];
        dataClose[0] = (byte) 0x84;
        dataClose[1] = (byte) 0x02;
        dataClose[2] = (byte) 0x05;
        dataClose[3] = (byte) cacheNokeyInfo.closeData;
        dataClose[4] = (byte) ((dataClose[0] + dataClose[1] + dataClose[2] + dataClose[3] + dataClose[4]) ^ 0xff);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataClose), false);
            }
        }, 200);
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int randomv = (new Random().nextInt(90)) + 9;
            switch (msg.what) {
                case HANDLER_JUMP_OPEN:
                    txt_elc_open.setText("" + randomv);
                    openDeaufaltValue = randomv;
                    break;
                case HANDLER_JUMP_CLOSE:
                    txt_elc_close.setText("" + randomv);
                    closeDefaultValue = randomv;
                    break;
                case HANDLER_CHANGE_OPEN_STATUS:
                    if (isSetKaoJInKai) {
                        if (ManagerNokey.getInstance().getSwitchOpen()) {
                            txt_nokey_open.setRightImg(R.drawable.car_set_on);
                            lin_nokey_open.setVisibility(VISIBLE);
                        } else {
                            txt_nokey_open.setRightImg(R.drawable.car_set_off);
                            lin_nokey_open.setVisibility(GONE);
                        }
                    } else {
                        setUI();
                    }
//                    Log.e("钥匙打野", "开启开关返回");
//                    if (ManagerNokey.getInstance().getSwitchOpen()) {
//                        txt_nokey_open.setRightImg(R.drawable.car_set_on);
//                        lin_nokey_open.setVisibility(VISIBLE);
//                    } else {//Type设置
//                        txt_nokey_open.setRightImg(R.drawable.car_set_off);
//                        lin_nokey_open.setVisibility(GONE);
//                        return;
//                    }


//                    lin_nokey_open.setVisibility(VISIBLE);

//                    if(openValue>99||openValue<=50){
//                        openDeaufaltValue=70;
//                        openIsDeaufault=true;
//                        txt_elc_open.setText(""+openDeaufaltValue);
//                    }else{
//                        openDeaufaltValue=openValue;
//                        txt_elc_open.setText(""+openValue);
//                        openIsDeaufault=false;
//                    }

                    Log.e("靠近開殺殺殺", "1" + "closeDefaultValue" + closeDefaultValue + "openDefal" + openDeaufaltValue);
//                    setTextValue(openDeaufaltValue);
//                    setTextLevel(openDeaufaltValue,closeDefaultValue);
                    break;
                case HANDLER_CHANGE_CLOSE_STATUS:
                    if (isSetLikaisuo) {
                        if (ManagerNokey.getInstance().getSwitchClose()) {
                            txt_nokey_close.setRightImg(R.drawable.car_set_on);
                            lin_nokey_close.setVisibility(VISIBLE);
                        } else {//Type设置
                            txt_nokey_close.setRightImg(R.drawable.car_set_off);
                            lin_nokey_close.setVisibility(GONE);
                        }
                    }else{
                        setUI();
                    }

//                    Log.e("钥匙打野", "关闭开关返回");
//                    if (ManagerNokey.getInstance().getSwitchClose()) {
//                        txt_nokey_close.setRightImg(R.drawable.car_set_on);
//                        lin_nokey_close.setVisibility(VISIBLE);
//                    } else {//Type设置
//                        txt_nokey_close.setRightImg(R.drawable.car_set_off);
//                        lin_nokey_close.setVisibility(GONE);
//                        return;
//                    }

//                    lin_nokey_close.setVisibility(VISIBLE);

//                    int closeValue = ManagerNokey.getInstance().getSwitchCloseValue();
//                    if(closeValue > 98 || closeValue < 71){
//                        closeDefaultValue=84;
//                        closeIsDefault=true;
////                        txt_elc_close.setText(""+closeDefaultValue);
//                    }else{
//                        closeDefaultValue=closeValue;
//                        closeIsDefault=false;
////                        txt_elc_close.setText(""+closeDefaultValue);
//                    }
//                    if(openDeaufaltValue==0){
//                        openDeaufaltValue=closeDefaultValue-8;
//                    }
//                    Log.e("靠近開殺殺殺", "2"+"closeDefaultValue"+closeDefaultValue+"openDefal"+openDeaufaltValue);
//                    setTextLevel(openDeaufaltValue,closeDefaultValue);
                    break;
                case TIP_WINDOW_SHOW:
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("设置开的距离" + openDeaufaltValue + "  关的距离" + closeDefaultValue).show();
                    break;
                case NOKEY_SET_WINDOW_SHOW:
                    no_key_pop_layout.setVisibility(View.INVISIBLE);
                    break;
                case HANDLER_CHANGE_OPEN_DATA:
                    if(isSetDisTance){
                        int openValue = ManagerNokey.getInstance().getSwitchOpenValue();
                        switch (openValue) {
                            case 65:
                            case 68:
                            case 71:
                            case 74:
                            case 77:
                            case 80:
                            case 83:
                                openIsDeaufault = false;
                                openDeaufaltValue = openValue;
                                break;
                            default:
                                openIsDeaufault = true;
                                openDeaufaltValue = 74;
                                break;
                        }
                    }else{
                        setUI();
                    }
                    //靠近开数据返回
//                    Log.e("钥匙打野", "开启数据返回");
//                    int openValue = ManagerNokey.getInstance().getSwitchOpenValue();
//                    switch (openValue) {
//                        case 65:
//                        case 68:
//                        case 71:
//                        case 74:
//                        case 77:
//                        case 80:
//                        case 83:
//                            openIsDeaufault = false;
//                            openDeaufaltValue = openValue;
//                            break;
//                        default:
//                            openIsDeaufault = true;
//                            openDeaufaltValue = 74;
//                            break;
//                    }
                    break;
                case HANDLER_CHANGE_CLOSE_DATA:
                    if(isSetDisTance){
                        //离开所数据返回
                        setTextValue(openDeaufaltValue);
                        int closeValue = ManagerNokey.getInstance().getSwitchCloseValue();
                        setTextLevel(openDeaufaltValue, closeValue);
                    }else{
                        setUI();
                    }
//                    Log.e("钥匙打野", "关闭数据返回");
//                    //离开所数据返回
//                    setTextValue(openDeaufaltValue);
//                    int closeValue = ManagerNokey.getInstance().getSwitchCloseValue();
//                    setTextLevel(openDeaufaltValue, closeValue);
                    break;

                case 188:
                 lock_pamduan_count.txt_bottom.setText(BlueInstructionCollection.getInstance().getLockCount()+"");
                    blueNotouchInData.lockQty=BlueInstructionCollection.getInstance().getLockCount()-1;
                    pushToserver();
                    break;
                case 189:
                    isFirstIn++;
                    unlock_pamduan_count.txt_bottom.setText(BlueInstructionCollection.getInstance().getUnLockCount()+"");
                    blueNotouchInData.openQty=BlueInstructionCollection.getInstance().getUnLockCount()-1;
                    pushToserver();
                    if(isFirstIn==1){
                        Log.e("是否改变", "第一次");
                        firstNotouchIndata=new BlueNotouchInData();
                        setCacheData(firstNotouchIndata,blueNotouchInData);
                        BlueNotouchInDataAndTerminum notouchInDataAndTerminum=GetCarInfoUtils.createDeviceSet(blueNotouchInData);
                        if(EquipmentManager.isMini()){
                            if(!TextUtils.isEmpty(notouchInDataAndTerminum.terminalNum)){
                                OCtrlCar.getInstance().ccmd6001_pushDeviceSet(BlueNotouchInDataAndTerminum.toJsonObject(notouchInDataAndTerminum));
                            }
                        }
                    }
                    break;

            }
        }
        private void setCacheData(BlueNotouchInData data1,BlueNotouchInData data2){
            data1.lockNear=data2.lockNear;
            data1.lockAgile=data2.lockAgile;
            data1.lockQty=data2.lockQty;
            data1.openNear=data2.openNear;
            data1.openDistance=data2.openDistance;
            data1.openQty=data2.openQty;
        }

        private void setUI() {
            if (isOpenBack && isCloseBack && isOpenDataBack && isCloseDataBack) {
                isOpenDataBack = false;
                isOpenBack = false;
                isCloseDataBack = false;
                isCloseBack = false;
                if (ManagerNokey.getInstance().getSwitchOpen()) {
                    txt_nokey_open.setRightImg(R.drawable.car_set_on);
                    lin_nokey_open.setVisibility(VISIBLE);
                } else {
                    txt_nokey_open.setRightImg(R.drawable.car_set_off);
                    lin_nokey_open.setVisibility(GONE);
                }

                if (ManagerNokey.getInstance().getSwitchClose()) {
                    txt_nokey_close.setRightImg(R.drawable.car_set_on);
                    lin_nokey_close.setVisibility(VISIBLE);
                } else {
                    txt_nokey_close.setRightImg(R.drawable.car_set_off);
                    lin_nokey_close.setVisibility(GONE);
                }

                int openValue = ManagerNokey.getInstance().getSwitchOpenValue();
                switch (openValue) {
                    case 65:
                    case 68:
                    case 71:
                    case 74:
                    case 77:
                    case 80:
                    case 83:
                        openIsDeaufault = false;
                        openDeaufaltValue = openValue;
                        break;
                    default:
                        openIsDeaufault = true;
                        openDeaufaltValue = 74;
                        break;
                }

                //离开所数据返回
                setTextValue(openDeaufaltValue);
                int closeValue = ManagerNokey.getInstance().getSwitchCloseValue();
                setTextLevel(openDeaufaltValue, closeValue);

            }
        }
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
        }
        txt_elc_open.setText(openStr);
    }

    private void setTextLevel(int openData, int closeData) {
        int chazhi = closeData - openData;
        int defaultChazhi;
        if (chazhi == 2) {
            defaultChazhi = 2;
            txt_elc_close.setText("最高");
        } else if (chazhi == 4) {
            defaultChazhi = 4;
            txt_elc_close.setText("高");
        } else if (chazhi == 6) {
            defaultChazhi = 6;
            txt_elc_close.setText("中");
        } else if (chazhi == 8) {
            defaultChazhi = 8;
            txt_elc_close.setText("低");
        } else if (chazhi == 10) {
            defaultChazhi = 10;
            txt_elc_close.setText("最低");
        } else {
            defaultChazhi = 6;
            txt_elc_close.setText("中");
            closeDefaultValue = openDeaufaltValue + 6;
            sendData();
            isSetDisTance = true;
        }
        closeDefaultValue = openDeaufaltValue + defaultChazhi;
        setOpenDistance(openDeaufaltValue);
        setLockAgile(defaultChazhi);
        pushToserver();
        if(handler!=null){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loading_view_layout.setVisibility(View.INVISIBLE);
                }
            },1000);
        }
    }
    private void pushToserver(){
        if(isRecivePush){
            if(EquipmentManager.isMini()){
                BlueNotouchInDataAndTerminum blueNotouchInDataAndTerminum=GetCarInfoUtils.createDeviceSet(blueNotouchInData);
                if(!TextUtils.isEmpty(blueNotouchInDataAndTerminum.terminalNum)){
                    OCtrlCar.getInstance().ccmd6001_pushDeviceSet(BlueNotouchInDataAndTerminum.toJsonObject(blueNotouchInDataAndTerminum));
                }
                isRecivePush=false;
            }
        }
    }
    private void setOpenDistance(int openValue){
        switch (openValue){
            case 65:
                blueNotouchInData.openDistance=0;
                break;
            case 68:
                blueNotouchInData.openDistance=1;
                break;
            case 71:
                blueNotouchInData.openDistance=2;
                break;
            case 74:
                blueNotouchInData.openDistance=3;
                break;
            case 77:
                blueNotouchInData.openDistance=4;
                break;
            case 80:
                blueNotouchInData.openDistance=5;
                break;
            case 83:
                blueNotouchInData.openDistance=6;
                break;
        }

    }

    private void setLockAgile(int defaultChazhi){
        switch (defaultChazhi){
            case 2:
                blueNotouchInData.lockAgile=4;
                break;
            case 4:
                blueNotouchInData.lockAgile=3;
                break;
            case 6:
                blueNotouchInData.lockAgile=2;
                break;
            case 8:
                blueNotouchInData.lockAgile=1;
                break;
            case 10:
                blueNotouchInData.lockAgile=0;
                break;
        }
    }
}
