package model;

import model.navigation.DataNavigation;

public class ManagerNavi {
    public         DataNavigation naviInfo;
    // ========================out======================
    private static ManagerNavi    _instance;
    private ManagerNavi() {
    }
    public static ManagerNavi getInstance() {
        if (_instance == null)
            _instance = new ManagerNavi();
        return _instance;
    }
    // =================================================
    public void saveNaviInfo(DataNavigation info) {
        this.naviInfo = info;
    }
}
