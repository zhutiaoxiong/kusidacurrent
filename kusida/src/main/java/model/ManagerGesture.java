package model;


import com.kulala.staticsfunc.dbHelper.ODBHelper;

import common.GlobalContext;

public class ManagerGesture {
	private int isOpenGesture;
	private String signPasswordGesture = "";
	// ========================out======================
	private static ManagerGesture _instance;
	private ManagerGesture() {
		init();
	}
	public static ManagerGesture getInstance() {
		if (_instance == null)
			_instance = new ManagerGesture();
		return _instance;
	}
	private void init() {
		loadLocalData();
	}
	// =================================================
	public void loadLocalData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenGesture");
				isOpenGesture = ODBHelper.queryResult2Integer(result,0);
				signPasswordGesture = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("signPasswordGesture");
			}
		}).start();
	}
	// =================================================
	public int getIsOpenGesture(){
		return isOpenGesture;
	}
	public String getSignPasswordGesture(){
		return signPasswordGesture;
	}
	// =================================================
	public void saveGesture(int isOpen,String signPassword) {
		isOpenGesture = isOpen;
		signPasswordGesture = signPassword;
		ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isOpenGesture", String.valueOf(isOpenGesture));
		ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("signPasswordGesture", signPasswordGesture);
	}
}
