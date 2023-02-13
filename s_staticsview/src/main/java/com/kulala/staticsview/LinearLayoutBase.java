package com.kulala.staticsview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.static_interface.OCallBack;

public abstract class LinearLayoutBase extends LinearLayout implements OEventObject, OCallBack {
	protected MyHandlerlerler handler = new MyHandlerlerler();
	public LinearLayoutBase(Context context) {
		super(context);
	}
	public LinearLayoutBase(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {}
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
				handler.sendMessage(message);
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
