package view.view4me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.OToastInput;

import common.GlobalContext;
import ctrl.OCtrlRegLogin;
import model.ManagerAnswer;
import model.ManagerLoginReg;
import model.loginreg.DataUser;
import model.safety.DataSafeTy;
import view.clip.ClipLineBtnInptxt;
import view.clip.gesture.GestureVerityPage;
import view.find.BasicParamCheckPassWord;
import view.view4me.set.ClipTitleMeSet;


public class ViewSafety extends LinearLayoutBase{
	private ClipTitleMeSet title_head;
	private ClipLineBtnInptxt txt_gesture_password,txt_password, txt_phone, txt_mail,txt_anquanwenti;
	public  static boolean  isfirst=true;
	private MyHandler handler = new MyHandler();
	public ViewSafety(Context context, AttributeSet attrs) {
		super(context, attrs);// this layout for add and edit
		LayoutInflater.from(context).inflate(R.layout.view_me_safety, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		txt_gesture_password = (ClipLineBtnInptxt) findViewById(R.id.txt_gesture_password);
		txt_password = (ClipLineBtnInptxt) findViewById(R.id.txt_password);
		txt_phone = (ClipLineBtnInptxt) findViewById(R.id.txt_phone);
		txt_mail = (ClipLineBtnInptxt) findViewById(R.id.txt_mail);
		txt_anquanwenti=(ClipLineBtnInptxt) findViewById(R.id.txt_anquanwenti);
//		txt_idcard = (ClipLineBtnInptxt) findViewById(R.id.txt_idcard);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.CHANGE_PASSWORD_RESULTBACK, this);
		ODispatcher.addEventListener(OEventName.CHANGE_PHONENUM_RESULTBACK, this);
		ODispatcher.addEventListener(OEventName.CHANGE_MAIL_RESULTBACK, this);
		ODispatcher.addEventListener(OEventName.CHANGE_IDCARD_RESULTBACK, this);
		ODispatcher.addEventListener(OEventName.CHECK_PASSWORD_RESULTBACK,this);
	}
	@Override
	public void initViews() {
		handleChangeData();
		handleSwitchRedPoint();
	}
	@Override
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_setup);
			}
		});
		txt_gesture_password.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				String name = txt_gesture_password.getText().toString().trim();
				if(name.equals(getContext().getResources().getString(R.string.click_modify))){
					GestureVerityPage.isNeedNextToEditGesture = true;
					GestureVerityPage.fromPage = "viewSafety";
					ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.clip_gesture_verify);
				}else if(name.equals(getContext().getResources().getString(R.string.click_settings))){
					GestureVerityPage.isNeedNextToEditGesture = false;
					ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.clip_gesture_edit);
				}
			}
		});
		//2：修改手机号，3：重设密码，4：修改邮箱，5：修改安全问题
		txt_password.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
//			OToastInput.getInstance().showInput(title_head,"修改密码", "", new String[]{OToastInput.PASS,OToastInput.NEW_PASS,OToastInput.REPT_PASS},"password", ViewSafety.this);
				DataSafeTy.from=3;
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_safety_resetitem);
					}
		});
		txt_phone.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
//			OToastInput.getInstance().showInput(title_head,"修改手机号码","", new String[]{OToastInput.PASS,OToastInput.NEW_PHONE,OToastInput.REPT_PHONE},"phone", ViewSafety.this);
				DataSafeTy.from=2;
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_safety_resetitem);
			}
		});
		txt_mail.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				DataSafeTy.from=4;
				if(ManagerLoginReg.getInstance().getCurrentUser().email.equals("")||ManagerLoginReg.getInstance().getCurrentUser().email==null){
//				OToastInput.getInstance().showInput(title_head,"修改E-mail","", new String[]{OToastInput.PASS,OToastInput.NEW_EMAIL,OToastInput.REPT_EMAIL},"mail", ViewSafety.this);
//				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_reset_password_txt_aderess);
					OToastInput.getInstance().showInput(title_head,getResources().getString(R.string.verify_password),"", new String[]{OToastInput.PASS},"yanzhengmima",ViewSafety.this);
				}else{
					ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_safety_resetitem);
				}
			}
		});
//		txt_idcard.setOnClickListener(new OnClickListenerMy(){
//			@Override
//			public void onClickNoFast(View view) {
//				OToastOMG.getInstance().showInput(getContext(),"修改身份证号","", new String[]{OToastOMG.PASS,OToastOMG.IDCARD,OToastOMG.REPT_IDCARD},"idcard", ViewSafety.this);
//			}
//		});
		txt_anquanwenti.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				DataSafeTy.from=5;
				if(ManagerLoginReg.getInstance().getCurrentUser().hasSecretQuestion==0){
					OToastInput.getInstance().showInput(title_head,getResources().getString(R.string.verify_password),"", new String[]{OToastInput.PASS},"yanzhengmima",ViewSafety.this);
				}else{
					ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_safety_resetitem);
				}
//				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_reset_password);
			}
		});
	}
	private String checkRep(JsonObject obj,String toastV,String toastVr){
		String newv = OJsonGet.getString(obj, toastV);
		String repv = OJsonGet.getString(obj, toastVr);
		if (!newv.equals(repv) && !newv.equals("")) {
			ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.two_information_input_please_input_again));
			return "";
		}
		return newv;
	}
	@Override
	public void callback(String key, Object value) {
		try {
			JsonObject obj = (JsonObject)value;
			String pass = OJsonGet.getString(obj, OToastInput.PASS);
			String repchk = "";
			if (key.equals("password")) {
				repchk = checkRep(obj,OToastInput.NEW_PASS,OToastInput.REPT_PASS);
				if(!repchk.equals(""))OCtrlRegLogin.getInstance().ccmd1108_changePassword(pass, repchk);
			} else if (key.equals("phone")) {
				repchk = checkRep(obj,OToastInput.NEW_PHONE,OToastInput.REPT_PHONE);
				OCtrlRegLogin.getInstance().ccmd1113_changePhoneNum(pass,"123465", repchk);
			} else if (key.equals("mail")) {
				repchk =checkRep(obj,OToastInput.NEW_EMAIL,OToastInput.REPT_EMAIL);
				OCtrlRegLogin.getInstance().ccmd1112_changeMail(pass, repchk);
			}else if(key.equals("yanzhengmima")){
				//OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass,5);
				BasicParamCheckPassWord.isFindMain=3;
				OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass,DataSafeTy.from);
					//OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass,4);
			}
//			else if (key.equals("idcard")) {
//				repchk =checkRep(obj,OToastOMG.IDCARD,OToastOMG.REPT_IDCARD);
//				OCtrlRegLogin.getInstance().ccmd1111_changeIdcard(pass,repchk );
//			}
		}catch (Exception e){
			return;
		}
		super.callback(key, value);
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		if ( eventName.equals(OEventName.CHANGE_PHONENUM_RESULTBACK)
				|| eventName.equals(OEventName.CHANGE_MAIL_RESULTBACK) || eventName.equals(OEventName.CHANGE_IDCARD_RESULTBACK)) {
			ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.modify_the_success));
		}else if(eventName.equals(OEventName.CHECK_PASSWORD_RESULTBACK)) {
//			if(whoSend.equals("txt_anquanwenti")){
//				ViewAnswerSet.isPhoneComeHere=R.layout.view_me_safety;
			if(BasicParamCheckPassWord.isFindMain==3){
				boolean isAllright= (boolean) paramObj;
				if(isAllright){
					if(DataSafeTy.from==4){
						ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_safety_reset_aderess);

					}else if(DataSafeTy.from==5){
						ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_safety_reset_qustion);
					}
				}
			}
//			}else if(whoSend.equals("txt_mail")){
//				ViewInputTwoPasswordTxtAderess.phonecomehere=R.layout.view_me_safety;

//			}
		}else if(eventName.equals(OEventName.CHANGE_MAIL_RESULTBACK)){
			handleSwitchRedPoint();
		}else if(eventName.equals(OEventName.SUBMMIT_PASSWORD_PROBLEM)){
			handleSwitchRedPoint();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		ODispatcher.removeEventListener(OEventName.CHANGE_PASSWORD_RESULTBACK, this);
		ODispatcher.removeEventListener(OEventName.CHANGE_PHONENUM_RESULTBACK, this);
		ODispatcher.removeEventListener(OEventName.CHANGE_IDCARD_RESULTBACK, this);
		ODispatcher.removeEventListener(OEventName.CHECK_PASSWORD_RESULTBACK,this);
		ODispatcher.removeEventListener(OEventName.CHANGE_MAIL_RESULTBACK,this);
		ODispatcher.removeEventListener(OEventName.SUBMMIT_PASSWORD_PROBLEM,this);
		super.onDetachedFromWindow();
	}

	@Override
	public void invalidateUI()  {
		DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
		if(!user.phoneNum.equals(""))txt_phone.setText(ManagerAnswer.getInstance().getPhoneNumStar(user.phoneNum));
//		if(!user.email.equals(""))txt_mail.setText(user.email);
//		if(!user.idCard.equals(""))txt_idcard.setText(user.idCard);

		//手势密码的文字显示
		String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenGesture");
		int isOpenGesture = ODBHelper.queryResult2Integer(result,0);
		if (isOpenGesture == 1) {
			txt_gesture_password.setText(getContext().getResources().getString(R.string.click_modify));
		} else {
			txt_gesture_password.setText(getContext().getResources().getString(R.string.click_settings));
		}
	}
	public void handleSwitchRedPoint() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 503;
				handler.sendMessage(message);
			}
		}).start();
	}
	// ===================================================
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 503 :
					DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
					if(TextUtils.isEmpty(user.email)){
						txt_mail.setText(getContext().getResources().getString(R.string.click_settings));
						txt_mail.setRedPointVisible(true);
					}else{
						txt_mail.setText(getContext().getResources().getString(R.string.click_modify));
						txt_mail.setRedPointVisible(false);
					}
					if(user.hasSecretQuestion == 0){
						txt_anquanwenti.setText(getContext().getResources().getString(R.string.click_settings));

						txt_anquanwenti.setRedPointVisible(true);
					}else{
						txt_anquanwenti.setText(getContext().getResources().getString(R.string.click_modify));
						txt_anquanwenti.setRedPointVisible(false);
					}
					break;
			}
		}
	}
	// ===================================================
}
