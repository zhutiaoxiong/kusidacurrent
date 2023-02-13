package model.gps;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

import common.global.NAVI;

public class DataGpsPath {
	public long ide;//轨迹id
	public long		startTime;		// 开始时间
	public String	startLocation;	// 开始位置
	public long		endTime;		// 结束时间
	public String	endLocation;	// 结束位置开始位置
	public String[]	latlngs;		// 经纬度列表 经纬度用逗号隔开
	public double	miles;			// 长度 单位km
	public  String comment  ;//备注信息
	public String  intervalTime  ;//间隔时长
	public int isCollect ; //是否收藏 0：未收藏 1：已收藏
	public long createTime  ;//创建时间
	public void fromJsonObject(JsonObject obj) {
		Gson gson = new Gson();
		DataGpsPath thisobj = gson.fromJson(obj, DataGpsPath.class);
		Log.i("msg", "Gson thisobj"+thisobj.startTime +thisobj.startLocation+" latlngs: "+thisobj.latlngs.length);
		this.ide = thisobj.ide;
		this.startTime = thisobj.startTime;
		this.startLocation = thisobj.startLocation;
		this.endTime = thisobj.endTime;
		this.endLocation = thisobj.endLocation;
		this.latlngs = thisobj.latlngs;
		this.miles = thisobj.miles;
		this.comment = thisobj.comment;
		this.intervalTime = thisobj.intervalTime;
		this.isCollect = thisobj.isCollect;
		this.createTime = thisobj.createTime;
		for(int i=0;i<this.latlngs.length;i++){
			String pos = latlngs[i];
			LatLng toBaidu = NAVI.gps2baidu(NAVI.Str2Latlng(pos));
			latlngs[i] = toBaidu.latitude+","+toBaidu.longitude;
		}
	}
	public static List<DataGpsPath> fromJsonArray(JsonArray arr) {
		Log.i("msg", "Gson");
		List<DataGpsPath> list = new ArrayList<DataGpsPath>();
		for(int i=0;i<arr.size();i++){
			JsonObject object = arr.get(i).getAsJsonObject();
			Log.i("msg", "Gson object"+object.toString());
			DataGpsPath data = new DataGpsPath();
			data.fromJsonObject(object);
			list.add(data);
		}
		Log.i("msg", "Gson end");
		return list;
	}
	public static LatLng getLatLngs(String location) {
		String[] st = location.split(",");
		LatLng start = new LatLng(Double.valueOf(st[0]), Double.valueOf(st[1]));
		return start;
	}
	public List<LatLng> getLatLngs() {
		List<LatLng> lat = new ArrayList<LatLng>();
		for (int i = 0; i < latlngs.length; i++) {
			String[] st = latlngs[i].split(",");
			LatLng start = new LatLng(Double.valueOf(st[0]), Double.valueOf(st[1]));
			lat.add(start);
		}
		return lat;
	}
	public static List<LatLng> getLatLngs(String[] latlngs) {
		List<LatLng> lat = new ArrayList<LatLng>();
		for (int i = 0; i < latlngs.length; i++) {
			String[] st = latlngs[i].split(",");
			LatLng start = new LatLng(Double.valueOf(st[0]), Double.valueOf(st[1]));
			lat.add(start);
		}
		return lat;
	}
}
