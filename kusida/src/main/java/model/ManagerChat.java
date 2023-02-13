package model;

import com.client.proj.kusida.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import model.chat.DataChat;

public class ManagerChat {
	public List<DataChat>		ChatList;
	// ========================out======================
	private static ManagerChat	_instance;
	private ManagerChat() {
		init();
	}
	public static ManagerChat getInstance() {
		if (_instance == null)
			_instance = new ManagerChat();
		return _instance;
	}
	private void init() {
		ChatList = new ArrayList<DataChat>();
		DataChat chat = new DataChat();
		chat.content = GlobalContext.getContext().getResources().getString(R.string.hello_what_can_i_help_you);
		chat.fromId = 1;
		chat.time = ODateTime.getNow();
		ChatList.add(chat);
	}
	// ==========================get=================================
	public void saveChatOne(JsonObject obj) {
		DataChat chat = new DataChat();
		chat.content = OJsonGet.getString(obj, "content");
		chat.time= OJsonGet.getLong(obj, "createTime");
		chat.fromId= OJsonGet.getLong(obj, "fromId");
		ChatList.add(chat);
	}
	public void saveChatList(JsonArray msgInfos) {
		if (msgInfos == null)return;
		ChatList = new ArrayList<DataChat>();
		for (int i = 0; i < msgInfos.size(); i++) {
			JsonObject obj = msgInfos.get(i).getAsJsonObject();
			DataChat chat = new DataChat();
			chat.content = OJsonGet.getString(obj, "content");
			chat.time= OJsonGet.getLong(obj, "createTime");
			chat.fromId= OJsonGet.getLong(obj, "fromId");
			ChatList.add(chat);
		}
	}
}
