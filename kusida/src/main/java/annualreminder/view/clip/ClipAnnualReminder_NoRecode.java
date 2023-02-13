package annualreminder.view.clip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
/**
 * Created by Administrator on 2017/2/23.
 * 主页选车，上半部,无记录
 */

public class ClipAnnualReminder_NoRecode extends RelativeLayoutBase {
    private ImageView img_add_reminder,img_add,img_no_recode;
    private TextView  txt_annual_reminder,txt_no_recode;
    public ClipAnnualReminder_NoRecode(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_annual_reminder_no_recode, this, true);
        img_add_reminder = (ImageView) findViewById(R.id.img_add_reminder);
        img_add = (ImageView) findViewById(R.id.img_add);
        img_no_recode = (ImageView) findViewById(R.id.img_no_recode);
        txt_annual_reminder = (TextView) findViewById(R.id.txt_annual_reminder);
        txt_no_recode = (TextView) findViewById(R.id.txt_no_recode);
        initViews();
        initEvents();
    }
    @Override
    protected void initViews() {

    }
    @Override
    protected void initEvents() {

    }
    @Override
    protected void invalidateUI() {

    }
}
