package view.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.timepicker.TimePickerView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapter.RecycleViewDivider;
import model.locator.LocatorTrackListBean;
import view.home.adapter.LocTrackListAdapter;
import view.home.commonview.CommonCenterItemForTrackList;
import view.home.commonview.MessageTitleFourItem;

public class ActivityLocatorTrackList extends AllActivity {
    private MessageTitleFourItem top_item;
    private RecyclerView recyclerView;
    private LinearLayout bottom_layout;
    private LinearLayout check_layout;
    private TextView confirm;
    private ImageView check_iv;
    private LocTrackListAdapter adapter;
    private RecycleViewDivider driver;
    private CommonCenterItemForTrackList center_item;
    private static final String TAG = "ActivityMessage";
    private int p = -1;//用来记录上一个点击条目的下标
    private List<LocatorTrackListBean> list;
    private boolean isAllSelect;
    private TimePickerView pvTime;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator_track_list);
        top_item=findViewById(R.id.top_item);
        recyclerView = findViewById(R.id.recycleview);
        bottom_layout = findViewById(R.id.bottom_layout);
        check_layout = findViewById(R.id.check_layout);
        confirm = findViewById(R.id.confirm);
        check_iv = findViewById(R.id.check_iv);
        center_item= findViewById(R.id.center_item);
        initView();
        initEvent();
    }
    private void initView() {
        //时间选择器
        pvTime = new TimePickerView(ActivityLocatorTrackList.this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);

        long time = System.currentTimeMillis();
        list = new ArrayList<>();
        LocatorTrackListBean bean = new LocatorTrackListBean("888888","广东东莞东坑" ,"广东东莞横沥",time,time, false);
        LocatorTrackListBean bean1 = new LocatorTrackListBean("777777", "广东东莞东坑" ,"广东东莞横沥",time,time, false);
        LocatorTrackListBean bean2 = new LocatorTrackListBean("666666", "广东东莞东坑" ,"广东东莞横沥",time,time, true);
        LocatorTrackListBean bean3 = new LocatorTrackListBean("555555", "广东东莞东坑" ,"广东东莞横沥",time,time, false);
        list.add(bean);
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        adapter = new LocTrackListAdapter(R.layout.loc_track_list_item, list);
        adapter.addChildClickViewIds(R.id.select_status);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        driver = new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this, 10), Color.parseColor("#ececec"));
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(driver);
        }
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date datee, String mark) {
                if (mark.equals("time")) {
                    long     time = datee.getTime();
                    String date = ODateTime.time2StringWithHH(time);
                    Logger.d(date);
                }
            }
        });
        top_item.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick delete");
                bottom_layout.setVisibility(View.VISIBLE);
                setListCheckAllVisible();
            }
        });
        top_item.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick select");
                Intent intent=new Intent(ActivityLocatorTrackList.this, ActivityMessageSelect.class);
                startActivity(intent);
            }
        });
        top_item.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick left ");
                finish();
            }
        });
        center_item.today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick today ");
                setCenterItemUI(0);
            }
        });
        center_item.lastday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick today ");
                setCenterItemUI(1);
            }
        });
        center_item.select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick today ");
                setCenterItemUI(2);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick confirm ");
                deleteListChangeUI();
            }
        });
        check_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick check_layout ");
                isAllSelect = !isAllSelect;
                if (isAllSelect) {
                    check_iv.setImageResource(R.drawable.locator_icon_list_item_check_true);
                    setListAllCheck();
                } else {
                    check_iv.setImageResource(R.drawable.message_checkbox);
                    setListAllCheckFalse();
                }
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e(TAG, "onClick item " + position);
                Intent intent=new Intent(ActivityLocatorTrackList.this, ActivityTracklistDetail.class);
                startActivity(intent);
            }
        });
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.select_status) {
                    Log.e(TAG, "onClick 点击选择框 ");
                    if(!isAllSelect){
                        if (p != -1 && p != position) {
                            list.get(p).setSelect(false);
                            list.get(position).setSelect(true);
                        } else {
                            list.get(position).setSelect(true);
                        }
                        //2.超出一页，滚动至点击条目
                    }else{
                        list.get(position).setSelect(!list.get(position).isSelect);
                        //2.超出一页，滚动至点击条目
                    }
                    p = position;
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(position);
                }
            }
        });
    }

    private void setCenterItemUI(int item){
        center_item.today.setBackgroundColor(Color.parseColor("#ffffff"));
        center_item.today.setTextColor(Color.parseColor("#000000"));
        center_item.lastday.setBackgroundColor(Color.parseColor("#ffffff"));
        center_item.lastday.setTextColor(Color.parseColor("#000000"));
        center_item.select_time.setBackgroundColor(Color.parseColor("#ffffff"));
        center_item.select_time.setTextColor(Color.parseColor("#000000"));
        switch (item){
            case 0:
                center_item.today.setBackgroundResource(R.drawable.locator_common_select_btn);
                center_item.today.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 1:
                center_item.lastday.setBackgroundResource(R.drawable.locator_common_select_btn);
                center_item.lastday.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 2:
                center_item.select_time.setBackgroundResource(R.drawable.locator_common_select_btn);
                center_item.select_time.setTextColor(Color.parseColor("#ffffff"));
                pvTime.show();
                pvTime.setMark("time");
                break;
        }
    }

    private void setListAllCheck() {
        if(list!=null&&list.size()>0)
            for (LocatorTrackListBean bean : list
            ) {
                if(bean!=null){
                    bean.isSelect=true;
                }
            }
        adapter.notifyDataSetChanged();
    }
    private void setListAllCheckFalse() {
        if(list!=null&&list.size()>0)
            for (LocatorTrackListBean bean : list
            ) {
                if(bean!=null){
                    bean.isSelect=false;
                }
            }
        adapter.notifyDataSetChanged();
    }
    private void setListCheckAllVisible() {
        if(list!=null&&list.size()>0)
            for (LocatorTrackListBean bean : list
            ) {
                if(bean!=null){
                    bean.isShow=true;
                }
            }
        adapter.notifyDataSetChanged();
    }
    private void deleteListChangeUI(){
        if(list!=null&&list.size()>0){
            for(int i=0; i<list.size(); i++){
                if(list.get(i)!=null&&list.get(i).isSelect){
                    list.remove(list.get(i));
                    i--;
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
