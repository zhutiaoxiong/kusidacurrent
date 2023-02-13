package com.kulala.staticsview.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class ViewPagerParent extends ViewPager {
	private int currentPos;
	public void changeCurrentPos(int pos){
		currentPos = pos;
	}
	public ViewPagerParent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ViewPagerParent(Context context) {
		super(context);
	}
//
	@Override
	public void scrollTo(int x, int y) {
			super.scrollTo(x, y);
	}
	//
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		// 用于拦截，是否传给其它控件,true不传子,只传0 false传子,有滚动
		int thisH = this.getHeight();
		if (currentPos == 0 && e.getY() < thisH * 65 / 100) {
			return false;
		} else {
			return super.onInterceptTouchEvent(e);
		}
		// }
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}

}