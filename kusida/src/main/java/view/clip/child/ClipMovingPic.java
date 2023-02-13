package view.clip.child;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsfunc.static_view_change.ORecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.information.DataBanner;
import view.ActivityWeb;

public class ClipMovingPic extends RelativeLayoutBase {
    private ViewPager    view_page;
    private LinearLayout layout_dot_contain;

    private List<SmartImageView> imageViews;                // ������ͼƬ����
    private List<SmartImageView> dots;                        // ͼƬ�������ĵ���Щ��
    private boolean[]            instantArr;

    private int currentItem = 0;            // ��ǰͼƬ��������
    private int oldPosition = 0;

    private static ScheduledExecutorService scheduledExecutorService;    // ��ʱ��
    private        List<DataBanner>         bannerList;
    private        tipHandler               handler;

    private boolean isInstant = false;
    private MyAdapter adapter;

    public ClipMovingPic(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new tipHandler();
        LayoutInflater.from(context).inflate(R.layout.clip_moving_pic, this, true);
        layout_dot_contain = (LinearLayout) this.findViewById(R.id.layout_dot_contain);
        view_page = (ViewPager) findViewById(R.id.view_page);
        initEvents();
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

    public boolean getIsInstant() {
        return isInstant;
    }

    public void invalidateViewPager() {
        if (imageViews != null) {
            stopRunning();
            for (int i = 0; i < imageViews.size(); i++) {
                SmartImageView img  = imageViews.get(i);
                String         http = bannerList.get(i).pic;
                img.setImageUrl(http);
            }
            startRunning();
        }
    }

    // ===================================public====================================
    public void setDataPic(List<DataBanner> bannerList) {
        isInstant = true;
        clearImage();
        this.bannerList = bannerList;
        imageViews = new ArrayList<SmartImageView>();
        dots = new ArrayList<SmartImageView>();
        LinearLayout.LayoutParams pars = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ODipToPx.dipToPx(getContext(), 150));
        // ��ʼ��ͼƬ��Դ
        instantArr = new boolean[bannerList.size()];
        for (int i = 0; i < bannerList.size(); i++) {
            SmartImageView imageView = new SmartImageView(this.getContext(), null);
            imageView.setLayoutParams(pars);
            imageView.setScaleType(ScaleType.FIT_XY);
            String http = bannerList.get(i).pic;
            imageView.setImageUrl(http);
//			ImageHttpLoader.getInstance().asyncloadImage(imageView, http, MD5.getImageName(http));
            imageViews.add(imageView);
            //before
        }
        layout_dot_contain.removeAllViews();
        for (int i = 0; i < bannerList.size(); i++) {
            SmartImageView            dot = new SmartImageView(getContext(), null);
            LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(ODipToPx.dipToPx(getContext(), 10), ODipToPx.dipToPx(getContext(), 10));
            par.setMargins(30, 0, 0, 0);
            dot.setLayoutParams(par);
            dot.setImageResource(R.drawable.dot_black);
            if (i == 0)
                dot.setImageResource(R.drawable.dot_white);
            layout_dot_contain.addView(dot);
            dots.add(dot);
        }
        adapter = new MyAdapter();
        view_page.setAdapter(adapter);
        view_page.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                if (dots == null) return;
                dots.get(oldPosition).setImageResource(R.drawable.dot_black);
                dots.get(position).setImageResource(R.drawable.dot_white);
                oldPosition = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        startRunning();
    }

    public void startRunning() {
        if (scheduledExecutorService != null && scheduledExecutorService.isShutdown() == false)
            scheduledExecutorService.shutdown();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // ��Activity��ʾ������ÿ�������л�һ��ͼƬ��ʾinitialDelay����ʼ����ʱ
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 4, 4, TimeUnit.SECONDS);
    }

    public void stopRunning() {
        if (scheduledExecutorService != null && scheduledExecutorService.isShutdown() == false)
            scheduledExecutorService.shutdown();
        scheduledExecutorService = null;
    }

    private void clearImage() {
        if (imageViews != null)
            for (SmartImageView imag :
                    imageViews) {
                ORecycle.recycleImageView(imag);
            }
        imageViews = null;
        view_page.removeAllViews();
        if (dots != null)
            for (SmartImageView imagg :
                    dots) {
                ORecycle.recycleImageView(imagg);
            }
        dots = null;
        layout_dot_contain.removeAllViews();
        stopRunning();
    }

    // =======================================================================

    /**
     * �����л�����
     */
    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (view_page) {
                System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % imageViews.size();
                handleChangePic(); // ͨ��Handler�л�ͼƬ
            }
        }
    }

    /**
     * ���ViewPagerҳ���������
     */
    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if (instantArr[position] == false) {
                container.addView(imageViews.get(position));
                instantArr[position] = true;
                imageViews.get(position).setOnClickListener(new OnClickListenerMy() {
                    @Override
                    public void onClickNoFast(View v) {
                        DataBanner banner = ClipMovingPic.this.bannerList.get(position);
                        if (banner.jumpUrl == null || banner.jumpUrl.length() == 0)return;// 无跳转
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(ActivityWeb.TITLE_NAME, "发现");
                        bundle.putString(ActivityWeb.HTTP_ADDRESS, banner.jumpUrl);
                        intent.putExtras(bundle);
                        intent.setClass(getContext(), ActivityWeb.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }
                });
            }
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
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

        //用于刷新
        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    }

    // =====================================================================
    private void handleChangePic() {
        Message message = new Message();
        message.what = 1;// ��־���ĸ��̴߳�����
        handler.sendMessage(message);// ����message��Ϣ
    }

    // ===================================================
    @SuppressLint("HandlerLeak")
    class tipHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (view_page == null) return;
                    view_page.setCurrentItem(currentItem);// �л���ǰ��ʾ��ͼƬ
                    break;
            }
        }
    }
    // =======================================================================
}