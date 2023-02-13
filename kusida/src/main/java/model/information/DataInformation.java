package model.information;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

public class DataInformation {
	public long ide;
	public String pic = "";
	public String title = "";
	public String comment = "";//信息
	public String jumpUrl = "";


	public static DataInformation fromJsonObject(JsonObject obj) {
		Gson gson = new Gson();
		DataInformation thisobj = gson.fromJson(obj, DataInformation.class);
		return thisobj;
	}
	public static List<DataInformation> fromJsonArray(JsonArray informInfos) {
		if (informInfos == null || informInfos.size() == 0)return null;
		List<DataInformation> data = new ArrayList<DataInformation>();
		for (int i = 0; i < informInfos.size(); i++) {
			JsonObject object = informInfos.get(i).getAsJsonObject();
			DataInformation info = DataInformation.fromJsonObject(object);
			data.add(info);
		}
		return data;
	}
}
