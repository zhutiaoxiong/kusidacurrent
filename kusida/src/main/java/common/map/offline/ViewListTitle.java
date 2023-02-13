package common.map.offline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

/**
 * 下载的城市item
 */
public class ViewListTitle extends LinearLayout {
    public TextView txt_title;
    public ViewListTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.listview_header_title, this, true);
        txt_title = (TextView) findViewById(R.id.txt_title);
    }
    public void setTitle(String title) {
        txt_title.setText(title);
    }
}
