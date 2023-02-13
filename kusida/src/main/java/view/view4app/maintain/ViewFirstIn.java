package view.view4app.maintain;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

/**
 * Created by qq522414074 on 2016/10/31.
 */
public class ViewFirstIn extends LinearLayoutBase {
    private Button set;

    public ViewFirstIn(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_car_maintenance_remind, this, true);
        set = (Button) findViewById(R.id.set);
        initEvents();
    }

    public ViewFirstIn(Context context) {
        super(context);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {
        set.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_add_maintain);
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }
}
