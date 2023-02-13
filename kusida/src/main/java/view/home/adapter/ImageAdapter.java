package view.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.client.proj.kusida.R;

import java.util.List;


public class ImageAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    private Context mContext;
    public ImageAdapter(List<Integer> list, Context context) {
        super(R.layout.item_image,list);
        mContext=context;
    }


    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = super.onCreateDefViewHolder(parent, viewType);
        return baseViewHolder;
    }

    //看这里 回调出正确的position
    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnItemClick(holder.itemView, position);
            }
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        Glide.with(mContext)
                .load(item)
                .into((ImageView) helper.getView(R.id.img));
    }


}
