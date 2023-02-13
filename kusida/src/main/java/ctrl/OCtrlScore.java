package ctrl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import common.http.HttpConn;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;

import model.ManagerScore;

/**
 * 100-299
 */
public class OCtrlScore {
    // ========================out======================
    private static OCtrlScore _instance;
    protected OCtrlScore() {
        init();
    }

    public static OCtrlScore getInstance() {
        if (_instance == null)
            _instance = new OCtrlScore();
        return _instance;
    }

    protected void init() {
    }


    public void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
        switch (protocol) {
            case 1123:
                backdata_1123_getScoreList(obj,CACHE_ERROR);
                break;
            case 1124:
                backdata_1124_getScoreDetail(obj,CACHE_ERROR);
                break;
            case 1125:
                backdata_1125_newScore(obj,CACHE_ERROR);
                break;
        }
    }
    // ============================ccmd==================================

    /**
     * 积分任务列表接口（1123）
     **/
    public void ccmd1123_getScoreList() {
        HttpConn.getInstance().sendMessage(null, 1123);
    }
    /**
     * 积分明细（1124）
     **/
    public void ccmd1124_getScoreDetail(long start,int size) {
        JsonObject data = new JsonObject();
        data.addProperty("start", start);
        data.addProperty("size", size);
        HttpConn.getInstance().sendMessage(data, 1124);
    }

    /**
     * 获取积分接口（1125）服务端没法检测到的积分途径
     * type 1：分享软件
     **/
    public void ccmd1125_newScore(int type) {
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1125);
    }

    // ============================scmd==================================
    /**
     * 积分任务列表接口（1123）
     **/
    public void backdata_1123_getScoreList(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            int score = OJsonGet.getInteger(obj, "score");//积分
            JsonArray everyDayInfos = OJsonGet.getJsonArray(obj, "everyDayInfos");//每日任务
            JsonArray newComerInfos = OJsonGet.getJsonArray(obj, "newComerInfos");//新手任务
            ManagerScore.getInstance().saveScoreAll(score);
            ManagerScore.getInstance().saveEveryDayInfos(everyDayInfos);
            ManagerScore.getInstance().saveNewComerInfos(newComerInfos);
            ODispatcher.dispatchEvent(OEventName.SCORE_LIST_RESULTBACK);
        }
    }
    /**
     * 积分明细（1124）
     **/
    public void backdata_1124_getScoreDetail(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray scoreInfos = OJsonGet.getJsonArray(obj, "scoreInfos");//积分明细列表
            ManagerScore.getInstance().saveScoreInfos(scoreInfos);
            ODispatcher.dispatchEvent(OEventName.SCORE_DETAIL_RESULTBACK);
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
