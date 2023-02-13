package ctrl;

import android.util.Log;

import com.client.proj.kusida.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.toast.ToastTxt;

import annualreminder.ctrl.OCtrlAnnual;
import common.GlobalContext;
import common.PHeadHttp;
import common.http.GetDataListener;
import common.http.HttpConn;
import common.http.OnHttpStateListener;
import model.ManagerCommon;
import view.EquipmentManager;

public class OCtrlBaseHttp{
	//	private static int lastprotocol = 0;
//	private static long lasttime = 0;
	// ========================out======================
	private static OCtrlBaseHttp _instance;

	private OCtrlBaseHttp() {
		init();
	}

	public static OCtrlBaseHttp getInstance() {
		if (_instance == null)
			_instance = new OCtrlBaseHttp();
		return _instance;
	}

	// ========================out======================
	protected void init() {
		HttpConn.getInstance().setGetDataListener(new GetDataListener() {
			@Override
			public JsonObject getPHeadJsonObj(String subscription) {
				return PHeadHttp.getPHead(subscription);
			}
			@Override
			public String getBasicUrl() {
				return PHeadHttp.getBasicUrl();
			}
		});
		HttpConn.getInstance().setOnHttpStateListener(new OnHttpStateListener() {
			@Override
			public void onSendStart(int cmd) {
				if(cmd == 1219)return;
//				ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_SHOW);//这里不要弹，太多会卡死
			}
			@Override
			public void onSendFailed(int cmd,String exceptionInfo) {
				if(cmd == 1219){//1219刷新车辆不提示
					if(OCtrlCar.getInstance().isRefreshBtn){//是点了刷新按扭
						ODispatcher.dispatchEvent(OEventName.PRESS_REFRESH_RESULTOK,false);
					}
//				ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_LOADING_HIDE);
					return;
				}
				if(cmd != 1408 && cmd != 1409 && cmd != 1232){
					if(cmd==6001){
						ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "参数同步失败，网络不良！");
					}else{
						ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "网络连接失败！");
					}
				}
				ODispatcher.dispatchEvent(OEventName.HTTP_CONN_ERROR, cmd);
			}
			@Override
			public void onReceiveData(int cmd, String receiveStr) {//收包了就是成功了
				try {
					Gson       gson   = new Gson();
					JsonObject data   = gson.fromJson(receiveStr, JsonObject.class);
					JsonElement jsonResult=	data.get("result");
//					JsonObject result = data.get("result").getAsJsonObject();
						if(jsonResult!=null) {
							JsonObject result = jsonResult.getAsJsonObject();
							int status = result.get("status").getAsInt();// status=1:处理成功；status=-1:服务器处理出错；status=-2:业务处理异常；
							if (status == 1) {
								processResult(cmd, data, "");//发包成功的
							} else {
								String CACHE_ERROR = handlerResult(result, cmd);
								processResult(cmd, data, CACHE_ERROR);
							}
						}
				} catch (JsonSyntaxException e) {
//					 if (BuildConfig.DEBUG) Log.e("<<<Http>>>", ">>>JsonSyntaxException<<<:" + e.toString() + "\n" + receiveStr);
				}
			}
		});
	}
	protected void processResult(int protocol, JsonObject obj,String CACHE_ERROR) {
		OCtrlRegLogin.getInstance().processResult(protocol,obj,CACHE_ERROR);
		OCtrlAnswer.getInstance().processResult(protocol,obj,CACHE_ERROR);
		OCtrlAuthorization.getInstance().processResult(protocol,obj,CACHE_ERROR);
		OCtrlCar.getInstance().processResult(protocol,obj,CACHE_ERROR);
		OCtrlCommon.getInstance().processResult(protocol,obj,CACHE_ERROR);
		OCtrlGesture.getInstance().processResult(protocol,obj,CACHE_ERROR);
		OCtrlGps.getInstance().processResult(protocol,obj,CACHE_ERROR);
		OCtrlInformation.getInstance().processResult(protocol,obj,CACHE_ERROR);
		OCtrlCard.getInstance().processResult(protocol, obj,CACHE_ERROR);
		OCtrlScore.getInstance().processResult(protocol, obj,CACHE_ERROR);
		OCtrlBlueTooth.getInstance().processResult(protocol, obj,CACHE_ERROR);
		OCtrlAnnual.getInstance().processResult(protocol, obj,CACHE_ERROR);
	}
	// ============================error==================================
	/**
	 * -1:运行时异常0:无异常(status=1的时候)1:数据库操作异常2:网络处理异常3:业务逻辑异常4:java代码异常5:通用错误：
	 * 客户端弹toast将msg显示出来6:账号过期，需要重新登录,客户端直接退出
	 **/
	protected String handlerResult(JsonObject result,int protoco1) {
		int status = result.get("status").getAsInt();// status=1:处理成功；status=-1:服务器处理出错；status=-2:业务处理异常；
		if (status == 1)return "";//发包成功的
		int errorcode = OJsonGet.getInteger(result, "errorcode");
		final String msg = OJsonGet.getString(result, "msg");
		Log.e("这是谁",""+protoco1+"-----"+msg);
		if(protoco1 == 1219 && errorcode!=6){
			return msg;//1219刷新车辆不提示,退出用户才执行下面
		}
		String CACHE_ERROR = "";
		switch (errorcode) {
			case -1 :
				CACHE_ERROR = GlobalContext.getContext().getString(R.string.runtime_exception);
				break;
			case 1 :
//				CACHE_ERROR = GlobalContext.getContext().getString(R.string.database_operations_exception);
				 break;
			case 2 :
				CACHE_ERROR = GlobalContext.getContext().getString(R.string.exception_handling_network);
				break;
			case 3 :
				CACHE_ERROR = GlobalContext.getContext().getString(R.string.business_logic_is_abnormal);
				break;
			case 4 :
				CACHE_ERROR = GlobalContext.getContext().getString(R.string.java_code_exception);
				break;
			case 5 :
				CACHE_ERROR = msg;
				break;
			case 6 :
				ManagerCommon.getInstance().exitToLogin(msg);
				break;
			case 7:
				CACHE_ERROR = msg;
				ODispatcher.dispatchEvent(OEventName.CHECK_VERFICODE_FAILED_THREE);
				break;
			case 8:
				CACHE_ERROR = msg;
				ODispatcher.dispatchEvent(OEventName.CHECK_VERFICODE_FAILED_FIVE);
				break;
		}
//		 if (BuildConfig.DEBUG) Log.e("HttpBase", "CACHE_ERROR:"+CACHE_ERROR);
		if(CACHE_ERROR.equals(GlobalContext.getContext().getString(R.string.business_logic_is_abnormal))){
			if (GlobalContext.IS_DEBUG_MODEL) {
				new ToastTxt(GlobalContext.getCurrentActivity(),null).withInfo(CACHE_ERROR).show();
			} else {
				//非debug不提示
			}
		}
		else
			if (!CACHE_ERROR.equals("")) {
//			if(errorcode==7||errorcode==8){
//				return "";
//			}
			if(protoco1 != 1408 && protoco1 != 1409 && protoco1!=1226&&errorcode!=7&&errorcode!=8&&protoco1!=2104&&protoco1!=2105&&!isMiniAnd1213(protoco1)&&protoco1!=2302) {
				new ToastTxt(GlobalContext.getCurrentActivity(),null).withInfo(CACHE_ERROR).show();
//				ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, CACHE_ERROR);
			}
		}
		return CACHE_ERROR;
	}
	private boolean isMiniAnd1213(int protoco1){
		if(EquipmentManager.isMini()&&protoco1==1213){
			return true;
		}
		return false;
	}

}
