package annualreminder.view.clip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import annualreminder.model.ManagerAnnual;
import com.kulala.staticsview.image.smart.SmartImageView;
/**
 * Created by Administrator on 2017/2/23.
 */

public class ClipAnnualReminder_select_car_item extends LinearLayout {
    private SmartImageView img_logo;
    private ImageView      img_arrow;
    private TextView       txt_name;
    public ClipAnnualReminder_select_car_item(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_annual_reminder_select_car_item, this, true);
        img_logo = (SmartImageView) findViewById(R.id.img_logo);
        img_arrow = (ImageView) findViewById(R.id.img_arrow);
        txt_name = (TextView) findViewById(R.id.txt_name);
    }
    public void setData(PopChooseCarWarp.DataCarChoose dataCar){
        if(dataCar == null)return;
        ManagerAnnual.currentCarId =dataCar.carId;
        txt_name.setText(dataCar.carName);
        img_logo.setImageUrl(dataCar.carLogo);
    }
}
