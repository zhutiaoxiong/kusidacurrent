package common.map;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;

import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;

import static view.view4app.carpath.ViewNaviSearch.listHistory;

/**
 * Created by Administrator on 2017/3/15.
 */

public class DataPos {
    public static List<DataPos> list;
    public DataPos() {}
    public DataPos(LatLng pos, String address, String addressName) {
        this.pos = pos;
        this.address = address;
        this.addressName = addressName;
    }

    public DataPos(LatLng pos, String address) {
        this.pos = pos;
        this.address = address;
    }

    public LatLng pos;
    public String addressName;
    public String address;
    public static JsonArray toJsonArray(List<DataPos> list) {
        Gson      gson = new Gson();
        String    json = gson.toJson(list);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        return arr;
    }
    public static void saveSearchHistory(DataPos data) {

        if (data == null || data.address.equals("")) return;
        if (listHistory == null) listHistory = new ArrayList<DataPos>();
        for (int i = 0; i < listHistory.size(); i++) {
            DataPos searchHistory = listHistory.get(i);
            if (searchHistory.address.equals(data.address)) {
                listHistory.remove(searchHistory);
            }
        }
        listHistory.add(0, data);
        JsonArray arr = DataPos.toJsonArray(listHistory);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("listNaviHistory", ODBHelper.convertString(arr));
    }
    public static List<DataPos> loadSearchHistory() {
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("listNaviHistory");
        JsonArray arr1 = ODBHelper.convertJsonArray(result);
        if (arr1 != null) {
            list = DataPos.fromJsonArray(arr1);
        }
        return list;
    }
    public static void deleteOneSearchHistory(DataPos pos) {
        if(pos==null)return;
        list = loadSearchHistory();
        for(int i=0;i<list.size();i++){

            if(list.get(i).addressName.equals(pos.addressName)){
                list.remove(i);
                break;
            }
        }
        JsonArray arr = DataPos.toJsonArray(list);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("listNaviHistory", ODBHelper.convertString(arr));
    }

    public static void deleteSearchHistoryAll() {
        list = loadSearchHistory();
        list.clear();
        JsonArray arr = DataPos.toJsonArray(list);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("listNaviHistory", ODBHelper.convertString(arr));
    }
    public static List<DataPos> fromJsonArray(JsonArray arr) {
        Log.i("msg", "Gson");
        List<DataPos> list = new ArrayList<>();
        Gson          gson = new Gson();
        for (int i = 0; i < arr.size(); i++) {
            JsonObject object = arr.get(i).getAsJsonObject();
            Log.i("msg", "Gson object" + object.toString());
            DataPos data = DataPos.fromJsonObject(object);
           if( data.addressName==null||data.addressName.equals("")){
           }else list.add(data);
        }
        Log.i("msg", "Gson end");
        return list;
    }
    public static DataPos fromJsonObject(JsonObject obj) {
        Gson    gson    = new Gson();
        DataPos thisobj = gson.fromJson(obj, DataPos.class);
        return thisobj;
    }

    /**
     * 将字符串转换成有地址和经纬度的对象
     *
     * @param latLngStr
     * @return
     */
    public static DataPos changeStrToLatLng(String latLngStr, String adress) {
        String[] a      = latLngStr.split(",");
        double   b      = Double.valueOf(a[0]);
        double   b1     = Double.valueOf(a[1]);
        LatLng   latLng = new LatLng(b, b1);
        DataPos  pos    = new DataPos(latLng, adress);
        return pos;
    }

    public static List<PoiInfo> DataPos2PoiInfo(List<DataPos> posList) {
        if (posList == null) return null;
        List<PoiInfo> allPoi = new ArrayList<PoiInfo>();
        for (int i = 0; i < posList.size(); i++) {
            DataPos pos  = posList.get(i);
            PoiInfo info = new PoiInfo();
            info.address = pos.address;
            info.name = pos.addressName;
            info.location = pos.pos;
            allPoi.add(info);
        }
        return allPoi;
    }
    public static DataPos PoiInfo2DataPos(PoiInfo info) {
        if (info == null) return null;
        DataPos pos = new DataPos();
        pos.pos = info.location;
        pos.addressName = info.name;
        pos.address = info.address;
        return pos;
    }

}
