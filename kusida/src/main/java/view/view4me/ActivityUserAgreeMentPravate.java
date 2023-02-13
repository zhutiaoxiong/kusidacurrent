package view.view4me;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;


import com.client.proj.kusida.R;
import com.kulala.staticsview.ActivityBase;

import view.view4me.set.ClipTitleMeSet;

/**隐私政策*/

public class ActivityUserAgreeMentPravate extends ActivityBase {

 private    TextView txt_info;
  private ClipTitleMeSet title_head;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useragreement);
        txt_info = (TextView) findViewById(R.id.txt_info);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        initViews();
        initEvents();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initEvents() {
        title_head.img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public void initViews() {
        title_head.setTitle("隐私政策");
        txt_info.setText(Html.fromHtml((getResources().getString(R.string.me_private))));
    }
}
