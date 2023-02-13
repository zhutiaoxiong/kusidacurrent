package view.clip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.ODateTime;

import common.timetick.OTimeSchedule;
import model.ManagerWarnings;
import model.carcontrol.DataWarnings;
public class ClipMoveMessage extends RelativeLayoutBase {
	private ImageView	img_alarm;
	private TextView	txt_alarm;
	private int			count	= 0;
	private MyHandler	handler	= new MyHandler();
	public ClipMoveMessage(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_pop_message_move, this, true);
		img_alarm = (ImageView) findViewById(R.id.img_alarm);
		txt_alarm = (TextView) findViewById(R.id.txt_alarm);
		this.setVisibility(View.INVISIBLE);
		initViews();
		initEvents();
		OTimeSchedule.getInstance().init();
		ODispatcher.addEventListener(OEventName.TIME_TICK_SECOND, this);
	}

	@Override
	protected void onDetachedFromWindow() {
		ODispatcher.removeEventListener(OEventName.TIME_TICK_SECOND, this);
		this.setVisibility(View.INVISIBLE);
		super.onDetachedFromWindow();
	}

	public void initViews() {
	}
	public void initEvents() {
	}

	@Override
	protected void invalidateUI() {

	}


	private boolean isShowing = false;
	private boolean canShowNext = true;
	@Override
	public void receiveEvent(String key, Object paramObj) {
		if (key.equals(OEventName.TIME_TICK_SECOND)) {
			DataWarnings warnings = ManagerWarnings.getInstance().getMoveingNewWarning();
			if (img_alarm == null || warnings == null)return;
			count++;
			if(canShowNext && !isShowing){
				isShowing = true;
				canShowNext = false;
				count = 0;
				handleShowMessage(warnings);
			}else if(count == 6){
				isShowing = false;
				handleHideMessage(warnings);
			}
		}
	}

	@Override
	public void callback(String key, Object value) {
//		if(key.equals("cancelArea")){
//			ManagerGps.areaMeter = 0;
//			OCtrlGps.getInstance().ccmd1214_setArea(ManagerCarList.getInstance().getCurrentCar().ide, 0, 0);
//		}
	}
	// ==============================================================
	public void handleShowMessage(DataWarnings dat) {
		Message message = new Message();
		message.what = 1;
		message.obj = dat;
		handler.sendMessage(message);
	}
	public void handleHideMessage(DataWarnings dat) {
		Message message = new Message();
		message.what = 2;
		message.obj = dat;
		handler.sendMessage(message);
	}
	// ===================================================
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1 :
					DataWarnings dat = (DataWarnings)msg.obj;
					if(dat == null ||img_alarm==null ||txt_alarm ==null)return;
					img_alarm.setImageResource(dat.getResId());
					txt_alarm.setText(dat.content + "  " + ODateTime.time2StringHHmm(dat.createTime));
					ClipMoveMessage.this.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.window_enter_animation));
					ClipMoveMessage.this.setVisibility(View.VISIBLE);
					//Activity已有提醒
//					if(dat.alertId == 17){
//							OToastOMG.getInstance().showConfirm(getContext(),"车辆已超围栏范围", "车辆已超围栏范围,是否现在取消围栏?",  "cancelArea",ClipMoveMessage.this);
//					}
					break;
				case 2 :
					DataWarnings datt = (DataWarnings)msg.obj;
					if(datt!=null)datt.isNew = false;
//					ClipMoveMessage.this.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.window_enter_animation));
					ClipMoveMessage.this.setVisibility(View.INVISIBLE);
					canShowNext = true;
					break;
			}
		}
	}
}
