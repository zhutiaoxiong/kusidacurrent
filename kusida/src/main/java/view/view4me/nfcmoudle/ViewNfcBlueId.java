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
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.toast.ToastConfirmNormal;

import java.util.ArrayList;
import java.util.List;

import adapter.RecycleViewDivider;
import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import model.BlueInstructionCollection;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.EquipmentManager;
import view.view4me.set.ClipTitleMeSet;

import static com.kulala.dispatcher.OEventName.MINI_NFC_RESULT_BACK;

public class ViewNfcBlueId extends RelativeLayoutBase {
    private final ClipTitleMeSet title_head;
    private final RecyclerView recycleview_nfc;
    private ViewNFCRecycleViewAdapter nfcAdapter;
    private List<NfcData> data;
    private final ImageView gif_nfc;
    private final LinearLayout nfc_read_view;
    private int cachePosition;
    private final Button btn_re_get;
    private boolean readCardMessageBack;
    private boolean isOpretion;//是否添加删除卡片操作
    private final View zhanweiview;

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
            }
        }
    };

    public ViewNfcBlueId(Context context, AttributeSet attrs) {
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
        ODispatcher.addEventListener(MINI_NFC_RESULT_BACK, this);
    }

    @Override
    protected void initViews() {
        data = new ArrayList<>();
        data= ManagerNfcBlue.getInstance().getListNfcId();
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
            nfcAdapter = new ViewNFCRecycleViewAdapter(data, getContext(),3);
            recycleview_nfc.setAdapter(nfcAdapter);
        } else {
            nfcAdapter.setData(data);
            nfcAdapter.notifyDataSetChanged();
        }
        nfcAdapter.setOnItemClickListener(new ViewNFCRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void centerTxtClick(View view, int position) {
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
                                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.addIdCard(cachePosition,1)),false);
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
                                        BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.deleteCard(cachePosition,1)),false);
                                    }
                                }
                            })
                            .show();
                }
            }
        });
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
            case MINI_NFC_RESULT_BACK:
                readCardMessageBack = true;
                if(isOpretion){
                    showToast(1);
                }
                StopGIF();
                handleChangeData();
                break;
        }
    }

    @Override
    protected void initEvents() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                if(EquipmentManager.isMini()||(EquipmentManager.isMinJiaQiang()&&EquipmentManager.isMinNoMozu())||EquipmentManager.isShouweiSix()){
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_nfc_blue);
                }else{
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_nfc);
                }
            }
        });
        btn_re_get.setOnClickListener(new OnClickListenerMy5000() {
            @Override
            public void onClickNoFast(View view) {
                isOpretion=false;
                BlueLinkReceiver.getInstance().sendMessage(ByteHelper.bytesToHexString(BlueInstructionCollection.askCardInfo()),false);
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
        if (ManagerNfcBlue.getInstance().getListNfcId()!= null) {
            data = ManagerNfcBlue.getInstance().getListNfcId();
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
}
