package common.map;

import com.baidu.location.Address;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.orhanobut.logger.Logger;

import java.text.NumberFormat;

import common.GlobalContext;
import view.basicview.MyLog;

/**
 * Created by Administrator on 2017/3/15.
 * 1.获取自己手机定位坐标 searchCurrentPos getPrePos
 * 2.获取坐标对应的地址 searchAddressByPos
 */

public class MapPosGet {
    private static DataPos prePos;//前次搜索的位置
    private static Address preAddress;//前次所有信息
    private static long preSearchTime = 0;//前次的时间
    private static LocationClient          mLocClient;
    private static OnCurrentPosGetListener listener;
    private static OnAddressGetListener onAddressGetListener;
    //=======================自己坐标搜索=================================
    public static int getPreCityId(){
        if(preAddress == null)return 0;
        return Integer.valueOf(preAddress.cityCode);
    }
    /**有可能为空*/
    public static Address getPreAddress(){
        return preAddress;
    }
    /**有可能为空*/
    public static DataPos getPrePos(){
//        searchCurrentPos(null);
        return prePos;
    }
    /**开始搜索一次自己坐标*/
    public static void searchCurrentPos(OnCurrentPosGetListener listener1){
        if(prePos!=null && listener1!=null){
            listener1.onCurrentPosGet(prePos);
            listener = null;
        }else {
            listener = listener1;
        }
        long now = System.currentTimeMillis();
        if(prePos!=null && now - preSearchTime < 3000)return;//5秒内不重复搜
        preSearchTime = now;
        getCurrentLocation();
    }
//    private static void getCurrentLocation(){
//        if(GlobalContext.getCurrentActivity()!=null){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                int permissionNet = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);//网络定位
//                //网络定位权限
//                if (permissionNet != PackageManager.PERMISSION_GRANTED) {
//                    GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//                }else {
//                    getCurrentLocationNext();
//                }
//            }else{
//                getCurrentLocationNext();
//            }
//        }
//    }

    private static void getCurrentLocation(){
        MyLog.loge("查看位置","这个再拿位置");
                getCurrentLocationNext();
    }
    private static void getCurrentLocationNext(){
//        String   provider = LocationManager.GPS_PROVIDER;// 指定LocationManager的定位方法
//        Location location = locationManager.getLastKnownLocation(provider);
        LocationClient.setAgreePrivacy(true);
        try {
            LocationClient.setAgreePrivacy(true);
            mLocClient = new LocationClient(GlobalContext.getCurrentActivity());
            mLocClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation loc) {
                    if(loc.getAddress().address == null || loc.getAddress().address.length()==0){
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "获取手机定位失败!");
                        return;
                    }
                    preAddress = loc.getAddress();
                    DataPos prePos1 = new DataPos();
                    prePos1.address = loc.getAddress().address;
                    prePos1.addressName = loc.getAddress().address;
                    prePos1.pos = new LatLng(loc.getLatitude(),loc.getLongitude());
                    mLocClient.stop();
                    if(listener != null && prePos1 ==null){
                        listener.onCurrentPosGet(prePos1);
                    }
                    prePos = prePos1;
//                ODispatcher.dispatchEvent(OEventName.GLOBAL_SHOW_LOG,"SELFPOS:"+prePos.pos.toString());
                }
                @Override
                public void onConnectHotSpotMessage(String arg0, int arg1) {
                }
            });//注册定位监听接口
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); //打开GPRS
            option.setIsNeedAddress(true);
            option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
            option.setScanSpan(3000); //设置发起定位请求的间隔时间为5000ms
            option.disableCache(false);//禁止启用缓存定位
            mLocClient.setLocOption(option);  //设置定位参数
            mLocClient.start();  // 调用此方法开始定位
        } catch (Exception e) {

        }

    }
    public interface OnCurrentPosGetListener{
        void onCurrentPosGet(DataPos posData);
    }
    //=======================根据坐标搜索地址=================================
    /**开始搜索一次自己坐标*/
    public static void searchAddressByPos(LatLng pos,OnAddressGetListener listener){
        if(pos == null)return;
        onAddressGetListener = listener;
        final GeoCoder mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                DataPos posData = new DataPos();
                posData.pos = geoCodeResult.getLocation();
                posData.address = geoCodeResult.getAddress();
                posData.addressName = geoCodeResult.getAddress();
                if(onAddressGetListener!=null)onAddressGetListener.onAddressGet(posData);
                if (mSearch != null) mSearch.destroy();
            }
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                DataPos posData = new DataPos();
                posData.pos = reverseGeoCodeResult.getLocation();
                posData.address = reverseGeoCodeResult.getAddress();
                posData.addressName = reverseGeoCodeResult.getSematicDescription();
                if(onAddressGetListener!=null)onAddressGetListener.onAddressGet(posData);
                if (mSearch != null) mSearch.destroy();
            }
        });
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(pos));// 执行geo
    }
    public interface OnAddressGetListener{
        void onAddressGet(DataPos posData);
    }

    /**计算两点之间距离*/
    public static String getLatLngDistance(LatLng start, LatLng end){
        if(start == null || end == null)return "0";
        double lat1 = (Math.PI/180)*start.latitude;
        double lat2 = (Math.PI/180)*end.latitude;
        double lon1 = (Math.PI/180)*start.longitude;
        double lon2 = (Math.PI/180)*end.longitude;
        double R = 6371.004;//地球半径
        //两点间距离 m，如果想要米的话，结果*1000就可以了
        double dis =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;
        NumberFormat nFormat = NumberFormat.getNumberInstance();  //数字格式化对象
        if(dis < 1){//当小于1千米的时候用,用米做单位保留一位小数
            nFormat.setMaximumFractionDigits(1);    //已可以设置为0，这样跟百度地图APP中计算的一样
            dis *= 1000;
            return nFormat.format(dis)+"m";
        }else{
            nFormat.setMaximumFractionDigits(2);
            return nFormat.format(dis)+"km";
        }
    }
    public static float getZoomLevel(LatLng start, LatLng end){
        float zoom=15;
      double disTance=  DistanceUtil. getDistance(start, end);
      if(0<disTance&&disTance<=5){
          zoom=21;
      }else  if(5<disTance&&disTance<=10){
          zoom=20;
      }else  if(10<disTance&&disTance<=20){
          zoom=19;
      }else  if(20<disTance&&disTance<=50){
          zoom=18;
      }else  if(50<disTance&&disTance<=100){
          zoom=17;
      }else  if(100<disTance&&disTance<=200){
          zoom=16;
      }else  if(200<disTance&&disTance<=500){
          zoom=15;
      }else  if(500<disTance&&disTance<=1000){
          zoom=14;
      }else  if(1000<disTance&&disTance<=2000){
          zoom=13;
      }else  if(2000<disTance&&disTance<=5000){
          zoom=12;
      }else  if(5000<disTance&&disTance<=10000){
          zoom=11;
      }else  if(10000<disTance&&disTance<=20000){
          zoom=10;
      }else  if(20000<disTance&&disTance<=25000){
          zoom=9;
      }else  if(25000<disTance&&disTance<=50000){
          zoom=8;
      }else  if(50000<disTance&&disTance<=100000){
          zoom=7;
      }else  if(100000<disTance&&disTance<=200000){
          zoom=6;
      }else  if(200000<disTance&&disTance<=500000){
          zoom=5;
      }else  if(500000<disTance&&disTance<=1000000){
          zoom=4;
      }
        return zoom;
    }
}
