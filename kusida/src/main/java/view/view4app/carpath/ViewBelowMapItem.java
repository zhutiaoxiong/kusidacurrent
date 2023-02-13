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

public class ViewBelowMapItem extends LinearLayoutBase {
    public TextView txt_address,txt_address_name,txt_confirm;
    private ImageView img_line;
    private String name,address;
    public ViewBelowMapItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_below_map_item, this, true);
        txt_address_name = (TextView) findViewById(R.id.txt_address_name);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_confirm = (TextView) findViewById(R.id.txt_confirm);
        img_line = (ImageView) findViewById(R.id.img_line);
        img_line.setVisibility(GONE);
        txt_confirm.setVisibility(GONE);
    }
    public void setData(String name,String address){
        this.name = name;
        this.address = address;
        handleChangeData();
    }
    public void setNeedConfirm(boolean needConfirm){
        if(needConfirm){
            img_line.setVisibility(VISIBLE);
            txt_confirm.setVisibility(VISIBLE);
        }else{
            img_line.setVisibility(GONE);
            txt_confirm.setVisibility(GONE);
        }
    }

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
