package adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/**
 * @author Administrator
 *
 */
public class BasicListView extends ListView {

	public BasicListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public BasicListView(Context context) { 
        super(context); 
    } 
    public BasicListView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 
    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
}
