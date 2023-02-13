package ctrl;

import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import common.http.HttpConn;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;

import model.ManagerCarList;
import model.carlist.DataCarInfo;

/**
 * 100-299
 */
public class OCtrlBlueTooth {
    private long lastUnBondCarid = 0;
    // ========================out======================
    private static OCtrlBlueTooth _instance;

    private OCtrlBlueTooth() {
        init();
    }

    public static OCtrlBlueTooth getInstance() {
        if (_instance == null)
            _instance = new OCtrlBlueTooth();
        return _instance;
    }

    // ========================out======================
    protected void init() {
    }

    // ========================out======================
    public void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
        switch (protocol) {
            case 1236:
                backdata_1236_bluetoothBond_GetCarSin(obj,CACHE_ERROR);
                break;
            case 1237:
                backdata_1237_bluetoothUnBondOK(obj,CACHE_ERROR);
                break;
            case 10001:
                backdata_10001_bind_factory_test_result(obj,CACHE_ERROR);
                break;
            case 10002:
                backdata_10002_bind_mini_result(obj,CACHE_ERROR);
                break;
            case 10003:
                backdata_10003_unbind_mini_result(obj,CACHE_ERROR);
                break;
        }
    }
    // ============================ccmd==================================

    /**
     * 连接蓝牙成功，提交服务器，取验证串
     **/
    public void ccmd1236_bluetoothBond_GetCarSin(long carId, String bluetoothName) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("bluetoothName", bluetoothName);
        DataCarInfo car = ManagerCarList.getInstance().getCarByID(carId);
        if(car!=null && car.isBindBluetooth == 0) {
            HttpConn.getInstance().sendMessage(data, 1236);//没绑定才发包
        }
    }

    /**
     * 蓝牙解绑，提交服务器
     **/
    public void ccmd1237_bluetoothUnBondOK(long carId) {
        lastUnBondCarid = carId;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        HttpConn.getInstance().sendMessage(data, 1237);
    }


    /**
     * 工厂测试用 10001
     **/
    public void ccmd_10001_bind_factory_test(String name,long carId,String blueCarSig) {
        JsonObject data = new JsonObject();
        data.addProperty("name", name);
        data.addProperty("carId", carId);
        data.addProperty("blueCarSig", blueCarSig);
        HttpConn.getInstance().sendMessage(data, 10001);
    }

    /**
     * 绑定蓝牙mini版 10002
     **/
    public void ccmd_10002_bind_mini(String name,long carId,String city) {
        JsonObject data = new JsonObject();
        data.addProperty("name", name);
        data.addProperty("carId", carId);
        data.addProperty("city", city);
        HttpConn.getInstance().sendMessage(data, 10002);
    }



    /**
     * 解绑蓝牙mini版 10003
     **/
    public void ccmd_10003_unbind_mini(long carId) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        HttpConn.getInstance().sendMessage(data, 10003);
    }


    // ============================scmd==================================

    /**
     * 绑定蓝牙,取carsign
     **/
    public void backdata_1236_bluetoothBond_GetCarSin(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1236_bluetoothBondOK");
            ODispatcher.dispatchEvent(OEventName.GET_CARSIGN_RESULTBACK);//刷新me界面
        }else{//已被绑定了
            ODispatcher.dispatchEvent(OEventName.BLUE_BOUND_FAILED);
        }
    }

    /**
     * 蓝牙解绑，提交服务器
     **/
    public void backdata_1237_bluetoothUnBondOK(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1237_bluetoothUnBondOK");
            ODispatcher.dispatchEvent(OEventName.BLUE_BOUND_CANCEL);
        }
    }

    /**
     * 绑定蓝牙10001
     **/
    public void backdata_10001_bind_factory_test_result(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject productMini=  OJsonGet.getJsonObject(obj,"productMini");
        }
    }

    /**
     * 绑定蓝牙10002
     **/
    public void backdata_10002_bind_mini_result(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject carInfo=  OJsonGet.getJsonObject(obj,"carInfo");
            ODispatcher.dispatchEvent(OEventName.MINI_BIND_RESULT,carInfo);
        }
    }

    /**
     * 绑定蓝牙,取carsign
     **/
    public void backdata_10003_unbind_mini_result(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.MINI_UNBIND_RESULT);
        }
    }

}
