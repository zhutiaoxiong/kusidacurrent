package view.home.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.client.proj.kusida.R;

import java.util.Objects;

import view.home.commonview.BaiDuMessageDetailInfoWindow;
import view.home.commonview.CommonTitletem;
import view.home.util.ImageUtil;

public class ActivityMessageDetail extends AllActivity {
    private CommonTitletem top_item;
    private ImageView map_top_right;
    private ImageView map_bottom_bottom;
    private ImageView map_bottom_center;
    private ImageView map_bottom_right_add;
    private ImageView map_bottom_delete;
    private MapView map_view;
    private static final String TAG="ActivityMessageDetail";


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        top_item=findViewById(R.id.titleme);
        map_top_right=findViewById(R.id.map_top_right);
        map_bottom_bottom=findViewById(R.id.map_bottom_bottom);
        map_bottom_center=findViewById(R.id.map_bottom_center);
        map_bottom_right_add=findViewById(R.id.map_bottom_right_add);
        map_bottom_delete=findViewById(R.id.map_bottom_delete);
        map_view=findViewById(R.id.map_view);
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

    private void initView() {
        top_item.title.setText("传过来的数值");
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
    }
    private void placceWindow(){
        LatLng llText = new LatLng(39.86923, 116.397428);
        BaiDuMessageDetailInfoWindow baiDuInfoWindow=new BaiDuMessageDetailInfoWindow(Objects.requireNonNull(ActivityMessageDetail.this),null);
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


    private void initEvent() {
        top_item.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
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
        map_bottom_right_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_bottom_right_add" );
            }
        });
        map_bottom_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_bottom_delete" );
            }
        });
        map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_view" );
            }
        });
    }
}
