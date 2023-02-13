package view.view4app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import adapter.AdapterVioList;
import model.ManagerCommon;
import model.common.DataViolation;
import com.kulala.staticsview.titlehead.ClipTitleHead;
/**
 * 违章列表
 */
public class ViewViolationVioList extends RelativeLayoutBase{
	private ClipTitleHead  title_head;
	private RelativeLayout lin_no_violation;
	private ListView       list_cars;
	private AdapterVioList adapter;
	public ViewViolationVioList(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_violation_violist, this, true);
		title_head = (ClipTitleHead) findViewById(R.id.title_head);
		list_cars = (ListView) findViewById(R.id.list_cars);
		lin_no_violation = (RelativeLayout) findViewById(R.id.lin_no_violation);
		initViews();
		initEvents();
	}

	@Override
	public void initViews() {
		adapter = new AdapterVioList(getContext(), ManagerCommon.getInstance().violationList,R.layout.list_item_violation);
		list_cars.setAdapter(adapter);
		if(ManagerCommon.getInstance().violationList.size()==0){
			lin_no_violation.setVisibility(VISIBLE);
		}else{
			lin_no_violation.setVisibility(INVISIBLE);
		}
	}

	@Override
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_violation_carlist);
			}
		});
		list_cars.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adad, View arg1, int position, long id) {
				ViewViolationDetail.selectedViolation = (DataViolation)adapter.getItem(position);
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_violation_detail);
			}
		});
	}
	@Override
	public void receiveEvent(String key, Object paramObj) {
		if(key.equals(OEventName.VIOLATION_LIST_BACK)){

		}
	}

	@Override
	public void callback(String key, Object value) {
	}
	@Override
	public void invalidateUI(){
	}
	// =====================================================
}
