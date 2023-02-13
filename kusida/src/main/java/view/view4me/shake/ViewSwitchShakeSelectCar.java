package view.view4me.shake;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.client.proj.kusida.R;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.titlehead.ClipTitleHead;

import java.util.List;

import model.carlist.DataCarInfo;

/**
 * Created by qq522414074 on 2016/11/7.
 */

public class ViewSwitchShakeSelectCar {
    private PopupWindow popContain;//弹出管理
    private View        parentView;//本对象显示
    private Context     context;

    private        LinearLayout             thisView;
    private        ClipTitleHead            title_head;
    private        ListView                 listView;
    private        ViewSwitchShakeAdapter   adapter;
    private        OnSelectCarListener      onSelectCarListener;
    // ========================out======================
    private static ViewSwitchShakeSelectCar _instance;
    public static ViewSwitchShakeSelectCar getInstance() {
        if (_instance == null)
            _instance = new ViewSwitchShakeSelectCar();
        return _instance;
    }
    public interface OnSelectCarListener {
        void onSelectCar(DataCarInfo carInfo);
    }
    //===================================================
    public void show(View parentView, DataCarInfo preCar, List<DataCarInfo> cacheBlueList, OnSelectCarListener listener) {
        onSelectCarListener = listener;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (LinearLayout) layoutInflater.inflate(R.layout.clip_title_list, null);
        title_head = (ClipTitleHead) thisView.findViewById(R.id.title_head);
        title_head.setTitle("选择车辆");
        listView = (ListView) thisView.findViewById(R.id.list_names);

        adapter = new ViewSwitchShakeAdapter(context,preCar, cacheBlueList);
        listView.setAdapter(adapter);
        initViews();
        initEvents();
    }
    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popContain.dismiss();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }
    public void initEvents() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onSelectCarListener!=null)onSelectCarListener.onSelectCar(adapter.getItem(position));
                popContain.dismiss();
            }
        });
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                popContain.dismiss();
            }
        });
    }
}
