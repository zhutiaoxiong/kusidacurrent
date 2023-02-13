package model.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

public class DataShare {

	public String shareTitle;//分享下载地址标题
	public String shareComment;//分享下载地址的内容
	public String sharePic;//分享下载地址图片的地址
	public String shareUrl;//分享下载的地址
	public String shareOfficeTitle;
	public String shareOfficeComment;
	public String shareOfficePic;
	public String shareOfficeUrl;

	public static DataShare fromJsonObject(JsonObject obj) {
		Gson gson = new Gson();
		DataShare thisobj = gson.fromJson(obj, DataShare.class);
		return thisobj;
	}

	public static List<DataShare> fromJsonArray(JsonArray payList) {
		if (payList == null || payList.size() == 0)return null;
		List<DataShare> data = new ArrayList<DataShare>();
		for (int i = 0; i < payList.size(); i++) {
			JsonObject object = payList.get(i).getAsJsonObject();
			DataShare info = DataShare.fromJsonObject(object);
			data.add(info);
		}
		return data;
	}
}
