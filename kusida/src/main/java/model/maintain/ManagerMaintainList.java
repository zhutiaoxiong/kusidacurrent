package model.maintain;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.List;

import common.GlobalContext;
import model.ManagerCarList;
import model.ManagerLoginReg;
import model.loginreg.DataUser;


/**
 * Created by qq522414074 on 2016/10/31.
 */
public class ManagerMaintainList {
    public List<DataMaintain> maintenanceInfos;//保养数据列表
    public DataMaintain maintain;//服务端收到的消息
    public int back = 0;//从保养提醒页面,增加提醒页面到选车页面的返回
    public String mileage = "";
    public String mainTaintime = "";
    public  int width;

    // ========================out======================
    private static ManagerMaintainList _instance;

    private ManagerMaintainList() {
    }

    public static ManagerMaintainList getInstance() {
        if (_instance == null)
            _instance = new ManagerMaintainList();
        return _instance;
    }

    public void saveMainTainceInfoList(JsonArray maintenanceInfoss) {
        this.maintenanceInfos = DataMaintain.fromJsonArray(maintenanceInfoss);
    }
    public void saveMainTainceInfoListLocal() {
        JsonObject obj1 = new JsonObject();
        JsonArray simpleArr = ManagerMaintainList.toJsonArrayLocal(maintenanceInfos);
        obj1.add( ManagerCarList.getInstance().getCurrentCar().ide + "maintenanceInfos", simpleArr);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo(ManagerCarList.getInstance().getCurrentCar().ide + "maintenanceInfos", ODBHelper.convertString(obj1));
    }

    public void loadMaintainListLocal() {
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo(ManagerCarList.getInstance().getCurrentCar().ide + "maintenanceInfos");
        JsonObject obj1 = ODBHelper.convertJsonObject(result);
        JsonArray arr1 = OJsonGet.getJsonArray(obj1, ManagerCarList.getInstance().getCurrentCar().ide + "maintenanceInfos");
        maintenanceInfos = DataMaintain.fromJsonArray(arr1);
    }

    public  void savehasMaintance(int hasMaintance) {
        DataUser use=ManagerLoginReg.getInstance().getCurrentUser();
        if(use==null)return;
        ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(use.userId,"hasMaintance", String.valueOf(hasMaintance));
    }

    public int gethasMaintance() {
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId,"hasMaintance");
       int hasMaintance= ODBHelper.queryResult2Integer(result,0);
        return hasMaintance;
    }

    public static JsonArray toJsonArrayLocal(List<DataMaintain> list) {
        Gson gson = new Gson();
        JsonArray arr = null;
        if (list != null) {
            String json = gson.toJson(list);
            arr = gson.fromJson(json, JsonArray.class);
        }
        return arr;
    }
}
