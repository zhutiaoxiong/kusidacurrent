package view.view4app;

import android.os.Bundle;
import android.view.View;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.OToastButton;
import com.kulala.staticsview.toast.OToastInput;

import common.map.DataCar_Area_Pos;
import common.map.DataPos;
import common.map.FullScreenMap;
import common.map.MapPosGet;
import ctrl.OCtrlGps;
import model.ManagerCarList;
import model.ManagerGps;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import model.demomode.DemoMode;
import view.view4app.carpath.ViewBelowMapItem;
import view.view4me.set.ClipTitleMeSet;

/**
 * 1.电子围栏初进判断是否有车，并隐藏车信息 2.得到车信息后更新信息 3.更新电子围栏信息 4.改变后重设信息
 *
 * @author Administrator
 */
public class ViewGpsCarArea extends ActivityBase {
    private ClipTitleMeSet title_head;
    private ViewBelowMapItem view_address;
    private FullScreenMap    map_full;

    private DataCarInfo      currentCar;
    private DataCar_Area_Pos carPosInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_app_gps_car_area);
        title_head =findViewById(R.id.title_head);
        map_full = (FullScreenMap) findViewById(R.id.map_full);
        view_address = (ViewBelowMapItem) findViewById(R.id.view_address);

        view_address.setNeedConfirm(false);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_CARPOS_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.GPS_SETAREA_RESULTBACK, this);
    }
    @Override
    public void initViews() {
        currentCar = ManagerCarList.getInstance().getCurrentCar();
        if (currentCar == null) {
            view_address.setData("", "");
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.did_not_choose_the_vehicle));
            return;
        } else if (currentCar.ide == 0) {
            view_address.setData("", "");
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.did_not_choose_the_vehicle));
            return;
        }
        // loadInfo
        view_address.setData(currentCar.num, "");
        boolean isDemoMode = DemoMode.getIsDemoMode();
        if (isDemoMode) {//演示开始
            OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 1);
        } else {
            OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 0);
        }
    }

    @Override
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                finish();
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        title_head.img_right.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastButton.getInstance().show(title_head, new String[]{getResources().getString(R.string.cancel_the_fence), getResources().getString(R.string.set_fence)}, "operate", ViewGpsCarArea.this);
            }
        });
    }

    @Override
    protected void invalidateUI() {
        DataPos selfPos = MapPosGet.getPrePos();
        String  dis     = MapPosGet.getLatLngDistance(selfPos.pos, carPosInfo.carPos);
        view_address.setData(currentCar.num, "距您 " + dis + "  " + carPosInfo.address);
        map_full.clearOverlay();
        map_full.placeCar(carPosInfo.carPos, carPosInfo.direction, carPosInfo.address);
        map_full.placeArea(carPosInfo.areaPos, carPosInfo.areaMeter, carPosInfo.areaOpen);
    }
    @Override
    protected void popView(int resId) {

    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.GPS_CARPOS_RESULTBACK)) {
            boolean isgetPos = (Boolean) paramObj;
            if(!isgetPos)return;
            //重置自己位置
            // 定位数据返回
            DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
            if (carPosInfo == null) carPosInfo = new DataCar_Area_Pos();
            currentCar = ManagerCarList.getInstance().getCurrentCar();
            carPosInfo.carPos = carStatus.getGps();
            carPosInfo.direction = carStatus.direction;
            carPosInfo.areaMeter = ManagerGps.getInstance().areaMeter;
            carPosInfo.areaOpen = ManagerGps.getInstance().areaOpen;
            carPosInfo.areaPos = ManagerGps.getInstance().areaPos;
            MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
                @Override
                public void onCurrentPosGet(final DataPos selfPos) {
                    handleChangeData();
                    MapPosGet.searchAddressByPos(carPosInfo.carPos, new MapPosGet.OnAddressGetListener() {
                        @Override
                        public void onAddressGet(final DataPos carPos) {
                            carPosInfo.address = carPos.address;
                            handleChangeData();
                        }
                    });

                }
            });
        } else if (eventName.equals(OEventName.GPS_SETAREA_RESULTBACK)) {
            boolean isDemoMode = DemoMode.getIsDemoMode();
            if (isDemoMode) {
                OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 1);
            } else {
                // 重获数据
                OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 0);
            }
        }
        super.receiveEvent(eventName,paramObj);
    }

    @Override
    public void callback(String key, Object value) {
        if (key.equals("operate")) {
            // "取消围栏","设置围栏"
            DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
            DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
//			if (car == null ||car.status.gps == null || "".equals(car.status.gps)) {
            if (car == null || carStatus.getGps() == null) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.countless_according_to_vehicle_position));
            } else {
                String op = (String) value;
                if (op.equals(getResources().getString(R.string.cancel_the_fence))) {
                    ManagerGps.areaMeter = 0;
                    OCtrlGps.getInstance().ccmd1214_setArea(currentCar.ide, 0, 0);
                } else if (op.equals(getResources().getString(R.string.set_fence))) {
                    OToastInput.getInstance().showInput(title_head, getResources().getString(R.string.input_radius_of_the_fence), getResources().getString(R.string.please_enter_the_radius_of_the_fence_unit_km), new String[]{OToastInput.OTHER_NUMBER}, "area",
                            ViewGpsCarArea.this);
                }
            }
        } else if (key.equals("area")) {
            JsonObject obj  = (JsonObject) value;
            String     area = OJsonGet.getString(obj, OToastInput.OTHER_NUMBER);
            if (area.equals(""))
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_enter_an_integer));
            try {
                ManagerGps.areaMeter = Integer.valueOf(area);//throw exception
                if (ManagerGps.areaMeter > 0) {
                    OCtrlGps.getInstance().ccmd1214_setArea(currentCar.ide, ManagerGps.areaMeter, 1);
                } else {
                    OCtrlGps.getInstance().ccmd1214_setArea(currentCar.ide, 0, 0);
                }
            } catch (NumberFormatException e) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.please_enter_an_integer));
                e.printStackTrace();
            }
        }
    }

}
