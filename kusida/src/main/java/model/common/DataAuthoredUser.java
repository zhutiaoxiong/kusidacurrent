package model.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.ArrayList;
import java.util.List;

import model.carlist.DataCarInfo;
import model.loginreg.DataUser;

public class DataAuthoredUser {

    public long                    authorityId;
    public DataUser                userinfo;
    public DataCarInfo             carinfo;
    public List<DataAuthorization> authors;
    public long                    startTime;
    public long                    endTime;
    public int                     authorCount;
    public int                     alertCount;
    public boolean                 isSelected;

    public static DataAuthoredUser fromJsonObjectt(JsonObject object) {
        DataAuthoredUser info = new DataAuthoredUser();
        info.authorityId = OJsonGet.getLong(object, "authorityId");
        info.startTime = OJsonGet.getLong(object, "startTime");
        info.endTime = OJsonGet.getLong(object, "endTime");
        info.authorCount = OJsonGet.getInteger(object, "authorCount");
        info.alertCount = OJsonGet.getInteger(object, "alertCount");
        JsonObject userinfo = OJsonGet.getJsonObject(object, "userInfo");
        info.userinfo = DataUser.fromJsonObject(userinfo);
        JsonObject carinfo = OJsonGet.getJsonObject(object, "carInfo");
        info.carinfo = DataCarInfo.fromJsonObject(carinfo);
        JsonArray authorArr = OJsonGet.getJsonArray(object, "authors");
        info.authors = new ArrayList<DataAuthorization>();
        if (authorArr != null) {
            for (int i = 0; i < authorArr.size(); i++) {
                JsonObject        obj    = authorArr.get(i).getAsJsonObject();
                DataAuthorization author = new DataAuthorization();
                author.jsonObjectToAuthor(obj);
                info.authors.add(author);
            }
        }
        return info;
    }
}
