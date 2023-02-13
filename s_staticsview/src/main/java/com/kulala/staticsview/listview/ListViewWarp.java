package com.kulala.staticsview.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
/**
 * @author Administrator
 * 自适应宽度，高度也会warp
 */
public class ListViewWarp extends ListView {
    int  maxWidth = 0;

	public ListViewWarp(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ListViewWarp(Context context) {
        super(context);
    }
    public ListViewWarp(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle); 
    } 
//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if( maxWidth==0 )maxWidth = meathureWidthByChilds() + getPaddingLeft() + getPaddingRight();
        super.onMeasure(MeasureSpec.makeMeasureSpec(maxWidth,MeasureSpec.AT_MOST),heightMeasureSpec);//注意，这个地方一定是MeasureSpec.UNSPECIFIED
    }
    public int meathureWidthByChilds() {
        int  maxWidth = 0;
        View view     = null;
        for (int i = 0; i < getAdapter().getCount(); i++) {
            view = getAdapter().getView(i, view, this);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            if (view.getMeasuredWidth() > maxWidth){
                maxWidth = view.getMeasuredWidth();
            }
            view = null;
        }
        return maxWidth;
    }
}
