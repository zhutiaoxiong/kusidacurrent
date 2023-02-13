package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class ViewSwitchMessage extends RelativeLayoutBase{
	private ClipTitleMeSet title_head;
	private ListView			listView;
	private Button				btn_control, btn_warning, btn_safety;
	private AdapterSwitchOnOff	adapter;
	private List<DataSwitch>	useSwitchList;
	private int					selectPos	= 0;
	public ViewSwitchMessage(Context context, AttributeSet attrs) {
		super(context, attrs);// this layout for add and edit
		LayoutInflater.from(context).inflate(R.layout.view_me_switch_message, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		listView = (ListView) findViewById(R.id.list_names);
		btn_control = (Button) findViewById(R.id.btn_control);
		btn_warning = (Button) findViewById(R.id.btn_warning);
		btn_safety = (Button) findViewById(R.id.btn_safety);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.SWITCH_WARNINGS_RESULTBACK, this);
	}
	@Override
	public void initViews() {
		useSwitchList = ManagerSwitchs.getInstance().getSwitchControls();
		if (useSwitchList == null || useSwitchList.size() == 0) {
		} else {
			handleChangeData();
		}
		OCtrlCommon.getInstance().ccmd1305_getSwitchInfo();
	}
	@Override
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_setup);
			}
		});
		btn_control.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				selectPos = 0;
				handleChangeData();
			}
		});
		btn_warning.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				selectPos = 1;
				handleChangeData();
			}
		});
		btn_safety.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				selectPos = 2;
				handleChangeData();
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
		btn_control.setBackgroundResource(R.color.white);
		btn_warning.setBackgroundResource(R.color.white);
		btn_safety.setBackgroundResource(R.color.white);
		if (selectPos == 0) {
			btn_control.setBackgroundResource(R.color.all_background_color);
			useSwitchList = ManagerSwitchs.getInstance().getSwitchControls();
		} else if (selectPos == 1) {
			btn_warning.setBackgroundResource(R.color.all_background_color);
			useSwitchList = ManagerSwitchs.getInstance().getSwitchWarnings();
		} else if (selectPos == 2) {
			btn_safety.setBackgroundResource(R.color.all_background_color);
			useSwitchList = ManagerSwitchs.getInstance().getSwitchSafetys();
		}
		if (useSwitchList == null || useSwitchList.size() == 0)
			return;
		adapter = new AdapterSwitchOnOff(getContext(), useSwitchList, R.layout.list_item_name_switch_pair);
		listView.setAdapter(adapter);
	}
	// ===================================================
}
