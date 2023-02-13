package view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.ActivityBase;

import adapter.AdapterShowChat;
import ctrl.OCtrlCommon;
import ctrl.OCtrlSocketMsg;
import model.ManagerChat;
import model.ManagerLoginReg;
import view.view4me.set.ClipTitleMeSet;

//import com.kulala.staticsview.ActivityBase;

public class ActivityChat extends ActivityBase {
	private ClipTitleMeSet title_head;
	private ListView list_names;
	private EditText txt_input;
	private Button btn_confirm;
	public ActivityChat() {
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		list_names = (ListView) findViewById(R.id.list_names);
		txt_input = (EditText) findViewById(R.id.txt_input);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.CHAT_INFO_BACK, this);
	}
	protected void initViews() {
		handleChangeData();
		OCtrlCommon.getInstance().ccmd1309_chatlist();
	}
	public void initEvents() {
		//back
		title_head.img_left.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				ActivityKulalaMain.GestureNeed = false;
				ActivityChat.this.finish();
			}
		});
		//btn_confirm
		btn_confirm.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				String txt = txt_input.getText().toString();
				//这个是走socket，不用回包确认
				if(txt!=null && !("".equals(txt))){
					JsonObject data = new JsonObject();//head
					long fromId = 111;
					long time = ODateTime.getNow();
					if(ManagerLoginReg.getInstance().getCurrentUser() !=null){
						fromId = ManagerLoginReg.getInstance().getCurrentUser().userId;
					}
					data.addProperty("fromId", fromId);
					data.addProperty("createTime", time);
					data.addProperty("content", txt);
					ManagerChat.getInstance().saveChatOne(data);
					handleChangeData();
					OCtrlSocketMsg.getInstance().ccmdSendChat(txt);
					txt_input.setText("");
				}
			}
		});
	}
	public void receiveEvent(String key, Object paramObj) {
		if(OEventName.CHAT_INFO_BACK.equals(key)){
			handleChangeData();
		}
		super.receiveEvent(key,paramObj);
	}
	@Override
	public void callback(String key, Object value) {
		super.callback(key, value);
	}
	@Override
	public void invalidateUI()  {
		AdapterShowChat adapter = new AdapterShowChat(this, ManagerChat.getInstance().ChatList, R.layout.list_item_chat);
		list_names.setAdapter(adapter);
		list_names.setSelection(list_names.getBottom());
	}

	@Override
	protected void popView(int resId) {

	}

}