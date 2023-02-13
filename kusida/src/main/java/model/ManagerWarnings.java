package model;

import android.database.Cursor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import common.GlobalContext;
import model.carcontrol.DataWarnings;

/**
 * 警告列表
 * 1.初始化更新消息0-19
 * 2.下移上移刷新消息
 * 3.如果选中了时间，就显示时间泛围内的
 * 4.初进先删除所有数据，其它全保存
 */

public class ManagerWarnings {
    public List<DataWarnings> states;//只有列表才存
    public List<DataWarnings> moveingShowWarnings;//keep max 4
    // ========================out======================
    private static ManagerWarnings _instance;

    private ManagerWarnings() {
        moveingShowWarnings = new ArrayList<DataWarnings>();
        states = new ArrayList<DataWarnings>();
        DBClearDataAll();
    }

    public static ManagerWarnings getInstance() {
        if (_instance == null)
            _instance = new ManagerWarnings();
        return _instance;
    }

    public int getNewWarningNum() {
        if (moveingShowWarnings == null) return 0;
        if (moveingShowWarnings.size() == 0) return 0;
        int num = 0;
        for (int i = 0; i < moveingShowWarnings.size(); i++) {
            DataWarnings war = moveingShowWarnings.get(i);
            if (war.isNew) num++;
        }
        return num;
    }

    public List<DataWarnings> getLast4Warning() {
        if (moveingShowWarnings == null) return null;
        if (moveingShowWarnings.size() == 0) return null;
        List<DataWarnings> data = new ArrayList<DataWarnings>();
        for (int i = moveingShowWarnings.size() - 1; i >= 0; i--) {
            data.add(moveingShowWarnings.get(i));
            if (i < moveingShowWarnings.size() - 3) return data;
        }
        return data;
    }
    public DataWarnings getMoveingNewWarning() {
        if (moveingShowWarnings == null) return null;
        if (moveingShowWarnings.size() == 0) return null;
        for (int i = moveingShowWarnings.size() - 1; i >= 0; i--) {
            DataWarnings war = moveingShowWarnings.get(i);
            if(war!=null){
                if (war.isNew) return war;
            }
        }
        while (moveingShowWarnings.size()>4)moveingShowWarnings.remove(4);
        return null;
    }
    // ========================get list======================

    // 预警类型,1：消息，2：警报，3：安全
    public List<DataWarnings> getListWarningByPos(int pos, long carId) {
        if (carId < 1) return null;
        if (states == null || states.size() == 0) return new ArrayList<DataWarnings>();
        List<DataWarnings> data = new ArrayList<DataWarnings>();
        for (int i = states.size() - 1; i >= 0; i--) {
            if (states.get(i).alertType == pos && states.get(i).carId == carId) {
                data.add(states.get(i));
            }
        }
        return data;
    }

    // 预警类型,1：消息，2：警报，3：安全
    public List<DataWarnings> getListWarningByPosTime(int pos, long carId, long timefrom, long timeto) {
        if (states == null || states.size() == 0) return new ArrayList<DataWarnings>();
        List<DataWarnings> data = new ArrayList<DataWarnings>();
        for (int i = states.size() - 1; i >= 0; i--) {
            DataWarnings da = states.get(i);
            if (da.alertType == pos && da.carId == carId) {
                if (da.createTime > timefrom && da.createTime < timeto) {
                    data.add(da);
                }
            }
        }
        return data;
    }

    // ========================put======================
    public void exit() {
        states = new ArrayList<DataWarnings>();
        DBClearDataAll();
    }

    //===========================================================
    //排序
    private void sort(List<DataWarnings> warnings) {
        if (warnings == null) return;
        if (warnings.size() <= 1) return;
        Collections.sort(warnings, new Comparator<DataWarnings>() {
            @Override
            public int compare(DataWarnings o1, DataWarnings o2) {
                if (o1 == null || o2 == null) return 0;
                if (o1.createTime > o2.createTime) {
                    return 1;
                } else if (o1.createTime < o2.createTime) {
                    return -1;
                }
                return 0;//-1小0=1大,无0会有异常
            }
        });
    }

    //清掉旧数据
//    private void clearOld() {
//        if (states == null) return;
//        if (states.size() <= 1) return;
//        long now = ODateTime.getNow();
//        for (int i = states.size() - 1; i >= 0; i--) {
//            if (now - states.get(i).createTime > 7776000000L) {//90天
//                states.remove(i);
//            }
//        }
//    }

    public void saveLoadingList(JsonArray arr) {
        if (arr == null) return;
        List<DataWarnings> li = DataWarnings.fromJsonArray(arr);
        DBInsertORIGNORE(li);
    }

    //===========================================================
    //动态收到socket消息不加入数据库
    public void saveNewWarnings(final JsonObject data) {
        DataWarnings war = DataWarnings.fromJsonObject(data);
        for (int i = 0; i < moveingShowWarnings.size(); i++) {
            if (war.equals(moveingShowWarnings.get(i))) return;
        }
        moveingShowWarnings.add(war);
        war.isNew = true;
        sort(moveingShowWarnings);
    }

    //插入不重复数据
    public void DBInsertORIGNORE(List<DataWarnings> newData) {
        if (newData == null) return;
        if (newData.size() == 0) return;
        try {
            for (DataWarnings data : newData) {
                String sql = "REPLACE into " + ODBHelper.TABLE_NAME_WARNING + " (carId,alertType,content,createTime,alertId,isNew)" +
                        " values('" + data.carId + "','" + data.alertType + "','" + data.content + "','" + data.createTime + "','" + data.alertId + "','" + data.isNew + "');";
                ODBHelper.getInstance(GlobalContext.getContext()).execSQL(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ManagerWarnings.this.states = DBQueryWarning();
    }

    //查询所有警告列表
    private List<DataWarnings> DBQueryWarning() {
        Cursor             cursor    = ODBHelper.getInstance(GlobalContext.getContext()).query(ODBHelper.TABLE_NAME_WARNING);
        List<DataWarnings> cacheList = new ArrayList<DataWarnings>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String       carId      = cursor.getString(cursor.getColumnIndex("carId"));
                String       alertType  = cursor.getString(cursor.getColumnIndex("alertType"));
                String       content    = cursor.getString(cursor.getColumnIndex("content"));
                String       createTime = cursor.getString(cursor.getColumnIndex("createTime"));
                String       alertId    = cursor.getString(cursor.getColumnIndex("alertId"));
                String       isNew      = cursor.getString(cursor.getColumnIndex("isNew"));
                DataWarnings warnings   = new DataWarnings();
                warnings.carId = Long.parseLong(carId);
                warnings.alertType = Integer.parseInt(alertType);
                warnings.content = content;
                warnings.createTime = Long.parseLong(createTime);
                warnings.alertId = Integer.parseInt(alertId);
                warnings.isNew = Boolean.parseBoolean(isNew);
                cacheList.add(warnings);
            } while (cursor.moveToNext());
        }
        sort(cacheList);
        return cacheList;
    }

    //    //保存最后插入时间
//    public static void DBSetLastTime(long lasttime) {
//        if (helper == null)
//            helper = new ODBHelper(GlobalContext.getContext(), CREATE_INFO, TABLE_NAME);
//        DBConfig.set("lastWarrningTime", String.valueOf(lasttime));
//    }
//
//    //保存最后插入时间
//    public static long DBGetLastTime() {
//        if (helper == null)
//            helper = new ODBHelper(GlobalContext.getContext(), CREATE_INFO, TABLE_NAME);
//        String time = DBConfig.get("lastWarrningTime");
//        if(time == null || time.length() == 0)return 0;
//        return Long.parseLong(time);
//    }
    //初进清空所有数据
    public void DBClearDataAll() {
        String sql = "DELETE FROM " + ODBHelper.TABLE_NAME_WARNING;
        ODBHelper.getInstance(GlobalContext.getContext()).execSQL(sql);
        states = new ArrayList<DataWarnings>();
    }
}
