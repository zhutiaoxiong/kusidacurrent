package view.view4control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;

import common.GlobalContext;
import model.skin.DataCarSkin;
import view.basicview.CheckForgroundUtils;


/**
 * 车身
 */
public class ViewCarPartThreeItem extends LinearLayout  {
    public TextView txt_bottom;
    public ImageView iv_top;
    public ViewCarPartThreeItem(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.part_three_item, this, true);
        iv_top = (ImageView) findViewById(R.id.iv_top);
        txt_bottom = (TextView) findViewById(R.id.txt_bottom);
        iv_top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ODispatcher.dispatchEvent(OEventName.SHOW_CAR_BODY);
            }
        });
    }

}
