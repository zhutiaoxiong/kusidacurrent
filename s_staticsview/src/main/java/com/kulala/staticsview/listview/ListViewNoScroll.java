package com.kulala.staticsview.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ListViewNoScroll extends ListView {
    public ListViewNoScroll(Context context) {
        super(context);
    }
    public ListViewNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListViewNoScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 重新测量
     * return changeH
     */
    public static int setListViewHeightBasedOnChildren(ListView listView,int changeH) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)return 0;
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
            Log.e("meatureH",i+" changeH:"+changeH+" preHeight:"+listView.getHeight()+" listItemH:"+listItem.getMeasuredHeight());
        }
        int preHeight = listView.getHeight();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+changeH;
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
//        listView.postInvalidate();
        Log.e("meatureH","nowH:"+params.height);
        return params.height - preHeight;
    }
}
