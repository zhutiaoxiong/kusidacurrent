package com.kulala.staticsview.listview;

/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓━━━━┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　　┃　＞　　　＜　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃...　⌒　...　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　史　┃   神兽保佑,代码无bug
 * 　　　　　　　　　┃　诗　┃
 * 　　　　　　　　　┃　之　┃
 * 　　　　　　　　　┃　宠　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┗━━━┓
 * 　　　　　　　　　┃BUG天敌　　　┣┓┣┓┣┓┣┓┣┓
 * 　　　　　　　　　┃　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.kulala.staticsview.R;

public class ListViewPushRefresh extends ListView implements AbsListView.OnScrollListener {
    private View footerView;//底部View
    private View headerView;//顶部View

    int     totalItemCount   = 0;//ListView item个数
    int     firstVisibleItem = 0;//首item号
    int     lastVisibleItem  = 0;//最后可见的Item
    boolean isLoading        = false;//是否加载标示
    private LayoutInflater mInflater;
    private int            measuredHeight;


    public ListViewPushRefresh(Context context) {
        super(context);
        initView(context);
    }

    public ListViewPushRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public ListViewPushRefresh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public int getLastVisibleItem() {
        return lastVisibleItem;
    }
    public int getFirstVisibleItem() {
        return firstVisibleItem;
    }

    /**
     * 初始化ListView
     */
    private void initView(Context context) {
        mInflater = LayoutInflater.from(context);
        footerView = mInflater.inflate(R.layout.listview_refresh_header, null);
        headerView = mInflater.inflate(R.layout.listview_refresh_header, null);
        this.addFooterView(footerView);
        this.addHeaderView(headerView);
        headerView.measure(0, 0);//先测量再拿到它的高度
        measuredHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -measuredHeight, 0, 0);// 隐藏
        footerView.setPadding(0, -measuredHeight, 0, 0);// 隐藏
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //当滑动到底端，并滑动状态为 not scrolling
        if (scrollState != SCROLL_STATE_IDLE) return;
        if (firstVisibleItem == 0) {//滑至顶部且结束滑动
            if (onLoadHeaderListener == null) return;
            if (!isLoading) {
                isLoading = true;
                //设置可见
                headerView.setPadding(0, 0, 0, 0);// 显示
                //加载数据
                onLoadHeaderListener.onLoad();

            }
        } else if (lastVisibleItem == totalItemCount || lastVisibleItem == totalItemCount - 2) {
            if (onLoadBottomListener == null) return;
            if (!isLoading) {
                isLoading = true;
                //设置可见
                footerView.setPadding(0, 0, 0, 0);// 显示
                onLoadBottomListener.onLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount - 1 - 1;
        this.totalItemCount = totalItemCount;
    }

    /**
     * 加载数据接口
     */

    private OnLoadBottomListener onLoadBottomListener;

    public void setOnLoadBottomListener(OnLoadBottomListener onLoadBottomListener) {
        this.onLoadBottomListener = onLoadBottomListener;
    }
    private OnLoadHeaderListener onLoadHeaderListener;

    public void setonLoadHeaderListener(OnLoadHeaderListener onLoadHeaderListener) {
        this.onLoadHeaderListener = onLoadHeaderListener;
    }
    public interface OnLoadBottomListener {
        void onLoad();
    }

    public interface OnLoadHeaderListener {
        void onLoad();
    }

    /**
     * 数据加载完成
     */
    public void loadComplete() {
        headerView.setPadding(0, -measuredHeight, 0, 0);// 隐藏
        footerView.setPadding(0, -measuredHeight, 0, 0);// 隐藏
        isLoading = false;
    }
    /**
     * 数据初始要显示load
     */
    public void loadStart() {
        headerView.setPadding(0, 0, 0, 0);// 显示
        isLoading = true;
    }
}