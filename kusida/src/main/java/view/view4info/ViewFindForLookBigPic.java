package view.view4info;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq522414074 on 2016/9/8.
 */
public class ViewFindForLookBigPic extends LinearLayoutBase {
    private LinearLayout linearLayout;
    private LinearLayout pointLinearLayout;
    private List<SmartImageView> pointList=new ArrayList<>();
    private int lastPosition=0;
    private RollViewPager rollViewPager;

    private static int fromLayout;
    private static String[] pics={"http://qiniu.91kulala.com/pic/car/test/14694280725798711_o.jpg","http://qiniu.91kulala.com/pic/car/test/14694280725798711_o.jpg","http://qiniu.91kulala.com/pic/car/test/14694280725798711_o.jpg"};
    public ViewFindForLookBigPic(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_find_for_look_bigpic,this,true);
        initViews();
        initEvents();

    }
    public static  void setDefaultData(String[] pics0,int fromLayout0){
        fromLayout = fromLayout0;
        pics = pics0;
    }

    @Override
    protected void initViews() {
        lastPosition=0;
        linearLayout= (LinearLayout) findViewById(R.id.top_news_viewpager_ll);
        pointLinearLayout= (LinearLayout) findViewById(R.id.dots_ll);
        rollViewPager = new RollViewPager(ViewFindForLookBigPic.this.getContext());
        linearLayout.addView(rollViewPager);
        rollViewPager.setImageUrls(pics);
        addPoints();
        rollViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                position=position%pics.length;
                pointList.get(lastPosition).setImageResource(R.drawable.dot_white);
                pointList.get(position).setImageResource(R.drawable.dot_black);
                lastPosition=position;
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        rollViewPager.setCurrentItem(50 - 50 % pics.length);
        rollViewPager.startRoll();
    }
    @Override
    protected void initEvents() {
        rollViewPager.setOnItemClickListener(new RollViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,fromLayout);
            }
        });
    }

    @Override
    protected void invalidateUI() {
    }
    private void addPoints() {
        pointList.clear();
        pointLinearLayout.removeAllViews();
        for(int x=0;x<pics.length;x++){
            SmartImageView imageView = new SmartImageView(ViewFindForLookBigPic.this.getContext());
            if(x==0){
                imageView.setImageResource(R.drawable.dot_black);
            }else{
                imageView.setImageResource(R.drawable.dot_white);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin=36;
            pointLinearLayout.addView(imageView,params);
            pointList.add(imageView);
        }
    }


}
