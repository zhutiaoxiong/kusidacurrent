package view.home.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.to.aboomy.pager2banner.Banner;
import com.to.aboomy.pager2banner.IndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import common.pinyinzhuanhuan.CircleImg;
import ctrl.OCtrlRegLogin;
import model.ManagerLoginReg;
import model.loginreg.DataUser;
import view.ActivityKulalaMain;
import view.home.commonview.DeviceAddItem;
import view.home.commonview.DeviceItem;
import view.home.adapter.ImageAdapter;
import view.home.activity.ActivityLocator;

/**
 * @author Evan_zch
 * @date 2018/8/23 20:40
 */
public class MainFragment extends MyBaseFragment implements OEventObject {
    private Banner banner;
    private CircleImg img_face;
    private TextView user_name,phone_number,no_info;
    private DeviceItem item1,item2;
    private DeviceAddItem item3;
    private static final String TAG="MainFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: " );
        banner = view.findViewById(R.id.banner);
        user_name = view.findViewById(R.id.user_name);
        phone_number = view.findViewById(R.id.phone_number);
        no_info = view.findViewById(R.id.no_info);
        img_face = view.findViewById(R.id.img_face);
        item1 = view.findViewById(R.id.item1);
        item2 = view.findViewById(R.id.item2);
        item3 = view.findViewById(R.id.item3);
        initView();
        initEvent();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setBanerState(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, " onResume" );
        setBanerState(false);
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, " onStop" );
        setBanerState(true);
    }

    private void setBanerState(boolean isHiden){
        if(banner!=null){
            if(isHiden){
                banner.setAutoPlay(false);
            }else{
                if(!banner.isAutoPlay()){
                    banner.setAutoPlay(true);
                }
            }
        }
    }

    private void initView(){
        ODispatcher.addEventListener(OEventName.CHANGE_USER_INFO_OK, this);
        setBanneer();
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("mainSelect");
        int count1=0;//设备数量通过服务器获取
        int count2=0;//设备数量通过服务器获取
        if(TextUtils.isEmpty(result)){
            item1.setTextAndBackGround(false,0);
            item2.setTextAndBackGround(false,0);
        }else if(result.equals("0")){
            item1.setTextAndBackGround(true,count1);
            item2.setTextAndBackGround(false,count2);
        }else if(result.equals("1")){
            item1.setTextAndBackGround(false,count1);
            item2.setTextAndBackGround(true,count2);
        }
        //获取头像手机号等信息
        OCtrlRegLogin.getInstance().ccmd1126_getUserInfo();
    }
    private void initEvent(){
        item1.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("mainSelect","0");
                item1.setBackGround(true);
                item2.setBackGround(false);
                Log.e(TAG, "onClick item1 tv1" );
            }
        });
        item2.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item1.setBackGround(false);
                item2.setBackGround(true);
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("mainSelect","1");
                Log.e(TAG, "onClick item2 tv1" );
            }
        });
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick item1" );
                Intent intent=new Intent(getActivity(), ActivityKulalaMain.class);
                startActivity(intent);
            }
        });
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick item2" );
                Intent intent=new Intent(getActivity(), ActivityLocator.class);
                startActivity(intent);
            }
        });
        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick item3" );
            }
        });
    }
    private void setBanneer(){
        //  .setIndicatorSelectedRatio(3)
        final IndicatorView indicatorView = new IndicatorView(getContext())
                .setIndicatorRatio(1f)
                .setIndicatorRadius(2f)
                .setIndicatorSelectedRadius(2f)
                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_CIRCLE)
                .setIndicatorColor(Color.GRAY)
                .setIndicatorSelectorColor(Color.WHITE);

        banner.setAutoPlay(true)
                .setIndicator(indicatorView)
                .setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
                .setPagerScrollDuration(800)
                .setPageMargin(SizeUtils.dp2px(40), 0)
//                .addPageTransformer(new ScaleInTransformer())
                .setOuterPageChangeListener(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        Log.e("aa", "onPageSelected position " + position);
                    }
                });
        ImageAdapter adapter = new ImageAdapter(getImage(4),getContext());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e(TAG, "onClick BANNER"+position );
            }
        });
        banner.setAdapter(adapter);
    }
    public static final int[] IMAGES = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
    };

    public static List<Integer> getImage(int count) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(IMAGES[i]);
        }
        return list;
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.CHANGE_USER_INFO_OK)){
            Log.e(TAG, "receiveEvent: " );
            sendChangeUserInfoUI();
        }
    }
    private void sendChangeUserInfoUI(){
        if(handler!=null){
            handler.sendEmptyMessage(1);
        }
    }
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                setUserInfoUI();
            }
        }
    };
    private void setUserInfoUI(){
        user_name.setVisibility(View.INVISIBLE);
        phone_number.setVisibility(View.INVISIBLE);
        no_info.setVisibility(View.INVISIBLE);
        final DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null||TextUtils.isEmpty(user.name)) {
            no_info.setVisibility(View.VISIBLE);
        }else{
            user_name.setVisibility(View.VISIBLE);
            phone_number.setVisibility(View.VISIBLE);
            user_name.setText(user.name);
            if(user.avatarUrl != null && user.avatarUrl.length() > 0){
                RequestOptions options = new RequestOptions();
                if(getActivity()!=null){
                    Glide.with(this)
                            .load(user.avatarUrl)
                            .apply(options)
                            .into(img_face);
                }
            }else{
                img_face.setImageResource(R.drawable.head_no);
            }
        }
    }
}
