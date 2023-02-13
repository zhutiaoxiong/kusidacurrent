package common.map.offline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.listview.ListViewNoScroll;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.toast.OToastButton;

/**
 * 下载的城市item
 */
public class ViewCityItem extends LinearLayout implements OEventObject, OCallBack {
    public static MKOLUpdateElement runningElement;
    public static long              runningTime;//if time < now - 8 stopcount;
    public static boolean isRunning = false;//false允许实时下载事件
    private MKOLUpdateElement element;
    private MKOLSearchRecord  data;
    public  TextView          city_name, city_size, download_info;
    public ImageView img_download, img_dropdown;
    public ListViewNoScroll list_child;
    public TextView         progress_bg, progress_cover;

    private boolean      needProgress;
    private MKOfflineMap offlineMap;
    private MyHandler handler = new MyHandler();
    private CountDownTimer countDownTimer;
    public ViewCityItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.map_offline_item, this, true);
        city_name = (TextView) findViewById(R.id.city_name);
        city_size = (TextView) findViewById(R.id.city_size);
        download_info = (TextView) findViewById(R.id.download_info);
        img_download = (ImageView) findViewById(R.id.img_download);
        img_dropdown = (ImageView) findViewById(R.id.img_dropdown);
        list_child = (ListViewNoScroll) findViewById(R.id.list_child);
        progress_bg = (TextView) findViewById(R.id.progress_bg);
        progress_cover = (TextView) findViewById(R.id.progress_cover);
        initEvents();
        ODispatcher.addEventListener(OEventName.MAP_OFFLINE_DATACHANGE, this);
//        ODispatcher.addEventListener(OEventName.LIST_VIEW_SIZE_CHANGE, this);
    }
    @Override
    protected void onDetachedFromWindow() {
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = null;
        offlineMap = null;
        super.onDetachedFromWindow();
    }
    public void setData(MKOLSearchRecord data, MKOfflineMap offlineMap, boolean needProgress) {
        this.data = data;
        this.offlineMap = offlineMap;
        this.needProgress = needProgress;
        element = ActivityOfflineMap.getUpdateCity(data.cityID);
        city_name.setText(data.cityName);
        city_size.setText(formatDataSize(data.size));
        handleChangeData();
        if (element != null && runningElement != null) {
            if (element.cityID == runningElement.cityID) {
                if (countDownTimer == null) countDownTimer = new CountDownTimer(80000, 1000) {
                    @Override
                    public void onTick(long l) {
                        if (ViewCityItem.runningTime < System.currentTimeMillis() - 10000) {//10秒没数据了
                            countDownTimer.cancel();
                        } else {
                            if (element != null && runningElement != null) {
                                if (element.cityID == runningElement.cityID)
                                    element = runningElement;
                            }
                            handleChangeData();
                        }
                    }
                    @Override
                    public void onFinish() {

                    }
                };
                countDownTimer.start();
            }
        }
    }
    private String formatDataSize(int size) {
        if (size == 0) return "";
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

    public void initEvents() {
        //点击开始下载
        img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                offlineMap.start(data.cityID);
                ActivityOfflineMap.allUpdateInfo = offlineMap.getAllUpdateInfo();
                ODispatcher.dispatchEvent(OEventName.MAP_OFFLINE_START_DOWNLOAD);
            }
        });
        //点击显示子城市
        img_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                if (list_child.getAdapter() != null) {//有显示变无显示
                    img_dropdown.setImageResource(R.drawable.arrow_white_triangle_down);
                    list_child.setAdapter(null);
                } else {//无显示变有显示
//                     if (BuildConfig.DEBUG) Log.e("meatureH","pre list_child: "+ViewCityItem.this.getHeight());
//                     if (BuildConfig.DEBUG) Log.e("meatureH","pre ViewCityItem.this: "+ViewCityItem.this.getHeight());
                    AdapterCityOfflineChild adapterChild = new AdapterCityOfflineChild(getContext(), data.childCities, offlineMap,needProgress);
                    list_child.setAdapter(adapterChild);
//                    ListViewNoScroll.setListViewHeightBasedOnChildren(list_child,0);
////                    list_child.measure(MeasureSpec.AT_MOST,MeasureSpec.AT_MOST);
////                    ViewCityItem.this.measure(MeasureSpec.AT_MOST,MeasureSpec.AT_MOST);
                    img_dropdown.setImageResource(R.drawable.arrow_white_triangle_up);
//                     if (BuildConfig.DEBUG) Log.e("meatureH","end list_child: "+ViewCityItem.this.getHeight());
//                     if (BuildConfig.DEBUG) Log.e("meatureH","end ViewCityItem.this: "+ViewCityItem.this.getHeight());
//                    ODispatcher.dispatchEvent(OEventName.LIST_VIEW_SIZE_CHANGE,data.cityID);
                }
//                int changeH = ListViewNoScroll.setListViewHeightBasedOnChildren(list_child,0);
            }
        });
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element == null) return;//未下载过
                if (runningElement != null && element.cityID == runningElement.cityID)
                    element = runningElement;
                String[] arr;
                String   add = "";
                if (element.update == true) {
                    arr = new String[]{"下载"};
                } else if (0 < element.ratio && element.ratio < 100 && element.status != element.SUSPENDED) {
                    arr = new String[]{ "删除","暂停下载"};
                } else if (0 < element.ratio && element.ratio < 100 && element.status == element.SUSPENDED) {
                    arr = new String[]{ "删除","继续下载"};
                } else {
                    arr = new String[]{"删除"};
                }
                OToastButton.getInstance().show(city_name, arr, "operate", ViewCityItem.this);
                OToastButton.getInstance().setCancleBtnTxtColor();
            }
        });
    }
    @Override
    public void callback(String key, Object value) {
        if (key.equals("operate")) {
            String opp = (String) value;
            if (opp.equals("下载")) {
                offlineMap.start(data.cityID);
            } else if (opp.equals("删除")) {
                offlineMap.remove(data.cityID);
            } else if (opp.equals("暂停下载")) {
                offlineMap.pause(data.cityID);
            } else if (opp.equals("继续下载")) {
                offlineMap.start(data.cityID);
            }
            isRunning = false;
            ActivityOfflineMap.allUpdateInfo = offlineMap.getAllUpdateInfo();
            element = runningElement = ActivityOfflineMap.getUpdateCity(data.cityID);
            if (opp.equals(("下载")) || opp.equals("删除")) {
                ODispatcher.dispatchEvent(OEventName.MAP_OFFLINE_LISTCHANGE);
            } else {
                ODispatcher.dispatchEvent(OEventName.MAP_OFFLINE_DATACHANGE, element);
            }
        }
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.MAP_OFFLINE_DATACHANGE)) {
            if (data.childCities != null) return;
            MKOLUpdateElement elementNow = (MKOLUpdateElement) paramObj;
            if (elementNow == null) return;//还未下载
            if (elementNow.cityID == data.cityID) {
                element = elementNow;
                Looper.prepare();
                if (countDownTimer == null) countDownTimer = new CountDownTimer(80000, 1000) {
                    @Override
                    public void onTick(long l) {
                        if (ViewCityItem.runningTime < System.currentTimeMillis() - 10000) {//10秒没数据了
                            countDownTimer.cancel();
                        } else {
                            if (element != null && runningElement != null) {
                                if (element.cityID == runningElement.cityID)
                                    element = runningElement;
                            }
                            handleChangeData();
                        }
                    }
                    @Override
                    public void onFinish() {

                    }
                };
                countDownTimer.start();
                Looper.loop();
            }
        }
//        else if(eventName.equals(OEventName.LIST_VIEW_SIZE_CHANGE)){
//            int cityId = (Integer)paramObj;
//            if(cityId!= data.cityID){
//                handlerHideChild();
//            }
//        }
    }
    public void handleChangeData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 101;
                handler.sendMessage(message);
            }
        }).start();
    }
    //    private void handlerHideChild(){
//        Message message = new Message();
//        message.what = 102;
//        handler.sendMessage(message);
//    }
    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    //其它先不显示
                    download_info.setText("");
                    progress_bg.setVisibility(View.INVISIBLE);
                    progress_cover.setVisibility(View.INVISIBLE);
                    img_download.setVisibility(View.INVISIBLE);
                    img_dropdown.setVisibility(View.INVISIBLE);
                    if (data.childCities != null) {//是省份
                        img_dropdown.setVisibility(View.VISIBLE);
                        img_download.setVisibility(View.INVISIBLE);
                    } else {//是城市
                        if (element == null) {//是无数据地图
                            img_download.setVisibility(View.VISIBLE);
                        } else {//有实时数据地图
                             if (BuildConfig.DEBUG) Log.e("download", "id:" + element.cityID + " update:" + element.update + " ratio:" + element.ratio + " status:" + element.status);
                            if (element.update == true) {
                                img_download.setVisibility(View.VISIBLE);
                                return;
                            }
                            if (element.ratio < 100 && element.status < element.FINISHED && needProgress) {
                                progress_bg.setVisibility(View.VISIBLE);
                                progress_cover.setVisibility(View.VISIBLE);
                                int                         bgW    = progress_bg.getWidth();
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) progress_cover.getLayoutParams();
                                if (element.ratio == 0) {
                                    params.width = 1;
                                } else {
                                    params.width = bgW * element.ratio / 100;
                                }
                                progress_cover.setLayoutParams(params);
                            }
                            if (element.status == element.WAITING) {
                                if(needProgress){
                                    download_info.setText("等待下载 0%");
                                }else{
                                    download_info.setText("正在下载");
                                }
                            } else if (element.status == element.DOWNLOADING && element.ratio < 100) {
                                if(needProgress){
                                    download_info.setText("正在下载 " + element.ratio + "%");
                                }else{
                                    download_info.setText("正在下载");
                                }
                            } else if (element.status >= element.FINISHED || element.ratio == 100) {
                                download_info.setText("已完成");
                            } else if (element.status == element.SUSPENDED) {
                                download_info.setText("已暂停");
                            } else {
                                progress_bg.setVisibility(View.INVISIBLE);
                                progress_cover.setVisibility(View.INVISIBLE);
                                download_info.setText("已完成");
                            }
                        }
                    }
                    break;
                case 102:
                    list_child.setAdapter(null);
                    break;
            }
        }
    }

}
