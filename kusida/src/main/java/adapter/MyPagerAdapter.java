package adapter;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.kulala.staticsview.LinearLayoutBase;

import java.util.List;

/**
 * Created by qq522414074 on 2016/9/21.
 */
public class MyPagerAdapter extends PagerAdapter {
    private List<LinearLayoutBase> list;
   public  MyPagerAdapter(List<LinearLayoutBase> list){
       this.list=list;
   }
    @Override
    public int getCount() {
        return list.size();
    }
    public LinearLayoutBase getItem(int position){
        return list.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(list.get(position));
    }

    @Override
    public LinearLayoutBase instantiateItem(ViewGroup container, int position) {
         ((ViewPager) container).addView(list.get(position));
        return list.get(position);
    }

}
