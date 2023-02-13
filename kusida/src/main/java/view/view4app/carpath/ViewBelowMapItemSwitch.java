package view.view4app.carpath;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;

/**
 * Created by qq522414074 on 2017/3/29.
 */

public class ViewBelowMapItemSwitch extends LinearLayoutBase {
    public TextView txt_address,txt_address_name,txt_confirm;
    public ImageView img_left,img_right;
    private ImageView img_line;
    private String name,address;
    public ViewBelowMapItemSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_below_map_item_switch, this, true);
        txt_address_name = (TextView) findViewById(R.id.txt_address_name);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_confirm = (TextView) findViewById(R.id.txt_confirm);
        img_line = (ImageView) findViewById(R.id.img_line);
        img_left = (ImageView) findViewById(R.id.img_left);
        img_right = (ImageView) findViewById(R.id.img_right);
//        img_line.setVisibility(INVISIBLE);
//        txt_confirm.setVisibility(INVISIBLE);
    }
    public void setData(String name,String address){
        this.name = name;
        this.address = address;
        handleChangeData();
    }
//    public void setNeedConfirm(boolean needConfirm){
//        if(needConfirm){
//            img_line.setVisibility(VISIBLE);
//            txt_confirm.setVisibility(VISIBLE);
//        }else{
//            img_line.setVisibility(INVISIBLE);
//            txt_confirm.setVisibility(INVISIBLE);
//        }
//    }

    @Override
    protected void initViews() {

    }


    @Override
    protected void initEvents() {

    }

    @Override
    protected void invalidateUI() {
        txt_address_name.setText(name);
        txt_address.setText(address);
    }
}
