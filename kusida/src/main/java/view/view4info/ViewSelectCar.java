package view.view4info;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterForSelectCar;
import ctrl.OCtrlInformation;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.view4me.set.ClipTitleMeSet;

/**
 * 装饰选择车型页面
 */

/**
 * Created by qq522414074 on 2016/9/1.
 */
public class ViewSelectCar extends LinearLayoutBase {
    private ClipTitleMeSet titleHead;
    private ListView carListView;
    private AdapterForSelectCar adapter;
    private Button submmit;
    public static int type=0;//是设置皮肤还是背景1车辆皮肤2车辆背景

    public static int useSkinId;
    private static int fromLayout;
    public ViewSelectCar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_find_select_car, this, true);
        titleHead = (ClipTitleMeSet) findViewById(R.id.title_head);
        carListView = (ListView) findViewById(R.id.view_find_selectcar_listview);
        submmit = (Button) findViewById(R.id.view_find_selectcat_submmit);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GET_CARDRESSUP_RESULTBACK,this);
    }
    public static void setDefaultData(int useSkinId0,int fromLayout0){
        fromLayout = fromLayout0;
        useSkinId = useSkinId0;
    }

    @Override
    protected void initViews() {
        handleChangeData();
    }

    @Override
    protected void initEvents() {
        titleHead.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                //从哪个页面进来跳到哪个页面
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, fromLayout);
            }
        });
        submmit.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                    List<DataCarInfo> list = ManagerCarList.getInstance().getCarInfoList();
                    if(list==null)return;
                    List<Long> list1=new ArrayList<Long>();
                    for(int i=0;i<list.size();i++){
                        if(list.get(i).skinSelect){
                            list1.add(list.get(i).ide) ;
                        }
                    }
                    Long[] result = list1.toArray(new Long[list1.size()]);
                    if(result.length == 0){
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.did_not_choose_the_vehicle));
                    }else {
                        OCtrlInformation.getInstance().ccmd1403_submit_select_car(result, useSkinId,type);
                    }
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if(eventName.equals(OEventName.GET_CARDRESSUP_RESULTBACK)){
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.set_up_the_success));
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_find_car_dressup);
        }
        super.receiveEvent(eventName, paramObj);
    }

    @Override
    protected void invalidateUI() {
        List<DataCarInfo> list = ManagerCarList.getInstance().getCarInfoList();
        if (list == null) {
            carListView.setAdapter(null);
            return;
        }
        if (adapter == null) {
            adapter = new AdapterForSelectCar(list,useSkinId,getContext());
            carListView.setAdapter(adapter);
        } else {
            adapter.changeData(list);
            carListView.invalidate();
        }
    }
}
