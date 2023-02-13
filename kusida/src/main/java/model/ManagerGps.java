package model;

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

public class ManagerGps {
    public List<DataGpsPath> singleCarPath;
    public List<DataGpsPoint> favoriteList;
    public DataGpsPath currentPath;        // only for cache
    public static int areaMeter = 0;    // 围栏半径
    public static int areaOpen = 0;    // 围栏打开
    public static LatLng areaPos;    // 围栏坐标
    public static List<LatLng> path;
    public static LatLng latLng;
    public static String startLocation;

    private List<SearchHistory> list;
    // ========================out======================
    private static ManagerGps _instance;

    private ManagerGps() {
        favoriteList = new ArrayList<DataGpsPoint>();
    }

    public static ManagerGps getInstance() {
        if (_instance == null)
            _instance = new ManagerGps();
        return _instance;
    }

    // ========================get======================
    public float getTotalMiles() {
        if (singleCarPath == null)
            return 0;
        float sum = 0;
        for (int i = 0; i < singleCarPath.size(); i++) {
            DataGpsPath di = singleCarPath.get(i);
            sum += di.miles;
        }
        return sum;
    }

    public List<DataGpsPoint> getSearchFavoriteList(String searchValue) {
        List<DataGpsPoint> search = new ArrayList<DataGpsPoint>();
        for (DataGpsPoint point : favoriteList) {
            if (point.note.contains(searchValue) ||
                    point.location.contains(searchValue)) {
                search.add(point);
            }
        }
        return search;
    }

    // ========================put======================
    public void saveFavoriteList(JsonArray flist) {
        favoriteList = new ArrayList<DataGpsPoint>();
        if (flist == null) return;
        for (int i = 0; i < flist.size(); i++) {
            JsonObject obj = flist.get(i).getAsJsonObject();
            DataGpsPoint pp = DataGpsPoint.fromJsonObject(obj);
            favoriteList.add(pp);
        }
    }

    public void savePathList(double miles, JsonArray paths) {
        if (paths == null)
            return;
        singleCarPath = DataGpsPath.fromJsonArray(paths);
    }

    public void saveSearchHistory(String searchTxt) {
        list = loadSearchHistory();
        if (searchTxt == null || searchTxt.equals("")) return;
        if (list == null) list = new ArrayList<SearchHistory>();
        for (int i = 0; i < list.size(); i++) {
            SearchHistory searchHistory = list.get(i);
            if (searchHistory.searchtxt.equals(searchTxt)) {
                list.remove(searchHistory);
            }
        }
        SearchHistory searchHistory1 = new SearchHistory();
        searchHistory1.searchtxt = searchTxt;
        list.add(searchHistory1);
        JsonArray arr = SearchHistory.toJsonArray(list);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("searchHistory", ODBHelper.convertString(arr));
    }

    public List<SearchHistory> loadSearchHistory() {
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("searchHistory");
        JsonArray arr1 = ODBHelper.convertJsonArray(result);
        if (arr1 != null) {
            list = SearchHistory.fromJsonArray(arr1);
        }
        return list;
    }

    public void deleteSearchHistory(String deleteTxt) {
        list = loadSearchHistory();
        if (deleteTxt == null || deleteTxt.equals("")) return;
        if (list == null) list = new ArrayList<SearchHistory>();
        for (int i = 0; i < list.size(); i++) {
            SearchHistory searchHistory = list.get(i);
            if (searchHistory.searchtxt.equals(deleteTxt)) {
                list.remove(searchHistory);
            }
        }
        JsonArray arr = SearchHistory.toJsonArray(list);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("searchHistory", ODBHelper.convertString(arr));
    }

    //删除所有的历史纪录
    public void deleteSearchHistoryAll() {
        list = loadSearchHistory();
        list.clear();
        JsonArray arr = SearchHistory.toJsonArray(list);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("searchHistory", ODBHelper.convertString(arr));
    }

}
