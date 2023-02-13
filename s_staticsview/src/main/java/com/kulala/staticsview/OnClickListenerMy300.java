package com.kulala.staticsview;

import android.view.View;

public class OnClickListenerMy300 implements View.OnClickListener{

	private static long beforeTime = 0;
	@Override
	public void onClick(View v) {
		long time = System.currentTimeMillis();
		if(Math.abs(time-beforeTime)<300L){
//			Toast.makeText(v.getContext(),"請",Toast.LENGTH_SHORT).show();
			return;
		}
		beforeTime = time;
		onClickNoFast(v);
	}

	public void onClickNoFast(View v){

	}
}
