package adapter.viewpager;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerChild extends ViewPager {
    public static boolean isTouchMe = false;

    @Override
    public int getCurrentItem() {
        return super.getCurrentItem();
    }

    public ViewPagerChild(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPagerChild(Context context) {
        super(context);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        /* return false;//super.onTouchEvent(arg0); */
        // true自身处理,false返回给上层(int)e.getY()
//		isTouchMe = true;
        return super.onTouchEvent(e);
//        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        // 用于拦截，是否传给其它控件,true不传子 false传子
        isTouchMe = true;
        return true;
//        return super.onInterceptTouchEvent(e);
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