package annualreminder.view.clip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.client.proj.kusida.R;
import annualreminder.model.AnnualRecode;
import annualreminder.model.ManagerAnnual;
import annualreminder.view.adapter.AdapterRecodeList;
import com.kulala.staticsview.RelativeLayoutBase;

import java.util.List;
/**
 * Created by Administrator on 2017/2/23.
 * 主页选车，上半部,有记录
 */

public class ClipAnnualReminder_Recode extends RelativeLayoutBase {
    private ListView list_recodes;
    public ClipAnnualReminder_Recode(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_annual_reminder_recode, this, true);
        list_recodes = (ListView) findViewById(R.id.list_recodes);
        initViews();
        initEvents();
    }
    @Override
    protected void initViews() {
        List<AnnualRecode> list =  ManagerAnnual.getInstance().getCurrentRecodeList();
        if(list == null)return;
        AdapterRecodeList adapter = new AdapterRecodeList(getContext(),list);
        list_recodes.setAdapter(adapter);
    }
    @Override
    protected void initEvents() {

    }
    @Override
    protected void invalidateUI() {

    }
}
