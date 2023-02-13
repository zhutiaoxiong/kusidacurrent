package view.view4app.codriver;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import adapter.AdapterPickAuthor;
import model.ManagerCommon;
import model.common.DataAuthorization;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by Administrator on 2016/10/15.
 */
@Deprecated
public class ViewChooseAuthor extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private ListView          listView;
    private AdapterPickAuthor adapter;
    public ViewChooseAuthor(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_app_choose_author, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        listView = (ListView) findViewById(R.id.list_names);
        initViews();
        initEvents();
    }

    public void initViews() {
        // 显示权限列表
        List<DataAuthorization> authorlist = ManagerCommon.getInstance().authorlist;
        adapter = new AdapterPickAuthor(getContext(), authorlist, R.layout.list_item_name_check_pair);
        listView.setAdapter(adapter);
    }


    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,0);
                listView.setAdapter(null);
                adapter = null;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id){
                adapter.setSelectItem(position);
                listView.setAdapter(null);
                adapter = null;
            }
        });
    }
    @Override
    public void receiveEvent(String key, Object paramObj) {
//        if(key.equals(OEventName.CAR_SEARCHWARNING_LISTBACK)){
//        }
    }
    public void invalidateUI() {
    }
}