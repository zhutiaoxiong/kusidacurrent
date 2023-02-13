package ctrl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import common.http.HttpConn;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;

import model.ManagerAuthorization;
import model.ManagerCarList;
import model.ManagerPublicData;

/**
 * 100-299
 */
public class OCtrlAuthorization{
	// ========================out======================
	private static OCtrlAuthorization	_instance;
	private OCtrlAuthorization() {
		init();
	}
	public static OCtrlAuthorization getInstance() {
		if (_instance == null)
			_instance = new OCtrlAuthorization();
		return _instance;
	}
	protected void init() {
	}
	// ========================out======================
	public void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
		switch (protocol) {
			case 1210 :
				backdata_1210_getuserlist(obj,CACHE_ERROR);
				break;
			case 1211 :
				backdata_1211_findnewuser(obj,CACHE_ERROR);
				break;
			case 1212 :
				backdata_1212_deleteuser(obj,CACHE_ERROR);
				break;
			case 1206 :
				backdata_1206_giveauthor(obj,CACHE_ERROR);
				break;
			case 1207 :
				backdata_1207_stopauthor(obj,CACHE_ERROR);
				break;
			case 1208 :
				backdata_1208_getAuthorUserlist(obj,CACHE_ERROR);
				break;
			case 1209 :
				backdata_1209_scan(obj,CACHE_ERROR);
				break;
			case 1222 :
				backdata_1222_codriver_confirmauthor(obj,CACHE_ERROR);
				break;
			case 1601 :
				backdata_1601_backAuthCode(obj,CACHE_ERROR);
				break;
			case 1602 :
				backdata_1602_backWXCode(obj,CACHE_ERROR);
				break;
		}
	}
	// ============================ccmd==================================
	/** 取联系人列表 **/
	public void ccmd1210_getuserlist() {
		HttpConn.getInstance().sendMessage(null, 1210);
	}
	/** 取授权中联系人列表 **/
	public void ccmd1208_getAuthorUserlist() {
		HttpConn.getInstance().sendMessage(null, 1208);
	}
	/** 添加新联系人 **/
	public void ccmd1211_findnewuser(String phoneNum) {
		JsonObject data = new JsonObject();
		data.addProperty("phoneNum", phoneNum);
		HttpConn.getInstance().sendMessage(data, 1211);
	}
	public void ccmd1211_findnewuser(String phoneNum,String name) {
		JsonObject data = new JsonObject();
		data.addProperty("phoneNum", phoneNum);
		data.addProperty("note",name);
		HttpConn.getInstance().sendMessage(data, 1211);
	}

	/** 删除联系人 **/
	public void ccmd1212_deleteuser(long userId) {
		JsonObject data = new JsonObject();
		data.addProperty("userId", userId);
		HttpConn.getInstance().sendMessage(data, 1212);
	}
	/** 授权副车主 **/
	public void ccmd1206_giveauthor(long carId, JsonArray authoritys, String phoneNum, long startTime, long endTime) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		data.add("authoritys", authoritys);
		data.addProperty("phoneNum", phoneNum);
		data.addProperty("startTime", startTime);
		data.addProperty("endTime", endTime);
		HttpConn.getInstance().sendMessage(data, 1206);
	}
	/** 授权副车主 type1先取消，再受出去 **/
	public void ccmd1206_giveauthor(long carId, JsonArray authoritys, String phoneNum, long startTime, long endTime,int type) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		data.add("authoritys", authoritys);
		data.addProperty("phoneNum", phoneNum);
		data.addProperty("startTime", startTime);
		data.addProperty("endTime", endTime);
		data.addProperty("type", type);
		HttpConn.getInstance().sendMessage(data, 1206);
	}
	/** 终止授权副车主 **/
	public void ccmd1207_stopauthor(long authorityId,long userId) {
		JsonObject data = new JsonObject();
		data.addProperty("authorityId", authorityId);
		data.addProperty("userId", userId);
		HttpConn.getInstance().sendMessage(data, 1207);
	}
	/** 终止授权副车主 **/
	public void ccmd1207_stopauthor(long authorityId,long userId,long carId) {
		JsonObject data = new JsonObject();
		data.addProperty("authorityId", authorityId);
		data.addProperty("userId", userId);
		data.addProperty("carId", carId);
		HttpConn.getInstance().sendMessage(data, 1207);
	}
	/** 终止授权type1林时 **/
	public void ccmd1207_stopauthor(long userId,long carId,int type) {
		JsonObject data = new JsonObject();
		data.addProperty("userId", userId);
		data.addProperty("carId", carId);
		data.addProperty("type", type);
		HttpConn.getInstance().sendMessage(data, 1207);
	}
	/** 副车主同意授权和终止授权 type 1：同意授权，2：拒绝授权，3：同意终止授权，4：拒绝终止授权**/
	public void ccmd1222_codriver_confirmauthor(long authorityId, int type) {
		JsonObject data = new JsonObject();
		data.addProperty("authorityId", authorityId);
		data.addProperty("type", type);
		HttpConn.getInstance().sendMessage(data, 1222);
	}
	/** 副车主扫码 **/
	public void ccmd1209_scan(String authCode) {
		JsonObject data = new JsonObject();
		data.addProperty("authCode", authCode);
		HttpConn.getInstance().sendMessage(data, 1209);
	}
	/** 取授权码 **/
	public void ccmd1601_getAuthCode(long carId) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		HttpConn.getInstance().sendMessage(data, 1601);
	}

	/** 取二维码 **/
	public void ccmd1602_getWXerweiCode(String authCode) {
		JsonObject data = new JsonObject();
		data.addProperty("authCode", authCode);
		HttpConn.getInstance().sendMessage(data, 1602);
	}
	// ============================scmd==================================
	/** 取联系人列表 **/
	private void backdata_1210_getuserlist(JsonObject obj,String CACHE_ERROR) {
		if(CACHE_ERROR.equals("")){
			ManagerAuthorization.getInstance().saveUserList(OJsonGet.getJsonArray(obj, "userInfos"));
			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USERLIST_RESULTBACK);
		}
	}
	/** 取授权中联系人列表 **/
	private void backdata_1208_getAuthorUserlist(JsonObject obj,String CACHE_ERROR) {
		if(CACHE_ERROR.equals("")){
			JsonArray athorInfos = OJsonGet.getJsonArray(obj, "athorInfos");
			ManagerAuthorization.getInstance().saveAuthoredUserlist(athorInfos);
			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USERLIST_AUTHORED_RESULTBACK);
		}
	}
	/** 添加新联系人 **/
	private void backdata_1211_findnewuser(JsonObject obj,String CACHE_ERROR) {
		if(CACHE_ERROR.equals("")){
			ManagerAuthorization.getInstance().saveUserList(OJsonGet.getJsonArray(obj, "userInfos"));
			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USERLIST_RESULTBACK_CHANGE,false);
		}else{
			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USERLIST_RESULTBACK_CHANGE,true);
			ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, CACHE_ERROR);
		}
	}
	/** 删除联系人 **/
	private void backdata_1212_deleteuser(JsonObject obj,String CACHE_ERROR) {
		if(CACHE_ERROR.equals("")){
			ManagerAuthorization.getInstance().saveUserList(OJsonGet.getJsonArray(obj, "userInfos"));
			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USERLIST_RESULTBACK);
		}
	}
	/** 授权副车主 **/
	private void backdata_1206_giveauthor(JsonObject obj,String CACHE_ERROR) {
		if(CACHE_ERROR.equals("")){
			String code = OJsonGet.getString(obj, "authCode");
			ManagerPublicData.authCode=code;
			//只有零时才有列表，不要影响手机收全
			JsonArray array = OJsonGet.getJsonArray(obj, "carInfos");
			if(array!=null && array.size()>0)ManagerCarList.getInstance().saveCarList(array, "backdata_1206");
			ODispatcher.dispatchEvent(OEventName.AUTHOR_CODRIVER_RESULTBACK,code);
		}
	}
	/** 副车主同意授权和终止授权 type 1：同意授权，2：拒绝授权，3：同意终止授权，4：拒绝终止授权**/
		private void backdata_1222_codriver_confirmauthor(JsonObject obj,String CACHE_ERROR) {
			if(CACHE_ERROR.equals("")){
				ODispatcher.dispatchEvent(OEventName.CODRIVER_CONFIRM_AUTHOR_RESULTBACK);
			}
		}
	/** 终止授权副车主 **/
	public void backdata_1207_stopauthor(JsonObject obj,String CACHE_ERROR) {
		if(CACHE_ERROR.equals("")){
			//只有零时才有列表，不要影响手机收全
			JsonArray array = OJsonGet.getJsonArray(obj, "carInfos");
			if(array!=null && array.size()>0)ManagerCarList.getInstance().saveCarList(array, "backdata_1207");
			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USER_STOPED);
		}
	}
	/** 副车主扫码 **/
	public void backdata_1209_scan(JsonObject obj,String CACHE_ERROR) {
		if(CACHE_ERROR.equals("")){
			ODispatcher.dispatchEvent(OEventName.QRCODE_CODRIVER_RESULTBACK);
		}
	}
	/** 取授权码结果 **/
	public void backdata_1601_backAuthCode(JsonObject obj,String CACHE_ERROR) {
		if(CACHE_ERROR.equals("")){
			String code = OJsonGet.getString(obj, "authCode");
			ManagerPublicData.authCode=code;
		}
	}

	/** 取授权码结果 **/
	public void backdata_1602_backWXCode(JsonObject obj,String CACHE_ERROR ) {
		if(CACHE_ERROR.equals("")){
			String appletCode = OJsonGet.getString(obj, "appletCode");
			ManagerPublicData.appleCode=appletCode;
			ODispatcher.dispatchEvent(OEventName.APPLECODE_RESULTBACK);
		}
	}


}
