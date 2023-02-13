package model;


//import common.PheadHttp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.wearkulala.www.wearfunc.WearReg;

import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import common.PHeadHttp;
import common.blue.BlueLinkReceiver;
import common.linkbg.BootBroadcastReceiver;
import model.loginreg.DataUser;

public class ManagerLoginReg {
    private        DataUser        user;
    private         List<DataUser>    userHistory;
    public static  String loginPhoneNum="";
    // ========================out======================
    private static ManagerLoginReg _instance;

    private ManagerLoginReg() {
        WearReg.getInstance().setManagerLoginRegListener(new WearReg.ManagerLoginRegListener() {
            @Override
            public long getUserId() {
                DataUser user = getCurrentUser();
                if(user == null)return -1;
                return user.userId;
            }
            @Override
            public String getWatchToken() {
                return PHeadHttp.getUserWatchToken();
            }
            @Override
            public void changeUserWatchToken(long userId, String watchToke) {
                PHeadHttp.changeUserWatchToken(userId,watchToke);
            }
        });
    }

    public static ManagerLoginReg getInstance() {
        if (_instance == null)
            _instance = new ManagerLoginReg();
        return _instance;
    }

    public DataUser getCurrentUser() {
        if(user == null){
            //用户信息确定不要异步
            String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("user");
            JsonObject obj = ODBHelper.convertJsonObject(result);
            if (obj != null) {
                DataUser userr = DataUser.fromJsonObject(obj);
                if (userr.userId != 0) {
                    user = userr;
                    PHeadHttp.changeUserInfo(user.userId);//初始有用户就要改useid
                    BootBroadcastReceiver.initOrChangeSocket();
                }
            }
        }
        if(user == null)return new DataUser();
        return user;
    }

    public List<DataUser>  getUserHistory(){
        if(userHistory == null || userHistory.size() == 0){
            userHistory = new ArrayList<DataUser>();
            String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("userHistory");
            JsonArray array = ODBHelper.convertJsonArray(result);
            if(array!=null){
                userHistory = DataUser.fromJsonArray(array);
            }
        }
        return userHistory;
    }

    // ========================out======================
    public boolean checkIsFirstLogin() {
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("firstLogin");
        JsonObject obj = ODBHelper.convertJsonObject(result);
        if (obj == null) return false;
        String isfist = OJsonGet.getString(obj, "firstLogin");
        if (isfist.equals("true")) return true;
        return false;
    }

    public void saveFristLogin() {
        JsonObject obj = new JsonObject();
        obj.addProperty("firstLogin", "true");
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("firstLogin", ODBHelper.convertString(obj));
    }

    //user为ＮＵＬＬ就是没登录
    public void exitLogin() {
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("user", ODBHelper.convertString(new DataUser().toJsonObject()));
        PHeadHttp.changeUserInfo(0);
        if(user!=null)PHeadHttp.changeUserToken(user.userId,"");
        user = null;
        BootBroadcastReceiver.initOrChangeSocket();
        BlueLinkReceiver.needChangeCar(0,"","",0,"",0);
        BlueLinkReceiver.needChangeLcdKey(0,"","",0,0,0);
    }

    public void saveUserInfo(JsonObject userr) {
        if (userr == null)
            return;
        user = DataUser.fromJsonObject(userr);
        if (user == null)
            return;
        //用户信息改变，可能是重登录
        PHeadHttp.changeUserInfo(user.userId);
        BootBroadcastReceiver.initOrChangeSocket();
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("user",ODBHelper.convertString(this.user.toJsonObject()));
    }

    public void saveUserInfoforSetQue(boolean hasQus) {
        if (hasQus) {
            if(user==null)return;
            this.user.hasSecretQuestion = 1;
        } else {
            if(user==null)return;
            this.user.hasSecretQuestion = 0;
        }
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("user", ODBHelper.convertString(this.user.toJsonObject()));
    }
    // ===========================================================
    public void putOneUserHistory(JsonObject userr) {
        if (userr == null)
            return;
        DataUser  serviceBackUser = DataUser.fromJsonObject(userr);
        if (serviceBackUser == null)
            return;
        if(userHistory == null)userHistory = new ArrayList<DataUser>();
        if(ManagerLoginReg.loginPhoneNum==null&&ManagerLoginReg.loginPhoneNum.length()!=11)return;
        if(serviceBackUser.phoneNum==null||serviceBackUser.phoneNum.equals(""))return;
        DataUser historyUser=serviceBackUser;
        historyUser.phoneNum=ManagerLoginReg.loginPhoneNum;
        for(DataUser dataUser:userHistory){
            if(dataUser.phoneNum.equals(historyUser.phoneNum) || dataUser.userId == historyUser.userId){
                return;
            }
        }
        userHistory.add(historyUser);//没有就新增
        JsonArray  arr  = DataUser.toJsonArray(userHistory);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("userHistory", ODBHelper.convertString(arr));
    }
    public void clearOneUserHistory(String phoneNum) {
        if(phoneNum == null)return;
        if(userHistory == null)return;
        boolean haveOld = false;
        for(int i = 0;i<userHistory.size();i++){
            DataUser dataUser = userHistory.get(i);
            if(dataUser.phoneNum.equals(phoneNum) ){
                userHistory.remove(i);
                JsonArray  arr  = DataUser.toJsonArray(userHistory);
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("userHistory", ODBHelper.convertString(arr));
                return;
            }
        }
    }
    public DataUser getUserFromHistory(String phoneNum){
        if(phoneNum == null || phoneNum.equals(""))return null;
        if(userHistory == null)return null;
        for(int i = 0;i<userHistory.size();i++){
            DataUser dataUser = userHistory.get(i);
            if(dataUser.phoneNum.equals(phoneNum) ){
               return dataUser;
            }
        }
        return null;
    }
}
