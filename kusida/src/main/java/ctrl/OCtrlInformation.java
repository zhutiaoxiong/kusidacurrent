package ctrl;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import common.http.HttpConn;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;

import model.ManagerCarList;
import model.ManagerInformation;
import model.find.DataSelectCar;

/**
 * 100-299
 */
public class OCtrlInformation{
	// ========================out======================
	private static OCtrlInformation	_instance;
	protected OCtrlInformation() {
		init();
	}
	public static OCtrlInformation getInstance() {
		if (_instance == null)
			_instance = new OCtrlInformation();
		return _instance;
	}
	protected void init() {
	}
	// ========================out======================
	public void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
		switch (protocol) {
			case 1308 :
				backdata_1308_getInfoList(obj,CACHE_ERROR);
				break;
			case 1401 :
				backdata_1401_getSkinList(obj,CACHE_ERROR);
				break;
			case 1402 :
				backdata_1402_getSkinAddress(obj,CACHE_ERROR);
				break;
			case 1403:
				backdata_1403getCardressupInfo(obj,CACHE_ERROR);
				break;
			case 1407:
				backdata_1407_getBoughtSkins(obj,CACHE_ERROR);
				break;
		}
	}
	// ============================ccmd==================================
	/** 取资讯列表 **/
	public void ccmd1308_getInfoList(long start,int size) {
		JsonObject data = new JsonObject();
		data.addProperty("start", start);
		data.addProperty("size", size);
		HttpConn.getInstance().sendMessage(data, 1308);
	}
	/** 取个性装扮列表 **/
	public void ccmd1401_getSkinList() {
		HttpConn.getInstance().sendMessage(null, 1401);
	}
	/** 取皮肤列表己购买 **/
	public void ccmd1407_getBoughtSkins() {
		HttpConn.getInstance().sendMessage(null, 1407);
	}
	/** 下载装扮地址 **/
	public int skinId1402;
	public void ccmd1402_getSkinAddress(int carTypeId,int type) {
		JsonObject data = new JsonObject();
		skinId1402 = carTypeId;
		data.addProperty("carTypeId", carTypeId);
		data.addProperty("type", type);
		HttpConn.getInstance().sendMessage(data, 1402);
	}

	/**选择要装扮的车辆*/
	public void ccmd1403_submit_select_car(Long[] carIds,  int carTypeId,int type) {
		DataSelectCar eafcer = new DataSelectCar();
		eafcer.carIds = carIds;
		eafcer.carTypeId = carTypeId;
		eafcer.type = type;
		JsonObject data = eafcer.toJsonObject(eafcer);
		Log.e("asdad",type+""+carTypeId);
		HttpConn.getInstance().sendMessage(data, 1403);
	}
	// ============================scmd==================================
	/** 取资讯列表 **/
	private void backdata_1308_getInfoList(JsonObject obj,String CACHE_ERROR) {
		if (CACHE_ERROR.equals("")) {
			JsonArray informInfos = OJsonGet.getJsonArray(obj, "informInfos");
			JsonArray bannerInfos = OJsonGet.getJsonArray(obj, "bannerInfos");
			ManagerInformation.getInstance().saveInfoList(informInfos);
			ManagerInformation.getInstance().saveBannerList(bannerInfos);
			ODispatcher.dispatchEvent(OEventName.INFORMATION_LIST_RESULTBACK);
		}
	}
	/** 取个性装扮列表 **/
	private void backdata_1401_getSkinList(JsonObject obj,String CACHE_ERROR) {
		if (CACHE_ERROR.equals("")) {
			JsonArray carTypeInfos = OJsonGet.getJsonArray(obj, "carTypeInfos");
			ManagerInformation.getInstance().saveSkinList(carTypeInfos);
			ODispatcher.dispatchEvent(OEventName.INFORMATION_SKINLIST_RESULTBACK);
		}
	}
	/** 取皮肤列表己购买 **/
	private void backdata_1407_getBoughtSkins(JsonObject obj,String CACHE_ERROR) {
		if (CACHE_ERROR.equals("")) {
			JsonArray carTypeInfos = OJsonGet.getJsonArray(obj, "carTypeInfos");
			ManagerInformation.getInstance().saveSkinBoughtList(carTypeInfos);
			ODispatcher.dispatchEvent(OEventName.INFORMATION_SKINBOUGHTLIST_RESULTBACK);
		}
	}
	/** 下载装扮地址 **/
	private void backdata_1402_getSkinAddress(JsonObject obj,String CACHE_ERROR) {
		if (CACHE_ERROR.equals("")) {
			String url = OJsonGet.getString(obj, "url");
			ODispatcher.dispatchEvent(OEventName.SKIN_URL_RESULTBACK,url);
		}
	}
	/**
	 * 取汽车装扮选择车辆返回的信息
	 */
	private void backdata_1403getCardressupInfo(JsonObject obj,String CACHE_ERROR){
		if(CACHE_ERROR.equals("")){
			JsonArray carInfos = OJsonGet.getJsonArray(obj, "carInfos");
			ManagerCarList.getInstance().saveCarList(carInfos,"backdata_1403getCardressupInfo");
			ODispatcher.dispatchEvent(OEventName.GET_CARDRESSUP_RESULTBACK);
			ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
		}
	}

}
