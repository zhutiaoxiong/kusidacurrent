package view.view4app.carpath;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.PoiInfo;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
/**
 * Created by Administrator on 2017/3/31.
 * 地图poi管理
 */
public class OPoiOverlay extends OverlayManager {
    private List<PoiInfo> allPoi = null;
    public OPoiOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }
    public void setData(List<PoiInfo> allPoi) {
        this.allPoi = allPoi;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getExtraInfo() != null) {
            int     index = marker.getExtraInfo().getInt("index");
            PoiInfo poi   = allPoi.get(index);
            // 返回数据
            ODispatcher.dispatchEvent(OEventName.MAP_POI_CLICKED,index);
//            OPoiSerch.getInstance().searchDetail(poi.);
            return true;
        }
        return false;
    }

    @Override
    public List<OverlayOptions> getOverlayOptions() {
        if (allPoi == null) return null;
        ArrayList<OverlayOptions> arrayList = new ArrayList<OverlayOptions>();
        for (int i = 0; i < allPoi.size(); i++) {
            if (allPoi.get(i).location == null)continue;
            // 给marker加上标签  
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            arrayList.add(new MarkerOptions()
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(setNumToIcon(i))).extraInfo(bundle)
                    .position(allPoi.get(i).location));
        }
        return arrayList;
    }

    /**
     * 往图片添加数字 
     */
    private Bitmap setNumToIcon(int num) {
        BitmapDrawable bd = (BitmapDrawable) GlobalContext.getContext().getResources().getDrawable(R.drawable.icon_gcoding);
        Bitmap bitmap = bd.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        int widthX;
        int heightY = 0;
        if (num < 10) {
            paint.setTextSize(30);
            widthX = 8;
            heightY = 6;
        } else {
            paint.setTextSize(20);
            widthX = 11;
        }

        canvas.drawText(String.valueOf(num),
                ((bitmap.getWidth() / 2) - widthX),
                ((bitmap.getHeight() / 2) + heightY), paint);
        return bitmap;
    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        return false;
    }
}