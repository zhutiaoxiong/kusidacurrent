package com.kulala.staticsfunc.static_view_change;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

public class ORecycle {

	/**
	 * 回收继承自AbsListView的类,如GridView,ListView等
	 * @param absView 
	 * @param recycleIds 要清理的Id的集合;
	 */
	public static void recycleAbsList(AbsListView absView,int imageResId){
		if(absView==null) return;
		synchronized(absView){
			//回收当前显示的区域
			for (int index = absView.getFirstVisiblePosition(); index <= absView.getLastVisiblePosition(); index++) {
				//获取每一个显示区域的具体ItemView
				ViewGroup views = (ViewGroup) absView.getAdapter().getView(index, null, absView);
				for(int count=0;count<absView.getAdapter().getCount();count++){
					recycleImageView(views.findViewById(imageResId));
				}
			}
		}
	}
	/**
	 * 回收继承自AbsListView的类,如GridView,ListView等
	 * @param absView 
	 * @param recycleIds 要清理的Id的集合;
	 */
	public static void recycleViewGroup(ViewGroup layout,int[]recycleIds){
		if(layout==null) return;
		synchronized(layout){
			for (int i = 0; i < layout.getChildCount(); i++) {
	            View subView = layout.getChildAt(i);
	            if (subView instanceof ViewGroup) {
	            	for(int count=0;count<recycleIds.length;count++){
						recycleImageView(subView.findViewById(recycleIds[count]));
					}
	            } else {
	                if (subView instanceof ImageView) {
	                	recycleImageView((ImageView)subView);
	                }
	            }
			}
		}
	}
	/**
	 * 回收ImageView占用的图像内存;
	 * @param view
	 */
	public static void recycleImageView(View view){
		if(view==null) return;
		if(view instanceof ImageView){
			Drawable drawable=((ImageView) view).getDrawable();
			if(drawable instanceof BitmapDrawable){
				Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
				if (bmp != null && !bmp.isRecycled()){
					((ImageView) view).setImageBitmap(null);
					bmp.recycle();
					bmp=null;
				}
			}
			((ImageView) view).setImageDrawable(null);
		}
	}
}
