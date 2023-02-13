package view.view4me.nfcmoudle;

import android.view.View;
import android.widget.Toast;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import common.GlobalContext;

public class OnClickListenerMy5000 implements View.OnClickListener{

	private static long beforeTime = 0;
	@Override
	public void onClick(View v) {
		long time = System.currentTimeMillis();
		if(Math.abs(time-beforeTime)<5000L){
			ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请5秒之后重试");
			return;
		}
		beforeTime = time;
		onClickNoFast(v);
	}

	public void onClickNoFast(View v){

	}
}
