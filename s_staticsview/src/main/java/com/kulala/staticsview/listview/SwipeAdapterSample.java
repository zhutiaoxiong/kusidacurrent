
package com.kulala.staticsview.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
/**
 * 一定主layoutItem 要 linnerLayout ,并且水平二个子layout
 * item_left 无删除时的显示，一定放前位 item_right删除按扭，一定放后位
 * 以下设置防列表重复:
 LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
 item.item_left.setLayoutParams(lp1);
 LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
 item.item_right.setLayoutParams(lp2);
 */

/**
 * VIEW中使用功能
 */

//private void initView() {
//        mListView = (SwipeListView)findViewById(R.id.listview);
//        mListView.setRightViewWidth(135);
//        SwipeAdapter adapter = new SwipeAdapter(this, mListView.getRightViewWidth(),
//        new SwipeAdapter.IOnItemRightClickListener() {
//@Override
//public void onRightClick(View v, int position) {
//        // TODO Auto-generated method stub
//        Toast.makeText(MainActivity.this, "right onclick " + position,
//        Toast.LENGTH_SHORT).show();
//        }
//        });
//        mListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
//        mListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
//        mListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
//        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
//        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
//        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
//        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
//        mListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.list_footer, null));
//        mListView.setAdapter(adapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//@Override
//public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(MainActivity.this, "item onclick " + position, Toast.LENGTH_SHORT)
//        .show();
//        }
//        });
//        }
public class SwipeAdapterSample extends BaseAdapter {
    private Context mContext = null;
    private int mRightWidth = 0;
    private IOnItemRightClickListener mListener = null;//单击事件监听器
    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }
    /**
     * @param
     */
    public SwipeAdapterSample(Context ctx, int rightWidth, IOnItemRightClickListener l) {
        mContext = ctx;
        mRightWidth = rightWidth;
        mListener = l;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder item;
//        final int thisPosition = position;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_gps_path, parent, false);
//            item = new ViewHolder();
//            item.item_left = (View)convertView.findViewById(R.id.movelayout);
//            item.item_right = (View)convertView.findViewById(R.id.item_right);
//            convertView.setTag(item);
//        } else {// 有直接获得ViewHolder
//            item = (ViewHolder)convertView.getTag();
//        }
//        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//        item.item_left.setLayoutParams(lp1);
//        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
//        item.item_right.setLayoutParams(lp2);
//        item.item_right.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClickNoFast(View v) {
//                if (mListener != null) {
//                    mListener.onRightClick(v, thisPosition);
//                }
//            }
//        });
        return convertView;
    }

    private class ViewHolder {
        View item_left;
        View item_right;
    }
}
