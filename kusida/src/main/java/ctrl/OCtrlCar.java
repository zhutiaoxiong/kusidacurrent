package ctrl;

import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkscarpods.blue.BlueLcdNet;
import com.kulala.linkscarpods.blue.BlueStaticValue;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.time.TimeDelayTask;
import com.kulala.staticsview.toast.ToastTxt;
import com.orhanobut.logger.Logger;


import java.util.List;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import common.http.HttpConn;
import model.ManagerCarList;
import model.ManagerLoginReg;
import model.ManagerWarnings;
import model.carlist.DataCarInfo;
import model.demomode.DemoMode;
import model.maintain.ManagerMaintainList;
import model.nfc.ManagerNfc;
import model.remotecontrol.ManagerRemoteControl;
import model.store.ManagerStore;
import model.store.ShopCarExamples;
import view.EquipmentManager;

import static ctrl.CarControlResult.CARCONTROL_SENDED;

/**
 * 100-299
 */
public class OCtrlCar {
    private static long     carId1219;
    private static long     carId1251;
    private static int     type1251;
    private static long     carId1252;
    private static String urlType;
    // ========================out======================
    private static OCtrlCar _instance;

    private OCtrlCar() {
        init();
    }

    public static OCtrlCar getInstance() {
        if (_instance == null)
            _instance = new OCtrlCar();
        return _instance;
    }

    protected void init() {
         if (BuildConfig.DEBUG) Log.e("查看动向", "走到init方法" );
        BlueLcdNet.getInstance().setoCtrl1233Listener(new BlueLcdNet.OCtrl1233Listener() {
            @Override
            public void ccmd1233_controlCar(com.kulala.linkscarpods.blue.DataCarInfo car, int controlCmd, int time) {
                 if (BuildConfig.DEBUG) Log.e("查看动向", "进这里了" );
                DataCarInfo carInfo=new DataCarInfo();
                carInfo.bluetoothName=car.bluetoothName;
                carInfo.ide=car.ide;
                carInfo.isBindBluetooth=car.isBindBluetooth;
                carInfo.carsig=car.carsig;
                OCtrlCar.this.ccmd1233_controlCar(carInfo, controlCmd,time);
                 if (BuildConfig.DEBUG) Log.e("查看动向", "发出去了" );
            }
        });
    }

    // ========================out======================
    public void processResult(int protocol, JsonObject obj, String CACHE_ERROR) {
        switch (protocol) {
            case 1201:
                backdata_1201_newrepairCar(obj, CACHE_ERROR);
                break;
            case 1202:
                backdata_1202_deleteCar(obj, CACHE_ERROR);
                break;
            case 1203:
                backdata_1203_getcarlist(obj, CACHE_ERROR);
                break;
            case 1204:
                backdata_1204_activatecar(obj, CACHE_ERROR);
                break;
            case 1219:
                backdata_1219_getCarState(obj, CACHE_ERROR);
                break;
            case 1221:
                backdata_1221_getWarninglist(obj, CACHE_ERROR);
                break;
            case 1220:
                backdata_1220_unactivatecar(obj, CACHE_ERROR);
                break;
            case 1226:
                backdata_1226_DemoMode(obj, CACHE_ERROR);
                break;
            case 1227:
                backdata_1227_getMaintainList(obj, CACHE_ERROR);
                break;
            case 1228:
                backdata_1228_AddModificationMaintainList(obj, CACHE_ERROR);
                break;
            case 1229:
                backdata_1229_DeleteMaintainList(obj, CACHE_ERROR);
                break;
            case 1230:
                backdata_1230_ConfirmMaintainList(obj, CACHE_ERROR);
                break;
            case 1231:
                backdata_1231_CompleteMaintainList(obj, CACHE_ERROR);
                break;
            case 1232:
                backdata_1232_getCarListFromPhone(obj, CACHE_ERROR);
                break;
            case 1233:
                backdata_1233_controlCar(obj, CACHE_ERROR);
                break;
            case 1238:
                backdata_1238_MantanceMode(obj, CACHE_ERROR);
                break;
            case 1248:
                backdata_1248_Change_Air_Condition(obj,CACHE_ERROR);
                break;
            case 1249:
                backdata_1249_bind_lcdKey(obj,CACHE_ERROR);
                break;
            case 1250:
                backdata_1250_LcdKey_autoOpen(obj,CACHE_ERROR);
                break;
            case 1251:
                backdata_1251_Car_Sound_Control(obj,CACHE_ERROR);
                break;
            case 1252:
                backdata_1252_middle_door_control(obj,CACHE_ERROR);
                break;
            case 2101:
                backdata_2101_queryNfc(obj,CACHE_ERROR);
                break;
            case 2102:
                backdata_2102_addOrDeleteNfc(obj,CACHE_ERROR);
                break;
            case 2103:
                backdata_2103_changeNfc(obj,CACHE_ERROR);
                break;
            case 2104:
                backdata_2104_reGetNfc(obj,CACHE_ERROR);
                break;
            case 2000:
                backdata_2000_pushresult(obj,CACHE_ERROR);
                break;
            case 2201:
                backdata_2201_apply_store_car(obj,CACHE_ERROR);
                break;
            case 2001:
                backdata_2001_tijiaoshenhe(obj,CACHE_ERROR);
                break;
            case 2202:
                backdata_2202_qurry_store_car(obj,CACHE_ERROR);
                break;
            case 2203:
                backdata_2203_change_car_store(obj,CACHE_ERROR);
                break;
            case 2105:
                backdata_2105_qurry_remote_control(obj,CACHE_ERROR);
                break;
            case 2106:
                backdata_2106_addordelete_remote_control(obj,CACHE_ERROR);
                break;
            case 2107:
                backdata_2107_oldLearn_remote_control(obj,CACHE_ERROR);
                break;
            case 2108:
                backdata_2108_shushijinru(obj,CACHE_ERROR);
                break;
            case 6001:
                backdata_6001_push_Result(obj,CACHE_ERROR);
                break;
        }
    }
    // ============================error==================================
    // ============================ccmd==================================

    /**
     * 新增车辆,修改
     **/
    public void ccmd1201_newrepairCar(DataCarInfo info) {
        JsonObject carInfo = new JsonObject();
        carInfo.add("carInfo", DataCarInfo.toJsonObject(info));
        HttpConn.getInstance().sendMessage(carInfo, 1201);
    }

    /**
     * 删除车辆
     **/
    public void ccmd1202_deletecar(long carid) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carid);
        HttpConn.getInstance().sendMessage(data, 1202);
    }

    /**
     * 激活车辆
     **/
    public void ccmd1204_activatecar(long carid, String terminalNum) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carid);
        data.addProperty("terminalNum", terminalNum);
        HttpConn.getInstance().sendMessage(data, 1204);
    }

    /**
     * 解绑车辆
     **/
    public void ccmd1220_unactivatecar(long carid) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carid);
        HttpConn.getInstance().sendMessage(data, 1220);
    }

    /**
     * 取车辆列表,first only once
     **/
    public void ccmd1203_getcarlist() {
        if (BuildConfig.DEBUG)Log.e("车列表拿的时间", "开始时间" +System.currentTimeMillis());
        HttpConn.getInstance().sendMessage(null, 1203);
    }

    /**
     * 置当前车辆状态
     **/
    public boolean isRefreshBtn;
    public void ccmd1219_getCarState(long carId, int isDemo, boolean isRefreshBtn) {
        this.isRefreshBtn = isRefreshBtn;
        if (carId == 0) return;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("isDemo", isDemo);
        carId1219 = carId;
        HttpConn.getInstance().sendMessage(data, 1219);
        if (BuildConfig.DEBUG) Log.e("查看1219发的什么", "carId"+carId+"使用的方法------------ccmd1219_getCarState long carId, int isDemo, boolean isRefreshBtn");
    }
//    private long pre1219CarId = 0,pre1219Time = 0;
    public void ccmd1219_getCarState(long carId, int isDemo) {
        if (carId == 0) return;
//        long now = System.currentTimeMillis();
//        if(pre1219CarId == carId && now-pre1219Time<1000)return;
//        pre1219CarId = carId;
//        pre1219Time = now;
        this.isRefreshBtn = false;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("isDemo", isDemo);
        carId1219 = carId;
        HttpConn.getInstance().sendMessage(data, 1219);
        if (BuildConfig.DEBUG)Log.e("查看1219发的什么", "carId"+carId+"使用的方法------------ccmd1219_getCarState long carId, int isDemo");
    }
    private boolean isSelfRefresh;
    /*isSelfFresh 是否自己主動刷新*/
    public void ccmd1219_getCarState(long carId, int isDemo,boolean selfFresh,int aaaaaaa) {
        this.isSelfRefresh=selfFresh;
        if (carId == 0) return;
//        long now = System.currentTimeMillis();
//        if(pre1219CarId == carId && now-pre1219Time<1000)return;
//        pre1219CarId = carId;
//        pre1219Time = now;
        this.isRefreshBtn = false;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("isDemo", isDemo);
        carId1219 = carId;
        HttpConn.getInstance().sendMessage(data, 1219);
        if (BuildConfig.DEBUG)Log.e("查看1219", "carId"+carId+"使用的方法------------ccmd1219_getCarState long carId, int isDemo,boolean selfFresh,int aaaaaaa");
    }
    /** 查警告列表 ,协议取消 **/
//	public void ccmd1221_getWarninglist(long carId,long startTime,long endTime,int alertType) {
//		JsonObject data = new JsonObject();
//		data.addProperty("carId", carId);
//		data.addProperty("startTime", startTime);
//		data.addProperty("endTime", endTime);
//		data.addProperty("alertType", alertType);
//		HttpConn.getInstance().sendMessage(data, 1221);
//	}

    /**
     * 查警告列表
     **/
    public void ccmd1221_getWarninglist(long startTime, long endTime, int type, int start, int size, long carId) {
        JsonObject data = new JsonObject();
        data.addProperty("startTime", startTime);
        data.addProperty("endTime", endTime);
        data.addProperty("type", type);//1：控制，2：警报，3：安全
        data.addProperty("start", start);//开始条数	必填，从0开始
        data.addProperty("size", size);//条数	必填，默认为20
        data.addProperty("carId", carId);//汽车id	必填
        HttpConn.getInstance().sendMessage(data, 1221);
    }

    /**
     * 演示模式开始和结束
     **/
    public void ccmd1226_DemoMode(int type) {
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1226);
    }

    /**
     * phead jsonObject 信息头 必填，具体见协议框架中的请求头信息
     * carId long 汽车id 必填
     * start long 开始下标 必填，从0开始
     * size int 获取的条数 必填，默认20
     */
    public void ccmd1227_CarMaintain(long carId, long start, int size) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("start", start);
        data.addProperty("size", size);
        HttpConn.getInstance().sendMessage(data, 1227);
    }

    /**
     * phead jsonObject 信息头 必填，具体见协议框架中的请求头信息
     * carId long 汽车id 必填，
     * miles int 保养里程数 必填，单位km
     * time int 保养的时间段 必填，单位月
     * maintenanceId
     * long 保养id 选填，如果是修改保养，则需要填写，具体见汽车保养对象中的ide
     */
    public void ccmd1228_CarMaintainAddModification(long carId, int miles, int time, long maintenanceId) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("miles", miles);
        data.addProperty("time", time);
        data.addProperty("maintenanceId", maintenanceId);
        HttpConn.getInstance().sendMessage(data, 1228);
    }

    /**
     * 起始
     * 接口描述：用于删除保养记录
     * 请求参数（封装到data参数中上传）
     * 名称 类型 说明 备注
     * phead jsonObject 信息头 必填，具体见协议框架中的请求头信息
     * maintenanceId
     * long 保养id 必填，具体见汽车保养对象中的ide
     */
    public void ccmd1229_CarMaintainDeleteModification(long maintenanceId) {
        JsonObject data = new JsonObject();
        data.addProperty("maintenanceId", maintenanceId);
        HttpConn.getInstance().sendMessage(data, 1229);
    }

    /**
     * type int 类型
     * 必填，1：为提前提示框中的点击 知道了 触发
     * 2：为到时提示框中的点击稍后提醒
     * 3：为到时提示框中的点击知道了，或者已保养提示框
     *
     * @param maintenanceId
     */
    public void ccmd1230_CarMaintainConfirmmineModification(long maintenanceId, int type) {
        JsonObject data = new JsonObject();
        data.addProperty("maintenanceId", maintenanceId);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1230);
    }

    /**
     * 汽车完成保养接口（1231）
     * 转至元数据结尾
     * 被fangwentao添加，被fangwentao最后更新于十一月 02, 2016
     * 转至元数据起始
     * 接口描述：用于完成保养时调用的接口
     * 请求参数（封装到data参数中上传）
     * 名称 类型 说明 备注
     * phead jsonObject 信息头 必填，具体见协议框架中的请求头信息
     * maintenanceId
     * long 保养id 必填，具体见汽车保养对象中的ide
     */
    public void ccmd1231_CarMaintainCompleteModification(long maintenanceId) {
        JsonObject data = new JsonObject();
        data.addProperty("maintenanceId", maintenanceId);
        HttpConn.getInstance().sendMessage(data, 1231);
    }

    public void ccmd1232_getCarListFromPhone(String phoneNum) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        HttpConn.getInstance().sendMessage(data, 1232);
    }

    //先发时间，再发控制
    private boolean isNeedBlueSendNext = false;
    private String cacheBlueName = "";
    private String cacheCmd = "";
    public int getPreCmd(){
        return preCmd;
    }
    public void clearPreCmd(){
        preCmd = -1;
    }
    public boolean ifNeedBlueSendNext(byte[] bytes) {//在发完计时后，再发控制
        if (bytes == null || bytes.length != 5) return false;
        byte bb = (byte) 0x82;//控制指令都 是0x82开头
        if (bytes[0] == bb)return false;
        if(isNeedBlueSendNext){
            isNeedBlueSendNext =false;
            new TimeDelayTask().runTask(100L, new TimeDelayTask.OnTimeEndListener() {
                @Override
                public void onTimeEnd() {
                    BlueLinkReceiver.getInstance().sendMessage(cacheCmd,false);//发送指令,间隔一点
                }
            });
            return true;
        }else{
            return false;
        }
    }

    /**
     * 控制车辆 instruction控制命令1：开启2：熄火3：设防4：撤防5：尾箱变化6：寻车
     time只有0到7档，每档5分钟，0档位5分钟
     *
     **/
    public void ccmd1233_controlCar(final DataCarInfo car, final int controlCmd, final int time) {
        preCarId = car.ide;
        preCmd = controlCmd;
        //蓝牙模式,不收socket控制状态事件
        if (BlueLinkReceiver.getIsBlueConnOK()){
            if (car.isBindBluetooth == 0) return;
            if (controlCmd == 1) {
                cacheBlueName = car.bluetoothName;
                cacheCmd = BlueStaticValue.getControlCmdByID(controlCmd);
                isNeedBlueSendNext = true;
                String setTime = BlueStaticValue.getBlueTimeCmd(time);//0-7
                BlueLinkReceiver.getInstance().sendMessage(setTime,false);//发送指令
            }else {
                BlueLinkReceiver.getInstance().sendMessage(BlueStaticValue.getControlCmdByID(controlCmd),false);//发送指令
            }
        }else{
            boolean isUseSocket = true;//选用socket还是http发消息
            if(isUseSocket){
                OCtrlSocketMsg.getInstance().ccmd_controlCar(car,controlCmd,time);
            }else {
                JsonObject data = new JsonObject();
                data.addProperty("carId", car.ide);
                //将{“instruction”:1,"time":1}利用每辆车的AES加密串进行加密，然后用base64编码
                JsonObject cache = new JsonObject();
                cache.addProperty("instruction", controlCmd);
                cache.addProperty("time", time);
                data.addProperty("instruction", cache.toString());
                try {
//                    byte[] bytes = AES.AESgenerator(cache.toString(), car.carsig);
//                    if (bytes == null) return;
//                    String strBase64 = new String(Base64.encode(bytes, Base64.DEFAULT));
//                    data.addProperty("instruction", strBase64);
                    HttpConn.getInstance().sendMessage(data, 1233);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void ccmd1238_MantanceMode(long carId, int type) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1238);
    }

    /**
     * carId long 汽车id 必填
     ACStatus int A/C状态 0：关闭，1：开启，不设置或值未改变为-1
     windStatus int 风力状态
     0：小，1：中，2：大，不设置或值未改变为-1
     tempStatus int 温度状态 0：强（冷），1：中（冷），2：弱（冷），3：中间档，4：低（热），5：中（热），6：高（热），不设置或值未改变为-1
     */
    public void ccmd1248_Change_Air_Condition(long carId, int ACStatus,int windStatus,int tempStatus) {
        Log.i("abcde", "发送协议 "+" carId"+carId+"ACStatus"+ACStatus+"windStatus"+windStatus+"tempStatus"+tempStatus);
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("ACStatus", ACStatus);
        data.addProperty("windStatus", windStatus);
        data.addProperty("tempStatus", tempStatus);
        carId1219=carId;
        HttpConn.getInstance().sendMessage(data, 1248);
    }
    /**
     * @type 必填，1：绑定，2：解绑
     * @keyBlueName 蓝牙名称
     */

    public void ccmd1249_isBindLcdKey(long carId, int type,String  keyBlueName) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("keyBlueName", keyBlueName);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1249);
         if (BuildConfig.DEBUG) Log.e("蓝牙绑定", "carId"+carId+"  type"+type+"  keyBlueName"+keyBlueName);
    }
    /**
     * @isOpen 0：关闭，1：开启
     * */

    public void ccmd1250_LcdKey_autoOpen(long carId, int isOpen) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("isOpen", isOpen);
        HttpConn.getInstance().sendMessage(data, 1250);
         if (BuildConfig.DEBUG) Log.e("蓝牙自动开启关闭", "carId"+carId+"  isOpen"+isOpen);
    }

    /**
     * type	int	类型	必填，0：静音，1：行车落锁，2：锁车升窗，3：洗护模式，
     * */
    public void ccmd1251_Car_Sound_Control(long carId, int isOpen,int type) {
        carId1251=carId;
        type1251=type;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("isOpen", isOpen);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1251);
         if (BuildConfig.DEBUG) Log.e("车辆禁声", "carId"+carId+"  isOpen"+isOpen+"type"+type);
    }
    /**
     * type	int	类型	必填，0：静音，1：行车落锁，2：锁车升窗，3：洗护模式，
     * */
    public void ccmd1251_Car_Sound_Control(long carId, int[] types,int mytype) {
        carId1251=carId;
        type1251=mytype;
        Gson gson = new Gson();
        String    json = gson.toJson(types);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        //==================send======
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.add("types", arr);
        HttpConn.getInstance().sendMessage(data, 1251);
         if (BuildConfig.DEBUG) Log.e("车辆禁声", "carId"+carId+"  types0"+types[0]+"  types1"+types[1]+"types2"+types[2]+"types3"+types[3]+arr);
    }

    /**
     * carId	long	汽车id	必填
     * isOpen	int	是否开关	0：关闭，1：开启，
     * type	int	类型
     * 1.左门，2：右门，3：左门+右门
     * */

    public void ccmd1252_middle_door_control(long carId, int isOpen,int type) {
        carId1252=carId;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("isOpen", isOpen);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1252);
         if (BuildConfig.DEBUG) Log.e("1252", "carId"+carId+"  isOpen"+isOpen);
    }

    /**
     * carId	long	汽车id	必填
     * instruct	int	11座椅加热，12座椅通风
     * type	int	类型
     * 0左关，1左开，1右关，2右开
     * */

    public void ccmd1253_seat_airheat_control(long carId, int instruct,int type) {
        carId1252=carId;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("instruct", instruct);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1253);
        if (BuildConfig.DEBUG) Log.e("1252", "carId"+carId+"  instruct"+instruct+"  type"+type);
    }



//    /**
//     * @isOpen 0：关闭，1：开启
//     * */
//    public void ccmd1251_Car_Sound_Control(long carId, int isOpen) {
//        carId1251=carId;
//        JsonObject data = new JsonObject();
//        data.addProperty("carId", carId);
//        data.addProperty("isOpen", isOpen);
//        HttpConn.getInstance().sendMessage(data, 1251);
//         if (BuildConfig.DEBUG) Log.e("车辆禁声", "carId"+carId+"  isOpen"+isOpen);
//    }

    // ============================scmd==================================
    public void ccmd2101_queryNfc(long carId) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        HttpConn.getInstance().sendMessage(data, 2101);
        if (BuildConfig.DEBUG) Log.e("2101", "carId"+carId);
    }

    public void ccmd2102_addOrDeleteNfc(long carId,String cardNum,String operateType) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("cardNum", cardNum);
        data.addProperty("operateType", operateType);
        HttpConn.getInstance().sendMessage(data, 2102);
        if (BuildConfig.DEBUG) Log.e("2102", "carId"+carId);
    }

    public void ccmd2103_changeNfc(long carId,String cardNum,String name) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("cardNum", cardNum);
        data.addProperty("name", name);
        HttpConn.getInstance().sendMessage(data, 2103);
        if (BuildConfig.DEBUG) Log.e("2103", "carId"+carId+"cardNum"+cardNum+"name"+name);
    }
    public void ccmd2104_reGetNfc(long carId) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        HttpConn.getInstance().sendMessage(data, 2104);
        if (BuildConfig.DEBUG) Log.e("2104", "carId"+carId);
    }
    public void ccmd2000_pushPic(String file,String type) {
        urlType=type;
        JsonObject data = new JsonObject();
        data.addProperty("file", file);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 2000);
        if (BuildConfig.DEBUG) Log.e("2000", "carId"+2000);
    }
    public void ccmd2001_tijiaoshenhe(String phoneNum,String verifyCode,String entrance,long pId) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("verifyCode", verifyCode);
        data.addProperty("entrance", entrance);
        data.addProperty("pId", pId);
        HttpConn.getInstance().sendMessage(data, 2001);
        if (BuildConfig.DEBUG) Log.e("2001", "carId"+2001);
    }
    public void ccmd2201_applySampleStore(ShopCarExamples shopCarExamples) {
        JsonObject object=ShopCarExamples.toJsonObject(shopCarExamples);
        JsonObject data = new JsonObject();
        data.add("shopCarExamples",object);
        HttpConn.getInstance().sendMessage(data, 2201);
        if (BuildConfig.DEBUG) Log.e("2201", "carId"+2201);
    }
/***
 * 查詢样车详情
 */
    public void ccmd2202_qurrySampleStore(String terminalNum) {
        JsonObject data = new JsonObject();
        data.addProperty("terminalNum",terminalNum);
        HttpConn.getInstance().sendMessage(data, 2202);
        if (BuildConfig.DEBUG) Log.e("2201", "carId"+2202);
    }
/**
 * 查询车俩PKE遥控器信息
 *
 * */
private boolean isRegetRemoteControl;
    public void ccmd2105_RemoteControl(long carId,boolean refresh) {
        isRegetRemoteControl=refresh;
        JsonObject data = new JsonObject();
        data.addProperty("carId",carId);
        data.addProperty("refresh",refresh);
        HttpConn.getInstance().sendMessage(data, 2105);
        if (BuildConfig.DEBUG) Log.e("2201", "carId"+2105);
    }

    /**
     * 添加删除PKE遥控器
     *
     * */
    public void ccmd2106_AddOrDelete_RemoteControl(long carId,String slotNum,String operateType ) {
        JsonObject data = new JsonObject();
        data.addProperty("carId",carId);
        data.addProperty("slotNum",slotNum);
        data.addProperty("operateType",operateType);
        HttpConn.getInstance().sendMessage(data, 2106);
        if (BuildConfig.DEBUG) Log.e("2201", "carId"+2106);
    }
    /**
     * 老主机学遥控器
     *
     * */
    public void ccmd2107_oldLearn_RemoteControl(long carId ) {
        JsonObject data = new JsonObject();
        data.addProperty("carId",carId);
        HttpConn.getInstance().sendMessage(data, 2107);
        if (BuildConfig.DEBUG) Log.e("2201", "carId"+2107);
    }

    public void ccmd2108_shushijinru(long carId ,String operateType,String isOpen) {
        JsonObject data = new JsonObject();
        data.addProperty("carId",carId);
        data.addProperty("operateType",operateType);
        data.addProperty("isOpen",isOpen);
        HttpConn.getInstance().sendMessage(data, 2108);
        if (BuildConfig.DEBUG) Log.e("2201", "carId"+2108);
    }

    /**
     * 修改门店样车
     *
     * */
    public void ccmd2203_changeSampleStore(ShopCarExamples shopCarExamples) {
        JsonObject object=ShopCarExamples.toJsonObject(shopCarExamples);
        JsonObject data = new JsonObject();
        data.add("shopCarExamples",object);
        HttpConn.getInstance().sendMessage(data, 2203);
        if (BuildConfig.DEBUG) Log.e("2201", "carId"+2203);
    }

    /**
     * 上传工程设置和蓝牙无感进入参数
     * */
    public void ccmd6001_pushDeviceSet(JsonObject deviceSet) {
        JsonObject data = new JsonObject();
        data.add("deviceSet",deviceSet);
        HttpConn.getInstance().sendMessage(data, 6001);
        if (BuildConfig.DEBUG) Log.e("6001", "carId"+6001+ deviceSet.toString());
    }


    /**
     * 新增车辆
     **/
    private void backdata_1201_newrepairCar(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1201_newrepairCar");
            ODispatcher.dispatchEvent(OEventName.CAR_NEW_SUCESS);
        }
    }

    /**
     * 删除车辆
     **/
    private void backdata_1202_deleteCar(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1202_deleteCar");
            ODispatcher.dispatchEvent(OEventName.CAR_DELETE_SUCESS);
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
        }
    }

    /**
     * 激活车辆
     **/
    private void backdata_1204_activatecar(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
//            ManagerSkins.getInstance().saveFlash(OJsonGet.getString(obj, "splashAddress"));
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1204_activatecar");
            ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);//刷新me界面解绑有用，此处无用
            ODispatcher.dispatchEvent(OEventName.CAR_ACTIVATE_SUCESS);
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
        }
    }

    /**
     * 解绑车辆
     **/
    private void backdata_1220_unactivatecar(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1220_unactivatecar");
//            JsonObject shakeInfo   = OJsonGet.getJsonObject(obj, "shakeInfo");
//            JsonArray nonekeyNoticeInfos = OJsonGet.getJsonArray(obj, "nonekeyNoticeInfos");
//            BlueLinkNetSwitch.saveSwitchShake(shakeInfo);
//            ManagerSwitchs.getInstance().saveSwitchNoKey(nonekeyNoticeInfos);
            ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);//刷新me界面
            ODispatcher.dispatchEvent(OEventName.CAR_UNACTIVATE_SUCESS);
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
//            if(ViewSwitchShake.viewSwitchShakeThis!=null)ViewSwitchShake.viewSwitchShakeThis.handlerChangeSwitch();
            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
            BlueLinkReceiver.getInstance().closeBlueConnClearName("解绑车辆成功");
            BlueLinkReceiver.getInstance().closeBlueConnClearNameLcdKey("解绑车辆成功");
        }
    }

    /**
     * 取车辆列表
     **/
    private void backdata_1203_getcarlist(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            Logger.json(obj.toString());
            Log.e("车列表拿的时间", "结束时间" +System.currentTimeMillis());
             if (BuildConfig.DEBUG) Log.e("carList1203",obj.toString());
             if (BuildConfig.DEBUG) Log.e("车辆禁声",obj.toString());
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1203_getcarlist");
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
        }
    }

    /**
     * 查警告列表
     **/
    private void backdata_1221_getWarninglist(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray messageInfos = OJsonGet.getJsonArray(obj, "messageInfos");
            if (messageInfos != null) ManagerWarnings.getInstance().saveLoadingList(messageInfos);
            ODispatcher.dispatchEvent(OEventName.CAR_SEARCHWARNING_LISTBACK);//全空的数据也要刷新
        }
    }

    /**
     * 下发数据参数
     * result jsonObject 响应信息头 具体见协议框架中的响应信息头
     * maintenanceInfos
     * jsonArray  保养列表  具体见 汽车保养对象
     */
    private void backdata_1227_getMaintainList(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray maintenanceInfos = OJsonGet.getJsonArray(obj, "maintenanceInfos");
            ManagerMaintainList.getInstance().saveMainTainceInfoList(maintenanceInfos);
            ManagerMaintainList.getInstance().saveMainTainceInfoListLocal();
            ODispatcher.dispatchEvent(OEventName.GET_MAINTAINLIST_RESULTBACK);
        }
    }

    /**
     * 下发数据参数
     * result jsonObject 响应信息头 具体见协议框架中的响应信息头
     * maintenanceInfos
     * jsonArray  保养列表  具体见 汽车保养对象，只下发第一页20条
     */
    private void backdata_1228_AddModificationMaintainList(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray maintenanceInfos = OJsonGet.getJsonArray(obj, "maintenanceInfos");
            ManagerMaintainList.getInstance().saveMainTainceInfoList(maintenanceInfos);
            ManagerMaintainList.getInstance().saveMainTainceInfoListLocal();
            ODispatcher.dispatchEvent(OEventName.MODIFICATION_MAINTAINLIST_RESULT_BACK);
        }
    }

    /**
     * 置当前车辆状态
     **/
    private void backdata_1219_getCarState(JsonObject obj, String CACHE_ERROR) {
        Log.e("网络杀手", "backdata_1219_getCarState: " );
//        if(BlueLinkReceiver.getIsBlueConnOK())return;//蓝牙状态
        if (CACHE_ERROR.equals("")) {
            JsonObject carStatusInfo = OJsonGet.getJsonObject(obj, "carStatusInfo");
            if (carStatusInfo != null) {
                if(!EquipmentManager.isMini()){
                    ManagerCarList.getInstance().saveCarStatus(carStatusInfo,"backdata_1219_getCarState");
                }
                Log.e("网络杀手", "1263" );
                ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
                if(isSelfRefresh){
                    ODispatcher.dispatchEvent(OEventName.CAR_SELF_REFRESH_STATUS);
                    isSelfRefresh=false;
                }
                if(isRefreshBtn)ODispatcher.dispatchEvent(OEventName.PRESS_REFRESH_RESULTOK,true);
            }
        }else{
            if(isRefreshBtn)ODispatcher.dispatchEvent(OEventName.PRESS_REFRESH_RESULTOK,false);
        }
    }

    /**
     * 下发数据参数
     * result jsonObject 响应信息头 具体见协议框架中的响应信息头
     * maintenanceInfos
     * jsonArray  保养列表  具体见 汽车保养对象，只下发第一页20条
     */
    private void backdata_1229_DeleteMaintainList(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray maintenanceInfos = OJsonGet.getJsonArray(obj, "maintenanceInfos");
            ManagerMaintainList.getInstance().saveMainTainceInfoList(maintenanceInfos);
            ManagerMaintainList.getInstance().saveMainTainceInfoListLocal();
            ODispatcher.dispatchEvent(OEventName.MAINTAIN_DELETE);
        }
    }

    private void backdata_1230_ConfirmMaintainList(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {

        }
    }

    private void backdata_1231_CompleteMaintainList(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray maintenanceInfos = OJsonGet.getJsonArray(obj, "maintenanceInfos");
            ManagerMaintainList.getInstance().saveMainTainceInfoList(maintenanceInfos);
            ManagerMaintainList.getInstance().saveMainTainceInfoListLocal();
            ODispatcher.dispatchEvent(OEventName.MAINTAIN_COMPELETE);
        }
    }

    private void backdata_1232_getCarListFromPhone(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ManagerCarList.getInstance().saveCarListOtherPeople(OJsonGet.getJsonArray(obj, "carInfos"));
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_OTHER_RESULTBACK);
        }
    }

    /**
     * @param obj
     * @param CACHE_ERROR
     */
    private void backdata_1226_DemoMode(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            if (DemoMode.isBeginOrFinish == 1) {
//				DemoMode.getInstance().saveIsDemoMode("演示开始")DemoMode.isDemoMode="演示开始";
                DemoMode.saveIsDemoMode(true);
                ODispatcher.dispatchEvent(OEventName.DEMO_MODE_START);
            } else if (DemoMode.isBeginOrFinish == 2) {
//				DemoMode.getInstance().saveIsDemoMode("演示结束");
                DemoMode.saveIsDemoMode(false);
//			DemoMode.isDemoMode="演示结束";
                ODispatcher.dispatchEvent(OEventName.EXIT_DEMOMODE_WINDOW_DISMISS);
            }
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1226_DemoMode");
            ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);

        } else {
            if (DemoMode.isBeginOrFinish == 1) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, GlobalContext.getContext().getResources().getString(R.string.network_anomaly_temporarily_unable_to_enter_the_demo_mode));
            } else if (DemoMode.isBeginOrFinish == 2) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, GlobalContext.getContext().getResources().getString(R.string.network_anomaly_temporarily_unable_to_exit_the_demo_mode));
            }
        }
    }

    /**
     * 控制车辆 instruction控制命令1：开启2：熄火3：设防4：撤防5：尾箱变化6：寻车 time只有0到7档，每档5分钟，0档位5分钟
     **/
    private long preCarId;
    private int preCmd;
    private void backdata_1233_controlCar(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject result = OJsonGet.getJsonObject(obj, "result");
            int        status = OJsonGet.getInteger(result, "status");
            if (status == 1) {
                CarControlResult result1 = new CarControlResult();
                result1.carId = preCarId;
                result1.currentProcess = CARCONTROL_SENDED;
                result1.instruction = preCmd;
                result1.status = 1;
                ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result1);
                int isDemoMode = DemoMode.getIsDemoMode() ? 1 : 0;
                ccmd1219_getCarState(preCarId,isDemoMode,true);
            }
        }
    }

    private void backdata_1238_MantanceMode(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1238_MantanceMode");
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
        }
    }
    private void backdata_1248_Change_Air_Condition(JsonObject obj, String CACHE_ERROR) {
//        if (CACHE_ERROR.equals("")) {
//            JsonObject carStatusInfo = OJsonGet.getJsonObject(obj, "carStatusInfo");
//            if (carStatusInfo != null) {
//                carStatusInfo.addProperty("ide", carId1219);
//                 if (BuildConfig.DEBUG) Log.e("backdata_1248","carId:"+carId1219+"http检测车辆启动状态"+DataCarStatus.fromJsonObject(carStatusInfo).isStart);
//                ManagerCarList.getInstance().saveCarStatus(carStatusInfo);
//            }
//        }
    }
    /**
     * 绑定液晶钥匙
     **/
    private void backdata_1249_bind_lcdKey(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
             if (BuildConfig.DEBUG) Log.e("蓝牙绑定", "成功");
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "ccmd1249_isBindLcdKey");
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
            ODispatcher.dispatchEvent(OEventName.LCDKEY_BIND_OPRATION,true);
            DataCarInfo carInfo= ManagerCarList.getInstance().getCurrentCar();
            long userId=  ManagerLoginReg.getInstance().getCurrentUser().userId;
            BlueLinkReceiver.needChangeLcdKey(carInfo.ide,carInfo.keyBlueName,carInfo.keySig,carInfo.isKeyBind,carInfo.isKeyOpen,userId);
        }else {
            ODispatcher.dispatchEvent(OEventName.LCDKEY_BIND_OPRATION,false);
        }
    }
    /**
     * 液晶钥匙自动开启关闭
     **/
    private void backdata_1250_LcdKey_autoOpen(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
             if (BuildConfig.DEBUG) Log.e("蓝牙自动开启关闭", "成功");
            ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "1250_LcdKey_autoOpen");
            ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
            ODispatcher.dispatchEvent(OEventName.LCDKEY_AUTO_OPEN);
            DataCarInfo carInfo= ManagerCarList.getInstance().getCurrentCar();
            long userId=ManagerLoginReg.getInstance().getCurrentUser().userId;
            BlueLinkReceiver.needChangeLcdKey(carInfo.ide,carInfo.keyBlueName,carInfo.keySig,carInfo.isKeyBind,carInfo.isKeyOpen,userId);
        }
    }

//    /**
//     * 噤声开关开启关闭
//     **/
//    private void backdata_1251_Car_Sound_Control(JsonObject obj, String CACHE_ERROR) {
//        if (CACHE_ERROR.equals("")) {
//            //存本地
//            List<DataCarInfo> currentList=ManagerCarList.getInstance().getCarInfoList();
//           if(currentList!=null&&currentList.size()>0){
//               for (int i = 0; i < currentList.size(); i++) {
//                   if(currentList.get(i)!=null){
//                       if(currentList.get(i).ide==carId1251){
//                           currentList.get(i).isMute=currentList.get(i).isMute==0?1:0;
//                       }
//                   }
//               }
//           }
//            ManagerCarList.getInstance().saveCarListLocal(currentList);
//            ODispatcher.dispatchEvent(OEventName.CAR_SOUND_CONTROL_RESULT);
//        }
//    }
    /**
     * 噤声开关开启关闭
     **/
    private void backdata_1251_Car_Sound_Control(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
             if (BuildConfig.DEBUG) Log.e("车辆禁声", "回报"+type1251);
            //存本地
            List<DataCarInfo> currentList=ManagerCarList.getInstance().getCarInfoList();
            if(currentList!=null&&currentList.size()>0){
                for (int i = 0; i < currentList.size(); i++) {
                    if(currentList.get(i)!=null){
                        if(currentList.get(i).ide==carId1251){
                            switch (type1251){
                                case 0:
                                    currentList.get(i).isMute=currentList.get(i).isMute==0?1:0;
                                    break;
                                case 1:
                                    currentList.get(i).isQuitLock=currentList.get(i).isQuitLock==0?1:0;
                                    break;
                                case 2:
                                    currentList.get(i).lockCloseWin=currentList.get(i).lockCloseWin==0?1:0;
                                    break;
                                case 3:
                                    currentList.get(i).washMould=currentList.get(i).washMould==0?1:0;
                                    break;
                                case 5:
                                    currentList.get(i).startDefend=currentList.get(i).startDefend==0?1:0;
                                    break;
                            }
                        }
                    }
                }
            }
            if(currentList!=null){
                ManagerCarList.getInstance().saveCarListLocal(currentList);
            }
            ODispatcher.dispatchEvent(OEventName.CAR_SET_CONTROL_RESULT);
            if(type1251==3){
                ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
            }
        }
    }
    /**
     * 中门控制开关返回结果
     **/
    private void backdata_1252_middle_door_control(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.CAR_MIDDLE_DOOR_CONTROL_RESULT);
        }
    }
    /**
     * 2102返回结果
     **/
    private void backdata_2101_queryNfc(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject productDevice = OJsonGet.getJsonObject(obj, "productDevice");
            if (BuildConfig.DEBUG) Log.e("2101", "---"+obj.toString());
            if(productDevice!=null){
                ManagerNfc.getInstance().saveDataNfc(productDevice);
            }
            ODispatcher.dispatchEvent(OEventName.QUERY_NFC_DATA_BACK);
        }
    }
    /**
     * 2102返回结果
     **/
    private void backdata_2102_addOrDeleteNfc(JsonObject obj, String CACHE_ERROR) {
        if (!CACHE_ERROR.equals("")) {
            if (BuildConfig.DEBUG) Log.e("2102", "---"+obj.toString());
            ODispatcher.dispatchEvent(OEventName.ADD_OR_DELETE_NFC_DATA_BACK,false);
        }
    }
    /**
     * 2102返回结果
     **/
    private void backdata_2103_changeNfc(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject productDevice = OJsonGet.getJsonObject(obj, "productDevice");
            if (BuildConfig.DEBUG) Log.e("2103", "---"+obj.toString());
            if(productDevice!=null){
                ManagerNfc.getInstance().saveDataNfc(productDevice);
            }
            ODispatcher.dispatchEvent(OEventName.CHANGE_NFC_DATA_BACK);
        }
    }
    /**
     * 2102返回结果
     **/
    private void backdata_2104_reGetNfc(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            if (BuildConfig.DEBUG) Log.e("2104", "---"+obj.toString());
            JsonObject productDevice = OJsonGet.getJsonObject(obj, "productDevice");
            if(productDevice!=null){
                ManagerNfc.getInstance().saveDataNfc(productDevice);
            }
            ODispatcher.dispatchEvent(OEventName.REGET_NFC_RESULT_BACK,true);
        }else{
            ODispatcher.dispatchEvent(OEventName.REGET_NFC_RESULT_BACK,false);
        }
    }
    /**
     * 2000返回结果
     **/
    private void backdata_2000_pushresult(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            String url = OJsonGet.getString(obj, "url");
            ManagerStore.getInstance().saveImgUrl(url,urlType);
            ODispatcher.dispatchEvent(OEventName.IMG_PUSH_RESULT,urlType);
        }else{
            ODispatcher.dispatchEvent(OEventName.IMG_PUSH_RESULT,"fail");
        }
    }
    /**
     * 2201返回结果
     **/
    private void backdata_2201_apply_store_car(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject shopCarExamples = OJsonGet.getJsonObject(obj, "shopCarExamples");
            ManagerStore.getInstance().saveShopExamolePid(shopCarExamples);
            ODispatcher.dispatchEvent(OEventName.APPLY_STORE_CAR_RESULT,true);
        }else{
            ODispatcher.dispatchEvent(OEventName.APPLY_STORE_CAR_RESULT,false);
        }
    }
    /**
     * 2000返回结果
     **/
    private void backdata_2001_tijiaoshenhe(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.SUBMIT_AUDIT_SUCCESS);
        }
    }

    /**
     * 2202 查询门店样车
     **/
    private void backdata_2202_qurry_store_car(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject shopCarExamples = OJsonGet.getJsonObject(obj, "shopCarExamples");
            ManagerStore.getInstance().saveShopExamolePid(shopCarExamples);
            ODispatcher.dispatchEvent(OEventName.QURRY_STORE_CAR_RESULT,true);
        }
    }
    /**
     * 2203 修改门店样车
     **/
    private void backdata_2203_change_car_store(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.CHANGE_CAR_STORE_RESULT,true);
        }
    }

    /**
     * 2105 查询车俩PKE遥控器信息
     **/
    private void backdata_2105_qurry_remote_control(JsonObject obj, String CACHE_ERROR) {
         if (CACHE_ERROR.equals("")) {
            JsonObject productRemote = OJsonGet.getJsonObject(obj, "productRemote");
            ManagerRemoteControl.getInstance().saveBeanRemoteControl(productRemote);
            ODispatcher.dispatchEvent(OEventName.QURRY_REMOTE_CONTROL_RESULT,"1");
        }else{
            //重新获取失败的时候
            if(isRegetRemoteControl){
                ODispatcher.dispatchEvent(OEventName.QURRY_REMOTE_CONTROL_RESULT,"2");
            }else{
                ODispatcher.dispatchEvent(OEventName.QURRY_REMOTE_CONTROL_RESULT,"3");
                new ToastTxt(GlobalContext.getCurrentActivity(),null).withInfo(CACHE_ERROR).show();
            }
        }
    }

    /**
     * 2106 添加删除PKE遥控器
     **/
    private void backdata_2106_addordelete_remote_control(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
//            JsonObject productRemote = OJsonGet.getJsonObject(obj, "productRemote");
//            ManagerRemoteControl.getInstance().saveBeanRemoteControl(productRemote);
            ODispatcher.dispatchEvent(OEventName.ADD_OR_DELETE_REMOTE_CONTROL,true);
        }else{
            ODispatcher.dispatchEvent(OEventName.ADD_OR_DELETE_REMOTE_CONTROL,false);
        }
    }

    /**
     * 2107 老主机学遥控器结果
     **/
    private void backdata_2107_oldLearn_remote_control(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {

        }
    }
    /**
     * 2108 舒适进入
     **/
    private void backdata_2108_shushijinru(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {

        }
    }
    /**
     * 6001结果
     **/
    private void backdata_6001_push_Result(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {

        }else{
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "参数同步失败，网络不良！");
        }
    }
}
