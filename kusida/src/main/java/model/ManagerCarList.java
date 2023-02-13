package model;

import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.ArrayList;
import java.util.List;

import annualreminder.view.clip.PopChooseCarWarp;
import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import model.loginreg.DataUser;
import view.EquipmentManager;
import view.view4control.MiniDataIsShowLock;
import view.view4me.myblue.LcdManagerCarStatus;

import static com.kulala.linkscarpods.blue.ConvertHexByte.bytesToHexString;

public class ManagerCarList {
    private List<DataCarInfo> carInfoList;
    private List<DataCarInfo> carInfoListOther;//其它人的车辆列表
    private long currentCarId;
    private DataCarStatus currentCarStatus;
    private DataCarStatus netcurrentCarStatus;
    // ========================out======================
    private static ManagerCarList _instance;
    public DataCarInfo currentPayCar;
    public static ManagerCarList getInstance() {
        if (_instance == null)
            _instance = new ManagerCarList();
        return _instance;
    }
    // ========================out======================

    public DataCarStatus getCurrentCarStatus() {
        if(currentCarStatus == null){
            if (BuildConfig.DEBUG) Log.e("查询车辆状态", "為空創建一個吧把id複製");
            currentCarStatus = new DataCarStatus();
        }
//        currentCarStatus.carId  = getCurrentCarID();
        return currentCarStatus;
    }
    public DataCarStatus getNetCurrentCarStatus() {
        if(netcurrentCarStatus == null){
            if (BuildConfig.DEBUG) Log.e("查询车辆状态", "為空創建一個吧把id複製");
            netcurrentCarStatus = new DataCarStatus();
        }
//        currentCarStatus.carId  = getCurrentCarID();
        return netcurrentCarStatus;
    }

    // ========================out======================

    public void saveCarInfo(JsonObject carinfo, String fromWhere) {
        if (BuildConfig.DEBUG) Log.e("PanelCar", "保存车辆信息"+fromWhere+carinfo.toString());
        DataCarInfo carInfo = DataCarInfo.fromJsonObject(carinfo);
        if (carInfo == null) return;
//        currentPayCar=carInfo;
        if (carInfoList == null) {
            carInfoList = new ArrayList<DataCarInfo>();
            carInfoList.add(carInfo);
        } else {
            for (int i = 0; i < carInfoList.size(); i++) {
                if (carInfo != null && carInfoList.get(i) != null) {
                    if (carInfo.ide == carInfoList.get(i).ide) {
                        carInfoList.remove(i);
                        carInfoList.add(carInfo);
                    }
                }
            }
        }
        saveCarListLocal();
    }

    // ==========================
    // local=================================
    public List<DataCarInfo> getCarInfoList() {
        if (carInfoList == null || carInfoList.size() == 0) {
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
            long userId = user == null ? 0 : user.userId;
            String result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(userId, "carList");
            if (result == null || result.length() == 0) {
                carInfoList = new ArrayList<DataCarInfo>();
                return carInfoList;
            }
            JsonObject obj = ODBHelper.convertJsonObject(result);
            if (obj != null) {
                JsonArray arr = OJsonGet.getJsonArray(obj, "carList");
                carInfoList = DataCarInfo.fromJsonArray(arr);
            }
        }
        return carInfoList;
    }

    public void saveCarListLocal() {
        if (carInfoList == null) return;
        JsonObject obj = new JsonObject();
        JsonArray arr = DataCarInfo.toJsonArrayLocal(carInfoList);
        obj.add("carList", arr);
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user != null) {
            ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(user.userId, "carList", ODBHelper.convertString(obj));
        }
    }
    public void saveCarListLocal(List<DataCarInfo> currentList) {
        if (currentList == null) return;
        JsonObject obj = new JsonObject();
        JsonArray arr = DataCarInfo.toJsonArrayLocal(currentList);
        obj.add("carList", arr);
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user != null) {
            ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(user.userId, "carList", ODBHelper.convertString(obj));
        }
    }

    // ==========================get=================================
    public List<PopChooseCarWarp.DataCarChoose> getCarAnnualList() {
        List<PopChooseCarWarp.DataCarChoose> list = new ArrayList<PopChooseCarWarp.DataCarChoose>();
        if (carInfoList == null) return list;
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.isActive == 1) {
                PopChooseCarWarp.DataCarChoose data = new PopChooseCarWarp().new DataCarChoose();
                data.carId = car.ide;
                data.carLogo = car.logo;
                data.carName = car.num;
                list.add(data);
            }
        }
        return list;
    }

    public List<DataCarInfo> getCarListActive() {
        if (carInfoList == null) return new ArrayList<DataCarInfo>();
        List<DataCarInfo> list = new ArrayList<DataCarInfo>();
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.isActive == 1) list.add(car);
        }
        return list;
    }

    public String[] getCarNameListOther() {
        if (carInfoListOther == null) return new String[]{};
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < carInfoListOther.size(); i++) {
            DataCarInfo car = carInfoListOther.get(i);
            if (car.isActive == 1) list.add(car.num);
        }
        return list.toArray(new String[list.size()]);
    }

    public String[] getCarNameListActive() {
        if (carInfoList == null) return new String[]{};
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.isActive == 1) list.add(car.num);
        }
        return list.toArray(new String[list.size()]);
    }
    public String[] getCarNameListActiveAndIsSelf() {
        if (carInfoList == null) return new String[]{};
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.isActive == 1&&car.isMyCar==1) list.add(car.num);
        }
        return list.toArray(new String[list.size()]);
    }

    public String[] getCarNameListAll() {
        if (carInfoList == null || carInfoList.size() == 0) {
            return new String[]{};
        }
        String[] arr = new String[carInfoList.size()];
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            arr[i] = car.num;
        }
        return arr;
    }

    public long getCarIdByName(String carName) {
        if (carInfoList == null) return -1;
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.num.equals(carName)) {
                return car.ide;
            }
        }
        return -1;
    }

    public DataCarInfo getCarByNameOther(String carName) {
        if (carInfoListOther == null) return null;
        for (int i = 0; i < carInfoListOther.size(); i++) {
            DataCarInfo car = carInfoListOther.get(i);
            if (car.num.equals(carName)) {
                return car;
            }
        }
        return null;
    }

    public DataCarInfo getCarByName(String carName) {
        if (carInfoList == null) return null;
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.num.equals(carName)) {
                return car;
            }
        }
        return null;
    }

    //only for viewcar
    public DataCarInfo getCarByID(long carId) {
        if (carInfoList == null) return new DataCarInfo();
        for (int i = 0; i < carInfoList.size(); i++) {
            DataCarInfo car = carInfoList.get(i);
            if (car.ide == carId) {
                return car;
            }
        }
        return new DataCarInfo();
    }

    public long getCurrentCarID() {//不要缓存，重等数据不会刷新
        return currentCarId;
    }

    public DataCarInfo getCurrentCar() {//不要缓存，重等数据不会刷新
        boolean haveCar = false;
        if (getCarInfoList() == null) {
            return new DataCarInfo();
        }
        for (DataCarInfo car : carInfoList) {
            if (car.ide == currentCarId) {
                return car;
            }
        }
        if (!haveCar && carInfoList.size() > 0) {
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
            if (user != null) {
                String id = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(user.userId, "currentCarID");
                if (id != null && id.length() > 0) {
                    for (DataCarInfo car1 : carInfoList) {
                        if (car1.ide == Long.valueOf(id)) {
                            currentCarId = car1.ide;
                            return car1;
                        }
                    }
                }
            }
            return carInfoList.get(0);
        }
        return new DataCarInfo();
    }

    // ==========================get=================================
    public void setCurrentCar(long carId) {
        if (carInfoList == null) return;
        boolean haveCar = false;
        for (DataCarInfo car : carInfoList) {
            if (car.ide == carId) {
                currentCarId = carId;
                haveCar = true;
                ODispatcher.dispatchEvent(OEventName.CHANGE_CURRENT_CAR_TO_MANAGER);

                DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
                if (user != null) {
                    ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(user.userId, "currentCarID", "" + currentCarId);
                }
                BlueLinkReceiver.getInstance().needChangeCar(car.ide,car.bluetoothName,car.blueCarsig,car.isBindBluetooth,car.carsig,car.isMyCar);

            }
        }
        if (!haveCar) carId = 0;
    }

    public void setCurrentCarByName(String carname) {
        if (carInfoList == null) return;
        for (DataCarInfo car : carInfoList) {
            if (car.num.equals(carname)) {
                setCurrentCar(car.ide);
                return;
            }
        }
    }

    // ==========================set=================================
    public void exit() {
        carInfoList = new ArrayList<DataCarInfo>();
        saveCarListLocal();
    }

    /**
     * 车辆数据，不包括车辆状态的
     */
    public void saveCarList(JsonArray carInfos, String fromPosForTest) {
        if (carInfos != null) {
            this.carInfoList = DataCarInfo.fromJsonArray(carInfos);
        } else {
            this.carInfoList = new ArrayList<DataCarInfo>();
        }
        saveCarListLocal();
    }

    public void saveCarListOtherPeople(JsonArray carInfos) {
        this.carInfoListOther = DataCarInfo.fromJsonArray(carInfos);
    }
    private int count;
    public void saveCarStatus(JsonObject carStatus,String from) {
         if (BuildConfig.DEBUG) Log.e("saveCarStatus",from+carStatus.toString());
        DataCarInfo carInfo = getCurrentCar();
        DataCarStatus getStatus = DataCarStatus.fromJsonObject(carStatus);
        if(getStatus==null||carInfo.ide !=getStatus.carId)return;
         if (BuildConfig.DEBUG) Log.e("传输车辆状态", "socket收到发给activity状态字符串 "+carStatus.toString());
        ODispatcher.dispatchEvent(OEventName.SEND_CAR_STATUS_NET_OR_SOCKET,carStatus.toString());
        DataCarStatus localStatus=ManagerCarList.getInstance().getCurrentCarStatus();
         if (BuildConfig.DEBUG) Log.e("比较车辆状态", "getStatus.isLock=: "+getStatus.isLock+"localStatus.isLock="+localStatus.isLock);
        if(EquipmentManager.isMini()||EquipmentManager.isShouweiSix()){
            if(MiniDataIsShowLock.isLockChange==-1){
                count++;
            }
            if (BuildConfig.DEBUG) Log.e("mini状态改变", "MiniDataIsShowLock.isLockChange "+MiniDataIsShowLock.isLockChange+"getStatus.isLock"+getStatus.isLock);
            if(MiniDataIsShowLock.isLockChange!=getStatus.isLock&&MiniDataIsShowLock.isLockChange!=-1){
                if(getStatus.isLock==0){
                    if (BuildConfig.DEBUG) Log.e("mini状态改变","执行开锁" );
                    if(BlueLinkReceiver.getIsBlueConnOK()){
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_TOAST_SCALE, "车辆开锁");
                    }
                }else{
                    if (BuildConfig.DEBUG) Log.e("mini状态改变","执行关锁" );
                    if(BlueLinkReceiver.getIsBlueConnOK()){
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_TOAST_SCALE, "车辆关锁");
                    }
                }
            }
            if(MiniDataIsShowLock.isLockChange==-1){
                if(count==2){
                    MiniDataIsShowLock.isLockChange=getStatus.isLock;
                    count=0;
                }
            }else{
                MiniDataIsShowLock.isLockChange=getStatus.isLock;
            }
        }
         if(CarStatusChangeUtil.matchDataChange(getStatus,localStatus)){
            //車輛狀態改變發送到液晶鑰匙
             if (BuildConfig.DEBUG) Log.e("比较车辆状态", "状态不一样已发送");
//            LcdManagerCarStatus.sendCarStatus(getStatus);
            BlueLinkReceiver.getInstance().sendMessagelcd(bytesToHexString( LcdManagerCarStatus.ObjectToByteBlue(getStatus)),true);
            if(CarStatusChangeUtil.machLockChange(getStatus,localStatus)){
                currentCarStatus = getStatus;
            }
        }
        currentCarStatus = getStatus;
        if(from.equals("scmd_3")||from.equals("backdata_1219_getCarState")){
            netcurrentCarStatus=getStatus;
        }
        if (BuildConfig.DEBUG) Log.e("查询车辆状态", DataCarStatus.toJsonObject(currentCarStatus).toString());
    }

}
