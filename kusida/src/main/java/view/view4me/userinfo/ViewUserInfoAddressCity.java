package view.view4me.userinfo;

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

import adapter.AdapterUserInfoAddressCityList;
import ctrl.OCtrlRegLogin;
import model.ManagerLoginReg;
import model.address.DataAddress;
import model.loginreg.DataUser;
import view.view4me.set.ClipTitleMeSet;

public class ViewUserInfoAddressCity extends RelativeLayoutBase{
	private ClipTitleMeSet title_head;
	private ListView      list_city;

	private AdapterUserInfoAddressCityList adapter;
	public static DataAddress data;
	public ViewUserInfoAddressCity(Context context, AttributeSet attrs) {//
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_me_userinfo_address_city, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		list_city = (ListView) findViewById(R.id.list_city);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.CHANGE_USER_INFO_OK, this);
	}

	@Override
	public void initViews() {
		handleChangeData();
	}
	@Override
	public void initEvents() {
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//会点到下层
			}
		});
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_USERINFO_GOTOVIEW, R.layout.view_me_userinfo_address);
			}
		});
		list_city.setOnItemClickListener(new OnItemClickListenerMy(){
			@Override
			public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
				String       city = adapter.getItem(position);
				DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
				user.location = data.province+" "+city;
				OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
				super.onItemClickNofast(parent, view, position, id);
			}
		});
	}
	@Override
	public void callback(String key,Object obj) {
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		if(eventName.equals(OEventName.CHANGE_USER_INFO_OK)){
			ODispatcher.dispatchEvent(OEventName.ACTIVITY_USERINFO_GOTOVIEW, R.layout.view_me_userinfo);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		ODispatcher.removeEventListener(OEventName.CHANGE_USER_INFO_OK, this);
		super.onDetachedFromWindow();
	}


	@Override
	public void invalidateUI()  {
		if(data == null || data.cityList.size()==0){
			return;
		}else {
			adapter = new AdapterUserInfoAddressCityList(getContext(),data.cityList);
			list_city.setAdapter(adapter);
		}
	}
	// =====================================================

}
