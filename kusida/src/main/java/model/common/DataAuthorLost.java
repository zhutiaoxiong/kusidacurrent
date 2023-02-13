package model.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/15.
 */
public class DataAuthorLost {
    public long authorityId;//授权id
    public String content;//弹框内容
    public long startTime;//开始时间
    public long endTime;//结束时间
    public String[] authoritys = new String[0];//权限列表
    public int type;//类型 1：主车主请求授权，2：主车主请求终止授权

	public static DataAuthorLost fromJsonObject(JsonObject obj) {
		if(obj == null)return new DataAuthorLost();
		Gson      gson    = new Gson();
        DataAuthorLost thisobj = gson.fromJson(obj, DataAuthorLost.class);
		return thisobj;
	}
	public static List<DataAuthorLost> fromJsonArray(JsonArray jsonarray) {
		if (jsonarray == null || jsonarray.size() == 0)
			return null;
		List<DataAuthorLost> data = new ArrayList<DataAuthorLost>();
		for (int i = 0; i < jsonarray.size(); i++) {
			JsonObject object = jsonarray.get(i).getAsJsonObject();
            DataAuthorLost info = DataAuthorLost.fromJsonObject(object);
			data.add(info);
		}
		return data;
	}
}
