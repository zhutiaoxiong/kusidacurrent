package view.view4info.temcontrol;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.toast.OToastInput;
import com.kulala.staticsview.toast.ToastConfirmNormal;

import java.util.ArrayList;
import java.util.List;

import adapter.RecycleViewDivider;
import common.GlobalContext;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.remotecontrol.BeanRemoteForRecycleView;
import model.remotecontrol.ManagerRemoteControl;
import view.clip.ClipPopLoading;

import view.view4me.nfcmoudle.OnClickListenerMy5000;
import view.view4me.set.ClipTitleMeSet;
import view.view4me.shake.ClipLeftTxtRightCheckBox;

public class ViewTemControl extends RelativeLayoutBase {
    private final ClipTitleMeSet title_head;
    private final RecyclerView recycleview_temcontrol;
    private ViewTemControlRecycleViewAdapter temControlAdapter;
    private List<BeanRemoteForRecycleView> data;
    private final ImageView gif_tem;
    private final LinearLayout tem_read_view;
    private int cachePosition;
    private final Button btn_re_get;
    private boolean getCardMessageBack;
    private boolean readCardMessageBack;
    private boolean isOpretion;//是否添加删除卡片操作
    private final View zhanweiview;
    private Button btn_learn;
    private View line_one;
    private ClipLeftTxtRightCheckBox nokey_btn;


    private final Handler myHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 11111) {
                tem_read_view.setVisibility(View.INVISIBLE);
                zhanweiview.setVisibility(View.INVISIBLE);
            } else if (msg.what == 11112)  {
                switch (msg.arg1){
                    case 1:
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "操作成功");
                        break;
                    case 2:
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "操作失败");
                        break;
                }
            }else if (msg.what == 11113) {
                //开始转圈
                if (BuildConfig.DEBUG) Log.e("转圈", "2104获取失败开始转圈");
                ClipPopLoading.getInstance().show(btn_re_get);
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //结束转圈
                        if (BuildConfig.DEBUG) Log.e("转圈", "5秒之后结束转圈");
                        ClipPopLoading.getInstance().stopLoading();
                        if (!getCardMessageBack) {
                            if (BuildConfig.DEBUG) Log.e("转圈", "2104获取失败之后5秒还是失败提示操作失败");
                            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无法获取遥控器信息");
                        }
                    }
                }, 5000);
            }else if(msg.what == 11114){
                switch (msg.arg1){
                    case 1:
                        //显示列表
                        recycleview_temcontrol.setVisibility(View.VISIBLE);
                        line_one.setVisibility(View.VISIBLE);
                        nokey_btn.setVisibility(View.VISIBLE);
                        btn_learn.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        recycleview_temcontrol.setVisibility(View.INVISIBLE);
                        line_one.setVisibility(View.INVISIBLE);
                        nokey_btn.setVisibility(View.INVISIBLE);
                        btn_learn.setVisibility(View.VISIBLE);
                        //显示textview
                        break;
                }
            }else if(msg.what==11115){
                ClipPopLoading.getInstance().stopLoading();
            }
        }
    };
    public ViewTemControl(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_tem_contorl, this, true);
        title_head = findViewById(R.id.title_head);
        recycleview_temcontrol = findViewById(R.id.recycleview_temcontrol);
        gif_tem = findViewById(R.id.gif_tem);
        tem_read_view = findViewById(R.id.tem_read_view);
        btn_re_get = findViewById(R.id.btn_re_get);
        zhanweiview= findViewById(R.id.zhanweiview);
        btn_learn= findViewById(R.id.btn_learn);
        line_one= findViewById(R.id.line_one);
        nokey_btn= findViewById(R.id.nokey_btn);
        initViews();
        initEvents();
        OCtrlCar.getInstance().ccmd2105_RemoteControl(ManagerCarList.getInstance().getCurrentCarID(),false);
        ODispatcher.addEventListener(OEventName.QURRY_REMOTE_CONTROL_RESULT, this);
        ODispatcher.addEventListener(OEventName.ADD_OR_DELETE_REMOTE_CONTROL, this);
        ODispatcher.addEventListener(OEventName.TEM_CONTROL_SOCKET_RESULT_BACK, this);
    }

    @Override
    protected void initViews() {
        data = new ArrayList<>();
        data= ManagerRemoteControl.getInstance().createinitRemoteControlList();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        recycleview_temcontrol.setLayoutManager(mLinearLayoutManager);
        RecycleViewDivider driver = new RecycleViewDivider(
                getContext(), LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(getContext(),15), Color.parseColor("#000000"));
        recycleview_temcontrol.setHasFixedSize(true);
        recycleview_temcontrol.setItemAnimator(new DefaultItemAnimator());
        if (recycleview_temcontrol.getItemDecorationCount() == 0) {
            recycleview_temcontrol.addItemDecoration(driver);
        }
        if (temControlAdapter == null) {
            temControlAdapter = new ViewTemControlRecycleViewAdapter(data, getContext());
            recycleview_temcontrol.setAdapter(temControlAdapter);
        } else {
            temControlAdapter.setData(data);
            temControlAdapter.notifyDataSetChanged();
        }
        temControlAdapter.setOnItemClickListener(new ViewTemControlRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void centerTxtClick(View view, int position) {
                cachePosition = position;
                OToastInput.getInstance().showInput(title_head, "", "编辑智卡名称:", new String[]{OToastInput.OTHER_TEXT}, "nfcname", ViewTemControl.this);
            }
            @Override
            public void rightTxtClick(View view, int position) {
                cachePosition = position;
                if (data.get(position).status.equals("0")) {
                    //添加卡
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withInfo("点击【确定】进入学习模式时喇叭响一声，这时请短按遥控任意键进行学习，学习成功APP界面返回成功提示后即可使用。")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm ) {
                                        //写卡
                                        isOpretion=true;
                                        OCtrlCar.getInstance().ccmd2106_AddOrDelete_RemoteControl(ManagerCarList.getInstance().getCurrentCarID(), getCardFromPosition(), "03");
                                        tem_read_view.setVisibility(View.VISIBLE);
                                        zhanweiview.setVisibility(View.VISIBLE);
                                        RequestOptions options = new RequestOptions()
                                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                                        Glide.with(getContext()).load(R.drawable.img_tem_connecting).apply(options).into(gif_tem);
                                        if (BuildConfig.DEBUG) Log.e("转圈", "开始写卡");
                                        readCardMessageBack = false;
                                        myHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!readCardMessageBack) {
                                                    if (BuildConfig.DEBUG) Log.e("转圈", "15秒之后没收到消息提示写卡操作失败");
                                                    showToast(2);
                                                }
                                                if (BuildConfig.DEBUG) Log.e("转圈", "15秒之后停止写卡GIF");
                                                tem_read_view.setVisibility(View.INVISIBLE);
                                               zhanweiview. setVisibility(View.INVISIBLE);
                                            }
                                        }, 15000);
                                    }
                                }
                            })
                            .show();
                } else {
                    //删除卡
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("提示")
                            .withInfo("遥控器移除后将不能控制车辆")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm ) {
                                        isOpretion=true;
                                        OCtrlCar.getInstance().ccmd2106_AddOrDelete_RemoteControl(ManagerCarList.getInstance().getCurrentCarID(), getCardFromPosition(), "04");
                                    }
                                }
                            })
                            .show();
                }
            }
        });
    }


    @Override
    public void callback(String key, Object value) {
        if (key.equals("nfcname")) {
            //点击验证密码确定框
            JsonObject obj = (JsonObject) value;
            String name = OJsonGet.getString(obj, OToastInput.OTHER_TEXT);
            isOpretion=false;
            OCtrlCar.getInstance().ccmd2103_changeNfc(ManagerCarList.getInstance().getCurrentCarID(), getCardFromPosition(), name);
        }
    }

    private String getCardFromPosition() {
        String cardNum = "";
        switch (cachePosition) {
            case 0:
                cardNum = "slotOne";
                break;
            case 1:
                cardNum = "slotTwo";
                break;
        }
        return cardNum;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        switch (eventName) {
            case OEventName.QURRY_REMOTE_CONTROL_RESULT:
                //是否重新获取失败的时候
                String isRegetRemoteControl = (String) paramObj;
                switch (isRegetRemoteControl){
                    case "1":
                        //获取成功的时候 显示listview隐藏text
                        handleChangeData();
                        showListOrText(1);
                        break;
                    case "2":
                        //重新获取失败的时候
                        showProgress();
                        break;
                    case "3":
                        //获取失败的时候 显示textview隐藏listview
                        showListOrText(2);
                        break;
                }
                break;
            case OEventName.ADD_OR_DELETE_REMOTE_CONTROL:
                boolean isSuccess = (Boolean) paramObj;
                if (!isSuccess) {
                    if (BuildConfig.DEBUG) Log.e("转圈", "添加删除协议失败停止GIF提示操作失败");
                    StopGIF();
                    showToast(2);
                }
                break;
            case OEventName.TEM_CONTROL_SOCKET_RESULT_BACK:
                getCardMessageBack = true;
                readCardMessageBack = true;
                StopGIF();
                if (BuildConfig.DEBUG) Log.e("转圈", "收到推送结束转圈");
                stopLoadding();
                if(isOpretion){
                    showToast(1);
                }

                if (BuildConfig.DEBUG) Log.e("转圈", "添加删除协议成功返回数据停止GIF提示操作成功");
                break;
        }
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
        btn_re_get.setOnClickListener(new OnClickListenerMy5000() {
            @Override
            public void onClickNoFast(View view) {
                getCardMessageBack = false;
                isOpretion=false;
                OCtrlCar.getInstance().ccmd2105_RemoteControl(ManagerCarList.getInstance().getCurrentCarID(),true);
            }
        });
        zhanweiview.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {

            }
        });
        nokey_btn.img_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //当前开发关
                String option;
                String isOpen=ManagerRemoteControl.getInstance().getIsOpen();
               if(isOpen.equals("1")){
                   option="0";
                   nokey_btn.setRightImg(R.drawable.switch_off);
               }else{
                   option="1";
                   nokey_btn.setRightImg(R.drawable.switch_on);
               }
                OCtrlCar.getInstance().ccmd2108_shushijinru(ManagerCarList.getInstance().getCurrentCarID(),"05",option);
            }
        });
        btn_learn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加卡
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withTitle("学习遥控器方法")
                        .withInfo("请携带需学习的遥控器靠近车辆，点击【确定】后，每个需要学习的遥控器分别按一次任意键进行学习，最后一个需要学习的遥控器多按几次任意键，中控锁有动作则学习成功。")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if (isClickConfirm ) {
                                    //写卡
                                    isOpretion=true;
                                    OCtrlCar.getInstance().ccmd2107_oldLearn_RemoteControl(ManagerCarList.getInstance().getCurrentCarID());
                                    tem_read_view.setVisibility(View.VISIBLE);
                                    zhanweiview.setVisibility(View.VISIBLE);
                                    RequestOptions options = new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                                    Glide.with(getContext()).load(R.drawable.img_tem_connecting).apply(options).into(gif_tem);
                                    if (BuildConfig.DEBUG) Log.e("转圈", "开始写卡");
                                    readCardMessageBack = false;
                                    myHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!readCardMessageBack) {
                                                if (BuildConfig.DEBUG) Log.e("转圈", "15秒之后没收到消息提示写卡操作失败");
//                                              //多少秒消失
//                                                showToast(2);
                                            }
                                            if (BuildConfig.DEBUG) Log.e("转圈", "15秒之后停止写卡GIF");
                                            tem_read_view.setVisibility(View.INVISIBLE);
                                            zhanweiview. setVisibility(View.INVISIBLE);
                                        }
                                    }, 15000);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void invalidateUI() {
        if ( ManagerRemoteControl.getInstance().getTemControlDataForView() != null) {
            data = ManagerRemoteControl.getInstance().getTemControlDataForView();
            temControlAdapter.setData(data);
            temControlAdapter.notifyDataSetChanged();
        }
        if(!TextUtils.isEmpty(ManagerRemoteControl.getInstance().getIsOpen())){
            String isOpen=ManagerRemoteControl.getInstance().getIsOpen();
            if(isOpen.equals("1")){
                nokey_btn.setRightImg(R.drawable.switch_on);
            }else{
                nokey_btn.setRightImg(R.drawable.switch_off);
            }
        }
    }

    private void StopGIF() {
        Message message = Message.obtain();
        message.what = 11111;
        myHandler.sendMessage(message);
    }

    private void showToast(int arg1) {
        Message message = Message.obtain();
        message.what = 11112;
        message.arg1 = arg1;
        myHandler.sendMessage(message);
    }

    private void showProgress() {
        Message message = Message.obtain();
        message.what = 11113;
        myHandler.sendMessage(message);
    }
    private void showListOrText(int whichCondition){
        Message message = Message.obtain();
        message.what = 11114;
        message.arg1=whichCondition;
        myHandler.sendMessage(message);
    }
    private void stopLoadding(){
        Message message = Message.obtain();
        message.what = 11115;
        myHandler.sendMessage(message);
    }
}
