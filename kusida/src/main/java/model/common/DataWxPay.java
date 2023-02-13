package model.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

public class DataWxPay {

	public String appId;
	public String prepayId;
	public String partnerid;
	public String nonceStr;
	public String timestamp;
	public String packageValue;
	public String sign;
	public long orderId;

	public static DataWxPay fromJsonObject(JsonObject obj) {
		Gson gson = new Gson();
		DataWxPay thisobj = gson.fromJson(obj, DataWxPay.class);
		return thisobj;
	}

	public static List<DataWxPay> fromJsonArray(JsonArray payList) {
		if (payList == null || payList.size() == 0)return null;
		List<DataWxPay> data = new ArrayList<DataWxPay>();
		for (int i = 0; i < payList.size(); i++) {
			JsonObject object = payList.get(i).getAsJsonObject();
			DataWxPay info = DataWxPay.fromJsonObject(object);
			data.add(info);
		}
		return data;
	}
}
