package view.view4control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.client.proj.kusida.R;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;

public class ViewPerformControl extends RelativeLayoutBase {
	private Button btn_confirm;
	public boolean isTouch;
	public ViewPerformControl(Context context, AttributeSet attrs) {
		super(context, attrs);//this layout for add and edit
		LayoutInflater.from(context).inflate(R.layout.view_pop_confirm, this, true);
		btn_confirm=(Button)findViewById(R.id.btn_confirm);
		initViews();
		initEvents();
	}
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
	public void initViews() {
//		handleChangeData();
	}
	public void initEvents() {
		btn_confirm.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				isTouch=true;
				ViewPerformControl.this.setVisibility(View.INVISIBLE);
			}
		});
	}
	public void receiveEvent(String eventName, Object paramObj) {

	}
	@Override
	public void callback(String key, Object value) {

		super.callback(key, value);
	}


	@Override
	public void invalidateUI()  {

	}
}
