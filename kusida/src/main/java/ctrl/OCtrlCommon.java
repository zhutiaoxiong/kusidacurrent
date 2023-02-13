package ctrl;

import android.text.TextUtils;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import annualreminder.model.ManagerAnnual;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import common.http.HttpConn;


import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.time.TimeDelayTask;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.wearkulala.www.wearfunc.WearReg;

import common.GlobalContext;
import common.PHeadHttp;
import model.ManagerCarList;
import model.ManagerChat;
import model.ManagerCommon;
import model.ManagerDisPlay;
import model.ManagerFlash;
import model.ManagerLoginReg;
import model.ManagerPublicData;
import model.ManagerSwitchs;
import model.ManagerWarnings;
import model.carcontrol.DataWarnings;
import model.maintain.DataMaintain;
import model.maintain.ManagerMaintainList;
import view.ActivityKulalaMain;

/**
 * 100-299
 */
public class OCtrlCommon {
    // ========================out======================
    private static OCtrlCommon _instance;

    protected OCtrlCommon() {
        WearReg.getInstance().setOCtrlCommonListener(new WearReg.OCtrlCommonListener() {
            @Override
            public void ccmd1305_getSwitchInfo() {
                OCtrlCommon.this.ccmd1305_getSwitchInfo();
            }

            @Override
            public void ccmd1306_changeSwitch(int noticeId, boolean isOpen) {
                OCtrlCommon.this.ccmd1306_changeSwitch(noticeId, isOpen);
            }
        });
    }

    public static OCtrlCommon getInstance() {
        if (_instance == null)
            _instance = new OCtrlCommon();
        return _instance;
    }


    // ========================out======================
    public void processResult(int protocol, JsonObject obj, String CACHE_ERROR) {
        switch (protocol) {
            case 1001:
                backdata_1001_wxpay(obj, CACHE_ERROR);
                break;
            case 1002:
                backdata_1002_checkpay(obj, CACHE_ERROR);
                break;
            case 1004:
                backdata_1004_pifupay(obj, CACHE_ERROR);
                break;
            case 1005:
                backdata_1005_recharge_vouchers(obj, CACHE_ERROR);
                break;
            case 1223:
                backdata_1223_getLostNotifation(obj, CACHE_ERROR);
                break;
            case 1205:
                backdata_1205_violationGetList(obj, CACHE_ERROR);
                break;
            case 1302:
                backdata_1302_getVersionInfo(obj, CACHE_ERROR);
                break;
            case 1303:
                backdata_1303_getCommonInfo(obj, CACHE_ERROR);
                break;
            case 1305:
                backdata_1305_getSwitchInfo(obj, CACHE_ERROR);
                break;
            case 1306://不需要处理,是没有钥一钥列表的
                backdata_1306_changeSwitch(obj, CACHE_ERROR);
                break;
            case 1309:
                backdata_1309_chatlist(obj, CACHE_ERROR);
                break;
            case 1310:
                backdata_1310_getBrandList(obj, CACHE_ERROR);
                break;
            case 1312:
                backdata_1312getuplloadPic(obj, CACHE_ERROR);
                break;
            case 1313:
                backdata_1313_sendSuggest(obj, CACHE_ERROR);
                break;
            case 1315:
                backdata_1315_setSwitchShakeOpen(obj, CACHE_ERROR);
                break;
            case 1316:
                backdata_1316_nokeyin_set_signal(obj, CACHE_ERROR);
                break;
            case 2301:
                backdata_2301_qurry_Display(obj, CACHE_ERROR);
                break;
            case 2401:
                backdata_2401_upGrade(obj, CACHE_ERROR);
                break;
            case 2402:
                backdata_2402_qurry_upGrade(obj, CACHE_ERROR);
                break;
            case 2303:
                backdata_2303_qurry_Taobao(obj, CACHE_ERROR);
                break;


        }
    }
    // ============================ccmd==================================

    /**
     * 取支付宝信息
     **/
    public void ccmd1001_wxpay(float fee, long carId, int time) {
        JsonObject data = new JsonObject();
        data.addProperty("hasSdk", 1);
        data.addProperty("fee", fee);
        data.addProperty("carId", carId);
        data.addProperty("time", time);
        HttpConn.getInstance().sendMessage(data, 1001);
    }


    /**
     * 取支付宝信息
     **/
    public void ccmd1002_checkpay(float fee, long carId, int time) {
        JsonObject data = new JsonObject();
        data.addProperty("hasSdk", 1);
        data.addProperty("fee", fee);
        data.addProperty("carId", carId);
        data.addProperty("time", time);
        HttpConn.getInstance().sendMessage(data, 1002);
    }

    /**
     * 皮肤支付
     **/
    public void ccmd1004_pifupay(float fee, int carTypeId, int type) {
        JsonObject data = new JsonObject();
        data.addProperty("hasSdk", 1);
        data.addProperty("fee", fee);
        data.addProperty("carTypeId", carTypeId);
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1004);
    }

    /**
     * 充值卷充值（1005）
     * 请求参数（封装到data参数中上传）
     * 名称 类型 说明 备注
     * phead jsonObject 信息头 必填，具体见协议框架中的请求头信息
     * code String 充值卷码 必填
     * carId long 车辆id 必填
     * 下发数据参数
     * carInfo jsonObject 汽车详情 具体见：汽车详情对象
     **/
    public void ccmd_1005_recharge_vouchers(String code, long carId) {
        JsonObject data = new JsonObject();
        data.addProperty("code", code);
        data.addProperty("carId", carId);
        HttpConn.getInstance().sendMessage(data, 1005);
    }

    /**
     * 违章查询
     **/
    public void ccmd1205_violationGetList(long carId, int isDemo) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("isDemo", isDemo);
        HttpConn.getInstance().sendMessage(data, 1205);
    }

    /**
     * 取版本信息
     *
     * @param type 0：首页发送，1：设置界面发送
     **/
    public void ccmd1302_getVersionInfo(int type) {
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        HttpConn.getInstance().sendMessage(data, 1302);
    }

    /**
     * 判断是否过期，需要重新登录
     **/
    public void ccmd1115_getLoginState() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1115);
    }

    /**
     * 汽车终止授权，授权和围栏通知
     **/
    public void ccmd1223_getLostNotifation() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1223);
    }

    /**
     * 取品牌列表
     **/
    public void ccmd1310_getBrandList(long lastUpdateTime) {
        JsonObject data = new JsonObject();
        data.addProperty("lastUpdateTime", lastUpdateTime);
        HttpConn.getInstance().sendMessage(data, 1310);
    }

    /**
     * 取通用信息
     **/
    public void ccmd1303_getCommonInfo() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1303);
    }

    /**
     * 取开关列表
     **/
    public void ccmd1305_getSwitchInfo() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1305);
    }

    /**
     * 修改开关列表
     **/
    public void ccmd1306_changeSwitch(int noticeId, boolean isOpen) {
        JsonObject data = new JsonObject();
        data.addProperty("noticeId", noticeId);
        data.addProperty("isOpen", isOpen ? 1 : 0);
        HttpConn.getInstance().sendMessage(data, 1306);
    }

    /**
     * 取聊天列表
     **/
    public void ccmd1309_chatlist() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1309);
    }

    /**
     * 投诉建议接口
     **/
    public void ccmd1313_sendSuggest(int type, String comment) {
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        data.addProperty("comment", comment);
        HttpConn.getInstance().sendMessage(data, 1313);
    }

    /**
     * 解除警报消息推送 （1314）
     **/
    public void ccmd1314_isGetMessage(long carId, int alertType) {
        JsonObject data = new JsonObject();
        data.addProperty("carId", carId);
        data.addProperty("alertType", alertType);
        HttpConn.getInstance().sendMessage(data, 1314);
    }

    /**
     * 摇一摇开关
     **/
    public void ccmd1315_setSwitchShakeOpen(long carId, boolean isOpen) {
        JsonObject data = new JsonObject();
        data.addProperty("isOpen", isOpen ? 1 : 0);
        data.addProperty("carId", carId);
        HttpConn.getInstance().sendMessage(data, 1315);
    }

    /**
     * 信号值设定 （1316）
     *
     * @param noticeId    开关id
     * @param signalValue 信号值
     **/
    public void ccmd1316_nokeyin_set_signal(int noticeId, double signalValue) {
        JsonObject data = new JsonObject();
        data.addProperty("noticeId", noticeId);
        data.addProperty("signalValue", signalValue);
        HttpConn.getInstance().sendMessage(data, 1316);
    }
    // ============================scmd==================================


    /**
     * 请求上传千牛服务器的token
     */
    public void cmmd1312_uplloadPic() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 1312);
    }
    /**
     * 取支付宝广告业是否展示
     **/
    public void cmmd_2301_qurry_Display() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 2301);
    }
    /**
     * 上报展示位点击事件
     **/
    public void cmmd_2302_push_Display(long disPlayId) {
        JsonObject data = new JsonObject();
        data.addProperty("displayId", disPlayId);
        HttpConn.getInstance().sendMessage(data, 2302);
    }

    /**
     * mini升級plus
     **/
    public void cmmd_2401_upGrade() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 2401);
    }
    /**
     * 查詢升級信息
     **/
    public void cmmd_2402_qurryUpGrade() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 2402);
    }
    /**
     * 查詢升級信息
     **/
    public void cmmd_2303_qurryTaoBaoInfo() {
        JsonObject data = new JsonObject();
        HttpConn.getInstance().sendMessage(data, 2303);
    }



    /**
     * 取微信支付信息
     **/
    private void backdata_1001_wxpay(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonObject tenpayParam = OJsonGet.getJsonObject(obj, "tenpayParam");
            ManagerCommon.getInstance().saveWxPay(tenpayParam);
            ODispatcher.dispatchEvent(OEventName.PAY_WX_RESULTBACK);
        }
    }


    /**
     * 取支付宝信息
     **/
    private void backdata_1002_checkpay(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            String paystr = OJsonGet.getString(obj, "paystr");
            ODispatcher.dispatchEvent(OEventName.PAY_CHECKPAY_RESULTBACK, paystr);
        }
    }

    /**
     * 取支付宝信息
     **/
    private void backdata_1004_pifupay(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.SKIN_PAY_RESULTBACK, obj);
        }
    }

    /**
     * 取充值券信息
     **/
    private void backdata_1005_recharge_vouchers(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            if (CACHE_ERROR.equals("")) {
                ManagerCarList.getInstance().saveCarInfo(OJsonGet.getJsonObject(obj, "carInfo"), "from_1005");
                ODispatcher.dispatchEvent(OEventName.VECHARGE_VOUCHERS_RESULTBACK);
            }
        }
    }


    /**
     * 取品牌列表
     **/
    public void backdata_1310_getBrandList(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            long updateTime = OJsonGet.getLong(obj, "updateTime");
            JsonArray arr = OJsonGet.getJsonArray(obj, "brands");
            ManagerCommon.getInstance().saveBrandList(arr, updateTime);
            ODispatcher.dispatchEvent(OEventName.COMMON_RESULTBACK);
        }
    }

    /**
     * 取通用信息
     **/
    private void backdata_1303_getCommonInfo(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ManagerCommon.getInstance().saveAuthorList(OJsonGet.getJsonArray(obj, "authors"));
            ManagerCommon.getInstance().saveBrandList(OJsonGet.getJsonArray(obj, "brands"), System.currentTimeMillis());
            ManagerCommon.getInstance().savePayWay(OJsonGet.getJsonArray(obj, "onlinePayInfos"));
            ManagerCommon.getInstance().saveContact(obj);
            ManagerCommon.getInstance().saveShare(obj);
            ManagerCommon.getInstance().savePayWayMiniX(OJsonGet.getJsonArray(obj, "miniXOnlinePayInfos"));
            ManagerCommon.hotLine = OJsonGet.getString(obj, "hotLine");
            ManagerCommon.email = OJsonGet.getString(obj, "email");
            ManagerCommon.dealerLine = OJsonGet.getString(obj, "dealerLine");
            JsonObject trackShareObj = OJsonGet.getJsonObject(obj, "trackShareObj");
            ManagerFlash.getInstance().saveFlash(OJsonGet.getString(obj, "splashAddress"));
            ManagerCommon.getInstance().saveTrackShareObj(trackShareObj);
            JsonObject adventInfoObj = OJsonGet.getJsonObject(obj, "adventInfo");
            if (adventInfoObj != null) {
                ManagerCommon.getInstance().saveDataAdvertising(adventInfoObj);
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("adventInfo", ODBHelper.convertString(adventInfoObj));
                if (ManagerCommon.getInstance().dataAdvertising != null && !TextUtils.isEmpty(ManagerCommon.getInstance().dataAdvertising.url)) {
                    ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("adventUrl", ManagerCommon.getInstance().dataAdvertising.url);
                    ManagerFlash.getInstance().saveAdvent(ManagerCommon.getInstance().dataAdvertising.url);
                }
            } else {
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("adventUrl", "");
            }
            ODispatcher.dispatchEvent(OEventName.COMMON_RESULTBACK);
        }
    }

    /**
     * 违章查询
     **/
    private void backdata_1205_violationGetList(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray illegalInfos = OJsonGet.getJsonArray(obj, "illegalInfos");
            ManagerCommon.getInstance().saveViolationList(illegalInfos);
            ODispatcher.dispatchEvent(OEventName.VIOLATION_LIST_BACK);
        }
    }

    /**
     * 取开关列表
     **/
    private void backdata_1305_getSwitchInfo(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray ctrlNoticeInfos = OJsonGet.getJsonArray(obj, "ctrlNoticeInfos");
            JsonArray alertNoticeInfos = OJsonGet.getJsonArray(obj, "alertNoticeInfos");
            JsonArray safeNoticeInfos = OJsonGet.getJsonArray(obj, "safeNoticeInfos");
            JsonArray secretNoticeInfos = OJsonGet.getJsonArray(obj, "secretNoticeInfos");
//            JsonObject shakeInfo   = OJsonGet.getJsonObject(obj, "shakeInfo");
//            JsonArray nonekeyNoticeInfos = OJsonGet.getJsonArray(obj, "nonekeyNoticeInfos");
            JsonArray watchNoticeInfos = OJsonGet.getJsonArray(obj, "watchNoticeInfos");
            String watchToken = OJsonGet.getString(obj, "watchToken");
            PHeadHttp.changeUserWatchToken(ManagerLoginReg.getInstance().getCurrentUser().userId, watchToken);
             if (BuildConfig.DEBUG) Log.e("watchToken", "switchback:" + watchToken);
            ManagerSwitchs.getInstance().saveSwitchControls(ctrlNoticeInfos);
            ManagerSwitchs.getInstance().saveSwitchWarnings(alertNoticeInfos);
            ManagerSwitchs.getInstance().saveSwitchSafetys(safeNoticeInfos);
            ManagerSwitchs.getInstance().saveSwitchPrivates(secretNoticeInfos);
//            BlueLinkNetSwitch.saveSwitchShake(shakeInfo);
//            ManagerSwitchs.getInstance().saveSwitchNoKey(nonekeyNoticeInfos);
            ManagerSwitchs.getInstance().saveSwitchWear(watchNoticeInfos);
            ODispatcher.dispatchEvent(OEventName.SWITCH_ALL_RESULTBACK);
            ODispatcher.dispatchEvent(OEventName.SWITCH_WARNINGS_RESULTBACK);
//            ODispatcher.dispatchEvent(OEventName.SWITCH_NOKEYS_RESULTBACK);
            ODispatcher.dispatchEvent(OEventName.SWITCH_WEARS_RESULTBACK);
//            ODispatcher.dispatchEvent(OEventName.CAR_CHOOSE_CHANGE);//刷新me界面
        }
    }

    /**
     * 修改开关列表
     **/
    private void backdata_1306_changeSwitch(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray ctrlNoticeInfos = OJsonGet.getJsonArray(obj, "ctrlNoticeInfos");
            JsonArray alertNoticeInfos = OJsonGet.getJsonArray(obj, "alertNoticeInfos");
            JsonArray safeNoticeInfos = OJsonGet.getJsonArray(obj, "safeNoticeInfos");
            JsonArray secretNoticeInfos = OJsonGet.getJsonArray(obj, "secretNoticeInfos");
//            JsonArray nonekeyNoticeInfos = OJsonGet.getJsonArray(obj, "nonekeyNoticeInfos");
            JsonArray watchNoticeInfos = OJsonGet.getJsonArray(obj, "watchNoticeInfos");
            String watchToken = OJsonGet.getString(obj, "watchToken");
            PHeadHttp.changeUserWatchToken(ManagerLoginReg.getInstance().getCurrentUser().userId, watchToken);
             if (BuildConfig.DEBUG) Log.e("watchToken", "switchback:" + watchToken);
            ManagerSwitchs.getInstance().saveSwitchControls(ctrlNoticeInfos);
            ManagerSwitchs.getInstance().saveSwitchWarnings(alertNoticeInfos);
            ManagerSwitchs.getInstance().saveSwitchSafetys(safeNoticeInfos);
            ManagerSwitchs.getInstance().saveSwitchPrivates(secretNoticeInfos);
//            ManagerSwitchs.getInstance().saveSwitchNoKey(nonekeyNoticeInfos);
            ManagerSwitchs.getInstance().saveSwitchWear(watchNoticeInfos);
            ODispatcher.dispatchEvent(OEventName.SWITCH_WARNINGS_RESULTBACK);
//            ODispatcher.dispatchEvent(OEventName.SWITCH_NOKEYS_RESULTBACK);
            ODispatcher.dispatchEvent(OEventName.SWITCH_WEARS_RESULTBACK);
        }
    }

    /**
     * 取聊天列表
     **/
    private void backdata_1309_chatlist(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            JsonArray msgInfos = OJsonGet.getJsonArray(obj, "msgInfos");
            ManagerChat.getInstance().saveChatList(msgInfos);
            ODispatcher.dispatchEvent(OEventName.CHAT_INFO_BACK);
        }

    }

    /**
     * 汽车终止授权，授权和围栏通知
     **/
    private void backdata_1223_getLostNotifation(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            //电子围栏处理消息
            JsonObject radiusMsgs = OJsonGet.getJsonObject(obj, "radiusMsgs");
            DataWarnings war = DataWarnings.fromJsonObject(radiusMsgs);
            if (war != null && war.alertId == 17) {
                ActivityKulalaMain.areaWar = war;//不能线程内用handler
                ODispatcher.dispatchEvent(OEventName.GLOBAL_NEED_CANCEL_AREA);
                ManagerWarnings.getInstance().saveNewWarnings(radiusMsgs);
            }
            //终止授权，同意授权消息
            JsonObject authorityMsgs = OJsonGet.getJsonObject(obj, "authorityMsgs");
            //保养到期消息
            JsonObject maintenanceMsg = OJsonGet.getJsonObject(obj, "maintenanceMsg");
            ManagerMaintainList.getInstance().maintain = DataMaintain.fromJsonObject(maintenanceMsg);
            if (ManagerMaintainList.getInstance().maintain != null) {
                ODispatcher.dispatchEvent(OEventName.MAINTAIN_MESSAGEBACK);
            }
            //紧急消息
            JsonObject urgentMsgs = OJsonGet.getJsonObject(obj, "urgentMsgs");
            if (urgentMsgs != null) {
                ManagerCommon.getInstance().saveMessageUserList(urgentMsgs);
                if (ManagerCommon.getInstance().messageUserList != null && ManagerCommon.getInstance().messageUserList.content != null) {

                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, false)
                            .withTitle("提示")
                            .withInfo(ManagerCommon.getInstance().messageUserList.content)
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm)
                                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_message_user);
                                }
                            }).show();
                }
            }
            //年检弹窗消息
            JsonObject annualMsg = OJsonGet.getJsonObject(obj, "annualMsg");
            if (annualMsg != null) {
                int alertType = OJsonGet.getInteger(annualMsg, "alertType");//弹框类型 1：过期提醒 2：其他弹窗
                final String alertContent = OJsonGet.getString(annualMsg, "alertContent");//弹窗内容
                final String comment = OJsonGet.getString(annualMsg, "comment");//备注
                long dueTime = OJsonGet.getLong(annualMsg, "dueTime");//提醒消息过期时间

                new TimeDelayTask().runTask(2000L, new TimeDelayTask.OnTimeEndListener() {
                    @Override
                    public void onTimeEnd() {
                        new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, false)
                                .withTitle(alertContent)
                                .withInfo(comment)
                                .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                    @Override
                                    public void onClickConfirm(boolean isClickConfirm) {
                                        if (!isClickConfirm) return;
                                        ManagerAnnual.getInstance().saveCarActiveList(ManagerCarList.getInstance().getCarAnnualList());
                                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_annual_reminder_main);
                                    }
                                }).show();

                    }
                });
            }
        }
    }

    /**
     * 取版本信息
     **/
    private void backdata_1302_getVersionInfo(JsonObject obj, String CACHE_ERROR) {
         if (BuildConfig.DEBUG) Log.e("1302", obj.toString());
        ODispatcher.dispatchEvent(OEventName.GETVERSIONINFO_RESULTBACK, obj);
    }

    /**
     * 取上传头像到千牛的token
     */
    private void backdata_1312getuplloadPic(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK, obj);
        }
    }

    /**
     * 投诉建议接口
     **/
    private void backdata_1313_sendSuggest(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.SUGGEST_HTTP_RESULTBACK, true);
        } else {
            ODispatcher.dispatchEvent(OEventName.SUGGEST_HTTP_RESULTBACK, false);
        }
    }

    /**
     * 摇一摇开关
     **/
    public void backdata_1315_setSwitchShakeOpen(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            //2018/08/09承车显示摇
//            JsonObject shakeInfo   = OJsonGet.getJsonObject(obj, "shakeInfo");
//            ManagerCarList.getInstance().saveCarInfo(OJsonGet.getJsonObject(obj, "carInfo"), "from_1315");
//            BlueLinkNetSwitch.saveSwitchShake(shakeInfo);
            OCtrlCar.getInstance().ccmd1203_getcarlist();
//            if (ViewSwitchShake.viewSwitchShakeThis != null)
//                ViewSwitchShake.viewSwitchShakeThis.handlerChangeSwitch();
        }

    }

    /**
     * 信号值设定 （1316）
     **/
    public void backdata_1316_nokeyin_set_signal(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            //成功
//            ODispatcher.dispatchEvent(OEventName.SWITCH_NOKEYS_SETVALUE_OK);
        }

    }

    /**
     * 取支付宝广告业是否展示
     **/
    private void backdata_2301_qurry_Display(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ManagerDisPlay.getInstance().savedisplayInfo(OJsonGet.getJsonArray(obj, "displays"));
            ODispatcher.dispatchEvent(OEventName.DISPLAY_RESULT_BACK);
        }
    }
    /**
     * 预约结果
     **/
    private void backdata_2401_upGrade(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"预约成功");
            ODispatcher.dispatchEvent(OEventName.UPGRADE_RESULT_BACK,true);
        }else{
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"预约失败");
            ODispatcher.dispatchEvent(OEventName.UPGRADE_RESULT_BACK,false);
        }
    }
    /**
     * 取预约信息
     **/
    private void backdata_2402_qurry_upGrade(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
           JsonObject object= OJsonGet.getJsonObject(obj, "upgradeRegister");
           if(object==null){
               ODispatcher.dispatchEvent(OEventName.UPGRADE_RESULT_BACK,false);
           }else{
               ODispatcher.dispatchEvent(OEventName.UPGRADE_RESULT_BACK,true);
           }
        }
    }
    /**
     * 取淘宝信息
     **/
    private void backdata_2303_qurry_Taobao(JsonObject obj, String CACHE_ERROR) {
        if (CACHE_ERROR.equals("")) {
            String url=  OJsonGet.getString(obj, "url");
            ManagerPublicData.taobaoUrl=url;
        }
    }
}
