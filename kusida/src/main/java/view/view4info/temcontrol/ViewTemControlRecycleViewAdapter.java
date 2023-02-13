package view.view4info.temcontrol;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import model.remotecontrol.BeanRemoteForRecycleView;

public class ViewTemControlRecycleViewAdapter extends RecyclerView.Adapter<ViewTemControlRecycleViewAdapter.ViewHolder> {
    private List<BeanRemoteForRecycleView> data;
    public OnItemClickListener itemClickListener;
    private Context context;

    public ViewTemControlRecycleViewAdapter(List<BeanRemoteForRecycleView> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public  void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void centerTxtClick(View view, int position);
        void rightTxtClick(View view, int position);
    }

    public void setData(List<BeanRemoteForRecycleView> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nfc, parent, false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.img_bianji.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null){
                    int position=viewHolder.getAdapterPosition();
                    itemClickListener.centerTxtClick(v,position);
                }
            }
        });
        viewHolder.txt_right.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null){
                    int position=viewHolder.getAdapterPosition();
                    itemClickListener.rightTxtClick(v,position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if(data==null)return;
        if(data.get(position)==null)return;
        if(data.get(position).status.equals("0")){
            holder.txt_right.setImageResource(R.drawable.img_tem_learn);
            holder.txt_left.setImageResource(R.drawable.img_tem_no);
            holder.txt_center.setTextColor(Color.parseColor("#686868"));
            holder.img_bianji.setVisibility(View.INVISIBLE);
        }else{
            holder.txt_left.setImageResource(R.drawable.img_tem_yes);
            holder.txt_right.setImageResource(R.drawable.img_tem_delete);
            holder.txt_center.setTextColor(Color.parseColor("#FFFFFF"));
            holder.img_bianji.setVisibility(View.INVISIBLE);
//            holder.txt_right.setText("移除");
//            holder.txt_center.setVisibility(View.VISIBLE);
        }
        holder.txt_center.setText(data.get(position).name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView txt_left;
        public ImageView txt_right;
        public TextView txt_center;
        public ImageView img_bianji;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_left= itemView.findViewById(R.id.txt_left);
            txt_right= itemView.findViewById(R.id.txt_right);
            txt_center= itemView.findViewById(R.id.txt_center);
            img_bianji= itemView.findViewById(R.id.img_bianji);
        }
    }
}
