package annualreminder.ctrl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import annualreminder.model.AnnualRecode;
import annualreminder.model.ManagerAnnual;
import com.kulala.dispatcher.OEventName;
import common.http.HttpConn;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;
/**
 * Created by Administrator on 2017/2/27.
 */

public class OCtrlAnnual {
    private long AnnualPreCarId;
    // ========================out======================
    private static OCtrlAnnual _instance;
    protected OCtrlAnnual() {}
    public static OCtrlAnnual getInstance() {
        if (_instance == null)
            _instance = new OCtrlAnnual();
        return _instance;
    }

    // ========================out======================
    public void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
        switch (protocol) {
            case 1239:
                backdata_1239_getAnnualInfoByCar(obj,CACHE_ERROR);
                break;
            case 1240:
                backdata_1240_setAnnualRecode(obj,CACHE_ERROR);
                break;
            case 1241:
                backdata_1241_setAnnualInfo(obj,CACHE_ERROR);
                break;
        }
    }
    // ============================ccmd==================================

    /**
     * 取年检信息
     * ClipAnnualReminder_select_car,中一设置完车，发出
     **/
    public void ccmd1239_getAnnualInfoByCar(long carId) {
        AnnualPreCarId = carId;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        HttpConn.getInstance().sendMessage(data, 1239);
    }

    /**
     * 添加/修改年检记录 （1240）
     **/
    public void ccmd1240_setAnnualRecode(long carId, AnnualRecode annualRecord) {
        AnnualPreCarId = carId;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.add("annualRecord", AnnualRecode.toJsonObject(annualRecord));
        HttpConn.getInstance().sendMessage(data, 1240);
    }

    /**
     * 年检提醒添加 （1241）
     * registrationTime 上牌日期
     * type 提醒类型 0：智能提醒，1：自定义提醒
     * reminderDate 提醒日期
     **/
    public void ccmd1241_setAnnualInfo(long carId, long registrationTime,int type,long reminderDate) {
        AnnualPreCarId = carId;
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("registrationTime", registrationTime);
        data.addProperty("type", type);
        data.addProperty("reminderDate", reminderDate);
        HttpConn.getInstance().sendMessage(data, 1241);
    }

    // ============================scmd==================================
    /**
     * 取年检信息,首页
     **/
    private void backdata_1239_getAnnualInfoByCar(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            int       isAlert  = OJsonGet.getInteger(obj, "isAlert");//是否已设提醒
            int       remainingDay  = OJsonGet.getInteger(obj, "remainingDay");//下次年检剩余天数
            JsonArray annualRecords = OJsonGet.getJsonArray(obj, "annualRecords");//年检消息记录
            ManagerAnnual.getInstance().saveCarAnnualRecode(AnnualPreCarId,remainingDay,annualRecords);
            ManagerAnnual.getInstance().setCarIsAlert(AnnualPreCarId,isAlert);
            ODispatcher.dispatchEvent(OEventName.ANNUAL_FROM_CAR_RESULTBACK);
        }
    }
    /**
     * 添加/修改年检记录 （1240）
     **/
    private void backdata_1240_setAnnualRecode(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            int       remainingDay  = OJsonGet.getInteger(obj, "remainingDay");//下次年检剩余天数
            JsonArray annualRecords = OJsonGet.getJsonArray(obj, "annualRecords");//年检消息记录
            ManagerAnnual.getInstance().saveCarAnnualRecode(AnnualPreCarId,remainingDay,annualRecords);
            ODispatcher.dispatchEvent(OEventName.ANNUAL_RECODE_RESULTBACK,true);
        }else{
            ODispatcher.dispatchEvent(OEventName.ANNUAL_RECODE_RESULTBACK,false);
        }
    }
    /**
     * 年检提醒添加 （1241）
     **/
    private void backdata_1241_setAnnualInfo(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            int       remainingDay  = OJsonGet.getInteger(obj, "remainingDay");//下次年检剩余天数,返回-1为未设置
            JsonArray annualRecords = OJsonGet.getJsonArray(obj, "annualRecords");//年检消息记录
            ManagerAnnual.getInstance().saveCarAnnualRecode(AnnualPreCarId,remainingDay,annualRecords);
            ODispatcher.dispatchEvent(OEventName.ANNUAL_CHANGE_RESULTBACK,true);
        }else{
            ODispatcher.dispatchEvent(OEventName.ANNUAL_CHANGE_RESULTBACK,false);
        }
    }
}
