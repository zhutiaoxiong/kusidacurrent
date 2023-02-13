package view.view4app.codriver;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;

import adapter.AdapterPickAuthor;
import model.common.DataAuthoredUser;
import view.clip.ClipLineBtnInptxt;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2017/9/29.
 */

public class ActivityAuthorDetails extends ActivityBase {
    private ClipTitleMeSet title_head;
    private ClipLineBtnInptxt txt_nickname, txt_phone, txt_carname;
    private        TextView            txt_time;
    private        ListView            list_authors;
    private AdapterPickAuthor authorAdapter;
    public static DataAuthoredUser selectedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clip_pop_author_detail);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        txt_nickname = (ClipLineBtnInptxt) findViewById(R.id.txt_nickname);
        txt_phone = (ClipLineBtnInptxt) findViewById(R.id.txt_phone);
        txt_carname = (ClipLineBtnInptxt) findViewById(R.id.txt_carname);
        txt_time = (TextView) findViewById(R.id.txt_time);
        list_authors = (ListView) findViewById(R.id.list_authors);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        if (selectedUser == null) return;
        txt_nickname.setText(selectedUser.userinfo.name);
        txt_phone.setText(selectedUser.userinfo.phoneNum);
        //carinfo是null，需要parent给初值
        txt_carname.setText(selectedUser.carinfo.num);
        String time = getResources().getString(R.string.from)+ ODateTime.time2StringWithHH(selectedUser.startTime) + ActivityAuthorDetails.this.getResources().getString(R.string.to) + ODateTime.time2StringWithHH(selectedUser.endTime);
        txt_time.setText(time);
        authorAdapter = new AdapterPickAuthor(ActivityAuthorDetails.this, selectedUser.authors, R.layout.list_item_name_check_pair);
        list_authors.setAdapter(authorAdapter);
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
