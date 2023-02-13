package view.view4control;

import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import adapter.viewpager.ViewPagerChild;
import model.carlist.DataCarInfo;

public class AdapterViewPagerCar extends PagerAdapter {
    private int currentPos=0;
    List<DataCarInfo>				listCar;
    ViewPagerChild view_pager;
    private int mChildCount = 0;
    public AdapterViewPagerCar(ViewPagerChild      view_page, List<DataCarInfo> list) {
        if(view_page!=null)view_page.removeAllViews();
        this.view_pager = view_page;
        setData(list);
    }
    public void setData(List<DataCarInfo> list){
        if(list == null  || list.size()==0){
            listCar = new ArrayList<DataCarInfo>();
        }else{
            listCar = list;//加载进先设pos
            for(int i = 0;i<list.size();i++){
                DataCarInfo info = list.get(i);
                info.carPos = i;
            }
        }
        if (listCar.size()==0) {
            listCar.add(new DataCarInfo());
        }
    }
    public void setCurrentPos(int pos) {
        currentPos = pos;
    }

    public DataCarInfo getDataByPos(int pos) {
        if (listCar == null || pos > listCar.size() - 1)
            return null;
        return listCar.get(pos);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//		 if (BuildConfig.DEBUG) Log.e("AdapterViewPagerCar des","position:"+position + " mListViews!=null"+(mListViews!=null));
//		if(mListViews!=null) {
//			ViewControlCar car = mListViews.get(position);
//		car.recycle();
        View view = container.findViewWithTag(position);
        if(view!=null) {
            container.removeView(view);// 删除页卡
        }
//			mListViews.remove(position);
//		}
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
//        ViewControlCarNew car = new ViewControlCarNew(container.getContext(), null);
//        car.setCarInfo(listCar.get(position));
        ViewCarBodyContain car = new ViewCarBodyContain(container.getContext(), null);
//        car.changeData(listCar.get(position).ide);
//        car.setTag(position);
        ViewPager viewPager = (ViewPager)container;
        viewPager.addView(car);// 添加页卡
        return car;
    }

    @Override
    public int getCount() {
        if (listCar == null)
            return 1;
        return listCar.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;// 官方提示这样写
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
    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

}
