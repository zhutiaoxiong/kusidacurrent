package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.AdapterMeHelp;
import model.ManagerPublicData;
import view.view4me.set.ClipTitleMeSet;


/**
 * Created by qq522414074 on 2017/1/11.
 */

public class ViewMeHelp extends RelativeLayoutBase {
    private final ClipTitleMeSet title_head;
    private final ListView listView;
    private AdapterMeHelp adapter;
    private String swit;
    public ViewMeHelp(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_help, this, true);
        title_head= findViewById(R.id.title_head);
        listView =  findViewById(R.id.list_mantance);
        initViews();
        initEvents();
    }
    @Override
    public void initViews() {
        List<String> list = createData();
        adapter = new AdapterMeHelp(getContext(), list);
        listView.setAdapter(adapter);
    }
    private List<String> createData(){
        String[] data=new String[]{"控车页面功能介绍","如何分享微信钥匙 (微信借车)","如何分享APP钥匙 (APP借车)","如何关闭开关锁时车辆喇叭声音","什么是启动保护","什么是洗护模式","如何添加NFC","如何给界面车辆换肤","如何关闭APP提示音","什么是摇一摇","什么是无匙进入","什么是行车落锁","什么是锁车升窗","如何添加物理遥控器"};
        List<String> list=new ArrayList<>();
        Collections.addAll(list, data);
        return list;
    }
    @Override
    public void initEvents() {
        //back
        title_head.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        listView.setOnItemClickListener(new OnItemClickListenerMy(){
            @Override
            public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
                swit = adapter.getItem(position);
                switch (swit){
                    case "控车页面功能介绍":
                        ManagerPublicData.meHelpNumber="1";
                        toLookPic();
                        break;
                    case "如何分享APP钥匙 (APP借车)":
                        ManagerPublicData.meHelpNumber="2";
                        toLookPic();
                        break;
                    case "如何分享微信钥匙 (微信借车)":
                        ManagerPublicData.meHelpNumber="3";
                        toLookPic();
                        break;
                    case "如何给界面车辆换肤":
                        ManagerPublicData.meHelpNumber="4";
                        toLookPic();
                        break;
                    case "如何关闭开关锁时车辆喇叭声音":
                        ManagerPublicData.meHelpNumber="5";
                        toLookPic();
                        break;
                    case "如何关闭APP提示音":
                        ManagerPublicData.meHelpNumber="6";
                        toLookPic();
                        break;

                    case "什么是启动保护":
                        ManagerPublicData.meHelpNumber="7";
                        toLookText();
                        break;
                    case "什么是洗护模式":
                        ManagerPublicData.meHelpNumber="8";
                        toLookText();
                        break;
                    case "如何添加NFC":
                        ManagerPublicData.meHelpNumber="9";
                        toLookText();
                        break;
                    case "什么是摇一摇":
                        ManagerPublicData.meHelpNumber="10";
                        toLookText();
                        break;
                    case "什么是无匙进入":
                        ManagerPublicData.meHelpNumber="11";
                        toLookText();
                        break;
                    case "什么是行车落锁":
                        ManagerPublicData.meHelpNumber="12";
                        toLookText();
                        break;
                    case "什么是锁车升窗":
                        ManagerPublicData.meHelpNumber="13";
                        toLookText();
                        break;
                    case "如何添加物理遥控器":
                        ManagerPublicData.meHelpNumber="14";
                        toLookText();
                        break;
                }
            }
        });
    }

    private void toLookPic(){
        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_help_lookpic);
    }

    private void toLookText(){
        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_help_looktext);
    }

    @Override
    protected void invalidateUI() {

    }

    // =====================================================
    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

    @Override
    protected void onDetachedFromWindow() {
        listView.setAdapter(null);
        adapter = null;
        super.onDetachedFromWindow();
    }

}
