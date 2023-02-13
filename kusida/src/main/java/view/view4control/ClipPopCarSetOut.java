package view.view4control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;

import model.ManagerCarList;
import model.carlist.DataCarInfo;

/**
 * 弹出四警告
 */
public class ClipPopCarSetOut {
	private PopupWindow popContain;//弹出管理
	private View parentView;//本对象显示
	private Context context;

	private RelativeLayout thisView; 
	private View view_background;
	private ImageView sound_Switch;

	// ========================out======================
	private static ClipPopCarSetOut _instance;
	public static ClipPopCarSetOut getInstance() {
		if (_instance == null)
			_instance = new ClipPopCarSetOut();
		return _instance;
	}
	//===================================================
	public void show(View parentView) {
		this.parentView = parentView;
		context = parentView.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		thisView = (RelativeLayout)layoutInflater.inflate(R.layout.clip_car_set_out, null);
		view_background = (View) thisView.findViewById(R.id.view_background);
		sound_Switch=(ImageView) thisView.findViewById(R.id.sound_switch);
		//设置UI
		DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
		if(car!=null){
			if(car.isMute==0){
				sound_Switch.setImageResource(R.drawable.car_set_on);
			}else{
				sound_Switch.setImageResource(R.drawable.car_set_off);
			}
		}
		initViews();
		initEvents();
	}
	public void initViews() {
		popContain = new PopupWindow(thisView);
		popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		popContain.setFocusable(true);
		popContain.setTouchable(true);
		popContain.setOutsideTouchable(true);
//		popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
		popContain.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					exitThis();
					return true;
				}
				return false;
			}
		});
		popContain.showAtLocation(parentView, Gravity.BOTTOM,  0, 0);
	}
	public void initEvents() {
		view_background.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				exitThis();
			}
		});
		sound_Switch.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
				if(car!=null&&car.isActive==1){
//					OCtrlCar.getInstance().ccmd1251_Car_Sound_Control(car.ide,car.isMute==0?1:0);
				}
			}
		});
	}

	private void exitThis(){
		context = null;
		popContain.dismiss();
	}
	public void chageUI(){
		Message message=Message.obtain();
		message.what=110;
		handler.sendMessage(message);
	}
	@SuppressLint("HandlerLeak")
	private Handler handler=new Handler(){
		@Override
		public void handleMessage( Message msg) {
			DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
			if(car!=null){
				if(car.isMute==0){
					sound_Switch.setImageResource(R.drawable.car_set_on);
				}else{
					sound_Switch.setImageResource(R.drawable.car_set_off);
				}
			}
		}
	};
}

