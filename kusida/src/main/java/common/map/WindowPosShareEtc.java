package common.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.client.proj.kusida.R;

public class WindowPosShareEtc extends RelativeLayout {
    public static TextView txt_favorite, txt_navorite, txt_share;

    private DataPos              posCar;
    private ClickButtonListener  clickButtonListener;
//    private OnClickCloseListener onClickCloseListener;
    public WindowPosShareEtc(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.map_window_share, this, true);
        txt_favorite = (TextView) findViewById(R.id.txt_favorite);
        txt_share = (TextView) findViewById(R.id.txt_share);
        txt_navorite = (TextView) findViewById(R.id.txt_navoritee);

        initEvent();
    }
    public void setData(DataPos posCar, ClickButtonListener listener) {
        this.posCar = posCar;
        this.clickButtonListener = listener;
    }
    private void initEvent() {
        txt_favorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(onClickCloseListener!=null)onClickCloseListener.onClose();
                if (posCar == null || posCar.pos == null || posCar.address == null) {
                    if (clickButtonListener != null) clickButtonListener.onError("当前位置无信息");
                    return;
                }
                if (clickButtonListener != null)
                    clickButtonListener.onClickFavorite(posCar.pos, posCar.address);
            }
        });
        txt_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(onClickCloseListener!=null)onClickCloseListener.onClose();
                if (posCar == null || posCar.pos == null || posCar.address == null) {
                    if (clickButtonListener != null) clickButtonListener.onError("当前位置无信息");
                    return;
                }
                ShareUrlSearch shareUrlSearch = ShareUrlSearch.newInstance();
                shareUrlSearch.setOnGetShareUrlResultListener(new OnGetShareUrlResultListener() {
                    @Override
                    public void onGetRouteShareUrlResult(ShareUrlResult result) {}
                    @Override
                    public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {}
                    @Override
                    public void onGetLocationShareUrlResult(ShareUrlResult result) {
                        if (clickButtonListener != null)
                            clickButtonListener.onClickShare(posCar.address, result.getUrl());
                    }
                });
                shareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
                        .location(posCar.pos).snippet("我的位置").name(posCar.address));
            }
        });
        txt_navorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
                    @Override
                    public void onCurrentPosGet(DataPos posData) {

                        if (posCar == null || posCar.pos == null || posCar.address == null) {
                            if (clickButtonListener != null) clickButtonListener.onError("当前位置无信息");
                            return;
                        }
                        LatLng selfPos = posData==null ? null : posData.pos;
                        if (clickButtonListener != null)
                            clickButtonListener.onClickNavorite(selfPos, posCar.pos, posCar.address);
                    }
                });
            }
        });
    }

    public interface ClickButtonListener {
        void onError(String error);//有错误就提示出来

        void onClickFavorite(LatLng favoritePos, String favoriteAddress);

        void onClickShare(String shareAddress, String shareUrl);

        void onClickNavorite(LatLng selfPos, LatLng carPos, String carPosAddress);
    }
//    public interface OnClickCloseListener {
//        void onClose();
//    }
//    public void setOnClickCloseListener(OnClickCloseListener listener){
//        this.onClickCloseListener = listener;
//    }
}

