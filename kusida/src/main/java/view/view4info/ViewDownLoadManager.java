package view.view4info;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;
import com.kulala.staticsview.titlehead.ClipTitleHead;
import com.kulala.staticsview.toast.OToastButton;

import java.util.List;

import adapter.AdapterDownLoad;
import common.GlobalContext;
import ctrl.OCtrlInformation;
import model.ManagerInformation;
import model.information.DataSkin;
/**
 * 汽车皮肤已买页面
 */

/**
 * Created by qq522414074 on 2016/9/1.
 */
public class ViewDownLoadManager extends LinearLayoutBase {
    private static boolean isBoughtView = false;
    private ClipTitleHead titleHead;
    private ListView list_skins;

    private List<DataSkin>  list;
    private AdapterDownLoad adapter;
    private int             position;
    public ViewDownLoadManager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_find_downloadmanager,this,true);
        titleHead=(ClipTitleHead) findViewById(R.id.title_head);
        list_skins=(ListView)findViewById(R.id.list_skins);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.INFORMATION_SKINBOUGHTLIST_RESULTBACK,this);
        ODispatcher.addEventListener(OEventName.SKIN_URL_RESULTBACK,this);
    }
    public static void setIsBoughtView(boolean isBoughtView1){
        isBoughtView = isBoughtView1;
    }
    @Override
    protected void initViews() {
        if(isBoughtView) {
            OCtrlInformation.getInstance().ccmd1407_getBoughtSkins();
            titleHead.setTitle(getResources().getString(R.string.have_bought_the_skin));
        }else
        handleChangeData();
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if(eventName.equals(OEventName.INFORMATION_SKINBOUGHTLIST_RESULTBACK)){
            handleChangeData();
        }else if (eventName.equals(OEventName.SKIN_URL_RESULTBACK)) {
            String url = (String)paramObj;
            adapter.startDownLoad(OCtrlInformation.getInstance().skinId1402,url);
        }
    }
    @Override
    protected void initEvents() {
        titleHead.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_find_car_dressup);
            }
        });
        list_skins.setOnItemClickListener(new OnItemClickListenerMy(){
            @Override
            public void onItemClickNofast(AdapterView<?> adapterView, View view, int i, long l) {
                OToastButton.getInstance().show(titleHead, new String[]{getResources().getString(R.string.delete)}, "operate", ViewDownLoadManager.this);
                position=i;
            }
        });
    }

    @Override
    protected void invalidateUI() {
        if(isBoughtView){
            list = ManagerInformation.getInstance().getSkinBoughtList();
        }else {
            list = ManagerInformation.getInstance().getSkinUsedList();
        }
        if (adapter == null) {
            adapter = new AdapterDownLoad(ViewDownLoadManager.this.getContext(), list);
            list_skins.setAdapter(adapter);
        } else {
            adapter.changeData(list);
        }
    }

    @Override
    public void callback(String key, Object value) {
        if(key.equals("operate")){
            String opp = (String)value;
            if(opp.equals(getResources()
            .getString(R.string.delete))){
                DataSkin skin= adapter.getItem(position);
                //删除保存
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("IsDownLoadZip"+skin.ide, String.valueOf(false));
                if(skin.ide == 0){
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.can_not_delete_the_default_skin));
                }else {
                    handleChangeData();
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.skin_removed_successfully_to_use_again_please_download_again));
                }
            }
        }
    }
}
