package model;

import com.kulala.staticsfunc.dbHelper.ODBHelper;

import common.GlobalContext;
import model.loginreg.DataUser;
public class ManagerNokey {
    public static final String  CMD_ASK_SWITCH_OPEN        = "0x84 02 00 02 77";
    public static final String  CMD_ASK_SWITCH_CLOSE       = "0x84 02 01 02 76";
    public static final String  CMD_ASK_DISTANCE_OPEN      = "0x84 02 02 02 75";
    public static final String  CMD_ASK_DISTANCE_CLOSE     = "0x84 02 03 02 74";
    public static final String  CMD_SET_SWITCH_OPEN_CLOSE  = "0x84 02 00 00 79";
    public static final String  CMD_SET_SWITCH_OPEN_OPEN   = "0x84 02 00 01 78";
    public static final String  CMD_SET_SWITCH_CLOSE_CLOSE = "0x84 02 01 00 78";
    public static final String  CMD_SET_SWITCH_CLOSE_OPEN  = "0x84 02 01 01 77";
    public static final String  CMD_SET_DISTANCE_OPEN      = "0x84 02 02 01 76";
    public static final String  CMD_SET_DISTANCE_CLOSE     = "0x84 02 03 01 75";
    public static       boolean SWITCH_OPEN                = false;
    public static       boolean SWITCH_CLOSE               = false;
    public static       int     SWITCH_OPEN_VALUE          = 0;
    public static       int     SWITCH_CLOSE_VALUE         = 0;
    // ========================out======================
    private static ManagerNokey _instance;
    private ManagerNokey() {
    }
    public static ManagerNokey getInstance() {
        if (_instance == null)
            _instance = new ManagerNokey();
        return _instance;
    }
    // ========================get set======================
    public boolean getSwitchOpen() {
        if (!SWITCH_OPEN) {
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
            if (user == null || user.userId == 0) {
                SWITCH_OPEN = false;
            } else {
                String value = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(user.userId, "SWITCH_OPEN");
                SWITCH_OPEN = (value != null && value.equals("true")) ? true : false;
            }
        }
        return SWITCH_OPEN;
    }
    public void setSwitchOpen(boolean open) {
        SWITCH_OPEN = open;
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null || user.userId == 0) {
            SWITCH_OPEN = false;
        } else {
            ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(user.userId, "SWITCH_OPEN", String.valueOf(open));
        }
    }
    public boolean getSwitchClose() {
        if (!SWITCH_CLOSE) {
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
            if (user == null || user.userId == 0) {
                SWITCH_CLOSE = false;
            } else {
                String value = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(user.userId, "SWITCH_CLOSE");
                SWITCH_CLOSE = (value != null && value.equals("true")) ? true : false;
            }
        }
        return SWITCH_CLOSE;
    }
    public void setSwitchClose(boolean open) {
        SWITCH_CLOSE = open;
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null || user.userId == 0) {
            SWITCH_CLOSE = false;
        } else {
            ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(user.userId, "SWITCH_CLOSE", String.valueOf(open));
        }
    }
    public int getSwitchOpenValue() {
        if (SWITCH_OPEN_VALUE == 0) {
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
            if (user == null || user.userId == 0) {
                SWITCH_OPEN_VALUE = 0;
            } else {
                String value = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(user.userId, "SWITCH_OPEN_VALUE");
                SWITCH_OPEN_VALUE = (value != null && value.length() > 0) ? Integer.valueOf(value) : 0;
            }
        }
        return SWITCH_OPEN_VALUE;
    }
    public void setSwitchOpenValue(int value) {
        SWITCH_OPEN_VALUE = value;
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null || user.userId == 0) {
            SWITCH_OPEN_VALUE = 0;
        } else {
            ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(user.userId, "SWITCH_OPEN_VALUE", String.valueOf(SWITCH_OPEN_VALUE));
        }
    }
    public int getSwitchCloseValue() {
        if (SWITCH_CLOSE_VALUE == 0) {
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
            if (user == null || user.userId == 0) {
                SWITCH_CLOSE_VALUE = 0;
            } else {
                String value = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(user.userId, "SWITCH_CLOSE_VALUE");
                SWITCH_CLOSE_VALUE = (value != null && value.length() > 0) ? Integer.valueOf(value) : 0;
            }
        }
        return SWITCH_CLOSE_VALUE;
    }
    public void setSwitchCloseValue(int value) {
        SWITCH_CLOSE_VALUE = value;
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null || user.userId == 0) {
            SWITCH_CLOSE_VALUE = 0;
        } else {
            ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(user.userId, "SWITCH_CLOSE_VALUE", String.valueOf(SWITCH_CLOSE_VALUE));
        }
    }
    // ========================set======================
}
