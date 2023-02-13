package ctrl;

import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;

import common.http.HttpConn;
import model.ManagerLocator;

/**
 * 100-299
 */
public class OCtrlPositor {
    // ========================out======================
    private static OCtrlPositor _instance;
    protected OCtrlPositor() {
        init();
    }

    public static OCtrlPositor getInstance() {
        if (_instance == null)
            _instance = new OCtrlPositor();
        return _instance;
    }

    protected void init() {
    }


    public void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
        switch (protocol) {
            case 8001:
                backdata_8001_positorbind(obj,CACHE_ERROR);
                break;
            case 8002:
                backdata_8002_positorunbind(obj,CACHE_ERROR);
                break;
            case 8003:
                backdata_8003_positor_list_back(obj,CACHE_ERROR);
                break;
            case 8004:
                backdata_8004_qurry_positor_info_detail_all_back(obj,CACHE_ERROR);
                break;
            case 8005:
                backdata_8005_qurry_positor_set_back(obj,CACHE_ERROR);
                break;
            case 8006:
                backdata_8006_qurry_positor_info_back(obj,CACHE_ERROR);
                break;
            case 8007:
                backdata_8007_qurry_positor_trajectory_list_back(obj,CACHE_ERROR);
                break;
            case 8008:
                backdata_8008_qurry_positor_recording_list_back(obj,CACHE_ERROR);
                break;
            case 8009:
                backdata_8009_qurry_positor_Alert_message_list_back(obj,CACHE_ERROR);
                break;
            case 8010:
                backdata_8010_positor_command_down_back(obj,CACHE_ERROR);
                break;
            case 8011:
                backdata_8011_positor_fence_set_back(obj,CACHE_ERROR);
                break;
        }
    }
    // ============================ccmd==================================

    /**
     * 定位器绑定（8001）
     **/
    public void ccmd8001_positor_bind(String deviceNum) {
        JsonObject data = new JsonObject();
        data.addProperty("deviceNum", deviceNum);
        HttpConn.getInstance().sendMessage(data, 8001);
    }
    /**
     * 定位器解绑（8002）
     **/
    public void ccmd8002_positor_unbind(String deviceNum) {
        JsonObject data = new JsonObject();
        data.addProperty("deviceNum", deviceNum);
        HttpConn.getInstance().sendMessage(data, 8002);
    }
    /**
     * 拿定位器列表（8003）
     **/
    public void ccmd8003_get_positor_list() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 8003);
    }
    /**
     * 拿定位器查询详情对象所有（8004）
     **/
    public void ccmd8004_get_qurry_positor_Info_detail_all(String deviceNum) {
        JsonObject data = new JsonObject();
        data.addProperty("deviceNum", deviceNum);
        HttpConn.getInstance().sendMessage(data, 8004);
    }
    /**
     * 拿定位器 查询 设置对象（8005）
     **/
    public void ccmd8005_get_qurry_positor_set(String deviceNum) {
        JsonObject data = new JsonObject();
        data.addProperty("deviceNum", deviceNum);
        HttpConn.getInstance().sendMessage(data, 8005);
    }

    /**
     * 拿定位器 轨迹列表（8007）
     **/
    public void ccmd8007_get_qurry_positor_trajectory_list(String deviceNum,int start,int size,long startTime,long endTime) {
        JsonObject data = new JsonObject();
        data.addProperty("deviceNum", deviceNum);
        data.addProperty("start", start);
        data.addProperty("size", size);
        data.addProperty("startTime", startTime);
        data.addProperty("endTime", endTime);
        HttpConn.getInstance().sendMessage(data, 8007);
    }
    /**
     * 拿定位器 录音列表（8008）
     **/
    public void ccmd8008_get_qurry_positor_recording_list(String deviceNum,int start,int size) {
        JsonObject data = new JsonObject();
        data.addProperty("deviceNum", deviceNum);
        data.addProperty("start", start);
        data.addProperty("size", size);
        HttpConn.getInstance().sendMessage(data, 8008);
    }

    /**
     * 拿定位器 预警消息列表（8009）
     **/
    public void ccmd8009_get_qurry_positor_Alert_message_list(String deviceNum,int start,int size) {
        JsonObject data = new JsonObject();
        data.addProperty("deviceNum", deviceNum);
        data.addProperty("start", start);
        data.addProperty("size", size);
        HttpConn.getInstance().sendMessage(data, 8009);
    }
    /**
     * 定位器 命令下发（8010）
     **/
    public void ccmd8010_locator_comand_down(int id,int type,int[] instruct) {
        Gson gson = new Gson();
        String    json = gson.toJson(instruct);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        JsonObject data = new JsonObject();
        data.addProperty("id", id);
        data.addProperty("type", type);
        data.add("instruct", arr);
        HttpConn.getInstance().sendMessage(data, 8010);
        if (BuildConfig.DEBUG) Log.e("命令下发", "instruct0"+instruct[0]+"  instruct1"+instruct[1]+"instruct2"+instruct[2]+"instruct3"+instruct[3]+arr);
    }
    /**
     * 定位器 围栏设置（8011）
     **/
    public void ccmd8011_locator_fence_set(String deviceNum,int redius,int isOpen) {
        JsonObject data = new JsonObject();
        data.addProperty("deviceNum", deviceNum);
        data.addProperty("redius", redius);
        data.addProperty("isOpen", isOpen);
        HttpConn.getInstance().sendMessage(data, 8011);
    }

    // ============================scmd==================================
    /**
     * 定位器绑定（8001 ）
     **/
    public void backdata_8001_positorbind(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {

        }
    }
    /**
     * 定位器绑定（8002）
     **/
    public void backdata_8002_positorunbind(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {

        }
    }
    /**
     * 定位器列表（8003）
     **/
    public void backdata_8003_positor_list_back(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray positionerList = OJsonGet.getJsonArray(obj, "positionerList");//积分明细列表
            ManagerLocator.getInstance().saveLocatorListInfo(positionerList);
            ODispatcher.dispatchEvent(OEventName.POSITOR_LIST_BACK);
        }
    }
    /**
     * 定位器查询详情对象（8004）
     **/
    public void backdata_8004_qurry_positor_info_detail_all_back(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject positioner = OJsonGet.getJsonObject(obj,"positioner");
            ManagerLocator.getInstance().saveCacheLocatorBean(positioner);
            JsonObject positionerPositionNow = OJsonGet.getJsonObject(obj,"positionerPositionNow");
            ManagerLocator.getInstance().savecachePositorInfoBean(positionerPositionNow);
            JsonObject positionerSet = OJsonGet.getJsonObject(obj,"positionerSet");
            ManagerLocator.getInstance().savecachePositorSetBean(positionerSet);
            ODispatcher.dispatchEvent(OEventName.POSITOR_DETAIL_ALL_BACK);
        }
    }
    /**
     * 定位器 查询  设置对象（8005）
     **/
    public void backdata_8005_qurry_positor_set_back(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject positionerSet = OJsonGet.getJsonObject(obj,"positionerSet");
            ManagerLocator.getInstance().savecachePositorSetBean(positionerSet);
            ODispatcher.dispatchEvent(OEventName.POSITOR_SET_BACK);
        }
    }

    /**
     * 定位器 查询 信息对象（8006）
     **/
    public void backdata_8006_qurry_positor_info_back(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject positionerPositionNow = OJsonGet.getJsonObject(obj,"positionerPositionNow");
            ManagerLocator.getInstance().savecachePositorInfoBean(positionerPositionNow);
            ODispatcher.dispatchEvent(OEventName.POSITOR_INFO_BACK);
        }
    }
    /**
     * 定位器 查询 轨迹列表（8007）
     **/
    public void backdata_8007_qurry_positor_trajectory_list_back(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray positionerTrail = OJsonGet.getJsonArray(obj, "positionerTrail");//积分明细列表
            ManagerLocator.getInstance().savePositorTrailListInfo(positionerTrail);
            ODispatcher.dispatchEvent(OEventName.POSITOR_TRAIL_LIST_BACK);
        }
    }

    /**
     * 定位器 查询 录音列表（8008）
     **/
    public void backdata_8008_qurry_positor_recording_list_back(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray recording = OJsonGet.getJsonArray(obj, "recording");//积分明细列表
            ManagerLocator.getInstance().savePositorRecordingListInfo(recording);
            ODispatcher.dispatchEvent(OEventName.POSITOR_RECORDING_LIST_BACK);
        }
    }

    /**
     * 定位器 查询 预警消息列表（8009）
     **/
    public void backdata_8009_qurry_positor_Alert_message_list_back(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray positionerWarnRecord = OJsonGet.getJsonArray(obj, "positionerWarnRecord");//积分明细列表
            ManagerLocator.getInstance().savePositionerWarnRecordListInfo(positionerWarnRecord);
            ODispatcher.dispatchEvent(OEventName.POSITOR_WARNING_RECORD_LIST_BACK);
        }
    }
    /**
     * 定位器 命令下发（8010）
     **/
    public void backdata_8010_positor_command_down_back(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.POSITOR_COMMAND_DOWN_BACK);
        }
    }
    /**
     * 定位器 设置围栏结果（8011）
     **/
    public void backdata_8011_positor_fence_set_back(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.POSITOR_FENCE_SET_BACK);
        }
    }



    /**
     * 获取积分接口（1125）服务端没法检测到的积分途径
     * type 1：分享软件
     **/
    public void backdata_1125_newScore(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            String comment = OJsonGet.getString(obj, "comment");//toast展示的内容
            if(comment ==null || comment.equals(""))return;
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,comment);
        }
    }



}
