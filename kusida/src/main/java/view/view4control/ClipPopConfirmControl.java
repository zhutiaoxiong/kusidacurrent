package view.view4control;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_assistant.ButtonBgStyle;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OCallBack;

import model.ManagerCarList;
import model.ManagerSkins;
import model.carlist.DataCarInfo;
import model.skin.DataTempSetup;

import static model.ManagerSkins.DEFAULT_NAME_TEMP;
import static model.ManagerSkins.TRANSPARENT;

public class ClipPopConfirmControl {
    private PopupWindow    popContain;//弹出管理
    private View           parentView;//本对象显示
    private Context        context;
    private RelativeLayout thisView;

    private RelativeLayout lin_base;
    private TextView txt_title,txt_info,txt_infosm;
    private ImageView split_center_line,img_bottom,spiline_x;
    private Button   btn_cancel, btn_confirm;
    private        OCallBack             callback;
    private        String                mark;
    // ========================out======================
    private static ClipPopConfirmControl _instance;

    public static ClipPopConfirmControl getInstance() {
        if (_instance == null)
            _instance = new ClipPopConfirmControl();
        return _instance;
    }

    //===================================================
    public void resetInfoWithWarningImg(String line1,String line2){
        String htmlFor02 = "<img src='" + R.drawable.icon_alarm_warning + "'>  " +line1 +"<br>" +line2;
        txt_info.setText(Html.fromHtml(htmlFor02, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int id = Integer.parseInt(source);
                Drawable drawable = ContextCompat.getDrawable(parentView.getContext(),R.drawable.icon_alarm_warning);
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() ,drawable.getIntrinsicHeight());
                drawable.setBounds(0, 0, ODipToPx.dipToPx(parentView.getContext(),16),ODipToPx.dipToPx(parentView.getContext(),16));
                return drawable;
            }
        }, null));
//        txt_info.setTextColor(Color.parseColor(textColor));
    }
    public void show(View parentView, String title,String info,String infosm,String confirm, String mark, OCallBack callback,boolean onlyConfirm) {
        show(0,parentView,title,info,infosm,confirm,mark,callback,onlyConfirm);
    }
    private Drawable getImage(String url,String name){
        if(ManagerSkins.TRANSPARENT.equals(name)){
            return ManagerSkins.getInstance().getPngImage(TRANSPARENT);
        }
        return ManagerSkins.getInstance().getPngImage(ManagerSkins.getCacheKey(false,(TextUtils.isEmpty(url) ? DEFAULT_NAME_TEMP : url),name));
    }
    public void show(int heightS,View parentView, String title,String info,String infosm,String confirm, String mark, OCallBack callback,boolean onlyConfirm) {
        this.parentView = parentView;
        this.callback = callback;
        this.mark = mark;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_pop_confirm_control, null);
        lin_base = (RelativeLayout) thisView.findViewById(R.id.lin_base);
        txt_title = (TextView) thisView.findViewById(R.id.txt_title);
        txt_info = (TextView) thisView.findViewById(R.id.txt_info);
        txt_infosm = (TextView) thisView.findViewById(R.id.txt_infosm);
        btn_cancel = (Button) thisView.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) thisView.findViewById(R.id.btn_confirm);
        split_center_line = (ImageView) thisView.findViewById(R.id.split_center_line);
        img_bottom = (ImageView) thisView.findViewById(R.id.img_bottom);
        spiline_x= (ImageView) thisView.findViewById(R.id.spiline_x);
        initViews();
        initEvents();
        txt_title.setText(title);
        txt_info.setText(info);
        txt_info.setCompoundDrawables(null,null,null,null);
        if(infosm.equals("")){
            txt_infosm.setVisibility(View.GONE);
        }else {
            txt_infosm.setText(infosm);
            txt_infosm.setVisibility(View.VISIBLE);
        }
        if(confirm.equals("")){
            btn_confirm.setText(context.getResources().getString(R.string.confirm));
        }else {
            btn_confirm.setText(confirm);
        }
        if(onlyConfirm){
            btn_cancel.setVisibility(View.GONE);
            split_center_line.setVisibility(View.INVISIBLE);
        }else{
            btn_cancel.setVisibility(View.VISIBLE);
            split_center_line.setVisibility(View.VISIBLE);
        }
        int usedHeight = (heightS!=0) ? heightS:135;
//        if(heightS!=0){
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ODipToPx.dipToPx(context,usedHeight));
//        lin_base.setLayoutParams(params);
//        img_bottom.setLayoutParams(params);
//        img_bottom.setScaleType(ImageView.ScaleType.FIT_XY);
        //style
        //再看用什么Action
        DataCarInfo   car = ManagerCarList.getInstance().getCurrentCar();
        String url = "";//使用默认的
        if(car != null && car.skinTemplateInfo!=null){//使用网络的
            url = car.getCarTemplate().url;
        }
        ManagerSkins.getInstance().loadTemp(context,url,"control_normal_0",null);
        //背景
        Drawable bg = getImage(url,"control_bg_confirm");
        if(bg == null){
            setBgAndTextColor("#00000000","#000000");
        }else{
            img_bottom.setImageDrawable(bg);

            DataTempSetup tempSetup = ManagerSkins.getInstance().getTempSetup(ManagerSkins.getTempZipFileName(url));
            if(tempSetup!=null) {
                int[][] states = new int[2][];
                states[0] = new int[]{android.R.attr.state_pressed};
                states[1] = new int[]{};
                int[] colors = new int[]{Color.parseColor(tempSetup.colorPopPress), Color.parseColor(tempSetup.colorPopNomal)};
                ColorStateList selector = new ColorStateList(states, colors);
                txt_title.setTextColor(Color.parseColor("#000000"));
                txt_info.setTextColor(Color.parseColor("#000000"));
                btn_confirm.setTextColor(selector);
                btn_cancel.setTextColor(Color.parseColor("#000000"));
            }
            //默认图片
            StateListDrawable btnCancleBg = ButtonBgStyle.createDrawableSelector(context, getImage(url,"img_bg_confirm_btn_normal_l"),getImage(url,"img_bg_confirm_btn_press_l"),getImage(url,"img_bg_confirm_btn_press_l"));
            btn_cancel.setBackground(btnCancleBg);
            if(onlyConfirm){
                btn_confirm.setBackgroundColor(Color.parseColor("#00000000"));
            }else{
                StateListDrawable btnConfimBg = ButtonBgStyle.createDrawableSelector(context, getImage(url,"img_bg_confirm_btn_normal_r"),getImage(url,"img_bg_confirm_btn_press_r"),getImage(url,"img_bg_confirm_btn_press_r"));
                btn_confirm.setBackground(btnConfimBg);
            }
        }
        thisView.postInvalidate();
    }
    public void setBgAndTextColor(String bgColor,String textColor){
        btn_confirm.setBackgroundColor(Color.parseColor(bgColor));
        btn_cancel.setBackgroundColor(Color.parseColor(bgColor));
        btn_confirm.setTextColor(Color.parseColor(textColor));
        btn_cancel.setTextColor(Color.parseColor(textColor));
        txt_title.setTextColor(Color.parseColor(textColor));
        txt_info.setTextColor(Color.parseColor(textColor));
    }

    public void initViews() {
        if(popContain==null){
            popContain = new PopupWindow();
        }
        popContain.setContentView(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);//设了这个就不能点外面了
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);//R.color.background_all
//        Drawable dw = GlobalContext.getContext().getResources().getDrawable(R.color.background_all);//no color no initClick
//        popContain.setBackgroundDrawable(dw);
//        popContain.setAnimationStyle(R.style.WindowEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popContain.dismiss();
                    callback = null;
                    parentView =null;
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    public void initEvents() {
        btn_cancel.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                popContain.dismiss();
                callback = null;
                parentView =null;
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(callback!=null){
                    callback.callback(mark, "");
                }
                popContain.dismiss();
                callback = null;
                parentView =null;
            }
        });
    }
    private Bitmap getBitmapFromUri(Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
             if (BuildConfig.DEBUG) Log.e("[Android]", e.getMessage());
             if (BuildConfig.DEBUG) Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }
}

