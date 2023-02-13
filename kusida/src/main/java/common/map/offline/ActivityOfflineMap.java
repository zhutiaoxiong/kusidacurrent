package common.map.offline;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.SystemMe;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.ToastResult;
import com.kulala.staticsview.titlehead.SwitchHead;

import java.util.ArrayList;

import common.GlobalContext;
import common.map.DataPos;
import common.map.MapPosGet;

public class ActivityOfflineMap extends Activity implements MKOfflineMapListener, OEventObject {
    private SwitchHead    title_head;
    private ViewListTitle title_currentCity;
    private ExpandableListView    list_all;
    private RelativeLayout lin_wifi;
    private ImageView img_wifi_auto;

    private AdapterCityOffline adapterCurrent, adapterHot, adapterAll, adapterSuggest, adapterDownloading, adapter_finish;

    private       MKOfflineMap                 offlineMap;
    public static ArrayList<MKOLSearchRecord>  currentList;//当前城市+全图基础包
    public static ArrayList<MKOLSearchRecord>  hotCityList;//所有热门城市
    public static ArrayList<MKOLSearchRecord>  offlineCityList;//所有离线城市
    public static ArrayList<MKOLUpdateElement> allUpdateInfo;//所有已经下载的城市列表
    private MyHandler handler = new MyHandler();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_offline_view);
        title_head = (SwitchHead) findViewById(R.id.title_head);
        list_all = (ExpandableListView) findViewById(R.id.list_all);
        lin_wifi = (RelativeLayout)View.inflate(getApplicationContext(),R.layout.map_offline_view_wifi_switch,null);
//        lin_wifi = (RelativeLayout) findViewById(R.id.lin_wifi);
        list_all.addFooterView(lin_wifi);
        img_wifi_auto = (ImageView) lin_wifi.findViewById(R.id.img_wifi_auto);

        offlineMap = new MKOfflineMap();
        offlineMap.init(this);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.MAP_OFFLINE_LISTCHANGE, this);
        ODispatcher.addEventListener(OEventName.MAP_OFFLINE_START_DOWNLOAD, this);
    }
    public static MKOLUpdateElement getUpdateCity(int cityId) {
        if (allUpdateInfo == null || allUpdateInfo.size() == 0) return null;
        for (MKOLUpdateElement element : allUpdateInfo) {
            if (element.cityID == cityId) return element;
        }
        return null;
    }
    public void initViews() {
        // 获得所有热门城市
        hotCityList = offlineMap.getHotCityList();
        // 获得所有离线城市
        offlineCityList = offlineMap.getOfflineCityList();
        // 获得所有已经下载的城市列表
        allUpdateInfo = offlineMap.getAllUpdateInfo();
        if(allUpdateInfo == null)allUpdateInfo = new ArrayList<MKOLUpdateElement>();
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("OfflineMapWifiOff");
        boolean wifiOff = ODBHelper.queryResult2boolean(result);
        img_wifi_auto.setTag(wifiOff);
        if (!wifiOff){//默认自动下载
            img_wifi_auto.setImageResource(R.drawable.switch_on);
            if(SystemMe.isWifiOn(getApplicationContext())) {//wifi开启时进行自动更新
                for (MKOLUpdateElement element : allUpdateInfo) {
                    if (element.update == true) offlineMap.start(element.cityID);
                }
            }
        }
        //所有城市分层
        for (MKOLSearchRecord record : offlineCityList) {
            if (record.childCities != null) {
                MKOLSearchRecord copy = new MKOLSearchRecord();
                copy.cityID = record.cityID;
                copy.size = record.size;
                copy.cityName = "所有城市";
                copy.cityType = record.cityType;
                record.childCities.add(0, copy);
                record.size = 0;//省分独立包下载放入内部
            }
        }
        switchView(SwitchHead.SELECT_LEFT);
    }
    //取当前城市+全国基础,
    private void setCurrentList() {
        //必需先找当前城市
        MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
            @Override
            public void onCurrentPosGet(DataPos posData) {
                currentList = new ArrayList<MKOLSearchRecord>();
                MKOLSearchRecord curr = getCurrentCityFromID(MapPosGet.getPreCityId(), offlineCityList);
                MKOLSearchRecord all  = getCurrentCityFromID(1, offlineCityList);
                if (curr != null) currentList.add(curr);
                if (all != null) currentList.add(all);
            }
        });
    }
    private MKOLSearchRecord getCurrentCityFromID(int cityID, ArrayList<MKOLSearchRecord> list) {
        if (list == null) return null;
        for (MKOLSearchRecord record : list) {
            if (record.childCities == null) {
                if (record.cityID == cityID) {
                    return record;
                }
            } else {
                MKOLSearchRecord next = getCurrentCityFromID(cityID, record.childCities);
                if (next != null) return next;
            }
        }
        return null;
    }
    //changedata
    private void switchView(int leftOrRight) {
        title_head.setSelected(leftOrRight);
        // 获得所有已经下载的城市列表
        allUpdateInfo = offlineMap.getAllUpdateInfo();
        //当前城市+基础全国地图
        setCurrentList();//刷新都要加载一次
        ArrayList<MKOLSearchRecord> listAll = new ArrayList<MKOLSearchRecord>();
        if (leftOrRight == SwitchHead.SELECT_LEFT) {
            SwitchHead.select_type = SwitchHead.SELECT_LEFT;
            lin_wifi.setVisibility(View.VISIBLE);
            if (allUpdateInfo == null || allUpdateInfo.size() == 0) {//无已下载或正下载城市,只显示推荐下载
                MKOLSearchRecord currRe = new MKOLSearchRecord();
                currRe.cityName = "推荐城市";
                currRe.childCities = currentList;
                listAll.add(currRe);
            } else {//是否有正在下载城市
                ArrayList<MKOLSearchRecord> loadingList = new ArrayList<MKOLSearchRecord>();
                ArrayList<MKOLSearchRecord> finishList  = new ArrayList<MKOLSearchRecord>();
                for (MKOLUpdateElement data : allUpdateInfo) {
                    MKOLSearchRecord next = new MKOLSearchRecord();
                    next.cityID = data.cityID;
                    next.size = data.serversize;
                    next.cityName = data.cityName;
                    if(data.status >= data.FINISHED || data.ratio == 100){
                        finishList.add(next);
                    }else{
                        loadingList.add(next);
                    }
                }
                if (loadingList.size() > 0) {
                    MKOLSearchRecord loadingRe = new MKOLSearchRecord();
                    loadingRe.cityName = "正在下载";
                    loadingRe.childCities = loadingList;
                    listAll.add(loadingRe);
                }
                if (finishList.size() > 0) {
                    MKOLSearchRecord finRe = new MKOLSearchRecord();
                    finRe.cityName = "已完成";
                    finRe.childCities = finishList;
                    listAll.add(finRe);
                }
            }
        } else {
            SwitchHead.select_type = SwitchHead.SELECT_RIGHT;
            lin_wifi.setVisibility(View.GONE);
            MKOLSearchRecord currRe = new MKOLSearchRecord();
            currRe.cityName = "当前城市";
            currRe.childCities = currentList;
            listAll.add(currRe);
            MKOLSearchRecord reRe = new MKOLSearchRecord();
            reRe.cityName = "热门城市";
            reRe.childCities = hotCityList;
            listAll.add(reRe);
            MKOLSearchRecord allRe = new MKOLSearchRecord();
            allRe.cityName = "所有城市";
            allRe.childCities = offlineCityList;
            listAll.add(allRe);
        }
        boolean needProgress = leftOrRight == SwitchHead.SELECT_LEFT ? true : false;
        AdapterCityOffline adapterCityOffline = new AdapterCityOffline(getApplicationContext(),listAll,offlineMap,needProgress);
        list_all.setAdapter(adapterCityOffline);
        for(int i=0;i<listAll.size();i++){
            list_all.expandGroup(i);
        }
    }

    public void initEvents() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ActivityOfflineMap.this.finish();
            }
        });
        title_head.SetOnSelectedListener(new SwitchHead.OnSelectedListener() {
            @Override
            public void onSelect(int selectWay) {
                if (selectWay == SwitchHead.SELECT_LEFT) {
                    switchView(SwitchHead.SELECT_LEFT);
                } else if (selectWay == SwitchHead.SELECT_RIGHT) {
                    switchView(SwitchHead.SELECT_RIGHT);
                }
            }
        });
        img_wifi_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wifiOff = (Boolean) img_wifi_auto.getTag();
                wifiOff = !wifiOff;
                if (wifiOff) {
                    img_wifi_auto.setImageResource(R.drawable.switch_off);
                    Toast.makeText(getApplicationContext(), "WIFI自动更新已关闭", Toast.LENGTH_SHORT).show();
                } else {
                    img_wifi_auto.setImageResource(R.drawable.switch_on);
                    Toast.makeText(getApplicationContext(), "WIFI自动更新已开启", Toast.LENGTH_SHORT).show();
                }
                img_wifi_auto.setTag(wifiOff);
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("OfflineMapWifiOff", String.valueOf(wifiOff));
            }
        });
    }
    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:// 离线地图下载更新事件类型
                MKOLUpdateElement update = offlineMap.getUpdateInfo(state);
                ViewCityItem.runningElement = update;
                ViewCityItem.runningTime = System.currentTimeMillis();
                if (!ViewCityItem.isRunning) {
                    allUpdateInfo = offlineMap.getAllUpdateInfo();
                    if (title_head.select_type == SwitchHead.SELECT_LEFT)
                        switchView(SwitchHead.SELECT_LEFT);
                    ODispatcher.dispatchEvent(OEventName.MAP_OFFLINE_DATACHANGE, update);//更新item数据
                    ViewCityItem.isRunning = true;
                }
                break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                break;
        }
    }
    // ==============================================================
//
    public void handleListChange(int switchType) {
        Message message = new Message();
        message.what = 912;
        message.arg1 = switchType;
        handler.sendMessage(message);
    }
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.MAP_OFFLINE_LISTCHANGE)) {
            handleListChange(SwitchHead.select_type);
        }else if (eventName.equals(OEventName.MAP_OFFLINE_START_DOWNLOAD)) {
            if(SwitchHead.select_type == SwitchHead.SELECT_RIGHT){
                handleListChange(SwitchHead.SELECT_LEFT);
            }
        }
    }
    //    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 911:
                    ToastResult.getInstance().show(title_head, true, null);
                    break;
                case 912:
                    switchView(msg.arg1);
                    break;
            }
        }
    }
    //=========================================================
    @Override
    protected void onDestroy() {
        ViewCityItem.isRunning = false;
        ViewCityItem.runningElement = null;
        offlineMap.destroy();
        super.onDestroy();
    }
}
