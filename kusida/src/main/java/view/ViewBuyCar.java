package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterForBigPic;
import adapter.PicTureArray;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/10/13.
 */
public class ViewBuyCar extends LinearLayoutBase {

    private ListView listView;
    private AdapterForBigPic adapter;
    private List<PicTureArray> list;
    private ClipTitleMeSet titleHead;
    public ViewBuyCar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.buy_car,this,true);
        listView=(ListView) findViewById(R.id.list_view);
        titleHead=(ClipTitleMeSet) findViewById(R.id.title_head);
        int[] pics=new int[]{R.drawable.buycarone,R.drawable.buycartwo,R.drawable.buycarthree,R.drawable.buycarfour};
        list=new ArrayList<>();

        for(int i=0;i<pics.length;i++){
            PicTureArray array=new PicTureArray();
            array.picid=pics[i];
            list.add(array);
        }
        adapter=new AdapterForBigPic(list,ViewBuyCar.this.getContext());
        listView.setAdapter(adapter);
        initViews();
        initEvents();
    }


    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {
        titleHead.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.activity_kulala_main);
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }
}
