package view.view4me.shake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.BuildConfig;
import com.kulala.staticsfunc.static_system.OConver;
import com.kulala.staticsfunc.time.CountDownTimerMy;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.ToastTxt;

import java.util.Random;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import common.global.TextViewEC;
import model.ManagerNokey;
import view.clip.ClipLineBtnTxt;
import view.view4me.set.ClipTitleMeSet;

public class ViewNoKeyNew extends LinearLayoutBase {

    private  ClipTitleMeSet title_head;
    private  RelativeLayout lin_nokey_open;
    private  RelativeLayout lin_nokey_close;
    private  ClipLineBtnTxt txt_nokey_open;
    private  ClipLineBtnTxt txt_nokey_close;
    private  TextViewEC txt_elc_open;
    private  TextViewEC txt_elc_close;
    private  Button btn_set_open;
    private  Button btn_set_kaojin_open;
    private  Button btn_set_likai_lock;

    private  Button btn_set_close;
    private  Button btn_set_close_2;
    private MyHandler handler = new MyHandler();
    public static ViewNoKeyNew viewNoKeyThis;
    private boolean openIsDeaufault=false;
    private boolean closeIsDefault=false;
    private int openDeaufaltValue=0;
    private int closeDefaultValue=0;

    private  ImageView iv_open_add;
    private  ImageView iv_open_delete;
    private  ImageView iv_close_add;
    private  ImageView iv_close_delete;
    private ViewNokeyPopWindow nokey_window;
    public ViewNoKeyNew(Context context, AttributeSet attrs) {
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
        txt_nokey_open = (ClipLineBtnTxt) findViewById(R.id.txt_nokey_open);
        txt_nokey_close = (ClipLineBtnTxt) findViewById(R.id.txt_nokey_close);
        btn_set_likai_lock = (Button) findViewById(R.id.btn_set_likai_lock);
        btn_set_kaojin_open = (Button) findViewById(R.id.btn_set_kaojin_open);
        nokey_window =findViewById(R.id.nokey_window);
        txt_nokey_open.setRightImgSize(50);
        txt_nokey_close.setRightImgSize(50);
        initViews();
        initEvents();
    }
    @Override
    protected void initViews() {
        ODispatcher.addEventListener(OEventName.NOKEY_SET_INFO,this);
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
    public void onBlueConnOK(){
        sendQueryState();
    }
    private void sendQueryState(){
        //取开关状态,蓝牙
        new CountDownTimerMy(5000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(messageNum == 0){
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_ASK_SWITCH_OPEN,false);
                }else if(messageNum == 1){
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_ASK_SWITCH_CLOSE,false);
                }else if(messageNum == 2){
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_ASK_DISTANCE_OPEN,false);
                }else if(messageNum == 3){
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_ASK_DISTANCE_CLOSE,false);
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
        viewNoKeyThis = null;
        super.onDetachedFromWindow();
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if(eventName.equals(OEventName.NOKEY_SET_INFO)){
            CacheNokeyInfo cacheNokeyInfo=(CacheNokeyInfo)paramObj;
            sendDataMy(cacheNokeyInfo);
            handler.obtainMessage(NOKEY_SET_WINDOW_SHOW).sendToTarget();
        }
        super.receiveEvent(eventName, paramObj);
    }
    @Override
    protected void initEvents() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        btn_set_kaojin_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                nokey_window.setType(1,openDeaufaltValue,closeDefaultValue);
                nokey_window.setVisibility(View.VISIBLE);
            }
        });
        btn_set_likai_lock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                nokey_window.setType(2,openDeaufaltValue,closeDefaultValue);
                nokey_window.setVisibility(View.VISIBLE);
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
                if (ManagerNokey.getInstance().getSwitchOpen()) {//之前是打开的,发消息去关
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_OPEN_CLOSE,false);
                } else {
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_OPEN_OPEN,false);
                }
            }
        });
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        iv_open_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeIsDefault=false;
                if(openIsDeaufault){
                    openDeaufaltValue=7;
                    closeDefaultValue=80;
                }else{
                    if(openDeaufaltValue<=89){
                        openDeaufaltValue++;
                        closeDefaultValue++;
                    }
                }
                txt_elc_open.setText(openDeaufaltValue+"");
                txt_elc_close.setText(closeDefaultValue+"");
                sendData();
            }
        });

        iv_open_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeIsDefault=false;
                if(openIsDeaufault){
                    openDeaufaltValue=77;
                    closeDefaultValue=80;
                }else{
                    if(openDeaufaltValue>=51){
                        openDeaufaltValue--;
                        closeDefaultValue--;
                    }
                }
                txt_elc_open.setText(openDeaufaltValue+"");
                txt_elc_close.setText(closeDefaultValue+"");
                sendData();
            }
        });
        iv_close_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(closeIsDefault){
                    closeDefaultValue=80;
                }else{
                    if((closeDefaultValue-openDeaufaltValue)<=8&&closeDefaultValue<=98){
                        closeDefaultValue++;
                    }else{
                        new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("已达到最大值").show();
                    }
                }
                txt_elc_close.setText(closeDefaultValue+"");
                sendData();
            }
        });
        iv_close_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(closeIsDefault){
                    closeDefaultValue=80;
                }else{
                    if(closeDefaultValue>=52&&(closeDefaultValue-openDeaufaltValue)>=4){
                        closeDefaultValue--;
                    }else{
                        new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("已达到最小值").show();
                    }
                }
                txt_elc_close.setText(closeDefaultValue+"");
                sendData();
            }
        });

        btn_set_close_2.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if(openDeaufaltValue==0&&closeDefaultValue==0){
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先设置靠近开锁和远离关锁的距离").show();
                    return;
                }
                String CMD_SET_DISTANCE_OPEN="0x84 02 04 01 "+openDeaufaltValue;
                String CMD_SET_DISTANCE_CLOSE="0x84 02 05 01 "+closeDefaultValue;
                 if (BuildConfig.DEBUG) Log.e("看数据", "CMD_SET_DISTANCE_OPEN"+CMD_SET_DISTANCE_OPEN+"CMD_SET_DISTANCE_CLOSE"+CMD_SET_DISTANCE_CLOSE);
                if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
                    return;
                }
                byte[] dataOpen=new byte[5];
                dataOpen[0]= (byte) 0x84;
                dataOpen[1]= (byte) 0x02;
                dataOpen[2]= (byte) 0x04;
                dataOpen[3 ]= (byte) openDeaufaltValue;
                dataOpen[4]= (byte) ((dataOpen[0]+dataOpen[1]+dataOpen[2]+dataOpen[3]+dataOpen[4]) ^ 0xff);
//                BlueAdapter.getInstance().sendMessage(dataOpen);//发送指令
                BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataOpen),false);

                byte[] dataClose=new byte[5];
                dataClose[0]= (byte) 0x84;
                dataClose[1]= (byte) 0x02;
                dataClose[2]= (byte) 0x05;
                dataClose[3 ]= (byte) closeDefaultValue;
                dataClose[4]= (byte) ((dataClose[0]+dataClose[1]+dataClose[2]+dataClose[3]+dataClose[4]) ^ 0xff);
                BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataClose),false);
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
                if (ManagerNokey.getInstance().getSwitchClose()) {//之前是打开的,发消息去关
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_CLOSE_CLOSE,false);
                } else {
                    BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_SWITCH_CLOSE_OPEN,false);
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
                BlueLinkReceiver.getInstance().sendMessage(ManagerNokey.CMD_SET_DISTANCE_OPEN,false);
                //启动跳值
                if(countDownTimerJumpOpen!=null)countDownTimerJumpOpen.start();
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
    public static String bytesToHexString(byte[] src){
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
    CountDownTimerMy countDownTimerJumpOpen =  new CountDownTimerMy(6000L, 250L) {
        @Override
        public void onTick(long millisUntilFinished) {
            handler.obtainMessage(HANDLER_JUMP_OPEN).sendToTarget();
        }
        @Override
        public void onFinish() {
        }
    };
    CountDownTimerMy countDownTimerJumpClose =  new CountDownTimerMy(6000L, 250L) {
        @Override
        public void onTick(long millisUntilFinished) {
            handler.obtainMessage(HANDLER_JUMP_CLOSE).sendToTarget();
        }
        @Override
        public void onFinish() {
        }
    };
    public void onDataReceived(long carId,int dataType,byte[] data) {
        if (data == null) return;
        //自动开关锁状态
        if (dataType == 0x24) {
            if(data.length!=2)return;
            if(BuildConfig.DEBUG
            )  if (BuildConfig.DEBUG) Log.e("<<blue>>","onDataReceive:"+dataType+" "+ OConver.bytesToHexString(data));
            int type = data[0];
            int value = data[1];
            if(type == 0){
                ManagerNokey.getInstance().setSwitchOpen((value == 1) ? true : false);
                handler.obtainMessage(HANDLER_CHANGE_OPEN_STATUS).sendToTarget();//设置值
            }else if(type == 1){
                ManagerNokey.getInstance().setSwitchClose((value == 1) ? true : false);
                handler.obtainMessage(HANDLER_CHANGE_CLOSE_STATUS).sendToTarget();//设置值
            }else if(type == 2){
                ManagerNokey.getInstance().setSwitchOpenValue(value);
                handler.obtainMessage(HANDLER_CHANGE_OPEN_STATUS).sendToTarget();//设置值
                handler.obtainMessage(TIP_WINDOW_SHOW).sendToTarget();//弹提示窗口
                if(countDownTimerJumpOpen!=null)countDownTimerJumpOpen.cancel();
            }else if(type == 3){
                ManagerNokey.getInstance().setSwitchCloseValue(value);
                handler.obtainMessage(HANDLER_CHANGE_CLOSE_STATUS).sendToTarget();//设置值
                if(countDownTimerJumpClose!=null)countDownTimerJumpClose.cancel();
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
    private void sendDataOpen(){
        if(openDeaufaltValue==0&&closeDefaultValue==0){
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先设置靠近开锁和远离关锁的距离").show();
            return;
        }
        String CMD_SET_DISTANCE_OPEN="0x84 02 04 01 "+openDeaufaltValue;
        String CMD_SET_DISTANCE_CLOSE="0x84 02 05 01 "+closeDefaultValue;
         if (BuildConfig.DEBUG) Log.e("看数据", "CMD_SET_DISTANCE_OPEN"+CMD_SET_DISTANCE_OPEN+"CMD_SET_DISTANCE_CLOSE"+CMD_SET_DISTANCE_CLOSE);
        if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
            return;
        }
        byte[] dataOpen=new byte[5];
        dataOpen[0]= (byte) 0x84;
        dataOpen[1]= (byte) 0x02;
        dataOpen[2]= (byte) 0x04;
        dataOpen[3 ]= (byte) openDeaufaltValue;
        dataOpen[4]= (byte) ((dataOpen[0]+dataOpen[1]+dataOpen[2]+dataOpen[3]+dataOpen[4]) ^ 0xff);
//                BlueAdapter.getInstance().sendMessage(dataOpen);//发送指令
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataOpen),false);
    }
    private void sendDataClose(){
        if(openDeaufaltValue==0&&closeDefaultValue==0){
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先设置靠近开锁和远离关锁的距离").show();
            return;
        }
        String CMD_SET_DISTANCE_OPEN="0x84 02 04 01 "+openDeaufaltValue;
        String CMD_SET_DISTANCE_CLOSE="0x84 02 05 01 "+closeDefaultValue;
         if (BuildConfig.DEBUG) Log.e("看数据", "CMD_SET_DISTANCE_OPEN"+CMD_SET_DISTANCE_OPEN+"CMD_SET_DISTANCE_CLOSE"+CMD_SET_DISTANCE_CLOSE);
        if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
            return;
        }
        byte[] dataClose=new byte[5];
        dataClose[0]= (byte) 0x84;
        dataClose[1]= (byte) 0x02;
        dataClose[2]= (byte) 0x05;
        dataClose[3 ]= (byte) closeDefaultValue;
        dataClose[4]= (byte) ((dataClose[0]+dataClose[1]+dataClose[2]+dataClose[3]+dataClose[4]) ^ 0xff);
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataClose),false);
    }

    private void sendData(){
        if(openDeaufaltValue==0&&closeDefaultValue==0){
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先设置靠近开锁和远离关锁的距离").show();
            return;
        }
        String CMD_SET_DISTANCE_OPEN="0x84 02 04 01 "+openDeaufaltValue;
        String CMD_SET_DISTANCE_CLOSE="0x84 02 05 01 "+closeDefaultValue;
         if (BuildConfig.DEBUG) Log.e("看数据", "CMD_SET_DISTANCE_OPEN"+CMD_SET_DISTANCE_OPEN+"CMD_SET_DISTANCE_CLOSE"+CMD_SET_DISTANCE_CLOSE);
        if (!BlueLinkReceiver.getInstance().getIsBlueConnOK()) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
            return;
        }
        byte[] dataOpen=new byte[5];
        dataOpen[0]= (byte) 0x84;
        dataOpen[1]= (byte) 0x02;
        dataOpen[2]= (byte) 0x04;
        dataOpen[3 ]= (byte) openDeaufaltValue;
        dataOpen[4]= (byte) ((dataOpen[0]+dataOpen[1]+dataOpen[2]+dataOpen[3]+dataOpen[4]) ^ 0xff);
//                BlueAdapter.getInstance().sendMessage(dataOpen);//发送指令
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataOpen),false);
        byte[] dataClose=new byte[5];
        dataClose[0]= (byte) 0x84;
        dataClose[1]= (byte) 0x02;
        dataClose[2]= (byte) 0x05;
        dataClose[3 ]= (byte) closeDefaultValue;
        dataClose[4]= (byte) ((dataClose[0]+dataClose[1]+dataClose[2]+dataClose[3]+dataClose[4]) ^ 0xff);
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataClose),false);
    }

    private void sendDataMy(CacheNokeyInfo cacheNokeyInfo){
        if(cacheNokeyInfo==null||cacheNokeyInfo.openData==0&&cacheNokeyInfo.closeData==0){
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("请先设置靠近开锁和远离关锁的距离").show();
            return;
        }
        String CMD_SET_DISTANCE_OPEN="0x84 02 04 01 "+openDeaufaltValue;
        String CMD_SET_DISTANCE_CLOSE="0x84 02 05 01 "+closeDefaultValue;
        if (BuildConfig.DEBUG) Log.e("看数据", "CMD_SET_DISTANCE_OPEN"+CMD_SET_DISTANCE_OPEN+"CMD_SET_DISTANCE_CLOSE"+CMD_SET_DISTANCE_CLOSE);
        if (!BlueLinkReceiver.getIsBlueConnOK()) {
            new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("蓝牙未连接!").show();
            return;
        }
        byte[] dataOpen=new byte[5];
        dataOpen[0]= (byte) 0x84;
        dataOpen[1]= (byte) 0x02;
        dataOpen[2]= (byte) 0x04;
        dataOpen[3 ]= (byte) cacheNokeyInfo.openData;
        dataOpen[4]= (byte) ((dataOpen[0]+dataOpen[1]+dataOpen[2]+dataOpen[3]+dataOpen[4]) ^ 0xff);
//                BlueAdapter.getInstance().sendMessage(dataOpen);//发送指令
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataOpen),false);
        byte[] dataClose=new byte[5];
        dataClose[0]= (byte) 0x84;
        dataClose[1]= (byte) 0x02;
        dataClose[2]= (byte) 0x05;
        dataClose[3 ]= (byte) cacheNokeyInfo.closeData;
        dataClose[4]= (byte) ((dataClose[0]+dataClose[1]+dataClose[2]+dataClose[3]+dataClose[4]) ^ 0xff);
        BlueLinkReceiver.getInstance().sendMessage(bytesToHexString(dataClose),false);
    }
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int randomv = (new Random().nextInt(90))+9;
            switch (msg.what) {
                case HANDLER_JUMP_OPEN:
                    txt_elc_open.setText(""+randomv);
                    openDeaufaltValue=randomv;
                    break;
                case HANDLER_JUMP_CLOSE:
                    txt_elc_close.setText(""+randomv);
                    closeDefaultValue=randomv;
                    break;
                case HANDLER_CHANGE_OPEN_STATUS:
                    if (ManagerNokey.getInstance().getSwitchOpen()) {
                        txt_nokey_open.setRightImg(getResources().getDrawable(R.drawable.switch_on));
                        lin_nokey_open.setVisibility(VISIBLE);
                    } else{//Type设置
                        txt_nokey_open.setRightImg(getResources().getDrawable(R.drawable.switch_off));
                        lin_nokey_open.setVisibility(GONE);
                        return;
                    }
//                    lin_nokey_open.setVisibility(VISIBLE);
                    int openValue = ManagerNokey.getInstance().getSwitchOpenValue();
                    if(openValue>0 && openValue<=9){
                        openIsDeaufault=false;
                        openDeaufaltValue=openValue;
                        txt_elc_open.setText("-"+openValue);
                        btn_set_open.setText("重新设定");
                    }else if(openValue>9 && openValue<=99){
                        openIsDeaufault=false;
                        openDeaufaltValue=openValue;
                        txt_elc_open.setText(""+openValue);
                        btn_set_open.setText("重新设定");
                    }else{
                        openDeaufaltValue=77;
                        openIsDeaufault=true;
                        txt_elc_open.setText("77");
                        btn_set_open.setText("初始设定");
                        sendDataOpen();
                    }
                    break;
                case HANDLER_CHANGE_CLOSE_STATUS:
                    if (ManagerNokey.getInstance().getSwitchClose()) {
                        txt_nokey_close.setRightImg(getResources().getDrawable(R.drawable.switch_on));
                        lin_nokey_close.setVisibility(VISIBLE);
                    } else{//Type设置
                        txt_nokey_close.setRightImg(getResources().getDrawable(R.drawable.switch_off));
                        lin_nokey_close.setVisibility(GONE);
                        return;
                    }
//                    lin_nokey_close.setVisibility(VISIBLE);
                    int closeValue = ManagerNokey.getInstance().getSwitchCloseValue();
                    if(closeValue>0 && closeValue<=9){
                        closeIsDefault=false;
                        closeDefaultValue=closeValue;
                        txt_elc_close.setText("-"+closeValue);
                        btn_set_close.setText("重新设定");
                    }else if(closeValue>9 && closeValue<=99){
                        closeIsDefault=false;
                        closeDefaultValue=closeValue;
                        txt_elc_close.setText(""+closeValue);
                        btn_set_close.setText("重新设定");
                    }else{
                        closeDefaultValue=80;
                        closeIsDefault=true;
                        txt_elc_close.setText("80");
                        btn_set_close.setText("初始设定");
                        sendDataClose();
                    }
                    break;
                case TIP_WINDOW_SHOW:
                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("设置开的距离"+openDeaufaltValue+"  关的距离"+closeDefaultValue).show();
                    break;
                case NOKEY_SET_WINDOW_SHOW:
                    nokey_window.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }
}
