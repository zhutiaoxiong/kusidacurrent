package com.baidu.mapapi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;

import common.GlobalContext;
import common.global.OToastNavigate;
import ctrl.OCtrlGps;

public class OMapInfoWindow {
	public static TextView		txt_favorite, txt_navorite, txt_share;
	private static RelativeLayout lin;
	public static  LatLng         selfPos;
	public static  Address        selfAddress;
	private static LocationClient mLocClient;
//	NaviParaOption para = new NaviParaOption().startPoint(start).endPoint(end).startName("当前位置").endName(location);
//	BaiduMapNavigation.openBaiduMapNavi(para, GlobalContext.getContext());
//Intent intent = Intent.getIntent("intent://map/direction?destination=latlng:"+end.latitude+","+end.longitude+"|name:"+location+"&mode=driving&region=广州&src=kulala|com.client.proj.kulala#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
	public static InfoWindow initInfoWindow(final Context context) {
		if (lin == null) {
			lin = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.clip_infowindow, null);
			txt_favorite = (TextView) lin.findViewById(R.id.txt_favorite);
			txt_share = (TextView) lin.findViewById(R.id.txt_share);
			txt_navorite = (TextView) lin.findViewById(R.id.txt_navoritee);
		}
		getCurrentLocation();
		return new InfoWindow(lin, new LatLng(0, 0), -90);
	}
	public static InfoWindow changePos(final Context context,final String location,final LatLng pos,final OCallBack callback){
		txt_favorite.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				OCtrlGps.getInstance().ccmd1216_favoritePos(pos.latitude+","+pos.longitude, location);
//				OToastOMG.getInstance().showConfirm(context, "添加收藏", "是否把:"+location+" 添加到收藏列表？", "favorite", callback);
			}
		});
		txt_share.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				callback.callback("pressShare", pos);
			}
		});
		txt_navorite.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				OToastNavigate.getInstance().showOpenNavigate(txt_navorite,selfPos, pos, location);
			}
		});
		return new InfoWindow(lin, pos, -90);
	}
	public static void getCurrentLocation(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			int permissionNet = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);//网络定位
			//拍照权限
			if (permissionNet != PackageManager.PERMISSION_GRANTED) {
				GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
			}else {
				getCurrentLocationNext();
			}
		}else{
			getCurrentLocationNext();
		}
	}
	public static void getCurrentLocationNext(){
		LocationClient.setAgreePrivacy(true);
		try {
			mLocClient = new LocationClient(GlobalContext.getCurrentActivity());
			mLocClient.registerLocationListener(new BDAbstractLocationListener() {
				@Override
				public void onReceiveLocation(BDLocation loc) {
					selfAddress = loc.getAddress();
					selfPos = new LatLng(loc.getLatitude(),loc.getLongitude());
					mLocClient.stop();
					ODispatcher.dispatchEvent(OEventName.GPS_FIND_SELFPOS_OK);
				}
				@Override
				public void onConnectHotSpotMessage(String arg0, int arg1) {
				}
			});//注册定位监听接口
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true); //打开GPRS
			option.setIsNeedAddress(true);
			option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
			option.setScanSpan(5000); //设置发起定位请求的间隔时间为5000ms
			option.disableCache(false);//禁止启用缓存定位
			mLocClient.setLocOption(option);  //设置定位参数
			mLocClient.start();  // 调用此方法开始定位
		} catch (Exception e) {

		}


	}
	public static void ExitForMainActivityExit(){
		if(mLocClient!=null)mLocClient.stop();
	}
}
