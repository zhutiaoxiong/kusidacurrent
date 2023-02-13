package view.view4info;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.image.smart.SmartImageView;

/**
 * Created by qq522414074 on 2016/9/8.
 */
public class RollViewPager extends ViewPager {


    private String[] pics;
    private static final int NEXT=99;
    private boolean isRunning=false;

        public RollViewPager(Context context) {
        super(context);
    }

    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageUrls(String[] pics) {
        this.pics = pics;
        setAdapter(new MyRollViePagerAdatper());
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){

                case NEXT:
                    if(isRunning==true){
                        setCurrentItem(getCurrentItem()+1);
                        handler.sendEmptyMessageDelayed(NEXT,2000);
                    }
                    break;
            }
        }
    };


    //取消滚动，只注是这二行
    public void startRoll(){
//        isRunning=true;
//        handler.sendEmptyMessageDelayed(NEXT,2000);
    }

    class MyRollViePagerAdatper extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position=position%pics.length;
            LinearLayout layout = (LinearLayout) View.inflate(getContext(), R.layout.view_find_for_look_bigpic_item, null);
//            Glide.with(context).load(new File(path)).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(vh.imageView);
            SmartImageView picture=(SmartImageView) layout.findViewById(R.id.view_find_for_lookbigpic_image);
            picture.setImageUrl(pics[position]);
            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }

    private int downTime=0;

    private int downX=0;
    private int downY=0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:
                downX= (int) ev.getX();
                downY= (int) ev.getY();
                downTime= (int) System.currentTimeMillis();
                isRunning=false;
                handler.removeMessages(NEXT);
                break;

            case MotionEvent.ACTION_UP:
                int upX= (int) ev.getX();
                int upY= (int) ev.getY();
                int disX=Math.abs(upX - downX);
                int disY=Math.abs(upY - downY);
                int upTime=(int) System.currentTimeMillis();
                if(upTime-downTime<500 && disX-disY<5 ){
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(getCurrentItem()%pics.length);//褰撳墠浣嶇疆灏辨槸鏄剧ず鐨勬潯鐩?
                    }
                }

                startRoll();
                break;

            case MotionEvent.ACTION_CANCEL:
                startRoll();
                break;

            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRunning=false;
        handler.removeMessages(NEXT);
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

}
