package view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.ActivityBase;

import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;

public class ActivityNavigation extends ActivityBase {
    private ViewPager            view_page;
    private List<SmartImageView> imageViews;
    private Button               btn_confirm;
    private int[] image_res_arr = new int[]{R.drawable.img_start_page1, R.drawable.img_start_page2, R.drawable.img_start_page3};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("naviPageLoaded");
        boolean naviPageLoaded = ODBHelper.queryResult2boolean(result);
        if (naviPageLoaded) {
            exitThis();
        } else {
            initViews();
            initEvents();
        }
    }

    private void exitThis() {
        Intent intent = new Intent();
        intent.setClass(this, ActivityFlash.class);
        this.startActivity(intent);
        this.finish();
    }

    protected void initViews() {
        setContentView(R.layout.activity_navigation);
        view_page = (ViewPager) findViewById(R.id.view_page);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setVisibility(View.INVISIBLE);
        imageViews = new ArrayList<SmartImageView>();
        LinearLayout.LayoutParams pars = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        // ��ʼ��ͼƬ��Դ
        for (int i = 0; i < image_res_arr.length; i++) {
            SmartImageView imageView = new SmartImageView(GlobalContext.getContext(), null);
            imageView.setLayoutParams(pars);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final int res = image_res_arr[i];
            imageView.setImageResource(res);
            imageViews.add(imageView);
        }
        view_page.setAdapter(new MyAdapter());
    }

    @Override
    protected void initEvents() {
        view_page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    btn_confirm.setVisibility(View.VISIBLE);
                } else {
                    btn_confirm.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("naviPageLoaded", "true");
                exitThis();
            }
        });
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        super.receiveEvent(eventName, paramObj);
    }

    @Override
    public void invalidateUI() {
    }

    @Override
    protected void popView(int resId) {

    }

    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
//            super.destroyItem(container, position, object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(ViewGroup container) {
        }

        @Override
        public void finishUpdate(ViewGroup container) {
        }
    }

}