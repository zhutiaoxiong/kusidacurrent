package view.view4me.carmanage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.TurnOffKeyBoard;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCar;
import ctrl.OCtrlCommon;
import model.ManagerCarList;
import model.ManagerCommon;
import model.carlist.DataCarInfo;
import view.view4me.set.ClipTitleMeSet;

/**
 * 车辆管理主页面
 */
public class ViewCarmanMain extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    private ListView list_cars;
    private AdapterCarmanCarlist        adapter;
    private MyHandler handler = new MyHandler();
//    private RelativeLayout re_shake;
    public ViewCarmanMain(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.carman_main, this, true);
        title_head =  findViewById(R.id.title_head);
//        re_shake = (RelativeLayout) findViewById(R.id.re_shake);
        list_cars = (ListView) findViewById(R.id.list_cars);
//        list_cars.setRightViewWidth(ODipToPx.dipToPx(getContext(), 135));
        ODispatcher.addEventListener(OEventName.CAR_LIST_CHANGE, this);
        ODispatcher.addEventListener(OEventName.SCAN_RESULT_BACK, this);
        initViews();
        initEvents();
    }
    @Override
    public void initViews() {
        TurnOffKeyBoard.closeKeyBoardCloseScoll(GlobalContext.getCurrentActivity());
        OCtrlCar.getInstance().ccmd1203_getcarlist();
    }

    @Override
    public void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
//                DataShakeTips.saveOutShake(getContext(),true);
                ODispatcher.dispatchEvent(OEventName.SHOW_CHONGZHI);
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
//                ODispatcher.dispatchEvent(OEventName.KULALA_SHAKE);
            }
        });
        // select car
        title_head.img_right.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_newcar);
            }
        });
//        re_shake.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View view) {
//                DataShakeTips.saveActive(getContext(),true);
//                re_shake.setVisibility(View.INVISIBLE);
//            }
//        });
//        list_cars.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                switch (scrollState) {
//                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态
////					adapter.notifyDataSetInvalidated();
//                        break;
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://滚动状态停止
////						adapter.notifyDataSetInvalidated();
//                        break;
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });

    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CAR_LIST_CHANGE)) {
            handleChangeData();
                //初始化设当前车
                long currentId=ManagerCarList.getInstance().getCurrentCarID();
                DataCarInfo cacheCar=null;
                List<DataCarInfo> carInfoList= ManagerCarList.getInstance().getCarInfoList();
                if(carInfoList!=null&&carInfoList.size()>0){
                    for (int i = 0; i < carInfoList.size(); i++) {
                        if(currentId==carInfoList.get(i).ide){
                            cacheCar= carInfoList.get(i);
                        }
                    }
                }
            if(cacheCar!=null){
                BlueLinkReceiver.clearBluetooth();
                Log.e("拿车列表", "cacheCar.bluetoothName" +cacheCar.bluetoothName+"cacheCar.blueCarsig" +cacheCar.blueCarsig);
                if(cacheCar.blueCarsig!=null&&cacheCar.blueCarsig.equals("-1")){
                    BlueLinkReceiver.needChangeCar(cacheCar.ide,"",cacheCar.blueCarsig,cacheCar.isBindBluetooth,cacheCar.carsig,cacheCar.isMyCar);
                }else{
                    BlueLinkReceiver.needChangeCar(cacheCar.ide,cacheCar.bluetoothName,cacheCar.blueCarsig,cacheCar.isBindBluetooth,cacheCar.carsig,cacheCar.isMyCar);
                }
            }
        }else  if (eventName.equals(OEventName.SCAN_RESULT_BACK)) {
            String result = (String) paramObj;
            if(!TextUtils.isEmpty(result)){
                ViewCarmanModelBind.equipmentNunber=result;
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.carman_model_set_bind);
            }
        }
    }

    @Override
    public void callback(String key, Object value) {
    }

    @Override
    protected void onDetachedFromWindow() {
        list_cars.setAdapter(null);
        adapter = null;
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
        List<DataCarInfo> list = ManagerCarList.getInstance().getCarInfoList();
//         if (BuildConfig.DEBUG) Log.e("查看摇一摇开关", "list"+list.get(0).isBindBluetooth);
        if (list == null) {
            list_cars.setAdapter(null);
//            re_shake.setVisibility(View.INVISIBLE);
            return;
        }
//        boolean isClickActive= DataShakeTips.loadIsClickActive(getContext());
//        if(!isClickActive){
//            re_shake.setVisibility(View.VISIBLE);
//        }else{
//            re_shake.setVisibility(View.INVISIBLE);
//        }
        // 加载品牌列表

        if (adapter == null) {
            adapter = new AdapterCarmanCarlist(getContext(), ODipToPx.dipToPx(getContext(), 90), list);
//            adapter.setCurrentItem(ManagerCarList.getInstance().getCurrentCar());
            list_cars.setAdapter(adapter);
        } else {
            adapter.changeData(list);
            adapter.notifyDataSetChanged();
        }
        Log.e("车列表拿的时间", "显示完时间" +System.currentTimeMillis());
        if (ManagerCommon.getInstance().getBrandsList() == null) {
            OCtrlCommon.getInstance().ccmd1310_getBrandList(ManagerCommon.getInstance().getBrandUpdateTime());
//            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.network_bad));
        }
    }
    public void handlePopView(int layoutId) {
        Message message = new Message();
        message.what = 1586;
        handler.sendMessage(message);
    }
    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1586:
                    break;
            }
        }
    }
}
