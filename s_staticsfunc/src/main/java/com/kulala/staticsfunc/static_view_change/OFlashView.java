package com.kulala.staticsfunc.static_view_change;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OFlashView {
	private static ScheduledExecutorService scheduledExecutorService;//
	private static HashSet<View> viewList;
	
	private static OFlashView _instance;
	public static OFlashView getInstance() {
		if (_instance == null) {
			_instance = new OFlashView();
		}
		return _instance;
	}
	public OFlashView(){
		_instance = this;
		if (scheduledExecutorService != null && scheduledExecutorService.isShutdown() == false)
			scheduledExecutorService.shutdown();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// ��Activity��ʾ������ÿ3�����л�һ��ͼƬ��ʾ initialDelay����ʼ����ʱ3,3
		scheduledExecutorService.scheduleAtFixedRate(new FlickTask(), 1, 10, TimeUnit.SECONDS);
	}

	public void startFlickNew(View v){
		if(viewList == null)viewList = new HashSet<View>();
		boolean haveView = false;
		Iterator<View> it = viewList.iterator();
		while(it.hasNext()){
			View vv = it.next();
			if(vv == v){
				haveView = true;
			}
		}
		if(!haveView){
			viewList.add(v);
		}
		if (scheduledExecutorService != null && scheduledExecutorService.isShutdown() == false)
			scheduledExecutorService.shutdown();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// ��Activity��ʾ������ÿ3�����л�һ��ͼƬ��ʾ initialDelay����ʼ����ʱ3,3
		scheduledExecutorService.scheduleAtFixedRate(new FlickTask(), 1, 10, TimeUnit.SECONDS);
	}
	public void stopFlickNew(View v){
		if(viewList == null || viewList.size()==0){
			if (scheduledExecutorService != null && scheduledExecutorService.isShutdown() == false)
				scheduledExecutorService.shutdown();
			return;
		}
		Iterator<View> it = viewList.iterator();
		while(it.hasNext()){
			View vv = it.next();
			if(vv == v){
				v.clearAnimation();
				viewList.remove(v);
			}
		}
	}
	private class FlickTask implements Runnable {
		public void run() {
	        Animation alphaAnimation = new AlphaAnimation( 1, 0 );
	        alphaAnimation.setDuration(300);
	        alphaAnimation.setInterpolator( new LinearInterpolator());
	        alphaAnimation.setRepeatCount( 5 );//�ظ����Animation.INFINITE
	        alphaAnimation.setRepeatMode( Animation.REVERSE );//RESTART��ʾ��ͷ��ʼ��REVERSE��ʾ��ĩβ����
			Iterator<View> it = viewList.iterator();
			while(it.hasNext()){
				View vv = it.next();
		        vv.startAnimation( alphaAnimation );
			}
			scheduledExecutorService.shutdown();
		}
	}

}
