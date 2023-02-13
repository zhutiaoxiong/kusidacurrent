package view.view4app;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.OMapInfoWindow;
import com.client.proj.kusida.R;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterShowGpsFavorite;
import common.GlobalContext;
import ctrl.OCtrlGps;
import model.ManagerGps;
import model.gps.DataGpsPoint;


import view.clip.child.ClipSearchHead;
import view.view4me.set.ClipTitleMeSet;

public class ViewGpsFavorite extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    private TextView               txt_delete;
    private ListView               list_paths;
    private ClipSearchHead         searchHead;
    private AdapterShowGpsFavorite adapter;

    public ViewGpsFavorite(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_app_gps_favorite, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_delete = (TextView) findViewById(R.id.txt_delete);
        list_paths = (ListView) findViewById(R.id.list_paths);
        searchHead = new ClipSearchHead(context, null);
        list_paths.addHeaderView(searchHead);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_FAVORITE_LISTCHANGE, this);
        ODispatcher.addEventListener(OEventName.GPS_FAVORITE_INTRO_CHANGE_OK, this);
    }

    @Override
    public void initViews() {
        OMapInfoWindow.getCurrentLocation();
        OCtrlGps.getInstance().ccmd1218_favoriteList();
        //remove
        handleChangeData();
    }

    @Override
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewGpsFindCar.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        // search
        searchHead.img_search.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String searchValue = searchHead.getSearchStr();
                if (searchValue == null) return;
                if (searchValue.equals("")) {
                    handleChangeData();
                } else {
                    List<DataGpsPoint> favoriteList = ManagerGps.getInstance().getSearchFavoriteList(searchValue);
                    if (favoriteList == null) return;
                    adapter = new AdapterShowGpsFavorite(getContext(), favoriteList, R.layout.list_item_gps_favorite);
                    list_paths.setAdapter(adapter);
                }
            }
        });
        txt_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = txt_delete.getText().toString();
                if (txt.equals(getResources().getString(R.string.delete))) {
                    if (adapter != null && adapter.getCount() > 0) {
                        adapter.showSelect(true);
                        txt_delete.setText(getResources().getString(R.string.confirm));
                    }
                } else if (txt.equals(getResources().getString(R.string.confirm))) {
                    txt_delete.setText(getResources().getString(R.string.delete));
                    adapter.showSelect(false);
                    int              num  = adapter.getSelectNum();
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("删除坐标")
                            .withInfo("你确定要删除选中的"+num+"条坐标吗?")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm ) {
                                        if (adapter != null) {
                                            List<DataGpsPoint> list       = adapter.getDataList();
                                            List<Long>         deleteList = new ArrayList<Long>();
                                            for (DataGpsPoint point : list) {
                                                if (point.isSelected) deleteList.add(point.ide);
                                            }
                                            if (deleteList.size() > 0) {
                                                OCtrlGps.getInstance().ccmd1217_favoriteDelete(deleteList);
                                            }
                                        }
                                    }
                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.GPS_FAVORITE_LISTCHANGE)) {
            handleChangeData();
        }else if (eventName.equals(OEventName.GPS_FAVORITE_INTRO_CHANGE_OK)) {
            handleChangeData();
        }
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

    @Override
    protected void onDetachedFromWindow() {
        list_paths.setAdapter(null);
        adapter = null;
        ODispatcher.removeEventListener(OEventName.GPS_FAVORITE_LISTCHANGE, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
        searchHead.setSearchStr("");
        List<DataGpsPoint> favoriteList = ManagerGps.getInstance().favoriteList;
        if (favoriteList == null) return;
        adapter = new AdapterShowGpsFavorite(getContext(), favoriteList, R.layout.list_item_gps_favorite);
        list_paths.setAdapter(adapter);
    }
    // =====================================================
}
