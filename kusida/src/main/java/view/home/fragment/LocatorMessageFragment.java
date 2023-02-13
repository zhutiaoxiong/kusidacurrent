package view.home.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import adapter.RecycleViewDivider;
import model.locator.MessageBean;
import view.home.activity.ActivityMessageDetail;
import view.home.activity.ActivityMessageSelect;
import view.home.adapter.MessageAdapter;
import view.home.commonview.MessageTopItem;


public class LocatorMessageFragment extends Fragment {
    private MessageTopItem top_item;
    private RecyclerView recyclerView;
    private LinearLayout bottom_layout;
    private LinearLayout check_layout;
    private TextView confirm;
    private ImageView check_iv;
    private MessageAdapter adapter;
    private RecycleViewDivider driver;
    private static final String TAG = "LocatorMessageFragment";
    private int p = -1;//用来记录上一个点击条目的下标
    private List<MessageBean> list;
    private boolean isAllSelect;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locator_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        top_item = view.findViewById(R.id.top_item);
        recyclerView = view.findViewById(R.id.recycleview);
        bottom_layout = view.findViewById(R.id.bottom_layout);
        check_layout = view.findViewById(R.id.check_layout);
        confirm = view.findViewById(R.id.confirm);
        check_iv = view.findViewById(R.id.check_iv);
        initView();
        initEvent();

    }

    private void initView() {
        long time = System.currentTimeMillis();
        list = new ArrayList<>();
        MessageBean bean = new MessageBean("广东-东莞", 0, time);
        MessageBean bean1 = new MessageBean("广东-广州", 1, time);
        MessageBean bean2 = new MessageBean("广东-佛山", 2, time);
        MessageBean bean3 = new MessageBean("广东-清远", 0, time);
        list.add(bean);
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        adapter = new MessageAdapter(R.layout.loc_message_rv_item, list);
        adapter.addChildClickViewIds(R.id.select_status);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        driver = new RecycleViewDivider(
                getContext(), LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(getContext(), 5), Color.parseColor("#FAFAFA"));
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(driver);
        }
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        top_item.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick delete");
                bottom_layout.setVisibility(View.VISIBLE);setListCheckAllVisible();
            }
        });
        top_item.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick select");
                Intent intent=new Intent(getActivity(), ActivityMessageSelect.class);
                startActivity(intent);
            }
        });
        top_item.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick search ");
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
                Intent intent=new Intent(getActivity(), ActivityMessageDetail.class);
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

    private void setListAllCheck() {
        if(list!=null&&list.size()>0)
        for (MessageBean bean : list
        ) {
            if(bean!=null){
                bean.isSelect=true;
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void setListAllCheckFalse() {
        if(list!=null&&list.size()>0)
            for (MessageBean bean : list
            ) {
                if(bean!=null){
                    bean.isSelect=false;
                }
            }
        adapter.notifyDataSetChanged();
    }
    private void setListCheckAllVisible() {
        if(list!=null&&list.size()>0)
            for (MessageBean bean : list
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