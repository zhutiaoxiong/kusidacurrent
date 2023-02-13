package view.view4app.carpath;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.tencent.tauth.TencentCommon;

import common.GlobalContext;
import common.global.NAVI;
import common.global.OWXShare;
import common.map.DataPos;
import common.map.FullScreenMap;
import common.map.MapPosGet;
import ctrl.OCtrlGps;
import view.view4me.set.ClipTitleMeSet;

public class ViewMapWatchPos extends ActivityBase {
    public static int PRE_VIEW_ID = 0;//跳转回去使用
    public static DataPos usePos;//跳转回去数据

    private ClipTitleMeSet title_head;
    private FullScreenMap map_full;
    private ViewBelowMapItem view_pos;
    private TextView txt_share, txt_navi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_watch_pos);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        map_full = (FullScreenMap) findViewById(R.id.map_full);
        view_pos = (ViewBelowMapItem) findViewById(R.id.view_pos);
        txt_share = (TextView) findViewById(R.id.txt_share);
        txt_navi = (TextView) findViewById(R.id.txt_navi);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_NAVI_HOME_SET_SUCESS, this);
    }

    @Override
    protected void initViews() {
        title_head.setTitle(usePos.address);
        MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
            @Override
            public void onCurrentPosGet(DataPos posData) {
//                usePos = posData;
                if (usePos == null) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无法获取当前位置，请开启wifi或gps定位!");
                } else {
                    view_pos.setData(usePos.addressName, usePos.address);
                    map_full.clearOverlay();
                    map_full.placeMapClickPos(usePos.pos);
//            map_full.setOnClickMapListener(new FullScreenMap.OnClickMapListener() {
//                @Override
//                public void onClickMap(DataPos dataPos) {
//                    usePos = dataPos;
//                    view_pos.setData(usePos.addressName, usePos.address);
//                    map_full.clearOverlay();
//                    map_full.placeMapClickPos(usePos.pos);
//                }
//            });
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

        txt_share.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ShareUrlSearch shareUrlSearch = ShareUrlSearch.newInstance();
                shareUrlSearch.setOnGetShareUrlResultListener(new OnGetShareUrlResultListener() {
                    @Override
                    public void onGetRouteShareUrlResult(ShareUrlResult result) {
                    }

                    @Override
                    public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {
                    }

                    @Override
                    public void onGetLocationShareUrlResult(final ShareUrlResult result) {
                        OToastSharePath.getInstance().show(title_head, "", new OToastSharePath.OnClickButtonListener() {
                            @Override
                            public void onClick(int pos) {
                                switch (pos) {
                                    case 1:
                                        OWXShare.SharePlace(usePos.address, result.getUrl());
                                        break;
                                    case 2:
                                        OWXShare.SharePlace(usePos.address, result.getUrl());
                                        break;
                                    case 3:
                                        TencentCommon.toTencent(getApplicationContext(), GlobalContext.getContext().getString(R.string.cool_your_friends_to_share_a_place_to_you), usePos.address, result.getUrl(), 0, "");
                                        break;
                                    case 4:
                                        TencentCommon.toTencent(getApplicationContext(), GlobalContext.getContext().getString(R.string.cool_your_friends_to_share_a_place_to_you), usePos.address, result.getUrl(), 1, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                                        break;
                                }
                            }
                        });
                    }
                });
                shareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
                        .location(usePos.pos).snippet("我的位置").name(usePos.address));
            }
        });
        txt_navi.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
                    @Override
                    public void onCurrentPosGet(DataPos posData) {
                        openNavi(posData, usePos);
                    }
                });

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

    private void openNavi(DataPos start, DataPos end) {
        if (start == null || start.pos == null) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "无法获取自已定位");
            return;
        }
//        BNRoutePlanNode startNode = new BNRoutePlanNode(start.pos.longitude, start.pos.latitude, start.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        BNRoutePlanNode endNode = new BNRoutePlanNode(end.pos.longitude, end.pos.latitude, end.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        Intent intent = new Intent(getApplicationContext(), ActivityNaviMap.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ActivityNaviMap.START_NODE, startNode);
//        bundle.putSerializable(ActivityNaviMap.END_NODE, endNode);
//        intent.putExtras(bundle);
//        getApplicationContext().startActivity(intent);
    }
}
