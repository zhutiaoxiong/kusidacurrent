package view.view4control;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import java.util.ArrayList;
import java.util.List;

public class DotIndicator implements ViewPager.OnPageChangeListener {
    private int mPageCount;//页数
    private List<ImageView> mImgList;//保存img总个数
    private int img_select;
    private int img_unSelect;
    private onPageChangeListener listener;
    private  long newCreateTime;//避免二个同时出现冲突
    private long        nowCreateTime;

    public DotIndicator(Context context, LinearLayout linearLayout, int pageCount,int currentItemSelecet,onPageChangeListener listener) {
        this.mPageCount = pageCount;
        if (BuildConfig.DEBUG) Log.e("PanelCar","传进来的数量"+pageCount);
        this.listener = listener;
        nowCreateTime = System.currentTimeMillis();
        newCreateTime = nowCreateTime;
        mImgList = new ArrayList<>();
        img_select = R.drawable.dot_selected;
        img_unSelect = R.drawable.dot_normal;
        final int imgSizeHeight = ODipToPx.dipToPx(context,2);
        final int imgSizeWidth = ODipToPx.dipToPx(context,18);
        linearLayout.removeAllViews();
        if(pageCount>0){
            for (int i = 0; i < mPageCount; i++) {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //为小圆点左右添加间距
                params.leftMargin = ODipToPx.dipToPx(context,5);
                params.rightMargin = ODipToPx.dipToPx(context,5);
                //给小圆点一个默认大小
                params.height = imgSizeHeight;
                params.width = imgSizeWidth;
                if (i == currentItemSelecet) {
                    imageView.setBackgroundResource(img_select);
                } else {
                    imageView.setBackgroundResource(img_unSelect);
                }
                //为LinearLayout添加ImageView
                linearLayout.addView(imageView, params);
                mImgList.add(imageView);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        if (BuildConfig.DEBUG) Log.e("PanelCar","判断时间"+position);
//        if(newCreateTime!=nowCreateTime)return;
        if (BuildConfig.DEBUG) Log.e("PanelCar","设置圆点ui"+position);
        for (int i = 0; i < mPageCount; i++) {
            //选中的页面改变小圆点为选中状态，反之为未选中
            if ((position % mPageCount) == i) {
                (mImgList.get(i)).setBackgroundResource(img_select);
            } else {
                (mImgList.get(i)).setBackgroundResource(img_unSelect);
            }
        }
        if (BuildConfig.DEBUG) Log.e("PanelCar","listener"+listener);
        if(listener!=null)listener.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(newCreateTime!=nowCreateTime)return;
        if(state==0){
            //滑动时添加音效
//            if(ManagerVoiceSet.getInstance().getIsOpenScrollButtonVoice()==-1){
//                SoundHelper.playSoundPool(GlobalContext.getContext(), ManagerVoiceSet.getInstance().getScrollButtonVoiceResId());
//            }
        }
    }
    public interface onPageChangeListener {
        void onPageSelected(int position);
    }
}
