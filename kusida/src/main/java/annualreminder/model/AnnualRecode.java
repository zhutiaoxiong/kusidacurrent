package annualreminder.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2017/2/25.
 * 年检记录对象定义
 */

public class AnnualRecode {
    public long id;//年检提醒记录id,修改时需要上传
    public float fee;//年检费用
    public String comment="";//年检备注
    public long inspectionTime;//年检日期
    public static AnnualRecode fromJsonObject(JsonObject obj) {
        Gson         gson    = new Gson();
        AnnualRecode thisobj = gson.fromJson(obj, AnnualRecode.class);
        return thisobj;
    }

    public static JsonObject toJsonObject(AnnualRecode info) {
        Gson       gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
    public static List<AnnualRecode> fromJsonArray(JsonArray array) {
        if (array == null || array.size() == 0)return null;
        List<AnnualRecode> data = new ArrayList<AnnualRecode>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject   object = array.get(i).getAsJsonObject();
            AnnualRecode info   = AnnualRecode.fromJsonObject(object);
            data.add(info);
        }
        return data;
    }
}
