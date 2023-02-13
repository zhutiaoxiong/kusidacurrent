package model.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

public class DataViolation {

	public int fee;//单位元
	public String area;//违章地点
	public String reason;//违章原因
	public String code;//违章代码
	public int status;//是否处理,1处理 0未处理
	public int score;//分数
	public long time;//违章时间
	public static DataViolation fromJsonObject(JsonObject obj) {
		Gson          gson    = new Gson();
		DataViolation thisobj = gson.fromJson(obj, DataViolation.class);
		return thisobj;
	}

	public static List<DataViolation> fromJsonArray(JsonArray payList) {
		if (payList == null || payList.size() == 0)return null;
		List<DataViolation> data = new ArrayList<DataViolation>();
		for (int i = 0; i < payList.size(); i++) {
			JsonObject    object = payList.get(i).getAsJsonObject();
			DataViolation info   = DataViolation.fromJsonObject(object);
			data.add(info);
		}
		return data;
	}
}
