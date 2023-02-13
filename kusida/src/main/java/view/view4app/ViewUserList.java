package view.view4app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.toast.OToastButton;
import com.kulala.staticsview.toast.OToastInput;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;
import com.kulala.staticsfunc.static_view_change.OInputValidation;

import java.util.List;

import adapter.AdapterUserList;
import common.GlobalContext;
import ctrl.OCtrlAuthorization;
import model.ManagerAuthorization;
import model.loginreg.DataUser;
import view.view4me.set.ClipTitleMeSet;


public class ViewUserList extends LinearLayoutBase {
	private ClipTitleMeSet title_head;
	private ListView		list_users;

	private AdapterUserList	adapter;
	private int				selectedPos;
	private DataUser		selectedUser;
	public static boolean NEED_ADD_NOW = false;
	public ViewUserList(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_app_userlist, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		list_users = (ListView) findViewById(R.id.list_users);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.AUTHORIZATION_USERLIST_RESULTBACK, this);
	}

	@Override
	public void initViews() {
		if(NEED_ADD_NOW){
			NEED_ADD_NOW = false;
			OToastInput.getInstance().showInput(title_head, getResources().getString(R.string.add_contact),getResources().getString(R.string.please_enter_the_contact_number_of_hands), new String[]{OToastInput.PHONE}, "phoneNum", ViewUserList.this);
		}
		OCtrlAuthorization.getInstance().ccmd1210_getuserlist();
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
		// new
		title_head.img_right.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				OToastInput.getInstance().showInput(title_head, getResources().getString(R.string.add_contact), getResources().getString(R.string.please_enter_the_contact_number_of_hands), new String[]{OToastInput.PHONE}, "phoneNum", ViewUserList.this);
			}
		});
		list_users.setOnItemClickListener(new OnItemClickListenerMy() {
			@Override
			public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
				selectedPos = position;
				selectedUser = adapter.getItem(position);
				adapter.setCurrentItem(position);
				OToastButton.getInstance().show(title_head, new String[]{getResources().getString(R.string.delete_the_contact),getResources().getString(R.string.make_a_phone_call)}, "operate", ViewUserList.this);
			}
		});

	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		if (eventName.equals(OEventName.AUTHORIZATION_USERLIST_RESULTBACK)) {
			String error = (String) paramObj;
			if (error.equals(""))
				handleChangeData();
		}
	}
	@Override
	public void callback(String key, Object value) {
		if (key.equals("phoneNum")) {
			String phone = (String) value;
			if (!phone.equals("")) {
				if (OInputValidation.chkInputPhoneNum(phone)) {
					OCtrlAuthorization.getInstance().ccmd1211_findnewuser(phone);
				} else {
					ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.you_enter_the_phone_number_format_error));
				}
			}
		} else if (key.equals("operate")) {
			// "删除联系人","拔打电话"
			String opp = (String) value;
			if (opp.equals(getResources().getString(R.string.delete_the_contact))) {
				new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
						.withTitle("删除联系人")
						.withInfo("你将要删除"+ selectedUser.name + ":" + selectedUser.phoneNum +"确定吗?")
						.withClick(new ToastConfirmNormal.OnButtonClickListener() {
							@Override
							public void onClickConfirm(boolean isClickConfirm) {
								if(isClickConfirm)
									OCtrlAuthorization.getInstance().ccmd1212_deleteuser(selectedUser.userId);
							}
						})
						.show();
			}else if(opp.equals(getResources().getString(R.string.make_a_phone_call))){
				ODispatcher.dispatchEvent(OEventName.CALL_MY_PHONE,selectedUser.phoneNum);
		}
	}
		super.callback(key, value);
	}

	@Override
	protected void onDetachedFromWindow() {
		list_users.setAdapter(null);
		adapter = null;
		ODispatcher.removeEventListener(OEventName.AUTHORIZATION_USERLIST_RESULTBACK, this);
		super.onDetachedFromWindow();
	}

	@Override
	public void invalidateUI(){
		List<DataUser> list = ManagerAuthorization.getInstance().userList;
		if (list == null)
			return;
		adapter = new AdapterUserList(getContext(), list, R.layout.list_item_head_2string);
		list_users.setAdapter(adapter);
	}
	// =====================================================
}
