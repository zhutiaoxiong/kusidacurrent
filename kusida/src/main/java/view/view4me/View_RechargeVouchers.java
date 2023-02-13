package view.view4me;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ctrl.OCtrlCommon;
import model.ManagerCarList;
import view.view4me.set.ClipTitleMeSet;

public class View_RechargeVouchers extends LinearLayoutBase {
    private ClipTitleMeSet title_head;
    private TextView txt_already_used, txt_delay;
    private Button btn_confirm;
    public static String scanResult;
    private String code;//充值券号码
    private String date;//充值券期限
    public View_RechargeVouchers(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_recharge_vouchers, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_already_used = (TextView) findViewById(R.id.txt_already_used);
        txt_delay = (TextView) findViewById(R.id.txt_delay);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        txt_already_used.setVisibility(View.INVISIBLE);
        txt_delay.setVisibility(View.INVISIBLE);
        btn_confirm.setVisibility(View.INVISIBLE);
       if(scanResult.contains("#")){
           if(scanResult.indexOf("#")==0||scanResult.indexOf("#")==scanResult.length()-1){
               txt_already_used.setVisibility(View.VISIBLE);
           }else{
               txt_delay.setVisibility(View.VISIBLE);
               btn_confirm.setVisibility(View.VISIBLE);
               code=scanResult.substring(0,scanResult.indexOf("#"));
               date=scanResult.substring(scanResult.indexOf("#")+1);
               SpannableString s = new SpannableString("您的爱车手机控车服务期限将延长 "+date+"。充值券号为："+code);
               Pattern p = Pattern.compile(code);
               Matcher m = p.matcher(s);
               while (m.find()) {
                   int start = m.start();
                   int end = m.end();
                   s.setSpan(new ForegroundColorSpan(Color.parseColor("#b72e2e")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
               }
               Pattern p1 = Pattern.compile(date);
               Matcher m1= p1.matcher(s);
               while (m1.find()) {
                   int start = m1.start();
                   int end = m1.end();
                   s.setSpan(new ForegroundColorSpan(Color.parseColor("#b72e2e")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
               }
               txt_delay.setText(s);
           }
       }else{
           txt_already_used.setVisibility(View.VISIBLE);
       }
    }

    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_pay);
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OCtrlCommon.getInstance().ccmd_1005_recharge_vouchers(code,ManagerCarList.getInstance().getCurrentCarID());
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_pay);
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }
}
