package com.kulala.linkscarpods.blue;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kulala.linkspods.BuildConfig;

public class ManagerCurrentCarStatus {
    private static ManagerCurrentCarStatus _instance;
    public static ManagerCurrentCarStatus getInstance() {
        if (_instance == null)
            _instance = new ManagerCurrentCarStatus();
        return _instance;
    }
    private DataCarStatus dataCarStatus;
    public DataCarStatus getCarStatus(){
        return dataCarStatus;
    }
    public  void setCarStatus(String jsonstrStatus){
        Gson gson=new Gson();
       JsonObject object= gson.fromJson(jsonstrStatus,JsonObject.class);
        this.dataCarStatus=fromJsonObject(object);
    }
    public static DataCarStatus fromJsonObject(JsonObject obj) {
        Gson          gson    = new Gson();
        DataCarStatus thisobj;
        try {
            thisobj = gson.fromJson(obj, DataCarStatus.class);
            return thisobj;
        }catch (NumberFormatException e){
             if (BuildConfig.DEBUG) Log.e("DataCarStatus",e.toString()+"\nNumberFormatException:"+obj.toString());
            return null;
        }
    }

    public  void setCarStatus(DataCarStatus dataCarStatus){
        this.dataCarStatus=dataCarStatus;
    }
    public  void setIsOn(int isOn){
        if(this.dataCarStatus!=null){
            this.dataCarStatus.isON=isOn;
        }
    }

}
