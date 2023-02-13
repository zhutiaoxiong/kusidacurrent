package com.kulala.staticsview.static_interface;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnItemClickListenerMy implements OnItemClickListener{

	private static long beforeTime = 0;
	public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id){
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		long time = System.currentTimeMillis();
		if(Math.abs(time-beforeTime)<600L){
//			OToastOMG.getInstance().showText("请不要频繁点击！");
		}
		beforeTime = time;
		onItemClickNofast(parent,view,position,id);
	}
}
