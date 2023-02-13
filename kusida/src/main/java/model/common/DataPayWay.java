package model.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

public class DataPayWay {

	public float fee;//单位元
	public int time;//返回给服务端的
	public String timeStr;//显示出来的时间

	public static DataPayWay fromJsonObject(JsonObject obj) {
		Gson gson = new Gson();
		DataPayWay thisobj = gson.fromJson(obj, DataPayWay.class);
		return thisobj;
	}

	public static List<DataPayWay> fromJsonArray(JsonArray payList) {
		if (payList == null || payList.size() == 0)return null;
		List<DataPayWay> data = new ArrayList<DataPayWay>();
		for (int i = 0; i < payList.size(); i++) {
			JsonObject object = payList.get(i).getAsJsonObject();
			DataPayWay info = DataPayWay.fromJsonObject(object);
			data.add(info);
		}
		return data;
	}
}
