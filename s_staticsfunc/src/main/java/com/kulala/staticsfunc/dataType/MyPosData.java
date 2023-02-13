package com.kulala.staticsfunc.dataType;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.BuildConfig;
import com.kulala.staticsfunc.exception.MyNaviException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class MyPosData implements Parcelable {
    public static final int TYPE_DATA_IS_BAIDU = 105;
    public static final int TYPE_DATA_IS_GAODE = 106;
    private int type;
    private double latitude;
    private double longitude;
    private String addressAB = "";//地址缩写
    private String addressDetail = "";//地址详情
    private double distance;

    public MyPosData(double lat, double lon,String addrAB,String addrDT,int dataType) {
        this.type = dataType;
        this.latitude = lat;
        this.longitude = lon;
        this.addressAB = addrAB == null ? "" :addrAB;
        this.addressDetail = addrDT == null ? "" :addrDT;
    }
    public void setDistance(double distance){
        this.distance=distance;
    }
    public double getDistance(){
        return distance;
    }


    public MyPosData() {
    }
    //=================================================================
    //外部用来检测数据是否有效
    public boolean isHavePosData(){
        if(latitude == 0 || longitude == 0)return false;
        return true;
    }
    public void setPosFromBaidu(double lat, double lon){
        type = TYPE_DATA_IS_BAIDU;
        this.latitude = lat;
        this.longitude = lon;
    }
    public void setPosFromGaode(double lat, double lon){
        type = TYPE_DATA_IS_GAODE;
        this.latitude = lat;
        this.longitude = lon;
    }
    public String Latlng2Str(int dataType){
        if(type == dataType){
        }else if(dataType == TYPE_DATA_IS_BAIDU){
            gd_2_bd(this.latitude,this.longitude);
        }else{
            bd_2_gd(this.latitude,this.longitude);
        }
        String result = String.valueOf(this.latitude);
        result += ",";
        result += String.valueOf(this.longitude);
        return result;
    }

    public void setPosStr2Pos(int dataType,String latLng,String address)throws MyNaviException{
        setAddressAB(address);
        setAddressDetail(address);
        if (latLng == null || "".equals(latLng)) throw new MyNaviException("坐标值错！");
        String[] st = latLng.split(",");
        if(st == null || st.length!=2) throw new MyNaviException("坐标值错！");
        try {
            this.latitude = Double.valueOf(st[0]);
            this.longitude = Double.valueOf(st[1]);
            this.type = dataType;
        }catch (Exception e){
            throw new MyNaviException("坐标值错！");
        }
    }
    public double getLatitude(int dataType) {
        if(type == dataType){
        }else if(dataType == TYPE_DATA_IS_BAIDU){
            gd_2_bd(this.latitude,this.longitude);
        }else{
            bd_2_gd(this.latitude,this.longitude);
        }
        return latitude;
    }
    public double getLongitude(int dataType) {
        if(type == dataType){
        }else if(dataType == TYPE_DATA_IS_BAIDU){
            gd_2_bd(this.latitude,this.longitude);
        }else{
            bd_2_gd(this.latitude,this.longitude);
        }
        return longitude;
    }
    public String getAddressAB() {
        return addressAB;
    }
    public void setAddressAB(String addrAB) {
        this.addressAB = addrAB == null ? "" :addrAB;
    }
    public String getAddressDetail() {
        return addressDetail;
    }
    public void setAddressDetail(String addrDT) {
        this.addressDetail = addrDT == null ? "" :addrDT;
    }
    /**高德转百度*/
    private void gd_2_bd(double gg_lat, double gg_lon)
    {
        double x = gg_lon, y = gg_lat;
        double z = sqrt(x * x + y * y) + 0.00002 * sin(y * PI);
        double theta = atan2(y, x) + 0.000003 * cos(x * PI);
        this.longitude = z * cos(theta) + 0.0065;
        this.latitude = z * sin(theta) + 0.006;
        this.type = TYPE_DATA_IS_BAIDU;
    }
    /**百度转高德*/
    private void bd_2_gd(double bd_lat, double bd_lon)
    {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * PI);
        double theta = atan2(y, x) - 0.000003 * cos(x * PI);
        this.longitude = z * cos(theta);
        this.latitude = z * sin(theta);
        this.type = TYPE_DATA_IS_GAODE;
    }
    //============================json部分==============================
    public static JsonArray toJsonArray(List<MyPosData> list){
        Gson      gson = new Gson();
        String    json = gson.toJson(list);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        return arr;
    }
    public static List<MyPosData> fromJsonArray(JsonArray arr) {
        if(arr == null)return null;
        List<MyPosData> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            JsonObject object = arr.get(i).getAsJsonObject();
            MyPosData data = MyPosData.fromJsonObject(object);
            if( data.isHavePosData())list.add(data);
        }
        return list;
    }
    public static MyPosData fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        MyPosData thisobj = gson.fromJson(obj, MyPosData.class);
        return thisobj;
    }
    //==========================================================
    public static final Creator<MyPosData> CREATOR = new Creator<MyPosData>() {
        @Override
        public MyPosData createFromParcel(Parcel in) {
            return new MyPosData(in);
        }

        @Override
        public MyPosData[] newArray(int size) {
            return new MyPosData[size];
        }
    };
    protected MyPosData(Parcel in) {
         if (BuildConfig.DEBUG) Log.e("b","a");
        type = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        addressAB = in.readString();
        addressDetail = in.readString();
    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        var1.writeInt(this.type);
        var1.writeDouble(this.latitude);
        var1.writeDouble(this.longitude);
        var1.writeString(this.addressAB);
        var1.writeString(this.addressDetail);
    }

    public boolean equals(Object var1) {
        if (var1 == null) {
            return false;
        } else if (this.hashCode() == var1.hashCode()) {
            return true;
        } else if (!(var1 instanceof MyPosData)) {
            return false;
        } else {
            MyPosData var2 = (MyPosData) var1;
            return Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(var2.latitude)
                    && Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(var2.longitude)
                    && this.addressAB.equals(var2.addressAB)
                    && this.addressDetail.equals(var2.addressDetail)
                    && this.type == var2.type;
        }
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return "lat/lng: (" + this.latitude + "," + this.longitude + ")" + " address:"+this.addressAB;
    }
}
