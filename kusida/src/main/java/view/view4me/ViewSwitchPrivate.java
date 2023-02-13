package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;

import java.util.List;

import adapter.AdapterSwitchOnOff;
import ctrl.OCtrlCommon;
import model.ManagerSwitchs;
import model.status.DataSwitch;
import view.view4me.set.ClipTitleMeSet;

public class ViewSwitchPrivate extends RelativeLayoutBase{
	private ClipTitleMeSet title_head;
	private ListView           listView;
	private AdapterSwitchOnOff adapter;
	private List<DataSwitch>   useSwitchList;
	public ViewSwitchPrivate(Context context, AttributeSet attrs) {
		super(context, attrs);//this layout for add and edit
		LayoutInflater.from(context).inflate(R.layout.view_me_switch_private, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		listView = (ListView) findViewById(R.id.list_names);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.SWITCH_WARNINGS_RESULTBACK, this);
	}
	@Override
	public void initViews() {
		useSwitchList = ManagerSwitchs.getInstance().getSwitchPrivates();
		if(useSwitchList == null){
		}else{
			handleChangeData();
		}
		OCtrlCommon.getInstance().ccmd1305_getSwitchInfo();
	}
	@Override
	public void initEvents() {
		//back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_setup);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListenerMy() {
			@Override
			public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
				DataSwitch info = adapter.getItem(position);
				info.isOpen = (info.isOpen == 1) ? 0: 1;
				adapter.notifyDataSetChanged();
				OCtrlCommon.getInstance().ccmd1306_changeSwitch(info.ide, (info.isOpen == 1)?true : false);
			}
		});
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		if (eventName.equals(OEventName.SWITCH_WARNINGS_RESULTBACK)) {
			handleChangeData();
		}
	}
	// =====================================================
	@Override
	public void callback(String key, Object value) {
		super.callback(key, value);
	}

	@Override
	protected void onDetachedFromWindow() {
		ODispatcher.removeEventListener(OEventName.SWITCH_WARNINGS_RESULTBACK, this);
		listView.setAdapter(null);
		adapter = null;
		super.onDetachedFromWindow();
	}

	@Override
	public void invalidateUI()  {
		useSwitchList = ManagerSwitchs.getInstance().getSwitchPrivates();
		adapter = new AdapterSwitchOnOff(getContext(),useSwitchList , R.layout.list_item_name_switch_pair);
		listView.setAdapter(adapter);
	}
	// ===================================================
}
