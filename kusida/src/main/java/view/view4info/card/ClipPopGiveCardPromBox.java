package view.view4info.card;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.static_interface.OCallBack;

import ctrl.OCtrlCard;


/**
 * Created by qq522414074 on 2016/10/20.
 */
public class ClipPopGiveCardPromBox {
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private Context context;
    private LinearLayout thisView;
    private TextView txt_title;
    private Button btn_cancel, btn_confirm;
    private EditText txt_edit;
    private int ide;//选择标记
    private static ClipPopGiveCardPromBox _instance;

    public static ClipPopGiveCardPromBox getInstance() {
        if (_instance == null)
            _instance = new ClipPopGiveCardPromBox();
        return _instance;
    }

    public void showInput(View parentView, int ide , OCallBack callbackk) {
        this.ide = ide;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (thisView == null) {
            thisView = (LinearLayout) layoutInflater.inflate(R.layout.clip_top_give_card_promebox, null);
            txt_title = (TextView) thisView.findViewById(R.id.txt_title);
            btn_cancel = (Button) thisView.findViewById(R.id.btn_cancel);
            btn_confirm = (Button) thisView.findViewById(R.id.btn_confirm);
            txt_edit=(EditText)thisView.findViewById(R.id.txt_edit) ;
            txt_edit.setHint(context.getResources().getString(R.string.please_enter_phone_number));
            popContain = new PopupWindow(thisView);
            popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popContain.setFocusable(true);
            popContain.setTouchable(true);
            popContain.setOutsideTouchable(false);
            popContain.showAtLocation(parentView,Gravity.TOP,0,0);
            initEvents();
        }
    }
    private void exitThis() {
        thisView=null;
        popContain.dismiss();
    }

    public void initEvents() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitThis();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String number= txt_edit.getText().toString();
                if(number.length()==11){
                    OCtrlCard.getInstance().ccmd1408_give_card(ide,number);
                    exitThis();
                }else{
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,context.getResources().getString(R.string.please_enter_the_complete_phone_number));
                }
            }
        });
    }

}