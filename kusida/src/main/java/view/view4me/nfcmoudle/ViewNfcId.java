package view.view4me.nfcmoudle;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import model.nfc.ManagerNfc;
import view.clip.ClipPopLoading;
import view.view4me.set.ClipTitleMeSet;

public class ViewNfcId extends RelativeLayoutBase {
    private final ClipTitleMeSet title_head;
    private final RecyclerView recycleview_nfc;
    private ViewNFCRecycleViewAdapter nfcAdapter;
    private List<NfcData> data;
    private final ImageView gif_nfc;
    private final LinearLayout nfc_read_view;
    private int cachePosition;
    private final Button btn_re_get;
    private boolean getCardMessageBack;
    private boolean readCardMessageBack;
    private boolean isOpretion;//是否添加删除卡片操作
    private View zhanweiview;

    private final Handler myHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 11111) {
                nfc_read_view.setVisibility(View.INVISIBLE);
                zhanweiview.setVisibility(View.INVISIBLE);
            } else if (msg.what == 11112) switch (msg.arg1) {
                case 1:
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "操作成功");
                    break;
                case 2:
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "操作失败");
                    break;
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
                            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "获取失败");
                        }
                    }
                }, 5000);
            }else if(msg.what==11115){
                ClipPopLoading.getInstance().stopLoading();
            }
        }
    };

    public ViewNfcId(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_nfc_id, this, true);
        title_head = findViewById(R.id.title_head);
        recycleview_nfc = findViewById(R.id.recycleview_nfc);
        gif_nfc = findViewById(R.id.gif_nfc);
        nfc_read_view = findViewById(R.id.nfc_read_view);
        btn_re_get = findViewById(R.id.btn_re_get);
        zhanweiview= findViewById(R.id.zhanweiview);

        initViews();
        initEvents();
        OCtrlCar.getInstance().ccmd2101_queryNfc(ManagerCarList.getInstance().getCurrentCarID());
        ODispatcher.addEventListener(OEventName.QUERY_NFC_DATA_BACK, this);
        ODispatcher.addEventListener(OEventName.ADD_OR_DELETE_NFC_DATA_BACK, this);
        ODispatcher.addEventListener(OEventName.CHANGE_NFC_DATA_BACK, this);
        ODispatcher.addEventListener(OEventName.READ_CARD_SUCCESS, this);
        ODispatcher.addEventListener(OEventName.REGET_NFC_RESULT_BACK, this);
    }

    @Override
    protected void initViews() {
        data = new ArrayList<>();
        List<NfcData> myList=ManagerNfc.getInstance().createinitIDList();
        data=myList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        recycleview_nfc.setLayoutManager(mLinearLayoutManager);
        RecycleViewDivider driver = new RecycleViewDivider(
                getContext(), LinearLayoutManager.HORIZONTAL,  ODipToPx.dipToPx(getContext(),15), Color.parseColor("#ffffff"));
        recycleview_nfc.setHasFixedSize(true);
        recycleview_nfc.setItemAnimator(new DefaultItemAnimator());
        if (recycleview_nfc.getItemDecorationCount() == 0) {
            recycleview_nfc.addItemDecoration(driver);
        }
        if (nfcAdapter == null) {
            nfcAdapter = new ViewNFCRecycleViewAdapter(data, getContext(),1);
            recycleview_nfc.setAdapter(nfcAdapter);
        } else {
            nfcAdapter.setData(data);
            nfcAdapter.notifyDataSetChanged();
        }
        nfcAdapter.setOnItemClickListener(new ViewNFCRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void centerTxtClick(View view, int position) {
                cachePosition = position;
                OToastInput.getInstance().showInput(title_head, "", "编辑智卡名称:", new String[]{OToastInput.OTHER_TEXT}, "nfcname", ViewNfcId.this);
            }
            @Override
            public void rightTxtClick(View view, int position) {
                cachePosition = position;
                if (data.get(position).status.equals("0")) {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("提示")
                            .withInfo("确定添加智卡吗？")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm ) {
                                        //写卡
                                        isOpretion=true;
                                        OCtrlCar.getInstance().ccmd2102_addOrDeleteNfc(ManagerCarList.getInstance().getCurrentCarID(), getCardFromPosition(), "05");
                                        nfc_read_view.setVisibility(View.VISIBLE);
                                        zhanweiview.setVisibility(View.VISIBLE);
                                        RequestOptions options = new RequestOptions()
                                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                                        Glide.with(getContext()).load(R.drawable.img_nfc_test).apply(options).into(gif_nfc);
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
                                                nfc_read_view.setVisibility(View.INVISIBLE);
                                                zhanweiview .setVisibility(View.INVISIBLE);
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
                            .withInfo("确定要删除此卡吗？")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm ) {
                                        isOpretion=true;
                                        OCtrlCar.getInstance().ccmd2102_addOrDeleteNfc(ManagerCarList.getInstance().getCurrentCarID(), getCardFromPosition(), "04");
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
                cardNum = "cardSix";
                break;
            case 1:
                cardNum = "cardSeven";
                break;
            case 2:
                cardNum = "cardEight";
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
            case OEventName.QUERY_NFC_DATA_BACK:
            case OEventName.CHANGE_NFC_DATA_BACK:
                handleChangeData();
                break;
            case OEventName.ADD_OR_DELETE_NFC_DATA_BACK:
                boolean isSuccess = (Boolean) paramObj;
                if (!isSuccess) {
                    if (BuildConfig.DEBUG) Log.e("转圈", "添加删除协议失败停止GIF提示操作失败");
                    StopGIF();
                    showToast(2);
                }
                break;
            case OEventName.READ_CARD_SUCCESS:
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
            case OEventName.REGET_NFC_RESULT_BACK:
                boolean isReGetSuccess = (Boolean) paramObj;
                if (isReGetSuccess) {
                    handleChangeData();
                } else {
                    showProgress();
                }
                break;
        }
    }

    @Override
    protected void initEvents() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_nfc);
            }
        });
        btn_re_get.setOnClickListener(new OnClickListenerMy5000() {
            @Override
            public void onClickNoFast(View view) {
                getCardMessageBack = false;
                isOpretion=false;
                OCtrlCar.getInstance().ccmd2104_reGetNfc(ManagerCarList.getInstance().getCurrentCarID());
            }
        });
        zhanweiview.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {

            }
        });
    }

    @Override
    protected void invalidateUI() {
        if (ManagerNfc.getInstance().getNfcDataForIdView() != null) {
            data = ManagerNfc.getInstance().getNfcDataForIdView();
            nfcAdapter.setData(data);
            nfcAdapter.notifyDataSetChanged();
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
    private void stopLoadding(){
        Message message = Message.obtain();
        message.what = 11115;
        myHandler.sendMessage(message);
    }
}
