package common.global;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;

public class NAVI {
	public static LatLng gps2baidu(LatLng gps) {
		CoordinateConverter converter = new CoordinateConverter();
		converter.from(CoordType.GPS);
		// sourceLatLng待转换坐标
		converter.coord(gps);
		LatLng desLatLng = converter.convert();
		return desLatLng;
	}
	public static LatLng Str2Latlng(String latLng) {
		if (latLng == null || "".equals(latLng)||"null".equals(latLng))
			return null;
		String[] st = latLng.split(",");
		LatLng latPos = new LatLng(Double.valueOf(st[0]), Double.valueOf(st[1]));
		return latPos;
	}
	public static String Latlng2Str(LatLng pos){
		if(pos == null)return null;
		String result = String.valueOf(pos.latitude);
		result += ",";
		result += String.valueOf(pos.longitude);
		return result;
	}
	/**
	 * 计算两点之间距离
	 * @return 米
	 */
	private final static double	PI	= Math.PI;	// 圆周率
	private final static double	R	= 6371.229; // 地球的半径
	public static double getDistance(LatLng lat_a, LatLng lat_b) {
		if(lat_a == null || lat_b == null)return 0;
		double x, y, distance;
		x = (lat_b.longitude - lat_a.longitude) * PI * R * Math.cos(((lat_a.latitude + lat_b.latitude) / 2) * PI / 180) / 180;
		y = (lat_b.latitude - lat_a.latitude) * PI * R / 180;
		distance = Math.hypot(x, y);//km
		return distance*1000;
	}
	public static String toBDMAP(LatLng bd09start,LatLng bd09end,String address) {
		// intent://product/[service/]action[?parameters]#Intent;scheme=bdapp;package=package;end
		StringBuilder loc = new StringBuilder();
		loc.append("intent://map/direction?");
		loc.append("origin=latlng:" + bd09start.latitude + "," + bd09start.longitude);// +"|name:我家"
		loc.append("&destination=latlng:" + bd09end.latitude + "," + bd09end.longitude);// +"|name:目的位置"
		loc.append("&mode=driving");
		loc.append("&region=西安");
		loc.append("&src=kulala_android");// yourCompanyName|yourAppName
		loc.append("#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
		return loc.toString();
	}
	public static String toAMAP(LatLng gcj02end,String address) {
		String type = "navi";// 导航
		String sourceApplication = "amap";// 第三方调用应用名称
		String poiname = address;
//		String lat = gcj02start.latitude;// 纬度
//		String lon = gcj02start.longitude;// 经度
		String dev = "0";// 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
		String style = "5";// 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5
							// 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
		// "androidamap://viewMap?sourceApplication=厦门通&poiname=百度奎科大厦&lat=40.047669&lon=116.313082&dev=0";
		return "androidamap://" + type + "?sourceApplication=" + sourceApplication + "&poiname=" + poiname + "&lat=" + gcj02end.latitude + "&lon="
				+ gcj02end.longitude + "&dev=" + dev + "&style=" + style;
	}
	public static LatLng BD09_to_gcj02(LatLng bd09) {
		double x = bd09.longitude - 0.0065, y = bd09.latitude - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new LatLng(gg_lat, gg_lon);
	}
	public static String getBaiduNaviStr(LatLng end, String location){
		return "intent://map/direction?destination=latlng:"+end.latitude+","+end.longitude+"|name:"+location+"&mode=driving&region=广州&src=kulala|com.client.proj.kulala#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
	}
	// public static Gps gcj02_To_Bd09(double gg_lat, double gg_lon) {
	// double x = gg_lon, y = gg_lat;
	// double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
	// double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
	// double bd_lon = z * Math.cos(theta) + 0.0065;
	// double bd_lat = z * Math.sin(theta) + 0.006;
	// return new Gps(bd_lat, bd_lon);
	// }
	// public static Gps gps84_To_Gcj02(double lat, double lon) {
	// if (outOfChina(lat, lon)) {
	// return null;
	// }
	// double dLat = transformLat(lon - 105.0, lat - 35.0);
	// double dLon = transformLon(lon - 105.0, lat - 35.0);
	// double radLat = lat / 180.0 * pi;
	// double magic = Math.sin(radLat);
	// magic = 1 - ee * magic * magic;
	// double sqrtMagic = Math.sqrt(magic);
	// dLat = (dLat * 180.0) / ((getProcessName * (1 - ee)) / (magic * sqrtMagic) * pi);
	// dLon = (dLon * 180.0) / (getProcessName / sqrtMagic * Math.cos(radLat) * pi);
	// double mgLat = lat + dLat;
	// double mgLon = lon + dLon;
	// return new Gps(mgLat, mgLon);
	// }
	//
	// /**
	// * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
	// * */
	// public static Gps gcj_To_Gps84(double lat, double lon) {
	// Gps gps = transform(lat, lon);
	// double lontitude = lon * 2 - gps.getWgLon();
	// double latitude = lat * 2 - gps.getWgLat();
	// return new Gps(latitude, lontitude);
	// }
}
