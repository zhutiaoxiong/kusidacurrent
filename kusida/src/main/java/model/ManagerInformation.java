package model;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import common.GlobalContext;
import common.SkinCheck;
import model.information.DataBanner;
import model.information.DataInformation;
import model.information.DataSkin;

public class ManagerInformation {
	public List<DataInformation>      infomationList;
	public List<DataBanner>           bannerList;
	public List<DataSkin>             skinList;
    public List<DataSkin>             skinListBg;
	public List<DataSkin>             skinBoughtList;
    public List<DataSkin>             skinBoughtListBg;
	public List<DataSkin>             skinUsedList;
    public List<DataSkin>             skinUsedListBg;
	public static String              jumpUrl;
	// ========================out======================
	private static ManagerInformation _instance;
	private ManagerInformation() {
		init();
	}
	public static ManagerInformation getInstance() {
		if (_instance == null)
			_instance = new ManagerInformation();
		return _instance;
	}
	private void init() {
		infomationList = new ArrayList<DataInformation>();
	}
	// ==========================get=================================
	public List<DataSkin>  getSkinUsedList(){
		if(skinUsedList == null)loadSkinUsedList();
		List<DataSkin> result = new ArrayList<DataSkin>();
		if(skinUsedList!=null&&skinUsedList.size()>0){
			for(int i=0;i<skinUsedList.size();i++){
				DataSkin data = skinUsedList.get(i);
				//查询以前是否下载过
				String resulttt = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("IsDownLoadZip"+data.ide);
				boolean IsDownLoadZip = ODBHelper.queryResult2boolean(resulttt);
				if(!data.isType && IsDownLoadZip){
					result.add(data);
				}
			}
		}
		return result;
	}
	public List<DataSkin>  getSkinBoughtList(){
		if(skinBoughtList == null)skinBoughtList = new ArrayList<DataSkin>();
		return skinBoughtList;
	}
	private void loadSkinUsedList(){
		//用户信息确定不要异步
		String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("skinUsed");
		JsonObject obj = ODBHelper.convertJsonObject(result);
		skinUsedList 	= new ArrayList<DataSkin>();
		if (obj != null) {
			JsonArray arr = OJsonGet.getJsonArray(obj, "skinUsed");
			skinUsedList = DataSkin.fromJsonArrayNoTitle(arr);
		}
	}
	private void loadSkinUsedListBg(){
		//用户信息确定不要异步
		String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("skinUsedBg");
		JsonObject obj = ODBHelper.convertJsonObject(result);
		skinUsedListBg 	= new ArrayList<DataSkin>();
		if (obj != null) {
			JsonArray arr = OJsonGet.getJsonArray(obj, "skinUsedBg");
			skinUsedListBg = DataSkin.fromJsonArrayNoTitle(arr);
		}
	}
	// ==========================set=================================
	public void saveInfoList(JsonArray informInfos) {
		if (informInfos == null){
			infomationList = new ArrayList<DataInformation>();
			return;
		}
		this.infomationList = DataInformation.fromJsonArray(informInfos);
	}
	public void saveBannerList(JsonArray bannerInfos) {
		if (bannerInfos == null){
			bannerList = new ArrayList<DataBanner>();
			return;
		}
		this.bannerList = DataBanner.fromJsonArray(bannerInfos);
	}
	public void saveSkinBoughtList(JsonArray carTypeInfos) {
		if (carTypeInfos == null){
			skinBoughtList = new ArrayList<DataSkin>();
			return;
		}
		this.skinBoughtList = DataSkin.fromJsonArrayNoTitle(carTypeInfos);
	}

	public void saveSkinList(JsonArray carTypeInfos) {
		if (carTypeInfos == null){
			skinList = new ArrayList<DataSkin>();
			return;
		}
		this.skinList = DataSkin.fromJsonArray(carTypeInfos);
		if(skinUsedList == null)loadSkinUsedList();
		List<DataSkin> cacheSkinUsedList=skinUsedList;
		if(cacheSkinUsedList==null){
			cacheSkinUsedList=new ArrayList<>();
		}
		if(skinUsedList!=null&&skinUsedList.size()>0){
			for(DataSkin skin : skinList){
				boolean haveItem = false;
				for(DataSkin skinU : skinUsedList){
					if (skinU.ide == skin.ide) {
						haveItem = true;
						break;
					}
				}
				if(!haveItem && !skin.isType){
//					skinUsedList.add(skin);
					cacheSkinUsedList.add(skin);
				}
			}
		}
		//排序
		MyComparator myComparator = new MyComparator();
//		Collections.sort(skinUsedList, myComparator);
		Collections.sort(cacheSkinUsedList, myComparator);
		JsonObject objj = new JsonObject();
//		objj.add("skinUsed", DataSkin.toJsonArray(skinUsedList));
		objj.add("skinUsed", DataSkin.toJsonArray(cacheSkinUsedList));
		ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("skinUsed", ODBHelper.convertString(objj));
	}

	private class MyComparator implements Comparator<DataSkin> {
		public int compare(DataSkin o1, DataSkin o2) {
			if (o1!=null&&o2!=null){
				if (o1.ide <o2.ide) {//小于
					return -1;
				} else {
					return 1;
				}
			}
			return Integer.valueOf(o1.ide).compareTo(o2.ide);
		}
	}
}
