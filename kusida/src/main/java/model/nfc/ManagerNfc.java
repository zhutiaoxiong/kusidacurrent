package model.nfc;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;

import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import model.gps.DataGpsPath;
import model.gps.DataGpsPoint;
import model.gps.SearchHistory;
import view.view4me.nfcmoudle.NfcData;

public class ManagerNfc {
    public DataNfc myDataNfc;
    private static ManagerNfc _instance;
    private List<NfcData> list;
    private List<NfcData> listForid;
    public static ManagerNfc getInstance() {
        if (_instance == null)
            _instance = new ManagerNfc();
        return _instance;
    }
    public void saveDataNfc(JsonObject JSON){
        myDataNfc=DataNfc.fromJsonObject(JSON);
        List<NfcData> myList=new ArrayList<>();
        if(myDataNfc!=null){
            myList.add(new NfcData(myDataNfc.cardOneName,myDataNfc.cardOne));
            myList.add(new NfcData(myDataNfc.cardTwoName,myDataNfc.cardTwo));
            myList.add(new NfcData(myDataNfc.cardThreeName,myDataNfc.cardThree));
            myList.add(new NfcData(myDataNfc.cardFourName,myDataNfc.cardFour));
            myList.add(new NfcData(myDataNfc.cardFiveName,myDataNfc.cardFive));
        }
        list=myList;
        List<NfcData> myListId=new ArrayList<>();
        if(myDataNfc!=null){
            myListId.add(new NfcData(myDataNfc.cardSixName,myDataNfc.cardSix));
            myListId.add(new NfcData(myDataNfc.cardSevenName,myDataNfc.cardSeven));
            myListId.add(new NfcData(myDataNfc.cardEightName,myDataNfc.cardEight));
        }
        listForid=myListId;
    }
    private DataNfc getDataNfc(){
        return myDataNfc;
    }
    public List<NfcData> getNfcDataForView(){
        return list;
    }
    public List<NfcData> getNfcDataForIdView(){
        return listForid;
    }
    public  List<NfcData> createinitICList(){
        List<NfcData> mysList=new ArrayList<>();
        if(mysList==null||mysList.size()==0){
            mysList.add(new NfcData("未检测到读卡器","0"));
            mysList.add(new NfcData("未检测到读卡器","0"));
            mysList.add(new NfcData("未检测到读卡器","0"));
            mysList.add(new NfcData("未检测到读卡器","0"));
            mysList.add(new NfcData("未检测到读卡器","0"));
        }
        return mysList;
    }
    public  List<NfcData> createinitIDList(){
        List<NfcData> mysList=new ArrayList<>();
        if(mysList==null||mysList.size()==0){
            mysList.add(new NfcData("未检测到读卡器","0"));
            mysList.add(new NfcData("未检测到读卡器","0"));
            mysList.add(new NfcData("未检测到读卡器","0"));
        }
        return mysList;
    }
}
