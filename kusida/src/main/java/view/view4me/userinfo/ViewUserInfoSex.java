package view.view4me.userinfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import ctrl.OCtrlRegLogin;
import model.ManagerLoginReg;
import model.loginreg.DataUser;
import view.clip.ClipLineBtnTxt;
import view.view4me.set.ClipTitleMeSet;

public class ViewUserInfoSex extends RelativeLayoutBase{
	private ClipTitleMeSet title_head;
	private ClipLineBtnTxt txt_boy,txt_girl;
	public ViewUserInfoSex(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_me_userinfo_sex, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		txt_boy = (ClipLineBtnTxt) findViewById(R.id.txt_boy);
		txt_girl = (ClipLineBtnTxt) findViewById(R.id.txt_girl);
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
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_USERINFO_GOTOVIEW, R.layout.view_me_userinfo);
			}
		});
		txt_boy.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
					DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
					user.sex = 1;
					OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
			}
		});
		txt_girl.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
				user.sex = 2;
				OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
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
		DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
		txt_boy.setRightImgVisible(false);
		txt_girl.setRightImgVisible(false);
		if(user.sex == 1){
			txt_boy.setRightImgVisible(true);
		}else if(user.sex == 2){
			txt_girl.setRightImgVisible(true);
		}
	}
	// =====================================================
}
