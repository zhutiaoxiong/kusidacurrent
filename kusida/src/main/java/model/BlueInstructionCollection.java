package model;

import android.util.Log;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.linkspods.BuildConfig;
import com.kulala.staticsfunc.static_assistant.ByteHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import view.find.ViewProSet;
import view.view4me.nfcmoudle.ManagerNfcBlue;

public class BlueInstructionCollection {
    private static BlueInstructionCollection _instance;
    private int isOpenSound;
    private int isOpenStartProtect;
    private int isOpenLockWindowUp;
    private int truckOpenMode;
    private int truckLevel;
    private int upWindowLevel;
    private int evasivePassLevel;
    private int evasiveCloseLevel;
    private int evasiveControlLevel;
    private int evasiveControlWay;
    private int lockTrigerWay;
    private int unLockTrigerWay;
    private int lockUpWindowIntervalTime;
    private int isRecoverFactorySet;
    private int isTouchIn;


    public int getIsTouchIn() {
        return isTouchIn;
    }

    public void setIsTouchIn(int isTouchIn) {
        this.isTouchIn = isTouchIn;
    }

    public int getTouchInRssi() {
        return touchInRssi;
    }
    public void setTouchInRssiMy(int touchInRssi) {
        this.touchInRssi = touchInRssi;
    }
    private int touchInRssi=-100;

    public int getIsRecoverFactorySet() {
        return isRecoverFactorySet;
    }

    public void setIsRecoverFactorySet(int isRecoverFactorySet) {
        this.isRecoverFactorySet = isRecoverFactorySet;
    }

    public int getLockCount() {
        return lockCount;
    }

    public void setLockCount(int lockCount) {
        this.lockCount = lockCount;
    }

    public int getUnLockCount() {
        return unLockCount;
    }

    public void setUnLockCount(int unLockCount) {
        this.unLockCount = unLockCount;
    }

    private int lockCount;
    private int unLockCount;

    public int getEvasiveDeviceMiniLevel() {
        return evasiveDeviceMiniLevel;
    }

    public void setEvasiveDeviceMiniLevel(int evasiveDeviceMiniLevel) {
        this.evasiveDeviceMiniLevel = evasiveDeviceMiniLevel;
    }

    private int evasiveDeviceMiniLevel;

    public int getLockUnlockSettingLevel() {
        return lockUnlockSettingLevel;
    }

    public void setLockUnlockSettingLevel(int lockUnlockSettingLevel) {
        this.lockUnlockSettingLevel = lockUnlockSettingLevel;
    }

    private int lockUnlockSettingLevel;

    public int getFindCarWay() {
        return findCarWay;
    }

    public void setFindCarWay(int findCarWay) {
        this.findCarWay = findCarWay;
    }

    public int getFindCarJianGeTime() {
        return findCarJianGeTime;
    }

    public void setFindCarJianGeTime(int findCarJianGeTime) {
        this.findCarJianGeTime = findCarJianGeTime;
    }

    private int findCarWay;
    private int findCarJianGeTime;

    private Map<String, Integer> cardInfo = new HashMap<>();

    public static BlueInstructionCollection getInstance() {
        if (_instance == null)
            _instance = new BlueInstructionCollection();
        return _instance;
    }

    public int getIsOpenSound() {
        return isOpenSound;
    }

    public int getIsOpenStartProtect() {
        return isOpenStartProtect;
    }

    public int getIsOpenLockWindowUp() {
        return isOpenLockWindowUp;
    }

    public int getTruckOpenMode() {
        return truckOpenMode;
    }

    public int getTruckLevel() {
        return truckLevel;
    }

    public int getUpWindowLevel() {
        return upWindowLevel;
    }

    public int getEvasivePassLevel() {
        return evasivePassLevel;
    }

    public int getEvasiveCloseLevel() {
        return evasiveCloseLevel;
    }

    public int getEvasiveControlLevel() {
        return evasiveControlLevel;
    }

    public int getEvasiveControlWay() {
        return evasiveControlWay;
    }
    public int getLockTrigerWay() {
        return lockTrigerWay;
    }
    public int getUnLockTrigerWay() {
        return unLockTrigerWay;
    }
    public int getLockUpWindowIntervalTime() {
        return lockUpWindowIntervalTime;
    }


    public void setUpWindowLevel(int upWindowLevel) {
        this.upWindowLevel = upWindowLevel;
    }

    public void setIsOpenSound(int isOpenSound) {
        this.isOpenSound = isOpenSound;
    }

    public void setIsOpenStartProtect(int isOpenStartProtect) {
        this.isOpenStartProtect = isOpenStartProtect;
    }

    public void setIsOpenLockWindowUp(int isOpenLockWindowUp) {
        this.isOpenLockWindowUp = isOpenLockWindowUp;
    }

    public void setTruckOpenMode(int truckOpenMode) {
        this.truckOpenMode = truckOpenMode;
    }

    public void setTruckLevel(int truckLevel) {
        this.truckLevel = truckLevel;
    }

    public void setEvasivePassLevel(int evasivePassLevel) {
        this.evasivePassLevel = evasivePassLevel;
    }

    public void setEvasiveCloseLevel(int evasiveCloseLevel) {
        this.evasiveCloseLevel = evasiveCloseLevel;
    }

    public void setEvasiveControlLevel(int evasiveControlLevel) {
        this.evasiveControlLevel = evasiveControlLevel;
    }

    public void setEvasiveControlWay(int evasiveControlWay) {
        this.evasiveControlWay = evasiveControlWay;
    }
    public void setLockTrigerWay(int lockTrigerWay) {
        this.lockTrigerWay = lockTrigerWay;
    }
    public void setUnLockTrigerWay(int unLockTrigerWay) {
        this.unLockTrigerWay = unLockTrigerWay;
    }
    public void setockUpWindowIntervalTimeData(int lockUpWindowIntervalTime) {
        this.lockUpWindowIntervalTime = lockUpWindowIntervalTime;
    }


    public static int noSignInt(byte a){
        return a&0xff;
    }
    public Map<String, Integer> getCardInfo() {
        return cardInfo;
    }

    public void saveAllSwitch(int dataType, byte[] data) {
        if (data != null) {
            if (dataType == 0x25) {
                if (data.length >= 1) {
                    byte[] states = ByteHelper.getBitArray(data[0]);
                    if (states.length >= 4) {
//                        setIsOpenStartProtect(states[0]);
                        setIsOpenLockWindowUp(states[1]);
                        setIsOpenSound(states[2]);
                        setTruckOpenMode(states[3]);
                        Log.e("??????0",""+states[3]);
                    }
                    ODispatcher.dispatchEvent(OEventName.MINI_CONTROL_SWITCH_RESULT_BACK);
                }
            } else if (dataType == 0x26) {
                if(BuildConfig.DEBUG) Log.e("???????????????", "------- "+ Arrays.toString(data));
                if (data.length >= 2) {
                    if (data[0] == 0x00) {
                        //????????????
                        setIsRecoverFactorySet(data[1]);
                        if(getIsRecoverFactorySet()==0){
                            ODispatcher.dispatchEvent(OEventName.MINI_SET_FACTORY_RESULT,true);
                        }else{
                            ODispatcher.dispatchEvent(OEventName.MINI_SET_FACTORY_RESULT,false);
                        }
                    }else if (data[0] == 0x01) {
                        //????????????
                        setTruckLevel(data[1]);
                    } else if (data[0] == 0x02) {
                        //????????????
                        setUpWindowLevel(data[1]);
                    } else if (data[0] == 0x03) {
                        //??????????????????
                        int passLevel=noSignInt(data[1]);
                        setEvasivePassLevel(passLevel);
                    }else if (data[0] == 0x04) {
                        //??????????????????
                        int closeLevel=noSignInt(data[1]);
                        setEvasiveCloseLevel(closeLevel);
                    }else if (data[0] == 0x05) {
                        //?????????????????????
                        int controlLevel=noSignInt(data[1]);
                        setEvasiveControlLevel(controlLevel);
                    }else if (data[0] == 0x06) {
                        //?????????????????????
                        setEvasiveControlWay(data[1]);
                        ODispatcher.dispatchEvent(OEventName.MINI_LOCK_OR_UNLOCK_RESULT_BACK,0);
                    }else if (data[0] == 0x07) {
                        //??????????????????
                        setLockTrigerWay(data[1]);
                        ODispatcher.dispatchEvent(OEventName.MINI_LOCK_OR_UNLOCK_RESULT_BACK,1);
                    }else if (data[0] == 0x08) {
                        //??????????????????
                       setUnLockTrigerWay(data[1]);
                        ODispatcher.dispatchEvent(OEventName.MINI_LOCK_OR_UNLOCK_RESULT_BACK,2);
                    }else if (data[0] == 0x09) {
                        //??????????????????
                        int lockupWindowInterverTime=noSignInt(data[1]);
                        setockUpWindowIntervalTimeData(lockupWindowInterverTime);
                    }else if (data[0] == 0x0A) {
                        //??????????????????
                        int lockCount=noSignInt(data[1]);
                        setLockCount(lockCount);
                        ODispatcher.dispatchEvent(OEventName.LOCK_COUNT_RESULT,1);
                    }else if (data[0] == 0x0B) {
                        //??????????????????
                        int unLockCount=noSignInt(data[1]);
                        setUnLockCount(unLockCount);
                        ODispatcher.dispatchEvent(OEventName.LOCK_COUNT_RESULT,2);
                    }else if (data[0] == 0x0C) {
                        //??????????????????
                        int lockunlocksetlevel=noSignInt(data[1]);
                        setLockUnlockSettingLevel(lockunlocksetlevel);
                        ODispatcher.dispatchEvent(OEventName.LOCK_UNLOCK_LEVEL_RESULT_BACK);
                    }else if (data[0] == 0x0D) {
                        //??????????????????
                        int evasiveDeviceMiniLevel=noSignInt(data[1]);
                        setEvasiveDeviceMiniLevel(evasiveDeviceMiniLevel);
                        ODispatcher.dispatchEvent(OEventName.EVASIVE_DEVICE_RESULT_BACK);
                    }else if (data[0] == 0x0E) {
                        //??????????????????
                        int truckOpenMode=noSignInt(data[1]);
                        Log.e("??????1",""+truckOpenMode);
                        if(truckOpenMode>1){
                            setTruckOpenMode(truckOpenMode);
                        }
                        ODispatcher.dispatchEvent(OEventName.MINI_CONTROL_SWITCH_RESULT_BACK,"1");
                    }else if (data[0] == 0x0F) {
                        int startProtect=noSignInt(data[1]);
                        setIsOpenStartProtect(startProtect);
                    }else if (data[0] == 0x10) {
                        Log.e("??????","???");
                        int way=noSignInt(data[1]);
                        setFindCarWay(way);
                        ODispatcher.dispatchEvent(OEventName.MINI_CONTROL_FINDCAR_RESULT_BACK);
                    }else if (data[0] == 0x11) {
                        int time=noSignInt(data[1]);
                        setFindCarJianGeTime(time);
                    }
                    ODispatcher.dispatchEvent(OEventName.MINI_CONTROL_SWITCH_RESULT_BACK);
                }
            } else if (dataType == 0x79) {
                if (data.length == 9) {
                    if (data[0] == 0x02) {
                        cardInfo.put("cardOne", (int) data[1]);
                        cardInfo.put("cardTwo", (int) data[2]);
                        cardInfo.put("cardThree", (int) data[3]);
                        cardInfo.put("cardFour", (int) data[4]);
                        cardInfo.put("cardFive", (int) data[5]);
                        cardInfo.put("cardSix", (int) data[6]);
                        cardInfo.put("cardSeven", (int) data[7]);
                        cardInfo.put("cardEight", (int) data[8]);
                        ManagerNfcBlue.getInstance().saveListNfc(cardInfo);
                        ODispatcher.dispatchEvent(OEventName.MINI_NFC_RESULT_BACK);
                    }
                }
            }else if (dataType == 0x27) {
                if(BuildConfig.DEBUG) Log.e("???????????????", "------- "+ Arrays.toString(data));
                if (data.length >= 2) {
                    if (data[0] == 0x00) {
                        //????????????
                        setIsTouchIn(data[1]);
                        ODispatcher.dispatchEvent(OEventName.TOUCH_IN_SWITCH_RESULT);
                    }else   if (data[0] == 0x02) {
                        //????????????
                        setTouchInRssiMy(data[1]);
                        Log.e("touchinrssi","????????????rssi"+data[1]);
                        ODispatcher.dispatchEvent(OEventName.TOUCH_IN_RSSI_RESULT,true);
                    }
                }
            }
        }
    }

    /**
     * ????????????
     */
    public static byte[] openSound() {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE4;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x02;
        instructions[4] = (byte) 0x01;
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????
     */
    public static byte[] closeSound() {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE4;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x02;
        instructions[4] = (byte) 0x00;
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????????????????
     */
    public static byte[] openStartProtect() {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x0F;
        instructions[4] = (byte) 0x01;
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????????????????
     */
    public static byte[] closeStartProtect() {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x0F;
        instructions[4] = (byte) 0x00;
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }
    /**
     * ??????????????????
     */
    public static byte[] setFindCarWayCoamd(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x10;
        switch (level) {
            case 1:
                instructions[4] = (byte) 0x00;
                break;
            case 2:
                instructions[4] = (byte) 0x10;
                break;
            case 3:
                instructions[4] = (byte) 0x20;
                break;
            case 4:
                instructions[4] = (byte) 0x30;
                break;
            case 5:
                instructions[4] = (byte) 0x13;
                break;
            case 6:
                instructions[4] = (byte) 0x15;
                break;
            case 7:
                instructions[4] = (byte) 0x17;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????????????????
     */
    public static byte[] setFindCarTimeCoamd(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x11;
        switch (level) {
            case 1:
                instructions[4] = (byte) 0x05;
                break;
            case 2:
                instructions[4] = (byte) 0x06;
                break;
            case 3:
                instructions[4] = (byte) 0x07;
                break;
            case 4:
                instructions[4] = (byte) 0x08;
                break;
            case 5:
                instructions[4] = (byte) 0x09;
                break;
            case 6:
                instructions[4] = (byte) 0x0A;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????????????????
     */
    public static byte[] openLockWindowUp() {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE4;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x01;
        instructions[4] = (byte) 0x01;
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????????????????
     */
    public static byte[] closeLockWindowUp() {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE4;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x01;
        instructions[4] = (byte) 0x00;
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????? ??????
     */
    public static byte[] longClickOpenTrunk() {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE4;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x03;
        instructions[4] = (byte) 0x01;
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????? ??????
     */
    public static byte[] doubleClickOpenTrunk() {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE4;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x03;
        instructions[4] = (byte) 0x00;
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????????????????
     */
    public static byte[] recoverFactorySet() {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x00;
        instructions[4] = (byte) 0x00;
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }



    /**
     * ??????????????????
     */
    public static byte[] setTrumpetsTime(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x01;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x00;
                break;
            case 1:
                instructions[4] = (byte) 0x01;
                break;
            case 2:
                instructions[4] = (byte) 0x02;
                break;
            case 3:
                instructions[4] = (byte) 0x03;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????????????????
     */
    public static byte[] setUpWindowTime(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x02;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x00;
                break;
            case 1:
                instructions[4] = (byte) 0x01;
                break;
            case 2:
                instructions[4] = (byte) 0x02;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] setEvasivePassTime(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x03;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x03;
                break;
            case 1:
                instructions[4] = (byte) 0x04;
                break;
            case 2:
                instructions[4] = (byte) 0x05;
                break;
            case 3:
                instructions[4] = (byte) 0x06;
                break;
            case 4:
                instructions[4] = (byte) 0x07;
                break;
            case 5:
                instructions[4] = (byte) 0x08;
                break;
            case 6:
                instructions[4] = (byte) 0x09;
                break;
            case 7:
                instructions[4] = (byte) 0x0A;
                break;
            case 8:
                instructions[4] = (byte) 0x0D;
                break;
            case 9:
                instructions[4] = (byte) 0x0F;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] setEvasiveCloseTime(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x04;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x03;
                break;
            case 1:
                instructions[4] = (byte) 0x04;
                break;
            case 2:
                instructions[4] = (byte) 0x05;
                break;
            case 3:
                instructions[4] = (byte) 0x06;
                break;
            case 4:
                instructions[4] = (byte) 0x07;
                break;
            case 5:
                instructions[4] = (byte) 0x08;
                break;
            case 6:
                instructions[4] = (byte) 0x09;
                break;
            case 7:
                instructions[4] = (byte) 0x0A;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ???????????????????????????
     */
    public static byte[] setEvasiveControlTime(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x05;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x03;
                break;
            case 1:
                instructions[4] = (byte) 0x04;
                break;
            case 2:
                instructions[4] = (byte) 0x05;
                break;
            case 3:
                instructions[4] = (byte) 0x06;
                break;
            case 4:
                instructions[4] = (byte) 0x07;
                break;
            case 5:
                instructions[4] = (byte) 0x08;
                break;
            case 6:
                instructions[4] = (byte) 0x09;
                break;
            case 7:
                instructions[4] = (byte) 0x0A;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ?????????????????????????????????settter??????
     */
    public static byte[] setEvasiveControlWayNotConstor(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x06;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x00;
                break;
            case 1:
                instructions[4] = (byte) 0x01;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] setLockTriggerWay (int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x07;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x00;
                break;
            case 1:
                instructions[4] = (byte) 0x01;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] setUnLockTriggerWay (int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x08;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x00;
                break;
            case 1:
                instructions[4] = (byte) 0x01;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] setLockUpWindowIntervalTime(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x09;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x05;
                break;
            case 1:
                instructions[4] = (byte) 0x06;
                break;
            case 2:
                instructions[4] = (byte) 0x07;
                break;
            case 3:
                instructions[4] = (byte) 0x08;
                break;
            case 4:
                instructions[4] = (byte) 0x09;
                break;
            case 5:
                instructions[4] = (byte) 0x0A;
                break;
            case 6:
                instructions[4] = (byte) 0x00;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }
    /**
     * ????????????????????????
     */
    public static byte[] setLockUnLocklll(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x0C;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x00;
                break;
            case 1:
                instructions[4] = (byte) 0x01;
                break;
            case 2:
                instructions[4] = (byte) 0x02;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ?????????????????????
     */
    public static byte[] setEvasiveDeviceMini(int level) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x0D;
        switch (level) {
            case 0:
                instructions[4] = (byte) 0x00;
                break;
            case 1:
                instructions[4] = (byte) 0x01;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }




    /**
     * ????????????????????????  ??????????????????
     */
    public static byte[] setTouchInSwitch(int isOpen) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0x86;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x00;
        instructions[3] = (byte) 0x00;
        if(isOpen==1){
            instructions[4] = (byte) 0x01;
        }else{
            instructions[4] = (byte) 0x00;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }
    /**
     * ????????????????????????  ???????????????????????????
     */
    public static byte[] setTouchInRssi(int rssi) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0x86;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x04;
        instructions[3] = (byte) 0x00;
        instructions[4] = (byte) rssi;
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }
    /**
     * ?????????????????????????????????
     */
    public static byte[] qurrySwitch() {
        byte[] instructions = new byte[4];
        instructions[0] = (byte) 0xE4;
        instructions[1] = (byte) 0x01;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) ((instructions[0] + instructions[1] + instructions[2]) ^ 0xff);
        return instructions;
    }
        /**
         * ??????????????????
         */
        public static byte[] qurryStartProTect() {
            byte[] instructions = new byte[5];
            instructions[0] = (byte) 0xE5;
            instructions[1] = (byte) 0x02;
            instructions[2] = (byte) 0x02;
            instructions[3] = (byte) 0x0F;
            instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
            return instructions;
        }

    /**
     * ??????????????????
     */
    public static byte[] qurryTrumpetsTime() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x01;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????????????????
     */
    public static byte[] qurryUpWindowTime() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x02;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] qurryEvasivePassTime() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x03;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] qurryEvasiveCloseTime() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x04;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ???????????????????????????
     */
    public static byte[] qurryEvasiveControlTime() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x05;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ???????????????????????????
     */
    public static byte[] qurryEvasiveControlWay() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x06;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] qurryLockTrigerWay() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x07;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] qurryUnLockTrigerWay() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x08;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] qurryLockUpWindowIntervalTime() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x09;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }
    /**
     * ??????????????????????????????
     */
    public static byte[] qurrylockunlocksetting() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x0C;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }
    /**
     * ?????????????????????
     */
    public static byte[] qurryEvasiveDeviceMini() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x0D;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    public static byte[] qurryTruckOpenTime() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x0E;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    public static byte[] qurryTruckFindCarWay() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x10;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }
    public static byte[] qurryTruckFindCarJianGeTime() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) 0x11;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }
    /**
     * ????????????????????????
     */
    public static byte[] setTrunkOpenTime(int time) {
        byte[] instructions = new byte[6];
        instructions[0] = (byte) 0xE5;
        instructions[1] = (byte) 0x03;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x0E;
        switch (time) {
            case 2:
                instructions[4] = (byte) 0x00;
                break;
            case 3:
                instructions[4] = (byte) 0x01;
                break;
            case 5:
                instructions[4] = (byte) 0x02;
                break;
            case 7:
                instructions[4] = (byte) 0x03;
                break;
            case 9:
                instructions[4] = (byte) 0x04;
                break;
            case 20:
                instructions[4] = (byte) 0x20;
                break;
        }
        instructions[5] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3] + instructions[4]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????????????????
     */
    public static byte[] qurryTouchIn() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0x86;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x00;
        instructions[3] = (byte) 0x01;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }
    /**
     * ????????????????????????rssi???
     */
    public static byte[] qurryTouchInRssi() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0x86;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x04;
        instructions[3] = (byte) 0x01;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????????????????
     */
    public static byte[] sendLock() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE6;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x40;
        instructions[3] = (byte) 0x00;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }
    /**
     * ??????????????????
     */
    public static byte[] sendUnLock() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE6;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x08;
        instructions[3] = (byte) 0x00;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }
    /**
     * ??????????????????
     */
    public static byte[] sendWeixiang() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0xE6;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x00;
        instructions[3] = (byte) 0x80;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }


    /**
     * ??????nfc?????????
     */
    public static byte[] askNfcNum() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0x96;
        instructions[1] = (byte) 0x01;
        instructions[2] = (byte) 0x01;
        instructions[3] = (byte) 0x08;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????????????????
     */
    public static byte[] askCardInfo() {
        byte[] instructions = new byte[4];
        instructions[0] = (byte) 0x96;
        instructions[1] = (byte) 0x01;
        instructions[2] = (byte) 0x02;
        instructions[3] = (byte) ((instructions[0] + instructions[1] + instructions[2]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????
     */
    public static byte[] addCard(int cardNum, int type) {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0x96;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x03;
        instructions[3] = getByteFromParam(cardNum, type);
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ????????????
     */
    public static byte[] deleteCard(int cardNum, int type) {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0x96;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x04;
        instructions[3] = getByteFromParam(cardNum, type);
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }


    /**
     * ??????id??????
     */
    public static byte[] addIdCard(int cardNum, int type) {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0x96;
        instructions[1] = (byte) 0x02;
        instructions[2] = (byte) 0x05;
        instructions[3] = getByteFromParam(cardNum, type);
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    /**
     * ??????nfc??????
     */
    public static byte[] askNfcVersion() {
        byte[] instructions = new byte[5];
        instructions[0] = (byte) 0x96;
        instructions[1] = (byte) 0x01;
        instructions[2] = (byte) 0x06;
        instructions[3] = (byte) 0x08;
        instructions[4] = (byte) ((instructions[0] + instructions[1] + instructions[2] + instructions[3]) ^ 0xff);
        return instructions;
    }

    private static byte getByteFromParam(int param, int type) {
        byte b = -1;
        if (type == 0) {
            switch (param) {
                case 0:
                    b = (byte) 0x00;
                    break;
                case 1:
                    b = (byte) 0x01;
                    break;
                case 2:
                    b = (byte) 0x02;
                    break;
                case 3:
                    b = (byte) 0x03;
                    break;
                case 4:
                    b = (byte) 0x04;
                    break;
            }
        } else {
            switch (param) {
                case 0:
                    b = (byte) 0x05;
                    break;
                case 1:
                    b = (byte) 0x06;
                    break;
                case 2:
                    b = (byte) 0x07;
                    break;
            }
        }
        return b;
    }
}
