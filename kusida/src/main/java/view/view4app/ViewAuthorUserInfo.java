package view.view4app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.titlehead.ClipTitleHead;

import adapter.AdapterPickAuthor;
import model.common.DataAuthoredUser;
import view.clip.ClipLineBtnInptxt;
import view.view4app.codriver.ActivityAuthorDetails;

public class ViewAuthorUserInfo extends LinearLayoutBase{
	private ClipTitleHead     title_head;
	private ClipLineBtnInptxt txt_nickname, txt_phone,txt_carname;
	private TextView txt_time;
	private ListView list_authors;
	private AdapterPickAuthor		authorAdapter;
	public ViewAuthorUserInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_pop_author_detail, this, true);
		title_head = (ClipTitleHead) findViewById(R.id.title_head);
		txt_nickname = (ClipLineBtnInptxt) findViewById(R.id.txt_nickname);
		txt_phone = (ClipLineBtnInptxt) findViewById(R.id.txt_phone);
		txt_carname = (ClipLineBtnInptxt) findViewById(R.id.txt_carname);
		txt_time = (TextView) findViewById(R.id.txt_time);
		list_authors = (ListView) findViewById(R.id.list_authors);
		initViews();
		initEvents();
	}

	@Override
	public void initViews() {
//		if(ClipPopAuthorDetail.selectedUser!=null)
		if(ActivityAuthorDetails.selectedUser!=null)
		handleChangeData();
	}
	@Override
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_codriver);
			}
		});
	}
	@Override
	public void callback(String key,Object obj) {
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
	}
	@Override
	public void invalidateUI(){

//		DataAuthoredUser selectedUser = ClipPopAuthorDetail.selectedUser;
		DataAuthoredUser selectedUser = ActivityAuthorDetails.selectedUser;
		if(selectedUser == null)return;
		txt_nickname.setText(selectedUser.userinfo.name);
		txt_phone.setText(selectedUser.userinfo.phoneNum);
		txt_carname.setText(selectedUser.carinfo.num);
		String time =getResources().getString(R.string.from)+ ODateTime.time2StringWithHH(selectedUser.startTime)+getResources().getString(R.string.to)+ODateTime.time2StringWithHH(selectedUser.endTime);
		txt_time.setText(time);
		
//		txt_nickname.setRightTextColorResid(R.color.black);
//		txt_phone.setRightTextColorResid(R.color.black);
//		txt_carname.setRightTextColorResid(R.color.black);
//		txt_time.setTextColor(getResources().getColor(R.color.black));
		
		authorAdapter = new AdapterPickAuthor(getContext(), selectedUser.authors, R.layout.list_item_name_check_pair);
		list_authors.setAdapter(authorAdapter);
	}
	// =====================================================
}
