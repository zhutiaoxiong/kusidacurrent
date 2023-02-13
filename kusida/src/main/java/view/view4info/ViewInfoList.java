package view.view4info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.BuildConfig;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsfunc.static_view_change.ORecycle;
import java.util.List;

import adapter.AdapterInformation;
import ctrl.OCtrlInformation;
import model.ManagerInformation;
import model.information.DataBanner;
import model.information.DataInformation;
import view.ActivityWeb;
import com.kulala.staticsview.titlehead.ClipTitleHead;
import view.clip.child.ClipMovingPic;

public class ViewInfoList extends RelativeLayoutBase{
    private ClipTitleHead      title_head;
    private ListView           list_info;
    private ClipMovingPic      move_pic;
    private AdapterInformation adapter;

    public ViewInfoList(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_info_infolist, this, true);
        title_head = (ClipTitleHead) findViewById(R.id.title_head);
        list_info = (ListView) findViewById(R.id.list_info);
        move_pic = new ClipMovingPic(context,null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, ODipToPx.dipToPx(context,150f));
        move_pic.setLayoutParams(params);
        list_info.addHeaderView(move_pic);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.INFORMATION_LIST_RESULTBACK, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
    @Override
    protected void onDetachedFromWindow() {
        ORecycle.recycleAbsList(list_info, R.id.img_left);
        list_info.setAdapter(null);
        adapter = null;
        super.onDetachedFromWindow();
    }

    public void initViews() {
        OCtrlInformation.getInstance().ccmd1308_getInfoList(0, 50);
//        handleChangeData();
    }

    public void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
                super.onClickNoFast(v);
            }
        });
        list_info.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if(position<=0)return;//加了head position+1
                try {
                    String jumpUrl = adapter.getItem(position-1).jumpUrl;
                    if (jumpUrl == null || jumpUrl.length() == 0)return;// 无跳转
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(ActivityWeb.TITLE_NAME, "发现");
                    bundle.putString(ActivityWeb.HTTP_ADDRESS, jumpUrl);
                    intent.putExtras(bundle);
                    intent.setClass(getContext(), ActivityWeb.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }catch (Exception e){
                     if (BuildConfig.DEBUG) Log.e("ViewInfoList","out of array bound");
                }
            }
        });
    }

    public void receiveEvent(String key, Object paramObj) {
        if (key.equals(OEventName.INFORMATION_LIST_RESULTBACK)) {
            handleChangeData();
        }
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }


    @Override
    public void invalidateUI() {
        List<DataInformation> list = ManagerInformation.getInstance().infomationList;
        List<DataBanner> bannerList = ManagerInformation.getInstance().bannerList;
        if (list == null || list.size() == 0 || bannerList == null || bannerList.size() == 0) {
            OCtrlInformation.getInstance().ccmd1308_getInfoList(0, 20);
            return;
        }
        if (list == null) {
            list_info.setAdapter(null);
            return;
        }
        adapter = new AdapterInformation(getContext(), list, R.layout.list_item_information);
        list_info.setAdapter(adapter);
        if (bannerList == null || bannerList.size() == 0) {
            if(move_pic != null)move_pic.setVisibility(View.GONE);
        } else {
            if(move_pic != null){
                if(move_pic.getIsInstant()){
                    move_pic.invalidateViewPager();
                }else {
                    move_pic.setDataPic(bannerList);
                }
            }
        }
    }
    // ==============================================================
}
