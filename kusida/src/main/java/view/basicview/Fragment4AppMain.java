package view.basicview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.FragmentBase;
import com.orhanobut.logger.Logger;
import com.tencent.tauth.TencentCommon;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import common.LoadPermissions;
import common.global.OWXShare;
import common.map.DataCarTime;
import common.map.DataPos;
import common.map.FullScreenMap;
import common.map.MapPosGet;
import ctrl.OCtrlGps;
import model.ManagerCarList;
import model.ManagerSwitchs;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import model.demomode.DemoMode;
import model.maintain.ManagerMaintainList;
import model.status.DataSwitch;
import view.ActivityKulalaMain;
import view.EquipmentManager;
import view.find.OToastButtonBlackStyle;
import view.view4app.carpath.OToastSharePath;
import view.view4control.CheckUpGradeUtil;
import view.view4me.ResizableImageView;

public class Fragment4AppMain extends FragmentBase {
    private FullScreenMap map_view;//需要动态加载
    private TextView      txt_address_name, txt_address;
    private ImageView img_movetocar;
    private TextView fuchezhu_view;
    private RelativeLayout master_layout;
    private RelativeLayout upgrade_layout;
    private ResizableImageView up_grade_view;

    public static int pageId = -1;
    private DataCarTime cacheDataCartime;
    private boolean isFirst;
    private LatLng tianAnMenpoint =  new LatLng(39.91582, 116.403406);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("control生命周期", " Fragment4AppMain onCreateView: ");
        //        if (SystemMe.isPad(getActivity())) {
//            return inflater.inflate(R.layout.view_app_list_pad, container, false);
//        } else {
        return inflater.inflate(R.layout.view_app_list, container, false);
//        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("control生命周期", " Fragment4AppMain onViewCreated: ");
        WindowManager wm    = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int           width = wm.getDefaultDisplay().getWidth();
        ManagerMaintainList.getInstance().width = width;
//        //下面用for循环进去findviewid
//        for (int i = 0; i <=4; i++) {
//            int             id   = getResources().getIdentifier("item" + i, "id", getActivity().getPackageName());
//            ViewAppListItem item = (ViewAppListItem) view.findViewById(id);
//            item.setData(i);
//        }
        map_view = view.findViewById(R.id.map_view);
        txt_address_name = view.findViewById(R.id.txt_address_name);
        txt_address = view.findViewById(R.id.txt_address);
        img_movetocar = view.findViewById(R.id.img_movetocar);
        fuchezhu_view= view.findViewById(R.id.fuchezhu_view);
        master_layout= view.findViewById(R.id.master_layout);
        upgrade_layout= view.findViewById(R.id.upgrade_layout);
        up_grade_view=view.findViewById(R.id.up_grade_view);
        Logger.d("onViewCreated");

        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CHANGE_CURRENT_CAR_TO_MANAGER,this);
        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE,this);
        ODispatcher.addEventListener(OEventName.CAR_SELECT_CHANGE, this);
        ODispatcher.addEventListener(OEventName.GPS_CARPOS_PAGECHANGE_RESULT_BACK, this);

        MapPosGet.searchCurrentPos(null);
        DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
        if(currentCar.isMyCar == 1) {
            if (DemoMode.getIsDemoMode()) {
                OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 1);
            } else {
                OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 0);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            handleChangeData();
            searchLocation();
        }
        Log.e("control生命周期", " Fragment4AppMain onHiddenChanged: "+hidden);
    }
    private void searchLocation(){
        DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
        DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
        Log.e("我是一只小青龙","status.gpsOpen"+status.gpsOpen+"currentCar.isActive"+currentCar.isActive+"currentCar.isMyCar"+currentCar.isMyCar);
        if( ActivityKulalaMain.getPAGEPOS() == 1
                && currentCar.isActive == 1 && currentCar.isMyCar == 1) {
            if (DemoMode.getIsDemoMode()) {
                OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 1,true);
            } else {
                Log.e("我是一只小青龙","1");
                OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 0,true);
            }
            FullScreenMap.setMapPreZoomDeaufalt();
        }else{
            txt_address_name.setText("无法获取车辆定位!");//显示车位置
            txt_address.setText("");//显示车位置
            FullScreenMap.setMapPreZoomTianAnmen();
            map_view.moveToPos(tianAnMenpoint);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    private long secondCount = 0;
    private long takeSelfLocationCount;
    private long reFreshTime;
    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if(eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)){
            long currentTimeMy=System.currentTimeMillis();
            if((currentTimeMy-reFreshTime)>1*1000){
                reFreshTime=currentTimeMy;
                MyLog.loge("查看位置","刷新2页面UI");
                boolean isAppForground=   CheckForgroundUtils.isAppForeground();
                if(isAppForground){
                    MyLog.loge("查看位置","刷新2页面UI前台");
                    if(EquipmentManager.isMini()){
                        return;
                    }
                    DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
                    DataCarInfo currentCar=ManagerCarList.getInstance().getCurrentCar();
                    if(currentCar==null||currentCar.isActive==0){
                        MyLog.loge("查看位置","车为空");
                        myHandler.obtainMessage(HANDLE_SETTO_NO_CAR).sendToTarget();
                        return;
                    }
                    if(carStatus.carId!=ManagerCarList.getInstance().getCurrentCarID()){
                        MyLog.loge("查看位置","车状态ID不等于当前id");
                        return;
                    }
                    if(FragmentActionBar.currentPos!=1 && carStatus.getGps()!=null){
                        //已取到坐标，在其它页不刷
                        MyLog.loge("查看位置","已取到坐标或者在其他页面");
                        return;
                    }
                    List<DataSwitch> useSwitchList = ManagerSwitchs.getInstance().getSwitchPrivates();
                    if(useSwitchList!=null&&useSwitchList.size()>=1){
                        DataSwitch dataSwitch=useSwitchList.get(0);
                        if(dataSwitch.isOpen==0){
                            myHandler.obtainMessage(HANDLE_SETTO_NO_CAR).sendToTarget();
//                    showSelf();
                            return;
                        }
                    }
                    secondCount ++;
                    //2秒一次自己位置
                    if(secondCount%2 == 0){
                        long currentTime= System.currentTimeMillis();

                        if((currentTime-takeSelfLocationCount)>4900){
                            takeSelfLocationCount=currentTime;
                            MyLog.loge("查看位置","取自己的位置");
//                            Logger.d("取自己的位置");
                            MapPosGet.searchCurrentPos(null);
                        }
                    }
                    //3秒刷一次图
                    if(secondCount<=4){
                        if(secondCount%2 == 0){
                            myHandler.obtainMessage(HANDLE_RESET_MAP_INFO).sendToTarget();
                        }
                    }else{
                        if(secondCount%9 == 0){
                            myHandler.obtainMessage(HANDLE_RESET_MAP_INFO).sendToTarget();
                        }
                    }

                    //10秒一次车位置
                    if(secondCount%5 == 0){
//                        Logger.d("取车的位置");
                        DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
                        if(ActivityKulalaMain.getPAGEPOS() == 1
                                && currentCar.isActive == 1 && currentCar.isMyCar == 1) {
                            if (DemoMode.getIsDemoMode()) {
                                OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 1);
                            } else {
                                OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 0);
                            }
                        }
                    }
                }
            }
        }else if(eventName.equals(OEventName.CHANGE_CURRENT_CAR_TO_MANAGER)){
            //当前车变了
            myHandler.obtainMessage(CHANGE_CURRENT_CAR_TO_MANAGER).sendToTarget();
        }else if(eventName.equals(OEventName.CAR_SELECT_CHANGE)){
            setPage();
        }else if(eventName.equals(OEventName.GPS_CARPOS_PAGECHANGE_RESULT_BACK)){
            myHandler.obtainMessage(HANDLE_RESET_MAP_INFO).sendToTarget();
        }
    }
    private void setPage(){
        Message message=Message.obtain();
        message.what=147;
        myHandler.sendMessage(message);
    }
//    private void showSelf(){
//        Message message=Message.obtain();
//        message.what=149;
//        myHandler.sendMessage(message);
//    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("control生命周期", " Fragment4AppMain onResume: ");
        if(FragmentActionBar.currentPos == 1) {
            //是否打开gps,在点actionbar时判定，未开也能进页面
            DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
            if (currentCar == null || currentCar.ide == 0) {
                myHandler.obtainMessage(HANDLE_SETTO_NO_CAR).sendToTarget();
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "未选择车辆");
                return;
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("control生命周期", "Fragment4AppMain onStop: ");
    }

    @Override
    public void initViews() {
        pageId = 0;
        handleChangeData();
    }

    @Override
    public void initEvents() {
        img_movetocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map_view != null) {
                    FullScreenMap.setMapPreZoomDeaufalt();
                    map_view.moveToCar();
                    map_view.showInfoWindow();
                    img_movetocar.setImageResource(R.drawable.applist_carpos_center);
                }
            }
        });
        up_grade_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckUpGradeUtil.toTaoBaoOrLiulanQi();
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putString(ActivityWeb.TITLE_NAME, "");
//                bundle.putString(ActivityWeb.HTTP_ADDRESS, "http://manage.kcmoco.com//mekongPlatfrom/html/index/reserve_carPods.html");
//                intent.putExtras(bundle);
//                intent.setClass(GlobalContext.getCurrentActivity(), ActivityUpGrade.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                GlobalContext.getContext().startActivity(intent);
//                Intent intent = new Intent();
//                intent.setClass(GlobalContext.getCurrentActivity(), ActivityUpGrade.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                GlobalContext.getContext().startActivity(intent);
            }
        });
    }
    // ===============================================

    @Override
    public void invalidateUI() {
        showPage();
    }

    // ==============================================================
    private static final int     HANDLE_RESET_MAP_INFO     = 18649;
    private static final int     HANDLE_SETTO_NO_CAR       = 18652;
    private static final int     CHANGE_CURRENT_CAR_TO_MANAGER       = 18653;
//    private long setCurrentLocationTime;
    // ===================================================
    @SuppressLint("HandlerLeak")
    private              Handler myHandler                 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_SETTO_NO_CAR://无车的显示
                    txt_address_name.setText("无法获取车辆定位!");
                    txt_address.setText("");
                    if (map_view == null)return;
                    map_view.clearOverlay();
                    map_view.clearMarker();
                    map_view.clearInfoWindow();
                    break;
                case CHANGE_CURRENT_CAR_TO_MANAGER:
                    if (map_view == null) return;
                    map_view.clearOverlay();
                    map_view.clearMarker();
                    map_view.clearInfoWindow();
                    break;
                case HANDLE_RESET_MAP_INFO:
                    /**刷新检测*/
                    //无效判定
                    if (!LoadPermissions.isOpenGps(getActivity()))return;
                    if (map_view == null) return;
                    DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                    DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
                    if (currentCar == null || currentCar.ide == 0 || carStatus == null)return;
                    //1.设车位置
                    DataCarTime timeData = new DataCarTime();
                    timeData.carID = currentCar.ide;
                    timeData.carName = currentCar.num;
                    timeData.carPos = carStatus.getGps();
                    timeData.isStart = carStatus.isStart;
                    timeData.logo = currentCar.logo;
                    if (carStatus.startTime == 0) {//防止时间给错，就不提示时间
                        timeData.time = System.currentTimeMillis();
                    } else {
                        timeData.time = carStatus.startTime;
                    }
//                    map_view.clearOverlay();//window也会清掉
                    map_view.placeCarTimeWindow(timeData);//自带clear
                    int direction = carStatus.direction;
                    DataPos selfPos = MapPosGet.getPrePos();
                    if(selfPos==null){
                        map_view.placeCar(timeData.carPos, direction, "", false);
                    }else{
                        map_view.placeSelfAndCar(selfPos.pos,timeData.carPos, direction);
                    }
                    mapSetListener();
                    //2.设自己位置
                    LatLng gps = carStatus.getGps();
                    if (gps == null) return;
                    MapPosGet.searchAddressByPos(carStatus.getGps(), new MapPosGet.OnAddressGetListener() {
                        @Override
                        public void onAddressGet(final DataPos address) {
                            if (address == null) return;
                            if(getActivity()!=null)getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt_address_name.setText(address.addressName);//显示车位置
//                                    txt_address.setText(address.address);
//                                    long currentTime=System.currentTimeMillis();
//                                    if((currentTime-setCurrentLocationTime)>10*1000){
//                                        setCurrentLocationTime=currentTime;
                                        MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
                                            @Override
                                            public void onCurrentPosGet(final DataPos posData) {
                                                if (posData == null) return;
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        String dis = MapPosGet.getLatLngDistance(address.pos, posData.pos);
                                                        txt_address.setText("爱车距您 " + dis+"\t\t\t"+address.address);//取了位置再设一次
                                                        if (BuildConfig.DEBUG) Log.e("AppMain", "HANDLE_RESET_ADDRESS_INFO OK");

                                                    }
                                                });
                                            }
                                        });
//                                    }
                                }
                            });
                        }
                    });
                    break;
                case 147:
                    showPage();
                    break;
                case 150:

                    break;
//                case 149:
//                    DataPos mySelfPos = MapPosGet.getPrePos();
//                    if(mySelfPos!=null){
//                        map_view.placeSelfPos(MapPosGet.getPrePos().pos,0,0);
//                    }
//                    break;
            }
        }

    };
    private void showPage(){
        DataCarInfo   car = ManagerCarList.getInstance().getCurrentCar();
        if(car!=null){
            if(car.isActive==0){
                master_layout.setVisibility(View.VISIBLE);
                fuchezhu_view.setVisibility(View.INVISIBLE);
                upgrade_layout.setVisibility(View.INVISIBLE);
            }else{
                if(car.isMyCar==0){
                    master_layout.setVisibility(View.INVISIBLE);
                    upgrade_layout.setVisibility(View.INVISIBLE);
                    fuchezhu_view.setVisibility(View.VISIBLE);
                    fuchezhu_view.setText("副车主无权访问此页面");
                }else{
                    if(EquipmentManager.isMini()){
                        master_layout.setVisibility(View.INVISIBLE);
                        fuchezhu_view.setVisibility(View.VISIBLE);
//                        upgrade_layout.setVisibility(View.VISIBLE);
                    fuchezhu_view.setText("当前设备不支持");
                    }else{
                        master_layout.setVisibility(View.VISIBLE);
                        fuchezhu_view.setVisibility(View.INVISIBLE);
                        upgrade_layout.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }
    private void mapSetListener() {
        map_view.setOnMapMoveListener(new FullScreenMap.OnMapMoveListener() {
            @Override
            public void onMapMove() {
                DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                if (currentCar == null || currentCar.ide == 0) return;
                img_movetocar.setImageResource(R.drawable.applist_carpos_outside);
            }
        });
        map_view.setOnClickShareListener(new FullScreenMap.OnClickShareListener() {
            @Override
            public void onClickShare(final DataCarTime dataCarTime) {
                final String   address        = txt_address_name.getText().toString().trim();
                ShareUrlSearch shareUrlSearch = ShareUrlSearch.newInstance();
                shareUrlSearch.setOnGetShareUrlResultListener(new OnGetShareUrlResultListener() {
                    @Override
                    public void onGetRouteShareUrlResult(ShareUrlResult result) {}
                    @Override
                    public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {}
                    @Override
                    public void onGetLocationShareUrlResult(ShareUrlResult result) {
                        final String shareUrl = result.getUrl();
                        OToastSharePath.getInstance().show(txt_address_name, "分享",
                                new OToastSharePath.OnClickButtonListener() {
                                    @Override
                                    public void onClick(int pos) {
                                        switch (pos) {
                                            case 1:
                                                OWXShare.SharePlace(address, shareUrl);
                                                break;
                                            case 2:
                                                OWXShare.SharePlace(address, shareUrl);
                                                break;
                                            case 3:
                                                TencentCommon.toTencent(getActivity(), getActivity().getString(R.string.cool_your_friends_to_share_a_place_to_you), address, shareUrl, 0, "");
                                                break;
                                            case 4:
                                                TencentCommon.toTencent(getActivity(), getActivity().getString(R.string.cool_your_friends_to_share_a_place_to_you), address, shareUrl, 1, "");
                                                break;
                                        }
                                    }
                                });

                    }
                });
                shareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
                        .location(dataCarTime.carPos).snippet("我的位置").name(address));
            }
            @Override
            public void onClickCollect(DataCarTime dataCarTime) {
                String address = txt_address_name.getText().toString().trim();
                OCtrlGps.getInstance().ccmd1216_favoritePos(dataCarTime.carPos.latitude + "," + dataCarTime.carPos.longitude, address);
            }

            @Override
            public void onClickNavi(DataCarTime dataCarTime) {
                cacheDataCartime=dataCarTime;
                List<String> list=getFinalList();
                if(list==null||list.size()==0){
                    Toast.makeText(GlobalContext.getContext(), "您尚未安装百度地图,高德地图，腾讯地图其中的任何一种", Toast.LENGTH_SHORT).show();
                }else{
                    String[] str = new String[list .size()];  //创建一个String型数组
                    list.toArray(str); //将List数组转为String数组
                    OToastButtonBlackStyle.getInstance().show(map_view,str , "navito", Fragment4AppMain.this, "导航");
                }
            }
        });
    }
     /*** 判断手机中是否安装指定包名的软件
     * @param context
     * @param pkgname 包名
     */
    public static boolean isInstallApk(Context context, String pkgname) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.equals(pkgname)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }

    /**
     * 跳转到百度地图
     * @param context
     * @param latitude 纬度
     * @param longtitude 经度
     * @param address 终点
     * */
    private void goBaiduMap(Context context,double latitude, double longtitude, String address) {
        if (isInstallApk(context, "com.baidu.BaiduMap")) {
            try {
                Intent intent = Intent.getIntent("intent://map/direction?destination=latlng:"
                        + latitude + ","
                        + longtitude + "|name:" + address + //终点：该地址会在导航页面的终点输入框显示
                        "&mode=driving&" + //选择导航方式 此处为驾驶
                        "region=" + //
                        "&src=#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (URISyntaxException e) {
                Log.e("goError", e.getMessage());
            }
        } else {
            Toast.makeText(context, "您尚未安装百度地图", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInsTallBaidu(){
        if (isInstallApk(GlobalContext.getContext(), "com.baidu.BaiduMap")) {
            return true;
        }
        return false;
    }
    private boolean isInsTallGaode(){
        if (isInstallApk(GlobalContext.getContext(), "com.autonavi.minimap")) {
            return true;
        }
        return false;
    }
    private boolean isInsTallTenxun(){
        if (isInstallApk(GlobalContext.getContext(), "com.tencent.map")) {
            return true;
        }
        return false;
    }
    private  List<String> getFinalList(){
        List<String> list=new ArrayList<>();
        if(isInsTallBaidu()&&isInsTallGaode()&&isInsTallTenxun()){
            list.add("腾讯导航");
            list.add("百度导航");
            list.add("高德导航");
        }else if(isInsTallBaidu()&&isInsTallGaode()&&!isInsTallTenxun()){
            list.add("百度导航");
            list.add("高德导航");
        }else if(isInsTallBaidu()&&!isInsTallGaode()&&isInsTallTenxun()){
            list.add("腾讯导航");
            list.add("高德导航");
        }else if(!isInsTallBaidu()&&isInsTallGaode()&&isInsTallTenxun()){
            list.add("腾讯导航");
            list.add("百度导航");
        }else if(!isInsTallBaidu()&&isInsTallGaode()&&!isInsTallTenxun()){
            list.add("高德导航");
        }else if(isInsTallBaidu()&&!isInsTallGaode()&&!isInsTallTenxun()){
            list.add("百度导航");
        }else if(!isInsTallBaidu()&&!isInsTallGaode()&&isInsTallTenxun()){
            list.add("腾讯导航");
        }
        return list;

    }

    @Override
    public void callback(String key, Object value) {
        if (key.equals("navito")) {
            String o = (String) value;
            if (o.equals("百度导航")) {
                if(cacheDataCartime!=null){
                    double latitude=cacheDataCartime.carPos.latitude;
                    double longtitude=cacheDataCartime.carPos.longitude;
                    goBaiduMap(GlobalContext.getContext(),latitude,longtitude,"");
                }
            } else if (o.equals("高德导航")) {
                LatLng endPoint = BD2GCJ(new LatLng(cacheDataCartime.carPos.latitude, cacheDataCartime.carPos.longitude));//坐标转换
//                StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=").append("amap");
//                stringBuffer.append("&lat=").append(endPoint.latitude)
//                        .append("&lon=").append(endPoint.longitude).append("&keywords=" + "")
//                        .append("&dev=").append(0)
//                        .append("&style=").append(2);
                StringBuffer stringBuffer = new StringBuffer("androidamap://route?sourceApplication=").append("amap");
                stringBuffer.append("&dlat=").append(endPoint.latitude)
                        .append("&dlon=").append(endPoint.longitude)
                        .append("&dname=").append("")
                        .append("&dev=").append(0)
                        .append("&t=").append(0);

                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
                intent.setPackage("com.autonavi.minimap");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (o.equals("腾讯导航")) {
                LatLng endPoint = BD2GCJ(new LatLng(cacheDataCartime.carPos.latitude, cacheDataCartime.carPos.longitude));//坐标转换
                StringBuffer stringBuffer = new StringBuffer("qqmap://map/routeplan?type=drive")
                        .append("&tocoord=").append(endPoint.latitude).append(",").append(endPoint.longitude).append("&to=" + "");
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GlobalContext.getContext().startActivity(intent);
            }
        }
        super.callback(key, value);
    }

    /**
     * BD-09 坐标转换成 GCJ-02 坐标
     */
    public static LatLng BD2GCJ(LatLng bd) {
        double x = bd.longitude - 0.0065, y = bd.latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);

        double lng = z * Math.cos(theta);//lng
        double lat = z * Math.sin(theta);//lat
        return new LatLng(lat, lng);
    }

    /**
     * GCJ-02 坐标转换成 BD-09 坐标
     */
    public static LatLng GCJ2BD(LatLng bd) {
        double x = bd.longitude, y = bd.latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        return new LatLng(tempLat, tempLon);
    }

//===============================end====================================
}
