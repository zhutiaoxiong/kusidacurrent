package com.kulala.linkscarpods.blue;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kulala.linkscarpods.service.KulalaServiceC;
import com.kulala.linkscarpods.service.SocketConnSer;
import com.kulala.linkscarpods.service.SoundPlay;
import com.kulala.linkspods.BuildConfig;
import com.kulala.staticsfunc.LogMe;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.static_system.SystemMe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.kulala.linkscarpods.blue.CarControlResult.CARCONTROL_SENDED;
import static com.kulala.linkscarpods.blue.CarControlResult.CARCONTROL_SUCESS;

/**
 * 1.连接命令（cmd=1，返回cmd=101）
 * 2.心跳命令（cmd=2，返回cmd=102）
 * 3.服务端推送命令（cmd=3，返回cmd=103）
 * 4.客户端端推送命令（cmd=4，返回cmd=104）
 *
 */
public class OCtrlSocketMsgBackground {
    HashMap<Long, Integer> messageIdList;//已发送的消息ID+控制的指令,value1-6,value10086是发消息
    private static OCtrlSocketMsgBackground _instance;
    private OCtrlSocketMsgBackground() {
            messageIdList = new HashMap<Long, Integer>();
    }
    private Context mContext;
    public static OCtrlSocketMsgBackground getInstance() {
        if (_instance == null)
            _instance = new OCtrlSocketMsgBackground();
        return _instance;
    }
    public void
    processResult(int protocol, JsonObject obj) {
         if (BuildConfig.DEBUG) Log.e("------------", "後臺網絡  收"+protocol+"__________"+obj.toString());
        switch (protocol) {

            case 104: scmd_104(obj); break;//服务端推送命令（cmd=4，返回cmd=104）
            case 3: scmd_3(obj); break;//服务端推送命令（cmd=3，返回cmd=103）
        }
    }
    /**
     * 控制车 客户端端推送命令（cmd=4，返回cmd=104）
     * instruction控制命令1：开启2：熄火3：开锁4：解锁5：开启尾箱6：寻车 time只有0到7档，每档5分钟，0档位5分钟
     **/
    public void ccmd_controlCar(DataCarInfo car, int controlCmd, int time,Context context,long userId) {
        //蓝牙模式不收socket控制状态事件
//        if (BlueLinkReceiver.getInstance().getIsBlueConnOK()) return;
        mContext=context;
        preCarId = car.ide;
        preCmd = controlCmd;
        JsonObject data = new JsonObject();
        data.addProperty("carId", car.ide);
        long currentTime = System.currentTimeMillis();
        data.addProperty("ts", currentTime);
        JsonObject dataa = new JsonObject();
        dataa.addProperty("instruction", controlCmd);
        dataa.addProperty("time", time);
        data.addProperty("instruction", dataa.toString());
        try {
//            byte[] bytes = AES.AESgenerator(dataa.toString(), car.carsig);
//            if (bytes == null) return;
//            String strBase64 = new String(Base64.encode(bytes, Base64.DEFAULT));
//            data.addProperty("instruction", strBase64);
            sendToSocket(data, currentTime, controlCmd,context, userId);
        } catch (Exception e) {
            LogMe.e("TsControl","1.<><>error:"+e.toString());
            e.printStackTrace();
        }
    }
    /**
     * dataa是要发送的数据,messageId10086是聊天
     **/
    private void sendToSocket(JsonObject dataa, long messageId, int controlCmd, Context context,long userId) {
        JsonObject data = new JsonObject();//head
        data.addProperty("mType", 1);
         if (BuildConfig.DEBUG) Log.e("------------", "准备发 " );
        long fromId = 111;
        if(userId==0)return;
//        DataCarBlueLcd dataCarBlueLcd=DataCarBlueLcd.loadLocal(context);
//        fromId=dataCarBlueLcd.userId;
        fromId=userId;
//         fromId =  Long.parseLong(SocketUtil.getUserId(context));
         if (BuildConfig.DEBUG) Log.e("------------", "fromId后台" +fromId);
         if(fromId==0)return;
        data.addProperty("fromId", fromId);
        data.addProperty("deviceToken", SystemMe.getUUID(context) );
         if (BuildConfig.DEBUG) Log.e("查看devicetoken","后台的devicetoken是"+ SystemMe.getUUID(context));
        data.addProperty("createTime", messageId);//消息时间
        data.addProperty("messageId", messageId);//消息id，客户端生成，为唯一id
        data.add("data", dataa);
        data.addProperty("status", 1);//-1：没法连接到终端，0：不成功，1：成功
//        LogMe.e("TsControl","2.准备发Broadcast广播");
         if (BuildConfig.DEBUG) Log.e("------------", " data"+data.toString());
       SocketConnSer.getInstance().sendMessage(4, data.toString());
       messageIdList.put(messageId, controlCmd);
    }

    // ============================scmd==================================
    /**
     * 4.客户端端推送命令（cmd=4，返回cmd=104）
     **/
    private long preCarId;
    private int  preCmd;
    private void scmd_104(JsonObject obj) {
        if (obj == null) return;
         if (BuildConfig.DEBUG) Log.e("------------", "後臺網絡收104");
        String msgid     = OJsonGet.getString(obj, "msgid");//== messageId//消息id，客户端生成，为唯一id
        if(msgid==null||msgid.equals("null"))return;
         if (BuildConfig.DEBUG) Log.e("------------", "msgid"+msgid);
        long   messageId = Long.valueOf(msgid);
        Integer va =  messageIdList.get(messageId);
         if (BuildConfig.DEBUG) Log.e("------------", "va"+va);
        if(va == null)return;//没有发出过指令，怎么回了
        int    value     = va;
        messageIdList.remove(messageId);
        if (value == 10086) {
            //messageId10086是聊天
        } else {
            //蓝牙模式不收socket控制状态事件
//            if (BlueLinkControl.getInstance().getIsBlueConnOK()) return;
            //是发送控车
            //判斷是否在後臺
             if (BuildConfig.DEBUG) Log.e("------------", "後臺網絡收104控制指令");
            CarControlResult result = new CarControlResult();
            result.carId = preCarId;
            result.currentProcess = CARCONTROL_SENDED;
            result.instruction = preCmd;
            result.status = 1;
             if (BuildConfig.DEBUG) Log.e("------------", "scmd_104: "+result.instruction);
//            LogMe.e("TsControl","10. <><>收到汽车控制发送成功:"+msgid);
//            DataCarBlueLcd carInfo = BlueLinkControlLcdKey.getInstance().getDataCar();
//            if(carInfo == null)return;//车ID不正确
//            if(result.carId!=carInfo.carId){
//                return;
//            }
            String showStr = getControlString(result.instruction);
            if(result.currentProcess == CARCONTROL_SENDED){
                 if (BuildConfig.DEBUG) Log.e("modelStep","CARCONTROL_SENDED");
                if(result.status == 1){
//                    if(result.instruction == 1)LoadingCarStart.getInstance().gotoStepSendOK();/**2**/
                    showStr += "指令发送成功!";
                    if (result.instruction == 1) {
                         if (BuildConfig.DEBUG) Log.e("SoundPlay","control play_start");
                         if (BuildConfig.DEBUG) Log.e("------------", "後臺網絡  伯啓動聲音");
                        SoundPlay.getInstance().play_start(KulalaServiceC.kulalaServiceCThis);
                    } else if (result.instruction == 5) {
                         if (BuildConfig.DEBUG) Log.e("------------", "後臺網絡  開啓動聲音");
                        SoundPlay.getInstance().play_backpag(KulalaServiceC.kulalaServiceCThis);
                    } else if (result.instruction == 6) {
                         if (BuildConfig.DEBUG) Log.e("------------", "後臺網絡  波尋車聲音");
                        SoundPlay.getInstance().play_findcar(KulalaServiceC.kulalaServiceCThis);
                    }
                }else{
                    showStr += "指令发送失败!";
                }
            }else if(result.currentProcess == CARCONTROL_SUCESS){
                 if (BuildConfig.DEBUG) Log.e("modelStep","CARCONTROL_SUCESS");
                if(result.status == 1){
//                    if(result.instruction == 1)LoadingCarStart.getInstance().gotoStepRunOK();/**5**/
                    showStr += "指令执行成功!";
                    if (result.instruction == 1) {
//						OSoundPlay.getInstance().play_start();
                    } else if (result.instruction == 3 || result.instruction == 4) {
//                        SoundPlay.getInstance().play_lock(getContext());
                    }
                } else {
                    showStr += "指令执行失败!";
                }
            }else{
                 if (BuildConfig.DEBUG) Log.e("modelStep","CARCONTROL_ARRIVE_MODEL");
                showStr += "指令已发送模组!";
            }
        }
        //清除错误的消息，未回包的消息
        if ( messageIdList.size() > 200) {
            long     now  = System.currentTimeMillis();
            Iterator iter =  messageIdList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                long      key   = (Long) entry.getKey();
                if (now - key > 30000L)  messageIdList.remove(key);
            }
        }
    }

    private String getControlString(int cmd) {
        switch (cmd) {
            case 1:
                return "车辆启动";
            case 2:
                return "车辆熄火";
            case 3:
                return "车辆设防";
            case 4:
                return "车辆撤防";
            case 5:
                return "车辆开启尾箱";
            case 6:
                return "车辆寻车";
        }
        return "";
    }

    /**
     * 1：汽车控制消息（客户端发送）
     2：汽车控制结果消息（云端发送）
     3：汽车状态更新消息（云端发送）
     4：汽车预警消息（云端发送）
     5：聊天消息（客户端推送）
     6：聊天消息（云端推送给客户端）
     7：踢出登录（云端推送，客户端收到这条消息，则退出登录）
     8：副车主授权结果消息（云端推送，如果收到则直接弹个确定框）
     9：主车主授权和取消授权消息（云端推送，如果收到则直接直接弹框）
     10：紧急消息（云端推送，如果收到则直接直接弹框）
     11：卡片赠送消息（云端发送，收到后展示卡片详情）
     12：积分消息（云端发送，收到后弹toast）
     13：保养消息（云端发送，具体弹出框见data中的汽车保养对象的msgType）
     14：汽车控制收到信息（云端发送，提示GPS已经收到的消息）
     */


    /**
     * 3.服务端推送命令（cmd=3，返回cmd=103）
     * @param result {"mType":4,"content":"【粤A446688】汽车启动成功"
     *                 ,"fromId":0,"createTime":1545102953616,"messageId":"2140013709"
     *                 ,"data":{"alertType":2,"content":"【粤A446688】汽车启动成功","createTime":1545102953701
     *                 ,"alertId":1,"carId":14250},"messageAlertType":0,"isNotice":1}
     */
    public void scmd_3(final JsonObject result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Gson gson             = new Gson();
//                JsonObject result           = gson.fromJson(JsonData, JsonObject.class);
                int        mType            = OJsonGet.getInteger(result, "mType");//消息类型 1-14
                String     content          = OJsonGet.getString(result, "content");//消息说明
                long       userId           = OJsonGet.getLong(result, "fromId");//用户ID
                String     messageId        = OJsonGet.getString(result, "messageId");//服务端消息指令
                long       createTime       = OJsonGet.getLong(result, "createTime");
                int        isNotice         = OJsonGet.getInteger(result, "isNotice");//是否通知栏展示，0：不展示，1：展示
                int        messageAlertType = OJsonGet.getInteger(result, "messageAlertType");//0：不展示弹窗，1：弹窗（我知道了）

                JsonObject data             = OJsonGet.getJsonObject(result, "data");
                final long carId          = OJsonGet.getLong(data, "carId");
                final int  alertType      = OJsonGet.getInteger(data, "alertType");// 1：消息，2：警报，3：安全
                int        alertId        = OJsonGet.getInteger(data, "alertId");//1-31
                long       createTimeMesg = OJsonGet.getLong(data, "createTime");//服务端消息时间
//                DataCarInfo carInfo          = ManagerCarList.getInstance().getCurrentCar();
//                //判定消息超时,服务器控制
//                long now = System.currentTimeMillis();
//                long distance = now - createTime;
//                if(distance > FogotMessageTime + AverageMessageInterval)return;//超时的消息不理
//                if(distance > AverageMessageInterval + AverageMessageIntervalADD*2)AverageMessageInterval += AverageMessageIntervalADD;//均值增加
//                else if(distance < AverageMessageInterval - AverageMessageIntervalADD*2)AverageMessageInterval -= AverageMessageIntervalADD;//均值减少
//                 if (BuildConfig.DEBUG) Log.e("Average","AverageMessageInterval:"+AverageMessageInterval + " distance:"+distance);
//                //判定消息超时
                switch (mType) {
                    case 2://2：汽车控制结果消息（云端发送）
                        // 蓝牙模式不收socket控制状态事件
//                        if (BlueLinkReceiver.getInstance().getIsBlueConnOK()) return;
//                        CarControlResult result11 = new CarControlResult();
//                        result11.fromData(data);
//                        result11.currentProcess = CARCONTROL_SUCESS;
//                        ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result11);//http与此重复，不发消息，纯socket控车才发

                        break;
                    case 3://3：汽车状态更新消息（云端发送）只要有此状态就一定刷新
                        //蓝牙模式不收socket控制状态事件 ,在后台的时候收控制消息
//                        if (BlueLinkControl.getInstance().getIsBlueConnOK()) return;
                         if (BuildConfig.DEBUG) Log.e("------------", "收3回状态钥匙");
                        if (data == null) return;
                        JsonObject carStatusInfo = OJsonGet.getJsonObject(data, "carStatusInfo");
                        //在後臺的socket不收gps信息
                        carStatusInfo.addProperty("ide", carId);
//                        DataCarStatus getStatus = DataCarStatus.fromJsonObject(carStatusInfo);

//                        DataCarBlueLcd dataCarBlueLcd=DataCarBlueLcd.loadLocal(mContext);
//                        if(getStatus==null)return;
//                         if(dataCarBlueLcd.carId !=getStatus.carId)return;
                         if (BuildConfig.DEBUG) Log.e("------------", "後臺網絡  囘狀態給液晶鑰匙");
                        if(carStatusInfo.toString().equals(""))return;
                        if(BlueLinkReciverServiceCToServiceA.getInstance()!=null){
                            BlueLinkReciverServiceCToServiceA.getInstance().sendCarstatusServiceCToServiceA(carStatusInfo.toString());
                        }
//                       BlueLinkControlLcdKey.getInstance().sendMessage(bytesToHexString( LcdManagerCarStatus.ObjectToByteBlue(getStatus)),true);
                        break;
                    case 4://4：汽车预警消息（云端发送）
                        break;
                    case 6://6：聊天消息（云端推送给客户端）
                        break;
                    case 7://7：踢出登录（云端推送，客户端收到这条消息，则退出登录）
                        break;
                    case 8://修改为副车主需要刷新页面,已返回汽车粤888888的权限
                        break;
                    case 9://9：主车主授权和取消授权消息（云端推送，如果收到则直接直接弹框）已取消
//			ManagerAuthorization.getInstance().saveAuthorInforSocketSingle(data);
//			ODispatcher.dispatchEvent(OEventName.GLOBAL_NEED_POPCONFIRM_NEXT);
                        break;
                    case 10://10：紧急消息（云端推送，如果收到则直接直接弹框）
                        if (data == null) return;
                        break;
                    case 11://11：卡片赠送消息（云端发送，收到后展示卡片详情）
                        break;
                    case 12://12：积分消息（云端发送，收到后弹toast）
                        break;
                    case 13://13：保养消息（云端发送，具体弹出框见data中的汽车保养对象的msgType）
                        break;
                    case 14://14：汽车控制收到信息（云端发送，提示GPS已经收到的消息） 已发送消息到模组
                        // 蓝牙模式不收socket控制状态事件
                        break;
                    case 15://15：副车主授权结果消息（车豆夹使用）
                        break;
                }
                SocketConnSer.getInstance().sendMessage( 103, "");
            }
        }).start();
    }
}
