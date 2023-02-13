package view.view4app;

import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.client.proj.kusida.R;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;

import common.map.DataCar_Area_Pos;
import common.map.DataPos;
import common.map.FullScreenMap;
import common.map.MapPosGet;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import view.view4me.set.ClipTitleMeSet;

public class ViewGpsFavoriteShowPos extends ActivityBase {
    private ClipTitleMeSet title_head;
    private FullScreenMap baidumap_view;

    public static LatLng           carPos;
    private       DataCar_Area_Pos carPosInfo;
    private       DataCarInfo      currentCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_app_gps_favorite_show_pos);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        baidumap_view = (FullScreenMap) findViewById(R.id.baidumap_view);
        initViews();
        initEvents();
        handleChangeData();
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                finish();
            }
        });
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }
    @Override
    public void invalidateUI() {
        if (carPosInfo == null) carPosInfo = new DataCar_Area_Pos();
        currentCar = ManagerCarList.getInstance().getCurrentCar();
        carPosInfo.carPos = carPos;
        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
        carPosInfo.direction = carStatus.direction;
        MapPosGet.searchAddressByPos(carPos, new MapPosGet.OnAddressGetListener() {
            @Override
            public void onAddressGet(DataPos posData) {
                carPosInfo.address = posData.address;
            }
        });
        baidumap_view.placeCar(carPosInfo.carPos, carPosInfo.direction, carPosInfo.address);
        // 初始化数据
//        if (baidumap_view == null) return;
//        if (map == null) map = baidumap_view.getMap();
//        map.clear();
//        map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//        map.setTrafficEnabled(true);
//
//        if (carPos == null) return;//没有坐标
//
//        MarkerOptions ooB = new MarkerOptions().position(carPos).icon(markCar).anchor(0.5f, 0.5f);//
//        map.addOverlay(ooB);
//        MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(carPos, 17);
//        map.setMapStatus(status);
    }
    @Override
    protected void popView(int resId) {

    }

}
