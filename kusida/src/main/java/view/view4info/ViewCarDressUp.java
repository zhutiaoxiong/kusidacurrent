package view.view4info;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import adapter.AdapterSkin;
import ctrl.OCtrlInformation;
import model.ManagerInformation;
import model.information.DataSkin;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/9/2.
 */
public class ViewCarDressUp extends LinearLayoutBase {
    private ClipTitleMeSet titleHead;
    private ListView list_skins;
    private AdapterSkin   adapter;
    private ImageView     img_bought;
    private boolean alreadyLoad = false;

    public ViewCarDressUp(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_find_car_dressup,this,true);
        titleHead=(ClipTitleMeSet) findViewById(R.id.title_head);
        list_skins=(ListView)findViewById(R.id.list_skins);
        img_bought=(ImageView)findViewById(R.id.img_bought);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.INFORMATION_SKINLIST_RESULTBACK,this);
        ODispatcher.addEventListener(OEventName.SKIN_URL_RESULTBACK,this);
    }

    @Override
    protected void initViews() {
        //先加载本地的，防卡慢
        List<DataSkin> skinList = ManagerInformation.getInstance().skinList;
        if(skinList!=null){
            if(skinList.size()>0){
                handleChangeData();
                alreadyLoad = true;
            }
        }
        OCtrlInformation.getInstance().ccmd1401_getSkinList();
    }

    @Override
    protected void initEvents() {
        titleHead.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.activity_kulala_main);
            }
        });
        titleHead.img_right.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ViewDownLoadManager.setIsBoughtView(false);
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_find_downloadmanager);
            }
        });
        img_bought.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ViewDownLoadManager.setIsBoughtView(true);
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_find_downloadmanager);
            }
        });
//        list_skins.setOnItemClickListener(new OnItemClickListenerMy(){
//            @Override
//            public void onItemClickNofast(AdapterView<?> adapterView, View view, int i, long l) {
//            }
//        });
    }


    public void receiveEvent(String key, Object paramObj) {
        if (key.equals(OEventName.INFORMATION_SKINLIST_RESULTBACK)) {
            if(!alreadyLoad) {
                handleChangeData();
            }
        }else if (key.equals(OEventName.SKIN_URL_RESULTBACK)) {
            String url = (String)paramObj;
            adapter.startDownLoad(OCtrlInformation.getInstance().skinId1402,url);
        }
    }
    @Override
    protected void invalidateUI() {
        List<DataSkin> skinList = ManagerInformation.getInstance().skinList;
        if(skinList!=null){
            adapter = new AdapterSkin(getContext(),skinList);
            list_skins.setAdapter(adapter);
        }
    }
}
