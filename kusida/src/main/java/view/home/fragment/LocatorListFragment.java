package view.home.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import model.locator.LocatorFragmentListBean;
import view.home.activity.ActivityMore;
import view.home.adapter.LocatorFragmentListAdapter;
import view.home.commonview.CommonCenterItem;
import view.home.commonview.CommonTopItem;


public class LocatorListFragment extends Fragment {
    private CommonTopItem top_item;
    private CommonCenterItem center_item;
    private RecyclerView recyclerView;
    private LocatorFragmentListAdapter adapter;
    private RecycleViewDivider driver;
    private static final String TAG="LocatorListFragment";
    private int p=-1;//用来记录上一个点击条目的下标
    List<LocatorFragmentListBean> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locator_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        top_item =  view.findViewById(R.id.top_item);
        center_item =  view.findViewById(R.id.center_item);
        recyclerView =  view.findViewById(R.id.recycleview);
        initView();
        initEvent();
    }
    private void initEvent(){
        top_item.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick back" );
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
            }
        });

        center_item.online_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick online_device  " );
            }
        });

        center_item.offline_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick offline_device  " );
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e(TAG, "onClick item " +position);
            }
        });
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.cons_1){
                    Log.e(TAG, "onClick 点击选择框 " );
                    if (p!=-1&&p!=position){
                        list.get(p).setSelect(false);
                        list.get(position).setSelect(true);
                    }else {
                        list.get(position).setSelect(true);
                    }
                    p=position;
                    adapter.notifyDataSetChanged();
                    //2.超出一页，滚动至点击条目
                    recyclerView.scrollToPosition(position);
                }else    if(view.getId()==R.id.cons_2){
                    Log.e(TAG, "onClick 点击更多 " );
                    Intent intent=new Intent(getActivity(), ActivityMore.class);
                    startActivity(intent);
                }else    if(view.getId()==R.id.tv_note){
                    Log.e(TAG, "onClick 点击备注" );
                }
            }
        });

    }

    private void initView(){
        list=new ArrayList<>();
        LocatorFragmentListBean bean=new LocatorFragmentListBean("120小时30分钟30秒","用户名1",1,"888888","6666666",500,"我是备注");
        LocatorFragmentListBean bean1=new LocatorFragmentListBean("120小时30分钟30秒","用户名1",1,"888888","6666666",500,"我是备注");
        LocatorFragmentListBean bean2=new LocatorFragmentListBean("120小时30分钟30秒","用户名1",0,"888888","6666666",500,"我是备注");
        LocatorFragmentListBean bean3=new LocatorFragmentListBean("120小时30分钟30秒","用户名1",1,"888888","6666666",500,"我是备注");
        list.add(bean);
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        adapter = new LocatorFragmentListAdapter(R.layout.loc_fragmentlist_item,list);
        adapter.addChildClickViewIds(R.id.cons_1,R.id.cons_2,R.id.tv_note);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        driver=  new RecycleViewDivider(
                getContext(), LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(getContext(),10), Color.parseColor("#ececec"));
        if(recyclerView.getItemDecorationCount()==0){
            recyclerView.addItemDecoration(driver);
        }
        recyclerView.setAdapter(adapter);
    }
}