package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import model.common.DataAuthorLost;
import model.common.DataAuthoredUser;
import model.loginreg.DataUser;



public class ManagerAuthorization {
	public List<DataUser>               userList;//联系人表
	public List<DataAuthoredUser>       userListAuthored;//已授权联系人表
	public List<DataAuthorLost>         authorInfoSocket;//未处理的请求和终止权限
	// ========================out======================
	private static ManagerAuthorization _instance;
	private ManagerAuthorization() {
		init();
	}
	public static ManagerAuthorization getInstance() {
		if (_instance == null)
			_instance = new ManagerAuthorization();
		return _instance;
	}
	private void init(){
		authorInfoSocket = new ArrayList<DataAuthorLost>();
		String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("athorInfos");
		JsonObject obj = ODBHelper.convertJsonObject(result);
		if (obj != null) {
			JsonArray arr = OJsonGet.getJsonArray(obj, "athorInfos");
			saveAuthoredUserlist(arr);
		} else {
			userListAuthored = new ArrayList<DataAuthoredUser>();
		}
		String result1 = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("userInfos");
		JsonObject obj1 = ODBHelper.convertJsonObject(result);
		if (obj1 != null) {
			JsonArray arr = OJsonGet.getJsonArray(obj1, "userInfos");
			saveUserList(arr);
		} else {
			userList = new ArrayList<DataUser>();
		}
	}
	// ========================get======================
	public String[] getUserNameList() {
		if (userList == null)return null;
		String[] arr = new String[userList.size()];
		for(int i = 0; i< userList.size(); i++){
			DataUser data = userList.get(i);
			arr[i] = data.name +" "+data.phoneNum;
		}
		return arr;
	}
	// ========================put======================
	public void exit() {
		userList = new ArrayList<DataUser>();
	}
	public void saveUserList(JsonArray userInfos){
		userList = new ArrayList<DataUser>();
		if(userInfos == null)return;
		for(int i=0;i<userInfos.size();i++){
			JsonObject object = userInfos.get(i).getAsJsonObject();
			DataUser user = DataUser.fromJsonObject(object);
			userList.add(user);
		}
		JsonObject obj = new JsonObject();
		obj.add("userInfos", userInfos);
		ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("userInfos", ODBHelper.convertString(obj));
	}
	public void saveAuthoredUserlist(JsonArray athorInfos){
		userListAuthored = new ArrayList<DataAuthoredUser>();
		if(athorInfos == null)return;
		for(int i=0;i<athorInfos.size();i++){
			JsonObject object = athorInfos.get(i).getAsJsonObject();
			DataAuthoredUser user = DataAuthoredUser.fromJsonObjectt(object);
			userListAuthored.add(user);
		}
		JsonObject obj = new JsonObject();
		obj.add("athorInfos", athorInfos);
		ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("athorInfos", ODBHelper.convertString(obj));
	}
	public void saveAuthorInforSocket(JsonArray athorInfo){
		authorInfoSocket = DataAuthorLost.fromJsonArray(athorInfo);
	}
	public void saveAuthorInforSocketSingle(JsonObject athorInfo){
		if(athorInfo == null)return;
		authorInfoSocket.add(DataAuthorLost.fromJsonObject(athorInfo));
	}
}
