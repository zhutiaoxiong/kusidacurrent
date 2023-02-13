package model.nfc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;

import model.score.DataScore;

public class DataNfc {
    public long id;
    public long productId;
    public String deviceNum;
    public String deviceVersion;
    public String cardOne;
    public String cardTwo;
    public String cardThree;
    public String cardFour;
    public String cardFive;
    public String cardSix;
    public String cardSeven;
    public String cardEight;
    public String cardOneName;
    public String cardTwoName;
    public String cardThreeName;
    public String cardFourName;
    public String cardFiveName;
    public String cardSixName;
    public String cardSevenName;
    public String cardEightName;
    public Date createTime;
    public Date updateTime;
    public JsonObject toJsonObject() {
        Gson gson = new Gson();
        String     json = gson.toJson(this);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
    public static DataNfc fromJsonObject(JsonObject obj) {
        Gson     gson    = new Gson();
        DataNfc thisobj = gson.fromJson(obj, DataNfc.class);
        return thisobj;
    }
}
