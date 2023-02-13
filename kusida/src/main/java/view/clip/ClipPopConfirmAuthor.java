package view.clip;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_system.ODateTime;

import common.GlobalContext;
import model.common.DataAuthorLost;

public class ClipPopConfirmAuthor{
    private PopupWindow popContain; // 弹出管理
    private View    parentView; // 本对象显示
    private Context     context;

    private RelativeLayout thisView;
    private View           touch_exit;
    private TextView       txt_info,txt_time,txt_author;
    private ClipLineBtnInptxt txt_showdetail;
    private RelativeLayout    lin_time,lin_author;
    private Button         btn_cancel, btn_confirm;

    public static  DataAuthorLost author;
    private        String             mark;                        // 选择标记
    private        OCallBack          callback;
    // ========================out======================
    private static ClipPopConfirmAuthor _instance;

    public static ClipPopConfirmAuthor getInstance() {
        if (_instance == null)
            _instance = new ClipPopConfirmAuthor();
        return _instance;
    }

    // ===================================================
    public void show(View parentV,DataAuthorLost author, String mark, OCallBack callback) {
        if(author ==null)return;
        this.mark = mark;
        this.callback = callback;
        this.parentView = parentV;
        this.author = author;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_pop_confirm_author, null);
        touch_exit = (View) thisView.findViewById(R.id.touch_exit);
        txt_info = (TextView) thisView.findViewById(R.id.txt_info);
        txt_time = (TextView) thisView.findViewById(R.id.txt_time);
        txt_author = (TextView) thisView.findViewById(R.id.txt_author);
        txt_showdetail = (ClipLineBtnInptxt) thisView.findViewById(R.id.txt_showdetail);
        lin_time = (RelativeLayout) thisView.findViewById(R.id.lin_time);
        lin_author = (RelativeLayout) thisView.findViewById(R.id.lin_author);
        btn_cancel = (Button) thisView.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) thisView.findViewById(R.id.btn_confirm);
        initView();
        initViews();
        initEvents();
    }

    private void initView(){
        txt_info.setText(author.content);
        txt_time.setText(GlobalContext.getContext().getResources().getString(R.string.from)+ ODateTime.time2StringWithHH(author.startTime)+ GlobalContext.getContext().getResources().getString(R.string.to)+ODateTime.time2StringWithHH(author.endTime));
        String authors = "";
        for(int i=0;i<author.authoritys.length;i++){
            authors += author.authoritys[i]+"\n";
        }
        txt_author.setText(authors);
        lin_time.setVisibility(View.GONE);
        lin_author.setVisibility(View.GONE);
        txt_showdetail.setRightImage(R.drawable.icon_common_arrow_down);
    }
    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        // popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popContain.dismiss();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }
    private void exitThis(){
//        parentView = null;
        callback = null;
        context  = null;
//        popContain = null;
        popContain.dismiss();
    }

    public void initEvents() {
        txt_showdetail.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(lin_time.getVisibility() == View.GONE){
                    lin_time.setVisibility(View.VISIBLE);
                    lin_author.setVisibility(View.VISIBLE);
                    txt_showdetail.setRightImage(R.drawable.arrow_up_gray);
                }else{
                    lin_time.setVisibility(View.GONE);
                    lin_author.setVisibility(View.GONE);
                    txt_showdetail.setRightImage(R.drawable.icon_common_arrow_down);
                }
                thisView.postInvalidate();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                callback.callback(mark, "confirm");
                exitThis();
            }
        });
        btn_cancel.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                callback.callback(mark, "cancel");
                exitThis();
            }
        });
        touch_exit.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
            }
        });
    }

}
