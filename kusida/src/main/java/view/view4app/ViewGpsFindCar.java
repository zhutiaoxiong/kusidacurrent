package view.view4app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;

import common.global.NAVI;
import common.global.OToastNavigate;
import common.global.OWXShare;
import common.map.DataCar_Area_Pos;
import common.map.DataPos;
import common.map.FullScreenMap;
import common.map.MapPosGet;
import common.map.WindowPosShareEtc;
import ctrl.OCtrlGps;
import model.ManagerCarList;
import model.ManagerGps;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import model.demomode.DemoMode;
import view.clip.ClipPopChooseStr;
import view.view4me.set.ClipTitleMeSet;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
/**
 * 车位轨迹
 */
public class ViewGpsFindCar extends ActivityBase {
    private ClipTitleMeSet title_head;
    private TextView      txt_carname, txt_time, txt_place;
    private LinearLayout lin_favorite, lin_carpath;
    private FullScreenMap map_full;
    private ImageView     img_gps;

    private DataCarInfo      currentCar;
    private DataCar_Area_Pos carPosInfo;
    private ShareUrlSearch   shareUrlSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if (BuildConfig.DEBUG) Log.e("ViewShow","ViewGpsFindCar");
        setContentView(R.layout.view_app_gps_findcar);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        img_gps = (ImageView) findViewById(R.id.img_gps);
        lin_favorite = (LinearLayout) findViewById(R.id.lin_favorite);
        lin_carpath = (LinearLayout) findViewById(R.id.lin_carpath);
        txt_carname = (TextView) findViewById(R.id.txt_carname);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_place = (TextView) findViewById(R.id.txt_place);
        map_full = (FullScreenMap) findViewById(R.id.map_full);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_CARPOS_RESULTBACK, this);
//        ODispatcher.addEventListener(OEventName.CAR_STATUS_CHANGE, this);
    }

    @Override
    public void initViews() {
        //setDefault
        txt_carname.setText("");
        txt_time.setText("");
        txt_place.setText("");
        img_gps.setVisibility(INVISIBLE);
        // initInfoWindow();
        currentCar = ManagerCarList.getInstance().getCurrentCar();
        if (currentCar == null || currentCar.ide == 0) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, getResources().getString(R.string.did_not_choose_the_vehicle));
            return;
        }
        shareUrlSearch = ShareUrlSearch.newInstance();
        txt_carname.setText(currentCar.num);
        boolean isDemoMode = DemoMode.getIsDemoMode();
        if (isDemoMode) {
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
            }
        });
        // select car
        title_head.img_right.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String[] arr = ManagerCarList.getInstance().getCarNameListActive();//只有已激活可选
                ClipPopChooseStr.getInstance().show(title_head, arr, getResources().getString(R.string.please_select_a_vehicle), "pickCar", ViewGpsFindCar.this);
            }
        });
        lin_favorite.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_gps_favorite);
            }
        });
        lin_carpath.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_gps_path_list);
            }
        });
    }
    @Override
    protected void invalidateUI() {

    }
    @Override
    protected void popView(int resId) {

    }

    @Override
    public void callback(String key, Object value) {
        if (key.equals("pickCar")) {
            String carname = (String) value;
            ManagerCarList.getInstance().setCurrentCarByName(carname);
            currentCar = ManagerCarList.getInstance().getCurrentCar();
            if (currentCar == null) {
                boolean isDemoMode = DemoMode.getIsDemoMode();
                if (isDemoMode) {
                    txt_carname.setText(currentCar.num);
                    OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 1);
                }
            } else {
                txt_carname.setText(currentCar.num);
                OCtrlGps.getInstance().ccmd1213_getCarPos(currentCar.ide, 0);
            }
        }
    }


    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.GPS_CARPOS_RESULTBACK)) {//|| eventName.equals(OEventName.CAR_STATUS_CHANGE
            boolean isgetPos = (Boolean) paramObj;
            if (!isgetPos && eventName.equals(OEventName.GPS_CARPOS_RESULTBACK)) return;
            //定位数据返回
            // 定位数据返回
            if (carPosInfo == null) carPosInfo = new DataCar_Area_Pos();
            currentCar = ManagerCarList.getInstance().getCurrentCar();
            DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
            carPosInfo.carPos = carStatus.getGps();
            carPosInfo.direction = carStatus.direction;
            carPosInfo.areaMeter = ManagerGps.getInstance().areaMeter;
            carPosInfo.areaOpen = ManagerGps.getInstance().areaOpen;
            carPosInfo.areaPos = ManagerGps.getInstance().areaPos;
            MapPosGet.searchAddressByPos(carPosInfo.carPos, new MapPosGet.OnAddressGetListener() {
                @Override
                public void onAddressGet(DataPos posData) {
                    carPosInfo.address = posData.address;
                    txt_time.setText(ODateTime.time2StringWithHH(ODateTime.getNow()));
//                    txt_carname.setText(currentCar.num);//已设，不用改
                    txt_place.setText(posData.address);
                    img_gps.setVisibility(VISIBLE);
                    float angle = countAngle(preCarPos, posData.pos);
                    map_full.clearOverlay();
                    map_full.placeCar(posData.pos, carPosInfo.direction, posData.address);//carPosInfo.direction
                    map_full.placeShareWindow(false, posData.pos, posData.address, new WindowPosShareEtc.ClickButtonListener() {
                        @Override
                        public void onError(String error) {
                            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, error);
                        }

                        @Override
                        public void onClickFavorite(LatLng favoritePos, String favoriteAddress) {
                            OCtrlGps.getInstance().ccmd1216_favoritePos(favoritePos.latitude + "," + favoritePos.longitude, favoriteAddress);
                        }

                        @Override
                        public void onClickShare(String shareAddress, String shareUrl) {
                            OWXShare.SharePlace(shareAddress, shareUrl);
                        }

                        @Override
                        public void onClickNavorite(LatLng selfPos, LatLng carPos, String carPosAddress) {
                            OToastNavigate.getInstance().showOpenNavigate(txt_carname, selfPos, carPos, carPosAddress);
                        }
                    });
                }
            });
        }
        super.receiveEvent(eventName, paramObj);
    }

    private float preAngle = 0;
    private LatLng preCarPos;

    private float countAngle(LatLng pre, LatLng now) {
        double dis = NAVI.getDistance(pre, now);//无数据为0
        if (dis < 4) {//小于四米时使用初始角度
            if (carPosInfo.direction != 0) {
                float dirAngle = -1 * carPosInfo.direction;
                preAngle = dirAngle;
                return dirAngle;
            }
        }
        float resultAngle = 0f;
        if (pre == null && now != null) {
            preCarPos = new LatLng(now.latitude, now.longitude);
            return 0f;//-60f
        }
        if (pre == null || now == null) return 0f;//-60f
        //X相等
        //latitude上下,longitude左右,0度为正上方,逆时针角度
        int equalsY = Double.compare(now.latitude * 1000, pre.latitude * 1000);//1Y正
        int equalsX = Double.compare(now.longitude * 1000, pre.longitude * 1000);//1X正
        if (equalsX == 0 || equalsY == 0) {
            if (equalsX == 0 && equalsY == 0) {//与前坐标相等
                resultAngle = preAngle;
            } else if (equalsX == 1 && equalsY == 0) {//左直走
                resultAngle = -90f;
            } else if (equalsX == -1 && equalsY == 0) {//右直走
                resultAngle = 90f;
            } else if (equalsX == 0 && equalsY == 1) {//上直走
                resultAngle = 0f;
            } else if (equalsX == 0 && equalsY == 0) {//下直走
                resultAngle = 180f;
            }
            preCarPos = new LatLng(now.latitude, now.longitude);
            preAngle = resultAngle;
            return resultAngle;
        }
        double yLength   = now.latitude * 10000 - pre.latitude * 10000;
        double xLength   = now.longitude * 10000 - pre.longitude * 10000;
        double atanAngle = Math.abs(Math.atan(yLength / xLength));
        if (Double.isNaN(atanAngle)) return preAngle;//-60f
        double angle = Math.toDegrees(atanAngle);
        if (xLength < 0 && yLength > 0) {
            angle = 180 - angle;
        } else if (xLength < 0 && yLength < 0) {
            angle = 180 + angle;
        } else if (xLength > 0 && yLength < 0) {
            angle = -angle;
        }
        preAngle = (float) angle - 90;
        preCarPos = new LatLng(now.latitude, now.longitude);
        return preAngle;
    }
}
