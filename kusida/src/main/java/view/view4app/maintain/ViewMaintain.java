package view.view4app.maintain;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;
import com.kulala.staticsview.toast.OToastButton;

import java.util.List;

import adapter.AdapterForCarList;
import adapter.AdapterForMaintain;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.maintain.DataMaintain;
import model.maintain.ManagerMaintainList;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/10/31.
 */
public class ViewMaintain extends LinearLayoutBase {
    private ClipTitleMeSet titleHead;
    private ViewFirstIn firstin;
    private ListView listView, listView1;
    private AdapterForMaintain adapter;
    private AdapterForCarList carListAdapter;
    private List<DataCarInfo> carInfoList;
    private List<DataMaintain> list;
    public static DataCarInfo selectedCar;
    public static DataMaintain maintain;
    private ImageView img_dropdown;
    private SmartImageView car_pic;
    private TextView car_brand;
    private boolean isClick = true;
    private RelativeLayout layout_nocontent;
    private View bg_view;

    public ViewMaintain(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_maintance_remind_me, this, true);
        titleHead = (ClipTitleMeSet) findViewById(R.id.title_head);
        firstin = (ViewFirstIn) findViewById(R.id.first_maintain);
        listView = (ListView) findViewById(R.id.listview);
        listView1 = (ListView) findViewById(R.id.listview1);
        img_dropdown = (ImageView) findViewById(R.id.img_dropdown);
        car_pic = (SmartImageView) findViewById(R.id.car_pic);
        car_brand = (TextView) findViewById(R.id.car_brand);
        layout_nocontent = (RelativeLayout) findViewById(R.id.layout_nocontent);
        bg_view = findViewById(R.id.bg_view);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.MAINTAIN_DELETE, this);
        ODispatcher.addEventListener(OEventName.MAINTAIN_COMPELETE, this);
        ODispatcher.addEventListener(OEventName.GET_MAINTAINLIST_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.SHANCHU_BAOYANG, this);
        ODispatcher.addEventListener(OEventName.BAOYANG_WANCHNEG, this);
//        ODispatcher.addEventListener(OEventName.I_KNOW, this);
//        ODispatcher.addEventListener(OEventName.WAKE_UP_ME_LATE, this);
//        ODispatcher.addEventListener(OEventName.ALREADY_MAINTAIN, this);
    }

    @Override
    protected void initViews() {
        handleChangeData();
        if (ManagerCarList.getInstance().getCurrentCar().isActive == 1) {
            OCtrlCar.getInstance().ccmd1227_CarMaintain(ManagerCarList.getInstance().getCurrentCar().ide, 0, 20);
        }
    }

    @Override
    protected void initEvents() {
        titleHead.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        OnClickListenerMy clickdropdown = new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (isClick) {
                    listView1.setVisibility(View.VISIBLE);
                    bg_view.setVisibility(View.VISIBLE);
                    showListView();
                    isClick = false;
                } else {
                    listView1.setVisibility(View.INVISIBLE);
                    bg_view.setVisibility(View.INVISIBLE);
                    isClick = true;
                }
            }
        };
        img_dropdown.setOnClickListener(clickdropdown);
        car_pic.setOnClickListener(clickdropdown);
        car_brand.setOnClickListener(clickdropdown);
        listView1.setOnItemClickListener(new OnItemClickListenerMy() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView1.setVisibility(View.INVISIBLE);
                carListAdapter.setCurrentItem(i);
                selectedCar = carListAdapter.getCurrentItem();
                long ide=selectedCar.ide;
                ManagerCarList.getInstance().setCurrentCar(selectedCar.ide);
                   OCtrlCar.getInstance().ccmd1227_CarMaintain(ManagerCarList.getInstance().getCurrentCar().ide, 0, 20);
            }
        });
        titleHead.img_right.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_add_maintain);
            }
        });
        listView.setOnItemClickListener(new OnItemClickListenerMy() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setCurrentItem(i);
                maintain = adapter.getCurrentItem();
                if (maintain.status == 0) {
                    OToastButton.getInstance().show(titleHead, new String[]{getResources().getString(R.string.complete_maintenance), getResources().getString(R.string.delete_records)}, "completeOrDelete", ViewMaintain.this);
                } else {
                    OToastButton.getInstance().show(titleHead, new String[]{getResources().getString(R.string.delete_records)}, "delete", ViewMaintain.this);
                }
            }
        });
    }

    @Override
    public void callback(String s, Object o) {
        if (s.equals("delete")) {
            handleShowDeletePromeBox();
//            OCtrlCar.getInstance().ccmd1229_CarMaintainDeleteModification(maintain.ide);
        } else if (s.equals("completeOrDelete")) {
            String haha = (String) o;
            if (haha.equals(getResources().getString(R.string.complete_maintenance))) {
                handleShowCompeletePromeBox();
//                OCtrlCar.getInstance().ccmd1231_CarMaintainCompleteModification(maintain.ide);
            } else if (haha.equals(getResources().getString(R.string.delete_records))) {
                handleShowDeletePromeBox();
//                OCtrlCar.getInstance().ccmd1229_CarMaintainDeleteModification(maintain.ide);
            }
        }
        super.callback(s, o);
    }

    private void handleShowDeletePromeBox() {
        Message message = Message.obtain();
        message.what = 110;
        handler.sendMessage(message);
    }

    private void handleShowCompeletePromeBox() {
        Message message = Message.obtain();
        message.what = 111;
        handler.sendMessage(message);
    }

    private void showListView() {
        if (carListAdapter == null) {
            carInfoList = ManagerCarList.getInstance().getCarInfoList();
            carListAdapter = new AdapterForCarList(ViewMaintain.this.getContext(), carInfoList);
            listView1.setAdapter(carListAdapter);
        } else {
            carListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.GET_MAINTAINLIST_RESULTBACK)) {
            handleChangeData();
        } else if (s.equals(OEventName.MAINTAIN_COMPELETE)) {
            handleChangeData();
        } else if (s.equals(OEventName.MAINTAIN_DELETE)) {
            handleChangeData();
        } else if (s.equals(OEventName.BAOYANG_WANCHNEG)) {
            OCtrlCar.getInstance().ccmd1231_CarMaintainCompleteModification(maintain.ide);
        } else if (s.equals(OEventName.SHANCHU_BAOYANG)) {
            OCtrlCar.getInstance().ccmd1229_CarMaintainDeleteModification(maintain.ide);
        }
//        else if (s.equals(OEventName.I_KNOW)) {
//            OCtrlCar.getInstance().ccmd1230_CarMaintainConfirmmineModification(maintain.ide, 1);
//        } else if (s.equals(OEventName.WAKE_UP_ME_LATE)) {
//            OCtrlCar.getInstance().ccmd1230_CarMaintainConfirmmineModification(maintain.ide, 2);
//        } else if (s.equals(OEventName.ALREADY_MAINTAIN)) {
//            OCtrlCar.getInstance().ccmd1230_CarMaintainConfirmmineModification(maintain.ide, 3);
//        }

    }

    @Override
    protected void invalidateUI() {
        if(isClick){
            isClick=false;
        }else{
            isClick=true;
        }
        int hasMaintance = ManagerMaintainList.getInstance().gethasMaintance();
        layout_nocontent.setVisibility(View.INVISIBLE);
        firstin.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        titleHead.img_right.setVisibility(View.INVISIBLE);
        img_dropdown.setVisibility(View.INVISIBLE);
        car_pic.setVisibility(View.INVISIBLE);
        car_brand.setVisibility(View.INVISIBLE);
        bg_view.setVisibility(View.INVISIBLE);
        if (hasMaintance == 0) {
            firstin.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            titleHead.img_right.setVisibility(View.VISIBLE);
            titleHead.img_right.setImageResource(R.drawable.add_maintain);
            img_dropdown.setVisibility(View.VISIBLE);
            car_pic.setVisibility(View.VISIBLE);
            car_brand.setVisibility(View.VISIBLE);
            car_pic.setImageUrl(ManagerCarList.getInstance().getCurrentCar().logo);
            car_brand.setText(ManagerCarList.getInstance().getCurrentCar().num);
            ManagerMaintainList.getInstance().loadMaintainListLocal();
            list = ManagerMaintainList.getInstance().maintenanceInfos;
            if(list==null||list.size()==0){
                layout_nocontent.setVisibility(View.VISIBLE);
            }
            if (adapter == null) {
                adapter = new AdapterForMaintain(ViewMaintain.this.getContext(), list);
                listView.setAdapter(adapter);
            } else {
                adapter.changeData(list);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.MAINTAIN_DELETE, this);
        ODispatcher.removeEventListener(OEventName.MAINTAIN_COMPELETE, this);
        ODispatcher.removeEventListener(OEventName.GET_MAINTAINLIST_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.SHANCHU_BAOYANG, this);
        ODispatcher.removeEventListener(OEventName.BAOYANG_WANCHNEG, this);
//        ODispatcher.removeEventListener(OEventName.I_KNOW, this);
//        ODispatcher.removeEventListener(OEventName.WAKE_UP_ME_LATE, this);
//        ODispatcher.removeEventListener(OEventName.ALREADY_MAINTAIN, this);
        super.onDetachedFromWindow();
    }


    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 110) {
                MaintainPromeBox.getInstance().show(titleHead, new MaintainPromeBoxData(getResources().getString(R.string.the_tip), getResources().getString(R.string.sure_to_delete_the_content_of_the_maintenance_set), 4));
            } else if (msg.what == 111) {
                MaintainPromeBox.getInstance().show(titleHead, new MaintainPromeBoxData(getResources().getString(R.string.the_tip), getResources().getString(R.string.sure_to_compelete_the_content_of_the_maintenance_set), 5));
            }
        }
    };
}
