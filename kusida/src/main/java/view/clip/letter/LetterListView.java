package view.clip.letter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import view.view4me.set.ClipTitleMeSet;

public class LetterListView extends RelativeLayoutBase {
    private final int MSG_HIDE_LETTER = 0x0;

    public ClipTitleMeSet title_head;
    public ListView mListView,mLetterListView;
    private LetterBaseAdapter mAdapter;
    private LetterAdapter mLetterAdapter;
    private TextView mLetterTextView;
    private Handler mLetterHandler;

    public LetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_pop_choose_brand, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        mListView = (ListView) findViewById(R.id.letter_list_container);
        mLetterListView = (ListView) findViewById(R.id.letter_list_letter);
        mLetterTextView = (TextView) findViewById(R.id.txt_center);

        //不这样设 android:scrollbars="none",onSizeChanged将不被执行
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setHorizontalScrollBarEnabled(false);
        mLetterListView.setVerticalScrollBarEnabled(false);
        mLetterListView.setHorizontalScrollBarEnabled(false);

        mLetterListView.setOnTouchListener(mLetterOnTouchListener);
        mLetterTextView.setVisibility(View.INVISIBLE);
        mLetterHandler = new LetterHandler(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void invalidateUI() {

    }

    public void setAdapter(LetterBaseAdapter adapter) {
        if (adapter != null) {
            mAdapter = adapter;
            mListView.setAdapter(mAdapter);
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

//        mLetterAdapter = new LetterAdapter(h - mLetterListView.getPaddingTop() - mLetterListView.getPaddingBottom());
        int height  = mLetterListView.getMeasuredHeight()>0 ? mLetterListView.getMeasuredHeight() : mLetterListView.getHeight();
        mLetterAdapter = new LetterAdapter(height);
        mLetterListView.setAdapter(mLetterAdapter);
    }

    /**
     * ��ʾ��ĸ
     *
     * @Description:
     * @Author Justlcw
     * @Date 2014-5-8
     */
    private void showLetter(String letter) {
        if (mLetterTextView.getVisibility() != View.VISIBLE) {
            mLetterTextView.setVisibility(View.VISIBLE);
            mLetterListView.setBackgroundResource(R.color.all_background_color);
        }
        mLetterTextView.setText(letter);

        mLetterHandler.removeMessages(MSG_HIDE_LETTER);
        mLetterHandler.sendEmptyMessageDelayed(MSG_HIDE_LETTER, 500);
    }

    /**
     * ������Ϣ {@link LetterHandler#handleMessage(Message)}
     *
     * @param msg ��Ϣ
     * @Description:
     * @Author Justlcw
     * @Date 2014-5-8
     */
    private void handleLetterMessage(Message msg) {
        mLetterTextView.setVisibility(View.INVISIBLE);
        mLetterListView.setBackgroundResource(R.color.white);
    }

    /**
     * ��ĸ��touch�¼�
     **/
    private OnTouchListener mLetterOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int height = (int) event.getY();// - v.getTop()

            int position = mLetterAdapter.getTouchPoistion(height);
            if (position >= 0) {
                char letter = (Character) mLetterAdapter.getItem(position);
                //��ʾ��ĸ
                showLetter(String.valueOf(letter));

                //��ʾ����ĸ��Ӧ��λ��
                int select = mAdapter.getIndex(letter);
                if (select >= 0) {
                    mListView.setSelection(select);
                }
                return true;
            }
            return false;
        }
    };

    /**
     * ��ĸ�б�������
     *
     * @Title:
     * @Description:
     * @Author:Justlcw
     * @Since:2014-5-7
     * @Version:
     */
    private class LetterAdapter extends BaseAdapter {
        /**
         * ��ĸ��
         **/
        private static final String LETTER_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        /**
         * ������ʾ����ĸarray
         **/
        private char[] letterArray;
        /**
         * ÿ����ĸ�ĸ߶�
         **/
        private int itemHeight;

        /**
         * ���췽��
         *
         * @param height view height
         */
        public LetterAdapter(int height) {
            if (mAdapter == null) return;
            if (mAdapter.hideLetterNotMatch()) {
                List<Character> list = new ArrayList<Character>();
                char[] allArray = LETTER_STR.toCharArray();
                for (int i = 0; i < allArray.length; i++) {
                    char letter = allArray[i];
                    int position = mAdapter.getIndex(letter);
                    if (position >= 0) {
                        list.add(letter);
                    }
                }
                letterArray = new char[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    letterArray[i] = list.get(i);
                }
                list.clear();
                list = null;
            } else {
                letterArray = LETTER_STR.toCharArray();
            }
//            int hh = title_head.getHeight()
            itemHeight = height / letterArray.length;
        }

        @Override
        public int getCount() {
            if(letterArray == null)return 0;
            return letterArray.length;
        }

        @Override
        public Object getItem(int position) {
            return letterArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(getContext());
                ((TextView) convertView).setTextColor(getResources().getColor(R.color.blue_light));
                ((TextView) convertView).setGravity(Gravity.CENTER);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT, itemHeight);
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
                convertView.setLayoutParams(lp);
            }
            ((TextView) convertView).setText(String.valueOf(letterArray[position]));

            return convertView;
        }

        /**
         * ��ȡtouch��λ��
         *
         * @return position
         * @Description:
         * @Author Justlcw
         * @Date 2014-5-8
         */
        public int getTouchPoistion(int touchHeight) {
            int position = touchHeight / itemHeight;
            if (position >= 0 && position < getCount()) {
                return position;
            }
            return -1;
        }
    }

    /**
     * ������ĸ��ʾ��handler.
     *
     * @Title:
     * @Description:
     * @Author:Justlcw
     * @Since:2014-5-8
     * @Version:
     */
    private static class LetterHandler extends Handler {
        /**
         * ������ {@link LetterListView}
         **/
        private SoftReference<LetterListView> srLetterListView;

        /**
         * ���췽��
         *
         * @param letterListView {@link LetterListView}
         */
        public LetterHandler(LetterListView letterListView) {
            srLetterListView = new SoftReference<LetterListView>(letterListView);
        }

        @Override
        public void handleMessage(Message msg) {
            LetterListView letterListView = srLetterListView.get();
            //���viewû�б����ٵ�,����view���������Ϣ
            if (letterListView != null) {
                letterListView.handleLetterMessage(msg);
            }
        }
    }
}
