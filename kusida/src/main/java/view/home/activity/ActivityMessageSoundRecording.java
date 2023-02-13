package view.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import adapter.RecycleViewDivider;
import model.locator.SoundRecordingBean;
import view.home.adapter.SoundRecordingAdapter;
import view.home.commonview.MessageTitleFourItem;

public class ActivityMessageSoundRecording extends AllActivity {
    private MessageTitleFourItem top_item;
    private RecyclerView recyclerView;
    private LinearLayout bottom_layout;
    private LinearLayout check_layout;
    private TextView confirm;
    private ImageView check_iv;
    private TextView txt_manual_recording;
    private TextView txt_automatic_recording;
    private SoundRecordingAdapter adapter;
    private RecycleViewDivider driver;
    private static final String TAG = "ActivityMessage";
    private int p = -1;//用来记录上一个点击条目的下标
    private List<SoundRecordingBean> list;
    private boolean isAllSelect;
    private int diologSelect=-1;//弹出选择几分钟 -1未选择



    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_recording);
        top_item=findViewById(R.id.top_item);
        recyclerView = findViewById(R.id.recycleview);
        bottom_layout = findViewById(R.id.bottom_layout);
        check_layout = findViewById(R.id.check_layout);
        confirm = findViewById(R.id.confirm);
        check_iv = findViewById(R.id.check_iv);
        txt_manual_recording = findViewById(R.id.txt_manual_recording);
        txt_automatic_recording = findViewById(R.id.txt_automatic_recording);
        initView();
        initEvent();
    }
    private void initView() {
        long time = System.currentTimeMillis();
        list = new ArrayList<>();
        SoundRecordingBean bean = new SoundRecordingBean(0.01f, 0, time);
        SoundRecordingBean bean1 = new SoundRecordingBean(0.6f, 1, time);
        SoundRecordingBean bean2 = new SoundRecordingBean(0.7f, 0, time);
        SoundRecordingBean bean3 = new SoundRecordingBean(1f, 1, time);
        bean3.isPlay=true;
        list.add(bean);
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        adapter = new SoundRecordingAdapter(R.layout.loc_sound_recording_item, list,ActivityMessageSoundRecording.this);
        adapter.addChildClickViewIds(R.id.select_status,R.id.img_sound_play,R.id.img_sound_play_gif);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        driver = new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this, 5), Color.parseColor("#ececec"));
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(driver);
        }
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        top_item.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick delete");
                bottom_layout.setVisibility(View.VISIBLE);setListCheckAllVisible();
            }
        });
        top_item.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick select");
                Intent intent=new Intent(ActivityMessageSoundRecording.this, ActivityRecordTimeSelect.class);
                startActivity(intent);
            }
        });
        top_item.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick left ");
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick confirm ");
                deleteListChangeUI();
            }
        });
        txt_manual_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick txt_manual_recording ");
                setDialog(0);
            }
        });
        txt_automatic_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick txt_automatic_recording ");
                setDialog(1);
            }
        });
        check_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick check_layout ");
                isAllSelect = !isAllSelect;
                if (isAllSelect) {
                    check_iv.setImageResource(R.drawable.locator_icon_list_item_check_true);
                    setListAllCheck();
                } else {
                    check_iv.setImageResource(R.drawable.message_checkbox);
                    setListAllCheckFalse();
                }
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e(TAG, "onClick item " + position);
                if(list!=null&&list.size()>0){
                    list.get(position).isPlay=! list.get(position).isPlay;
                    setAllPlayZapCurrentClickPosition(position);
                }
            }
        });
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.select_status) {
                    Log.e(TAG, "onClick 点击选择框 ");
                    if(!isAllSelect){
                        if (p != -1 && p != position) {
                            list.get(p).setSelect(false);
                            list.get(position).setSelect(true);
                        } else {
                            list.get(position).setSelect(true);
                        }
                        //2.超出一页，滚动至点击条目
                    }else{
                        list.get(position).setSelect(!list.get(position).isSelect);
                        //2.超出一页，滚动至点击条目
                    }
                    p = position;
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(position);
                }
            }
        });
    }

    private void setDialog(int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMessageSoundRecording.this,R.style.dialog);
        View view = View.inflate(ActivityMessageSoundRecording.this, R.layout.luyin_diolog, null);
        Button cancle=  view.findViewById(R.id.cancle);
        Button sure=  view.findViewById(R.id.sure);
        ImageView select_one=  view.findViewById(R.id.select_one);
        ImageView select_two=  view.findViewById(R.id.select_two);
        ImageView select_three=  view.findViewById(R.id.select_three);
        ImageView select_four=  view.findViewById(R.id.select_four);
        ImageView select_five=  view.findViewById(R.id.select_five);
        ImageView select_six=  view.findViewById(R.id.select_six);
        TextView mode= view.findViewById(R.id.mode);
        if(type==0){
            mode.setText("手动录音");
        }else{
            mode.setText("自动录音");
        }
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d( "onClick cancle" );
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d( "onClick sure" );
                dialog.dismiss();
            }
        });
        select_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d( "onClick select_one" );
                diologSelect =1;
                setLuyinTimeSelectUI(select_one,select_two,select_three,select_four,select_five,select_six,1);
            }
        });
        select_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d( "onClick select_two" );
                diologSelect =2;
                setLuyinTimeSelectUI(select_one,select_two,select_three,select_four,select_five,select_six,2);
            }
        });
        select_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d( "onClick select_three" );
                diologSelect =3;
                setLuyinTimeSelectUI(select_one,select_two,select_three,select_four,select_five,select_six,3);
            }
        });
        select_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d( "onClick select_four" );
                diologSelect =4;
                setLuyinTimeSelectUI(select_one,select_two,select_three,select_four,select_five,select_six,4);
            }
        });
        select_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d( "onClick select_five" );
                diologSelect =5;
                setLuyinTimeSelectUI(select_one,select_two,select_three,select_four,select_five,select_six,5);
            }
        });
        select_six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d( "onClick select_six" );
                diologSelect =6;
                setLuyinTimeSelectUI(select_one,select_two,select_three,select_four,select_five,select_six,6);
            }
        });
        dialog.show();
    }
    private void setLuyinTimeSelectUI(ImageView iv1, ImageView iv2,ImageView iv3,ImageView iv4,ImageView iv5,ImageView iv6,int position ){
        iv1.setImageResource(R.drawable.locator_icon_list_item_check_false);
        iv2.setImageResource(R.drawable.locator_icon_list_item_check_false);
        iv3.setImageResource(R.drawable.locator_icon_list_item_check_false);
        iv4.setImageResource(R.drawable.locator_icon_list_item_check_false);
        iv5.setImageResource(R.drawable.locator_icon_list_item_check_false);
        iv6.setImageResource(R.drawable.locator_icon_list_item_check_false);
        switch (position){
            case 1:
                iv1.setImageResource(R.drawable.locator_icon_list_item_check_true);
                break;
            case 2:
                iv2.setImageResource(R.drawable.locator_icon_list_item_check_true);
                break;
            case 3:
                iv3.setImageResource(R.drawable.locator_icon_list_item_check_true);
                break;
            case 4:
                iv4.setImageResource(R.drawable.locator_icon_list_item_check_true);
                break;
            case 5:
                iv5.setImageResource(R.drawable.locator_icon_list_item_check_true);
                break;
            case 6:
                iv6.setImageResource(R.drawable.locator_icon_list_item_check_true);
                break;
        }

    }

    private void setAllPlayZapCurrentClickPosition(int position){
        for (int i = 0; i <list.size() ; i++) {
            if(i!=position){
                list.get(i).isPlay=false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setListAllCheck() {
        if(list!=null&&list.size()>0)
            for (SoundRecordingBean bean : list
            ) {
                if(bean!=null){
                    bean.isSelect=true;
                }
            }
        adapter.notifyDataSetChanged();
    }
    private void setListAllCheckFalse() {
        if(list!=null&&list.size()>0)
            for (SoundRecordingBean bean : list
            ) {
                if(bean!=null){
                    bean.isSelect=false;
                }
            }
        adapter.notifyDataSetChanged();
    }
    private void setListCheckAllVisible() {
        if(list!=null&&list.size()>0)
            for (SoundRecordingBean bean : list
            ) {
                if(bean!=null){
                    bean.isShow=true;
                }
            }
        adapter.notifyDataSetChanged();
    }
    private void deleteListChangeUI(){
        if(list!=null&&list.size()>0){
            for(int i=0; i<list.size(); i++){
                if(list.get(i)!=null&&list.get(i).isSelect){
                    list.remove(list.get(i));
                    i--;
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
