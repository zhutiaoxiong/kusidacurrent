package ctrl;

import com.google.gson.JsonObject;
import com.kulala.staticsfunc.static_system.OJsonGet;
/**
 * 车控制结果回包肖息
 */

public class CarControlResult{
    public static int CARCONTROL_SENDED = 1;
    public static int CARCONTROL_ARRIVE_MODEL = 2;
    public static int CARCONTROL_SUCESS = 3;
    public int currentProcess;//1,2,3当前执行进度
    public long carId;

    public int instruction;//控制指令 1：开启2：熄火3：开锁4：解锁5：开启尾箱6：寻车
    public int status;// 0：不成功，1：成功
    public void fromData(JsonObject data){
        carId       = OJsonGet.getLong(data, "carId");
        instruction = OJsonGet.getInteger(data, "instruction");// 1：开启2：熄火3：开锁4：解锁
        status      = OJsonGet.getInteger(data, "status");// 0：不成功，1：成功
    }
}
