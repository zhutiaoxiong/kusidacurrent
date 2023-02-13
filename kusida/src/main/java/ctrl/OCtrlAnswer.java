package ctrl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import common.http.HttpConn;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;

import model.ManagerAnswer;

//import common.http.HttpConn;

/**
 * 100-299
 */
public class OCtrlAnswer{
    // ========================out======================
    private static OCtrlAnswer _instance;

    protected OCtrlAnswer() {
        init();
    }

    public static OCtrlAnswer getInstance() {
        if (_instance == null)
            _instance = new OCtrlAnswer();
        return _instance;
    }

    protected void init() {
    }


    public void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
        switch (protocol) {
            case 1119:
                backdata_1119_answer(obj,CACHE_ERROR);
                break;
        }
    }
    // ============================ccmd==================================

    /**
     * 取密保问题列表
     * type: 1：验证时下发的列表，2：设置和修改需要的问题列表
     **/
    public void ccmd1119_answer(String phoneNum, int type) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1119);
    }

    // ============================scmd==================================

    /**
     * 取密保问题列表
     **/
    public void backdata_1119_answer(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray secretInfos = OJsonGet.getJsonArray(obj, "secretInfos");//密保问题列表
            JsonArray secretTypes = OJsonGet.getJsonArray(obj, "secretTypes");//密保类型列表
            ManagerAnswer.getInstance().saveSecretTypeslist(secretTypes);
            ManagerAnswer.getInstance().saveSecretInfoslist(secretInfos);
            ODispatcher.dispatchEvent(OEventName.SECRETINFOS_RESULTBACK);
        }
    }


}
