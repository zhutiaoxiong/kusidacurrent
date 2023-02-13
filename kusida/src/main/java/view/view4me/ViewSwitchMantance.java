package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterSwitchMantance;
import common.GlobalContext;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.view4me.set.ClipTitleMeSet;


/**
 * Created by qq522414074 on 2017/1/11.
 */

public class ViewSwitchMantance extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    private ListView listView;
    private AdapterSwitchMantance adapter;
    private List<DataCarInfo> list=new ArrayList<>();
    private DataCarInfo swit;
    public ViewSwitchMantance(Context context, AttributeSet attrs) {
        super(context, attrs);//this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_me_switch_mantance, this, true);
        title_head = findViewById(R.id.title_head);
        listView = (ListView) findViewById(R.id.list_mantance);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CAR_LIST_CHANGE,this);
        ODispatcher.addEventListener(OEventName.CAR_SET_CONTROL_RESULT,this);
    }
    @Override
    public void initViews() {
        list=ManagerCarList.getInstance().getCarInfoList();
        adapter = new AdapterSwitchMantance(getContext(),list, R.layout.list_item_name_switch_mantance);
        listView.setAdapter(adapter);
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
                if(swit!=null&&swit.isActive==1){
                    OCtrlCar.getInstance().ccmd1251_Car_Sound_Control(swit.ide,swit.washMould==0?1:0,3);
                }
//                if(swit.washMould==0){
//                    OCtrlCar.getInstance().ccmd1238_MantanceMode(swit.ide,1);
//                }else if(swit.washMould==1){
//                    OCtrlCar.getInstance().ccmd1238_MantanceMode(swit.ide,2);
//                }

//                swit.setisOpen(!swit.getisOpen());
//                adapter.notifyDataSetChanged();
//                ManagerSwitchs.getInstance().saveSwitchVoice(swit);
            }
        });
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

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.CAR_LIST_CHANGE)){
            handleChangeData();
        }else if(s.equals(OEventName.CAR_SET_CONTROL_RESULT)){
            handleChangeData();
        }
    }

    @Override
    public void invalidateUI()  {
        if(adapter!=null){
            List<DataCarInfo> listaa=new ArrayList<>();
            listaa=ManagerCarList.getInstance().getCarInfoList();
            if(listaa==null||listaa.size()==0)return;
           adapter. changeUI(listaa);
            if(swit!=null&&swit.washMould==1){
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withInfo(swit.num+ "的维修模式已经启动,所有人均无法控制该车直至关闭。")
                        .withButton("","知道了")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if (isClickConfirm){
                                }
                            }
                        })
                        .show();
            }else{
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,GlobalContext.getContext().getResources().getString(R.string.mantancemode_close));
            }
        }

    }
}
