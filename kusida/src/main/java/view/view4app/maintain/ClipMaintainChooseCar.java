package view.view4app.maintain;

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
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import adapter.AdapterShowCarList;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import com.kulala.staticsview.titlehead.ClipTitleHead;

/**
 * Created by qq522414074 on 2016/11/7.
 */

public class ClipMaintainChooseCar {
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private Context context;
    private String[] data;

    private LinearLayout thisView;
    private ClipTitleHead title_head;
    private ListView listView;
    private String mark;//选择标记
    private OCallBack callback;
    private AdapterShowCarList adapter;
    private DataCarInfo selectedCar;
    private List<DataCarInfo> list;
    // ========================out======================
    private static ClipMaintainChooseCar _instance;
    public static ClipMaintainChooseCar getInstance() {
        if (_instance == null)
            _instance = new ClipMaintainChooseCar();
        return _instance;
    }
    //===================================================
    public void show(View parentView,String[] strArr,String titleStr,String mark,OCallBack callback) {
        if(strArr == null)return;
        this.mark = mark;
        this.callback = callback;
        this.parentView = parentView;
        context = parentView.getContext();
        this.data = strArr;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (LinearLayout)layoutInflater.inflate(R.layout.clip_title_list, null);
        title_head = (ClipTitleHead) thisView.findViewById(R.id.title_head);
        listView = (ListView) thisView.findViewById(R.id.list_names);
         list = ManagerCarList.getInstance().getCarInfoList();
            adapter = new AdapterShowCarList(context, list, R.layout.list_item_carname);
            adapter.setCurrentItem(ManagerCarList.getInstance().getCurrentCar());
            listView.setAdapter(adapter);
        initViews();
        initEvents();
        title_head.setTitle(titleStr);
    }
    public void addRightImage(int resId){
        title_head.setRightRes(resId);
        title_head.img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback("ClipPopChooseStr_Click_Right", "");
                popContain.dismiss();
            }
        });
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
        popContain.showAtLocation(parentView, Gravity.BOTTOM,  0, 0);
    }
    public void initEvents() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id){
                if(list==null||list.size()==0)return;
                adapter.setCurrentItem(position);
                selectedCar = adapter.getCurrentItem();
                ManagerCarList.getInstance().setCurrentCar(selectedCar.ide);
                    adapter.changeData(list);
                    adapter.notifyDataSetChanged();

                }
        });
        title_head.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(selectedCar!=null){
                callback.callback(mark, selectedCar.num);}
                popContain.dismiss();
            }
        });
    }
}
