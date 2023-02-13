package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import model.carlist.DataCarInfo;
import model.maintain.DataMaintain;

public class ManagerDisPlay {
    public         DataDisPlay displayInfo;
    public         DataDisPlay displayInfo1;
    // ========================out======================
    private static ManagerDisPlay _instance;
    private ManagerDisPlay() {
    }
    public static ManagerDisPlay getInstance() {
        if (_instance == null)
            _instance = new ManagerDisPlay();
        return _instance;
    }
    public  void savedisplayInfo(JsonArray displays) {
        if (displays == null || displays.size() == 0)return;
        List<DataDisPlay> data = new ArrayList<DataDisPlay>();
        for (int i = 0; i < displays.size(); i++) {
            JsonObject objjj = displays.get(i).getAsJsonObject();
            DataDisPlay info = DataDisPlay.fromJsonObject(objjj);
            data.add(info);
        }
        if(data.size()==1){
            this.displayInfo= data.get(0);
        }
        if(data.size()==2){
            this.displayInfo=data.get(0);
            this.displayInfo1=data.get(1);
        }
    }
}
