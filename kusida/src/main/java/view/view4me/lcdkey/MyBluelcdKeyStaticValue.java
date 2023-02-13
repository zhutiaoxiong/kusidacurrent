package view.view4me.lcdkey;

public class MyBluelcdKeyStaticValue {
    public static final String[] controlCmdArr = new String[]{
            "0x82 02 00 10 6B",//start stop
            "0x82 02 40 00 3B",//lock
            "0x82 02 08 00 73",//unlock
            "0x82 02 00 80 FB",//backpag
            "0x82 02 01 00 7A"//findcar
    };

    public static String getBlueTimeCmd(int time){
        switch (time) {
            case 0: return "E1 02 10 00 0C";
            case 1: return "E1 02 30 00 EC";
            case 2: return "E1 02 50 00 CC";
            case 3: return "E1 02 70 00 AC";
            case 4: return "E1 02 90 00 8C";
            case 5: return "E1 02 B0 00 6C";
            case 6: return "E1 02 D0 00 4C";
            case 7: return "E1 02 F0 00 2C";
        }
        return "";
    }
    /**
     * 控制车辆 instruction控制命令1：开启2：熄火3：设防4：撤防5：尾箱变化6：寻车 time只有0到7档，每档5分钟，0档位5分钟
     */
    public static String getControlCmdByID(int controlCmd) {
        switch (controlCmd) {
            case 1: return controlCmdArr[0];
            case 2: return controlCmdArr[0];
            case 3: return controlCmdArr[1];
            case 4: return controlCmdArr[2];
            case 5: return controlCmdArr[3];
            case 6: return controlCmdArr[4];
        }
        return "";
    }
}
