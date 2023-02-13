package view.view4me.lcdkey;

import com.kulala.staticsfunc.dbHelper.ODBHelper;

import common.GlobalContext;

public class ManagerLcdKey {
    private String isBindLCDkey;
    private String blueAderess;
    private static ManagerLcdKey _instance;
    private ManagerLcdKey() {
    }
    public static ManagerLcdKey getInstance() {
        if (_instance == null)
            _instance = new ManagerLcdKey();
        return _instance;
    }
    public void setIsBindLCDkey(String isBindLCDkeyy){
        isBindLCDkey=isBindLCDkeyy;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isBindLCDkey", isBindLCDkeyy);
    }

    public String getIsBindLCDkey(){
        isBindLCDkey = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isBindLCDkey");
        return isBindLCDkey;
    }

    public void setBlueAderess(String blueAderesss){
        blueAderess=blueAderesss;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("blueAderess", blueAderesss);
    }


    public String getBlueAderess(){
        blueAderess = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("blueAderess");
        return blueAderess;
    }
}
