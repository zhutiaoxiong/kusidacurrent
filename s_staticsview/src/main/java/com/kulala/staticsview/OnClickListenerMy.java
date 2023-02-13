package com.kulala.staticsview;

import android.view.View;

public class OnClickListenerMy implements View.OnClickListener{

	private static long beforeTime = 0;
	@Override
	public void onClick(View v) {
		long time = System.currentTimeMillis();
		if(Math.abs(time-beforeTime)<600L){
			return;
		}
		beforeTime = time;
		onClickNoFast(v);
	}

	public void onClickNoFast(View v){

	}
}
