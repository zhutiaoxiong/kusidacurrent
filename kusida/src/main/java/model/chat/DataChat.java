package model.chat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class DataChat {
	public String content = "";
	public long time;
	public long fromId;//系统消息1

	public static DataChat fromJsonObject(JsonObject obj) {
		Gson gson = new Gson();
		DataChat thisobj = gson.fromJson(obj, DataChat.class);
		return thisobj;
	}
}
