package view.home.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.client.proj.kusida.R;

import java.util.ArrayList;
import java.util.List;

import model.locator.MessageSelectBean;
import view.home.adapter.MessageSelectAdapter;
import view.home.commonview.CommonTitletem;

public class ActivityRecordTimeSelect extends AllActivity {
    private CommonTitletem top_item;
    private RecyclerView recyclerView;
    private TextView confirm;
    private ImageView check_iv;
    private LinearLayout check_layout;
    private MessageSelectAdapter adapter;
    private static final String TAG = "ActivityMessageSelect";
    private int p = -1;//用来记录上一个点击条目的下标
    private List<MessageSelectBean> list;
    private boolean isAllSelect;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_time_select);
        top_item=findViewById(R.id.titleme);
        recyclerView = findViewById(R.id.recycleview);
        confirm = findViewById(R.id.confirm);
        check_iv = findViewById(R.id.check_iv);
        check_layout = findViewById(R.id.check_layout);
        initView();
        initEvent();
    }
    private void initView() {
        list = new ArrayList<>();
        String[] arr={"进电子围栏","出电子围栏","低电压报警","超速报警","振动报警","拆除报警","静止报警","录音条数不够","通电报警"};
        for (int i = 0; i <arr.length ; i++) {
            MessageSelectBean messageSelectBean=new MessageSelectBean(arr[i]);
            if(i==arr.length-1){
                messageSelectBean.isShowLine=false;
            }
            list.add(messageSelectBean);
        }
        adapter = new MessageSelectAdapter(R.layout.loc_message_select_item, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.addChildClickViewIds(R.id.select_status);
    }

    private void initEvent() {
        top_item.back.setOnClickListener(v -> {
            Log.e(TAG, "onClick back");
           finish();
        });
        confirm.setOnClickListener(v -> {
            Log.e(TAG, "onClick confirm ");
            deleteListChangeUI();
        });
        check_layout.setOnClickListener(v -> {
            Log.e(TAG, "onClick check_layout ");
            isAllSelect = !isAllSelect;
            if (isAllSelect) {
                check_iv.setImageResource(R.drawable.locator_icon_list_item_check_true);
                setListAllCheck();
            } else {
                check_iv.setImageResource(R.drawable.message_checkbox);
                setListAllCheckFalse();
            }
        });

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.select_status) {
                Log.e(TAG, "onClick 点击选择框 ");
                if(!isAllSelect){
                    if (p != -1 && p != position) {
                        list.get(p).setSelect(false);
                    }
                    list.get(position).setSelect(true);
                    //2.超出一页，滚动至点击条目
                }else{
                    list.get(position).setSelect(!list.get(position).isSelect);
                    //2.超出一页，滚动至点击条目
                }
                p = position;
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(position);
            }
        });
    }
    private void setListAllCheck() {
        if(list!=null&&list.size()>0)
            for (MessageSelectBean bean : list
            ) {
                if(bean!=null){
                    bean.isSelect=true;
                }
            }
        adapter.notifyDataSetChanged();
    }
    private void setListAllCheckFalse() {
        if(list!=null&&list.size()>0)
            for (MessageSelectBean bean : list
            ) {
                if(bean!=null){
                    bean.isSelect=false;
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
