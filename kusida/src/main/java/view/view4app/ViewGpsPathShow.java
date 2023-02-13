package view.view4app;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.OMapInfoWindow;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.BaiduZoomLevel;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.BuildConfig;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.ArrayList;
import java.util.List;

import common.global.NAVI;
import common.global.OWXShare;
import model.ManagerCarList;
import model.ManagerGps;
import model.gps.DataGpsPath;
import model.gps.DataGpsPoint;
import view.view4me.set.ClipTitleMeSet;

public class ViewGpsPathShow extends RelativeLayoutBase {
	private ClipTitleMeSet title_head;
	private TextView		txt_carname, txt_time, txt_place;
	private MapView baidumap_view;
	private ImageView		img_gps;

	private BitmapDescriptor	markStart, markStop;
	private MarkerOptions		opStart1, opEnd1;
	private InfoWindow     infowindow;
	private BaiduMap       map;
	private DataGpsPath    data;
	private DataGpsPoint   clickedPoint;
	private ShareUrlSearch shareUrlSearch;

	public ViewGpsPathShow(Context context, AttributeSet attrs) {
		super(context, attrs);
		 if (BuildConfig.DEBUG) Log.e("ViewShow","ViewGpsPathShow");
		LayoutInflater.from(context).inflate(R.layout.view_app_gps_path_show, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		txt_carname = (TextView) findViewById(R.id.txt_carname);
		txt_time = (TextView) findViewById(R.id.txt_time);
		txt_place = (TextView) findViewById(R.id.txt_place);
		baidumap_view = (MapView) findViewById(R.id.baidumap_view);
		img_gps = (ImageView) findViewById(R.id.img_gps);
		initViews();
		initEvents();
	}
	@Override
	public void initViews() {
		shareUrlSearch = ShareUrlSearch.newInstance();
		data = ManagerGps.getInstance().currentPath;
		if (data == null)return;
		infowindow = OMapInfoWindow.initInfoWindow(getContext());
		clickedPoint = new DataGpsPoint();
		clickedPoint.location = data.startLocation;
		clickedPoint.latlng = data.latlngs[0];
		clickedPoint.isStart = true;
		clickedPoint.createTime = data.startTime;
		//

		if(map == null)map = baidumap_view.getMap();
		map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		map.setTrafficEnabled(true);
		String[] arr = ManagerGps.getInstance().currentPath.latlngs;
		if ((arr == null || arr.length == 0))
			return;
		LatLng start = DataGpsPath.getLatLngs(arr[0]);
		LatLng end = DataGpsPath.getLatLngs(arr[arr.length - 1]);


		// PlanNode stNode = PlanNode.withLocation(start);


		markStart = BitmapDescriptorFactory.fromResource(R.drawable.map_path_start);
		markStop = BitmapDescriptorFactory.fromResource(R.drawable.map_path_stop);
		opStart1 = new MarkerOptions().position(start).icon(markStart);
		opEnd1 = new MarkerOptions().position(end).icon(markStop);
		map.addOverlay(opStart1);
		map.addOverlay(opEnd1);
		LatLngBounds bounds = new LatLngBounds.Builder().include(start).include(end).build();

		List<Integer> colorList = new ArrayList<Integer>();
		colorList.add(0x77dddddd);
		colorList.add(0x770FC436);//0x770000FF
		colorList.add(0x77dddddd);


		OverlayOptions ooPolyline = new PolylineOptions().width(14).color(0x770000FF).points(ManagerGps.getInstance().currentPath.getLatLngs());
		map.addOverlay(ooPolyline);
		OverlayOptions ooPolylineUP = new PolylineOptions().width(6).color(0xFFFFDD66).dottedLine(true).points(ManagerGps.getInstance().currentPath.getLatLngs());
		map.addOverlay(ooPolylineUP);
		int dis = (int) NAVI.getDistance(start, end);
		MapStatusUpdate factory = MapStatusUpdateFactory.newLatLngZoom(bounds.getCenter(), BaiduZoomLevel.getLevelFromMeter(dis));
		map.setMapStatus(factory);
		handleChangeData();
	}
	@Override
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_gps_path_list);
			}
		});
		map.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
			@Override
			public void onTouch(MotionEvent arg0) {
				map.hideInfoWindow();
			}
		});
		map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker mark) {
				LatLng pos = mark.getPosition();
				if (mark.getIcon().equals(markStart)) {
					clickedPoint.location = data.startLocation;
					clickedPoint.latlng = data.latlngs[0];
					clickedPoint.isStart = true;
					infowindow = OMapInfoWindow.changePos(getContext(), clickedPoint.location, NAVI.Str2Latlng(clickedPoint.latlng),ViewGpsPathShow.this);
					map.showInfoWindow(infowindow);// 显示此infoWindow
					MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(pos);
					map.setMapStatus(status);
					handleChangeData();
				} else if (mark.getIcon().equals(markStop)) {
					clickedPoint.location = data.endLocation;
					clickedPoint.latlng = data.latlngs[data.latlngs.length-1];
					clickedPoint.isStart = false;
					infowindow = OMapInfoWindow.changePos(getContext(), clickedPoint.location,NAVI.Str2Latlng(clickedPoint.latlng), ViewGpsPathShow.this);
					map.showInfoWindow(infowindow);// 显示此infoWindow
					MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(pos);
					map.setMapStatus(status);
					handleChangeData();
				}
				return false;
			}
		});

	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
	}
	@Override
	public void callback(String key, Object value) {
		if(key.equals("pressShare")){
			LatLng pos = (LatLng)value;
			if(shareUrlSearch == null)shareUrlSearch = ShareUrlSearch.newInstance();
			shareUrlSearch.setOnGetShareUrlResultListener(new OnGetShareUrlResultListener() {
				@Override
				public void onGetRouteShareUrlResult(ShareUrlResult arg0) {
				}
				@Override
				public void onGetPoiDetailShareUrlResult(ShareUrlResult arg0) {
				}
				@Override
				public void onGetLocationShareUrlResult(ShareUrlResult result) {
					OWXShare.SharePlace(clickedPoint.location, result.getUrl());
				}
			});
			shareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
					.location(pos).snippet(getResources().getString(R.string.test_share_point)).name(clickedPoint.location));
		}
		super.callback(key, value);
	}

	@Override
	protected void onDetachedFromWindow() {
		baidumap_view.onDestroy();//清除地图
		map.clear();
		map = null;
		baidumap_view = null;
		super.onDetachedFromWindow();
	}

	@Override
	public void invalidateUI(){
		txt_carname.setText(ManagerCarList.getInstance().getCurrentCar().num);
		txt_time.setText(ODateTime.time2StringWithHH(clickedPoint.createTime));
		txt_place.setText(clickedPoint.location);
		if(clickedPoint.isStart){
			img_gps.setImageResource(R.drawable.map_path_start);
		}else{
			img_gps.setImageResource(R.drawable.map_path_stop);
		}
	}
	// =====================================================// 定制RouteOverly
}
