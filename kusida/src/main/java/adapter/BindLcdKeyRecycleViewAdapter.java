package adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.OnClickListenerMy;

public class BindLcdKeyRecycleViewAdapter extends RecyclerView.Adapter<BindLcdKeyRecycleViewAdapter.ViewHolder> {
    private String[] data;
    public OnItemClickListener itemClickListener;
    private Context context;

    public BindLcdKeyRecycleViewAdapter(String[] data, Context context) {
        this.data = data;
        this.context = context;
    }

    public  void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onMACnumberSelect(View view, int position);
    }

    public void setData(String[] data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lcd_numbers, parent, false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.txt_lcd_number.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null){
                    int position=viewHolder.getAdapterPosition();
                    itemClickListener.onMACnumberSelect(v,position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if(data==null)return;
        if(data[position]==null)return;
        holder.txt_lcd_number.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_lcd_number;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_lcd_number= itemView.findViewById(R.id.txt_lcd_number);
        }
    }
}
