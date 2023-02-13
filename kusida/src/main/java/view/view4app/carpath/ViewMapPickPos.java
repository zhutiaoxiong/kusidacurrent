package view.view4app.carpath;

import android.os.Bundle;
import android.view.View;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;

import common.global.NAVI;
import common.map.DataPos;
import common.map.FullScreenMap;
import common.map.MapPosGet;
import ctrl.OCtrlGps;
import view.view4me.set.ClipTitleMeSet;

public class ViewMapPickPos extends ActivityBase {
    public static int PRE_VIEW_ID = 0;//跳转回去使用
    public static DataPos usePos;//跳转回去数据

    private ClipTitleMeSet title_head;
    private FullScreenMap    map_full;
    private ViewBelowMapItem view_pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_pick_pos);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        map_full = (FullScreenMap) findViewById(R.id.map_full);
        view_pos = (ViewBelowMapItem) findViewById(R.id.view_pos);
        view_pos.setNeedConfirm(true);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_NAVI_HOME_SET_SUCESS, this);
    }
    @Override
    protected void initViews() {

        MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
            @Override
            public void onCurrentPosGet(DataPos posData) {

                usePos = posData;
                if (usePos == null) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无法获取当前位置，请开启wifi或gps定位!");
                } else {
                    view_pos.setData(usePos.addressName, usePos.address);
                    map_full.clearOverlay();
                    map_full.placeMapClickPos(usePos.pos);
                    map_full.setOnClickMapListener(new FullScreenMap.OnClickMapListener() {
                        @Override
                        public void onClickMap(DataPos dataPos) {
                            usePos = dataPos;
                            view_pos.setData(usePos.addressName, usePos.address);
                            map_full.clearOverlay();
                            map_full.placeMapClickPos(usePos.pos);
                        }
                    });
                }
            }
        });

    }
    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                finish();
            }
        });
        view_pos.txt_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (usePos == null) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "未获取位置!");
                } else {
                    int num = ViewHomePosChoose.IS_CHOOSE_HOME_POS ? 0 : 1;
                    OCtrlGps.getInstance().ccmd1246_setNavigation(usePos.addressName, NAVI.Latlng2Str(usePos.pos), num);
                }
            }
        });

    }
    @Override
    public void receiveEvent(String key, Object value) {
        if (key.equals(OEventName.GPS_NAVI_HOME_SET_SUCESS)) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "设置成功!");
            finish();
        }
        super.receiveEvent(key, value);
    }
    @Override
    protected void invalidateUI() {

    }
    @Override
    protected void popView(int resId) {

    }
}
