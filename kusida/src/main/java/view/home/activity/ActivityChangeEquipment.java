package view.home.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import java.util.ArrayList;
import java.util.List;

import adapter.RecycleViewDivider;
import model.locator.ChangEquipmentBean;
import view.home.adapter.ChanEquipmentAdapter;
import view.home.commonview.CommonCenterItem;
import view.home.commonview.CommonTopItem;

public class ActivityChangeEquipment extends AllActivity {
    private CommonTopItem top_item;
    private CommonCenterItem center_item;
    private RecyclerView recyclerView;
    private ChanEquipmentAdapter adapter;
    private RecycleViewDivider driver;
    private static final String TAG="LocatorListFragment";
    List<ChangEquipmentBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_equipment);
        top_item =  findViewById(R.id.top_item);
        center_item =findViewById(R.id.center_item);
        recyclerView =findViewById(R.id.recycleview);
        initView();
        initEvent();
    }
    private void initEvent(){
        top_item.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick back" );
                finish();
            }
        });
        top_item.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick select  search" );
            }
        });
        top_item.scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick scan  " );
            }
        });

        center_item.all_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick all_device  " );
                setCenterItemUI(0);
            }
        });

        center_item.online_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick online_device  " );
                setCenterItemUI(1);
            }
        });

        center_item.offline_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick offline_device  " );
                setCenterItemUI(2);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e(TAG, "onClick item " +position);
            }
        });
    }
    private void setCenterItemUI(int item){
        center_item.all_device.setBackgroundColor(Color.parseColor("#ffffff"));
        center_item.all_device.setTextColor(Color.parseColor("#000000"));
        center_item.online_device.setBackgroundColor(Color.parseColor("#ffffff"));
        center_item.online_device.setTextColor(Color.parseColor("#000000"));
        center_item.offline_device.setBackgroundColor(Color.parseColor("#ffffff"));
        center_item.offline_device.setTextColor(Color.parseColor("#000000"));
        switch (item){
            case 0:
                center_item.all_device.setBackgroundResource(R.drawable.locator_common_select_btn);
                center_item.all_device.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 1:
                center_item.online_device.setBackgroundResource(R.drawable.locator_common_select_btn);
                center_item.online_device.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 2:
                center_item.offline_device.setBackgroundResource(R.drawable.locator_common_select_btn);
                center_item.offline_device.setTextColor(Color.parseColor("#ffffff"));
                break;
        }
    }


    private void initView(){
        list=new ArrayList<>();
        ChangEquipmentBean bean=new ChangEquipmentBean("120小时30分钟30秒","用户名1",1,0);
        ChangEquipmentBean bean1=new ChangEquipmentBean("120小时30分钟30秒","用户名1",1,1);
        ChangEquipmentBean bean2=new ChangEquipmentBean("120小时30分钟30秒","用户名1",0,2);
        ChangEquipmentBean bean3=new ChangEquipmentBean("120小时30分钟30秒","用户名1",1,3);
        ChangEquipmentBean bean4=new ChangEquipmentBean("120小时30分钟30秒","用户名1",1,4);
        list.add(bean);
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        adapter = new ChanEquipmentAdapter(R.layout.loc_change_eqipment_item,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        driver=  new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this,10), Color.parseColor("#ececec"));
        if(recyclerView.getItemDecorationCount()==0){
            recyclerView.addItemDecoration(driver);
        }
        recyclerView.setAdapter(adapter);
    }
}
