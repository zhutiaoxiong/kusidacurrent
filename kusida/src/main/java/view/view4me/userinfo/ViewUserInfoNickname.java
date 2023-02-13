package view.view4me.userinfo;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import ctrl.OCtrlRegLogin;
import model.ManagerLoginReg;
import model.loginreg.DataUser;
import view.view4me.set.ClipTitleMeSet;

public class ViewUserInfoNickname extends RelativeLayoutBase{
	private ClipTitleMeSet title_head;
	private TextView      txt_save;
	private EditText      txt_input;
	private ImageView img_delete;
	public ViewUserInfoNickname(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_me_userinfo_nickname, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		txt_save = (TextView) findViewById(R.id.txt_save);
		img_delete = (ImageView) findViewById(R.id.img_delete);
		txt_input = (EditText) findViewById(R.id.txt_input);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.CHANGE_USER_INFO_OK, this);
	}

	@Override
	public void initViews() {
		String username = ManagerLoginReg.getInstance().getCurrentUser().name;
		if(username!=null)txt_input.setText(username);
		String ttt = txt_input.getText().toString();
		if(ttt.length()>0){
			img_delete.setVisibility(VISIBLE);
		}else{
			img_delete.setVisibility(INVISIBLE);
		}
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
				InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(txt_input.getWindowToken(), 0);
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_USERINFO_GOTOVIEW, R.layout.view_me_userinfo);
			}
		});
		img_delete.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				txt_input.setText("");
			}
		});
		txt_input.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				String ttt = txt_input.getText().toString();
				if(ttt.length()>0){
					img_delete.setVisibility(VISIBLE);
				}else{
					img_delete.setVisibility(INVISIBLE);
				}
			}
		});
		txt_save.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				String ttt = txt_input.getText().toString();
				if(ttt.length()>0){
					DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
					user.name = ttt;
					OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
				}else{
					ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.no_input_information));
				}
			}
		});
	}
	@Override
	public void callback(String key,Object obj) {
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		if(eventName.equals(OEventName.CHANGE_USER_INFO_OK)){
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(txt_input.getWindowToken(), 0);
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
	}
	// =====================================================
}
