package view.view4me.carmanage;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class MyLocationListener extends BDAbstractLocationListener {
    public static String myCity;

    @Override
    public void onReceiveLocation(BDLocation location) {
        String addr = location.getAddrStr();    //获取详细地址信息
        String country = location.getCountry();    //获取国家
        String province = location.getProvince();    //获取省份
        String city = location.getCity();    //获取城市
        String district = location.getDistrict();    //获取区县
        String street = location.getStreet();    //获取街道信息
        String provinceSplit="";
        if(!TextUtils.isEmpty(province)){
             provinceSplit=province.substring(0,province.length()-1);
        }
        String citySplit="";
        if(!TextUtils.isEmpty(city)){
            citySplit=city.substring(0,city.length()-1);
        }
        String theCity = "";
        if(!TextUtils.isEmpty(provinceSplit)&&!TextUtils.isEmpty(citySplit)){
            theCity=provinceSplit+"-"+citySplit;
        }
        myCity=theCity;
        Log.e("取自己定位", "mCity"+myCity);
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
