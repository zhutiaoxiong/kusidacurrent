package view.view4me;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterForCityList;

/**
 * Created by qq522414074 on 2017/3/20.
 */

public class OPopSHowCityList {
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private RelativeLayout thisView;
    private Context context;
    private LinearLayout li1,li2,li3,li4;
    private TextView txt_cancel;
    private AdapterForCityList adapterForCityList;
    private GridView city_list;
    private        View         touch_exit;
    private        String       mark;//选择标记
    private OCallBack callback;
    private        MyHandler    handler;
    // ========================out======================
    private static OPopSHowCityList _instance;
    private List<String> list;
    public static String[] citys=new String[]{"京","津","冀","晋","蒙","辽","吉","黑","沪","苏","浙","皖","闽","赣","鲁","豫","鄂",
            "湘","粤","桂","琼","渝","川","贵","云","藏","陕","甘","青","宁","新","其他"};
    public static OPopSHowCityList getInstance() {
        if (_instance == null)
            _instance = new OPopSHowCityList();
        return _instance;
    }

    public void show(View parentView,  String mark, OCallBack callback,String str) {
        if (handler == null) handler = new MyHandler();
        this.mark = mark;
        this.callback = callback;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.city_list, null);
        touch_exit=  thisView.findViewById(R.id.touch_exit);
        city_list= (GridView) thisView.findViewById(R.id.city_list);
        list=new ArrayList<>();
            for(int i=0;i<citys.length;i++){
                list.add(citys[i]);
            }
        adapterForCityList=new AdapterForCityList(parentView.getContext(),list,str);
        city_list.setAdapter(adapterForCityList);
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(false);
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }


    public void initEvents() {
        city_list.setOnItemClickListener(new OnItemClickListenerMy(){
            @Override
            public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {

                handlehide();
                String city=list.get(position);
                if(callback!=null){
                    callback.callback(mark,city);
                }
            }
        });
        touch_exit.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlehide();
            }
        });
    }
    private void handlehide() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16596;
        handler.sendMessage(message);
    }

    // ===================================================
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16596:
                    if(popContain == null)return;
                    popContain.dismiss();
                    callback = null;
                    parentView = null;
                    thisView = null;
                    context = null;
                    adapterForCityList=null;
                    list=null;
                    break;
            }
        }
    }
}
