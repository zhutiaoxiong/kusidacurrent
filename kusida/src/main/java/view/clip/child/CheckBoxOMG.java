package view.clip.child;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

public class CheckBoxOMG extends RelativeLayoutBase {
    private ImageView img_selected;
    private TextView  txt_info;
    private int thisPos;//多个选择用来定位

    private boolean isSelected = false;

    public CheckBoxOMG(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_checkbox_omg, this, true);
        img_selected = (ImageView) findViewById(R.id.img_selected);
        txt_info = (TextView) findViewById(R.id.txt_info);

        TypedArray ta   = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     text = ta.getString(R.styleable.androidMe_text);
        if (text != null && !text.equals("")) {
            txt_info.setText(text);
        }
        boolean selected = ta.getBoolean(R.styleable.androidMe_selected, false);
        if (selected) {
            img_selected.setImageResource(R.drawable.select_true_red);
            isSelected = true;
        }
        int pos = ta.getInteger(R.styleable.androidMe_pos, 0);
        if (pos!=0) {
            thisPos = pos;
        }
        initViews();
        initEvents();
        ta.recycle();
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        handleChangeData();
    }

    public boolean getSelected() {
        return isSelected;
    }
    public int getThisPos(){
        return thisPos;
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initEvents() {
        txt_info.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(!isSelected){
                    isSelected = true;
                    img_selected.setImageResource(R.drawable.select_true_red);
                    ODispatcher.dispatchEvent(OEventName.CLIP_CHECKBOX_CHANGESELECT,thisPos);
                }
                super.onClickNoFast(v);
            }
        });
        img_selected.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(!isSelected){
                    isSelected = true;
                    img_selected.setImageResource(R.drawable.select_true_red);
                    ODispatcher.dispatchEvent(OEventName.CLIP_CHECKBOX_CHANGESELECT,thisPos);
                }
                super.onClickNoFast(v);
            }
        });
    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }


    @Override
    public void invalidateUI() {
        if(isSelected){
            isSelected = true;
            img_selected.setImageResource(R.drawable.select_true_red);
        }else{
            isSelected = false;
            img_selected.setImageResource(R.drawable.select_false_slide);
        }
    }
}
