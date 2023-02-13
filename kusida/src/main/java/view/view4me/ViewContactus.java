package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_view_change.ImageHttpLoader;

import com.kulala.staticsview.RelativeLayoutBase;

import view.view4me.set.ClipTitleMeSet;

public class ViewContactus extends RelativeLayoutBase {
	private ClipTitleMeSet title_head;
//	private TextView          txt_callme;
//	private ClipLineBtnInptxt txt_online, txt_mail, txt_phone_seles, txt_bolo;
	private ImageView img_qrcode;
	public ViewContactus(Context context, AttributeSet attrs) {
		super(context, attrs);// this layout for add and edit
		LayoutInflater.from(context).inflate(R.layout.view_me_contactus, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
//		txt_callme = (TextView) findViewById(R.id.txt_callme);
//		txt_online = (ClipLineBtnInptxt) findViewById(R.id.txt_online);
//		txt_mail = (ClipLineBtnInptxt) findViewById(R.id.txt_mail);
//		txt_phone_seles = (ClipLineBtnInptxt) findViewById(R.id.txt_phone_seles);
//		txt_bolo = (ClipLineBtnInptxt) findViewById(R.id.txt_bolo);
		img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
		initViews();
		initEvents();
//		txt_online.setVisibility(View.GONE);//在线咨询先清掉
	}
	@Override
	public void initViews() {
		ImageHttpLoader.getInstance().asyncloadImage(getContext(),img_qrcode, R.drawable.img_qrcode_contact_us);
//		if (ManagerCommon.hotLine == null || "".equals(ManagerCommon.hotLine)) {
//			OCtrlCommon.getInstance().ccmd1303_getCommonInfo();
//		} else {
//			txt_callme.setText(ManagerCommon.hotLine);
//			txt_mail.setText(ManagerCommon.email);
//			txt_phone_seles.setText(ManagerCommon.dealerLine);
//		}
	}
	@Override
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_setup);
			}
		});
//		txt_callme.setOnClickListener(new OnClickListenerMy() {
//			@Override
//			public void onClickNoFast(View v) {
////				OToastOMG.getInstance().showConfirm(getContext(), "服务热线", "现在就拔打服务热线吗?", "call", ViewContactus.this);
//			}
//		});
//		txt_online.setOnClickListener(new OnClickListenerMy() {
//			@Override
//			public void onClickNoFast(View v) {
//				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_chat);
//			}
//		});
	}
	@Override
	public void callback(String key, Object value) {
//		if (key.equals("call")) {
//			ODispatcher.dispatchEvent(OEventName.CALL_MY_PHONE, "02089625923");
//		}
		super.callback(key, value);
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
//		if (eventName.equals(OEventName.CHANGE_PASSWORD_RESULTBACK) || eventName.equals(OEventName.CHANGE_PHONENUM_RESULTBACK)
//				|| eventName.equals(OEventName.CHANGE_MAIL_RESULTBACK) || eventName.equals(OEventName.CHANGE_IDCARD_RESULTBACK)) {
//			ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.modify_the_success));
//		}
	}


	@Override
	public void invalidateUI() {
	}
	// ===================================================
}
