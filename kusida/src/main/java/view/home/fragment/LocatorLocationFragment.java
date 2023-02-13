package view.home.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.client.proj.kusida.R;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import common.GlobalContext;
import view.home.activity.ActivityChangeEquipment;
import view.home.activity.ActivityMessage;
import view.home.activity.ActivityMessageSoundRecording;
import view.home.commonview.BaiDuInfoWindow;
import view.home.commonview.CommonTitleFourItem;
import view.home.util.ImageUtil;


public class LocatorLocationFragment extends Fragment {
    private CommonTitleFourItem top_item;
    private ImageView map_top_right;
    private ImageView map_bottom_bottom;
    private ImageView map_bottom_center;
    private ImageView map_bottom_top;
    private ImageView map_bottom_right_add;
    private ImageView map_bottom_delete;
    private MapView map_view;
    private ImageView flush;
    private static final String TAG="LocatorLocationFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locator_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        top_item=view.findViewById(R.id.top_item);
        map_top_right=view.findViewById(R.id.map_top_right);
        map_bottom_bottom=view.findViewById(R.id.map_bottom_bottom);
        map_bottom_center=view.findViewById(R.id.map_bottom_center);
        map_bottom_top=view.findViewById(R.id.map_bottom_top);
        map_bottom_right_add=view.findViewById(R.id.map_bottom_right_add);
        map_bottom_delete=view.findViewById(R.id.map_bottom_delete);
        map_view=view.findViewById(R.id.map_view);
        map_view.showZoomControls(false);
        flush=view.findViewById(R.id.flush);
        initView();
        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        map_view.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map_view.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map_view.onDestroy();
    }

    private void initView(){
        map_view.getMap().setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            /**
             * 地图加载完成，才能进行overlay添加
             */
            @Override
            public void onMapLoaded() {
                placeMarket();
                placceWindow();
            }
        });
        map_view.getMap().setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                Logger.d("clear");
//                map_view.getMap().clear();
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                Logger.d("show");
//                placeMarket();
//                placceWindow();
            }
        });
    }
    private void placceWindow(){
        LatLng llText = new LatLng(39.86923, 116.397428);
        BaiDuInfoWindow baiDuInfoWindow=new BaiDuInfoWindow(Objects.requireNonNull(getContext()),null);
        InfoWindow mInfoWindow = new InfoWindow(baiDuInfoWindow, llText, -100);
//使InfoWindow生效
        map_view.getMap().showInfoWindow(mInfoWindow);

//        //用来构造InfoWindow的Button
//        Button button = new Button(getContext());
//        button.setBackgroundResource(R.drawable.a1);
//        button.setText("InfoWindow");
//
////构造InfoWindow
////point 描述的位置点
////-100 InfoWindow相对于point在y轴的偏移量
//        InfoWindow mInfoWindow = new InfoWindow(button, llText, -100);
////使InfoWindow生效
//        map_view.getMap().showInfoWindow(mInfoWindow);
    }
    private void placeMarket(){
        //定义Maker坐标点
        LatLng point =  new LatLng(39.86923, 116.397428);
//构建Marker图标
        Bitmap bitmapp = ImageUtil.zoomImg(BitmapFactory.decodeResource(getResources(), R.drawable.icon_dingwei_type_3), 52, 70);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(bitmapp);
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.map_info_loc_icon);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);//.anchor(0.5f, 0.5f)
//在地图上添加Marker，并显示
        map_view.getMap().addOverlay(option);
    }
    public static void getCurrentLocationNext(){
        LocationClient.setAgreePrivacy(true);
        try {
            LocationClient    mLocClient = new LocationClient(GlobalContext.getCurrentActivity());
            mLocClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation loc) {
                    Logger.d("那定位");
//                    selfAddress = loc.getAddress();
//                    selfPos = new LatLng(loc.getLatitude(),loc.getLongitude());
                    mLocClient.stop();
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
    private void initEvent(){
        top_item.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick top_item.left" );
                Intent intent=new Intent(getActivity(), ActivityChangeEquipment.class);
                startActivity(intent);
            }
        });
        top_item.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick top_item.right" );
                Intent intent=new Intent(getActivity(), ActivityMessage.class);
                startActivity(intent);
            }
        });
        top_item.right_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick top_item.right_more" );
                Intent intent=new Intent(getActivity(), ActivityMessageSoundRecording.class);
                startActivity(intent);
            }
        });
        map_top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_top_right" );
            }
        });

        map_bottom_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_bottom_bottom" );
            }
        });
        map_bottom_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_bottom_center" );
            }
        });
        map_bottom_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_bottom_top" );
            }
        });
        map_bottom_right_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_bottom_right_add" );
                map_view.getMap().animateMapStatus(MapStatusUpdateFactory.zoomIn());
            }
        });
        map_bottom_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_bottom_delete" );
                map_view.getMap().animateMapStatus(MapStatusUpdateFactory.zoomOut());
            }
        });
        map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_view" );
            }
        });
        flush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick flush" );
            }
        });
    }
}