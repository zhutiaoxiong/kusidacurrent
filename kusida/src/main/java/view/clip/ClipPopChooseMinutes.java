package view.clip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_assistant.ButtonBgStyle;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OCallBack;

import common.GlobalContext;
import model.ManagerCarList;
import model.ManagerSkins;
import model.carlist.DataCarInfo;
import model.skin.DataTempSetup;
import view.EquipmentManager;
import view.clip.child.ClipStartCarMinutes;

import static model.ManagerSkins.DEFAULT_NAME_TEMP;
import static model.ManagerSkins.TRANSPARENT;

/**
 * 时间选择器 启动车
 */
public class ClipPopChooseMinutes {
	private PopupWindow					popContain; // 弹出管理
	private View						parentView; // 本对象显示
	private Context						context;

	private RelativeLayout				thisView;
	private TextView					txt_cancel, txt_confirm,txt1,txt3,txt4;//,txt_default_time
	private ClipStartCarMinutes			clip_min;
	private String						mark;						// 选择标记
	private OCallBack					callback;
	private ImageView img_bg;

	// ========================out======================
	private static ClipPopChooseMinutes	_instance;
	public static ClipPopChooseMinutes getInstance() {
		if (_instance == null)
			_instance = new ClipPopChooseMinutes();
		return _instance;
	}
	// ===================================================
	private Drawable getImage(String url, String name){
		if(ManagerSkins.TRANSPARENT.equals(name)){
			return ManagerSkins.getInstance().getPngImage(TRANSPARENT);
		}
		return ManagerSkins.getInstance().getPngImage(ManagerSkins.getCacheKey(false,(TextUtils.isEmpty(url) ? DEFAULT_NAME_TEMP : url),name));
	}
	public void show(View parentView, String mark, OCallBack callback) {
		this.mark = mark;
		this.callback = callback;
		this.parentView = parentView;
		context = parentView.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_pop_choose_minutes, null);
		txt_cancel = (TextView) thisView.findViewById(R.id.txt_cancel);
		txt_confirm = (TextView) thisView.findViewById(R.id.txt_confirm);
		txt3 = (TextView) thisView.findViewById(R.id.txt3);
		txt1 = (TextView) thisView.findViewById(R.id.txt1);
		txt4= (TextView) thisView.findViewById(R.id.txt4);
		if(EquipmentManager.isMinJiaQiang()){
			txt1.setText("点火时长");
			txt3.setText("可滑动选择启动马达的点火时长");
			txt4.setTextColor(Color.parseColor("#000000"));
			txt4.setTextSize(14f);
			txt4.setText("您将要启动发动机，请确保车辆处于安全且通风良好的位置。维修中和档位在非停车档时切勿操作。");
		}else{
			txt1.setText("设置启动时长");
			txt3.setText("您将要启动发动机，请确保车辆处于安全且通风良好的位置。维修中和档位在非停车档时切勿操作。");
			txt4.setTextColor(Color.parseColor("#000000"));
			txt4.setTextSize(14f);
			txt4.setText("本次启动后车辆处于防盗状态，开门或踩刹车会熄火，需要开锁后才能正常行驶。");
		}
		img_bg = (ImageView) thisView.findViewById(R.id.img_bg);
		clip_min = (ClipStartCarMinutes) thisView.findViewById(R.id.clip_minnn);
		String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("controlCarTime");
		int chooseTime = ODBHelper.queryResult2Integer(result,0);
		if(chooseTime>0 && chooseTime<100){
//			txt_default_time.setText("默认启动时长为"+chooseTime+"分钟，可滑动按扭自行设置");
			clip_min.setChooseNum(chooseTime);
			clip_min.postInvalidate();
		}
		DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
		String url = "";//使用默认的
		if(car != null && car.skinTemplateInfo!=null){//使用网络的
			url = car.getCarTemplate().url;
		}
		DataTempSetup panelSkin = ManagerSkins.getInstance().getTempSetup(ManagerSkins.getTempZipFileName(url));
		if(panelSkin!=null){
			if(panelSkin.control_pop_confirm==0){
				setBgAndTextColor("#00000000","#000000");
				img_bg.setImageResource(R.drawable.control_bg_confirm_old);
				clip_min.setLineColor("#000000");
			}else if(panelSkin.control_pop_confirm==1){
//				img_bg.setImageDrawable(getImage(url,"control_bg_confirm"));
				img_bg.setImageResource(R.drawable.shefangbg);
				clip_min.setLineColor(panelSkin.colorPopNomal);
				int [][]states=new int[2][];
				states[0] = new int[] { android.R.attr.state_pressed };
				states[1] = new int[] {};
				int [] colors=new int[]{Color.parseColor(panelSkin.colorPopPress),Color.parseColor(panelSkin.colorPopNomal)};
				ColorStateList selector = new ColorStateList(states,colors);
				//默认图片
				txt_confirm.setTextColor(selector);
				txt_cancel.setTextColor(Color.parseColor("#000000"));
				txt3.setTextColor(Color.parseColor("#000000"));
				txt1.setTextColor(Color.parseColor("#000000"));
				StateListDrawable btnCancleBg = ButtonBgStyle.createDrawableSelector(context, getImage(url,"img_bg_confirm_btn_normal_l"),getImage(url,"img_bg_confirm_btn_press_l"),getImage(url,"img_bg_confirm_btn_press_l"));
				txt_cancel.setBackground(btnCancleBg);
				StateListDrawable btnConfimBg = ButtonBgStyle.createDrawableSelector(context, getImage(url,"img_bg_confirm_btn_normal_r"),getImage(url,"img_bg_confirm_btn_press_r"),getImage(url,"img_bg_confirm_btn_press_r"));
				txt_confirm.setBackground(btnConfimBg);
			}
		}
		initViews();
		initEvents();
	}
	public void setBgAndTextColor(String bgColor,String textColor){
		txt_confirm.setBackgroundColor(Color.parseColor(bgColor));
		txt_cancel.setBackgroundColor(Color.parseColor(bgColor));
		txt_confirm.setTextColor(Color.parseColor(textColor));
		txt_cancel.setTextColor(Color.parseColor(textColor));
		txt3.setTextColor(Color.parseColor(textColor));
		txt1.setTextColor(Color.parseColor(textColor));
	}
	public void initViews() {
		popContain = new PopupWindow(thisView);
		popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		popContain.setFocusable(true);
		popContain.setTouchable(true);
		popContain.setOutsideTouchable(true);
		// popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
		popContain.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					exitthis();
					return true;
				}
				return false;
			}
		});
		popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	}
	public void initEvents() {
		txt_confirm.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				int level =  clip_min.getChooseNum()/5-1;
				ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("controlCarTime", String.valueOf(clip_min.getChooseNum()));
				if(callback!=null){
					callback.callback(mark, level);
				}
				exitthis();
			}
		});
		txt_cancel.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				exitthis();
			}
		});
	}
	private void exitthis(){
		context = null;
		callback = null;
		popContain.dismiss();

	}
}
