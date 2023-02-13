package common.http;

import com.google.gson.JsonObject;
/**
 * Created by Administrator on 2017/2/10.
 */
public interface GetDataListener {
    JsonObject getPHeadJsonObj(String subscription);

    String getBasicUrl();
}
