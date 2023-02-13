package model.information;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

public class DataBanner {
	public long ide;
	public String pic = "";
	public int jumpType = 0;//0：不跳转，1：跳转webview 
	public String jumpUrl = "";

	public static DataBanner fromJsonObject(JsonObject obj) {
		Gson gson = new Gson();
		DataBanner thisobj = gson.fromJson(obj, DataBanner.class);
		return thisobj;
	}
	public static List<DataBanner> fromJsonArray(JsonArray informInfos) {
		if (informInfos == null || informInfos.size() == 0)return null;
		List<DataBanner> data = new ArrayList<DataBanner>();
		for (int i = 0; i < informInfos.size(); i++) {
			JsonObject object = informInfos.get(i).getAsJsonObject();
			DataBanner info = DataBanner.fromJsonObject(object);
			data.add(info);
		}
		return data;
	}
}
