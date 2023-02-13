package view.view4me.userinfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.mapapi.OMapInfoWindow;
import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;

import java.util.List;

import adapter.AdapterUserInfoAddressList;
import ctrl.OCtrlRegLogin;
import model.ManagerAddress;
import model.ManagerLoginReg;
import model.address.DataAddress;
import model.loginreg.DataUser;
import view.view4me.set.ClipTitleMeSet;

public class ViewUserInfoAddress extends RelativeLayoutBase{
	private ClipTitleMeSet title_head;
	private TextView      txt_result;
	private ListView      list_city;

	private AdapterUserInfoAddressList adapter;
	public ViewUserInfoAddress(Context context, AttributeSet attrs) {//
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_me_userinfo_address, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		txt_result = (TextView) findViewById(R.id.txt_result);
		list_city = (ListView) findViewById(R.id.list_city);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.ADDRESS_LOAD_OK, this);
		ODispatcher.addEventListener(OEventName.GPS_FIND_SELFPOS_OK, this);
		ODispatcher.addEventListener(OEventName.CHANGE_USER_INFO_OK, this);
	}

	@Override
	public void initViews() {
		List<DataAddress> addressList = ManagerAddress.getInstance().addressList;
		if(addressList == null || addressList.size()==0){
			ManagerAddress.getInstance().loadAddress();
		}else {
			handleChangeData();
		}
		OMapInfoWindow.getCurrentLocation();//加载自身位置
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
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_USERINFO_GOTOVIEW, R.layout.view_me_userinfo);
			}
		});
		list_city.setOnItemClickListener(new OnItemClickListenerMy(){
			@Override
			public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
				ViewUserInfoAddressCity.data = adapter.getItem(position);
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_USERINFO_GOTOVIEW, R.layout.view_me_userinfo_address_city);
				super.onItemClickNofast(parent, view, position, id);
			}
		});
		txt_result.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				Address data = OMapInfoWindow.selfAddress;
				if(data==null || data.province ==null)return;
				DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
				user.location = data.province+" "+data.city;
				OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
			}
		});
	}
	@Override
	public void callback(String key,Object obj) {
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		if(eventName.equals(OEventName.ADDRESS_LOAD_OK)){
			handleChangeData();
		}else if(eventName.equals(OEventName.GPS_FIND_SELFPOS_OK)){
			handleChangeData();
		}else if(eventName.equals(OEventName.CHANGE_USER_INFO_OK)){
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
		//loc
		Address data = OMapInfoWindow.selfAddress;
		if(data!=null){
			if(data.province==null){
				txt_result.setText(getResources().getString(R.string.positioning_can_not));
			}else{
				txt_result.setText(data.province+" "+data.city);
			}
		}
		List<DataAddress> addressList = ManagerAddress.getInstance().addressList;
		if(addressList == null || addressList.size()==0){
			return;
		}else {
			adapter = new AdapterUserInfoAddressList(getContext(),addressList);
			list_city.setAdapter(adapter);
		}
	}
	// =====================================================

}
