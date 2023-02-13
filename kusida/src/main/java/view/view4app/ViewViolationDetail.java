package view.view4app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import model.common.DataViolation;
import com.kulala.staticsview.titlehead.ClipTitleHead;
/**
 * 违章详情
 */
public class ViewViolationDetail extends LinearLayoutBase{
	private ClipTitleHead title_head;
	private TextView      txt_fee,txt_place,txt_reason,txt_code,txt_completed,txt_score;

	public static DataViolation selectedViolation;
	public ViewViolationDetail(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_violation_detail, this, true);
		title_head = (ClipTitleHead) findViewById(R.id.title_head);
		txt_fee = (TextView) findViewById(R.id.txt_fee);
		txt_place = (TextView) findViewById(R.id.txt_place);
		txt_reason = (TextView) findViewById(R.id.txt_reason);
		txt_code = (TextView) findViewById(R.id.txt_code);
		txt_completed = (TextView) findViewById(R.id.txt_completed);
		txt_score = (TextView) findViewById(R.id.txt_score);
		initViews();
		initEvents();
	}

	@Override
	public void initViews() {
		if(selectedViolation == null)return;
		txt_fee.setText(getResources().getString(R.string.fines)+selectedViolation.fee+getResources().getString(R.string.yuan));
		txt_place.setText(getResources().getString(R.string.illegal_sites)+selectedViolation.area);
		txt_reason.setText(getResources().getString(R.string.illegal_reason)+selectedViolation.reason);
		txt_code.setText(getResources().getString(R.string.violation_of_the_code)+selectedViolation.code);
		txt_completed.setText(getResources().getString(R.string.processing_state)+(selectedViolation.status == 1 ? getResources().getString(R.string.have_to_deal_with) :getResources().getString(R.string.undisposed)));
		txt_score.setText(getResources().getString(R.string.illegal_points)+selectedViolation.score+getResources().getString(R.string.mark));
	}

	@Override
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_violation_violist);
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
