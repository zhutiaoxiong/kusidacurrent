package view.view4me;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_system.OConver;

import view.view4me.set.ClipTitleMeSet;

public class ViewAboutIntro extends LinearLayoutBase{
	private ClipTitleMeSet title_head;
	private TextView txt_info;
	public ViewAboutIntro(Context context, AttributeSet attrs) {
		super(context, attrs);//this layout for add and edit
		LayoutInflater.from(context).inflate(R.layout.view_me_about_intro, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		txt_info = (TextView) findViewById(R.id.txt_info);
		initViews();
		initEvents();
	}
	public void initViews() {
		txt_info.setMovementMethod(new ScrollingMovementMethod());
		txt_info.setText(OConver.StrToDBC(getResources().getString(R.string.me_about)));
	}
	public void initEvents() {
		//back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_about);
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
	public void invalidateUI(){
	}
	// ==============================================================
}
