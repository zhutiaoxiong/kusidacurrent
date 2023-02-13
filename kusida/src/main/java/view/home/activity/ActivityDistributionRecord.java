package view.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import adapter.RecycleViewDivider;
import model.locator.DistributionRecordBean;
import view.home.adapter.DistributionRecordAdapter;
import view.home.commonview.CommonTitletem;

public class ActivityDistributionRecord extends AllActivity {
    private CommonTitletem top_item;
    private RecyclerView recyclerView;
    private DistributionRecordAdapter adapter;
    private RecycleViewDivider driver;
    private static final String TAG = "ActivityDistributionRecord";
    private List<DistributionRecordBean> list;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_record);
        top_item=findViewById(R.id.top_item);
        recyclerView = findViewById(R.id.recycleview);
        initView();
        initEvent();
    }
    private void initView() {
        long time = System.currentTimeMillis();
        list = new ArrayList<>();
        DistributionRecordBean bean = new DistributionRecordBean("工作模式(深度睡眠)", 0,0, time,time);
        DistributionRecordBean bean1 = new DistributionRecordBean("工作模式(深度睡眠)", 0, 0,time,time);
        DistributionRecordBean bean2 = new DistributionRecordBean("工作模式(深度睡眠)", 0,0, time,time);
        DistributionRecordBean bean3 = new DistributionRecordBean("工作模式(深度睡眠)", 0,0, time,time);
        list.add(bean);
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        adapter = new DistributionRecordAdapter(R.layout.loc_dis_record_rv_item, list);
        adapter.addChildClickViewIds(R.id.select_status);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        driver = new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this, 5), Color.parseColor("#FAFAFA"));
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(driver);
        }
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        top_item.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "onClick left ");
                finish();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.d(TAG, "onClick item " + position);
                Intent intent=new Intent(ActivityDistributionRecord.this, ActivityMessageDetail.class);
                startActivity(intent);
            }
        });
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }
}
