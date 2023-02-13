package view.view4me.lcdkey;

import android.os.Bundle;
import android.view.View;

import com.client.proj.kusida.R;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.titlehead.ClipTitleHead;

public class ActivityLcdKeyTips extends ActivityBase {
    private ClipTitleHead title_head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcdkey_tips);
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
            public void onClickNoFast(View v) {
                finish();
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }

    @Override
    protected void popView(int resId) {

    }
}
