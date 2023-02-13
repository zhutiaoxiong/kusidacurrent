package model.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

public class DataContact {

	public String hotLine;
	public String email;
	public String dealerLine;

	public static DataContact fromJsonObject(JsonObject obj) {
		Gson gson = new Gson();
		DataContact thisobj = gson.fromJson(obj, DataContact.class);
		return thisobj;
	}

	public static List<DataContact> fromJsonArray(JsonArray payList) {
		if (payList == null || payList.size() == 0)return null;
		List<DataContact> data = new ArrayList<DataContact>();
		for (int i = 0; i < payList.size(); i++) {
			JsonObject object = payList.get(i).getAsJsonObject();
			DataContact info = DataContact.fromJsonObject(object);
			data.add(info);
		}
		return data;
	}


}
