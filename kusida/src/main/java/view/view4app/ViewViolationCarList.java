package view.view4app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;

import ctrl.OCtrlCommon;
import model.ManagerCarList;
import model.demomode.DemoMode;
import com.kulala.staticsview.titlehead.ClipTitleHead;

/**
 * 违章车列表
 */
public class ViewViolationCarList extends LinearLayoutBase {
    private ClipTitleHead title_head;
    private ListView list_cars;

    public ViewViolationCarList(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_violation_carlist, this, true);
        title_head = (ClipTitleHead) findViewById(R.id.title_head);
        list_cars = (ListView) findViewById(R.id.list_cars);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.VIOLATION_LIST_BACK, this);
    }

    @Override
    public void initViews() {
        String[] list = ManagerCarList.getInstance().getCarNameListAll();
        ListAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_onlytext, list);
        list_cars.setAdapter(adapter);
    }

    @Override
    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        list_cars.setOnItemClickListener(new OnItemClickListenerMy() {
            @Override
            public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
                String carname = (String) parent.getItemAtPosition(position);
                if (carname == null || carname.equals("")) return;
                boolean isDemoMode=DemoMode.getIsDemoMode();
//                        if (!DemoMode.isDemoMode.equals("演示开始")) {
                if (isDemoMode) {
//                if (DemoMode.isDemoMode.equals("演示开始")) {
                    OCtrlCommon.getInstance().ccmd1205_violationGetList(ManagerCarList.getInstance().getCarIdByName(carname), 1);
                } else {
                    OCtrlCommon.getInstance().ccmd1205_violationGetList(ManagerCarList.getInstance().getCarIdByName(carname), 0);
                }
                super.onItemClickNofast(parent, view, position, id);
            }
        });
    }

    @Override
    public void receiveEvent(String key, Object paramObj) {
        if (key.equals(OEventName.VIOLATION_LIST_BACK)) {
            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_violation_violist);
        }
    }

    @Override
    public void callback(String key, Object value) {
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.VIOLATION_LIST_BACK, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
    }
    // =====================================================
}
