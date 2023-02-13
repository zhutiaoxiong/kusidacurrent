package ctrl;

import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import common.http.HttpConn;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;

import model.find.ManagerCardInfo;

/**
 * Created by qq522414074 on 2016/9/18.
 */
public class OCtrlCard {
    private static OCtrlCard _instance;

    protected OCtrlCard() {
        init();
    }

    public static OCtrlCard getInstance() {
        if (_instance == null)
            _instance = new OCtrlCard();
        return _instance;
    }
    protected void init() {
    }
    public void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
        switch (protocol) {
            case 1404:
                backdata_1404_getcardinfo(obj,CACHE_ERROR);
                break;
            case 1405:
               backdata_1405_getcardarray(obj, CACHE_ERROR);
                break;
            case 1406:
                backdata_1406_commit_cardResultBack(obj,CACHE_ERROR);
                break;
            case 1408:
                backdata_1408_give_cardResultBack(obj,CACHE_ERROR);
                break;
            case 1409:
                backdata_1409_synthetic_card(obj,CACHE_ERROR);
                break;
        }
    }
    public void ccmd1404_get_cardinfo(){
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1404);
    }
    public void ccmd1405_get_cardarray(){
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1405);
    }

    public void ccmd1406_commit_card(int cardId){
        JsonObject data = new JsonObject();
        data.addProperty("cardId", cardId);
        HttpConn.getInstance().sendMessage(data, 1406);
    }
    public void ccmd1408_give_card(int cardId, String phoneNum){
        JsonObject data = new JsonObject();
        data.addProperty("cardId", cardId);
        data.addProperty("phoneNum", phoneNum);
        HttpConn.getInstance().sendMessage(data, 1408);
    }
    public void ccmd1409_synthetic_card(int type){
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1409);
    }
    public void backdata_1404_getcardinfo(JsonObject obj,String CACHE_ERROR){
        if(CACHE_ERROR.equals("")){
            ManagerCardInfo.getInstance().saveCardInfo(obj);
            ODispatcher.dispatchEvent(OEventName.GET_CARDINFO_RESULTBACK);
        }
    }
    public void backdata_1405_getcardarray(JsonObject obj,String CACHE_ERROR){
        if(CACHE_ERROR.equals("")){
         ManagerCardInfo.getInstance().saveCardList(obj);
            ODispatcher.dispatchEvent(OEventName.GET_CARDINFO_LIST_RESULTBACK);
        }
    }
    public void backdata_1406_commit_cardResultBack (JsonObject obj,String CACHE_ERROR){
        if(CACHE_ERROR.equals("")){
            ManagerCardInfo.getInstance().saveCardList(obj);
          ODispatcher.dispatchEvent(OEventName.COMMIT_CARDINFO_RESULTBACK);
        }else{
            ODispatcher.dispatchEvent(OEventName.COMMIT_CARDINFO_RESULTBACK_FAILED);
        }
    }
    public void backdata_1408_give_cardResultBack (JsonObject obj,String CACHE_ERROR){
        if(CACHE_ERROR.equals("")){
            ManagerCardInfo.getInstance().saveCardList(obj);
//            ODispatcher.dispatchEvent(OEventName.CARD_GIVE_RESULT,1);
            ODispatcher.dispatchEvent(OEventName.CARD_GIVE_RESULT,"");
            ODispatcher.dispatchEvent(OEventName.GET_CARDINFO_LIST_RESULTBACK);
     }
        else{
            ODispatcher.dispatchEvent(OEventName.CARD_GIVE_RESULT,CACHE_ERROR);
        }
    }
    public void backdata_1409_synthetic_card (JsonObject obj,String CACHE_ERROR){
        if(CACHE_ERROR.equals("")){
            JsonObject cardInfo=OJsonGet.getJsonObject(obj,"newCard");
            ManagerCardInfo.getInstance().savesyntheticCardInfo(cardInfo);
            ManagerCardInfo.getInstance().saveCardList(obj);
            ODispatcher.dispatchEvent(OEventName.CARD_SYNTHETIC_RESULT,"");
            ODispatcher.dispatchEvent(OEventName.GET_CARDINFO_LIST_RESULTBACK);
        }
        else{
            ODispatcher.dispatchEvent(OEventName.CARD_SYNTHETIC_RESULT,CACHE_ERROR);
        }
    }
}
