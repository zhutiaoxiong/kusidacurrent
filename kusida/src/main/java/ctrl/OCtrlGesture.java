package ctrl;

import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import common.http.HttpConn;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.MD5;

/**
 * 100-299
 */
public class OCtrlGesture{
	// ========================out======================
	private static OCtrlGesture _instance;
	protected OCtrlGesture() {
		init();
	}
	public static OCtrlGesture getInstance() {
		if (_instance == null)
			_instance = new OCtrlGesture();
		return _instance;
	}
	protected void init() {
	}
	public void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
		switch (protocol) {
			case 1331 :
				backdata_1311_setupGesture(obj,CACHE_ERROR);
				break;
		}
	}
	// ============================ccmd==================================
	/** 设置手势密码
	 * isOpen : 0：关闭，1：开启
	 * signPassword :用MD5加密，手势密码：从左到右，0-8; 加密方式：将"kulala_sign_" + 手势号码
	 **/
	public void ccmd1311_setupGesture(int isOpen, String signPassword) {
		JsonObject data = new JsonObject();
		data.addProperty("isOpen", isOpen);
		data.addProperty("signPassword", MD5.MD5generator("kulala_sign_"+signPassword));
		HttpConn.getInstance().sendMessage(data, 1311);
	}
	// ============================scmd==================================
	/** 设置手势密码 **/
	private void backdata_1311_setupGesture(JsonObject obj,String CACHE_ERROR) {
		if (CACHE_ERROR.equals("")) {
			ODispatcher.dispatchEvent(OEventName.SETUP_GESTURE_RESULTBACK);
		}
	}

}
