package view.view4me.shake;

import android.view.View;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.toast.ToastTxt;

import common.GlobalContext;

public class OnClickListenerMy3000 implements View.OnClickListener{

	private static long beforeTime = 0;
	@Override
	public void onClick(View v) {
		long time = System.currentTimeMillis();
		if(Math.abs(time-beforeTime)<3000L){
			return;
		}
		beforeTime = time;
		onClickNoFast(v);
	}

	public void onClickNoFast(View v){

	}
}
