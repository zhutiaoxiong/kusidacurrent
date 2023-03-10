package ctrl;

import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.MD5;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.wearkulala.www.wearfunc.WearReg;

import common.GlobalContext;
import common.PHeadHttp;
import common.http.HttpConn;
import common.linkbg.BootBroadcastReceiver;
import model.ManagerAnswer;
import model.ManagerCarList;
import model.ManagerCommon;
import model.ManagerGesture;
import model.ManagerLoginReg;
import model.loginreg.DataForTiJiaoFindWay;
import model.loginreg.DataResetPassword;
import model.loginreg.DataUser;
import model.maintain.ManagerMaintainList;

/**
 * 100-299
 */
public class OCtrlRegLogin {
    // ========================out======================
    private static OCtrlRegLogin _instance;

    private OCtrlRegLogin() {
        init();
    }

    public static OCtrlRegLogin getInstance() {
        if (_instance == null)
            _instance = new OCtrlRegLogin();
        return _instance;
    }

    // ========================out======================
    protected void init() {
    }

    // ========================out======================
    public void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
        switch (protocol) {
            case 1101:
                backdata_1101_getVerficode(obj,CACHE_ERROR);
                break;
            case 1102:
                backdata_1102_reg(obj,CACHE_ERROR);
                break;
            case 1103:
                backdata_1103_login(obj,CACHE_ERROR);
                break;
            case 1104:
                backdata_1104_checkPassword(obj,CACHE_ERROR);
                break;
            case 1107:
                backdata_1107_findpassFromIdcard(obj,CACHE_ERROR);
                break;
            case 1108:
                backdata_1108_changePassword(obj,CACHE_ERROR);
                break;
            case 1109:
                backdata_1109_exitlogin(obj,CACHE_ERROR);
                break;
            case 1110:
                backdata_1110_changeUserInfo(obj,CACHE_ERROR);
                break;
            case 1111:
                backdata_1111_changeIdcard(obj,CACHE_ERROR);
                break;
            case 1112:
                backdata_1112_changeMail(obj,CACHE_ERROR);
                break;
            case 1113:
                backdata_1113_changePhoneNum(obj,CACHE_ERROR);
                break;
            case 1114:
                backdata_1114_findpassFromMail(obj,CACHE_ERROR);
                break;
            case 1116:
                backdata_1116_checkVerifycode(obj,CACHE_ERROR);
                break;
            case 1117:
                backdata_1117_resetPassword_from_phoneNum(obj,CACHE_ERROR);
                break;
            case 1120:
                backdata_1120_password_problem(obj,CACHE_ERROR);
            case 1121:
                backdata_1121_select_findpassword_way(obj,CACHE_ERROR);
                break;
            case 1122:
                backdata_1122_get_findpassword_way(obj,CACHE_ERROR);
                break;
            case 1126:
                backdata_1126_getUserInfo(obj,CACHE_ERROR);
                break;
        }
    }
    // ============================ccmd==================================

    /**
     * ??????????????????????????????,????????????result entrance 1????????????2?????????????????????3???????????????
     * entrance :1????????????2?????????????????????3??????????????????4??????????????????5?????????????????????
     * type : 1????????????2?????????
     **/
    public void ccmd1101_getVerificationCode(String phoneNum, int entrance, int type) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("entrance", entrance);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1101);
    }

    /**
     * ??????????????????
     **/
    public void ccmd1102_Reg(String phoneNum, String password, String verifyCode) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("password", MD5.MD5generator(password));
        data.addProperty("verifyCode", verifyCode);
        HttpConn.getInstance().sendMessage(data, 1102);
    }

    /**
     *
     **/
    public void ccmd1103_login(String phoneNum, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("password", MD5.MD5generator(password));
        HttpConn.getInstance().sendMessage(data, 1103);
    }

    /**
     * ????????????
     **/
    public void ccmd1109_exitlogin() {
        HttpConn.getInstance().sendMessage(null, 1109);
    }

    /**
     * ????????????
     **/
    public void ccmd1108_changePassword(String oldpass, String newpass) {
        JsonObject data = new JsonObject();
        data.addProperty("oldPwd", MD5.MD5generator(oldpass));
        data.addProperty("pwd", MD5.MD5generator(newpass));
        HttpConn.getInstance().sendMessage(data, 1108);
    }

    /**
     * ????????????
     **/
    public void ccmd1104_checkPassword(String pass, int entrance) {
        JsonObject data = new JsonObject();
        data.addProperty("password", MD5.MD5generator(pass));
        data.addProperty("entrance", entrance);
        HttpConn.getInstance().sendMessage(data, 1104);
    }

    public void ccmd1104_checkPassword(String pass) {
        JsonObject data = new JsonObject();
        data.addProperty("password", MD5.MD5generator(pass));
        HttpConn.getInstance().sendMessage(data, 1104);
    }

    /**
     * ???????????????
     **/
    public void ccmd1113_changePhoneNum(String phoneNum, String verifyCode, String verifyStr) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("verifyCode", verifyCode);
        data.addProperty("verifyStr", verifyStr);
        HttpConn.getInstance().sendMessage(data, 1113);
    }

    /**
     * ??????E-mail
     **/
    public void ccmd1112_changeMail(String email, String verifyStr) {
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("verifyStr", verifyStr);
        HttpConn.getInstance().sendMessage(data, 1112);
    }

    /**
     * ??????????????????
     **/
    public void ccmd1111_changeIdcard(String password, String idCard) {
        JsonObject data = new JsonObject();
        data.addProperty("password", MD5.MD5generator(password));
        data.addProperty("idCard", idCard);
        HttpConn.getInstance().sendMessage(data, 1111);
    }
    /**
     * ?????????????????????
     */

    /**
     * ??????????????????
     **/
    public void ccmd1114_findpassFromMail(String phoneNum, int entrance) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("entrance", entrance);
        HttpConn.getInstance().sendMessage(data, 1114);
    }

    /**
     * ?????????????????????
     **/
    public void ccmd1107_findpassFromIdcard(String phoneNum, String idCard, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("idCard", idCard);
        data.addProperty("password", MD5.MD5generator(password));
        HttpConn.getInstance().sendMessage(data, 1107);
    }

    /**
     * ???????????????
     **/
    public void ccmd1110_changeUserInfo(JsonObject userInfo) {
        JsonObject data = new JsonObject();
        data.add("userInfo", userInfo);
        HttpConn.getInstance().sendMessage(data, 1110);
    }
    public void ccmd1126_getUserInfo() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1126);
        Log.e("MainFragment", "send 1126 " );
    }
    /**
     * ???????????????
     **/
    public void backdata_1126_getUserInfo(JsonObject obj,String CACHE_ERROR) {
        Log.e("MainFragment", "back 1126 " );
        if (CACHE_ERROR.equals("")) {
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            ODispatcher.dispatchEvent(OEventName.CHANGE_USER_INFO_OK);
        }
    }


    /**
     * ??????????????? entrance 1????????????2?????????????????????3???????????????
     **/
    public void ccmd1116_checkVerifycode(String phoneNum, String verifyCode, int entrance) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("verifyCode", verifyCode);
        data.addProperty("entrance", entrance);
        HttpConn.getInstance().sendMessage(data, 1116);
    }

    /**
     * ?????????????????????
     **/
    public void ccmd1117_resetPassword_from_phoneNum(String phoneNum, String verifyStr, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("phoneNum", phoneNum);
        data.addProperty("verifyStr", verifyStr);
        data.addProperty("password", MD5.MD5generator(password));
        HttpConn.getInstance().sendMessage(data, 1117);
    }

    /**
     * ??????????????????
     */

    public void ccmd1120_submit_password_problem(Integer[] problemId, String[] answerId, int type, String phoneNum, int entrance, String verifyStr) {
        DataResetPassword eafcer = new DataResetPassword();
        eafcer.problemId = problemId;
        eafcer.answerId = answerId;
        eafcer.type = type;
        eafcer.phoneNum = phoneNum;
        eafcer.entrance = entrance;
        eafcer.verifyStr = verifyStr;
        JsonObject data = eafcer.toJsonObject(eafcer);
        HttpConn.getInstance().sendMessage(data, 1120);
    }


    public void ccmd1121_select_findpassword_way(Integer[] secretTypes) {
        DataForTiJiaoFindWay mibaofangshi = new DataForTiJiaoFindWay();
        mibaofangshi.secretTypes = secretTypes;
        JsonObject data = mibaofangshi.toJsonObject(mibaofangshi);
        HttpConn.getInstance().sendMessage(data, 1121);
    }

    public void ccmd1122_get_findpassword_way() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1122);
    }

    // ============================scmd==================================

    /**
     * ???????????????
     */
    public void backdata_1101_getVerficode(JsonObject obj,String CACHE_ERROR) {
        if (!CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.VERIFICATION_CODE_BACKOK);
        }

    }

    /**
     * ??????????????????
     **/
    public void backdata_1102_reg(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            String token = OJsonGet.getString(obj, "token");
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            DataUser user = DataUser.fromJsonObject(userInfo);
            if(user!=null)PHeadHttp.changeUserToken(user.userId,token);
            ODispatcher.dispatchEvent(OEventName.REGISTER_SUCCESS);
            //??????????????????
            OCtrlCommon.getInstance().ccmd1305_getSwitchInfo();
            //?????????ID
            BootBroadcastReceiver.initOrChangeSocket();
        } else {
            ODispatcher.dispatchEvent(OEventName.REG_FAILED);
        }
    }

    /**
     * ???????????? hasMaintance int ??????????????????????????? 0???????????????1????????????
     **/
    public void backdata_1103_login(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject result = OJsonGet.getJsonObject(obj, "result");
            JsonArray carInfos = OJsonGet.getJsonArray(obj, "carInfos");
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            String token = OJsonGet.getString(obj, "token");
            String watchToken = OJsonGet.getString(obj, "watchToken");
            int isFirst = OJsonGet.getInteger(obj, "isFirst");
            int isOpen = OJsonGet.getInteger(obj, "isOpen");//????????????????????????	0????????????1?????????
            String signPassword = OJsonGet.getString(obj, "signPassword");//???MD5???????????????????????????????????????kulala_sign_ + 0-8
            ManagerGesture.getInstance().saveGesture(isOpen, signPassword);
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            ManagerLoginReg.getInstance().putOneUserHistory(userInfo);
            DataUser user = DataUser.fromJsonObject(userInfo);
            if(user!=null)PHeadHttp.changeUserToken(user.userId,token);
            if(user!=null)PHeadHttp.changeUserWatchToken(user.userId,watchToken);
            int hasMaintance = OJsonGet.getInteger(obj, "hasMaintance");
            ManagerMaintainList.getInstance().savehasMaintance(hasMaintance);
            ManagerCarList.getInstance().saveCarList(carInfos,"backdata_1103_login");
            //2018/08/09???????????????
//            JsonObject shakeInfo = obj.getAsJsonObject("shakeInfo");
//            BlueLinkNetSwitch.saveSwitchShake(shakeInfo);
            OCtrlAuthorization.getInstance().ccmd1210_getuserlist();
            ODispatcher.dispatchEvent(OEventName.LOGIN_SUCCESS, isFirst);
            //??????????????????
            OCtrlCommon.getInstance().ccmd1305_getSwitchInfo();
            //??????????????????
            OCtrlCar.getInstance().ccmd1203_getcarlist();
            //?????????ID
            BootBroadcastReceiver.initOrChangeSocket();
             if (BuildConfig.DEBUG) Log.e("isUserExitUser", "?????????false" );
            GlobalContext.isUserExitUser=false;
            //???????????????????????????
            int watchOpen = OJsonGet.getInteger(obj, "watchOpen");
            if(watchOpen == 1)
                WearReg.getInstance().wearServiceChangeUser(ManagerLoginReg.getInstance().getCurrentUser().userId,PHeadHttp.getUserWatchToken());
        } else {
            ODispatcher.dispatchEvent(OEventName.LOGIN_FAILED);
        }

    }

    /**
     * ????????????
     **/
    public void backdata_1109_exitlogin(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
           ManagerCommon.getInstance().exitToLogin("");
        }
    }

    /**
     * ????????????
     **/
    public void backdata_1108_changePassword(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals(""))
            ODispatcher.dispatchEvent(OEventName.CHANGE_PASSWORD_RESULTBACK);
    }

    /**
     * ???????????????
     **/
    public void backdata_1113_changePhoneNum(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            ODispatcher.dispatchEvent(OEventName.CHANGE_PHONENUM_RESULTBACK);
        }
    }

    /**
     * ????????????
     **/
    public void backdata_1104_checkPassword(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            String verifyStr = OJsonGet.getString(obj, "verifyStr");
            ManagerAnswer.verifyStr = verifyStr;
            ODispatcher.dispatchEvent(OEventName.CHECK_PASSWORD_RESULTBACK, true);
        } else {
            ODispatcher.dispatchEvent(OEventName.CHECK_PASSWORD_RESULTBACK, false);
        }
    }

    /**
     * ??????E-mail
     **/
    public void backdata_1112_changeMail(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            ODispatcher.dispatchEvent(OEventName.CHANGE_MAIL_RESULTBACK);
        }
    }

    /**
     * ??????????????????
     **/
    public void backdata_1111_changeIdcard(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            ODispatcher.dispatchEvent(OEventName.CHANGE_IDCARD_RESULTBACK);
        }
    }

    /**
     * ??????????????????
     **/
    public void backdata_1114_findpassFromMail(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            String message = OJsonGet.getString(obj, "message");
            ODispatcher.dispatchEvent(OEventName.FINDPASS_MAIL_RESULTBACK, message);
        }
    }

    /**
     * ?????????????????????
     **/
    public void backdata_1107_findpassFromIdcard(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.FINDPASS_IDCARD_RESULTBACK);
        }
    }

    /**
     * ???????????????
     **/
    public void backdata_1110_changeUserInfo(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject userInfo = obj.getAsJsonObject("userInfo");
            ManagerLoginReg.getInstance().saveUserInfo(userInfo);
            ODispatcher.dispatchEvent(OEventName.CHANGE_USER_INFO_OK);
        }
    }

    /**
     * ??????????????? entrance 1????????????2?????????????????????3???????????????
     **/
    public void backdata_1116_checkVerifycode(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            String verifyStr = OJsonGet.getString(obj, "verifyStr");
            ManagerAnswer.verifyStr = verifyStr;
            ODispatcher.dispatchEvent(OEventName.CHECK_VERIFYCODE_SUCCESS);
        }
    }

    /**
     * ?????????????????????
     **/
    public void backdata_1117_resetPassword_from_phoneNum(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.RESET_PASSWORD_FROM_PHONENUM_SUCCESS);
        }
    }

    public void backdata_1122_get_findpassword_way(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray ooo = OJsonGet.getJsonArray(obj, "secretTypes");
            ManagerAnswer.getInstance().saveSecretTypeslist(ooo);
            ODispatcher.dispatchEvent(OEventName.GET_FIND_PASSWORD_WAY);
        }
    }

    /**
     * ??????????????????
     *
     */
    public void backdata_1120_password_problem(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ManagerAnswer.verifyStr = OJsonGet.getString(obj, "verifyStr");
            ODispatcher.dispatchEvent(OEventName.SUBMMIT_PASSWORD_PROBLEM);
        }
    }
    public void backdata_1121_select_findpassword_way(JsonObject obj,String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.SELECT_FIND_PASSWAY);
        }
    }
}
