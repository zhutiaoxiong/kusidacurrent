package view.view4control;

import android.view.View;

import com.kulala.staticsfunc.static_system.ODateTime;


public class OnClickListenerControl implements View.OnClickListener{

	private static long beforeTime = 0;
	@Override
	public void onClick(View v) {
		long time = ODateTime.getNow();
		if(Math.abs(time-beforeTime)<1000){
			return;
		}
		beforeTime = time;
		onClickNoFast(v);
	}

	public void onClickNoFast(View v){
		
	}
}
