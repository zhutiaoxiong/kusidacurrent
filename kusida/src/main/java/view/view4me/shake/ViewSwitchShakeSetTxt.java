package view.view4me.shake;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2017/10/25.
 */

public class ViewSwitchShakeSetTxt extends RelativeLayoutBase {
    private ClipTitleMeSet title_head;
    private TextView text_set;

    public ViewSwitchShakeSetTxt(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_me_switch_set_txt, this, true);
        title_head = findViewById(R.id.title_head);
//        text_set = (TextView) findViewById(R.id.text_set);
//        String htmlFor=getResources().getString(R.string.shake_front)+"<img src='"
//                + R.drawable.img_txt_lock + "'>"+getResources().getString(R.string.shake_bihind);
//        text_set.setText(Html.fromHtml(htmlFor, new Html.ImageGetter() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public Drawable getDrawable(String source) {
//                int id = Integer.parseInt(source);
//                Drawable drawable = getResources().getDrawable(id, null);
//                drawable.setBounds(0, -3, drawable.getIntrinsicWidth() ,
//                        drawable.getIntrinsicHeight());
//                return drawable;
//            }
//        }, null));
        initEvents();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_switch_shake);
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }
}
