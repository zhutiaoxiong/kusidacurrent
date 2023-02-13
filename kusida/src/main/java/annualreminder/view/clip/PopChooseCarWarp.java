package annualreminder.view.clip;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.listview.ListViewWarp;

import java.util.List;
/**
 * Created by qq522414074 on 2016/11/7.
 * 主页选车列表
 */

public class PopChooseCarWarp {
    private PopupWindow popContain;//弹出管理
    private View        parentView;//本对象显示

    private RelativeLayout          thisView;
    private ListViewWarp            list_cars;
    private ImageView               img_bg;
    private String                  mark;//选择标记
    private PopChooseCarWarpAdapter adapter;
    private List<DataCarChoose>     list;

    private OnCarChooseListener chooseListener;
    // ========================out======================
    private static PopChooseCarWarp    _instance;
    public static PopChooseCarWarp getInstance() {
        if (_instance == null)
            _instance = new PopChooseCarWarp();
        return _instance;
    }
    //===================================================
    public void show(View parentView, List<DataCarChoose> listChooseCar) {
        if (listChooseCar == null || listChooseCar.size() == 0) return;
        this.parentView = parentView;
        Context context = parentView.getContext();
        this.list = listChooseCar;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.list_annual_reminder_select_car, null);
        list_cars = (ListViewWarp) thisView.findViewById(R.id.list_cars);
        img_bg = (ImageView) thisView.findViewById(R.id.img_bg);
        adapter = new PopChooseCarWarpAdapter(context, list);
        list_cars.setAdapter(adapter);
        initViews();
        initEvents();
    }
    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Drawable dw = parentView.getContext().getResources().getDrawable(R.color.annual_transparent);//no color no click
        popContain.setBackgroundDrawable(dw);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    closeThis();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView,Gravity.LEFT,0,0);
//        popContain.showAsDropDown(parentView, 0, 100);//, Gravity.LEFT
        list_cars.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    public void initEvents() {
        list_cars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list == null || list.size() == 0) return;
                if(chooseListener!=null)chooseListener.onChooseCar(list.get(position));
                closeThis();
            }
        });
        img_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeThis();
            }
        });
    }
    private void closeThis(){
        list = null;
        adapter = null;
        parentView = null;
        popContain.dismiss();
    }
    //========================================================
    public class DataCarChoose {
        public String carName = "";
        public long carId;
        public String carLogo = "";
    }

    public interface OnCarChooseListener {
        void onChooseCar(DataCarChoose car);//收包了就是成功了
    }
    public void setOnCarChooseListener(OnCarChooseListener listener) {
        this.chooseListener = listener;
    }
}
