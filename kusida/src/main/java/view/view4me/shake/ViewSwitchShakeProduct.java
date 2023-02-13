package view.view4me.shake;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.titlehead.ClipTitleHead;

/**
 * Created by qq522414074 on 2017/10/25.
 */

public class ViewSwitchShakeProduct extends RelativeLayoutBase {
    private ClipTitleHead title_head;

    public ViewSwitchShakeProduct(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_me_switch_product, this, true);
        title_head = (ClipTitleHead) findViewById(R.id.title_head);
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
