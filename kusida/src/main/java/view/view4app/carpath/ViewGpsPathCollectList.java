package view.view4app.carpath;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.listview.SwipeAndPushRefreshListView;
import com.kulala.staticsview.listview.SwipeListView;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.input.ViewAboveInput;
import com.kulala.staticsview.titlehead.SwitchHead;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import adapter.AdapterForFavouriteList;
import adapter.AdapterShowGpsPathCollect;
import ctrl.OCtrlGps;
import model.ManagerCarList;
import model.ManagerGps;
import model.gps.DataGpsPath;
import model.gps.DataGpsPoint;

import static com.kulala.dispatcher.OEventName.GPS_FAVORITE_INTRO_CHANGE_OK;
import static com.kulala.dispatcher.OEventName.GPS_FAVORITE_LISTCHANGE;

/**
 * 收藏的轨迹和地点
 */

public class ViewGpsPathCollectList extends RelativeLayoutBase {
    private SwipeListView list_paths;
    private SwipeAndPushRefreshListView listview_location;
    private SwitchHead swich_head;
    private AdapterShowGpsPathCollect adapterShowGpsPathCollect;
    private AdapterForFavouriteList adapterForFavouriteList;
    private String inputStr;//输入的备注信息
    private ViewAboveInput inputAbove;
    private RelativeLayout     lin_pop_input;
    private long carId = ManagerCarList.getInstance().getCurrentCar().ide;
    private int state=1;
    private List<DataGpsPath> listGpsPath;
    private RelativeLayout re_nolayout;
    private boolean isFirst=true; //是否第一次进来

    public ViewGpsPathCollectList(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.gps_path_collect_list, this, true);
        list_paths = (SwipeListView) findViewById(R.id.listview_paths);
        listview_location = (SwipeAndPushRefreshListView) findViewById(R.id.listview_location);
        swich_head = (SwitchHead) findViewById(R.id.swich_head);
        lin_pop_input = (RelativeLayout) findViewById(R.id.lin_pop_input);
        re_nolayout = (RelativeLayout) findViewById(R.id.re_nolayout);
        re_nolayout.setVisibility(View.INVISIBLE);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_PATHLIST_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.GPS_FAVORITE_LISTCHANGE, this);
        ODispatcher.addEventListener(OEventName.GPS_FAVORITE_INTRO_CHANGE_OK, this);
        AdapterForFavouriteList.whereUse=0;
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.GPS_PATHLIST_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.GPS_FAVORITE_LISTCHANGE, this);
        ODispatcher.removeEventListener(OEventName.GPS_FAVORITE_INTRO_CHANGE_OK, this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void initViews() {
        listview_location.setRightViewWidth(ODipToPx.dipToPx(getContext(),80f));
//        list_paths.setRightViewWidth(ODipToPx.dipToPx(getContext(),90f));
        list_paths.setRightViewWidth(0);
        list_paths.setVisibility(View.VISIBLE);
        if(ManagerCarList.getInstance().getCurrentCar() !=null && ManagerCarList.getInstance().getCurrentCar().ide>0){
            OCtrlGps.getInstance().ccmd1245_getGPSAreaList(carId, 0, 20);
            OCtrlGps.getInstance().ccmd1218_favoriteList();
        }
    }

    @Override
    protected void initEvents() {
        swich_head.SetOnSelectedListener(new SwitchHead.OnSelectedListener() {
            @Override
            public void onSelect(int selectWay) {
                switch (selectWay) {
                    case 1:
                        list_paths.setVisibility(View.VISIBLE);
                        listview_location.setVisibility(View.INVISIBLE);
                        listGpsPath=ManagerGps.getInstance().singleCarPath;
                        if(listGpsPath==null||listGpsPath.size()==0){
                            re_nolayout.setVisibility(View.VISIBLE);
                        }else{
                            re_nolayout.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 2:
                        isFirst=false;
                        list_paths.setVisibility(View.INVISIBLE);
                        listview_location.setVisibility(View.VISIBLE);
                        List<DataGpsPoint> listFavorite = ManagerGps.getInstance().favoriteList;
                        if(listFavorite==null||listFavorite.size()==0){
                            re_nolayout.setVisibility(View.VISIBLE);
                        }else{
                            re_nolayout.setVisibility(View.INVISIBLE);
                        }
                        break;
                }
            }
        });
        listview_location.setOnLoadBottomListener(new SwipeAndPushRefreshListView.OnLoadBottomListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listview_location.loadComplete();
                    }
                }, 2000);
            }
        });
        listview_location.setonLoadHeaderListener(new SwipeAndPushRefreshListView.OnLoadHeaderListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listview_location.loadComplete();
                    }
                }, 2000);
            }
        });
        swich_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_gps_path_list);
            }
        });
//        list_paths.setonLoadHeaderListener(new ListViewPushRefresh.OnLoadHeaderListener() {
//            @Override
//            public void onLoad() {
//                state=2;
//                OCtrlGps.getInstance().ccmd1245_getGPSAreaList(carId, 0, 20);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        list_paths.loadComplete();
//                    }
//                }, 2000);
//            }
//        });
//        list_paths.setOnLoadBottomListener(new ListViewPushRefresh.OnLoadBottomListener() {
//            @Override
//            public void onLoad() {
//                int startpos = list_paths.getCount() - 2;
//                state=3;
//                OCtrlGps.getInstance().ccmd1245_getGPSAreaList(carId, startpos, 20);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        list_paths.loadComplete();
//                    }
//                }, 2000);
//            }
//        });
    }

    @Override
    public void receiveEvent(String key, Object value) {
        if (key.equals(OEventName.GPS_PATHLIST_RESULTBACK)) {
            int result = (Integer) value;
            if (result == 1245) {
                gpsListResultBack();
            }
            else if (result == 1244) {
                adapterShowGpsPathCollect.notifyDeleteRecord();
                IsHaveGpsPathRecord();
            }else if (result == 1243) {
                adapterShowGpsPathCollect.addComment(inputStr);
            }

        } else if (key.equals(GPS_FAVORITE_LISTCHANGE)) {
            switch ((Integer)value){
                case 1217:
                    adapterForFavouriteList.notifyDeleteRecord();
                    IsHaveFavouriteRecord();
                    break;
                case 1218:
                    gpsFavouriteLsitBack();
                    break;
            }
        }else if(key.equals(GPS_FAVORITE_INTRO_CHANGE_OK)){
            adapterForFavouriteList.addComment(inputStr);
        }
    }

    /**
     * 轨迹列表数据返回
     */
    public void gpsListResultBack() {
        Message message = Message.obtain();
        message.what = 1245;
        handler.sendMessage(message);
    }

    /**
     * 收藏列表收据返回
     */
    public void gpsFavouriteLsitBack() {
        Message message = Message.obtain();
        message.what = 1218;
        handler.sendMessage(message);
    }
    public void IsHaveGpsPathRecord() {
        Message message = Message.obtain();
        message.what = 1244;
        handler.sendMessage(message);
    }
    public void IsHaveFavouriteRecord() {
        Message message = Message.obtain();
        message.what = 1217;
        handler.sendMessage(message);
    }
    public void test(){
        List<Map<String, String>> list1 = new LinkedList<Map<String, String>>();
        List<Map<String, String>> list2 = new LinkedList<Map<String, String>>();
        Map<String,Integer> idsMap = new HashMap<String,Integer>();
        for (int i = 0; i < list1.size(); i++) {
            String id = list1.get(i).get("id");
            idsMap.put(id,i);
        }

        for (Map<String, String> map : list2) {
            String id = map.get("id");
            Integer index = idsMap.get(id);
            if(index != null){
                Map<String, String> tMap = list1.get(index);
                map.putAll(tMap);
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1245) {
                if(state==1){
                    listGpsPath = ManagerGps.getInstance().singleCarPath;
                }else if(state==2){
                    listGpsPath = ManagerGps.getInstance().singleCarPath;
                }else if(state==3){
                    List<DataGpsPath> list=ManagerGps.getInstance().singleCarPath;
                    listGpsPath.addAll(list);
                }
                if(listGpsPath==null||listGpsPath.size()==0){
                    re_nolayout.setVisibility(View.VISIBLE);
                }else{
                    re_nolayout.setVisibility(View.INVISIBLE);
                }
                if (adapterShowGpsPathCollect == null) {
                    adapterShowGpsPathCollect = new AdapterShowGpsPathCollect(getContext(),ODipToPx.dipToPx(getContext(),90f) ,listGpsPath);
                    list_paths.setAdapter(adapterShowGpsPathCollect);
                }else{
                    adapterShowGpsPathCollect.notifyDataSetChanged();
                }
                adapterShowGpsPathCollect.setonClickBianJi(new AdapterShowGpsPathCollect.onClickBianJi() {
                    @Override
                    public void click(final DataGpsPath info) {
                        if (lin_pop_input.getChildCount() > 0) return;//已加入过
//                    TurnOffKeyBoard.openKeyBoard(GlobalContext.getCurrentActivity());
                        if (inputAbove == null)inputAbove = new ViewAboveInput(getContext(), null);
                        inputAbove.show("请输入备注内容", "", 20);
                        lin_pop_input.addView(inputAbove);
                        inputAbove.SetOnClickConfirmListener(new ViewAboveInput.OnClickConfirmListener() {
                            @Override
                            public void onClickConfirm(String InputTxt) {
                                inputStr=InputTxt;
                                if(ManagerCarList.getInstance().getCurrentCar().ide>=0){
                                    OCtrlGps.getInstance().ccmd1243_addComment(ManagerCarList.getInstance().getCurrentCar().ide,info.ide,InputTxt,0,0,0,20);
                                }
                                lin_pop_input.removeAllViews();
                            }
                        });
                    }
                });
            } else if (msg.what == 1218) {
                List<DataGpsPoint> listFavorite = ManagerGps.getInstance().favoriteList;
                if(!isFirst){
                    if(listFavorite==null||listFavorite.size()==0){
                        re_nolayout.setVisibility(View.VISIBLE);
                    }else{
                        re_nolayout.setVisibility(View.INVISIBLE);
                    }
                }
                if(adapterForFavouriteList==null){
                    adapterForFavouriteList = new AdapterForFavouriteList(listFavorite,getContext(),ODipToPx.dipToPx(getContext(),80f));
                    listview_location.setAdapter(adapterForFavouriteList);
                }
                adapterForFavouriteList.setonClickBianJi(new AdapterForFavouriteList.onClickBianJi() {
                    @Override
                    public void click(final DataGpsPoint info) {
                        if (lin_pop_input.getChildCount() > 0) return;//已加入过
//                    TurnOffKeyBoard.openKeyBoard(GlobalContext.getCurrentActivity());
                        if (inputAbove == null)inputAbove = new ViewAboveInput(getContext(), null);
                        inputAbove.show("请输入备注内容", "", 20);
                        lin_pop_input.addView(inputAbove);
                        inputAbove.SetOnClickConfirmListener(new ViewAboveInput.OnClickConfirmListener() {
                            @Override
                            public void onClickConfirm(String InputTxt) {
                                inputStr=InputTxt;
                                if(ManagerCarList.getInstance().getCurrentCar().ide>=0){
                                    OCtrlGps.getInstance().ccmd1225_favoriteIntro(info.ide,InputTxt);
                                }
                                lin_pop_input.removeAllViews();
                            }
                        });
                    }
                });
            }else if(msg.what == 1217){
                List<DataGpsPoint> listFavorite = ManagerGps.getInstance().favoriteList;
                if(listFavorite==null||listFavorite.size()==0){
                    re_nolayout.setVisibility(View.VISIBLE);
                }else{
                    re_nolayout.setVisibility(View.INVISIBLE);
                }
            }else if(msg.what==1244){
                listGpsPath=ManagerGps.getInstance().singleCarPath;
                if(listGpsPath==null||listGpsPath.size()==0){
                    re_nolayout.setVisibility(View.VISIBLE);
                }else{
                    re_nolayout.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    @Override
    protected void invalidateUI() {
    }
}
