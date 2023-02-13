package view.home.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.client.proj.kusida.R;

import java.util.ArrayList;
import java.util.List;

import view.home.commonview.CommonTitletem;
import view.home.commonview.LocTrackListDetailBottom;
import view.home.util.ImageUtil;

public class ActivityTracklistDetail extends AllActivity {
    private CommonTitletem top_item;
    private MapView map_view;
    private LocTrackListDetailBottom bottom;
    private ImageView jiantou;
    private boolean isShowBottom=true;
    private boolean isPlay;
    private static final String TAG="ActivityMessageDetail";


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list_detail);
        top_item=findViewById(R.id.titleme);
        map_view=findViewById(R.id.map_view);
        bottom=findViewById(R.id.bottom);
        jiantou=findViewById(R.id.jiantou);
        map_view.showZoomControls(false);
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
        top_item.title.setText("轨迹详情");
        placeMarketLine();
    }
    private void placeMarketLine(){
        LatLng p1 = new LatLng(39.97923, 116.357428);
        LatLng p2 = new LatLng(39.94923, 116.397428);
        LatLng p3 = new LatLng(39.97923, 116.437428);
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        Bitmap startBitmap = ImageUtil.zoomImg(BitmapFactory.decodeResource(getResources(), R.drawable.loc_start_marker_img), 59, 79);
        BitmapDescriptor bitmapStart = BitmapDescriptorFactory.fromBitmap(startBitmap);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions optionStart = new MarkerOptions()
                .position(p1)
                .icon(bitmapStart);
        Bitmap endBitmap = ImageUtil.zoomImg(BitmapFactory.decodeResource(getResources(), R.drawable.loc_end_marker_img), 59, 79);
        BitmapDescriptor bitmapEnd = BitmapDescriptorFactory.fromBitmap(endBitmap);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions optionEnd = new MarkerOptions()
                .position(p2)
                .icon(bitmapEnd);
//在地图上添加Marker，并显示
        map_view.getMap().addOverlay(optionStart);
        map_view.getMap().addOverlay(optionEnd);
        //设置折线的属性
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(11)
                .color(Color.parseColor("#FF00FF"))
                .points(points);
//在地图上绘制折线
//mPloyline 折线对象
        Overlay mPolyline =  map_view.getMap().addOverlay(mOverlayOptions);
    }

    private void initEvent() {
        top_item.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick map_view" );
            }
        });
        jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowBottom=!isShowBottom;
                if(isShowBottom){
                    jiantou.setImageResource(R.drawable.track_arrow_bottom);
                    bottom.setVisibility(View.VISIBLE);
                }else{
                    jiantou.setImageResource(R.drawable.track_arrow_top);
                    bottom.setVisibility(View.INVISIBLE);
                }
            }
        });
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowBottom=!isShowBottom;
                if(isShowBottom){
                    jiantou.setImageResource(R.drawable.track_arrow_bottom);
                    bottom.setVisibility(View.VISIBLE);
                }else{
                    jiantou.setImageResource(R.drawable.track_arrow_top);
                    bottom.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
