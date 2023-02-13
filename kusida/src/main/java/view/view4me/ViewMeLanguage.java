package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapter.AdapterForChooseLanguage;
import common.GlobalContext;
import common.LanguageChoose;
import model.AppList.AppListData;
import model.ManagerSwitchs;
import com.kulala.staticsview.titlehead.ClipTitleHead;

/**
 * Created by qq522414074 on 2016/11/17.
 */

public class ViewMeLanguage extends LinearLayoutBase{
    private ClipTitleHead titleHead;
    private ListView listView;
    private AdapterForChooseLanguage adpater;
    private List<String> list;

    public ViewMeLanguage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_me_language,this,true);
        titleHead=(ClipTitleHead)findViewById(R.id.title_head);
        listView=(ListView)findViewById(R.id.view_me_language_listview);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        String[] a=new String []{"汉语","English"};

            list=new ArrayList<>();
            for(int i=0;i<a.length;i++){
                list.add(a[i]);
            }
            adpater=new AdapterForChooseLanguage(list,ViewMeLanguage.this.getContext());

        listView.setAdapter(adpater);
//        handleChangeData();
    }

    @Override
    protected void initEvents() {
        titleHead.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClick(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_me_setup);
            }
        });
        listView.setOnItemClickListener(new OnItemClickListenerMy(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                adpater.setCurrentItem(position);
                adpater.notifyDataSetChanged();
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("position", String.valueOf(position));
                clickLanguage(position);
                handleChangeData();
            }
        });

    }
    //改变语言时修改静态数据
    private void clickLanguage(int position){
        if(position==0){
            LanguageChoose.choose(Locale.CHINESE);
        }else if(position==1){
            LanguageChoose.choose(Locale.US);
        }
        ManagerSwitchs.getInstance().createNewswitchVoiceList();
        ManagerSwitchs.getInstance().createNewswitchConfirmList();
        AppListData.names= new String[] {GlobalContext.getContext().getResources().getString(R.string.deputy_owner_management), GlobalContext.getContext().getResources().getString(R.string.app_state_recode_title),GlobalContext.getContext(). getResources().getString(R.string.electronic_fence),
                GlobalContext.getContext().getResources().getString(R.string.parking_trajectory), GlobalContext.getContext().getResources().getString(R.string.app_violation_title), GlobalContext.getContext().getResources().getString(R.string.mantain_remind)};
    }

    @Override
    protected void invalidateUI() {

    }
}
