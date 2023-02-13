package model;

import com.client.proj.kusida.R;
import com.google.gson.JsonArray;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.wearkulala.www.wearfunc.WearReg;

import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import common.linkbg.BootBroadcastReceiver;
import model.carlist.DataCarInfo;
import model.status.DataSwitch;

public class ManagerSwitchs {
    private static final String[] voiceArr   = new String[]{"声音", "震动"};//if(switchVoiceList.size()!=2)createNewswitchVoiceList();
    private static final String[] ConfirmArr = new String[]{GlobalContext.getContext().getResources().getString(R.string.fortified_prompt_box_switch), GlobalContext.getContext().getResources().getString(R.string.prompt_box_machine_will_switch), GlobalContext.getContext().getResources().getString(R.string.boot_prompt_box_switch), GlobalContext.getContext().getResources().getString(R.string.seek_prompt_box_car_switch)};

    private        List<DataSwitch> switchControls;
    private        List<DataSwitch> switchWarnings;
    private        List<DataSwitch> switchSafetys;
    private        List<DataSwitch> switchPrivates;
//    private        List<DataSwitch> switchNoKeys;//固定id：72靠近开，73离开锁,null means unShow
    private        List<DataSwitch> switchConfirms;//提示框
    private        List<DataSwitch> switchWears;//手表开关
    // ========================out======================
    private static ManagerSwitchs   _instance;

    private ManagerSwitchs() {
        WearReg.getInstance().setManagerSwitchsListener(new WearReg.ManagerSwitchsListener() {
            @Override
            public int getSwitchWearsOpen() {
                List<DataSwitch> wears = getSwitchWears();
                if(wears == null || wears.size()==0)return -1;
                return wears.get(0).isOpen;
            }
            @Override
            public void setSwitchWearsOpen(int open) {
                List<DataSwitch> wears = getSwitchWears();
                if(wears == null || wears.size()==0)return;
                wears.get(0).isOpen = open;
            }
            @Override
            public int getSwitchWearsIde() {
                List<DataSwitch> wears = getSwitchWears();
                if(wears == null || wears.size()==0)return -1;
                return wears.get(0).ide;
            }
        });
    }
    public static ManagerSwitchs getInstance() {
        if (_instance == null)
            _instance = new ManagerSwitchs();
        return _instance;
    }
    // ========================get======================
    public List<DataSwitch> getSwitchControls() {
        if (switchControls == null || switchControls.size() == 0) {
            String    queryResult = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("switchControls");
            JsonArray switchArr   = ODBHelper.convertJsonArray(queryResult);
            switchControls = DataSwitch.fromJsonArray(switchArr);
        }
        return switchControls;
    }
    public List<DataSwitch> getSwitchWarnings() {
        if (switchWarnings == null || switchWarnings.size() == 0) {
            String    queryResult = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("switchWarnings");
            JsonArray switchArr   = ODBHelper.convertJsonArray(queryResult);
            switchWarnings = DataSwitch.fromJsonArray(switchArr);
        }
        return switchWarnings;
    }
    public List<DataSwitch> getSwitchSafetys() {
        if (switchSafetys == null || switchSafetys.size() == 0) {
            String    queryResult = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("switchSafetys");
            JsonArray switchArr   = ODBHelper.convertJsonArray(queryResult);
            switchSafetys = DataSwitch.fromJsonArray(switchArr);
        }
        return switchSafetys;
    }
    public List<DataSwitch> getSwitchPrivates() {
        if (switchPrivates == null || switchPrivates.size() == 0) {
            String    queryResult = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("switchPrivates");
            JsonArray switchArr   = ODBHelper.convertJsonArray(queryResult);
            switchPrivates = DataSwitch.fromJsonArray(switchArr);
        }
        return switchPrivates;
    }
    public List<DataSwitch> getSwitchNoKeys() {
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        return DataSwitch.fromJsonArray(carInfo.nonekeyNoticeInfos);
        //2018/08/09无钥显示改为随车
//        if (switchNoKeys == null || switchNoKeys.size() == 0) {
//            String    queryResult = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(ManagerLoginReg.getInstance().getCurrentUser().userId,"switchNoKeys");
//            JsonArray switchArr   = ODBHelper.convertJsonArray(queryResult);
//            switchNoKeys = DataSwitch.fromJsonArray(switchArr);
//        }
//        return switchNoKeys;
    }
    public List<DataSwitch> getSwitchWears() {
        if (switchWears == null || switchWears.size() == 0) {
            String    queryResult = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("switchWears");
            JsonArray switchArr   = ODBHelper.convertJsonArray(queryResult);
            switchWears = DataSwitch.fromJsonArray(switchArr);
        }
        return switchWears;
    }
    public List<DataSwitch> getswitchVoices() {
        String    queryResult = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("switchVoices");
        JsonArray switchArr   = ODBHelper.convertJsonArray(queryResult);
        if (switchArr == null || switchArr.size() != 2) {
            return createNewswitchVoiceList();
        } else {
            return DataSwitch.fromJsonArray(switchArr);
        }
    }
    public List<DataSwitch> getSwitchConfirms() {
        if (switchConfirms == null || switchConfirms.size() == 0) {
            String    queryResult = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("switchConfirms");
            JsonArray switchArr   = ODBHelper.convertJsonArray(queryResult);
            if (switchArr == null || switchArr.size() == 0) {
                createNewswitchConfirmList();
            } else {
                switchConfirms = DataSwitch.fromJsonArray(switchArr);
            }
        }
        return switchConfirms;
    }
    // ========================put======================
    public void saveSwitchControls(JsonArray ctrlNoticeInfos) {
        if (ctrlNoticeInfos == null) return;
        this.switchControls = DataSwitch.fromJsonArray(ctrlNoticeInfos);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("switchControls", ctrlNoticeInfos.toString());
    }
    public void saveSwitchWarnings(JsonArray switchWarning) {
        if (switchWarning == null) return;
        this.switchWarnings = DataSwitch.fromJsonArray(switchWarning);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("switchWarnings", switchWarning.toString());
    }

    public void saveSwitchSafetys(JsonArray switchSafety) {
        if (switchSafety == null) return;
        this.switchSafetys = DataSwitch.fromJsonArray(switchSafety);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("switchSafetys", switchSafety.toString());
    }

    public void saveSwitchPrivates(JsonArray secretNoticeInfos) {
        if (secretNoticeInfos == null) return;
        this.switchPrivates = DataSwitch.fromJsonArray(secretNoticeInfos);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("switchPrivates", secretNoticeInfos.toString());
    }
    //2018/08/09无钥显示改为随车
//    public void saveSwitchNoKey(JsonArray nonekeyNoticeInfos) {
//        long userId = ManagerLoginReg.getInstance().getCurrentUser().userId;
//        if (nonekeyNoticeInfos == null){
//            this.switchNoKeys = null;
//            ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(userId,"switchNoKeys", "");
//        }else {
//            this.switchNoKeys = DataSwitch.fromJsonArray(nonekeyNoticeInfos);
//            ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(userId,"switchNoKeys", nonekeyNoticeInfos.toString());
//        }
//    }
    public void saveSwitchWear(JsonArray watchNoticeInfos) {
        if (watchNoticeInfos == null) return;
        this.switchWears = DataSwitch.fromJsonArray(watchNoticeInfos);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("switchWears", watchNoticeInfos.toString());
    }

    public void saveSwitchVoice(DataSwitch swit) {
        List<DataSwitch> switchVoices= getswitchVoices();
        if (swit == null || switchVoices == null) return;
        for (DataSwitch da : switchVoices) {
            if (da.ide == swit.ide) {
                da.isOpen = swit.isOpen;
            }
        }
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("switchVoices", ODBHelper.convertString(DataSwitch.toJsonArray(switchVoices)));
        BootBroadcastReceiver.initNotification();
    }
    public void saveSwitchConfirm(DataSwitch swit) {
        if (swit == null || switchConfirms == null) return;
        for (DataSwitch da : switchConfirms) {
            if (da.ide == swit.ide) {
                da.isOpen = swit.isOpen;
            }
        }
        JsonArray arr = DataSwitch.toJsonArray(switchConfirms);
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("switchConfirms", arr.toString());
    }
    // ==========================get=================================

    public ArrayList<DataSwitch> createNewswitchVoiceList() {
        ArrayList<DataSwitch> switchVoices = new ArrayList<DataSwitch>();
        for (int i = 0; i < voiceArr.length; i++) {
            DataSwitch a1 = new DataSwitch();
            a1.ide = i;
            a1.name = voiceArr[i];
            switchVoices.add(a1);
        }
        return switchVoices;
    }
    public void createNewswitchConfirmList() {
        switchConfirms = new ArrayList<DataSwitch>();
        for (int i = 0; i < ConfirmArr.length; i++) {
            DataSwitch a1 = new DataSwitch();
            a1.ide = i;
            a1.name = ConfirmArr[i];
            switchConfirms.add(a1);
        }
    }
    public boolean getSoundOpen() {
        List<DataSwitch> switchVoices = getswitchVoices();
        if (switchVoices == null || switchVoices.size() <2 || switchVoices.get(0) == null)
            return true;
        if (switchVoices.get(0).isOpen == 1) return true;
        return false;
    }
    public boolean getVibratorOpen() {
        List<DataSwitch> switchVoices = getswitchVoices();
        if (switchVoices == null || switchVoices.size() <2  || switchVoices.get(1) == null) return true;
        if (switchVoices.get(1).isOpen == 1) return true;
        return false;
    }

}
