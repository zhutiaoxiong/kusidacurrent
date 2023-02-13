package view.view4app.carpath;

import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.tencent.tauth.TencentCommon;

import common.GlobalContext;
import common.global.OWXShare;
import common.map.DataPos;
import common.map.FullScreenMap;
import common.map.MapPosGet;
import common.map.WindowPosShareEtc;
import ctrl.OCtrlGps;
import model.ManagerGps;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2017/3/24.
 * 车位轨迹详情页面
 */

public class ViewCarPath extends ActivityBase {
    private FullScreenMap map;
    private ClipTitleMeSet title_head;
    private DataPos       myLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_car_path);
        map = (FullScreenMap) findViewById(R.id.map);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        initViews();
        initEvents();
        getMyLocation();
    }
    public void getMyLocation() {
        MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
            @Override
            public void onCurrentPosGet(DataPos posData) {
                myLocation = posData;
            }
        });
    }
    @Override
    protected void initViews() {
        if(ManagerGps.path == null)return;
        map.placePath(ManagerGps.path);
        map.placeShareWindow(false, ManagerGps.path.get(0), ManagerGps.startLocation, new WindowPosShareEtc.ClickButtonListener() {
            @Override
            public void onError(String error) {
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, error);
            }

            @Override
            public void onClickFavorite(LatLng favoritePos, String favoriteAddress) {
                OCtrlGps.getInstance().ccmd1216_favoritePos(favoritePos.latitude + "," + favoritePos.longitude, favoriteAddress);
            }

            @Override
            public void onClickShare(final String shareAddress, final String shareUrl) {
                OToastSharePath.getInstance().show(title_head, "sharePath00", new OToastSharePath.OnClickButtonListener() {
                    @Override
                    public void onClick(int pos) {
                        switch (pos) {
                            case 1:
                                OWXShare.SharePlace(shareAddress, shareUrl);
                                break;
                            case 2:
                                OWXShare.SharePlace(shareAddress, shareUrl);
                                break;
                            case 3:
                                TencentCommon.toTencent(getApplicationContext(), GlobalContext.getContext().getString(R.string.cool_your_friends_to_share_a_place_to_you), shareAddress, shareUrl, 0, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                                break;
                            case 4:
                                TencentCommon.toTencent(getApplicationContext(), GlobalContext.getContext().getString(R.string.cool_your_friends_to_share_a_place_to_you), shareAddress, shareUrl, 1, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                                break;
                        }

                    }
                });

            }

            @Override
            public void onClickNavorite(LatLng selfPos, final LatLng carPos, final String carPosAddress) {
//                OToastNavigate.getInstance().showOpenNavigate(title_head, selfPos, carPos, carPosAddress);
                MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
                    @Override
                    public void onCurrentPosGet(DataPos posData) {
                        DataPos end = new DataPos(carPos, carPosAddress);
                        openNavi(posData, end);
                    }
                });
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
//        BNRoutePlanNode endNode   = new BNRoutePlanNode(end.pos.longitude, end.pos.latitude, end.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        Intent          intent    = new Intent(getApplicationContext(), ActivityNaviMap.class);
//        Bundle          bundle    = new Bundle();
//        bundle.putSerializable(ActivityNaviMap.START_NODE, startNode);
//        bundle.putSerializable(ActivityNaviMap.END_NODE, endNode);
//        intent.putExtras(bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
//        getApplicationContext().startActivity(intent);
    }
}
