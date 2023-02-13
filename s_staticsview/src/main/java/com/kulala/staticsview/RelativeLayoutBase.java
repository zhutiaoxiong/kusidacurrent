package com.kulala.staticsview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.static_interface.OCallBack;

public abstract class RelativeLayoutBase extends RelativeLayout implements OEventObject, OCallBack {
	private MyHandlerlerler handler = new MyHandlerlerler();
	public RelativeLayoutBase(Context context) {
		super(context);
	}
	public RelativeLayoutBase(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public void receiveEvent(String key, Object value) {}
	protected abstract void initViews();
	protected abstract void initEvents();
	protected abstract void invalidateUI();
	@Override
	public void callback(String key,Object value) {}

	@Override
	protected void onDetachedFromWindow() {
		ODispatcher.recycle(this);
		super.onDetachedFromWindow();
	}
	public void handleChangeData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 311;
				handler.sendMessageDelayed(message,200L);
			}
		}).start();
	}
	// ===================================================
	@SuppressLint("HandlerLeak")
	class MyHandlerlerler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 311 :
					invalidateUI();
					break;
			}
		}
	}

}
