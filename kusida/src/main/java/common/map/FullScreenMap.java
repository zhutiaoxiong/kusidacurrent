package common.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.BaiduZoomLevel;
import com.baidu.mapapi.search.core.PoiInfo;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.BuildConfig;
import com.kulala.staticsview.OnClickListenerMy;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import common.global.NAVI;
import view.view4app.carpath.OPoiOverlay;
/**
 * 百度地图之全屏地图,集成围栏，导航等功能
 * window:
 * 1.placeCarTimeWindow 放置车时长窗
 * 2.placeShareWindow   放置分享窗
 * overlay:
 * 1.clearOverlay 清除显示
 * 2.placeCar           放置车
 * setOnCarAddressListener 放置车同时获取车位置的中文地址
 * 3.placeArea          放置围栏
 * 4.placePath          放置路径
 * OnClickPosListener 点击了起点，终点，或车
 * 5.placeSelfPos        放置自己坐标
 * 6.placeMapClickPos    放置地图点击点
 * setOnClickMapListener  点击地图的地址,选取地图点击点
 */
public class FullScreenMap extends RelativeLayout {
    private static float mapPreZoom = 18;
    //    public static int IS_SHOW_SHARE_WINDOW = 1;//window要显示的状态
    private ImageView img_gotocar, img_gotoarea, img_gotoself;
    private RelativeLayout   lin_map;
    private MapView          baidumap_view;//动态加载才能去按扭
    private BitmapDescriptor markCar, markSelf, markStart, markStop;

    private DataCar_Area_Pos carPosInfo=new DataCar_Area_Pos();
    private LatLng           selfPos;//moveto need

    private BaiduMap map;
    private MyHandler handler = new MyHandler();
    private OnCarAddressListener onCarAddressListener;
    private OnClickPosListener   onClickPosListener;
    private OnClickMapListener   onClickMapListener;
    private OnClickShareListener onClickShareListener;
    private OnMapMoveListener    onMapMoveListener;

    private InfoWindow infowindow;
    private View       infowindowView;
    private WindowStartStopTime timeWindow;
    public FullScreenMap(Context context, AttributeSet attrs) {
        super(context, attrs);
//        SDKInitializer.initialize(java.lang.String sdcardPath, Context context)
        LayoutInflater.from(context).inflate(R.layout.full_scren_map, this, true);
        lin_map = (RelativeLayout) findViewById(R.id.lin_map);
        img_gotocar = (ImageView) findViewById(R.id.img_gotocar);
        img_gotoarea = (ImageView) findViewById(R.id.img_gotoarea);
        img_gotoself = (ImageView) findViewById(R.id.img_gotoself);
//        baidumap_view = (MapView) findViewById(R.id.baidumap_viewoo);
        BaiduMapOptions options = new BaiduMapOptions();
        options.zoomControlsEnabled(false);
        options.scaleControlEnabled(false);
        baidumap_view = new MapView(getContext(), options);
        RelativeLayout.LayoutParams params_map = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lin_map.addView(baidumap_view, params_map);
        initViews();
        initEvents();
    }
    public static void setMapPreZoomDeaufalt(){
        mapPreZoom=18;
    }
    public static void setMapPreZoomTianAnmen(){
        mapPreZoom=12;
    }
    /**
     * 清除地图
     */
    public void clearMarker() {
        markerCar=null;
        markerSelf=null;
    }
    private void initViews() {
//        if (!LoadPermissions.isOpenGps(getContext())) {
//            LoadPermissions.openGPS(getContext());
//        }
        // setDefault
        img_gotocar.setVisibility(INVISIBLE);
        img_gotoarea.setVisibility(INVISIBLE);
        img_gotoself.setVisibility(INVISIBLE);
        markCar = BitmapDescriptorFactory.fromResource(R.drawable.map_pos_car);
        markSelf = BitmapDescriptorFactory.fromResource(R.drawable.map_pos_self);
        markStart = BitmapDescriptorFactory.fromResource(R.drawable.map_path_start);
        markStop = BitmapDescriptorFactory.fromResource(R.drawable.map_path_stop);
        map = baidumap_view.getMap();
    }
    private void initEvents() {
        img_gotocar.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (!moveToCar()) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.the_gps_did_not_provide_a_vehicle_coordinates));
                }
            }
        });
        img_gotoarea.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (!moveToArea()) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.not_set_electronic_fence));
                }
            }
        });
        img_gotoself.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (!moveToSelf()) {//点击后显示为蓝图标
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "未设置自己定位!");
                }
            }
        });
        //点击地图取目的点坐标
        map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (onClickMapListener != null) {//进行坐标点搜索
                    MapPosGet.searchAddressByPos(latLng, new MapPosGet.OnAddressGetListener() {
                        @Override
                        public void onAddressGet(DataPos posData) {
                            onClickMapListener.onClickMap(posData);
                        }
                    });
                }
            }
            @Override
            public void onMapPoiClick(MapPoi mapPoi) {
                DataPos posData = new DataPos(mapPoi.getPosition(), mapPoi.getName());
                posData.addressName = mapPoi.getName();
                if (onClickMapListener != null) onClickMapListener.onClickMap(posData);
            }
        });
        map.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                map.hideInfoWindow();
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {//地图有移动过
                    img_gotoself.setImageResource(R.drawable.map_gotoself_away);//移动后显示为白图标
                    if (onMapMoveListener != null) onMapMoveListener.onMapMove();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {//点击
                }
            }
        });
        map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker mark) {//点击车辆或起终点
                LatLng pos = mark.getPosition();
                if (mark.getIcon().equals(markCar)) {//点车
                    if (infowindow != null) map.showInfoWindow(infowindow);
                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(pos);
                    map.setMapStatus(status);
                    if (onClickPosListener != null) {//进行坐标点搜索
                        MapPosGet.searchAddressByPos(pos, new MapPosGet.OnAddressGetListener() {
                            @Override
                            public void onAddressGet(DataPos posData) {
                                onClickPosListener.onClickPosCar(posData);
                            }
                        });
                    }
                } else if (mark.getIcon().equals(markStart)) {//点起点
                    if (infowindow != null) {
                        if (infowindowView != null)
                            infowindow = new InfoWindow(infowindowView, pos, -35);//-90 y offset
                        map.showInfoWindow(infowindow);
                    }
                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(pos);
                    map.setMapStatus(status);
                    if (onClickPosListener != null) {//进行坐标点搜索
                        MapPosGet.searchAddressByPos(pos, new MapPosGet.OnAddressGetListener() {
                            @Override
                            public void onAddressGet(DataPos posData) {
                                onClickPosListener.onClickPosStart(posData);
                            }
                        });
                    }
                } else if (mark.getIcon().equals(markStop)) {//点终点
                    if (infowindow != null) {
                        if (infowindowView != null)
                            infowindow = new InfoWindow(infowindowView, pos, -35);//-90 y offset
                        map.showInfoWindow(infowindow);
                    }
                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(pos);
                    map.setMapStatus(status);
                    if (onClickPosListener != null) {//进行坐标点搜索
                        MapPosGet.searchAddressByPos(pos, new MapPosGet.OnAddressGetListener() {
                            @Override
                            public void onAddressGet(DataPos posData) {
                                onClickPosListener.onClickPosStop(posData);
                            }
                        });
                    }
                }
                return false;
            }
        });

        map.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
            }
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mapPreZoom = mapStatus.zoom;
            }
        });
    }
    // ========================action===========================
    public void showInfoWindow() {
        if (infowindow != null) map.showInfoWindow(infowindow);
    }
    /**
     * 移到车位置
     *
     * @return if move failed "GPS未提供车辆坐标!"
     */
    public boolean moveToCar() {
        if(!canResetScale){
            mapPreZoom=18;
            canResetScale=true;
        }
        if (carPosInfo == null) return false;
        if (carPosInfo.carPos == null) return false;
        float           zoom    =  mapPreZoom;
        Logger.d("moveToCar"+zoom);
        MapStatusUpdate factory = MapStatusUpdateFactory.newLatLngZoom(carPosInfo.carPos, zoom);
        map.setMapStatus(factory);
         if (BuildConfig.DEBUG) Log.e("mapView","moveToCar");
        return true;
    }
    /**
     * 移到自已位置
     *
     * @return if move failed "未设置自己定位!"
     */
    private boolean moveToSelf() {
        Logger.d("moveToSelf");
        if (selfPos == null) return false;
        float           zoom    =mapPreZoom;
        MapStatusUpdate factory = MapStatusUpdateFactory.newLatLngZoom(selfPos, zoom);
        map.setMapStatus(factory);
        img_gotoself.setImageResource(R.drawable.map_gotoself_normal);//点击后显示为蓝图标
        return true;
    }

    /**
     * 移到围栏位置
     *
     * @return if move failed "未设置电子围栏!"
     */
    public boolean moveToArea() {
        if (carPosInfo == null) return false;
        if (carPosInfo.areaPos == null) return false;
        if (carPosInfo.areaOpen != 1 || carPosInfo.areaMeter <= 0) return false;
        int             meter   = carPosInfo.areaMeter * 1000;//米转为公里
        MapStatusUpdate factory = MapStatusUpdateFactory.newLatLngZoom(carPosInfo.areaPos, BaiduZoomLevel.getLevelFromMeter(meter));
        map.setMapStatus(factory);
        return true;
    }
    /**
     * 移到指定位置
     */
    public void moveToPos(LatLng pos) {
        Logger.d("moveToPos");
        if (pos == null) return;
        float           zoom    =mapPreZoom;
        MapStatusUpdate factory = MapStatusUpdateFactory.newLatLngZoom(pos, zoom);
        map.setMapStatus(factory);
    }
    // =========================设置地图要显示的元素==========================
    /**
     * 清除地图
     */
    public void clearOverlay() {
        carPosInfo = new DataCar_Area_Pos();
        Message msg = new Message();
        msg.what = 100;
        handler.sendMessage(msg);
    }
    public void clearInfoWindow(){
        Message msg = new Message();
        msg.what = 10000;
        handler.sendMessage(msg);
    }
    public void clearListener() {
        onCarAddressListener = null;
        onClickPosListener = null;
        onClickMapListener = null;
    }
    /**
     * 放置车
     * setOnClickPosListener
     */
    public void placeCar(LatLng carPos, int direction, String address) {
        placeCar(carPos, direction, address, true);
    }
    public void placeCar(final LatLng carPos, final int direction, final String address, final boolean needMoveIcon) {
        if (carPosInfo == null) carPosInfo = new DataCar_Area_Pos();
        carPosInfo.carPos = carPos;
        carPosInfo.direction = direction;
        carPosInfo.address = address;
        Message msg = new Message();
        msg.what = 101;
        msg.arg1 = needMoveIcon ? 0 : 1;//需要ICON 0，不需要 1
        handler.sendMessage(msg);
    }
    /**
     * 放置自身定位
     *
     * @param needMovetoCenter 是否需要把自已定位移至中心 0:不用 1:要
     */
    public void placeSelfPos(final LatLng selfPos, final int needMovetoCenter, final int needShowButton) {
        Message msg = new Message();
        msg.what = 104;
        msg.arg1 = needMovetoCenter;
        msg.arg2 = needShowButton;
        msg.obj = selfPos;
        handler.sendMessage(msg);
    }
    /**
     * 放置自身定位+车
     * canResetScale 控制是否可以再重定位缩放中心点
     */
    private Marker markerCar;
    private Marker markerSelf;
    public static boolean canResetScale = false;
    public void placeSelfAndCar(final LatLng selfPos,final LatLng carPos, final int direction) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(selfPos == null || carPos == null || map == null || markSelf == null || markCar == null)return;
                //放置自身定位
                if(markerSelf==null){
                    MarkerOptions selfOp = new MarkerOptions().position(selfPos).icon(markSelf).anchor(0.5f, 0.5f);
                    markerSelf=(Marker) map.addOverlay(selfOp);
                }else{
                    markerSelf.setPosition(selfPos);
                }
                //放置车
                carPosInfo.carPos = carPos;
                carPosInfo.direction = direction;
                float dirAngle = -1 * direction;
                if(markerCar==null){
                    MarkerOptions carOp = new MarkerOptions().position(carPos).rotate(dirAngle).icon(markCar).anchor(0.5f, 0.5f);
                    markerCar=(Marker) map.addOverlay(carOp);
                }else{
                    markerCar.setPosition(carPos);
                    markerCar.setRotate(dirAngle);
                }
                moveToCar();
//                if(!canResetScale) {
//                    //缩放
//                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                    builder.include(carPos);
//                    builder.include(selfPos);
//                    map.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build()));
//                    mapPreZoom= MapPosGet.getZoomLevel(selfPos,carPos);
//                    canResetScale = true;
//                }else{
//
//                }
            }
        });
    }
    /**
     * 放置围栏,是否成功显示,如果有车的情况显示包括二个
     */
    public void placeArea(final LatLng areaPos, final int areaMeter, final int areaOpen) {
        if (carPosInfo == null) carPosInfo = new DataCar_Area_Pos();
        carPosInfo.areaPos = areaPos;
        carPosInfo.areaMeter = areaMeter;
        carPosInfo.areaOpen = areaOpen;
        Message msg = new Message();
        msg.what = 102;
        handler.sendMessage(msg);
    }
    /**
     * 放置路径
     * setOnClickPosListener
     */
    public void placePath(final List<LatLng> path) {
        Message msg = new Message();
        msg.what = 103;
        msg.obj = path;
        handler.sendMessage(msg);
    }
    /**
     * 放置地图点击选取的点
     */
    public void placeMapClickPos(final LatLng mapclickPos) {
        Message msg = new Message();
        msg.what = 105;
        msg.obj = mapclickPos;
        handler.sendMessage(msg);
    }
    /**
     * 放置poi
     */
    public void placeMapPoi(List<PoiInfo> allPoi) {
        Message msg = new Message();
        msg.what = 106;
        msg.obj = allPoi;
        handler.sendMessage(msg);
    }
    /**
     * 放置车时长窗 inside thread,自带clear
     */
    public void placeCarTimeWindow(final DataCarTime dataCarTime) {
        Message msg = new Message();
        msg.what = 201;
        msg.obj = dataCarTime;
        handler.sendMessage(msg);
    }
    /**
     * 放置分享窗
     */
    public void placeShareWindow(boolean defaultShow, LatLng carPos, String carAddress, WindowPosShareEtc.ClickButtonListener listener) {
        Message msg = new Message();
        msg.what = 202;
        List<Object> li = new ArrayList<Object>();
        li.add(carPos);
        li.add(carAddress);
        li.add(listener);
        li.add(defaultShow);
        msg.obj = li;
        handler.sendMessage(msg);
    }
    //    new WindowPosShareEtc.ClickButtonListener() {
//        @Override
//        public void onError(String error) {
//            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,error);
//        }
//        @Override
//        public void onClickFavorite(LatLng favoritePos, String favoriteAddress) {
//            OCtrlGps.getInstance().ccmd1216_favoritePos(favoritePos.latitude+","+favoritePos.longitude, favoriteAddress);
//        }
//        @Override
//        public void onClickShare(String shareAddress,String shareUrl) {
//            OWXShare.SharePlace(shareAddress, shareUrl);
//        }
//        @Override
//        public void onClickNavorite(LatLng selfPos, LatLng carPos, String carPosAddress) {
//            OToastNavigate.getInstance().showOpenNavigate(img_gotoarea,selfPos, carPos, carPosAddress);
//        }
//    }
    //=========================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (baidumap_view == null) return;
                    if (map == null) map = baidumap_view.getMap();
                    map.clear();
                    map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    map.setTrafficEnabled(true);
                    break;
                case 10000:
                    if (baidumap_view == null) return;
                    if (map == null) map = baidumap_view.getMap();
                    map.hideInfoWindow();
                    infowindow = null;
                    break;
                case 101://放置车
                    if (baidumap_view == null) return;
                    if (map == null) map = baidumap_view.getMap();
                    if (carPosInfo == null) return;
                    float dirAngle = -1 * carPosInfo.direction;
                    if (carPosInfo.carPos == null || markCar == null) return;
                    if(markerCar==null){
                        MarkerOptions ooB = new MarkerOptions().position(carPosInfo.carPos).rotate(dirAngle).icon(markCar).anchor(0.5f, 0.5f);
                        markerCar= (Marker)map.addOverlay(ooB);
                    }else{
                        markerCar.setRotate(dirAngle);
                        markerCar.setPosition(carPosInfo.carPos);
                    }
                    if (msg.arg1 == 0) img_gotocar.setVisibility(VISIBLE);
                    moveToCar();
                    break;
                case 102://放置围栏
                    if (carPosInfo == null) return;
                    if (carPosInfo.areaOpen == 1) {
                        if (carPosInfo.areaPos != null && carPosInfo.areaMeter > 0) {
                            OverlayOptions ooCircle = new CircleOptions().center(carPosInfo.areaPos).fillColor(0x330000FF).stroke(new Stroke(3, 0x770000FF))
                                    .radius(carPosInfo.areaMeter * 1000);//米转为公里
                            if(map!=null){
                                map.addOverlay(ooCircle);
                                img_gotoarea.setVisibility(VISIBLE);
                                //并不是移至area,而是有车二个一起显示
                                LatLngBounds bounds = new LatLngBounds.Builder().include(carPosInfo.carPos).include(carPosInfo.areaPos).build();
                                int          dis    = (int) NAVI.getDistance(carPosInfo.carPos, carPosInfo.areaPos);
//			int usedis = dis > ManagerGps.areaMeter ? dis : ManagerGps.areaMeter;
                                int             usedis  = dis > carPosInfo.areaMeter * 1000 ? dis : carPosInfo.areaMeter * 1000;
                                MapStatusUpdate factory = MapStatusUpdateFactory.newLatLngZoom(bounds.getCenter(), BaiduZoomLevel.getLevelFromMeter(usedis));
                                map.setMapStatus(factory);
                            }
                            break;
                        }
                    }
                    img_gotoarea.setVisibility(INVISIBLE);
                    break;
                case 103://放置路径
                    List<LatLng> path = (ArrayList<LatLng>) msg.obj;
                    if (path == null || path.size() < 2) return;
                    LatLng startPos = path.get(0);
                    LatLng stopPos = path.get(path.size() - 1);

                    MarkerOptions opStart1 = new MarkerOptions().position(startPos).icon(markStart).anchor(0.5f, 20.5f);
                    MarkerOptions opEnd1 = new MarkerOptions().position(stopPos).icon(markStop).anchor(0.5f, 20.5f);
                    map.addOverlay(opStart1);
                    map.addOverlay(opEnd1);
                    LatLngBounds bounds = new LatLngBounds.Builder().include(startPos).include(stopPos).build();
                    OverlayOptions ooPolyline = new PolylineOptions().width(14).color(0x770000FF).points(path);
                    map.addOverlay(ooPolyline);
                    OverlayOptions ooPolylineUP = new PolylineOptions().width(6).color(0xFFFFDD66).dottedLine(true).points(path);
                    map.addOverlay(ooPolylineUP);
                    int dis = (int) NAVI.getDistance(startPos, stopPos);
                    MapStatusUpdate factory = MapStatusUpdateFactory.newLatLngZoom(bounds.getCenter(), BaiduZoomLevel.getLevelFromMeter(dis));
                    map.setMapStatus(factory);
                    break;
                case 104://放置自身定位
                    int needMovetoCenter = msg.arg1;
                    selfPos = (LatLng) msg.obj;
                    if (selfPos == null) return;
                    MarkerOptions ooC = new MarkerOptions().position(selfPos).icon(markSelf).anchor(0.5f, 0.5f);
                   if(map!=null){
                       map.addOverlay(ooC);
                   }
                    if(msg.arg2 == 1)img_gotoself.setVisibility(VISIBLE);
                    if (needMovetoCenter == 1) moveToSelf();
                    break;
                case 105:// 放置地图点击选取的点
                    LatLng mapclickPos = (LatLng) msg.obj;
                    if (mapclickPos == null) return;
                    MarkerOptions ooD = new MarkerOptions().position(mapclickPos).icon(markStop).anchor(0.5f, 20.5f);
                    map.addOverlay(ooD);
                    factory = MapStatusUpdateFactory.newLatLngZoom(mapclickPos, 18);
                    map.setMapStatus(factory);
                    break;
                case 106:// 放置poi
                    List<PoiInfo> allPoi = (List<PoiInfo>) msg.obj;
                    if (allPoi == null) return;
                    OPoiOverlay overlay = new OPoiOverlay(map);
                    map.setOnMarkerClickListener(overlay);
                    overlay.setData(allPoi);
                    overlay.addToMap();
                    // 缩放地图，使所有Overlay都在合适的视野内
//                    overlay.zoomToSpan();//此功能无效
                    break;
                case 201://放置车时长窗,自带clear
                    final DataCarTime dataCarTime = (DataCarTime) msg.obj;
                    if (dataCarTime == null) return;
                    if (map == null) return;
                    if(dataCarTime.carPos == null)return;
                    if(timeWindow == null)timeWindow = new WindowStartStopTime(getContext(), null);
                    timeWindow.setData(dataCarTime);
                    infowindow = new InfoWindow(timeWindow, dataCarTime.carPos, -35);//-90 y offset
                    infowindowView = timeWindow;
                     if (BuildConfig.DEBUG) Log.e("placeCarTime", "dataCarTime.carPos:" + dataCarTime.carPos);
                    map.showInfoWindow(infowindow);// 显示此infoWindow,点击时就关了，点车又开了
                    timeWindow.txt_collect.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onClickShareListener != null)
                                onClickShareListener.onClickCollect(dataCarTime);
                        }
                    });
                    timeWindow.txt_share.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onClickShareListener != null)
                                onClickShareListener.onClickShare(dataCarTime);
                        }
                    });
                    timeWindow.txt_navigate.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onClickShareListener != null)
                                onClickShareListener.onClickNavi(dataCarTime);
                        }
                    });
                    break;
                case 202://放置分享窗
                    List<Object> li = (ArrayList<Object>) msg.obj;
                    if (li == null || li.size() < 3) return;
                    LatLng carPos = (LatLng) li.get(0);
                    String carAddress = (String) li.get(1);
                    boolean defaultShow = (Boolean) li.get(3);
                    WindowPosShareEtc.ClickButtonListener listener = (WindowPosShareEtc.ClickButtonListener) li.get(2);
                    WindowPosShareEtc windowsh = new WindowPosShareEtc(getContext(), null);
                    DataPos data = new DataPos();
                    data.pos = carPos;
                    data.address = carAddress;
                    windowsh.setData(data, listener);
                    infowindow = new InfoWindow(windowsh, carPos, -35);
                    infowindowView = windowsh;
                    if (defaultShow) map.showInfoWindow(infowindow);// 显示此infoWindow,点击时就关了，点车又开了
                    break;
            }
        }
    }
    // ===================================================
    @Override
    protected void onAttachedToWindow() {
        if (baidumap_view != null) baidumap_view.onResume();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (map != null) map.clear();
        if (baidumap_view != null) baidumap_view.onDestroy();
        map = null;
        baidumap_view = null;
        super.onDetachedFromWindow();
    }
    // ===================================================
    public interface OnCarAddressListener {
        void onAddressResultBack(String address);
    }
    public void setOnCarAddressListener(OnCarAddressListener listener) {
        this.onCarAddressListener = listener;
    }
    public interface OnClickPosListener {
        void onClickPosCar(DataPos dataPos);

        void onClickPosStart(DataPos dataPos);

        void onClickPosStop(DataPos dataPos);
    }
    public void setOnClickPosListener(OnClickPosListener listener) {
        this.onClickPosListener = listener;
    }
    public interface OnClickMapListener {
        void onClickMap(DataPos dataPos);
    }
    public void setOnClickMapListener(OnClickMapListener listener) {
        this.onClickMapListener = listener;
    }
    public interface OnClickShareListener {
        void onClickShare(DataCarTime dataCarTime);

        void onClickCollect(DataCarTime dataCarTime);
        void onClickNavi(DataCarTime dataCarTime);
    }
    public void setOnClickShareListener(OnClickShareListener listener) {
        this.onClickShareListener = listener;
    }
    public interface OnMapMoveListener {
        void onMapMove();
    }
    public void setOnMapMoveListener(OnMapMoveListener listener) {
        this.onMapMoveListener = listener;
    }
}
