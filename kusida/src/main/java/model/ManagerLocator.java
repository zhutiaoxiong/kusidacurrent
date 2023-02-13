package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import model.mylocator.PositionerBean;
import model.mylocator.PositionerInfoBean;
import model.mylocator.PositionerRecordingBean;
import model.mylocator.PositionerSetBean;
import model.mylocator.PositionerTrailBean;
import model.mylocator.PositionerWarnRecordBean;
import model.score.DataScore;

public class ManagerLocator {
	public         List<PositionerBean> positorList;//定位器列表对象
	public         PositionerBean cachePositorBean;//查询的定位器对象
	public         PositionerInfoBean cachePositorInfoBean;//查询的定位器信息对象
	public         PositionerSetBean cachePositorSetBean;//查询的定位器设置对象
	public         List<PositionerTrailBean> positorTrailList;//定位器轨迹列表
	public         List<PositionerRecordingBean> positorRecordingList;//定位器录音列表
	public         List<PositionerWarnRecordBean> positionerWarnRecordList;//定位器预警消息列表
	// ========================out======================
	private static ManagerLocator _instance;
	private ManagerLocator() {
		init();
	}
	public static ManagerLocator getInstance() {
		if (_instance == null)
			_instance = new ManagerLocator();
		return _instance;
	}
	private void init() {
	}
	public void saveLocatorListInfo(JsonArray positorlist) {
		if (positorlist == null) return;
		this.positorList = PositionerBean.fromJsonArray(positorlist);
	}
	public void saveCacheLocatorBean(JsonObject cacheLocatorBean) {
		if (cacheLocatorBean == null) return;
		this.cachePositorBean = PositionerBean.fromJsonObject(cacheLocatorBean);
	}
	public void savecachePositorInfoBean(JsonObject cachePositorInfoBean) {
		if (cachePositorInfoBean == null) return;
		this.cachePositorInfoBean = PositionerInfoBean.fromJsonObject(cachePositorInfoBean);
	}
	public void savecachePositorSetBean(JsonObject cachePositorSetBean) {
		if (cachePositorSetBean == null) return;
		this.cachePositorSetBean = PositionerSetBean.fromJsonObject(cachePositorSetBean);
	}
	public void savePositorTrailListInfo(JsonArray positorTrailList) {
		if (positorTrailList == null) return;
		this.positorTrailList = PositionerTrailBean.fromJsonArray(positorTrailList);
	}
	public void savePositorRecordingListInfo(JsonArray positorRecordingList) {
		if (positorRecordingList == null) return;
		this.positorRecordingList = PositionerRecordingBean.fromJsonArray(positorRecordingList);
	}
	public void savePositionerWarnRecordListInfo(JsonArray positionerWarnRecordList) {
		if (positionerWarnRecordList == null) return;
		this.positionerWarnRecordList = PositionerWarnRecordBean.fromJsonArray(positionerWarnRecordList);
	}

}
