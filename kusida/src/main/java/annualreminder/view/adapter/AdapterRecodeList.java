package annualreminder.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.client.proj.kusida.R;
import annualreminder.model.AnnualRecode;
import annualreminder.view.ActivityAnnual_RecodeAdd;
import com.kulala.staticsfunc.static_system.ODateTime;

import java.util.List;

/**
 * Created by qq522414074 on 2016/11/3.
 */
public class AdapterRecodeList extends BaseAdapter {
    private Context            context;
    private List<AnnualRecode> list;

    public AdapterRecodeList(Context context, List<AnnualRecode> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.clip_annual_reminder_recode_item, null);
            holder.txt_annual_day = (TextView) convertView.findViewById(R.id.txt_annual_day);
            holder.txt_annual_date = (TextView) convertView.findViewById(R.id.txt_annual_date);
            holder.txt_annual_fee = (TextView) convertView.findViewById(R.id.txt_annual_fee);
            holder.txt_annual_note = (TextView) convertView.findViewById(R.id.txt_annual_note);
            holder.txt_repair = (TextView) convertView.findViewById(R.id.txt_repair);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设下拉箭头显示
        if (list != null && list.get(position)!=null){
            AnnualRecode recode = list.get(position);
            holder.txt_annual_day.setText(ODateTime.time2OnlyYear(recode.inspectionTime));
            holder.txt_annual_date.setText("年检日期: "+ODateTime.time2StringOnlyDate(recode.inspectionTime));
            holder.txt_annual_fee.setText("年检费用: "+recode.fee+"元");
            holder.txt_annual_note.setText("备　　注: "+recode.comment);
            holder.txt_repair.setTag(recode);
            holder.txt_repair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnnualRecode recode = (AnnualRecode)v.getTag();
//                    ViewAnnualRecode_Add.repairRecode = recode;
//                    ODispatcher.dispatchEvent(OEventName.VIEW_ANNUAL_REMINDER_GOTOVIEW,R.layout.view_annual_recode_add);
                    ActivityAnnual_RecodeAdd.repairRecode = recode;
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), ActivityAnnual_RecodeAdd.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
        return convertView;
    }

    private class Holder {
        TextView  txt_annual_day,txt_repair,txt_annual_date,txt_annual_fee,txt_annual_note;
    }
}
