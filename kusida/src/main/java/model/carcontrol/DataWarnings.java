package model.carcontrol;

import com.client.proj.kusida.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;

public class DataWarnings {

	public long		carId;
	public int		alertType;	// 预警类型,1：消息，2：警报，3：安全
	public String	content;	// 消息内容
	public long		createTime; // 消息时间
	public int		alertId;//1-31
	
	public boolean isNew = false;

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DataWarnings))return false;
		DataWarnings cache = (DataWarnings)obj;
		if(cache.carId != carId)return false;
		if(cache.alertType != alertType)return false;
		if(cache.createTime != createTime)return false;
		if(cache.alertId != alertId)return false;
		if(cache.isNew != isNew)return false;
		if(cache.content.equals(carId))return false;
		return true;
	}

	/**
1	车辆启动提醒
2	尾箱开启提醒
3	车辆设防
4	车辆撤防
5	车辆熄火
6	设防失败，左前门未关
7	设防失败，右前门未关
8	设防失败，左后门未关
9	设防失败，右后门未关
10	设防失败，小灯未关
11	设防失败，尾箱未关
12	设防失败，小灯未关
13	设防失败，尾箱未关
14	车辆启动失败
15	车辆熄火失败
16	进入区域提醒
17	离开区域提醒
18	超速提醒
19	低电压提醒
20	车灯未关提醒
21	未锁车提醒
22	左前窗未关提醒
23	右前窗未关提醒
24	左后窗未关提醒
25	右后窗未关提醒
26	剪线提醒
27	震动报警
28	车辆被盗，车门开启
29	车辆被盗，尾箱开启
30	车辆未预约启动
31	车辆已离线
	 */
	public String getTitle() {
		if (alertType == 1)
			return GlobalContext.getContext().getResources().getString(R.string.message);
		if (alertType == 2)
			return GlobalContext.getContext().getResources().getString(R.string.the_alarm);
		if (alertType == 3)
			return GlobalContext.getContext().getResources().getString(R.string.security);
		return "";
	}
	public int getResId() {
		if (alertType == 1)
			return R.drawable.img_record_info;
		if (alertType == 2)
			return R.drawable.icon_alarm_warning;
		if (alertType == 3)
			return R.drawable.icon_alarm_warning;
		return 0;
	}
	public static JsonArray toJsonArray(List<DataWarnings> states) {
//		Gson gson = new Gson();
		 JsonArray arr = new JsonArray();
		 for(int i=0;i<states.size();i++){
		 arr.add(states.get(i).toJsonObject());
		 }
//		String json = gson.toJson(states);
//		JsonArray arr = gson.fromJson(json, JsonArray.class);
		return arr;
	}
	public JsonObject toJsonObject() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		JsonObject obj = gson.fromJson(json, JsonObject.class);
		return obj;
	}
	public static List<DataWarnings> fromJsonArray(JsonArray states) {
		if (states == null || states.size() == 0)
			return null;
		List<DataWarnings> data = new ArrayList<DataWarnings>();
		for (int i = 0; i < states.size(); i++) {
			JsonObject object = states.get(i).getAsJsonObject();
			data.add(fromJsonObject(object));
		}
		return data;
	}
	public static DataWarnings fromJsonObject(JsonObject obj) {
		Gson gson = new Gson();
		DataWarnings thisobj = gson.fromJson(obj, DataWarnings.class);
		return thisobj;
	}
}
