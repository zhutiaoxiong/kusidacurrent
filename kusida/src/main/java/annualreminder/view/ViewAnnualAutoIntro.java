package annualreminder.view;

import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import annualreminder.view.style.StyleTitleHead;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
/**
 * 智提醒说明
 */

/**
 * Annual智能提醒说明
 */
public class ViewAnnualAutoIntro extends LinearLayout {

        private StyleTitleHead title_head;
        private TextView txt_info;
        public ViewAnnualAutoIntro(Context context, AttributeSet attrs) {
            super(context, attrs);//this layout for add and edit
            LayoutInflater.from(context).inflate(R.layout.view_annual_auto_intro, this, true);
            title_head = (StyleTitleHead) findViewById(R.id.title_head);
            txt_info = (TextView) findViewById(R.id.txt_info);
            initViews();
            initEvents();
        }
        public void initViews() {
            txt_info.setMovementMethod(new ScrollingMovementMethod());
            txt_info.setText(Html.fromHtml((getResources().getString(R.string.intro_annual_auto))));
        }
        public void initEvents() {
            //back
            title_head.img_left.setOnClickListener(new OnClickListenerMy(){
                @Override
                public void onClickNoFast(View view) {
                    ODispatcher.dispatchEvent(OEventName.VIEW_ANNUAL_REMINDER_GOTOVIEW, R.layout.view_annual_reminder_add);
                }
            });
        }
        // ==============================================================
    }