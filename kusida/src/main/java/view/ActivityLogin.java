package view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.ActivityBase;
import com.wearkulala.www.wearfunc.WearLinkServicePhone;
import com.wearkulala.www.wearfunc.WearReg;

import common.pinyinzhuanhuan.KeyBoard;
import ctrl.OCtrlBaseHttp;
import model.ManagerCommon;
import view.clip.gesture.GestureVerityPage;
import view.loginreg.ViewAnswerGetForVerfication;
import view.loginreg.ViewFinshInfo;
import view.loginreg.ViewLogin;
import view.loginreg.ViewLoginRegResetPassWordByPhone;
import view.loginreg.ViewLoginRegSubmmitPassWord;
import view.loginreg.ViewLoginRegUserName;
import view.loginreg.ViewPassfind;
import view.loginreg.ViewReg;
import view.loginreg.ViewResetPassWord;
import view.loginreg.ViewVerificationCodeHow;

public class ActivityLogin extends ActivityBase implements OnLayoutChangeListener {
	private ScrollView layer_scroll;
	public static int currentPageRedId;
	private int lastViewResId = 0;
	private  String content;
	private LinearLayout lin_shows;
	private TextView btn_confirm;
	private TextView txt_text;
	private View outview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginreg);
		lin_shows = (LinearLayout) findViewById(R.id.lin_shows);
		Intent intent = getIntent();
		//5). 通过intent读取额外数据
		content = intent.getStringExtra("MESSAGE");
		outview= findViewById(R.id.outview);
		txt_text=(TextView) findViewById(R.id.txt_text);
		if(!TextUtils.isEmpty(content)){
			lin_shows.setVisibility(View.VISIBLE);
			outview.setVisibility(View.VISIBLE);
			txt_text.setText(content);
		}
		btn_confirm=(TextView) findViewById(R.id.btn_confirm);
		layer_scroll = (ScrollView) findViewById(R.id.layer_scroll);
		layer_scroll.addOnLayoutChangeListener(this);
		btn_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lin_shows.setVisibility(View.GONE);
				outview.setVisibility(View.GONE);
			}
		});
		outview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lin_shows.setVisibility(View.GONE);
				outview.setVisibility(View.GONE);
			}
		});
		initViews();
		initEvents();
		OCtrlBaseHttp.getInstance();
		ODispatcher.addEventListener(OEventName.ACTIVITY_LOGIN_GOTOVIEW, this);
		ODispatcher.addEventListener(OEventName.LANGUAGE_CHANGE,this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		WearReg.setIsCanGetLoginState(true);
		if(WearReg.isGooglePlayServiceAvailable(this)) {
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
				Intent intent1 = new Intent(ActivityLogin.this, WearLinkServicePhone.class);
				startService(intent1);
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	protected void initViews() {
		handlePopView(R.layout.view_loginreg_login);
	}

	@Override
	public void initEvents() {
	}
	private int	screenHeight;
	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		if (oldBottom > screenHeight)
			screenHeight = oldBottom;
		int keyHeight = screenHeight / 3;
		// 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
		if (oldBottom != 0 && bottom != 0 && (screenHeight - bottom > keyHeight)) {
			layer_scroll.scrollTo(0, 300);
		}else if (oldBottom != 0 && bottom != 0 && (bottom - screenHeight > keyHeight)) {
			layer_scroll.scrollTo(0, 0);
		}
	}

	long preBackTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK  && event.getRepeatCount() == 0) {
			long now = System.currentTimeMillis();
			if (now - preBackTime < 2000L) {
				if(lastViewResId == R.layout.view_loginreg_login) {
					ManagerCommon.getInstance().ExitAndFinish();
					finish();
					System.exit(0);
				}else{
					ODispatcher.dispatchEvent(OEventName.MAIN_CLICK_BACK);
				}
			} else {
				Toast.makeText(getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
				preBackTime = now;
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void popView(int resId) {
		lastViewResId = resId;
		layer_scroll.removeAllViews();
		switch (resId) {
			case R.layout.view_loginreg_input_two_password:
				currentPageRedId = R.layout.view_loginreg_input_two_password;
				layer_scroll.addView(new ViewLoginRegSubmmitPassWord(this, null));
				break;
			case R.layout.view_reset_password_logreg:
				currentPageRedId = R.layout.view_reset_password_logreg;
				layer_scroll.addView(new ViewResetPassWord(this, null));
				break;
			case R.layout.view_answer_get_for_login:
				currentPageRedId = R.layout.view_answer_get_for_login;
				layer_scroll.addView(new ViewAnswerGetForVerfication(this, null));
				break;
			case R.layout.view_loginreg_username:
				currentPageRedId = R.layout.view_loginreg_username;
				layer_scroll.addView(new ViewLoginRegUserName(this, null));
				break;
			case R.layout.view_loginreg_phone_resetpassword:
				currentPageRedId = R.layout.view_loginreg_phone_resetpassword;
				layer_scroll.addView(new ViewLoginRegResetPassWordByPhone(this, null));
				break;
			case R.layout.view_loginreg_emailadress_findpassword:
				currentPageRedId = R.layout.view_loginreg_emailadress_findpassword;
				layer_scroll.addView(new ViewLoginRegUserName(this, null));
				break;
			case R.layout.view_loginreg_login :
				currentPageRedId = R.layout.view_loginreg_login;
				layer_scroll.addView(new ViewLogin(this, null));
				break;
			case R.layout.view_loginreg_reg :
				currentPageRedId = R.layout.view_loginreg_reg;
				layer_scroll.addView(new ViewReg(this, null));
				break;
			case R.layout.view_loginreg_passfind :
				currentPageRedId = R.layout.view_loginreg_passfind;
				layer_scroll.addView(new ViewPassfind(this, null));
				break;
			case R.layout.view_loginreg_finshinfo :
				currentPageRedId = R.layout.view_loginreg_finshinfo;
				layer_scroll.addView(new ViewFinshInfo(this, null));
				break;
			case R.layout.view_loginreg_verificationcode_how :
				currentPageRedId = R.layout.view_loginreg_verificationcode_how;
				layer_scroll.addView(new ViewVerificationCodeHow(this, null));
				break;
			case R.layout.clip_gesture_verify :
				currentPageRedId = R.layout.clip_gesture_verify;
				layer_scroll.addView(new GestureVerityPage(this, null));
				break;
			case 0:// 回主面板
				ActivityKulalaMain.GestureNeed = false;
                Intent intent = new Intent();
                intent.setClass(ActivityLogin.this, ActivityKulalaMain.class);
                ActivityLogin.this.startActivity(intent);
				currentPageRedId = 0;
				ActivityLogin.this.finish();
				break;
		}
	}

	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		if (eventName.equals(OEventName.ACTIVITY_LOGIN_GOTOVIEW)) {
			KeyBoard.hintKb();
			int resid = (Integer) paramObj;
			handlePopView(resid);
		}else if(eventName.equals(OEventName.LANGUAGE_CHANGE)){
			handleChangeData();
		}
		super.receiveEvent(eventName,paramObj);
	}
	@Override
	public void callback(String key, Object value) {
		super.callback(key, value);
	}

	@Override
	protected void onDestroy() {
		ODispatcher.removeEventListener(OEventName.ACTIVITY_LOGIN_GOTOVIEW, this);
		ODispatcher.removeEventListener(OEventName.LANGUAGE_CHANGE, this);
		super.onDestroy();
	}

	@Override
	public void invalidateUI() {
	}

}
