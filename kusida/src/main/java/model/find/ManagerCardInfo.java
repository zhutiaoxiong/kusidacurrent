package model.find;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.List;

import common.GlobalContext;
import model.ManagerLoginReg;

/**
 * Created by qq522414074 on 2016/9/13.
 */
public class ManagerCardInfo {
    /**
     * isShow int 是否展示 0：不展示，1：展示
     * closePic String 关闭宝箱的图片 如果本地存在则不需要再次获取
     * openPic String 开启宝箱图片 如果本地存在则不需要再次获取
     * cardInfo jsonObject 卡片详情 具体见：卡片对象
     */
    public static int isShow;
    public  static String closePic;
    public static String openPic;
    public static CardInfo cardInfo;
    public static CardInfo syntheticCard;
    private static ManagerCardInfo _instance;
    public List<CardDetails> simplecardList;
    public List<CardDetails> middlecardList;
    public List<CardDetails> highcardList;
    public List<CardDetails> luxurycardList;
    public List<CardDetails> moreLuxurycardList;
    public List<CardDetails> extremecardList;

    public static ManagerCardInfo getInstance() {
        if (_instance == null)
            _instance = new ManagerCardInfo();
        return _instance;
    }
    public void savesyntheticCardInfo(JsonObject obj){
        syntheticCard=CardInfo.fromJsonObject(obj);
    }
    public void saveCardInfo(JsonObject obj) {
       closePic = OJsonGet.getString(obj, "closePic");
        isShow = OJsonGet.getInteger(obj, "isShow");
        openPic = OJsonGet.getString(obj, "openPic");
        JsonObject cardInfos = OJsonGet.getJsonObject(obj, "cardInfo");
        cardInfo = CardInfo.fromJsonObject(cardInfos);
    }
    public List<CardDetails> useCardList(int mark){
        switch (mark){
            case 0:return simplecardList;
            case 1:return middlecardList;
            case 2:return highcardList;
            case 3:return luxurycardList;
            case 4:return moreLuxurycardList;
            case 5:return extremecardList;
        }
        return null;
    }


    public void saveCardList(JsonObject obj) {
        if (obj != null) {
            JsonArray simpleCards = OJsonGet.getJsonArray(obj, "simpleCards");
            simplecardList = CardDetails.fromJsonArray(simpleCards);
            JsonArray middleCards = OJsonGet.getJsonArray(obj, "middleCards");
            middlecardList = CardDetails.fromJsonArray(middleCards);
            JsonArray highCards = OJsonGet.getJsonArray(obj, "highCards");
            highcardList = CardDetails.fromJsonArray(highCards);
            JsonArray luxuryCards = OJsonGet.getJsonArray(obj, "luxuryCards");
            luxurycardList = CardDetails.fromJsonArray(luxuryCards);
            JsonArray moreLuxuryCards = OJsonGet.getJsonArray(obj, "moreLuxuryCards");
            moreLuxurycardList = CardDetails.fromJsonArray(moreLuxuryCards);
            JsonArray extremeCards = OJsonGet.getJsonArray(obj, "extremeCards");
            extremecardList = CardDetails.fromJsonArray(extremeCards);
        }
//        else{
//            this.simplecardList = new ArrayList<>();
//            this.middlecardList=new ArrayList<>();
//            this.highcardList = new ArrayList<>();
//            this.luxurycardList=new ArrayList<>();
//            this.moreLuxurycardList = new ArrayList<>();
//            this.extremecardList=new ArrayList<>();
//        }
//        if (simplecardList == null) {
//            simplecardList = new ArrayList<>();
//        }
//        if (middlecardList == null) {
//            middlecardList = new ArrayList<>();
//        }
//        if (highcardList == null) {
//            highcardList = new ArrayList<>();
//        }
//        if (luxurycardList == null) {
//            luxurycardList = new ArrayList<>();
//        }
//        if (moreLuxurycardList == null) {
//            moreLuxurycardList = new ArrayList<>();
//        }
//        if (extremecardList == null) {
//            extremecardList = new ArrayList<>();
//        }
        saveCardListLocal();
    }

    private void saveCardListLocal() {
        JsonObject obj1 = new JsonObject();
        long userId = ManagerLoginReg.getInstance().getCurrentUser() == null ? 0 : ManagerLoginReg.getInstance().getCurrentUser().userId;
        JsonArray simpleArr = ManagerCardInfo.toJsonArrayLocal(simplecardList);
        obj1.add(userId + "simpleArr", simpleArr);
        ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(userId,"simpleArr", ODBHelper.convertString(obj1));
        JsonObject obj2 = new JsonObject();
        JsonArray middleArr = ManagerCardInfo.toJsonArrayLocal(middlecardList);
        obj2.add(userId + "middleArr", middleArr);
        ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(userId,"middleArr", ODBHelper.convertString(obj2));
        JsonObject obj3 = new JsonObject();
        JsonArray highArr = ManagerCardInfo.toJsonArrayLocal(highcardList);
        obj3.add(userId + "highArr", highArr);
        ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(userId,"highArr", ODBHelper.convertString(obj3));
        JsonObject obj4 = new JsonObject();
        JsonArray luxuryArr = ManagerCardInfo.toJsonArrayLocal(luxurycardList);
        obj4.add(userId + "luxuryArr", luxuryArr);
        ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(userId,"luxuryArr", ODBHelper.convertString(obj4));
        JsonObject obj5 = new JsonObject();
        JsonArray moreLuxuryArr = ManagerCardInfo.toJsonArrayLocal(moreLuxurycardList);
        obj5.add(userId + "moreLuxuryArr", moreLuxuryArr);
        ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(userId,"moreLuxuryArr", ODBHelper.convertString(obj5));
        JsonObject obj6 = new JsonObject();
        JsonArray extremeArr = ManagerCardInfo.toJsonArrayLocal(extremecardList);
        obj6.add(userId + "extremeArr", extremeArr);
        ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(userId,"extremeArr", ODBHelper.convertString(obj6));
    }

    public void loadCardListLocal() {
        long userId = ManagerLoginReg.getInstance().getCurrentUser() == null ? 0 : ManagerLoginReg.getInstance().getCurrentUser().userId;
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(userId,"simpleArr");
        JsonObject obj1 = ODBHelper.convertJsonObject(result);
        if (obj1 != null) {
            JsonArray arr1 = OJsonGet.getJsonArray(obj1, userId + "simpleArr");
            simplecardList = CardDetails.fromJsonArray(arr1);
        }
        result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(userId,"middleArr");
        JsonObject obj2 = ODBHelper.convertJsonObject(result);
        if (obj2 != null) {
            JsonArray arr2 = OJsonGet.getJsonArray(obj2, userId + "middleArr");
            middlecardList = CardDetails.fromJsonArray(arr2);
        }
        result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(userId,"highArr");
        JsonObject obj3 = ODBHelper.convertJsonObject(result);
        if (obj3 != null) {
            JsonArray arr3 = OJsonGet.getJsonArray(obj3, userId + "highArr");
            highcardList = CardDetails.fromJsonArray(arr3);
        }
        result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(userId,"luxuryArr");
        JsonObject obj4 = ODBHelper.convertJsonObject(result);
        if (obj4 != null) {
            JsonArray arr4 = OJsonGet.getJsonArray(obj4, userId + "luxuryArr");
            luxurycardList = CardDetails.fromJsonArray(arr4);
        }
        result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(userId,"moreLuxuryArr");
        JsonObject obj5 = ODBHelper.convertJsonObject(result);
        if (obj5 != null) {
            JsonArray arr5 = OJsonGet.getJsonArray(obj5, userId + "moreLuxuryArr");
            moreLuxurycardList = CardDetails.fromJsonArray(arr5);
        }
        result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(userId,"extremeArr");
        JsonObject obj6 = ODBHelper.convertJsonObject(result);
        if (obj6 != null) {
            JsonArray arr6 = OJsonGet.getJsonArray(obj6, userId + "extremeArr");
            extremecardList = CardDetails.fromJsonArray(arr6);
        }
    }

    public static JsonArray toJsonArrayLocal(List<CardDetails> list) {
        Gson gson = new Gson();
        JsonArray arr = null;
        if (list != null) {
            String json = gson.toJson(list);
            arr = gson.fromJson(json, JsonArray.class);
        }
        return arr;
    }
}
